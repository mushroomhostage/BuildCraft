// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package buildcraft.core;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.LinkedList;

// Referenced classes of package buildcraft.core:
//            TileNetworkData

public class ClassMapping
{
    public static class Indexes
    {

        int intIndex;
        int floatIndex;
        int stringIndex;

        public Indexes(int i, int j, int k)
        {
            intIndex = 0;
            floatIndex = 0;
            stringIndex = 0;
            intIndex = i;
            floatIndex = j;
            stringIndex = k;
        }
    }


    public ClassMapping(Class class1)
    {
        floatFields = new LinkedList();
        doubleFields = new LinkedList();
        stringFields = new LinkedList();
        intFields = new LinkedList();
        booleanFields = new LinkedList();
        enumFields = new LinkedList();
        objectFields = new LinkedList();
        intArrayFields = new LinkedList();
        booleanArrayFields = new LinkedList();
        objectArrayFields = new LinkedList();
        Field afield[] = class1.getFields();
        try
        {
            Field afield1[] = afield;
            int i = afield1.length;
            for(int j = 0; j < i; j++)
            {
                Field field1 = afield1[j];
                if(isSynchronizedField(field1))
                {
                    Type type = field1.getGenericType();
                    if((type instanceof Class) && !((Class)type).isArray())
                    {
                        Class class2 = (Class)type;
                        if(class2.equals(Integer.TYPE))
                        {
                            sizeInt++;
                            intFields.add(field1);
                        } else
                        if(class2.equals(Boolean.TYPE))
                        {
                            sizeInt++;
                            booleanFields.add(field1);
                        } else
                        if((java.lang.Enum.class).isAssignableFrom(class2))
                        {
                            sizeInt++;
                            enumFields.add(field1);
                        } else
                        if(class2.equals(java.lang.String.class))
                        {
                            sizeString++;
                            stringFields.add(field1);
                        } else
                        if(class2.equals(Float.TYPE))
                        {
                            sizeFloat++;
                            floatFields.add(field1);
                        } else
                        if(class2.equals(Double.TYPE))
                        {
                            sizeFloat++;
                            doubleFields.add(field1);
                        } else
                        {
                            ClassMapping classmapping = new ClassMapping(class2);
                            classmapping.field = field1;
                            objectFields.add(classmapping);
                            sizeInt++;
                            sizeInt += classmapping.sizeInt;
                            sizeFloat += classmapping.sizeFloat;
                            sizeString += classmapping.sizeString;
                        }
                    }
                    if((type instanceof Class) && ((Class)type).isArray())
                    {
                        TileNetworkData tilenetworkdata = (TileNetworkData)field1.getAnnotation(buildcraft.core.TileNetworkData.class);
                        if(tilenetworkdata.staticSize() == -1)
                        {
                            throw new RuntimeException("arrays must be provided with an explicit size");
                        }
                        Class class3 = (Class)type;
                        Class class4 = class3.getComponentType();
                        if(class4.equals(Integer.TYPE))
                        {
                            sizeInt += tilenetworkdata.staticSize();
                            intArrayFields.add(field1);
                        } else
                        if(class4.equals(Boolean.TYPE))
                        {
                            sizeInt += tilenetworkdata.staticSize();
                            booleanArrayFields.add(field1);
                        } else
                        {
                            ClassMapping classmapping1 = new ClassMapping(class4);
                            classmapping1.field = field1;
                            objectArrayFields.add(classmapping1);
                            sizeInt += tilenetworkdata.staticSize();
                            sizeInt += tilenetworkdata.staticSize() * classmapping1.sizeInt;
                            sizeFloat += tilenetworkdata.staticSize() * classmapping1.sizeFloat;
                            sizeString += tilenetworkdata.staticSize() * classmapping1.sizeString;
                        }
                    }
                }
            }

        }
        catch(IllegalArgumentException illegalargumentexception)
        {
            illegalargumentexception.printStackTrace();
        }
    }

    private boolean isSynchronizedField(Field field1)
    {
        TileNetworkData tilenetworkdata = (TileNetworkData)field1.getAnnotation(buildcraft.core.TileNetworkData.class);
        return tilenetworkdata != null;
    }

