package com.dfdyz.epicacg.world.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tier;
import yesman.epicfight.world.item.WeaponItem;

public class SimpleWeaponItem extends WeaponItem {
   protected final int EnchantmentValue = 15;

   public SimpleWeaponItem(Item.Properties builder, Tier tier, int EnchantmentValue) {
      super(tier, EnchantmentValue, 0.0F, builder);
   }

   public int m_6473_() {
      return this.EnchantmentValue;
   }
}
