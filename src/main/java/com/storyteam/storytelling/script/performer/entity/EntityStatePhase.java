package com.storyteam.storytelling.script.performer.entity;

public enum EntityStatePhase {
   MARKED_FOR_DELETE,
   QUEUED_FOR_DELETE,
   ALIVE,
   WAITING;

   public boolean isAlive() {
      return this == ALIVE;
   }

   public boolean shouldNotDead() {
      return this != MARKED_FOR_DELETE && this != QUEUED_FOR_DELETE;
   }
}
