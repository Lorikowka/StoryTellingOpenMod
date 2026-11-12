package com.storyteam.storytelling.system._core.structio.structure.v3;

import com.storyteam.storytelling.system._core.structio.structure.StructureST;
import lombok.Generated;
import net.minecraft.server.level.ServerLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface IAttachmentProcessor {
   void gather(ServerLevel var1, StructureST var2, StrAttachment var3);

   void prePlace(ServerLevel var1, StructureST var2, StrAttachment var3);

   void place(ServerLevel var1, StructureST var2, StrAttachment var3);

   public static class SafeWrapper implements IAttachmentProcessor {
      @Generated
      private static final Logger log = LoggerFactory.getLogger(IAttachmentProcessor.SafeWrapper.class);
      protected final String name;
      protected final IAttachmentProcessor wrapped;
      protected int errors;
      protected boolean enabled = true;

      public SafeWrapper(String name, IAttachmentProcessor wrapped) {
         this.name = name;
         this.wrapped = wrapped;
      }

      @Override
      public void gather(ServerLevel level, StructureST structure, StrAttachment attachment) {
         if (this.enabled) {
            try {
               this.wrapped.gather(level, structure, attachment);
            } catch (Throwable var5) {
               this.reportError(var5);
            }
         }
      }

      @Override
      public void prePlace(ServerLevel level, StructureST structure, StrAttachment attachment) {
         if (this.enabled) {
            try {
               this.wrapped.prePlace(level, structure, attachment);
            } catch (Throwable var5) {
               this.reportError(var5);
            }
         }
      }

      @Override
      public void place(ServerLevel level, StructureST structure, StrAttachment attachment) {
         if (this.enabled) {
            try {
               this.wrapped.place(level, structure, attachment);
            } catch (Throwable var5) {
               this.reportError(var5);
            }
         }
      }

      protected void reportError(Throwable error) {
         this.errors++;
         log.error("IAttachmentProcessor {} reported a problem ({}/10)", new Object[]{this.name, this.errors, error});
         if (this.errors >= 10) {
            this.enabled = false;
         }
      }
   }
}
