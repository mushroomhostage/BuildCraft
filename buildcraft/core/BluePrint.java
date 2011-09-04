package buildcraft.core;

import buildcraft.core.BlockContents;
import buildcraft.core.CoreProxy;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import net.minecraft.server.mod_BuildCraftCore;

public class BluePrint {

   File file;
   BlockContents[][][] contents;
   public int anchorX;
   public int anchorY;
   public int anchorZ;
   public int sizeX;
   public int sizeY;
   public int sizeZ;


   public BluePrint(File var1) {
      this.file = var1;
   }

   public BluePrint(BluePrint var1) {
      var1.loadIfNeeded();
      this.anchorX = var1.anchorX;
      this.anchorY = var1.anchorY;
      this.anchorZ = var1.anchorZ;
      this.sizeX = var1.sizeX;
      this.sizeY = var1.sizeY;
      this.sizeZ = var1.sizeZ;
      this.contents = new BlockContents[this.sizeX][this.sizeY][this.sizeZ];

      for(int var2 = 0; var2 < this.sizeX; ++var2) {
         for(int var3 = 0; var3 < this.sizeY; ++var3) {
            for(int var4 = 0; var4 < this.sizeZ; ++var4) {
               this.contents[var2][var3][var4] = var1.contents[var2][var3][var4];
            }
         }
      }

   }

   public BluePrint(int var1, int var2, int var3) {
      this.contents = new BlockContents[var1][var2][var3];
      this.sizeX = var1;
      this.sizeY = var2;
      this.sizeZ = var3;
      this.anchorX = 0;
      this.anchorY = 0;
      this.anchorZ = 0;
   }

   public void setBlockId(int var1, int var2, int var3, int var4) {
      if(this.contents[var1][var2][var3] == null) {
         this.contents[var1][var2][var3] = new BlockContents();
         this.contents[var1][var2][var3].x = var1;
         this.contents[var1][var2][var3].y = var2;
         this.contents[var1][var2][var3].z = var3;
      }

      this.contents[var1][var2][var3].blockId = var4;
   }

   public void rotateLeft() {
      this.loadIfNeeded();
      BlockContents[][][] var1 = new BlockContents[this.sizeZ][this.sizeY][this.sizeX];

      int var2;
      int var3;
      int var4;
      for(var2 = 0; var2 < this.sizeZ; ++var2) {
         for(var3 = 0; var3 < this.sizeY; ++var3) {
            for(var4 = 0; var4 < this.sizeX; ++var4) {
               var1[var2][var3][var4] = this.contents[var4][var3][this.sizeZ - 1 - var2];
            }
         }
      }

      var2 = this.sizeZ - 1 - this.anchorZ;
      var3 = this.anchorY;
      var4 = this.anchorX;
      this.contents = var1;
      int var5 = this.sizeX;
      this.sizeX = this.sizeZ;
      this.sizeZ = var5;
      this.anchorX = var2;
      this.anchorY = var3;
      this.anchorZ = var4;
   }

   public void save(int var1) {
      this.loadIfNeeded();

      try {
         File var2 = CoreProxy.getBuildCraftBase();
         var2.mkdir();
         File var3 = new File(CoreProxy.getBuildCraftBase(), "blueprints/" + var1 + ".bpt");
         if(!var3.exists()) {
            var3.createNewFile();
         }

         FileOutputStream var4 = new FileOutputStream(var3);
         BufferedWriter var5 = new BufferedWriter(new OutputStreamWriter(var4, "8859_1"));
         var5.write("version:" + mod_BuildCraftCore.version());
         var5.newLine();
         var5.write("kind:template");
         var5.newLine();
         var5.write("sizeX:" + this.sizeX);
         var5.newLine();
         var5.write("sizeY:" + this.sizeY);
         var5.newLine();
         var5.write("sizeZ:" + this.sizeZ);
         var5.newLine();
         var5.write("anchorX:" + this.anchorX);
         var5.newLine();
         var5.write("anchorY:" + this.anchorY);
         var5.newLine();
         var5.write("anchorZ:" + this.anchorZ);
         var5.newLine();
         var5.write("mask:");
         boolean var6 = true;

         for(int var7 = 0; var7 < this.sizeX; ++var7) {
            for(int var8 = 0; var8 < this.sizeY; ++var8) {
               for(int var9 = 0; var9 < this.sizeZ; ++var9) {
                  if(var6) {
                     var6 = false;
                  } else {
                     var5.write(",");
                  }

                  var5.write(this.contents[var7][var8][var9].blockId + "");
               }
            }
         }

         var5.newLine();
         var5.flush();
         var4.close();
      } catch (FileNotFoundException var10) {
         var10.printStackTrace();
      } catch (UnsupportedEncodingException var11) {
         var11.printStackTrace();
      } catch (IOException var12) {
         var12.printStackTrace();
      }

   }

