package com.dfdyz.epicacg.registry;

import com.dfdyz.epicacg.efmextra.skills.EpicACGSkillSlot;
import com.dfdyz.epicacg.efmextra.weapon.GenShinBow;
import com.dfdyz.epicacg.efmextra.weapon.WeaponCollider;
import com.dfdyz.epicacg.utils.OjangUtils;
import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Function;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TieredItem;
import org.slf4j.Logger;
import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.api.animation.LivingMotions;
import yesman.epicfight.api.forgeevent.WeaponCapabilityPresetRegistryEvent;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.gameasset.ColliderPreset;
import yesman.epicfight.gameasset.EpicFightSounds;
import yesman.epicfight.world.capabilities.entitypatch.HumanoidMobPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.capabilities.item.WeaponCapability;
import yesman.epicfight.world.capabilities.item.WeaponCategory;
import yesman.epicfight.world.capabilities.item.CapabilityItem.Styles;
import yesman.epicfight.world.capabilities.item.CapabilityItem.WeaponCategories;
import yesman.epicfight.world.entity.ai.attribute.EpicFightAttributes;

public class WeaponTypes {
   public static final Function<Item, CapabilityItem.Builder> GENSHIN_BOW = (item) -> {
      WeaponCapability.Builder builder = (WeaponCapability.Builder)WeaponCapability.builder().category(WeaponCategories.BOW).styleProvider((playerpatch) -> Styles.ONE_HAND).collider(WeaponCollider.GenShin_Bow_scan).swingSound((SoundEvent)EpicFightSounds.WHOOSH_BIG.get()).hitSound((SoundEvent)EpicFightSounds.BLADE_HIT.get()).canBePlacedOffhand(false).passiveSkill(MySkills.GS_Bow_Internal).newStyleCombo(Styles.ONE_HAND, new AnimationManager.AnimationAccessor[]{MyAnimations.GS_Yoimiya_Auto1, MyAnimations.GS_Yoimiya_Auto2, MyAnimations.GS_Yoimiya_Auto3, MyAnimations.GS_Yoimiya_Auto4, MyAnimations.GS_Yoimiya_Auto5, MyAnimations.GS_Yoimiya_Auto2, null}).innateSkill(Styles.ONE_HAND, (itemStack) -> MySkills.GS_YOIMIYA_SPECIALATK).livingMotionModifier(Styles.ONE_HAND, LivingMotions.IDLE, Animations.BIPED_IDLE).livingMotionModifier(Styles.ONE_HAND, LivingMotions.WALK, Animations.BIPED_WALK).livingMotionModifier(Styles.ONE_HAND, LivingMotions.RUN, Animations.BIPED_RUN).livingMotionModifier(Styles.ONE_HAND, LivingMotions.JUMP, Animations.BIPED_JUMP).livingMotionModifier(Styles.ONE_HAND, LivingMotions.KNEEL, Animations.BIPED_KNEEL).livingMotionModifier(Styles.ONE_HAND, LivingMotions.SNEAK, Animations.BIPED_SNEAK).livingMotionModifier(Styles.ONE_HAND, LivingMotions.SWIM, Animations.BIPED_SWIM).livingMotionModifier(Styles.ONE_HAND, LivingMotions.AIM, Animations.BIPED_BOW_AIM).livingMotionModifier(Styles.ONE_HAND, LivingMotions.BLOCK, Animations.BIPED_BLOCK).constructor(GenShinBow::new);
      return builder;
   };
   public static final Function<Item, CapabilityItem.Builder> GENSHIN_SPEAR = (item) -> {
      WeaponCapability.Builder builder = WeaponCapability.builder().category(WeaponCategories.SPEAR).styleProvider((playerpatch) -> Styles.TWO_HAND).collider(ColliderPreset.LONGSWORD).hitSound((SoundEvent)EpicFightSounds.BLADE_HIT.get()).newStyleCombo(Styles.TWO_HAND, new AnimationManager.AnimationAccessor[]{MyAnimations.GS_LAODENG_AUTO1, MyAnimations.GS_LAODENG_AUTO2, MyAnimations.GS_LAODENG_AUTO3, MyAnimations.GS_LAODENG_AUTO4, MyAnimations.GS_LAODENG_AUTO5, MyAnimations.GS_LAODENG_AUTO6, Animations.SPEAR_DASH, Animations.SPEAR_TWOHAND_AIR_SLASH}).innateSkill(Styles.TWO_HAND, (itemStack) -> MySkills.BATTLE_SCYTHE_SA).livingMotionModifier(Styles.TWO_HAND, LivingMotions.BLOCK, Animations.SPEAR_GUARD).livingMotionModifier(Styles.TWO_HAND, LivingMotions.IDLE, MyAnimations.SAO_SCYTHE_IDLE).livingMotionModifier(Styles.TWO_HAND, LivingMotions.WALK, MyAnimations.SAO_SCYTHE_WALK).livingMotionModifier(Styles.TWO_HAND, LivingMotions.RUN, MyAnimations.SAO_SCYTHE_RUN).weaponCombinationPredicator((entitypatch) -> false);
      return builder;
   };
   public static final Function<Item, CapabilityItem.Builder> BATTLE_SCYTHE = (item) -> {
      WeaponCapability.Builder builder = WeaponCapability.builder().category(WeaponCategories.SPEAR).styleProvider((playerpatch) -> Styles.TWO_HAND).collider(ColliderPreset.LONGSWORD).hitSound((SoundEvent)EpicFightSounds.BLADE_HIT.get()).newStyleCombo(Styles.TWO_HAND, new AnimationManager.AnimationAccessor[]{MyAnimations.BATTLE_SCYTHE_AUTO1, MyAnimations.BATTLE_SCYTHE_AUTO2, MyAnimations.BATTLE_SCYTHE_AUTO3, MyAnimations.BATTLE_SCYTHE_AUTO4, MyAnimations.BATTLE_SCYTHE_AUTO5, MyAnimations.BATTLE_SCYTHE_DASH, Animations.SPEAR_TWOHAND_AIR_SLASH}).innateSkill(Styles.TWO_HAND, (itemStack) -> MySkills.BATTLE_SCYTHE_SA).livingMotionModifier(Styles.TWO_HAND, LivingMotions.BLOCK, Animations.SPEAR_GUARD).livingMotionModifier(Styles.TWO_HAND, LivingMotions.IDLE, MyAnimations.SAO_SCYTHE_IDLE).livingMotionModifier(Styles.TWO_HAND, LivingMotions.WALK, MyAnimations.SAO_SCYTHE_WALK).livingMotionModifier(Styles.TWO_HAND, LivingMotions.RUN, MyAnimations.SAO_SCYTHE_RUN).weaponCombinationPredicator((entitypatch) -> false);
      return builder;
   };
   public static final Function<Item, CapabilityItem.Builder> SAO_SINGLE_SWORD = (item) -> {
      WeaponCapability.Builder builder = WeaponCapability.builder().category(WeaponCategories.SWORD).styleProvider((playerpatch) -> {
         if (playerpatch instanceof PlayerPatch) {
            if (playerpatch.getHoldingItemCapability(InteractionHand.OFF_HAND).getWeaponCategory() == WeaponCategories.SWORD && ((PlayerPatch)playerpatch).getSkill(EpicACGSkillSlot.SAO_SINGLE_SWORD).getSkill() != null && ((PlayerPatch)playerpatch).getSkill(EpicACGSkillSlot.SAO_SINGLE_SWORD).getSkill().getRegistryName().m_135815_().equals("sao_dual_sword_skill")) {
               return com.dfdyz.epicacg.registry.WeaponTypes.EpicACGStyles.SAO_DUAL_SWORD;
            }

            if (((PlayerPatch)playerpatch).getSkill(EpicACGSkillSlot.SAO_SINGLE_SWORD).getSkill() != null && ((PlayerPatch)playerpatch).getSkill(EpicACGSkillSlot.SAO_SINGLE_SWORD).getSkill().getRegistryName().m_135815_().equals("sao_rapier_skill")) {
               return com.dfdyz.epicacg.registry.WeaponTypes.EpicACGStyles.SAO_RAPIER;
            }
         } else if (playerpatch instanceof HumanoidMobPatch) {
            Set<String> tags = ((LivingEntity)playerpatch.getOriginal()).m_19880_();
            Iterator var2 = tags.iterator();
            if (var2.hasNext()) {
               String tag = (String)var2.next();
               String[] arg = tag.split(":");
               if (arg.length > 2 && arg[0] == "epicacg" && arg[1] == "sao_single_sword") {
                  switch (arg[3]) {
                     case "dual_sword" -> {
                        return com.dfdyz.epicacg.registry.WeaponTypes.EpicACGStyles.SAO_DUAL_SWORD;
                     }
                     case "rapier" -> {
                        return com.dfdyz.epicacg.registry.WeaponTypes.EpicACGStyles.SAO_RAPIER;
                     }
                     default -> {
                        return com.dfdyz.epicacg.registry.WeaponTypes.EpicACGStyles.SAO_SINGLE_SWORD;
                     }
                  }
               }

               return Styles.ONE_HAND;
            }

            return com.dfdyz.epicacg.registry.WeaponTypes.EpicACGStyles.SAO_SINGLE_SWORD;
         }

         return com.dfdyz.epicacg.registry.WeaponTypes.EpicACGStyles.SAO_SINGLE_SWORD;
      }).passiveSkill(MySkills.SAO_SINGLESWORD_INTERNAL).collider(WeaponCollider.SAO_SWORD).hitSound((SoundEvent)EpicFightSounds.BLADE_HIT.get()).newStyleCombo(com.dfdyz.epicacg.registry.WeaponTypes.EpicACGStyles.SAO_SINGLE_SWORD, new AnimationManager.AnimationAccessor[]{Animations.SWORD_AUTO1, Animations.SWORD_AUTO2, Animations.SWORD_AUTO3, Animations.SWORD_DASH, Animations.SWORD_DASH, Animations.SWORD_AIR_SLASH}).newStyleCombo(com.dfdyz.epicacg.registry.WeaponTypes.EpicACGStyles.SAO_RAPIER, new AnimationManager.AnimationAccessor[]{MyAnimations.SAO_RAPIER_AUTO1, MyAnimations.SAO_RAPIER_AUTO2, MyAnimations.SAO_RAPIER_AUTO3, MyAnimations.SAO_RAPIER_AUTO4, MyAnimations.SAO_RAPIER_AUTO5, MyAnimations.SAO_RAPIER_DASH, MyAnimations.SAO_RAPIER_DASH, MyAnimations.SAO_RAPIER_AIR}).newStyleCombo(com.dfdyz.epicacg.registry.WeaponTypes.EpicACGStyles.SAO_DUAL_SWORD, new AnimationManager.AnimationAccessor[]{MyAnimations.SAO_DUAL_SWORD_AUTO1, MyAnimations.SAO_DUAL_SWORD_AUTO2, MyAnimations.SAO_DUAL_SWORD_AUTO3, MyAnimations.SAO_DUAL_SWORD_AUTO4, MyAnimations.SAO_DUAL_SWORD_AUTO5, MyAnimations.SAO_DUAL_SWORD_AUTO6, MyAnimations.SAO_DUAL_SWORD_AUTO7, MyAnimations.SAO_DUAL_SWORD_AUTO8, MyAnimations.SAO_DUAL_SWORD_AUTO9, MyAnimations.SAO_DUAL_SWORD_AUTO10, MyAnimations.SAO_DUAL_SWORD_AUTO11, MyAnimations.SAO_DUAL_SWORD_AUTO12, MyAnimations.SAO_DUAL_SWORD_DASH, Animations.SPEAR_DASH, Animations.GREATSWORD_AIR_SLASH}).innateSkill(com.dfdyz.epicacg.registry.WeaponTypes.EpicACGStyles.SAO_SINGLE_SWORD, (itemstack) -> MySkills.SAO_SINGLESWORD_SA).innateSkill(com.dfdyz.epicacg.registry.WeaponTypes.EpicACGStyles.SAO_DUAL_SWORD, (itemstack) -> MySkills.SAO_DUAL_SWORD_SA).innateSkill(com.dfdyz.epicacg.registry.WeaponTypes.EpicACGStyles.SAO_RAPIER, (itemstack) -> MySkills.WEAPON_SKILL_RAPIER).livingMotionModifier(com.dfdyz.epicacg.registry.WeaponTypes.EpicACGStyles.SAO_SINGLE_SWORD, LivingMotions.IDLE, Animations.BIPED_IDLE).livingMotionModifier(com.dfdyz.epicacg.registry.WeaponTypes.EpicACGStyles.SAO_SINGLE_SWORD, LivingMotions.BLOCK, Animations.SWORD_GUARD).livingMotionModifier(com.dfdyz.epicacg.registry.WeaponTypes.EpicACGStyles.SAO_RAPIER, LivingMotions.BLOCK, Animations.SWORD_GUARD).livingMotionModifier(com.dfdyz.epicacg.registry.WeaponTypes.EpicACGStyles.SAO_RAPIER, LivingMotions.IDLE, MyAnimations.SAO_RAPIER_IDLE).livingMotionModifier(com.dfdyz.epicacg.registry.WeaponTypes.EpicACGStyles.SAO_RAPIER, LivingMotions.WALK, MyAnimations.SAO_RAPIER_WALK).livingMotionModifier(com.dfdyz.epicacg.registry.WeaponTypes.EpicACGStyles.SAO_RAPIER, LivingMotions.RUN, MyAnimations.SAO_RAPIER_RUN).livingMotionModifier(com.dfdyz.epicacg.registry.WeaponTypes.EpicACGStyles.SAO_RAPIER, LivingMotions.KNEEL, MyAnimations.SAO_RAPIER_IDLE).livingMotionModifier(com.dfdyz.epicacg.registry.WeaponTypes.EpicACGStyles.SAO_DUAL_SWORD, LivingMotions.IDLE, MyAnimations.SAO_DUAL_SWORD_HOLD).livingMotionModifier(com.dfdyz.epicacg.registry.WeaponTypes.EpicACGStyles.SAO_DUAL_SWORD, LivingMotions.WALK, MyAnimations.SAO_DUAL_SWORD_WALK).livingMotionModifier(com.dfdyz.epicacg.registry.WeaponTypes.EpicACGStyles.SAO_DUAL_SWORD, LivingMotions.CHASE, MyAnimations.SAO_DUAL_SWORD_HOLD).livingMotionModifier(com.dfdyz.epicacg.registry.WeaponTypes.EpicACGStyles.SAO_DUAL_SWORD, LivingMotions.RUN, MyAnimations.SAO_DUAL_SWORD_RUN).livingMotionModifier(com.dfdyz.epicacg.registry.WeaponTypes.EpicACGStyles.SAO_DUAL_SWORD, LivingMotions.JUMP, MyAnimations.SAO_DUAL_SWORD_NORMAL).livingMotionModifier(com.dfdyz.epicacg.registry.WeaponTypes.EpicACGStyles.SAO_DUAL_SWORD, LivingMotions.KNEEL, MyAnimations.SAO_RAPIER_IDLE).livingMotionModifier(com.dfdyz.epicacg.registry.WeaponTypes.EpicACGStyles.SAO_DUAL_SWORD, LivingMotions.SWIM, MyAnimations.SAO_DUAL_SWORD_NORMAL).livingMotionModifier(com.dfdyz.epicacg.registry.WeaponTypes.EpicACGStyles.SAO_DUAL_SWORD, LivingMotions.BLOCK, Animations.SWORD_GUARD).weaponCombinationPredicator((entitypatch) -> {
         boolean tag = false;
         if (entitypatch.getHoldingItemCapability(InteractionHand.OFF_HAND).getWeaponCategory() == WeaponCategories.SWORD && entitypatch instanceof PlayerPatch && ((PlayerPatch)entitypatch).getSkill(EpicACGSkillSlot.SAO_SINGLE_SWORD).getSkill() != null && ((PlayerPatch)entitypatch).getSkill(EpicACGSkillSlot.SAO_SINGLE_SWORD).getSkill().getRegistryName().m_135815_().equals("sao_dual_sword_skill")) {
            tag = true;
         }

         return tag;
      });
      if (item instanceof TieredItem) {
         int harvestLevel = ((TieredItem)item).m_43314_().m_6604_();
         builder.addStyleAttibutes(Styles.COMMON, Pair.of((Attribute)EpicFightAttributes.IMPACT.get(), EpicFightAttributes.getImpactModifier((double)0.5F + 0.2 * (double)harvestLevel)));
         builder.addStyleAttibutes(Styles.COMMON, Pair.of((Attribute)EpicFightAttributes.MAX_STRIKES.get(), EpicFightAttributes.getMaxStrikesModifier(1)));
      }

      return builder;
   };

