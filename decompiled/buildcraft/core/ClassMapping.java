package buildcraft.core;

import buildcraft.api.TileNetworkData;
import buildcraft.core.ByteBuffer;
import buildcraft.core.ClassMapping.Indexes;
import buildcraft.core.ClassMapping.Reporter;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.TreeMap;
import net.minecraft.server.BuildCraftCore;

public class ClassMapping
{
	public static class Reporter {
		Class <? extends Object> clas;
		int occurences = 0;
		int dataInt = 0;
		int dataFloat = 0;
		int dataString = 0;
		int bytes = 0;

		public String toString () {
			String res = clas + ": " + occurences + " times (" + dataInt + ", "
					+ dataFloat + ", " + dataString + " = " + bytes + ")";

			return res;
		}
	}

    private static TreeMap<String, Reporter> report = new TreeMap();
    private LinkedList<Field> floatFields = new LinkedList();
    private LinkedList<Field> doubleFields = new LinkedList();
    private LinkedList<Field> stringFields = new LinkedList();
    private LinkedList<Field> shortFields = new LinkedList();
    private LinkedList<Field> intFields = new LinkedList();
    private LinkedList<Field> booleanFields = new LinkedList();
    private LinkedList<Field> enumFields = new LinkedList();
    private LinkedList<Field> unsignedByteFields = new LinkedList();
    private LinkedList<ClassMapping> objectFields = new LinkedList();
    private LinkedList<Field> doubleArrayFields = new LinkedList();
    private LinkedList<Field> shortArrayFields = new LinkedList();
    private LinkedList<Field> intArrayFields = new LinkedList();
    private LinkedList<Field> booleanArrayFields = new LinkedList();
    private LinkedList<Field> unsignedByteArrayFields = new LinkedList();
    private LinkedList<ClassMapping> objectArrayFields = new LinkedList();
    private int sizeBytes;
    private int sizeFloat;
    private int sizeString;
    private Field field;
    private Class <? extends Object > clas;

	public static class Indexes {
		public Indexes (int initFloat, int initString) {
			floatIndex = initFloat;
			stringIndex = initString;
		}

		int floatIndex = 0;
		int stringIndex = 0;
	}


    public static int report()
    {
        int var0 = 0;

        Reporter var2;
        for (Iterator var1 = report.values().iterator(); var1.hasNext(); var0 += var2.bytes)
        {
            var2 = (Reporter)var1.next();
            System.out.println(var2);
        }

        report.clear();
        return var0;
    }

