package com.storyteam.storytelling.system._core.cloud;

import org.zeith.hammerlib.util.java.net.HttpRequest;
import org.zeith.hammerlib.util.shaded.json.JSONObject;

public record RemoteFileInfo(String sha1, String name, String uri, long lastModified) {
   public RemoteFileInfo(JSONObject json) {
      this(json.getString("sha1"), json.getString("name"), json.getString("uri"), json.getLong("lastModified"));
   }

   public HttpRequest get() {
      return CloudIO.request(this.uri, "GET");
   }
}
