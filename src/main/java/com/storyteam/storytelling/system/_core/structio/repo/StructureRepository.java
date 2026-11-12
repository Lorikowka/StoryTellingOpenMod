package com.storyteam.storytelling.system._core.structio.repo;

import com.storyteam.storytelling.Storytelling;
import com.storyteam.storytelling.system._core.cloud.CloudIO;
import com.storyteam.storytelling.system._core.cloud.RemoteFileInfo;
import com.storyteam.storytelling.system._core.structio.StructIO;
import com.storyteam.storytelling.system._core.structio.structure.StructureST;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import org.zeith.hammerlib.util.java.Hashers;
import org.zeith.hammerlib.util.java.net.HttpRequest;
import org.zeith.hammerlib.util.java.net.HttpRequest.HttpRequestException;
import org.zeith.hammerlib.util.java.tuples.Tuple2;
import org.zeith.hammerlib.util.java.tuples.Tuples;
import org.zeith.hammerlib.util.shaded.json.JSONArray;
import org.zeith.hammerlib.util.shaded.json.JSONObject;
import org.zeith.hammerlib.util.shaded.json.JSONTokener;

public class StructureRepository {
   private static long lastRefresh = 0L;
   private static final String BASE_URI = "https://st.zeith.org/structio/";
   static Map<String, RemoteFileInfo> REMOTE_STRUCTURES = Map.of();

   public static CompletableFuture<?> forceRefresh() {
      return CompletableFuture.completedFuture(null);
   }

   public static CompletableFuture<?> refresh() {
      return CompletableFuture.completedFuture(null);
   }

   public static RemoteFileInfo get(String name) {
      return REMOTE_STRUCTURES.get(name);
   }

   public static Set<String> getRemoteStructureNames() {
      return REMOTE_STRUCTURES.keySet();
   }

   public static Collection<RemoteFileInfo> getRemoteStructures() {
      return REMOTE_STRUCTURES.values();
   }

   public static CompletableFuture<?> uploadStructure(String name, Consumer<String> log) {
      return CompletableFuture.failedFuture(new IllegalStateException("Storytelling offline build: remote service disabled; use local files and /script commands."));
   }

   public static CompletableFuture<Map<String, DownloadResult>> downloadStructures(
      RegistryAccess access, List<String> names, boolean quickCheck, Consumer<String> log
   ) {
      return CompletableFuture.failedFuture(new IllegalStateException("Storytelling offline build: remote service disabled; use local files and /script commands."));
   }

   public static CompletableFuture<?> downloadStructure(RegistryAccess access, String name, Consumer<String> log) {
      return CompletableFuture.failedFuture(new IllegalStateException("Storytelling offline build: remote service disabled; use local files and /script commands."));
   }

   public static CompletableFuture<Tuple2<Integer, String>> deleteStructure(String name) {
      return CompletableFuture.failedFuture(new IllegalStateException("Storytelling offline build: remote service disabled; use local files and /script commands."));
   }

   static HttpRequest request(String url, String method) {
      throw new IllegalStateException("Storytelling offline build: remote service disabled; use local files and /script commands.");
   }

   private static enum STREndpoint {
      LIST_STRUCTURES("GET", "repo"),
      UPLOAD_STRUCTURE("POST", "repo"),
      DELETE_STRUCTURE("DELETE", "repo"),
      BULK_DOWNLOAD_STRUCTURES("POST", "bulk");

      final String method;
      final String path;

      private STREndpoint(String method, String path) {
         this.method = method;
         this.path = path;
      }

      private HttpRequest request() {
         return this.request(Map.of());
      }

      private HttpRequest request(Map<String, String> params) {
         String furl = "https://st.zeith.org/structio/" + this.path;
         if (!params.isEmpty()) {
            furl = furl
               + "?"
               + params.entrySet()
                  .stream()
                  .map(e -> URLEncoder.encode(e.getKey(), StandardCharsets.UTF_8) + "=" + URLEncoder.encode(e.getValue(), StandardCharsets.UTF_8))
                  .collect(Collectors.joining("&"));
         }

         return StructureRepository.request(furl, this.method);
      }
   }
}
