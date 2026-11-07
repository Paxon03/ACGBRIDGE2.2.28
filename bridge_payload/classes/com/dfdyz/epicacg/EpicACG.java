package com.dfdyz.epicacg;

import com.dfdyz.epicacg.command.ClientCommands;
import com.dfdyz.epicacg.compat.controllable.ControllableCompat;
import com.dfdyz.epicacg.config.ClientConfig;
import com.dfdyz.epicacg.efmextra.skills.EpicACGSkillCategories;
import com.dfdyz.epicacg.efmextra.skills.EpicACGSkillSlot;
import com.dfdyz.epicacg.event.LoadingEvents;
import com.dfdyz.epicacg.network.Netmgr;
import com.dfdyz.epicacg.registry.Entities;
import com.dfdyz.epicacg.registry.Items;
import com.dfdyz.epicacg.registry.MobEffects;
import com.dfdyz.epicacg.registry.MyAnimations;
import com.dfdyz.epicacg.registry.MyModels;
import com.dfdyz.epicacg.registry.MySkillDataKeys;
import com.dfdyz.epicacg.registry.MySkills;
import com.dfdyz.epicacg.registry.Particles;
import com.dfdyz.epicacg.registry.PostPasses;
import com.dfdyz.epicacg.registry.Sounds;
import com.dfdyz.epicacg.registry.Tab;
import com.dfdyz.epicacg.registry.WeaponTypes;
import com.dfdyz.epicacg.registry.WeaponTypes.EpicACGStyles;
import com.mojang.logging.LogUtils;
import com.paxon03.epicacgruntime.compat.EpicSkillsCompat;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RecipesUpdatedEvent;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.slf4j.Logger;

@Mod("epicacg")
public class EpicACG {
   public static final String MODID = "epicacg";
   public static final Logger LOGGER = LogUtils.getLogger();
   public static final String VERSION = "20.9.6.1";

   public EpicACG() {
      IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
      IEventBus fg_bus = MinecraftForge.EVENT_BUS;
      bus.addListener(this::setupCommon);
      if (FMLEnvironment.dist == Dist.CLIENT) {
         bus.addListener(EventPriority.LOWEST, this::regClientReloader);
         bus.addListener(Particles::registryParticles);
         bus.addListener(PostPasses::register);
         bus.addListener(this::setupClient);
         bus.addListener(LoadingEvents::onModelRegister);
         bus.addListener(LoadingEvents::onModelBaked);
         fg_bus.addListener(ClientCommands::registerClientCommands);
      }

      bus.addListener(MyAnimations::registerAnimations);
      bus.addListener(WeaponTypes::register);
      EpicACGSkillCategories.ENUM_MANAGER.registerEnumCls("epicacg", EpicACGSkillCategories.class);
      EpicACGSkillSlot.ENUM_MANAGER.registerEnumCls("epicacg", EpicACGSkillSlot.class);
      WeaponTypes.EpicACGWeaponCategories.ENUM_MANAGER.registerEnumCls("epicacg", WeaponTypes.EpicACGWeaponCategories.class);
      EpicACGStyles.ENUM_MANAGER.registerEnumCls("epicacg", WeaponTypes.EpicACGStyles.class);
      Items.ITEMS.register(bus);
      Entities.ENTITIES.register(bus);
      Particles.PARTICLES.register(bus);
      MobEffects.EFFECTS.register(bus);
      Tab.TABS.register(bus);
      Sounds.SOUNDS.register(bus);
      MySkillDataKeys.DATA_KEYS.register(bus);
      bus.addListener(MySkills::BuildSkills);
      bus.register(Netmgr.CHANNEL);
      fg_bus.register(this);
   }

   private void setupCommon(FMLCommonSetupEvent event) {
      event.enqueueWork(Netmgr::register);
   }

   private void setupClient(FMLClientSetupEvent event) {
      EpicSkillsCompat.register();
      MyAnimations.LoadCamAnims();
      MyModels.LoadOtherModel();
      event.enqueueWork(LoadingEvents::RegItemModelOverride);
      ControllableCompat.load();

      try {
         ClientConfig.Load(false);
      } catch (Exception e) {
         throw new RuntimeException(e);
      }
   }

   @OnlyIn(Dist.CLIENT)
   public void regClientReloader(RegisterClientReloadListenersEvent event) {
   }

   @SubscribeEvent
   public void paxon03$bridgeServerRecipes(ServerAboutToStartEvent var1) {
      Tab.paxon03$injectBridgeData(var1);
   }

   @SubscribeEvent
   public void paxon03$bridgeClientDamageRefresh(RecipesUpdatedEvent var1) {
   }
}
