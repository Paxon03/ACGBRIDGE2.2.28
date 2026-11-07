package com.dfdyz.epicacg.registry;

import com.dfdyz.epicacg.world.item.FireFlySwordItem;
import com.dfdyz.epicacg.world.item.SimpleWeaponItem;
import com.dfdyz.epicacg.world.item.SpecialWeaponItem;
import com.dfdyz.epicacg.world.item.GenShinImpact.BowWeaponItem;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class Items {
   static final List<RegistryObject<Item>> REGISTRY_OBJECTS = new ArrayList();
   public static final DeferredRegister<Item> ITEMS;
   public static final RegistryObject<Item> TrainingBow;
   public static final RegistryObject<Item> BattleScythe;
   public static final RegistryObject<Item> Elucidator;
   public static final RegistryObject<Item> DarkRepulsor;
   public static final RegistryObject<Item> LambentLight;
   public static final RegistryObject<Item> SlimeSpear;
   public static final RegistryObject<Item> FireFlySword;
   public static final RegistryObject<Item> DragonShitCrystal;

   public static Item.Properties weapon() {
      return (new Item.Properties()).m_41487_(1);
   }

   public static RegistryObject<Item> register(String n, Supplier<Item> s) {
      RegistryObject<Item> i = ITEMS.register(n, s);
      REGISTRY_OBJECTS.add(i);
      return i;
   }

   static {
      ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, "epicacg");
      TrainingBow = register("training_bow", () -> new BowWeaponItem(weapon().m_41503_(600)));
      BattleScythe = register("battle_scythe", () -> new SimpleWeaponItem(weapon(), Tiers.DIAMOND, 0));
      Elucidator = register("elucidator", () -> new SpecialWeaponItem(weapon(), com.dfdyz.epicacg.registry.Items.EpicACGTier.SAO_Special, -1));
      DarkRepulsor = register("dark_repulsor", () -> new SimpleWeaponItem(weapon(), com.dfdyz.epicacg.registry.Items.EpicACGTier.SAO_Normal, 0));
      LambentLight = register("lambent_light", () -> new SimpleWeaponItem(weapon(), com.dfdyz.epicacg.registry.Items.EpicACGTier.SAO_Normal, -2));
      SlimeSpear = register("slime_spear", () -> new SimpleWeaponItem(weapon(), com.dfdyz.epicacg.registry.Items.EpicACGTier.SAO_Normal, 0));
      FireFlySword = register("firefly_sword", () -> new FireFlySwordItem((new Item.Properties()).m_41487_(1), com.dfdyz.epicacg.registry.Items.EpicACGTier.SAO_Normal, 0));
      DragonShitCrystal = register("dragon_shit_crystal", () -> new Item((new Item.Properties()).m_41487_(16)));
   }
}
