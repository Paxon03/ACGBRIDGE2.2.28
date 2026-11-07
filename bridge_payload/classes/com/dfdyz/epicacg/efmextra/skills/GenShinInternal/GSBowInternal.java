package com.dfdyz.epicacg.efmextra.skills.GenShinInternal;

import com.dfdyz.epicacg.efmextra.skills.EpicACGSkillSlot;
import com.dfdyz.epicacg.registry.MySkills;
import com.dfdyz.epicacg.utils.OjangUtils;
import yesman.epicfight.gameasset.EpicFightSkills;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillBuilder;
import yesman.epicfight.skill.SkillCategories;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.SkillSlots;
import yesman.epicfight.world.capabilities.skill.CapabilitySkill;

public class GSBowInternal extends Skill {
   private static final EpicACGSkillSlot AIR_ATTACK_SLOT;

   public static SkillBuilder<GSBowInternal> GetBuilder(String var0) {
      return (new SkillBuilder()).setRegistryName(OjangUtils.newRL("epicacg", var0)).setCategory(SkillCategories.WEAPON_PASSIVE);
   }

   public GSBowInternal(SkillBuilder<? extends Skill> var1) {
      super(var1);
   }

   public void onInitiate(SkillContainer var1) {
      super.onInitiate(var1);
      CapabilitySkill var2 = var1.getExecutor().getSkillCapability();
      var2.skillContainers[AIR_ATTACK_SLOT.universalOrdinal()].setSkill(MySkills.GS_Bow_FallAttackPatch);
      var2.skillContainers[SkillSlots.BASIC_ATTACK.universalOrdinal()].setSkill(MySkills.GS_Bow_BasicAttackPatch);
   }

   public void onRemoved(SkillContainer var1) {
      super.onRemoved(var1);
      CapabilitySkill var2 = var1.getExecutor().getSkillCapability();
      var2.skillContainers[AIR_ATTACK_SLOT.universalOrdinal()].setSkill((Skill)null);
      var2.skillContainers[SkillSlots.BASIC_ATTACK.universalOrdinal()].setSkill(EpicFightSkills.BASIC_ATTACK);
   }

   static {
      AIR_ATTACK_SLOT = EpicACGSkillSlot.EPICACG_AIR_ATTACK;
   }
}
