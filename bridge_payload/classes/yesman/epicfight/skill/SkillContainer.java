package yesman.epicfight.skill;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import yesman.epicfight.client.events.engine.ControlEngine;
import yesman.epicfight.client.world.capabilites.entitypatch.player.LocalPlayerPatch;
import yesman.epicfight.network.client.CPSkillRequest;
import yesman.epicfight.network.client.CPSkillRequest.WorkType;
import yesman.epicfight.network.server.SPChangeSkill;
import yesman.epicfight.network.server.SPSetRemotePlayerSkill;
import yesman.epicfight.skill.Skill.ActivateType;
import yesman.epicfight.skill.modules.ChargeableSkill;
import yesman.epicfight.skill.modules.HoldableSkill;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;
import yesman.epicfight.world.entity.eventlistener.SkillCastEvent;
import yesman.epicfight.world.entity.eventlistener.SkillConsumeEvent;
import yesman.epicfight.world.entity.eventlistener.PlayerEventListener.EventType;
import yesman.epicfight.world.gamerule.EpicFightGameRules;

public class SkillContainer {
   protected Skill containingSkill;
   protected int prevDuration;
   protected int duration;
   protected int maxDuration;
   protected int stack;
   protected int replaceCooldown;
   protected float resource;
   protected float prevResource;
   protected float maxResource;
   protected boolean isActivated;
   protected boolean disabled;
   protected final SkillSlot slot;
   protected final PlayerPatch<?> executor;
   protected final SkillDataManager skillDataManager;

   public SkillContainer(PlayerPatch<?> executor, SkillSlot skillSlot) {
      this.executor = executor;
      this.slot = skillSlot;
      this.skillDataManager = new SkillDataManager(this);
   }

   public PlayerPatch<?> getExecutor() {
      return this.executor;
   }

   public LocalPlayerPatch getClientExecutor() {
      return (LocalPlayerPatch)this.executor;
   }

   public ServerPlayerPatch getServerExecutor() {
      return (ServerPlayerPatch)this.executor;
   }

   public boolean setSkill(@Nullable Skill skill) {
      return this.setSkill(skill, false);
   }

   public boolean setSkill(@Nullable Skill skill, boolean initialize) {
      if (this.executor.isLogicalClient() && !((Player)this.executor.getOriginal()).m_7578_()) {
         return false;
      } else if (this.containingSkill == skill && !initialize) {
         return false;
      } else if (skill != null && skill.category != this.slot.category()) {
         return false;
      } else {
         if (this.containingSkill != null) {
            this.containingSkill.onRemoved(this);
            if (this.executor.isLogicalClient()) {
               this.containingSkill.onRemoveClient(this);
            }

            this.executor.getSkillCapability().removeSkillFromContainer(this.containingSkill);
         }

         this.containingSkill = skill;
         this.resetValues();
         this.skillDataManager.clearData();
         if (skill != null) {
            Set<SkillDataKey<?>> datakeys = (Set)SkillDataKey.getSkillDataKeyMap().get(skill.getClass());
            if (datakeys != null) {
               SkillDataManager var10001 = this.skillDataManager;
               Objects.requireNonNull(var10001);
               datakeys.forEach(var10001::registerData);
            }

            skill.onInitiate(this);
            if (this.executor.isLogicalClient()) {
               skill.onInitiateClient(this);
            }

            this.setMaxResource(skill.consumption);
            this.setMaxDuration(skill.maxDuration);
            this.executor.getSkillCapability().setSkillToContainer(skill, this);
         }

         this.executor.clampMaxAttributes();
         this.stack = 0;
         if (initialize) {
            this.setDisabled(false);
         }

         return true;
      }
   }

   @OnlyIn(Dist.CLIENT)
   public void setSkillRemote(@Nullable Skill skill) {
      if (this.executor.isLogicalClient() && !((Player)this.executor.getOriginal()).m_7578_()) {
         if (this.containingSkill != skill) {
            if (skill == null || skill.category == this.slot.category()) {
               if (this.containingSkill != null) {
                  this.containingSkill.onRemoveClient(this);
                  this.executor.getSkillCapability().removeSkillFromContainer(this.containingSkill);
               }

               this.containingSkill = skill;
               this.resetValues();
               this.skillDataManager.clearData();
               if (skill != null) {
                  Set<SkillDataKey<?>> datakeys = (Set)SkillDataKey.getSkillDataKeyMap().get(skill.getClass());
                  if (datakeys != null && !datakeys.isEmpty()) {
                     Stream var10000 = datakeys.stream().filter(SkillDataKey::syncronizeToTrackingPlayers);
                     SkillDataManager var10001 = this.skillDataManager;
                     Objects.requireNonNull(var10001);
                     var10000.forEach(var10001::registerData);
                  }

                  skill.onInitiateClient(this);
                  this.executor.getSkillCapability().setSkillToContainer(skill, this);
                  this.setMaxResource(skill.consumption);
                  this.setMaxDuration(skill.maxDuration);
               }

               this.stack = 0;
            }
         }
      }
   }