   public void loadIfNeeded() {
      if(this.file != null) {
         try {
            FileInputStream var1 = new FileInputStream(this.file);
            BufferedReader var2 = new BufferedReader(new InputStreamReader(var1, "8859_1"));

            while(true) {
               String var3 = var2.readLine();
               if(var3 == null) {
                  break;
               }

               String[] var4 = var3.split(":");
               String var5 = var4[0];
               if(var5.equals("sizeX")) {
                  this.sizeX = Integer.parseInt(var4[1]);
               } else if(var5.equals("sizeY")) {
                  this.sizeY = Integer.parseInt(var4[1]);
               } else if(var5.equals("sizeZ")) {
                  this.sizeZ = Integer.parseInt(var4[1]);
               } else if(var5.equals("anchorX")) {
                  this.anchorX = Integer.parseInt(var4[1]);
               } else if(var5.equals("anchorY")) {
                  this.anchorY = Integer.parseInt(var4[1]);
               } else if(var5.equals("anchorZ")) {
                  this.anchorZ = Integer.parseInt(var4[1]);
               } else if(var5.equals("mask")) {
                  this.contents = new BlockContents[this.sizeX][this.sizeY][this.sizeZ];
                  String[] var6 = var4[1].split(",");
                  int var7 = 0;

                  for(int var8 = 0; var8 < this.sizeX; ++var8) {
                     for(int var9 = 0; var9 < this.sizeY; ++var9) {
                        for(int var10 = 0; var10 < this.sizeZ; ++var10) {
                           this.contents[var8][var9][var10] = new BlockContents();
                           this.contents[var8][var9][var10].x = var8;
                           this.contents[var8][var9][var10].y = var9;
                           this.contents[var8][var9][var10].z = var10;
                           this.contents[var8][var9][var10].blockId = Integer.parseInt(var6[var7]);
                           ++var7;
                        }
                     }
                  }
               }
            }
         } catch (UnsupportedEncodingException var11) {
            var11.printStackTrace();
         } catch (FileNotFoundException var12) {
            var12.printStackTrace();
         } catch (IOException var13) {
            var13.printStackTrace();
         }

         this.file = null;
      }
   }

   public boolean equals(Object var1) {
      this.loadIfNeeded();
      if(!(var1 instanceof BluePrint)) {
         return false;
      } else {
         BluePrint var2 = (BluePrint)var1;
         var2.loadIfNeeded();
         if(this.sizeX == var2.sizeX && this.sizeY == var2.sizeY && this.sizeZ == var2.sizeZ && this.anchorX == var2.anchorX && this.anchorY == var2.anchorY && this.anchorZ == var2.anchorZ) {
            for(int var3 = 0; var3 < this.contents.length; ++var3) {
               for(int var4 = 0; var4 < this.contents[0].length; ++var4) {
                  for(int var5 = 0; var5 < this.contents[0][0].length; ++var5) {
                     if(this.contents[var3][var4][var5] != null && var2.contents[var3][var4][var5] == null) {
                        return false;
                     }

                     if(this.contents[var3][var4][var5] == null && var2.contents[var3][var4][var5] != null) {
                        return false;
                     }

                     if(this.contents[var3][var4][var5].blockId != var2.contents[var3][var4][var5].blockId) {
                        return false;
                     }
                  }
               }
            }

            return true;
         } else {
            return false;
         }
      }
   }
}
