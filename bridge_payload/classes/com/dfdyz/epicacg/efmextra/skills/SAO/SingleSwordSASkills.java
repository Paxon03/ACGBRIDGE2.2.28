package com.dfdyz.epicacg.efmextra.skills.SAO;

import com.dfdyz.epicacg.efmextra.skills.EpicACGSkillSlot;
import com.dfdyz.epicacg.efmextra.skills.IMutiSpecialSkill;
import com.dfdyz.epicacg.efmextra.skills.MultiSpecialSkill;
import com.dfdyz.epicacg.efmextra.skills.SimpleWeaponSASkill;
import com.dfdyz.epicacg.registry.MyAnimations;
import com.dfdyz.epicacg.registry.MySkillDataKeys;
import com.dfdyz.epicacg.registry.MySkills;
import com.dfdyz.epicacg.utils.OjangUtils;
import java.util.ArrayList;
import java.util.UUID;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.api.animation.types.AttackAnimation;
import yesman.epicfight.api.utils.AttackResult.ResultType;
import yesman.epicfight.skill.SkillCategories;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.SkillDataKey;
import yesman.epicfight.skill.SkillSlots;
import yesman.epicfight.skill.weaponinnate.SimpleWeaponInnateSkill;
import yesman.epicfight.skill.weaponinnate.WeaponInnateSkill;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.entity.eventlistener.PlayerEventListener.EventType;

public class SingleSwordSASkills extends SimpleWeaponSASkill implements IMutiSpecialSkill {
   private final ArrayList<ResourceLocation> noPower = new ArrayList();
   private final ArrayList<ResourceLocation> morePower = new ArrayList();
   private final UUID EventUUID = UUID.fromString("eb69decf-48a1-5333-dacc-884fd345c02a");
   private final AnimationManager.AnimationAccessor<? extends AttackAnimation> noPowerAnimation1;
   private final AnimationManager.AnimationAccessor<? extends AttackAnimation> morePowerAnimation1;

   public SingleSwordSASkills(SimpleWeaponInnateSkill.Builder builder) {
      super(builder);
      this.noPowerAnimation1 = MyAnimations.HSR_ACHERON_SA;
      this.morePowerAnimation1 = MyAnimations.DMC5_V_JC;
      ResourceLocation name = this.getRegistryName();
      this.noPower.add(OjangUtils.newRL(name.m_135827_(), "textures/gui/skills/" + name.m_135815_() + ".png"));
      this.noPower.add(OjangUtils.newRL(name.m_135827_(), "textures/gui/skills/single/judgement_cut.png"));
   }

   public static SimpleWeaponInnateSkill.Builder createBuilder(ResourceLocation resourceLocation) {
      return (SimpleWeaponInnateSkill.Builder)SimpleWeaponInnateSkill.createSimpleWeaponInnateBuilder().setCategory(SkillCategories.WEAPON_INNATE).setRegistryName(resourceLocation);
   }

   public void onInitiate(SkillContainer container) {
      super.onInitiate(container);
      container.getExecutor().getSkillCapability().skillContainers[EpicACGSkillSlot.SKILL_SELECTOR.universalOrdinal()].setSkill(MySkills.MUTI_SPECIAL_ATTACK);
      container.getExecutor().getEventListener().addEventListener(EventType.TAKE_DAMAGE_EVENT_ATTACK, this.EventUUID, (e) -> {
         PlayerPatch playerPatch = e.getPlayerPatch();
         SkillContainer sc = playerPatch.getSkill(SkillSlots.WEAPON_INNATE);
         if (sc != null) {
            boolean inv = (Boolean)sc.getDataManager().getDataValue((SkillDataKey)MySkillDataKeys.JCE_Invincible.get());
            if (inv) {
               e.setResult(ResultType.BLOCKED);
               e.setCanceled(true);
            }
         }

      });
   }

   public void onRemoved(SkillContainer container) {
      super.onRemoved(container);
      container.getDataManager().setData((SkillDataKey)MySkillDataKeys.JCE_Invincible.get(), false);
      container.getExecutor().getEventListener().removeListener(EventType.TAKE_DAMAGE_EVENT_ATTACK, this.EventUUID);
   }

   public boolean checkExecuteCondition(SkillContainer container) {
      PlayerPatch<?> executer = container.getExecutor();
      SkillContainer skillContainer = executer.getSkill(SkillSlots.WEAPON_INNATE);
      int selected = (Integer)executer.getSkill(EpicACGSkillSlot.SKILL_SELECTOR).getDataManager().getDataValue((SkillDataKey)MySkillDataKeys.CHILD_SKILL_INDEX.get());
      boolean ok = skillContainer.getStack() >= (selected == 0 ? 1 : 1);
      return ok || ((Player)executer.getOriginal()).m_7500_();
   }

   public WeaponInnateSkill registerPropertiesToAnimation() {
      return this;
   }

   public void executeOnServer(SkillContainer container, FriendlyByteBuf args) {
      PlayerPatch<?> executer = container.getExecutor();
      int selected = MultiSpecialSkill.getSelected(executer);
      if (selected == 0) {
         executer.playAnimationSynchronized(this.noPowerAnimation1, 0.0F);
         this.setStackSynchronize(container, executer.getSkill(SkillSlots.WEAPON_INNATE).getStack() - 1);
      } else {
         executer.playAnimationSynchronized(MyAnimations.DMC5_V_JC, 0.0F);
         this.setStackSynchronize(container, executer.getSkill(SkillSlots.WEAPON_INNATE).getStack() - 1);
      }

   }

   public ArrayList<ResourceLocation> getSkillTextures(PlayerPatch<?> executer) {
      return this.noPower;
   }

   public boolean isSkillActive(PlayerPatch<?> executer, int idx) {
      if (((Player)executer.getOriginal()).m_7500_()) {
         return true;
      } else if (idx == 0) {
         return true;
      } else {
         return idx == 1 && executer.getSkill(SkillSlots.WEAPON_INNATE).getStack() >= 1;
      }
   }
}