    public ClassMapping(Class <? extends Object > var1)
    {
        this.clas = var1;
        Field[] var2 = var1.getFields();

        try
        {
            Field[] var3 = var2;
            int var4 = var2.length;

            for (int var5 = 0; var5 < var4; ++var5)
            {
                Field var6 = var3[var5];
                if (this.isSynchronizedField(var6))
                {
                    Type var7 = var6.getGenericType();
                    if (var7 instanceof Class && !((Class)var7).isArray())
                    {
                        Class var8 = (Class)var7;
                        if (var8.equals(Short.TYPE))
                        {
                            this.sizeBytes += 2;
                            this.shortFields.add(var6);
                        }
                        else if (var8.equals(Integer.TYPE))
                        {
                            TileNetworkData var9 = (TileNetworkData)var6.getAnnotation(TileNetworkData.class);
                            if (var9.intKind() == 1)
                            {
                                ++this.sizeBytes;
                                this.unsignedByteFields.add(var6);
                            }
                            else
                            {
                                this.sizeBytes += 4;
                                this.intFields.add(var6);
                            }
                        }
                        else if (var8.equals(Boolean.TYPE))
                        {
                            ++this.sizeBytes;
                            this.booleanFields.add(var6);
                        }
                        else if (Enum.class.isAssignableFrom(var8))
                        {
                            ++this.sizeBytes;
                            this.enumFields.add(var6);
                        }
                        else if (var8.equals(String.class))
                        {
                            ++this.sizeString;
                            this.stringFields.add(var6);
                        }
                        else if (var8.equals(Float.TYPE))
                        {
                            ++this.sizeFloat;
                            this.floatFields.add(var6);
                        }
                        else if (var8.equals(Double.TYPE))
                        {
                            ++this.sizeFloat;
                            this.doubleFields.add(var6);
                        }
                        else
                        {
                            ClassMapping var14 = new ClassMapping(var8);
                            var14.field = var6;
                            this.objectFields.add(var14);
                            ++this.sizeBytes;
                            this.sizeBytes += var14.sizeBytes;
                            this.sizeFloat += var14.sizeFloat;
                            this.sizeString += var14.sizeString;
                        }
                    }

                    if (var7 instanceof Class && ((Class)var7).isArray())
                    {
                        TileNetworkData var13 = (TileNetworkData)var6.getAnnotation(TileNetworkData.class);
                        if (var13.staticSize() == -1)
                        {
                            throw new RuntimeException("arrays must be provided with an explicit size");
                        }

                        Class var15 = (Class)var7;
                        Class var10 = var15.getComponentType();
                        if (var10.equals(Double.TYPE))
                        {
                            this.sizeFloat += var13.staticSize();
                            this.doubleArrayFields.add(var6);
                        }
                        else if (var10.equals(Short.TYPE))
                        {
                            this.sizeBytes += var13.staticSize() * 2;
                            this.shortArrayFields.add(var6);
                        }
                        else if (var10.equals(Integer.TYPE))
                        {
                            var13 = (TileNetworkData)var6.getAnnotation(TileNetworkData.class);
                            if (var13.intKind() == 1)
                            {
                                this.sizeBytes += var13.staticSize();
                                this.unsignedByteArrayFields.add(var6);
                            }
                            else
                            {
                                this.sizeBytes += var13.staticSize() * 4;
                                this.intArrayFields.add(var6);
                            }
                        }
                        else if (var10.equals(Boolean.TYPE))
                        {
                            this.sizeBytes += var13.staticSize();
                            this.booleanArrayFields.add(var6);
                        }
                        else
                        {
                            ClassMapping var11 = new ClassMapping(var10);
                            var11.field = var6;
                            this.objectArrayFields.add(var11);
                            this.sizeBytes += var13.staticSize();
                            this.sizeBytes += var13.staticSize() * var11.sizeBytes;
                            this.sizeFloat += var13.staticSize() * var11.sizeFloat;
                            this.sizeString += var13.staticSize() * var11.sizeString;
                        }
                    }
                }
            }
        }
        catch (IllegalArgumentException var12)
        {
            var12.printStackTrace();
        }

    }

    private boolean isSynchronizedField(Field var1)
    {
        TileNetworkData var2 = (TileNetworkData)var1.getAnnotation(TileNetworkData.class);
        return var2 != null;
    }