   public static void register(WeaponCapabilityPresetRegistryEvent event) {
      Logger LOGGER = LogUtils.getLogger();
      LOGGER.info("Loading WeaponCapability");
      event.getTypeEntry().put(OjangUtils.newRL("epicacg", "sao_single_sword"), SAO_SINGLE_SWORD);
      event.getTypeEntry().put(OjangUtils.newRL("epicacg", "genshin_bow"), GENSHIN_BOW);
      event.getTypeEntry().put(OjangUtils.newRL("epicacg", "battle_scythe"), BATTLE_SCYTHE);
      event.getTypeEntry().put(OjangUtils.newRL("epicacg", "gs_spear"), GENSHIN_SPEAR);
      LOGGER.info("WeaponCapability Loaded");
   }

   public static enum EpicACGWeaponCategories implements WeaponCategory {
      SCYTHE,
      SINGLE_SWORD,
      LONG_SWORD,
      GS_SPEAR;

      final int id;

      private EpicACGWeaponCategories() {
         this.id = WeaponCategory.ENUM_MANAGER.assign(this);
      }

      public int universalOrdinal() {
         return this.id;
      }

      // $FF: synthetic method
      private static EpicACGWeaponCategories[] $values() {
         return new EpicACGWeaponCategories[]{SCYTHE, SINGLE_SWORD, LONG_SWORD, GS_SPEAR};
      }
   }
}
