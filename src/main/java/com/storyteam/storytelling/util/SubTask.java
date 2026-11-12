package com.storyteam.storytelling.util;

import lombok.Generated;

public class SubTask {
   public String name;
   public boolean done;

   public SubTask(String name, boolean done) {
      this.name = name;
      this.done = done;
   }

   @Override
   public String toString() {
      return ToStringBuilder.builder(this).append("name", this.name).append("done", this.done).build();
   }

   public void complete() {
      this.setDone(true);
   }

   public void undo() {
      this.setDone(false);
   }

   @Generated
   public String getName() {
      return this.name;
   }

   @Generated
   public boolean isDone() {
      return this.done;
   }

   @Generated
   public void setDone(boolean done) {
      this.done = done;
   }
}