    public void setData(Object var1, ByteBuffer var2, float[] var3, String[] var4, Indexes var5) throws IllegalArgumentException, IllegalAccessException
    {
        Reporter var6 = null;
        if (BuildCraftCore.trackNetworkUsage)
        {
            if (!report.containsKey(this.clas.getName()))
            {
                report.put(this.clas.getName(), new Reporter());
            }

            var6 = (Reporter)report.get(this.clas.getName());
            var6.clas = this.clas;
        }
        else
        {
            var6 = new Reporter();
        }

        ++var6.occurences;

        Iterator var7;
        Field var8;
        for (var7 = this.shortFields.iterator(); var7.hasNext(); ++var6.dataInt)
        {
            var8 = (Field)var7.next();
            var2.writeShort(var8.getShort(var1));
            var6.bytes += 2;
        }

        for (var7 = this.intFields.iterator(); var7.hasNext(); ++var6.dataInt)
        {
            var8 = (Field)var7.next();
            var2.writeInt(var8.getInt(var1));
            var6.bytes += 4;
        }

        for (var7 = this.booleanFields.iterator(); var7.hasNext(); ++var6.dataInt)
        {
            var8 = (Field)var7.next();
            var2.writeUnsignedByte(var8.getBoolean(var1) ? 1 : 0);
            ++var6.bytes;
        }

        for (var7 = this.enumFields.iterator(); var7.hasNext(); ++var6.dataInt)
        {
            var8 = (Field)var7.next();
            var2.writeUnsignedByte(((Enum)var8.get(var1)).ordinal());
            ++var6.bytes;
        }

        for (var7 = this.unsignedByteFields.iterator(); var7.hasNext(); ++var6.dataInt)
        {
            var8 = (Field)var7.next();
            var2.writeUnsignedByte(var8.getInt(var1));
            ++var6.bytes;
        }

        for (var7 = this.floatFields.iterator(); var7.hasNext(); ++var6.dataFloat)
        {
            var8 = (Field)var7.next();
            var3[var5.floatIndex] = var8.getFloat(var1);
            ++var5.floatIndex;
            var6.bytes += 4;
        }

        for (var7 = this.doubleFields.iterator(); var7.hasNext(); ++var6.dataFloat)
        {
            var8 = (Field)var7.next();
            var3[var5.floatIndex] = (float)var8.getDouble(var1);
            ++var5.floatIndex;
            var6.bytes += 4;
        }

        for (var7 = this.stringFields.iterator(); var7.hasNext(); ++var6.dataString)
        {
            var8 = (Field)var7.next();
            var4[var5.stringIndex] = (String)var8.get(var1);
            ++var5.stringIndex;
            var6.bytes += var4[var5.stringIndex].length();
        }

        var7 = this.objectFields.iterator();

        int var10;
        ClassMapping var14;
        while (var7.hasNext())
        {
            var14 = (ClassMapping)var7.next();
            Object var9 = var14.field.get(var1);
            if (var9 == null)
            {
                var2.writeUnsignedByte(0);

                for (var10 = 0; var10 < var14.sizeBytes; ++var10)
                {
                    var2.writeUnsignedByte(0);
                    ++var6.bytes;
                    ++var6.dataInt;
                }

                var5.floatIndex += var14.sizeFloat;
                var5.stringIndex += var14.sizeString;
            }
            else
            {
                var2.writeUnsignedByte(1);
                ++var6.bytes;
                ++var6.dataInt;
                var14.setData(var9, var2, var3, var4, var5);
            }
        }

        var7 = this.doubleArrayFields.iterator();

        TileNetworkData var13;
        while (var7.hasNext())
        {
            var8 = (Field)var7.next();
            var13 = (TileNetworkData)var8.getAnnotation(TileNetworkData.class);

            for (var10 = 0; var10 < var13.staticSize(); ++var10)
            {
                var3[var5.floatIndex] = (float)((double[])((double[])var8.get(var1)))[var10];
                ++var5.floatIndex;
                var6.bytes += 4;
                ++var6.dataFloat;
            }
        }

        var7 = this.shortArrayFields.iterator();

        while (var7.hasNext())
        {
            var8 = (Field)var7.next();
            var13 = (TileNetworkData)var8.getAnnotation(TileNetworkData.class);

            for (var10 = 0; var10 < var13.staticSize(); ++var10)
            {
                var2.writeShort(((short[])((short[])var8.get(var1)))[var10]);
                var6.bytes += 2;
                ++var6.dataInt;
            }
        }

        var7 = this.intArrayFields.iterator();

        while (var7.hasNext())
        {
            var8 = (Field)var7.next();
            var13 = (TileNetworkData)var8.getAnnotation(TileNetworkData.class);

            for (var10 = 0; var10 < var13.staticSize(); ++var10)
            {
                var2.writeInt(((int[])((int[])var8.get(var1)))[var10]);
                var6.bytes += 4;
                ++var6.dataInt;
            }
        }

        var7 = this.booleanArrayFields.iterator();

        while (var7.hasNext())
        {
            var8 = (Field)var7.next();
            var13 = (TileNetworkData)var8.getAnnotation(TileNetworkData.class);

            for (var10 = 0; var10 < var13.staticSize(); ++var10)
            {
                var2.writeUnsignedByte(((boolean[])((boolean[])var8.get(var1)))[var10] ? 1 : 0);
                ++var6.bytes;
                ++var6.dataInt;
            }
        }

        var7 = this.unsignedByteFields.iterator();

        while (var7.hasNext())
        {
            var8 = (Field)var7.next();
            var13 = (TileNetworkData)var8.getAnnotation(TileNetworkData.class);

            for (var10 = 0; var10 < var13.staticSize(); ++var10)
            {
                var2.writeUnsignedByte(((int[])((int[])var8.get(var1)))[var10]);
                ++var6.bytes;
                ++var6.dataInt;
            }
        }

        var7 = this.objectArrayFields.iterator();

        while (var7.hasNext())
        {
            var14 = (ClassMapping)var7.next();
            var13 = (TileNetworkData)var14.field.getAnnotation(TileNetworkData.class);
            Object[] var15 = (Object[])((Object[])var14.field.get(var1));

            for (int var11 = 0; var11 < var13.staticSize(); ++var11)
            {
                if (var15[var11] == null)
                {
                    var2.writeUnsignedByte(0);

                    for (int var12 = 0; var12 < var14.sizeBytes; ++var12)
                    {
                        var2.writeUnsignedByte(0);
                        ++var6.bytes;
                        ++var6.dataInt;
                    }

                    var5.floatIndex += var14.sizeFloat;
                    var5.stringIndex += var14.sizeString;
                }
                else
                {
                    var2.writeUnsignedByte(1);
                    ++var6.bytes;
                    ++var6.dataInt;
                    var14.setData(var15[var11], var2, var3, var4, var5);
                }
            }
        }

    }

