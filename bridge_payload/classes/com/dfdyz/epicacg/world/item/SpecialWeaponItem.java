package com.dfdyz.epicacg.world.item;

import java.util.function.Consumer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import yesman.epicfight.world.item.WeaponItem;

public class SpecialWeaponItem extends WeaponItem {
   protected final int EnchantmentValue = 15;

   public SpecialWeaponItem(Item.Properties builder, Tier tier, int EnchantmentValue) {
      super(tier, EnchantmentValue, 0.0F, builder);
   }

   public int m_6473_() {
      return this.EnchantmentValue;
   }

   public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
      return 0;
   }

   public boolean m_41465_() {
      return false;
   }
}
