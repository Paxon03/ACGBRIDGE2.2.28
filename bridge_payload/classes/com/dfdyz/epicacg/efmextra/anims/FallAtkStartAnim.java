package com.dfdyz.epicacg.efmextra.anims;

import com.dfdyz.epicacg.registry.MySkillDataKeys;
import com.dfdyz.epicacg.registry.MySkills;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;
import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.api.animation.JointTransform;
import yesman.epicfight.api.animation.Pose;
import yesman.epicfight.api.animation.property.AnimationProperty.ActionAnimationProperty;
import yesman.epicfight.api.animation.types.ActionAnimation;
import yesman.epicfight.api.animation.types.DynamicAnimation;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.asset.AssetAccessor;
import yesman.epicfight.api.client.animation.Layer.LayerType;
import yesman.epicfight.api.client.animation.Layer.Priority;
import yesman.epicfight.api.client.animation.property.ClientAnimationProperties;
import yesman.epicfight.api.model.Armature;
import yesman.epicfight.api.utils.math.OpenMatrix4f;
import yesman.epicfight.api.utils.math.Vec3f;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.SkillDataKey;
import yesman.epicfight.skill.SkillSlots;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;

public class FallAtkStartAnim extends ActionAnimation {
   public AnimationManager.AnimationAccessor<? extends StaticAnimation> Loop;

   public FallAtkStartAnim(float convertTime, AnimationManager.AnimationAccessor<? extends FallAtkStartAnim> path, AssetAccessor<? extends Armature> model, AnimationManager.AnimationAccessor<? extends StaticAnimation> Loop) {
      super(convertTime, path, model);
      this.Loop = Loop;
      this.addProperty(ActionAnimationProperty.CANCELABLE_MOVE, false);
      if (FMLEnvironment.dist == Dist.CLIENT) {
         this.addProperty(ClientAnimationProperties.PRIORITY, Priority.HIGHEST);
         this.addProperty(ClientAnimationProperties.LAYER_TYPE, LayerType.BASE_LAYER);
      }

   }

   public void begin(LivingEntityPatch<?> entitypatch) {
      super.begin(entitypatch);
      ((LivingEntity)entitypatch.getOriginal()).m_20334_((double)0.0F, (double)0.0F, (double)0.0F);
      ((LivingEntity)entitypatch.getOriginal()).m_20242_(true);
      if (!entitypatch.isLogicalClient() && entitypatch instanceof ServerPlayerPatch spp) {
         SkillContainer container = spp.getSkill(SkillSlots.BASIC_ATTACK);
         if (container.getSkill() == MySkills.GS_Bow_FallAttackPatch) {
            if (!container.getDataManager().hasData((SkillDataKey)MySkillDataKeys.GS_FA_FALL_STATE.get())) {
               container.getDataManager().registerData((SkillDataKey)MySkillDataKeys.GS_FA_FALL_STATE.get());
            }

            container.getDataManager().setData((SkillDataKey)MySkillDataKeys.GS_FA_FALL_STATE.get(), 0);
            container.update();
         }
      }

   }

   public void tick(LivingEntityPatch<?> entitypatch) {
      super.tick(entitypatch);
      ((LivingEntity)entitypatch.getOriginal()).m_20334_((double)0.0F, (double)0.0F, (double)0.0F);
      ((LivingEntity)entitypatch.getOriginal()).m_20242_(true);
   }

   public void modifyPose(DynamicAnimation animation, Pose pose, LivingEntityPatch<?> entitypatch, float time, float partialTicks) {
      JointTransform jt = pose.orElseEmpty("Root");
      Vec3f jointPosition = jt.translation();
      OpenMatrix4f toRootTransformApplied = entitypatch.getArmature().searchJointByName("Root").getLocalTransform().removeTranslation();
      OpenMatrix4f toOrigin = OpenMatrix4f.invert(toRootTransformApplied, (OpenMatrix4f)null);
      Vec3f worldPosition = OpenMatrix4f.transform3v(toRootTransformApplied, jointPosition, (Vec3f)null);
      worldPosition.x = 0.0F;
      worldPosition.y = 0.0F;
      worldPosition.z = 0.0F;
      OpenMatrix4f.transform3v(toOrigin, worldPosition, worldPosition);
      jointPosition.x = worldPosition.x;
      jointPosition.y = worldPosition.y;
      jointPosition.z = worldPosition.z;
   }

   public void end(LivingEntityPatch<?> entitypatch, AssetAccessor<? extends DynamicAnimation> nextAnimation, boolean isEnd) {
      super.end(entitypatch, nextAnimation, isEnd);
      ((LivingEntity)entitypatch.getOriginal()).m_20242_(false);
      if (!entitypatch.isLogicalClient() && entitypatch instanceof ServerPlayerPatch spp) {
         SkillContainer container = spp.getSkill(SkillSlots.BASIC_ATTACK);
         if (container.getSkill() == MySkills.GS_Bow_FallAttackPatch) {
            if (!container.getDataManager().hasData((SkillDataKey)MySkillDataKeys.GS_FA_FALL_STATE.get())) {
               container.getDataManager().registerData((SkillDataKey)MySkillDataKeys.GS_FA_FALL_STATE.get());
               container.getDataManager().setData((SkillDataKey)MySkillDataKeys.GS_FA_FALL_STATE.get(), 0);
            }

            int state = (Integer)container.getDataManager().getDataValue((SkillDataKey)MySkillDataKeys.GS_FA_FALL_STATE.get());
            if (state == 0) {
               container.getDataManager().setData((SkillDataKey)MySkillDataKeys.GS_FA_FALL_STATE.get(), 1);
               ((LivingEntity)entitypatch.getOriginal()).m_20334_((double)0.0F, (double)0.0F, (double)0.0F);
               ((LivingEntity)entitypatch.getOriginal()).m_20242_(true);
               spp.playAnimationSynchronized(this.Loop, 0.0F);
            }

            container.update();
         }
      }

   }

   public boolean isBasicAttackAnimation() {
      return true;
   }
}
