package com.dfdyz.epicacg.efmextra.skills;

import yesman.epicfight.skill.SkillCategories;
import yesman.epicfight.skill.SkillCategory;
import yesman.epicfight.skill.SkillSlot;

public enum EpicACGSkillSlot implements SkillSlot {
   SKILL_SELECTOR(EpicACGSkillCategories.MutiSpecialAttack),
   SAO_SINGLE_SWORD(EpicACGSkillCategories.SAO_SINGLE_SWORD),
   EPICACG_AIR_ATTACK(SkillCategories.BASIC_ATTACK);

   SkillCategory category;
   int id;

   private EpicACGSkillSlot(SkillCategory var3) {
      this.category = var3;
      this.id = SkillSlot.ENUM_MANAGER.assign(this);
   }

   public SkillCategory category() {
      return this.category;
   }

   public int universalOrdinal() {
      return this.id;
   }

   // $FF: synthetic method
   private static EpicACGSkillSlot[] $values() {
      return new EpicACGSkillSlot[]{SKILL_SELECTOR, SAO_SINGLE_SWORD, EPICACG_AIR_ATTACK};
   }
}
