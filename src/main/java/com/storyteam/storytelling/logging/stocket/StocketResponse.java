package com.storyteam.storytelling.logging.stocket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class StocketResponse {
   protected boolean hasConnected;
   protected String reply = "";
   protected Map<String, String> data = new HashMap<>();

   public void setReply(String reply) {
      this.reply = reply;
   }

   public void addData(String key, String data) {
      this.data.put(key, data);
   }

   public String getReply() {
      return this.reply;
   }

   public Map<String, String> getData() {
      return this.data;
   }

   public StocketResponse setHasConnected() {
      this.hasConnected = true;
      return this;
   }

   public boolean didConnect() {
      return this.hasConnected;
   }

   public void read(DataInputStream input) throws IOException {
      this.reply = input.readUTF();
      short parCount = input.readShort();
      this.data.clear();

      for (int i = 0; i < parCount; i++) {
         String key = input.readUTF();
         String val = input.readUTF();
         this.data.put(key, val);
      }
   }

   public void write(DataOutputStream output) throws IOException {
      output.writeUTF(this.reply);
      output.writeShort(this.data.size());

      for (Entry<String, String> e : this.data.entrySet()) {
         output.writeUTF(e.getKey());
         output.writeUTF(e.getValue());
      }
   }

   @Override
   public String toString() {
      return "StocketResponse{reply='" + this.reply + "', data=" + this.data + "}";
   }
}
