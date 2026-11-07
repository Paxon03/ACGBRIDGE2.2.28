package com.dfdyz.epicacg.registry;

import com.dfdyz.epicacg.efmextra.skills.EpicACGSkillCategories;
import com.dfdyz.epicacg.efmextra.skills.MultiSpecialSkill;
import com.dfdyz.epicacg.efmextra.skills.SimpleWeaponSASkill;
import com.dfdyz.epicacg.efmextra.skills.TagSkill;
import com.dfdyz.epicacg.efmextra.skills.GenShinInternal.GSBasicAtkPatch;
import com.dfdyz.epicacg.efmextra.skills.GenShinInternal.GSBowInternal;
import com.dfdyz.epicacg.efmextra.skills.GenShinInternal.GSFallAttack;
import com.dfdyz.epicacg.efmextra.skills.GenShinInternal.GSSpecialAttack;
import com.dfdyz.epicacg.efmextra.skills.SAO.DualBladeSkill;
import com.dfdyz.epicacg.efmextra.skills.SAO.RapierSkill;
import com.dfdyz.epicacg.efmextra.skills.SAO.RapierSpicialAttackSkill;
import com.dfdyz.epicacg.efmextra.skills.SAO.SAOBasicAtkPatch;
import com.dfdyz.epicacg.efmextra.skills.SAO.SAOSingleSwordInternal;
import com.dfdyz.epicacg.efmextra.skills.SAO.SingleSwordSASkills;
import com.dfdyz.epicacg.utils.OjangUtils;
import com.mojang.logging.LogUtils;
import net.minecraft.world.item.CreativeModeTab;
import org.slf4j.Logger;
import yesman.epicfight.api.forgeevent.SkillBuildEvent;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.skill.BasicAttack;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillCategories;
import yesman.epicfight.skill.Skill.ActivateType;
import yesman.epicfight.skill.Skill.Resource;
import yesman.epicfight.skill.passive.PassiveSkill;
import yesman.epicfight.skill.weaponinnate.SimpleWeaponInnateSkill;

public class MySkills {
   public static Skill MUTI_SPECIAL_ATTACK;
   public static Skill GS_Bow_FallAttackPatch;
   public static Skill GS_Bow_BasicAttackPatch;
   public static Skill GS_Bow_Internal;
   public static Skill SAO_SINGLESWORD_INTERNAL;
   public static Skill SAO_BASICATK_PATCH;
   public static Skill SAO_SINGLESWORD_SA;
   public static Skill BATTLE_SCYTHE_SA;
   public static Skill SAO_DUALSWORD;
   public static Skill SAO_SINGLESWORD;
   public static Skill SAO_RAPIER_A;
   public static Skill WEAPON_SKILL_RAPIER;
   public static Skill SAO_DUAL_SWORD_SA;
   public static Skill GS_YOIMIYA_SPECIALATK;

