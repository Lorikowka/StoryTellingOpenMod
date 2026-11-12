package com.storyteam.storytelling.system._core.cloud;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
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

public class RepositoryWorker implements SuggestionProvider<CommandSourceStack> {
   public final Logger LOG;
   private long lastRefresh = 0L;
   private final String endpointUrl;
   Map<String, RemoteFileInfo> REMOTE_VARS = Map.of();
   CompletableFuture<?> currentRefresh = CompletableFuture.completedFuture(null);

   public RepositoryWorker(String endpointUrl, String endpointName) {
      this.endpointUrl = endpointUrl;
      this.LOG = LoggerFactory.getLogger("Storytelling/CloudIO_RepositoryWorker/" + endpointName);
   }

   private void ensureLoaded() {
      if (this.lastRefresh == 0L) {
         this.refresh();
      }

      this.currentRefresh.join();
   }

   public RemoteFileInfo get(String name) {
      this.ensureLoaded();
      return this.REMOTE_VARS.get(name);
   }

   public Set<String> getRemoteKeys() {
      this.ensureLoaded();
      return this.REMOTE_VARS.keySet();
   }

   public Collection<RemoteFileInfo> getRemoteFiles() {
      this.ensureLoaded();
      return this.REMOTE_VARS.values();
   }

   public CompletableFuture<?> forceRefresh() {
      return CompletableFuture.completedFuture(null);
   }

   public CompletableFuture<?> refresh() {
      return CompletableFuture.completedFuture(null);
   }

   public CompletableFuture<RemoteFileInfo> upload(String name, Consumer<String> log, byte[] data) {
      return CompletableFuture.failedFuture(new IllegalStateException("Storytelling offline build: remote service disabled; use local files and /script commands."));
   }

   public CompletableFuture<byte[]> download(String name, Consumer<String> log) {
      return CompletableFuture.failedFuture(new IllegalStateException("Storytelling offline build: remote service disabled; use local files and /script commands."));
   }

   public CompletableFuture<Tuple2<Integer, String>> delete(String name) {
      return CompletableFuture.failedFuture(new IllegalStateException("Storytelling offline build: remote service disabled; use local files and /script commands."));
   }

   private HttpRequest request(String method, Map<String, String> params) {
      String furl = this.endpointUrl;
      if (!params.isEmpty()) {
         furl = furl
            + "?"
            + params.entrySet()
               .stream()
               .map(e -> URLEncoder.encode(e.getKey(), StandardCharsets.UTF_8) + "=" + URLEncoder.encode(e.getValue(), StandardCharsets.UTF_8))
               .collect(Collectors.joining("&"));
      }

      return CloudIO.request(furl, method);
   }

   public RequiredArgumentBuilder<CommandSourceStack, String> commandArgument(String name) {
      return Commands.argument(name, StringArgumentType.string()).suggests(this);
   }

   public RemoteFileInfo fromCommand(CommandContext<CommandSourceStack> cs, String name) {
      return this.get(StringArgumentType.getString(cs, name));
   }

   public CompletableFuture<Suggestions> getSuggestions(CommandContext<CommandSourceStack> ctx, SuggestionsBuilder b) {
      this.ensureLoaded();

      for (Entry<String, RemoteFileInfo> e : this.REMOTE_VARS.entrySet()) {
         b.suggest(e.getKey(), Component.literal(e.getValue().sha1()).setStyle(Style.EMPTY.withColor(ChatFormatting.DARK_GRAY)));
      }

      return b.buildFuture();
   }
}
