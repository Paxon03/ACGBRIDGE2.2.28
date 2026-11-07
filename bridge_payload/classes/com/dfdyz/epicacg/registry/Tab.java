package com.dfdyz.epicacg.registry;

import com.dfdyz.epicacg.utils.OjangUtils;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import yesman.epicfight.world.item.EpicFightItems;

public class Tab {
   public static final DeferredRegister<CreativeModeTab> TABS;
   public static final RegistryObject<CreativeModeTab> TAB_ITEMS;

   private static ItemStack icon() {
      return new ItemStack((ItemLike)EpicFightItems.SKILLBOOK.get());
   }

   private static void addItem(CreativeModeTab.Output var0, RegistryObject<?> var1) {
      var0.m_246326_((ItemLike)var1.get());
   }

   private static void display(CreativeModeTab.ItemDisplayParameters var0, CreativeModeTab.Output var1) {
      Items.REGISTRY_OBJECTS.forEach((var1x) -> addItem(var1, var1x));
   }

   private static CreativeModeTab buildTab() {
      return CreativeModeTab.builder().m_257941_(Component.m_237115_("itemGroup.epicacg.items")).m_257737_(Tab::icon).withBackgroundLocation(OjangUtils.newRL("epicfight", "textures/gui/container/epicfight_creative_tab.png")).m_257809_().m_257501_(Tab::display).m_257652_();
   }

   static {
      TABS = DeferredRegister.create(Registries.f_279569_, "epicacg");
      TAB_ITEMS = TABS.register("items", Tab::buildTab);
   }

   public static void paxon03$injectBridgeData(Object var0) {
      try {
         String[] var1 = new String[]{"lambent_light", "elucidator", "dark_repulsor", "firefly_sword", "battle_scythe", "slime_spear"};
         String[] var2 = new String[]{"{\"type\":\"minecraft:crafting_shaped\",\"pattern\":[\" D \",\"DCD\",\" B \"],\"key\":{\"D\":{\"item\":\"minecraft:diamond\"},\"C\":{\"item\":\"epicacg:dragon_shit_crystal\"},\"B\":{\"item\":\"minecraft:blaze_rod\"}},\"result\":{\"item\":\"epicacg:lambent_light\",\"count\":1}}", "{\"type\":\"minecraft:smithing_transform\",\"template\":{\"item\":\"minecraft:netherite_upgrade_smithing_template\"},\"base\":{\"item\":\"epicacg:lambent_light\"},\"addition\":{\"item\":\"epicacg:dragon_shit_crystal\"},\"result\":{\"item\":\"epicacg:elucidator\"}}", "{\"type\":\"minecraft:smithing_transform\",\"template\":{\"item\":\"minecraft:netherite_upgrade_smithing_template\"},\"base\":{\"item\":\"epicacg:elucidator\"},\"addition\":{\"item\":\"epicacg:dragon_shit_crystal\"},\"result\":{\"item\":\"epicacg:dark_repulsor\"}}", "{\"type\":\"minecraft:smithing_transform\",\"template\":{\"item\":\"minecraft:netherite_upgrade_smithing_template\"},\"base\":{\"item\":\"epicacg:dark_repulsor\"},\"addition\":{\"item\":\"epicacg:dragon_shit_crystal\"},\"result\":{\"item\":\"epicacg:firefly_sword\"}}", "{\"type\":\"minecraft:crafting_shaped\",\"pattern\":[\"CCN\",\"  N\",\"  B\"],\"key\":{\"C\":{\"item\":\"epicacg:dragon_shit_crystal\"},\"N\":{\"item\":\"minecraft:netherite_ingot\"},\"B\":{\"item\":\"minecraft:blaze_rod\"}},\"result\":{\"item\":\"epicacg:battle_scythe\",\"count\":1}}", "{\"type\":\"minecraft:crafting_shaped\",\"pattern\":[\"  C\",\" S \",\"B  \"],\"key\":{\"C\":{\"item\":\"epicacg:dragon_shit_crystal\"},\"S\":{\"item\":\"minecraft:slime_block\"},\"B\":{\"item\":\"minecraft:blaze_rod\"}},\"result\":{\"item\":\"epicacg:slime_spear\",\"count\":1}}"};
         Object var3 = paxon03$zeroArgReturning(var0, "net.minecraft.server.MinecraftServer");
         if (var3 == null) {
            throw new IllegalStateException("MinecraftServer not found from server event");
         }

         Object var4 = paxon03$zeroArgReturning(var3, "net.minecraft.world.item.crafting.RecipeManager");
         if (var4 == null) {
            throw new IllegalStateException("RecipeManager not found from MinecraftServer");
         }

         Method var5 = null;
         Method var6 = null;

         for(Method var10 : var4.getClass().getMethods()) {
            if (var10.getParameterCount() == 0 && Collection.class.isAssignableFrom(var10.getReturnType())) {
               var5 = var10;
            }

            if (var10.getParameterCount() == 1 && var10.getReturnType() == Void.TYPE && var10.getParameterTypes()[0] == Iterable.class) {
               var6 = var10;
            }
         }

         if (var5 == null || var6 == null) {
            throw new IllegalStateException("RecipeManager accessors not found");
         }

         Collection var12 = (Collection)var5.invoke(var4);
         ArrayList var13 = new ArrayList(var12.size() + var1.length);
         var13.addAll(var12);

         for(int var14 = 0; var14 < var1.length; ++var14) {
            Object var15 = paxon03$parseRecipe(var4.getClass(), var1[var14], var2[var14]);
            if (var15 != null) {
               var13.add(var15);
            }
         }

         var6.invoke(var4, var13);
         System.out.println("[EpicACG Bridge 2.2.19] Injected 6 weapon recipes directly into RecipeManager and applied sword damage tiers.");
      } catch (Throwable var11) {
         System.err.println("[EpicACG Bridge 2.2.19] Recipe injection failed: " + String.valueOf(var11));
         var11.printStackTrace();
      }

   }

