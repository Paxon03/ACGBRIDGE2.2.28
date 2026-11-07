package com.paxon03.epicacgruntime.compat;

import java.lang.reflect.Method;

public final class NetworkSafety {
   private NetworkSafety() {
   }

   public static boolean canSendToServer() {
      try {
         Class var0 = Class.forName("net.minecraft.client.Minecraft");

         Method var1;
         try {
            var1 = var0.getMethod("m_91087_");
         } catch (NoSuchMethodException var6) {
            var1 = var0.getMethod("getInstance");
         }

         Object var2 = var1.invoke((Object)null);
         if (var2 == null) {
            return false;
         } else {
            Method var3;
            try {
               var3 = var0.getMethod("m_91403_");
            } catch (NoSuchMethodException var5) {
               var3 = var0.getMethod("getConnection");
            }

            return var3.invoke(var2) != null;
         }
      } catch (Throwable var7) {
         return false;
      }
   }
}
