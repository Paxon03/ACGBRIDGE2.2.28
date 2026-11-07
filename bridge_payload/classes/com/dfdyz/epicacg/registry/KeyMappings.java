package com.dfdyz.epicacg.registry;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(
   value = {Dist.CLIENT},
   bus = Bus.MOD
)
public class KeyMappings {
   public static final KeyMapping SwitchSkill = new KeyMapping("Skill Select", 66, "Epic ACG");
   public static final KeyMapping RightSkill;
   public static final KeyMapping LeftSkill;

   @SubscribeEvent
   public static void registerKeys(RegisterKeyMappingsEvent event) {
      event.register(SwitchSkill);
      event.register(RightSkill);
      event.register(LeftSkill);
   }

   static {
      RightSkill = new KeyMapping("Next Skill", InputConstants.f_84822_.m_84873_(), "Epic ACG");
      LeftSkill = new KeyMapping("Previous Skill", InputConstants.f_84822_.m_84873_(), "Epic ACG");
   }
}