    public void setData(Object obj, int ai[], float af[], String as[], Indexes indexes)
        throws IllegalArgumentException, IllegalAccessException
    {
        for(Iterator iterator = intFields.iterator(); iterator.hasNext();)
        {
            Field field1 = (Field)iterator.next();
            ai[indexes.intIndex] = field1.getInt(obj);
            indexes.intIndex++;
        }

        for(Iterator iterator1 = booleanFields.iterator(); iterator1.hasNext();)
        {
            Field field2 = (Field)iterator1.next();
            ai[indexes.intIndex] = field2.getBoolean(obj) ? 1 : 0;
            indexes.intIndex++;
        }

        for(Iterator iterator2 = enumFields.iterator(); iterator2.hasNext();)
        {
            Field field3 = (Field)iterator2.next();
            ai[indexes.intIndex] = ((Enum)field3.get(obj)).ordinal();
            indexes.intIndex++;
        }

        for(Iterator iterator3 = floatFields.iterator(); iterator3.hasNext();)
        {
            Field field4 = (Field)iterator3.next();
            af[indexes.floatIndex] = field4.getFloat(obj);
            indexes.floatIndex++;
        }

        for(Iterator iterator4 = doubleFields.iterator(); iterator4.hasNext();)
        {
            Field field5 = (Field)iterator4.next();
            af[indexes.floatIndex] = (float)field5.getDouble(obj);
            indexes.floatIndex++;
        }

        for(Iterator iterator5 = stringFields.iterator(); iterator5.hasNext();)
        {
            Field field6 = (Field)iterator5.next();
            as[indexes.stringIndex] = (String)field6.get(obj);
            indexes.stringIndex++;
        }

        for(Iterator iterator6 = objectFields.iterator(); iterator6.hasNext();)
        {
            ClassMapping classmapping = (ClassMapping)iterator6.next();
            Object obj1 = classmapping.field.get(obj);
            if(obj1 == null)
            {
                ai[indexes.intIndex] = 0;
                indexes.intIndex++;
                indexes.intIndex += classmapping.sizeInt;
                indexes.floatIndex += classmapping.sizeFloat;
                indexes.stringIndex += classmapping.sizeString;
            } else
            {
                ai[indexes.intIndex] = 1;
                indexes.intIndex++;
                classmapping.setData(obj1, ai, af, as, indexes);
            }
        }

        for(Iterator iterator7 = intArrayFields.iterator(); iterator7.hasNext();)
        {
            Field field7 = (Field)iterator7.next();
            TileNetworkData tilenetworkdata = (TileNetworkData)field7.getAnnotation(buildcraft.core.TileNetworkData.class);
            int i = 0;
            while(i < tilenetworkdata.staticSize()) 
            {
                ai[indexes.intIndex] = ((int[])field7.get(obj))[i];
                indexes.intIndex++;
                i++;
            }
        }

        for(Iterator iterator8 = booleanArrayFields.iterator(); iterator8.hasNext();)
        {
            Field field8 = (Field)iterator8.next();
            TileNetworkData tilenetworkdata1 = (TileNetworkData)field8.getAnnotation(buildcraft.core.TileNetworkData.class);
            int j = 0;
            while(j < tilenetworkdata1.staticSize()) 
            {
                ai[indexes.intIndex] = ((boolean[])field8.get(obj))[j] ? 1 : 0;
                indexes.intIndex++;
                j++;
            }
        }

        for(Iterator iterator9 = objectArrayFields.iterator(); iterator9.hasNext();)
        {
            ClassMapping classmapping1 = (ClassMapping)iterator9.next();
            TileNetworkData tilenetworkdata2 = (TileNetworkData)classmapping1.field.getAnnotation(buildcraft.core.TileNetworkData.class);
            Object aobj[] = (Object[])classmapping1.field.get(obj);
            int k = 0;
            while(k < tilenetworkdata2.staticSize()) 
            {
                if(aobj[k] == null)
                {
                    ai[indexes.intIndex] = 0;
                    indexes.intIndex++;
                    indexes.intIndex += classmapping1.sizeInt;
                    indexes.floatIndex += classmapping1.sizeFloat;
                    indexes.stringIndex += classmapping1.sizeString;
                } else
                {
                    ai[indexes.intIndex] = 1;
                    indexes.intIndex++;
                    classmapping1.setData(aobj[k], ai, af, as, indexes);
                }
                k++;
            }
        }

    }

