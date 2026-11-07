package com.paxon03.epicacgruntime.compat;

import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public final class EpicSkillsCompat {
   private static boolean attempted;

   private EpicSkillsCompat() {
   }

   public static synchronized void register() {
      if (!attempted) {
         attempted = true;

         try {
            ClassLoader var0 = Thread.currentThread().getContextClassLoader();

            Class var1;
            try {
               var1 = Class.forName("com.dfdyz.epicacg.compat.IrisCompat", false, var0);
            } catch (Throwable var15) {
               var1 = Class.forName("com.dfdyz.epicacg.compat.IrisCompat", false, EpicSkillsCompat.class.getClassLoader());
            }

            ClassLoader var2 = var1.getClassLoader();

            try {
               Class.forName("com.yesman.epicskills.client.gui.screen.CategorySlotTexture", false, var2);
            } catch (ClassNotFoundException var14) {
               System.out.println("[EpicACG Bridge 2.2.6] Epic Skills not present; skipping skill-tree category registration.");
               return;
            }

            String var3 = "com.dfdyz.epicacg.compat.EpicSkillsCategorySlotTextures";

            Class var4;
            try {
               var4 = Class.forName(var3, false, var2);
            } catch (ClassNotFoundException var17) {
               InputStream var7 = EpicSkillsCompat.class.getClassLoader().getResourceAsStream("bridge_payload/epicskills/com/dfdyz/epicacg/compat/EpicSkillsCategorySlotTextures.class");

               byte[] var6;
               try {
                  if (var7 == null) {
                     throw new IllegalStateException("Missing embedded Epic Skills category helper payload");
                  }

                  var6 = var7.readAllBytes();
               } catch (Throwable var16) {
                  if (var7 != null) {
                     try {
                        var7.close();
                     } catch (Throwable var13) {
                        var16.addSuppressed(var13);
                     }
                  }

                  throw var16;
               }

               if (var7 != null) {
                  var7.close();
               }

               MethodHandles.Lookup var20 = trustedLookup().in(var1);
               var4 = var20.defineClass(var6);
               System.out.println("[EpicACG Bridge 2.2.6] Defined Epic Skills category helper inside Epic ACG's runtime module only.");
            }

            Class var5 = Class.forName("com.yesman.epicskills.client.gui.screen.CategorySlotTexture", true, var2);
            Field var19 = var5.getField("ENUM_MANAGER");
            Object var21 = var19.get((Object)null);
            Method var8 = null;

            for(Method var12 : var21.getClass().getMethods()) {
               if (var12.getName().equals("registerEnumCls") && var12.getParameterCount() == 2) {
                  var8 = var12;
                  break;
               }
            }

            if (var8 == null) {
               throw new NoSuchMethodException("ExtendableEnumManager.registerEnumCls");
            }

            var8.invoke(var21, "epicacg", var4);
            System.out.println("[EpicACG Bridge 2.2.6] Epic Skills category registered: epicacg / SAO_SINGLE_SWORD");
         } catch (Throwable var18) {
            System.err.println("[EpicACG Bridge 2.2.6] Epic Skills integration skipped: " + String.valueOf(var18));
         }

      }
   }

   private static MethodHandles.Lookup trustedLookup() throws Exception {
      Field var0 = MethodHandles.Lookup.class.getDeclaredField("IMPL_LOOKUP");
      Object var1 = unsafe();
      Class var2 = var1.getClass();
      Object var3 = var2.getMethod("staticFieldBase", Field.class).invoke(var1, var0);
      long var4 = ((Number)var2.getMethod("staticFieldOffset", Field.class).invoke(var1, var0)).longValue();
      return (MethodHandles.Lookup)var2.getMethod("getObjectVolatile", Object.class, Long.TYPE).invoke(var1, var3, var4);
   }

   private static Object unsafe() throws Exception {
      Class var0 = Class.forName("sun.misc.Unsafe");
      Field var1 = var0.getDeclaredField("theUnsafe");
      var1.setAccessible(true);
      return var1.get((Object)null);
   }
}
