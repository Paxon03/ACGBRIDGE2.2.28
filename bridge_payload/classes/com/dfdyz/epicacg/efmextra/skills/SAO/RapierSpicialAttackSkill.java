package com.dfdyz.epicacg.efmextra.skills.SAO;

import com.dfdyz.epicacg.efmextra.skills.EpicACGSkillSlot;
import com.dfdyz.epicacg.efmextra.skills.IMutiSpecialSkill;
import com.dfdyz.epicacg.efmextra.skills.SimpleWeaponSASkill;
import com.dfdyz.epicacg.registry.MyAnimations;
import com.dfdyz.epicacg.registry.MySkillDataKeys;
import com.dfdyz.epicacg.registry.MySkills;
import com.dfdyz.epicacg.utils.OjangUtils;
import java.util.ArrayList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.SkillDataKey;
import yesman.epicfight.skill.SkillSlots;
import yesman.epicfight.skill.Skill.Resource;
import yesman.epicfight.skill.weaponinnate.WeaponInnateSkill;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;

public class RapierSpicialAttackSkill extends SimpleWeaponSASkill implements IMutiSpecialSkill {
   private final ArrayList<ResourceLocation> childSkills = new ArrayList();
   private final ArrayList<ResourceLocation> childSkills2 = new ArrayList();
   private final AnimationManager.AnimationAccessor<? extends StaticAnimation> Normal;
   private final AnimationManager.AnimationAccessor<? extends StaticAnimation> OnRun;

   public RapierSpicialAttackSkill(_Builder builder) {
      super(builder);
      this.Normal = MyAnimations.SAO_RAPIER_SA2;
      this.OnRun = MyAnimations.SAO_RAPIER_SPECIAL_DASH;
      ResourceLocation name = this.getRegistryName();
      this.childSkills.add(OjangUtils.newRL(name.m_135827_(), "textures/gui/skills/sao_rapier_skill.png"));
      this.childSkills.add(OjangUtils.newRL(name.m_135827_(), "textures/gui/skills/" + name.m_135815_() + ".png"));
      this.childSkills2.add(OjangUtils.newRL(name.m_135827_(), "textures/gui/skills/" + name.m_135815_() + ".png"));
   }

   public static _Builder create_Builder(ResourceLocation resourceLocation) {
      return (_Builder)(new _Builder(resourceLocation)).setCategory(SkillSlots.WEAPON_INNATE.category()).setResource(Resource.STAMINA);
   }

   public void onInitiate(SkillContainer container) {
      super.onInitiate(container);
      container.getExecutor().getSkillCapability().skillContainers[EpicACGSkillSlot.SKILL_SELECTOR.universalOrdinal()].setSkill(MySkills.MUTI_SPECIAL_ATTACK);
   }

   public boolean checkExecuteCondition(SkillContainer container) {
      PlayerPatch<?> executer = container.getExecutor();
      boolean ok = false;
      SkillContainer skillContainer = executer.getSkill(SkillSlots.WEAPON_INNATE);
      int selected = (Integer)executer.getSkill(EpicACGSkillSlot.SKILL_SELECTOR).getDataManager().getDataValue((SkillDataKey)MySkillDataKeys.CHILD_SKILL_INDEX.get());
      if (!((Player)executer.getOriginal()).m_20142_()) {
         ok = skillContainer.getStack() > (selected == 0 ? 1 : 0);
      } else {
         ok = skillContainer.getStack() > 0;
      }

      return ok || ((Player)executer.getOriginal()).m_7500_();
   }

   public WeaponInnateSkill registerPropertiesToAnimation() {
      return this;
   }

   public boolean canExecute(SkillContainer container) {
      return super.canExecute(container);
   }

   public void executeOnServer(SkillContainer container, FriendlyByteBuf args) {
      ServerPlayerPatch executer = container.getServerExecutor();
      SkillContainer skill = executer.getSkill(SkillSlots.WEAPON_INNATE);
      int selected = (Integer)executer.getSkill(EpicACGSkillSlot.SKILL_SELECTOR).getDataManager().getDataValue((SkillDataKey)MySkillDataKeys.CHILD_SKILL_INDEX.get());
      if (((ServerPlayer)executer.getOriginal()).m_20142_()) {
         executer.playAnimationSynchronized(this.OnRun, 0.0F);
         this.setStackSynchronize(container, executer.getSkill(SkillSlots.WEAPON_INNATE).getStack() - 1);
      } else if (selected == 0) {
         executer.playAnimationSynchronized(this.Normal, 0.0F);
         this.setStackSynchronize(container, executer.getSkill(SkillSlots.WEAPON_INNATE).getStack() - 2);
      } else {
         executer.playAnimationSynchronized(this.OnRun, 0.0F);
         this.setStackSynchronize(container, executer.getSkill(SkillSlots.WEAPON_INNATE).getStack() - 1);
      }

      this.setDurationSynchronize(container, this.maxDuration);
      skill.activate();
   }

   public ArrayList<ResourceLocation> getSkillTextures(PlayerPatch<?> executer) {
      return ((Player)executer.getOriginal()).m_20142_() ? this.childSkills2 : this.childSkills;
   }

   public boolean isSkillActive(PlayerPatch<?> executer, int idx) {
      boolean c = ((Player)executer.getOriginal()).m_7500_();
      if (!((Player)executer.getOriginal()).m_20142_()) {
         if (idx == 0) {
            return executer.getSkill(SkillSlots.WEAPON_INNATE).getStack() > 1 || c;
         } else {
            return executer.getSkill(SkillSlots.WEAPON_INNATE).getStack() > 0 || c;
         }
      } else {
         return executer.getSkill(SkillSlots.WEAPON_INNATE).getStack() > 0 || c;
      }
   }

   public ResourceLocation getSkillTexture() {
      return OjangUtils.newRL("epicacg", "textures/gui/skills/weapon_skill_rapier.png");
   }
}