    public void updateFromData(Object obj, int ai[], float af[], String as[], Indexes indexes)
        throws IllegalArgumentException, IllegalAccessException
    {
        for(Iterator iterator = intFields.iterator(); iterator.hasNext();)
        {
            Field field1 = (Field)iterator.next();
            field1.setInt(obj, ai[indexes.intIndex]);
            indexes.intIndex++;
        }

        for(Iterator iterator1 = booleanFields.iterator(); iterator1.hasNext();)
        {
            Field field2 = (Field)iterator1.next();
            field2.setBoolean(obj, ai[indexes.intIndex] == 1);
            indexes.intIndex++;
        }

        for(Iterator iterator2 = enumFields.iterator(); iterator2.hasNext();)
        {
            Field field3 = (Field)iterator2.next();
            field3.set(obj, ((Class)field3.getGenericType()).getEnumConstants()[ai[indexes.intIndex]]);
            indexes.intIndex++;
        }

        for(Iterator iterator3 = floatFields.iterator(); iterator3.hasNext();)
        {
            Field field4 = (Field)iterator3.next();
            field4.setFloat(obj, af[indexes.floatIndex]);
            indexes.floatIndex++;
        }

        for(Iterator iterator4 = doubleFields.iterator(); iterator4.hasNext();)
        {
            Field field5 = (Field)iterator4.next();
            field5.setDouble(obj, af[indexes.floatIndex]);
            indexes.floatIndex++;
        }

        for(Iterator iterator5 = stringFields.iterator(); iterator5.hasNext();)
        {
            Field field6 = (Field)iterator5.next();
            field6.set(obj, as[indexes.stringIndex]);
            indexes.stringIndex++;
        }

        for(Iterator iterator6 = objectFields.iterator(); iterator6.hasNext();)
        {
            ClassMapping classmapping = (ClassMapping)iterator6.next();
            boolean flag = ai[indexes.intIndex] == 0;
            indexes.intIndex++;
            if(flag)
            {
                indexes.intIndex += classmapping.sizeInt;
                indexes.floatIndex += classmapping.sizeFloat;
                indexes.stringIndex += classmapping.sizeString;
            } else
            {
                classmapping.updateFromData(classmapping.field.get(obj), ai, af, as, indexes);
            }
        }

        for(Iterator iterator7 = intArrayFields.iterator(); iterator7.hasNext();)
        {
            Field field7 = (Field)iterator7.next();
            TileNetworkData tilenetworkdata = (TileNetworkData)field7.getAnnotation(buildcraft.core.TileNetworkData.class);
            int i = 0;
            while(i < tilenetworkdata.staticSize()) 
            {
                ((int[])field7.get(obj))[i] = ai[indexes.intIndex];
                indexes.intIndex++;
                i++;
            }
        }

        for(Iterator iterator8 = booleanArrayFields.iterator(); iterator8.hasNext();)
        {
            Field field8 = (Field)iterator8.next();
            TileNetworkData tilenetworkdata1 = (TileNetworkData)field8.getAnnotation(buildcraft.core.TileNetworkData.class);
            int j = 0;
            while(j < tilenetworkdata1.staticSize()) 
            {
                ((boolean[])field8.get(obj))[j] = ai[indexes.intIndex] == 1;
                indexes.intIndex++;
                j++;
            }
        }

        for(Iterator iterator9 = objectArrayFields.iterator(); iterator9.hasNext();)
        {
            ClassMapping classmapping1 = (ClassMapping)iterator9.next();
            TileNetworkData tilenetworkdata2 = (TileNetworkData)classmapping1.field.getAnnotation(buildcraft.core.TileNetworkData.class);
            Object aobj[] = (Object[])classmapping1.field.get(obj);
            int k = 0;
            while(k < tilenetworkdata2.staticSize()) 
            {
                boolean flag1 = ai[indexes.intIndex] == 0;
                indexes.intIndex++;
                if(flag1)
                {
                    indexes.intIndex += classmapping1.sizeInt;
                    indexes.floatIndex += classmapping1.sizeFloat;
                    indexes.stringIndex += classmapping1.sizeString;
                } else
                {
                    classmapping1.updateFromData(aobj[k], ai, af, as, indexes);
                }
                k++;
            }
        }

    }

    public int[] getSize()
    {
        int ai[] = new int[3];
        ai[0] = sizeInt;
        ai[1] = sizeFloat;
        ai[2] = sizeString;
        return ai;
    }

    private LinkedList floatFields;
    private LinkedList doubleFields;
    private LinkedList stringFields;
    private LinkedList intFields;
    private LinkedList booleanFields;
    private LinkedList enumFields;
    private LinkedList objectFields;
    private LinkedList intArrayFields;
    private LinkedList booleanArrayFields;
    private LinkedList objectArrayFields;
    private int sizeInt;
    private int sizeFloat;
    private int sizeString;
    private Field field;
}
