package com.paxon03.epicacgbootstrap;

import cpw.mods.modlauncher.api.IEnvironment;
import cpw.mods.modlauncher.api.IModuleLayerManager;
import cpw.mods.modlauncher.api.ITransformationService;
import cpw.mods.modlauncher.api.ITransformer;
import cpw.mods.modlauncher.api.ITransformerVotingContext;
import cpw.mods.modlauncher.api.IncompatibleEnvironmentException;
import cpw.mods.modlauncher.api.TargetType;
import cpw.mods.modlauncher.api.TransformerVoteResult;
import cpw.mods.modlauncher.api.ITransformer.Target;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;
import org.objectweb.asm.tree.ClassNode;

public final class EpicACGRuntimeEngine implements ITransformationService {
   private static final String EXPECTED_ACG_SHA256 = "f0314ecd0345ef629759711bac0e438e1eff6d758d8bc6621007e823c80b5504";
   private static final LinkedHashMap<String, String> PAYLOADS = new LinkedHashMap();
   private static Path verifiedACGPath;

   public String name() {
      return "epicacgruntimeengine";
   }

   public void initialize(IEnvironment var1) {
      verifiedACGPath = locateAndVerifyCleanACG();
      System.out.println("[EpicACG Runtime Engine 2.2.24 SAFE] Clean original verified READ-ONLY by hash: f0314ecd0345ef629759711bac0e438e1eff6d758d8bc6621007e823c80b5504");
      System.out.println("[EpicACG Runtime Engine 2.2.17 SAFE] Target: " + String.valueOf(verifiedACGPath));
      Runtime.getRuntime().addShutdownHook(new Thread(() -> {
         try {
            verifyStillClean("JVM shutdown");
         } catch (Throwable var1) {
            System.err.println("[EpicACG Runtime Engine 2.2.17 SAFE] HASH WATCH FAILED AT SHUTDOWN: " + String.valueOf(var1));
         }

      }, "EpicACG-Hash-Watch"));
   }

   public void onLoad(IEnvironment var1, Set<String> var2) throws IncompatibleEnvironmentException {
   }

   public List<?> beginScanning(IEnvironment var1) {
      verifyStillClean("before dependency alias");
      installAlias();
      verifyStillClean("after dependency alias");
      return Collections.emptyList();
   }

   public List<?> completeScan(IModuleLayerManager var1) {
      verifyStillClean("after Forge scan");
      return Collections.emptyList();
   }

   public List<? extends ITransformer<?>> transformers() {
      return List.of(new FullCompatTransformer());
   }

   private static byte[] readResource(String var0) throws Exception {
      InputStream var1 = EpicACGRuntimeEngine.class.getClassLoader().getResourceAsStream(var0);

      byte[] var2;
      try {
         if (var1 == null) {
            throw new FileNotFoundException(var0);
         }

         var2 = var1.readAllBytes();
      } catch (Throwable var5) {
         if (var1 != null) {
            try {
               var1.close();
            } catch (Throwable var4) {
               var5.addSuppressed(var4);
            }
         }

         throw var5;
      }

      if (var1 != null) {
         var1.close();
      }

      return var2;
   }

   private static ClassNode parseClass(byte[] var0) throws Exception {
      ClassLoader var1 = EpicACGRuntimeEngine.class.getClassLoader();
      Class var2 = Class.forName("org.objectweb.asm.ClassReader", true, var1);
      Object var3 = var2.getConstructor(byte[].class).newInstance(var0);
      Class var4 = Class.forName("org.objectweb.asm.tree.ClassNode", true, var1);
      Object var5 = var4.getConstructor().newInstance();
      Method var6 = null;

      for(Method var10 : var2.getMethods()) {
         Class[] var11 = var10.getParameterTypes();
         if (var10.getName().equals("accept") && var11.length == 2 && var11[1] == Integer.TYPE && var11[0].isAssignableFrom(var4)) {
            var6 = var10;
            break;
         }
      }

      if (var6 == null) {
         throw new NoSuchMethodException("ClassReader.accept(ClassVisitor,int)");
      } else {
         var6.invoke(var3, var5, 0);
         return (ClassNode)var5;
      }
   }

