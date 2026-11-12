package com.storyteam.storytelling.api.entity;

public interface IEpicFightHUDEntity {
   default boolean showEpicFightHealthBar() {
      return false;
   }

   default boolean showEpicFightTargetIndicator() {
      return false;
   }
}
