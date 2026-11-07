package com.dfdyz.epicacg.efmextra.weapon;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.UseAnim;
import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.api.animation.LivingMotion;
import yesman.epicfight.api.animation.LivingMotions;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.skill.guard.GuardSkill;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.capabilities.item.WeaponCapability;
import yesman.epicfight.world.capabilities.item.CapabilityItem.ZoomInType;

public class GenShinBow extends WeaponCapability {
   public GenShinBow(CapabilityItem.Builder var1) {
      super(var1);
   }

   public UseAnim getUseAnimation(LivingEntityPatch<?> var1) {
      return UseAnim.NONE;
   }

   public LivingMotion getLivingMotion(LivingEntityPatch<?> var1, InteractionHand var2) {
      LivingEntity var3 = (LivingEntity)var1.getOriginal();
      return var3.m_6117_() && var3.m_21211_().m_41780_() == UseAnim.BOW ? LivingMotions.AIM : null;
   }

   public CapabilityItem.ZoomInType getZoomInType() {
      return ZoomInType.USE_TICK;
   }

   public AnimationManager.AnimationAccessor<? extends StaticAnimation> getGuardMotion(GuardSkill var1, GuardSkill.BlockType var2, PlayerPatch<?> var3) {
      return null;
   }
}