   private static Path locateAndVerifyCleanACG() {
      try {
         Path var0 = locateGameDir().resolve("mods");
         if (!Files.isDirectory(var0, new LinkOption[0])) {
            throw new IllegalStateException("Mods directory not found: " + String.valueOf(var0));
         } else {
            ArrayList var1 = new ArrayList();
            Stream var2 = Files.list(var0);

            try {
               Stream var10000 = var2.filter((var0x) -> Files.isRegularFile(var0x, new LinkOption[0])).filter((var0x) -> var0x.getFileName().toString().toLowerCase(Locale.ROOT).endsWith(".jar")).filter((var0x) -> {
                  String var1 = var0x.getFileName().toString().toLowerCase(Locale.ROOT);
                  return var1.contains("epicacg") && !var1.contains("bridge") && !var1.contains("runtime") && !var1.contains("resources") && !var1.contains("compat");
               });
               Objects.requireNonNull(var1);
               var10000.forEach(var1::add);
            } catch (Throwable var8) {
               if (var2 != null) {
                  try {
                     var2.close();
                  } catch (Throwable var7) {
                     var8.addSuppressed(var7);
                  }
               }

               throw var8;
            }

            if (var2 != null) {
               var2.close();
            }

            Path var11 = null;
            ArrayList var3 = new ArrayList();

            for(Path var5 : var1) {
               String var6 = sha256(var5);
               String var10001 = String.valueOf(var5.getFileName());
               var3.add(var10001 + "=" + var6);
               if ("f0314ecd0345ef629759711bac0e438e1eff6d758d8bc6621007e823c80b5504".equals(var6)) {
                  if (var11 != null) {
                     String var10002 = String.valueOf(var11);
                     throw new IllegalStateException("More than one clean Epic ACG JAR found. Keep only one: " + var10002 + " and " + String.valueOf(var5));
                  }

                  var11 = var5;
               }
            }

            if (var11 == null) {
               throw new IllegalStateException("Clean Epic ACG 20.12.5.0 not found. Expected SHA-256 f0314ecd0345ef629759711bac0e438e1eff6d758d8bc6621007e823c80b5504; candidates: " + String.valueOf(var3));
            } else {
               return var11.toAbsolutePath().normalize();
            }
         }
      } catch (RuntimeException var9) {
         throw var9;
      } catch (Throwable var10) {
         throw new RuntimeException("Could not verify original Epic ACG JAR", var10);
      }
   }

   private static Path locateGameDir() {
      try {
         Optional var0 = ProcessHandle.current().info().arguments();
         if (var0.isPresent()) {
            String[] var1 = (String[])var0.get();

            for(int var2 = 0; var2 < var1.length - 1; ++var2) {
               if ("--gameDir".equals(var1[var2])) {
                  return Paths.get(var1[var2 + 1]).toAbsolutePath().normalize();
               }
            }
         }
      } catch (Throwable var3) {
      }

      return Paths.get(System.getProperty("user.dir", ".")).toAbsolutePath().normalize();
   }

   private static void verifyStillClean(String var0) {
      if (verifiedACGPath == null) {
         throw new IllegalStateException("ACG path was not verified");
      } else {
         try {
            String var1 = sha256(verifiedACGPath);
            if (!"f0314ecd0345ef629759711bac0e438e1eff6d758d8bc6621007e823c80b5504".equals(var1)) {
               throw new IllegalStateException("Epic ACG JAR changed during launch at phase '" + var0 + "'. Expected f0314ecd0345ef629759711bac0e438e1eff6d758d8bc6621007e823c80b5504 but got " + var1 + ". Bridge stopped immediately.");
            } else {
               System.out.println("[EpicACG Runtime Engine 2.2.17 SAFE] SHA-256 unchanged " + var0 + ": " + var1);
            }
         } catch (RuntimeException var2) {
            throw var2;
         } catch (Throwable var3) {
            throw new RuntimeException("Unable to verify Epic ACG JAR " + var0, var3);
         }
      }
   }