    public void updateFromData(Object var1, ByteBuffer var2, float[] var3, String[] var4, Indexes var5) throws IllegalArgumentException, IllegalAccessException
    {
        Reporter var6 = null;
        if (BuildCraftCore.trackNetworkUsage)
        {
            if (!report.containsKey(this.clas.getName()))
            {
                report.put(this.clas.getName(), new Reporter());
            }

            var6 = (Reporter)report.get(this.clas.getName());
            var6.clas = this.clas;
        }
        else
        {
            var6 = new Reporter();
        }

        ++var6.occurences;

        Iterator var7;
        Field var8;
        for (var7 = this.shortFields.iterator(); var7.hasNext(); ++var6.dataInt)
        {
            var8 = (Field)var7.next();
            var8.setShort(var1, var2.readShort());
            var6.bytes += 2;
        }

        for (var7 = this.intFields.iterator(); var7.hasNext(); ++var6.dataInt)
        {
            var8 = (Field)var7.next();
            var8.setInt(var1, var2.readInt());
            var6.bytes += 4;
        }

        for (var7 = this.booleanFields.iterator(); var7.hasNext(); ++var6.dataInt)
        {
            var8 = (Field)var7.next();
            var8.setBoolean(var1, var2.readUnsignedByte() == 1);
            ++var6.bytes;
        }

        for (var7 = this.enumFields.iterator(); var7.hasNext(); ++var6.dataInt)
        {
            var8 = (Field)var7.next();
            var8.set(var1, ((Class)var8.getGenericType()).getEnumConstants()[var2.readUnsignedByte()]);
            ++var6.bytes;
        }

        for (var7 = this.unsignedByteFields.iterator(); var7.hasNext(); ++var6.dataInt)
        {
            var8 = (Field)var7.next();
            var8.setInt(var1, var2.readUnsignedByte());
            ++var6.bytes;
        }

        for (var7 = this.floatFields.iterator(); var7.hasNext(); ++var6.dataFloat)
        {
            var8 = (Field)var7.next();
            var8.setFloat(var1, var3[var5.floatIndex]);
            ++var5.floatIndex;
            var6.bytes += 4;
        }

        for (var7 = this.doubleFields.iterator(); var7.hasNext(); ++var6.dataFloat)
        {
            var8 = (Field)var7.next();
            var8.setDouble(var1, (double)var3[var5.floatIndex]);
            ++var5.floatIndex;
            var6.bytes += 4;
        }

        for (var7 = this.stringFields.iterator(); var7.hasNext(); ++var6.dataString)
        {
            var8 = (Field)var7.next();
            var8.set(var1, var4[var5.stringIndex]);
            ++var5.stringIndex;
            var6.bytes += var4[var5.stringIndex].length();
        }

        var7 = this.objectFields.iterator();

        int var10;
        ClassMapping var15;
        while (var7.hasNext())
        {
            var15 = (ClassMapping)var7.next();
            boolean var9 = var2.readUnsignedByte() == 0;
            ++var6.bytes;
            ++var6.dataInt;
            if (!var9)
            {
                var15.updateFromData(var15.field.get(var1), var2, var3, var4, var5);
            }
            else
            {
                for (var10 = 0; var10 < var15.sizeBytes; ++var10)
                {
                    var2.readUnsignedByte();
                }

                var5.floatIndex += var15.sizeFloat;
                var5.stringIndex += var15.sizeString;
            }
        }

        var7 = this.doubleArrayFields.iterator();

        TileNetworkData var14;
        while (var7.hasNext())
        {
            var8 = (Field)var7.next();
            var14 = (TileNetworkData)var8.getAnnotation(TileNetworkData.class);

            for (var10 = 0; var10 < var14.staticSize(); ++var10)
            {
                ((double[])((double[])var8.get(var1)))[var10] = (double)var3[var5.floatIndex];
                ++var5.floatIndex;
                var6.bytes += 4;
                ++var6.dataFloat;
            }
        }

        var7 = this.shortArrayFields.iterator();

        while (var7.hasNext())
        {
            var8 = (Field)var7.next();
            var14 = (TileNetworkData)var8.getAnnotation(TileNetworkData.class);

            for (var10 = 0; var10 < var14.staticSize(); ++var10)
            {
                ((short[])((short[])var8.get(var1)))[var10] = var2.readShort();
                var6.bytes += 2;
                ++var6.dataInt;
            }
        }

        var7 = this.intArrayFields.iterator();

        while (var7.hasNext())
        {
            var8 = (Field)var7.next();
            var14 = (TileNetworkData)var8.getAnnotation(TileNetworkData.class);

            for (var10 = 0; var10 < var14.staticSize(); ++var10)
            {
                ((int[])((int[])var8.get(var1)))[var10] = var2.readInt();
                var6.bytes += 4;
                ++var6.dataInt;
            }
        }

        var7 = this.booleanArrayFields.iterator();

        while (var7.hasNext())
        {
            var8 = (Field)var7.next();
            var14 = (TileNetworkData)var8.getAnnotation(TileNetworkData.class);

            for (var10 = 0; var10 < var14.staticSize(); ++var10)
            {
                ((boolean[])((boolean[])var8.get(var1)))[var10] = var2.readUnsignedByte() == 1;
                ++var6.bytes;
                ++var6.dataInt;
            }
        }

        var7 = this.unsignedByteArrayFields.iterator();

        while (var7.hasNext())
        {
            var8 = (Field)var7.next();
            var14 = (TileNetworkData)var8.getAnnotation(TileNetworkData.class);

            for (var10 = 0; var10 < var14.staticSize(); ++var10)
            {
                ((int[])((int[])var8.get(var1)))[var10] = var2.readUnsignedByte();
                ++var6.bytes;
                ++var6.dataInt;
            }
        }

        var7 = this.objectArrayFields.iterator();

        while (var7.hasNext())
        {
            var15 = (ClassMapping)var7.next();
            var14 = (TileNetworkData)var15.field.getAnnotation(TileNetworkData.class);
            Object[] var16 = (Object[])((Object[])var15.field.get(var1));

            for (int var11 = 0; var11 < var14.staticSize(); ++var11)
            {
                boolean var12 = var2.readUnsignedByte() == 0;
                ++var6.bytes;
                ++var6.dataInt;
                if (var12)
                {
                    for (int var13 = 0; var13 < var15.sizeBytes; ++var13)
                    {
                        var2.readUnsignedByte();
                    }

                    var5.floatIndex += var15.sizeFloat;
                    var5.stringIndex += var15.sizeString;
                }
                else
                {
                    var15.updateFromData(var16[var11], var2, var3, var4, var5);
                }
            }
        }

    }

    public int[] getSize()
    {
        int[] var1 = new int[] {this.sizeBytes, this.sizeFloat, this.sizeString};
        return var1;
    }

}
