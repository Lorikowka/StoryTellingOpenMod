package com.storyteam.storytelling.network;

import com.storyteam.storytelling.Storytelling;
import com.storyteam.storytelling.network.packets.ErrorPacket;
import com.storyteam.storytelling.network.packets.RequestPacket;
import com.storyteam.storytelling.network.packets.ResponsePacket;
import java.io.File;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.net.http.WebSocket.Listener;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import net.minecraft.Util;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;
import org.zeith.hammerlib.util.java.Cast;

public class StoryServer {
   private static final HttpClient HTTP = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10L)).build();
   private static final boolean NO = true; // Offline: do not authenticate or connect to StoryServer.
   private static final int MAX_RETRY_ATTEMPTS = 3;
   private static final long RETRY_DELAY_MS = 1000L;
   private static final String DEFAULT_WS_URL = "wss://i.story-team.com/api/ws/storytelling";
   private static StoryServer instance;
   private final String wsUrl;
   private final Object lock = new Object();
   private final AtomicReference<WebSocket> webSocket = new AtomicReference<>();
   private final AtomicBoolean connected = new AtomicBoolean(false);
   private final Map<UUID, CompletableFuture<? extends ResponsePacket>> pendingRequests = new ConcurrentHashMap<>();
   private final BlockingQueue<OutboundPacket> packetQueue = new PriorityBlockingQueue<>(32, Comparator.comparingInt(OutboundPacket::priority));
   private final ExecutorService packetProcessor = Executors.newSingleThreadExecutor(r -> {
      Thread t = new Thread(r, "StoryServer-PacketProcessor");
      t.setDaemon(true);
      return t;
   });
   private String authKey;
   private volatile CompletableFuture<Void> connectionFuture;
   private StoryServer.WebSocketListener wsListener;
   private Logger logger = Logger.getLogger("StoryServer");
   private StoryNetworkResponder packetHandler = new StoryNetworkResponder();

   private StoryServer(String key) {
      // Offline mode: no browser authorization, network worker, or log reconfiguration.
      this.authKey = "";
      this.wsUrl = "";
   }

   private static void disableStdoutLogging() {
      Logger rootLogger = Logger.getLogger("");
      Handler[] handlers = rootLogger.getHandlers();

      for (Handler handler : handlers) {
         if (handler instanceof ConsoleHandler) {
            rootLogger.removeHandler(handler);
         }
      }
   }

   public static StoryServer getInstance() {
      if (instance == null) {
         instance = new StoryServer(getKey());
      }

      return instance;
   }

   private void initializeConnection() {
      this.connect().exceptionally(e -> {
         this.logger.log(Level.SEVERE, "Ошибка при подключении к StoryServer", e);
         return null;
      });
   }

   private void startPacketProcessor() {
      this.packetProcessor.submit(this::processPackets);
   }

   private void processPackets() {
      while (!Thread.currentThread().isInterrupted()) {
         try {
            OutboundPacket packet = this.packetQueue.poll(100L, TimeUnit.MILLISECONDS);
            if (packet != null) {
               WebSocket ws = this.webSocket.get();
               if (ws != null) {
                  this.logger.info("> " + packet.content());
                  ws.sendText(packet.content(), true).exceptionally(ex -> null).get();
                  if (packet.future() != null) {
                     packet.future().complete(null);
                  }
               } else {
                  this.packetQueue.add(packet);
               }
            }
         } catch (InterruptedException var3) {
            Thread.currentThread().interrupt();
            break;
         } catch (Exception var4) {
            var4.printStackTrace();
         }
      }
   }

   public CompletableFuture<Void> connect() {
      if (NO) {
         return CompletableFuture.failedFuture(new IllegalStateException("StoryServer is disabled in offline mode"));
      } else {
         synchronized (this.lock) {
            if (this.connectionFuture != null) {
               return this.connectionFuture;
            } else {
               this.connectionFuture = new CompletableFuture<>();
               this.wsListener = new StoryServer.WebSocketListener();
               CompletableFuture.runAsync(
                  () -> {
                     int attempts = 0;

                     while (attempts < 3 && !this.connected.get()) {
                        try {
                           String clientName = Storytelling.PROXY.getIngameName();
                           HTTP.newWebSocketBuilder()
                              .buildAsync(
                                 URI.create(this.wsUrl + "?session=" + this.authKey + "&name=" + URLEncoder.encode(clientName, StandardCharsets.UTF_8)),
                                 this.wsListener
                              )
                              .thenAccept(ws -> {
                                 this.webSocket.set(ws);
                                 this.connected.set(true);
                              })
                              .get(10L, TimeUnit.SECONDS);
                           if (this.connectionFuture != null) {
                              this.connectionFuture.complete(null);
                           }
                           break;
                        } catch (Exception var5) {
                           if (++attempts >= 3) {
                              Storytelling.LOG.warn("Failed to connect to StoryServer: " + var5.getMessage());
                              if (this.connectionFuture != null) {
                                 this.connectionFuture.completeExceptionally(var5);
                              }
                              break;
                           }

                           try {
                              Thread.sleep(1000L);
                           } catch (InterruptedException var4) {
                              Thread.currentThread().interrupt();
                              break;
                           }
                        }
                     }
                  }
               );
               return this.connectionFuture;
            }
         }
      }
   }

   public void disconnect() {
      if (!NO) {
         WebSocket ws = this.webSocket.get();
         if (ws != null) {
            try {
               ws.sendClose(1000, "Disconnecting");
            } finally {
               this.connected.set(false);
               this.webSocket.set(null);
               if (this.connectionFuture != null) {
                  this.connectionFuture = null;
               }
            }
         }

         this.connected.set(false);
         this.webSocket.set(null);
         if (this.connectionFuture != null) {
            this.connectionFuture.complete(null);
         }

         this.connectionFuture = null;
      }
   }

   private WebSocket ensureConnection() {
      if (!this.connected.get()) {
         try {
            this.connect().get(5L, TimeUnit.SECONDS);
         } catch (Exception var2) {
            var2.printStackTrace();
            return null;
         }
      }

      return this.webSocket.get();
   }

   public int getPacketQueueSize() {
      return this.packetQueue.size();
   }

   public void sendPacket(@NotNull BasePacket packet) {
      this.sendPacket(packet, 0);
   }

   public OutboundPacket sendPacket(@NotNull BasePacket packet, int priority) {
      OutboundPacket outbound = new OutboundPacket(NetworkParser.serialize(packet), priority, null);
      // Offline mode must not accumulate an unconsumed queue.
      return outbound;
   }

   public <T extends ResponsePacket> CompletableFuture<T> sendPacketSync(RequestPacket<T> packet) {
      return CompletableFuture.failedFuture(new IllegalStateException("StoryServer is disabled in offline mode"));
   }

   public void sendPacketSync(BasePacket packet, int priority) {
      if (!NO) {
         CompletableFuture<Void> future = new CompletableFuture<>();
         OutboundPacket outbound = new OutboundPacket(NetworkParser.serialize(packet), priority, future);
         this.packetQueue.offer(outbound);

         try {
            future.get();
         } catch (Exception var6) {
            var6.printStackTrace();
         }
      }
   }

   private static void resetKey() {
      // Offline mode does not modify saved credentials.
   }

   private static String getKey() {
      return "";
   }

   private class WebSocketListener implements Listener {
      private final StringBuilder messageBuffer = new StringBuilder();
      private int bracketCount = 0;

      @Override
      public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
         webSocket.request(1L);
         if (StoryServer.this.webSocket.get() == null) {
            StoryServer.this.webSocket.set(webSocket);
         }

         for (int i = 0; i < data.length(); i++) {
            char c = data.charAt(i);
            if (c == '{') {
               this.bracketCount++;
            } else if (c == '}') {
               this.bracketCount--;
            }

            this.messageBuffer.append(c);
            if (this.bracketCount == 0 && this.messageBuffer.length() > 0) {
               this.handleMessage(this.messageBuffer.toString());
               this.messageBuffer.setLength(0);
            }
         }

         return CompletableFuture.completedStage(null);
      }

      private void handleMessage(String message) {
         StoryServer.this.logger.info("< " + message);
         BasePacket packet = NetworkParser.deserialize(message);
         if (packet != null) {
            if (packet instanceof ErrorPacket errorPacket) {
               this.handleErrorPacket(errorPacket);
            } else if (packet instanceof ResponsePacket responsePacket) {
               this.handleResponsePacket(responsePacket);
            }

            if (StoryServer.this.packetHandler != null) {
               Method[] methods = null;

               try {
                  methods = StoryServer.this.packetHandler.getClass().getMethods();
               } catch (NoClassDefFoundError var5) {
                  StoryServer.this.packetHandler = new StoryNetworkResponder();
                  methods = StoryServer.this.packetHandler.getClass().getMethods();
               }

               if (methods != null) {
                  Arrays.stream(methods)
                     .filter(method -> method.getParameterCount() == 1)
                     .filter(method -> method.getParameterTypes()[0].isAssignableFrom(packet.getClass()))
                     .forEach(
                        method -> {
                           try {
                              method.invoke(StoryServer.this.packetHandler, packet);
                           } catch (Exception var4x) {
                              StoryServer.this.logger
                                 .log(
                                    Level.SEVERE,
                                    "Ошибка обработки пакета " + packet.getClass().getSimpleName() + " методом " + method.getName(),
                                    (Throwable)var4x
                                 );
                           }
                        }
                     );
               }
            }
         }
      }

      private void handleErrorPacket(ErrorPacket errorPacket) {
         int errorCode = errorPacket.code;
         switch (errorCode) {
            case 1:
               StoryServer.this.logger.severe("Ошибка: Недействительный сессионный токен");
               synchronized (StoryServer.this.lock) {
                  StoryServer.this.disconnect();
                  StoryServer.resetKey();
                  StoryServer.this.authKey = StoryServer.getKey();
                  StoryServer.this.initializeConnection();
                  break;
               }
            case 2:
               StoryServer.this.logger.severe("Ошибка: Отсутствуют обязательные параметры");
               break;
            case 3:
               StoryServer.this.logger.severe("Ошибка: Сессия была перехвачена другим подключением");
               break;
            case 4:
               StoryServer.this.logger.severe("Ошибка: Не удалось разобрать входящее сообщение");
               break;
            default:
               StoryServer.this.logger.severe("Неизвестная ошибка с кодом: " + errorCode);
         }
      }

      private void handleResponsePacket(ResponsePacket responsePacket) {
         CompletableFuture<? extends ResponsePacket> future = StoryServer.this.pendingRequests.remove(responsePacket.getResponseId());
         if (future != null) {
            ((CompletableFuture<ResponsePacket>)(CompletableFuture<?>) future).complete(responsePacket);
         }
      }

      @Override
      public void onOpen(WebSocket webSocket) {
         StoryServer.this.connected.set(true);
         Listener.super.onOpen(webSocket);
      }

      @Override
      public CompletionStage<?> onClose(WebSocket webSocket, int statusCode, String reason) {
         StoryServer.this.connected.set(false);
         return Listener.super.onClose(webSocket, statusCode, reason);
      }

      @Override
      public void onError(WebSocket webSocket, Throwable error) {
         StoryServer.this.connected.set(false);
         error.printStackTrace();
         Listener.super.onError(webSocket, error);
      }
   }
}
