package com.storyteam.storytelling.system._core.cloud;

import com.storyteam.storytelling.api.Since;
import com.storyteam.storytelling.system._core.structio.repo.StructureRepository;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeith.hammerlib.util.java.Hashers;
import org.zeith.hammerlib.util.java.net.HttpRequest;
import org.zeith.hammerlib.util.java.net.HttpRequest.HttpRequestException;
import org.zeith.hammerlib.util.java.tuples.Tuple2;
import org.zeith.hammerlib.util.java.tuples.Tuples;
import org.zeith.hammerlib.util.shaded.json.JSONArray;
import org.zeith.hammerlib.util.shaded.json.JSONObject;
import org.zeith.hammerlib.util.shaded.json.JSONTokener;

public class CloudIO {
   public static final Logger LOG = LoggerFactory.getLogger("Storytelling/CloudIO");
   private static final List<RepositoryWorker> WORKERS = new ArrayList<>();
   public static final String BRANCH = "st-" + Since.Season.CURRENT.getSt();
   public static final String AUTHORIZATION = "d9f40b22-40f7-4d3a-8521-29cf44bc2c20";
   public static final ExecutorService REPOSITORY_EXECUTOR = Executors.newWorkStealingPool();
   private static long lastRefresh = 0L;
   public static final String USER_AGENT = "Storytelling Mod";
   private static final String ROOT_URI = "https://st.zeith.org/";
   private static final String BASE_URI = "https://st.zeith.org/data/";
   public static final RepositoryWorker CLOUD_IMAGES = register(new RepositoryWorker("https://st.zeith.org/images", "OnlineImages"));
   static Map<String, RemoteFileInfo> REMOTE_VARS = Map.of();
   static CompletableFuture<?> currentRefresh = CompletableFuture.completedFuture(null);

   public static CompletableFuture<?> createReload() {
      return CompletableFuture.completedFuture(null);
   }

   public static RepositoryWorker register(RepositoryWorker worker) {
      WORKERS.add(worker);
      return worker;
   }

   private static void ensureLoaded() {
      
   }

   public static RemoteFileInfo get(String name) {
      ensureLoaded();
      return REMOTE_VARS.get(name);
   }

   public static Set<String> getRemoteVarNames() {
      ensureLoaded();
      return REMOTE_VARS.keySet();
   }

   public static Collection<RemoteFileInfo> getCloudVars() {
      ensureLoaded();
      return REMOTE_VARS.values();
   }

   public static CompletableFuture<?> forceRefresh() {
      return CompletableFuture.completedFuture(null);
   }

   public static CompletableFuture<?> refresh() {
      return CompletableFuture.completedFuture(null);
   }

   public static CompletableFuture<?> uploadVarInt(String name, Consumer<String> log, int data) {
      return CompletableFuture.failedFuture(new IllegalStateException("Storytelling offline build: remote service disabled; use local files and /script commands."));
   }

   public static CompletableFuture<Integer> downloadVarInt(String name, Consumer<String> log) {
      return CompletableFuture.failedFuture(new IllegalStateException("Storytelling offline build: remote service disabled; use local files and /script commands."));
   }

   public static CompletableFuture<?> uploadVarNbt(String name, Consumer<String> log, CompoundTag data) {
      return CompletableFuture.failedFuture(new IllegalStateException("Storytelling offline build: remote service disabled; use local files and /script commands."));
   }

   public static CompletableFuture<CompoundTag> downloadVarNbt(String name, Consumer<String> log) {
      return CompletableFuture.failedFuture(new IllegalStateException("Storytelling offline build: remote service disabled; use local files and /script commands."));
   }

   public static CompletableFuture<?> uploadVarStr(String name, Consumer<String> log, String data) {
      return CompletableFuture.failedFuture(new IllegalStateException("Storytelling offline build: remote service disabled; use local files and /script commands."));
   }

   public static CompletableFuture<String> downloadVarStr(String name, Consumer<String> log) {
      return CompletableFuture.failedFuture(new IllegalStateException("Storytelling offline build: remote service disabled; use local files and /script commands."));
   }

   public static CompletableFuture<?> uploadVar(String name, Consumer<String> log, byte[] data) {
      return CompletableFuture.failedFuture(new IllegalStateException("Storytelling offline build: remote service disabled; use local files and /script commands."));
   }

   public static CompletableFuture<byte[]> downloadVar(String name, Consumer<String> log) {
      return CompletableFuture.failedFuture(new IllegalStateException("Storytelling offline build: remote service disabled; use local files and /script commands."));
   }

   public static CompletableFuture<Tuple2<Integer, String>> deleteVar(String name) {
      return CompletableFuture.failedFuture(new IllegalStateException("Storytelling offline build: remote service disabled; use local files and /script commands."));
   }

   public static boolean shaCodesMatch(String a, String b) {
      if (a.equalsIgnoreCase(b)) {
         return true;
      } else {
         while (a.startsWith("0")) {
            a = a.substring(1);
         }

         while (b.startsWith("0")) {
            b = b.substring(1);
         }

         return a.equalsIgnoreCase(b);
      }
   }

   static HttpRequest request(String url, String method) {
      throw new IllegalStateException("Storytelling offline build: remote service disabled; use local files and /script commands.");
   }

   private static enum CIOEndpoint {
      LIST_STRUCTURES("GET", "repo"),
      UPLOAD_STRUCTURE("POST", "repo"),
      DELETE_STRUCTURE("DELETE", "repo");

      final String method;
      final String path;

      private CIOEndpoint(String method, String path) {
         this.method = method;
         this.path = path;
      }

      private HttpRequest request() {
         return this.request(Map.of());
      }

      private HttpRequest request(Map<String, String> params) {
         String furl = "https://st.zeith.org/data/" + this.path;
         if (!params.isEmpty()) {
            furl = furl
               + "?"
               + params.entrySet()
                  .stream()
                  .map(e -> URLEncoder.encode(e.getKey(), StandardCharsets.UTF_8) + "=" + URLEncoder.encode(e.getValue(), StandardCharsets.UTF_8))
                  .collect(Collectors.joining("&"));
         }

         return CloudIO.request(furl, this.method);
      }
   }
}