   public static void paxon03$patchCapabilities() {
      try {
         String[] var0 = new String[]{"lambent_light", "elucidator", "dark_repulsor", "firefly_sword"};
         String[] var1 = new String[]{"{\"attributes\":{\"common\":{\"armor_negation\":30.0,\"impact\":2.0,\"max_strikes\":4,\"speed_bonus\":-0.8,\"damage_bonus\":2.0},\"sao_single_sword\":{\"armor_negation\":30.0,\"impact\":2.0,\"max_strikes\":4,\"speed_bonus\":-0.8,\"damage_bonus\":2.0},\"sao_dual_sword\":{\"armor_negation\":30.0,\"impact\":2.0,\"max_strikes\":4,\"speed_bonus\":2.5,\"damage_bonus\":2.0},\"sao_rapier\":{\"armor_negation\":50.0,\"impact\":2.0,\"max_strikes\":4,\"speed_bonus\":-0.8,\"damage_bonus\":2.0}},\"type\":\"epicacg:sao_single_sword\",\"collider\":{\"number\":3,\"center\":[0.0,0.0,-0.72],\"size\":[0.3,0.3,1.285]}}", "{\"attributes\":{\"common\":{\"armor_negation\":30.0,\"impact\":2.0,\"max_strikes\":4,\"speed_bonus\":-1.6,\"damage_bonus\":4.0},\"sao_single_sword\":{\"armor_negation\":30.0,\"impact\":2.0,\"max_strikes\":4,\"speed_bonus\":-1.6,\"damage_bonus\":4.0},\"sao_dual_sword\":{\"armor_negation\":30.0,\"impact\":2.0,\"max_strikes\":4,\"speed_bonus\":-2.0,\"damage_bonus\":4.0},\"sao_rapier\":{\"armor_negation\":30.0,\"impact\":2.0,\"max_strikes\":4,\"speed_bonus\":-1.6,\"damage_bonus\":4.0}},\"type\":\"epicacg:sao_single_sword\"}", "{\"attributes\":{\"common\":{\"armor_negation\":30.0,\"impact\":2.0,\"max_strikes\":4,\"speed_bonus\":-1.6,\"damage_bonus\":6.0},\"sao_single_sword\":{\"armor_negation\":30.0,\"impact\":2.0,\"max_strikes\":4,\"speed_bonus\":-1.6,\"damage_bonus\":6.0},\"sao_dual_sword\":{\"armor_negation\":30.0,\"impact\":2.0,\"max_strikes\":4,\"speed_bonus\":-2.0,\"damage_bonus\":6.0},\"sao_rapier\":{\"armor_negation\":30.0,\"impact\":2.0,\"max_strikes\":4,\"speed_bonus\":-1.6,\"damage_bonus\":6.0}},\"type\":\"epicacg:sao_single_sword\"}", "{\"attributes\":{\"common\":{\"armor_negation\":50.0,\"impact\":2.0,\"max_strikes\":8,\"speed_bonus\":-1.6,\"damage_bonus\":8.0},\"sao_dual_sword\":{\"armor_negation\":50.0,\"impact\":2.0,\"max_strikes\":10,\"speed_bonus\":-2.0,\"damage_bonus\":9.0}},\"collider\":{\"number\":6,\"center\":[0.0,0.0,-0.9],\"size\":[0.5,0.8,1.6]},\"type\":\"epicacg:sao_single_sword\"}"};
         Class var2 = Class.forName("net.minecraftforge.registries.ForgeRegistries");
         Object var3 = var2.getField("ITEMS").get((Object)null);
         Method var4 = null;

         for(Method var8 : var3.getClass().getMethods()) {
            if (var8.getParameterCount() == 1 && var8.getParameterTypes()[0].getName().equals("net.minecraft.resources.ResourceLocation") && var8.getReturnType() != Void.TYPE) {
               if (var8.getName().equals("getValue")) {
                  var4 = var8;
                  break;
               }

               if (var4 == null) {
                  var4 = var8;
               }
            }
         }

         if (var4 == null) {
            throw new IllegalStateException("Forge item registry getValue not found");
         }

         Class var19 = Class.forName("net.minecraft.nbt.TagParser");
         Method var20 = null;

         for(Method var10 : var19.getDeclaredMethods()) {
            if (Modifier.isStatic(var10.getModifiers()) && var10.getParameterCount() == 1 && var10.getParameterTypes()[0] == String.class && var10.getReturnType().getName().equals("net.minecraft.nbt.CompoundTag")) {
               var20 = var10;
               break;
            }
         }

         if (var20 == null) {
            throw new IllegalStateException("TagParser method not found");
         }

         var20.setAccessible(true);
         Class var22 = Class.forName("yesman.epicfight.api.data.reloader.ItemCapabilityReloadListener");
         Method var24 = null;

         for(Method var12 : var22.getMethods()) {
            Class[] var13 = var12.getParameterTypes();
            if (Modifier.isStatic(var12.getModifiers()) && var13.length == 2 && var13[0].getName().equals("net.minecraft.world.item.Item") && var13[1].getName().equals("net.minecraft.nbt.CompoundTag") && var12.getReturnType().getName().contains("CapabilityItem")) {
               var24 = var12;
               break;
            }
         }

         if (var24 == null) {
            throw new IllegalStateException("Epic Fight deserializeWeapon not found");
         }

         Class var26 = Class.forName("yesman.epicfight.world.capabilities.provider.ItemCapabilityProvider");
         Method var28 = null;

         for(Method var14 : var26.getMethods()) {
            Class[] var15 = var14.getParameterTypes();
            if (Modifier.isStatic(var14.getModifiers()) && var14.getReturnType() == Void.TYPE && var15.length == 2 && var15[0].getName().equals("net.minecraft.world.item.Item") && var15[1].getName().contains("CapabilityItem")) {
               var28 = var14;
               break;
            }
         }

         if (var28 == null) {
            throw new IllegalStateException("Epic Fight ItemCapabilityProvider.put not found");
         }

         Map var30 = null;

         try {
            Field var32 = var22.getDeclaredField("WEAPON_COMPOUNDS");
            if (var32.trySetAccessible()) {
               var30 = (Map)var32.get((Object)null);
            }
         } catch (Throwable var17) {
         }

         for(int var33 = 0; var33 < var0.length; ++var33) {
            Object var35 = paxon03$newRL("epicacg", var0[var33]);
            Object var36 = var4.invoke(var3, var35);
            if (var36 != null) {
               Object var37 = var20.invoke((Object)null, var1[var33]);
               Object var16 = var24.invoke((Object)null, var36, var37);
               var28.invoke((Object)null, var36, var16);
               if (var30 != null) {
                  var30.put(var36, var37);
               }
            }
         }
      } catch (Throwable var18) {
         System.err.println("[EpicACG Bridge 2.2.19] Sword damage patch failed: " + String.valueOf(var18));
      }

   }

