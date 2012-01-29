package buildcraft.builders;

import buildcraft.core.CoreProxy;
import forge.ITextureProvider;
import java.util.Properties;
import net.minecraft.server.BuildCraftCore;
import net.minecraft.server.Entity;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.LocaleLanguage;
import net.minecraft.server.ModLoader;
import net.minecraft.server.World;

public class ItemTemplate extends Item implements ITextureProvider
{

    public ItemTemplate(int var1)
    {
        super(var1);
        this.maxStackSize = 1;
        this.textureId = 32;
    }

    public String getItemNameIS(ItemStack var1)
    {
        if (var1.getData() == 0)
        {
            return this.b();
        }
        else
        {
            String var2 = this.b() + "#" + var1.getData();

            try
            {
                Properties var3 = (Properties)ModLoader.getPrivateValue(LocaleLanguage.class, LocaleLanguage.a(), 1);
                String var4 = var2 + ".name";
                if (var3.get(var4) == null)
                {
                    CoreProxy.addLocalization(var4, "Template #" + var1.getData());
                }
            }
            catch (IllegalArgumentException var5)
            {
                var5.printStackTrace();
            }
            catch (SecurityException var6)
            {
                var6.printStackTrace();
            }
            catch (NoSuchFieldException var7)
            {
                var7.printStackTrace();
            }

            return var2;
        }
    }

    public void a(ItemStack var1, World var2, Entity var3, int var4, boolean var5) {}

    public String getTextureFile()
    {
        return BuildCraftCore.customBuildCraftSprites;
    }
}