   public static void BuildSkills(SkillBuildEvent var0) {
      Logger var1 = LogUtils.getLogger();
      var1.info("Build EpicACG Skill");
      SkillBuildEvent.ModRegistryWorker var2 = var0.createRegistryWorker("epicacg");
      MUTI_SPECIAL_ATTACK = var2.build("muti_sa", MultiSpecialSkill::new, PassiveSkill.createPassiveBuilder().setRegistryName(OjangUtils.newRL("epicacg", "muti_sa")).setCategory(EpicACGSkillCategories.MutiSpecialAttack));
      GS_YOIMIYA_SPECIALATK = ((SimpleWeaponInnateSkill)var2.build("gs_yoimiya_sa", GSSpecialAttack::new, (SimpleWeaponInnateSkill.Builder)SimpleWeaponInnateSkill.createSimpleWeaponInnateBuilder().setAnimations(MyAnimations.GS_Yoimiya_SA).setResource(Resource.WEAPON_CHARGE).setRegistryName(OjangUtils.newRL("epicacg", "gs_yoimiya_sa")))).newProperty();
      GS_Bow_FallAttackPatch = var2.build("gs_air_attack_patch", GSFallAttack::new, GSFallAttack.createBuilder().setCategory(SkillCategories.BASIC_ATTACK).setRegistryName(OjangUtils.newRL("epicacg", "gs_air_attack_patch")).setActivateType(ActivateType.ONE_SHOT).setResource(Resource.STAMINA));
      GS_Bow_BasicAttackPatch = var2.build("gs_basic_attack_patch", GSBasicAtkPatch::new, GSBasicAtkPatch.createBuilder().setCategory(SkillCategories.BASIC_ATTACK).setRegistryName(OjangUtils.newRL("epicacg", "gs_basic_attack_patch")).setActivateType(ActivateType.ONE_SHOT).setResource(Resource.NONE));
      GS_Bow_Internal = var2.build("gs_bow_internal", GSBowInternal::new, GSBowInternal.GetBuilder("gs_bow_internal").setCategory(SkillCategories.WEAPON_PASSIVE).setResource(Resource.NONE));
      SAO_BASICATK_PATCH = var2.build("sao_basic_attack_patch", SAOBasicAtkPatch::new, BasicAttack.createBasicAttackBuilder().setRegistryName(OjangUtils.newRL("epicacg", "sao_basic_attack_patch")));
      SAO_SINGLESWORD_INTERNAL = var2.build("sao_single_sword_internal", SAOSingleSwordInternal::new, SAOSingleSwordInternal.createBuilder().setCategory(SkillCategories.WEAPON_PASSIVE).setRegistryName(OjangUtils.newRL("epicacg", "sao_single_sword_internal")).setResource(Resource.NONE));
      SAO_SINGLESWORD_SA = ((SimpleWeaponInnateSkill)var2.build("single_sword_sa", SingleSwordSASkills::new, (SimpleWeaponInnateSkill.Builder)SingleSwordSASkills.createBuilder(OjangUtils.newRL("epicacg", "single_sword_sa")).setAnimations(Animations.SWORD_AUTO1).setCategory(SkillCategories.WEAPON_INNATE))).newProperty();
      BATTLE_SCYTHE_SA = ((SimpleWeaponInnateSkill)var2.build("battle_scythe_sa", SimpleWeaponSASkill::new, (SimpleWeaponInnateSkill.Builder)SimpleWeaponInnateSkill.createSimpleWeaponInnateBuilder().setAnimations(MyAnimations.BATTLE_SCYTHE_SA1).setResource(Resource.WEAPON_CHARGE).setRegistryName(OjangUtils.newRL("epicacg", "battle_scythe_sa")))).newProperty();
      SAO_SINGLESWORD = var2.build("sao_single_sword", TagSkill::new, TagSkill.createBuilder(OjangUtils.newRL("epicacg", "sao_single_sword"), EpicACGSkillCategories.SAO_SINGLE_SWORD).setCreativeTab((CreativeModeTab)Tab.TAB_ITEMS.get()));
      SAO_DUALSWORD = var2.build("sao_dual_sword_skill", DualBladeSkill::new, DualBladeSkill.createBuilder(OjangUtils.newRL("epicacg", "sao_dual_sword_skill")).setCreativeTab((CreativeModeTab)Tab.TAB_ITEMS.get()));
      SAO_RAPIER_A = var2.build("sao_rapier_skill", RapierSkill::new, RapierSkill.createBuilder(OjangUtils.newRL("epicacg", "sao_rapier_skill")).setCreativeTab((CreativeModeTab)Tab.TAB_ITEMS.get()));
      WEAPON_SKILL_RAPIER = var2.build("weapon_skill_rapier", RapierSpicialAttackSkill::new, (RapierSpicialAttackSkill._Builder)RapierSpicialAttackSkill.create_Builder(OjangUtils.newRL("epicacg", "weapon_skill_rapier")).setAnimations(MyAnimations.SAO_RAPIER_SPECIAL_DASH).setResource(Resource.WEAPON_CHARGE).setCategory(SkillCategories.WEAPON_INNATE));
      SAO_DUAL_SWORD_SA = ((SimpleWeaponInnateSkill)var2.build("sao_dual_sword_sa", SimpleWeaponSASkill::new, (SimpleWeaponInnateSkill.Builder)SimpleWeaponInnateSkill.createSimpleWeaponInnateBuilder().setAnimations(MyAnimations.SAO_DUAL_SWORD_SA1).setResource(Resource.WEAPON_CHARGE).setRegistryName(OjangUtils.newRL("epicacg", "sao_dual_sword_sa")))).newProperty();
   }
}
