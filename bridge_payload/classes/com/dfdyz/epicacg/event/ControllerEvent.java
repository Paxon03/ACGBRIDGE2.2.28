package com.dfdyz.epicacg.event;

import com.dfdyz.epicacg.client.feedback.FeedBackManager;
import com.dfdyz.epicacg.network.Netmgr;
import com.dfdyz.epicacg.network.Client.C_RollSkillSelect;
import com.dfdyz.epicacg.registry.KeyMappings;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(
   modid = "epicacg",
   value = {Dist.CLIENT}
)
public class ControllerEvent {
   private static boolean isCtrlPressed = false;
   private static int roll_unit = 0;
   private static boolean directToggleRight = true;

   @SubscribeEvent(
      priority = EventPriority.HIGHEST
   )
   public static void mouseEvent(InputEvent.MouseScrollingEvent event) {
      if (isCtrlPressed || KeyMappings.SwitchSkill.m_90857_()) {
         double delta = event.getScrollDelta();
         if (delta > (double)0.0F) {
            Netmgr.sendToServer(new C_RollSkillSelect(-1));
            directToggleRight = true;
         } else if (delta < (double)0.0F) {
            Netmgr.sendToServer(new C_RollSkillSelect(1));
            directToggleRight = false;
         }

         if (delta != (double)0.0F) {
            event.setCanceled(true);
         }
      }

   }

   @SubscribeEvent
   public static void keyboardEvent(InputEvent.Key event) {
      int key = event.getKey();
      int action = event.getAction();
      int switchKey = KeyMappings.SwitchSkill.getKey().m_84873_();
      if (key != switchKey) {
         if (key == KeyMappings.RightSkill.getKey().m_84873_() && action == 1) {
            Netmgr.sendToServer(new C_RollSkillSelect(1));
            directToggleRight = false;
         } else {
            if (key == KeyMappings.LeftSkill.getKey().m_84873_() && action == 1) {
               Netmgr.sendToServer(new C_RollSkillSelect(-1));
               directToggleRight = true;
            }

         }
      } else {
         if (action != 1 && action != 2) {
            if (action == 0) {
               isCtrlPressed = false;
            }
         } else {
            isCtrlPressed = true;
            if (action == 1) {
               Netmgr.sendToServer(new C_RollSkillSelect(directToggleRight ? 1 : -1));
               directToggleRight = !directToggleRight;
            }
         }

      }
   }

   @SubscribeEvent
   public static void Tick(TickEvent.ClientTickEvent event) {
      FeedBackManager.tick();
   }
}
