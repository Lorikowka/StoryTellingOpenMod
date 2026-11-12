package com.storyteam.storytelling.system._core.structio.repo;

import java.io.File;

public record DownloadResult(DwnResult result, File file) {
   public boolean wasDownloaded() {
      return this.result == DwnResult.DOWNLOADED;
   }
}
