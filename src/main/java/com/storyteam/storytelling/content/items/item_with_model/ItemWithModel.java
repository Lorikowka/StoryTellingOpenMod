package com.storyteam.storytelling.content.items.item_with_model;

import java.util.function.Consumer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

public class ItemWithModel extends Item {
   protected ModelContainer model;

   public ItemWithModel(Properties pProperties, ModelContainer container) {
      super(pProperties);
      this.model = container;
   }

   public void initializeClient(Consumer<IClientItemExtensions> consumer) {
      super.initializeClient(consumer);
      consumer.accept(new IClientItemExtensions() {
         private ItemWithModelRenderer renderer;

         public BlockEntityWithoutLevelRenderer getCustomRenderer() {
            if (this.renderer == null) {
               this.renderer = new ItemWithModelRenderer(ItemWithModel.this.model);
            }

            return this.renderer;
         }
      });
   }
}
