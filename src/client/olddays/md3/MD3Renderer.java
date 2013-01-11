package net.minecraft.src;

import java.util.HashMap;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

public final class MD3Renderer {

   private MD3Model model;
   private boolean b = false;
   private int displayList;
   private boolean useAnimation = true;

   public MD3Renderer(MD3Model var1, boolean anim) {
      model = var1;
      displayList = 0;
      useAnimation = anim;
   }

   public final int getAnimFrames() {
      return model.animFrames;
   }

   public final void renderFrame(int var1, int var2, float var3) {
       if(displayList == 0 || useAnimation) {
            if (!useAnimation){
               displayList = GL11.glGenLists(/*model.animFrames*/1);
            }

//          for(int frame = 0; frame < model.animFrames; ++frame) {
            GL11.glEnableClientState('\u8074');
            GL11.glEnableClientState('\u8078');
            GL11.glEnableClientState('\u8075');
            if (!useAnimation){
               GL11.glNewList(displayList/* + frame*/, 4864);
            }

            for(int i = 0; i < model.surfaces.length; ++i) {
               MD3Surface surface = model.surfaces[i];
               if (useAnimation){
                  surface.setFrame(var1, var2, var3);
               }else{
                  surface.setFrame(0, 0, 0.0F);
               }
//                surface.vertices.position(0);
               surface.triangles.position(0);
//                surface.normals.position(0);
               surface.d.position(0);
               GL11.glVertexPointer(3, 0, surface.vertices);
               GL11.glNormalPointer(0, surface.normals);
               GL11.glTexCoordPointer(2, 0, surface.d);
               GL11.glDrawElements(4, surface.triangles);
            }

            if (!useAnimation){
               GL11.glEndList();
            }
            GL11.glDisableClientState('\u8074');
            GL11.glDisableClientState('\u8078');
            GL11.glDisableClientState('\u8075');
//          }
       }
       if (!useAnimation){
           GL11.glCallList(displayList/* + var1*/);
       }
   }
}