   public boolean isDisabled() {
      return this.disabled;
   }

   public void setDisabled(boolean disable) {
      this.disabled = disable;
   }

   public void resetValues() {
      this.isActivated = false;
      this.prevDuration = 0;
      this.duration = 0;
      this.prevResource = 0.0F;
      this.resource = 0.0F;
   }

   public boolean isEmpty() {
      return this.containingSkill == null;
   }

   public boolean hasSkill() {
      return this.containingSkill != null;
   }

   public void setResource(float value) {
      if (this.containingSkill != null) {
         this.containingSkill.setConsumption(this, value);
      } else {
         this.prevResource = 0.0F;
         this.resource = 0.0F;
      }

   }

   public void setMaxDuration(int value) {
      this.maxDuration = Math.max(value, 0);
   }

   public void setDuration(int value) {
      if (this.containingSkill != null) {
         if (!this.isActivated() && value > 0) {
            this.isActivated = true;
         }

         this.duration = Mth.m_14045_(value, 0, this.maxDuration);
      } else {
         this.duration = 0;
      }

   }

   public void setStack(int stack) {
      if (this.containingSkill != null) {
         this.stack = Mth.m_14045_(stack, 0, this.containingSkill.maxStackSize);
         if (this.stack <= 0 && this.containingSkill.shouldDeactivateAutomatically(this.executor)) {
            this.deactivate();
            this.containingSkill.onReset(this);
         }
      } else {
         this.stack = 0;
      }

   }

   public void setMaxResource(float maxResource) {
      this.maxResource = maxResource;
   }

   public void setReplaceCooldown(int replaceCooldown) {
      replaceCooldown *= this.slot.toString().equals("SAO_SINGLE_SWORD") ^ 1;
      this.replaceCooldown = Mth.m_14045_(replaceCooldown, 0, (Integer)EpicFightGameRules.SKILL_REPLACE_COOLDOWN.getRuleValue(((Player)this.executor.getOriginal()).m_9236_()));
   }

   @OnlyIn(Dist.CLIENT)
   public SkillCastEvent sendCastRequest(LocalPlayerPatch executor, ControlEngine controlEngine) {
      SkillCastEvent event = new SkillCastEvent(executor, this, this.containingSkill == null ? null : this.containingSkill.gatherArguments(this, controlEngine));
      if (this.containingSkill == null) {
         return event;
      } else {
         Object packet;
         label39: {
            Skill var6 = this.containingSkill;
            if (var6 instanceof HoldableSkill) {
               HoldableSkill holdableSkill = (HoldableSkill)var6;
               if (this.containingSkill.getActivateType() == ActivateType.HELD) {
                  if (executor.isHoldingSkill(this.containingSkill)) {
                     packet = this.containingSkill.getExecutionPacket(this, event.getArguments());
                     executor.resetHolding();
                  } else {
                     if (!this.canUse(executor, event)) {
                        this.containingSkill.validationFeedback(this);
                        return event;
                     }

                     CPSkillRequest buffer = new CPSkillRequest(this.getSlot(), WorkType.HOLD_START);
                     holdableSkill.gatherHoldArguments(this, controlEngine, buffer.getBuffer());
                     packet = buffer;
                  }
                  break label39;
               }
            }

            if (!this.canUse(executor, event)) {
               this.containingSkill.validationFeedback(this);
               return event;
            }

            packet = this.containingSkill.getExecutionPacket(this, event.getArguments());
         }

         if (packet != null) {
            controlEngine.addPacketToSend(packet);
         }

         return event;
      }
   }

   @OnlyIn(Dist.CLIENT)
   public void sendCancelRequest(LocalPlayerPatch executor, ControlEngine controlEngine) {
      CPSkillRequest packet = new CPSkillRequest(this.getSlot(), WorkType.CANCEL);
      controlEngine.addPacketToSend(packet);
   }

   public boolean requestCasting(ServerPlayerPatch executor, FriendlyByteBuf buf) {
      SkillCastEvent event = new SkillCastEvent(executor, this, buf);
      if (this.canUse(executor, event)) {
         this.containingSkill.executeOnServer(this, event.getArguments());
         return true;
      } else {
         return false;
      }
   }

   public boolean requestCancel(ServerPlayerPatch executor, FriendlyByteBuf buf) {
      if (this.containingSkill != null) {
         this.containingSkill.cancelOnServer(this, buf);
         return true;
      } else {
         return false;
      }
   }

   public boolean requestHold(ServerPlayerPatch executor, FriendlyByteBuf buf) {
      Skill var4 = this.containingSkill;
      if (var4 instanceof HoldableSkill holdableSkill) {
         SkillCastEvent event = new SkillCastEvent(executor, this, buf);
         if (this.canUse(executor, event)) {
            SkillConsumeEvent consumeEvent = new SkillConsumeEvent(executor, this.containingSkill, this.containingSkill.resource, buf);
            executor.getEventListener().triggerEvents(EventType.SKILL_CONSUME_EVENT, consumeEvent);
            if (!consumeEvent.isCanceled()) {
               consumeEvent.getArguments().resetReaderIndex();
               consumeEvent.getResourceType().consumer.consume(this, executor, consumeEvent.getAmount());
            }

            executor.startSkillHolding(holdableSkill);
            return true;
         }
      }

      return false;
   }

