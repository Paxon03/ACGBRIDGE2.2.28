package com.dfdyz.epicacg.compat;

import com.yesman.epicskills.client.gui.screen.CategorySlotTexture;

public enum EpicSkillsCategorySlotTextures implements CategorySlotTexture {
   SAO_SINGLE_SWORD(3, 3, 38, 38);

   private final int offsetX;
   private final int offsetY;
   private final int texWidth;
   private final int texHeight;
   private final int universalOrder;

   private EpicSkillsCategorySlotTextures(int var3, int var4, int var5, int var6) {
      this.offsetX = var3;
      this.offsetY = var4;
      this.texWidth = var5;
      this.texHeight = var6;
      this.universalOrder = CategorySlotTexture.ENUM_MANAGER.assign(this);
   }

   public int offsetX() {
      return this.offsetX;
   }

   public int offsetY() {
      return this.offsetY;
   }

   public int texWidth() {
      return this.texWidth;
   }

   public int texHeight() {
      return this.texHeight;
   }

   public int universalOrdinal() {
      return this.universalOrder;
   }

   // $FF: synthetic method
   private static EpicSkillsCategorySlotTextures[] $values() {
      return new EpicSkillsCategorySlotTextures[]{SAO_SINGLE_SWORD};
   }
}
