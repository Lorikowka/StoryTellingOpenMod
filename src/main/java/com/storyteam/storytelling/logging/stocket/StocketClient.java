package com.storyteam.storytelling.logging.stocket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Map;
import java.util.Map.Entry;

public class StocketClient {
   protected final String ip;
   protected final int port;
   protected final int timeout;

   public StocketClient(String ip, int port, int timeout) {
      this.ip = ip;
      this.port = port;
      this.timeout = timeout;
   }

   protected Socket connect() throws IOException {
      Socket s = new Socket();
      s.connect(new InetSocketAddress(this.ip, this.port), this.timeout);
      return s;
   }

   public StocketResponse send(StocketRequest req) {
      try {
         StocketResponse var12;
         try (Socket connection = this.connect()) {
            DataInputStream input = new DataInputStream(connection.getInputStream());
            DataOutputStream output = new DataOutputStream(connection.getOutputStream());
            output.writeInt(-1091585346);
            output.writeUTF(req.getMethod());
            Map<String, String> data = req.getData();
            output.writeShort(data.size());

            for (Entry<String, String> e : data.entrySet()) {
               output.writeUTF(e.getKey());
               output.writeUTF(e.getValue());
            }

            output.flush();
            StocketResponse response = new StocketResponse();
            response.read(input);
            var12 = response.setHasConnected();
         }

         return var12;
      } catch (IOException var10) {
         return new StocketResponse();
      }
   }
}