   private static Object paxon03$parseRecipe(Class<?> var0, String var1, String var2) throws Exception {
      Object var3 = paxon03$newRL("epicacg", var1);
      Class var4 = Class.forName("com.google.gson.JsonParser");
      Method var5 = null;

      for(Method var9 : var4.getMethods()) {
         if (Modifier.isStatic(var9.getModifiers()) && var9.getParameterCount() == 1 && var9.getParameterTypes()[0] == String.class && var9.getName().equals("parseString")) {
            var5 = var9;
            break;
         }
      }

      Object var14;
      if (var5 != null) {
         var14 = var5.invoke((Object)null, var2);
      } else {
         Object var15 = var4.getConstructor().newInstance();
         Method var17 = var4.getMethod("parse", String.class);
         var14 = var17.invoke(var15, var2);
      }

      Object var16 = var14.getClass().getMethod("getAsJsonObject").invoke(var14);
      Method var18 = null;

      for(Method var12 : var0.getDeclaredMethods()) {
         Class[] var13 = var12.getParameterTypes();
         if (Modifier.isStatic(var12.getModifiers()) && var13.length == 2 && var13[0].getName().equals("net.minecraft.resources.ResourceLocation") && var13[1].getName().equals("com.google.gson.JsonObject")) {
            var18 = var12;
            break;
         }
      }

      if (var18 == null) {
         throw new IllegalStateException("RecipeManager.fromJson(ResourceLocation,JsonObject) not found");
      } else {
         var18.setAccessible(true);
         return var18.invoke((Object)null, var3, var16);
      }
   }

   private static Object paxon03$newRL(String var0, String var1) throws Exception {
      Class var2 = Class.forName("net.minecraft.resources.ResourceLocation");

      try {
         Constructor var3 = var2.getConstructor(String.class, String.class);
         return var3.newInstance(var0, var1);
      } catch (NoSuchMethodException var8) {
         for(Method var7 : var2.getMethods()) {
            if (Modifier.isStatic(var7.getModifiers()) && var7.getReturnType() == var2 && Arrays.equals(var7.getParameterTypes(), new Class[]{String.class, String.class})) {
               return var7.invoke((Object)null, var0, var1);
            }
         }

         throw var8;
      }
   }

   private static Object paxon03$zeroArgReturning(Object var0, String var1) throws Exception {
      for(Method var5 : var0.getClass().getMethods()) {
         if (var5.getParameterCount() == 0 && var5.getReturnType().getName().equals(var1)) {
            return var5.invoke(var0);
         }
      }

      return null;
   }
}
