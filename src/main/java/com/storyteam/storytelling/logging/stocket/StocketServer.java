package com.storyteam.storytelling.logging.stocket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class StocketServer {
   protected final ServerSocket socket;
   protected final StocketProcessor processor = new StocketProcessor();
   protected ExecutorService executor;
   protected Future<?> future;
   protected UUID session;

   public StocketServer(int serverPort) throws IOException {
      this.socket = new ServerSocket(serverPort);
   }

   public StocketProcessor getProcessor() {
      return this.processor;
   }

   public void start() {
      if (this.future != null) {
         this.future.cancel(true);
         this.future = null;
      }

      UUID session = this.session = UUID.randomUUID();
      if (this.executor == null) {
         this.executor = Executors.newSingleThreadExecutor();
      }

      this.future = this.executor.submit(() -> this.receiver(session));
   }

   public void stop() {
      this.session = null;
      if (this.future != null) {
         this.future.cancel(true);
         this.future = null;
      }

      if (this.executor != null) {
         this.executor.shutdown();
         this.executor = null;
      }
   }

   private void receiver(UUID session) {
      while (Objects.equals(this.session, session)) {
         try (Socket connection = this.socket.accept()) {
            connection.setSoTimeout(1000);
            DataInputStream input = new DataInputStream(connection.getInputStream());
            DataOutputStream output = new DataOutputStream(connection.getOutputStream());
            StocketResponse resp = new StocketResponse();
            if (input.readInt() == -1091585346) {
               String method = input.readUTF();
               short parCount = input.readShort();
               Map<String, String> data = new HashMap<>();

               for (int i = 0; i < parCount; i++) {
                  String key = input.readUTF();
                  String val = input.readUTF();
                  data.put(key, val);
               }

               this.processor.handle(new StocketRequest(method, Collections.unmodifiableMap(data)), resp);
            }

            resp.write(output);
            output.flush();
         } catch (SocketTimeoutException var14) {
         } catch (Exception var15) {
            var15.printStackTrace();
         }
      }
   }
}