   private static String sha256(Path var0) throws Exception {
      MessageDigest var1 = MessageDigest.getInstance("SHA-256");
      InputStream var2 = Files.newInputStream(var0, StandardOpenOption.READ);

      try {
         byte[] var3 = new byte[65536];

         int var4;
         while((var4 = var2.read(var3)) >= 0) {
            if (var4 > 0) {
               var1.update(var3, 0, var4);
            }
         }
      } catch (Throwable var8) {
         if (var2 != null) {
            try {
               var2.close();
            } catch (Throwable var7) {
               var8.addSuppressed(var7);
            }
         }

         throw var8;
      }

      if (var2 != null) {
         var2.close();
      }

      StringBuilder var9 = new StringBuilder();

      for(byte var6 : var1.digest()) {
         var9.append(String.format("%02x", var6 & 255));
      }

      return var9.toString();
   }

   private static void installAlias() {
      try {
         ClassLoader var0 = EpicACGRuntimeEngine.class.getClassLoader();
         Class var1 = Class.forName("net.minecraftforge.fml.loading.FMLLoader", false, var0);
         Object var2 = var1.getMethod("versionInfo").invoke((Object)null);
         if (var2 == null) {
            throw new IllegalStateException("FMLLoader.versionInfo null");
         } else {
            Class var3 = Class.forName("net.minecraftforge.fml.loading.VersionSupportMatrix", true, var0);
            Field var4 = var3.getDeclaredField("overrideVersions");
            Object var5 = unsafeGetStatic(var4);
            if (!(var5 instanceof Map)) {
               throw new IllegalStateException("overrideVersions not Map");
            } else {
               Map var6 = (Map)var5;
               Class var7 = Class.forName("org.apache.maven.artifact.versioning.DefaultArtifactVersion", true, var0);
               Object var8 = var7.getConstructor(String.class).newInstance("20.12.5");
               List var9 = (List)var6.computeIfAbsent("mod.epicfight", (var0x) -> new ArrayList());
               boolean var10 = false;

               for(Object var12 : var9) {
                  if ("20.12.5".equals(String.valueOf(var12))) {
                     var10 = true;
                     break;
                  }
               }

               if (!var10) {
                  var9.add(var8);
               }

               System.out.println("[EpicACG Runtime Engine 2.2.24 SAFE] Installed in-memory dependency alias only.");
            }
         }
      } catch (Throwable var13) {
         throw new RuntimeException("Unable to install ACG dependency alias; no files were modified", var13);
      }
   }

   private static Object unsafe() throws Exception {
      Class var0 = Class.forName("sun.misc.Unsafe");
      Field var1 = var0.getDeclaredField("theUnsafe");
      var1.setAccessible(true);
      return var1.get((Object)null);
   }

   private static Object unsafeGetStatic(Field var0) throws Exception {
      Object var1 = unsafe();
      Class var2 = var1.getClass();
      Object var3 = var2.getMethod("staticFieldBase", Field.class).invoke(var1, var0);
      long var4 = ((Number)var2.getMethod("staticFieldOffset", Field.class).invoke(var1, var0)).longValue();
      return var2.getMethod("getObjectVolatile", Object.class, Long.TYPE).invoke(var1, var3, var4);
   }

