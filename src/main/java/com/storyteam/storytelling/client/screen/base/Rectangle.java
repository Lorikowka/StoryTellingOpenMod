package com.storyteam.storytelling.client.screen.base;

import lombok.Generated;

public class Rectangle {
   private int left;
   private int top;
   private int right;
   private int bottom;

   public static Rectangle create(int left, int top, int width, int height) {
      return new Rectangle(left, top, left + width, top + height);
   }

   public int getWidth() {
      return this.right - this.left;
   }

   public int getHeight() {
      return this.bottom - this.top;
   }

   public boolean contains(int x, int y) {
      return x >= this.left && y >= this.top && x <= this.right && y <= this.bottom;
   }

   @Generated
   public int getLeft() {
      return this.left;
   }

   @Generated
   public int getTop() {
      return this.top;
   }

   @Generated
   public int getRight() {
      return this.right;
   }

   @Generated
   public int getBottom() {
      return this.bottom;
   }

   @Generated
   public void setLeft(int left) {
      this.left = left;
   }

   @Generated
   public void setTop(int top) {
      this.top = top;
   }

   @Generated
   public void setRight(int right) {
      this.right = right;
   }

   @Generated
   public void setBottom(int bottom) {
      this.bottom = bottom;
   }

   @Generated
   @Override
   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof Rectangle other)) {
         return false;
      } else if (!other.canEqual(this)) {
         return false;
      } else if (this.getLeft() != other.getLeft()) {
         return false;
      } else if (this.getTop() != other.getTop()) {
         return false;
      } else {
         return this.getRight() != other.getRight() ? false : this.getBottom() == other.getBottom();
      }
   }

   @Generated
   protected boolean canEqual(Object other) {
      return other instanceof Rectangle;
   }

   @Generated
   @Override
   public int hashCode() {
      int PRIME = 59;
      int result = 1;
      result = result * 59 + this.getLeft();
      result = result * 59 + this.getTop();
      result = result * 59 + this.getRight();
      return result * 59 + this.getBottom();
   }

   @Generated
   @Override
   public String toString() {
      return "Rectangle(left=" + this.getLeft() + ", top=" + this.getTop() + ", right=" + this.getRight() + ", bottom=" + this.getBottom() + ")";
   }

   @Generated
   public Rectangle(int left, int top, int right, int bottom) {
      this.left = left;
      this.top = top;
      this.right = right;
      this.bottom = bottom;
   }
}
