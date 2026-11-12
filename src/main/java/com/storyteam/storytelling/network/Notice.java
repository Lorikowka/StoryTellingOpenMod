package com.storyteam.storytelling.network;

import java.time.Instant;

public class Notice {
   private String id;
   private Integer script;
   private String text;
   private String noteType;
   private Instant time;

   public Notice(String id, Integer script, String text, String noteType, Instant time) {
      this.id = id;
      this.script = script;
      this.text = text;
      this.noteType = noteType;
      this.time = time;
   }

   public String getId() {
      return this.id;
   }

   public Integer getScript() {
      return this.script;
   }

   public String getText() {
      return this.text;
   }

   public String getNoteType() {
      return this.noteType;
   }

   public Instant getTime() {
      return this.time;
   }
}