   public SkillDataManager getDataManager() {
      return this.skillDataManager;
   }

   public void transferDataTo(SkillContainer skillContainer) {
      this.skillDataManager.transferDataTo(skillContainer.skillDataManager);
      skillContainer.prevDuration = this.prevDuration;
      skillContainer.duration = this.duration;
      skillContainer.maxDuration = this.maxDuration;
      skillContainer.resource = this.resource;
      skillContainer.prevResource = this.prevResource;
      skillContainer.maxResource = this.maxResource;
      skillContainer.isActivated = this.isActivated;
      skillContainer.disabled = this.disabled;
      skillContainer.stack = this.stack;
   }

   public float getResource() {
      return this.resource;
   }

   public int getRemainDuration() {
      return this.duration;
   }

   public boolean canUse(PlayerPatch<?> executor, SkillCastEvent event) {
      if (this.containingSkill == null) {
         return false;
      } else {
         if (executor.isHoldingSkill(this.containingSkill)) {
            Skill var4 = this.containingSkill;
            if (var4 instanceof ChargeableSkill) {
               ChargeableSkill chargingSkill = (ChargeableSkill)var4;
               if (executor.isLogicalClient()) {
                  return true;
               }

               return executor.getSkillChargingTicks() >= chargingSkill.getMinChargingTicks();
            }
         }

         event.setSkillExecutable(this.containingSkill.canExecute(this));
         event.setStateExecutable(this.containingSkill.isExecutableState(executor));
         executor.getEventListener().triggerEvents(EventType.SKILL_CAST_EVENT, event);
         if (!event.isCanceled() && event.isExecutable()) {
            return ((Player)executor.getOriginal()).m_7500_() || this.containingSkill.resourcePredicate(executor, event) || this.isActivated() && this.containingSkill.activateType == ActivateType.DURATION;
         } else {
            return false;
         }
      }
   }

   public void update() {
      if (this.replaceCooldown > 0) {
         this.replaceCooldown = Mth.m_14045_(this.replaceCooldown - 1, 0, (Integer)EpicFightGameRules.SKILL_REPLACE_COOLDOWN.getRuleValue(((Player)this.executor.getOriginal()).m_9236_()));
      }

      if (this.containingSkill != null) {
         this.containingSkill.updateContainer(this);
      }

   }

   public int getStack() {
      return this.stack;
   }

   public SkillSlot getSlot() {
      return this.slot;
   }

   public int getSlotId() {
      return this.slot.universalOrdinal();
   }

   public Skill getSkill() {
      return this.containingSkill;
   }

   public float getMaxResource() {
      return this.maxResource;
   }

   public void activate() {
      if (!this.isActivated) {
         this.prevDuration = this.maxDuration;
         this.duration = this.maxDuration;
         this.isActivated = true;
      }

   }

   public void deactivate() {
      if (this.isActivated) {
         this.prevDuration = 0;
         this.duration = 0;
         this.isActivated = false;
      }

   }

   public boolean isActivated() {
      return this.isActivated;
   }

   public boolean hasSkill(Skill skill) {
      return this.containingSkill != null && this.containingSkill.equals(skill);
   }

   public boolean isFull() {
      return this.containingSkill == null || this.stack >= this.containingSkill.maxStackSize;
   }

   public float getResource(float partialTicks) {
      return this.containingSkill != null && this.maxResource > 0.0F ? (this.prevResource + (this.resource - this.prevResource) * partialTicks) / this.maxResource : 0.0F;
   }

   public float getNeededResource() {
      return this.containingSkill != null ? this.maxResource - this.resource : 0.0F;
   }

   public float getDurationRatio(float partialTicks) {
      return this.containingSkill != null && this.maxDuration > 0 ? ((float)this.prevDuration + (float)(this.duration - this.prevDuration) * partialTicks) / (float)this.maxDuration : 0.0F;
   }

   public boolean onReplaceCooldown() {
      return (this.replaceCooldown > 0 && !((Player)this.executor.getOriginal()).m_7500_()) & (this.slot.toString().equals("SAO_SINGLE_SWORD") ^ true);
   }

   public int getReplaceCooldown() {
      return this.replaceCooldown;
   }

   public SPChangeSkill createSyncPacketToLocalPlayer() {
      return new SPChangeSkill(this.getSlot(), ((Player)this.executor.getOriginal()).m_19879_(), this.getSkill());
   }

   public SPSetRemotePlayerSkill createSyncPacketToRemotePlayer() {
      return new SPSetRemotePlayerSkill(((Player)this.executor.getOriginal()).m_19879_(), this.getSlot(), this.getSkill());
   }

   public boolean equals(Object object) {
      if (object instanceof SkillContainer skillContainer) {
         return this.slot.equals(skillContainer.slot);
      } else {
         return false;
      }
   }
}