   static {
      PAYLOADS.put("com.dfdyz.epicacg.EpicACG", "bridge_payload/classes/com/dfdyz/epicacg/EpicACG.class");
      PAYLOADS.put("com.dfdyz.epicacg.efmextra.anims.FallAtkStartAnim", "bridge_payload/classes/com/dfdyz/epicacg/efmextra/anims/FallAtkStartAnim.class");
      PAYLOADS.put("com.dfdyz.epicacg.efmextra.skills.GenShinInternal.GSBowInternal", "bridge_payload/classes/com/dfdyz/epicacg/efmextra/skills/GenShinInternal/GSBowInternal.class");
      PAYLOADS.put("com.dfdyz.epicacg.efmextra.skills.SAO.RapierSpicialAttackSkill", "bridge_payload/classes/com/dfdyz/epicacg/efmextra/skills/SAO/RapierSpicialAttackSkill.class");
      PAYLOADS.put("com.dfdyz.epicacg.efmextra.skills.SAO.SingleSwordSASkills", "bridge_payload/classes/com/dfdyz/epicacg/efmextra/skills/SAO/SingleSwordSASkills.class");
      PAYLOADS.put("com.dfdyz.epicacg.efmextra.weapon.GenShinBow", "bridge_payload/classes/com/dfdyz/epicacg/efmextra/weapon/GenShinBow.class");
      PAYLOADS.put("com.dfdyz.epicacg.network.Netmgr", "bridge_payload/classes/com/dfdyz/epicacg/network/Netmgr.class");
      PAYLOADS.put("com.dfdyz.epicacg.registry.MyAnimations", "bridge_payload/classes/com/dfdyz/epicacg/registry/MyAnimations.class");
      PAYLOADS.put("com.dfdyz.epicacg.registry.MySkills", "bridge_payload/classes/com/dfdyz/epicacg/registry/MySkills.class");
      PAYLOADS.put("com.dfdyz.epicacg.registry.Tab", "bridge_payload/classes/com/dfdyz/epicacg/registry/Tab.class");
      PAYLOADS.put("com.dfdyz.epicacg.registry.WeaponTypes$EpicACGWeaponCategories", "bridge_payload/classes/com/dfdyz/epicacg/registry/WeaponTypes$EpicACGWeaponCategories.class");
      PAYLOADS.put("com.dfdyz.epicacg.registry.WeaponTypes", "bridge_payload/classes/com/dfdyz/epicacg/registry/WeaponTypes.class");
      PAYLOADS.put("com.dfdyz.epicacg.efmextra.skills.EpicACGSkillSlot", "bridge_payload/classes/com/dfdyz/epicacg/efmextra/skills/EpicACGSkillSlot.class");
      PAYLOADS.put("com.dfdyz.epicacg.event.ControllerEvent", "bridge_payload/classes/com/dfdyz/epicacg/event/ControllerEvent.class");
      PAYLOADS.put("com.dfdyz.epicacg.registry.KeyMappings", "bridge_payload/classes/com/dfdyz/epicacg/registry/KeyMappings.class");
      PAYLOADS.put("yesman.epicfight.skill.SkillContainer", "bridge_payload/classes/yesman/epicfight/skill/SkillContainer.class");
      PAYLOADS.put("com.dfdyz.epicacg.registry.Items", "bridge_payload/classes/com/dfdyz/epicacg/registry/Items.class");
      PAYLOADS.put("com.dfdyz.epicacg.world.item.SimpleWeaponItem", "bridge_payload/classes/com/dfdyz/epicacg/world/item/SimpleWeaponItem.class");
      PAYLOADS.put("com.dfdyz.epicacg.world.item.SpecialWeaponItem", "bridge_payload/classes/com/dfdyz/epicacg/world/item/SpecialWeaponItem.class");
      PAYLOADS.put("net.minecraftforge.event.AddPackFindersEvent", "bridge_payload/classes/net/minecraftforge/event/AddPackFindersEvent.class");
   }

   private static final class FullCompatTransformer implements ITransformer<ClassNode> {
      public ClassNode transform(ClassNode var1, ITransformerVotingContext var2) {
         String var3 = var1.name.replace('/', '.');
         String var4 = (String)EpicACGRuntimeEngine.PAYLOADS.get(var3);
         if (var4 == null) {
            return var1;
         } else {
            try {
               EpicACGRuntimeEngine.verifyStillClean("before runtime patch " + var3);
               ClassNode var5 = EpicACGRuntimeEngine.parseClass(EpicACGRuntimeEngine.readResource(var4));
               EpicACGRuntimeEngine.verifyStillClean("after runtime patch " + var3);
               System.out.println("[EpicACG Runtime Engine 2.2.8 SAFE] Runtime-only class patch: " + var3);
               return var5;
            } catch (Throwable var6) {
               throw new RuntimeException("Failed in-memory replacement for " + var3 + "; original ACG JAR was not modified", var6);
            }
         }
      }

      public TransformerVoteResult castVote(ITransformerVotingContext var1) {
         return TransformerVoteResult.YES;
      }

      public Set<ITransformer.Target<ClassNode>> targets() {
         HashSet var1 = new HashSet();

         for(String var3 : EpicACGRuntimeEngine.PAYLOADS.keySet()) {
            var1.add(Target.targetClass(var3));
         }

         return var1;
      }

      public TargetType<ClassNode> getTargetType() {
         return TargetType.CLASS;
      }
   }
}
