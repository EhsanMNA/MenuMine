package me.ehsanmna.menumine.nbt;

import me.ehsanmna.menumine.utils.ReflectionUtils;
import org.bukkit.inventory.ItemStack;

import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class NBTReflection implements NBTItem{

    private static final Class<?> craftBukkitItemStack = ReflectionUtils.getCraftClass("inventory.CraftItemStack");
    private static final Class<?> nmsItemStackClass = ReflectionUtils.supports(17) ? ReflectionUtils.getNMSClass("world.item.ItemStack") : ReflectionUtils.getNMSClass("ItemStack");
    private static final Class<?> nmsNBTTagCompoundClass = ReflectionUtils.supports(17) ? ReflectionUtils.getNMSClass("nbt.NBTTagCompound") : ReflectionUtils.getNMSClass("NBTTagCompound");
    private Object nmsItemStack;
    private Object nmsNBTTagCompound;



    public NBTReflection(ItemStack item) {
        try {
            Method method = craftBukkitItemStack.getDeclaredMethod("asNMSCopy", ItemStack.class);
            method.setAccessible(true);
            nmsItemStack = method.invoke(null,item);

            Field tagComponent = ReflectionUtils.supports(17) ?
                    nmsItemStack.getClass().getDeclaredField("u"):
                    nmsItemStack.getClass().getDeclaredField("tag");
            tagComponent.setAccessible(true);

            Method getOrCreateTag = ReflectionUtils.supports(18) ?
                    nmsItemStack.getClass().getDeclaredMethod("u"):
                    ReflectionUtils.supports(16) ?nmsItemStack.getClass().getDeclaredMethod("getOrCreateTag"):
                            nmsItemStack.getClass().getDeclaredMethod("getTag");
            getOrCreateTag.setAccessible(true);


            nmsNBTTagCompound = tagComponent.get(nmsItemStack) != null ? getOrCreateTag.invoke(nmsItemStack) :
                    ReflectionUtils.supports(16) ? getOrCreateTag.invoke(nmsItemStack) :
                            nmsNBTTagCompoundClass.getDeclaredConstructor().newInstance();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public ItemStack getItem() {
        try {
            Method method = craftBukkitItemStack.getDeclaredMethod("asBukkitCopy", nmsItemStackClass);
            method.setAccessible(true);
            Object obj = method.invoke(null,nmsItemStack);
            return (ItemStack) obj;
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean hasTag(String tag) {
        try {
            Method hasTag = ReflectionUtils.supports(18) ?
                    nmsNBTTagCompoundClass.getDeclaredMethod("e",String.class):
                    nmsNBTTagCompoundClass.getDeclaredMethod("hasKey",String.class);
            hasTag.setAccessible(true);
            return (boolean) hasTag.invoke(nmsNBTTagCompound,tag);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(nmsNBTTagCompound);
            System.out.println(nmsItemStack);
        }
        return false;
    }

    @Override
    public void setTag(String key, String value) {
        try {
            Method setTag = ReflectionUtils.supports(18) ?
                    nmsNBTTagCompoundClass.getDeclaredMethod("a",String.class,String.class) :
                    nmsNBTTagCompoundClass.getDeclaredMethod("setString",String.class,String.class);
            setTag.setAccessible(true);
            setTag.invoke(nmsNBTTagCompound,key,value);
            setTag.setAccessible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setTag(String key, int value) {
        try {
            Method setTag = ReflectionUtils.supports(18) ?
                    nmsNBTTagCompoundClass.getDeclaredMethod("a",String.class,int.class):
                    nmsNBTTagCompoundClass.getDeclaredMethod("setInt",String.class,int.class);
            setTag.setAccessible(true);
            setTag.invoke(nmsNBTTagCompound,key,value);
            setTag.setAccessible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setTag(String key, boolean value) {
        try {
            Method setTag = ReflectionUtils.supports(18) ?
                    nmsNBTTagCompoundClass.getDeclaredMethod("a",String.class,boolean.class):
                    nmsNBTTagCompoundClass.getDeclaredMethod("setBoolean",String.class,boolean.class);
            setTag.setAccessible(true);
            setTag.invoke(nmsNBTTagCompound,key,value);
            setTag.setAccessible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setTag(String key, float value) {
        try {
            Method setTag = ReflectionUtils.supports(18) ?
                    nmsNBTTagCompoundClass.getDeclaredMethod("a",String.class,float.class):
                    nmsNBTTagCompoundClass.getDeclaredMethod("setFloat",String.class,float.class);
            setTag.setAccessible(true);
            setTag.invoke(nmsNBTTagCompound,key,value);
            setTag.setAccessible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setTag(String key, double value) {
        try {
            Method setTag = ReflectionUtils.supports(18) ?
                    nmsNBTTagCompoundClass.getDeclaredMethod("a",String.class,double.class):
                    nmsNBTTagCompoundClass.getDeclaredMethod("setDouble",String.class,double.class);
            setTag.setAccessible(true);
            setTag.invoke(nmsNBTTagCompound,key,value);
            setTag.setAccessible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object getTag(String key) {
        try {
            Method setTag = ReflectionUtils.supports(18) ?
                    nmsNBTTagCompoundClass.getDeclaredMethod("c",String.class):
                    nmsNBTTagCompoundClass.getDeclaredMethod("get",String.class);
            setTag.setAccessible(true);
            return setTag.invoke(nmsNBTTagCompound,key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getString(String key) {
        try {
            Method get = ReflectionUtils.supports(18) ?
                    nmsNBTTagCompoundClass.getDeclaredMethod("l",String.class):
                    nmsNBTTagCompoundClass.getDeclaredMethod("getString",String.class);
            get.setAccessible(true);
            return (String) get.invoke(nmsNBTTagCompound,key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int getInt(String key) {
        try {
            Method get = ReflectionUtils.supports(18) ?
                    nmsNBTTagCompoundClass.getDeclaredMethod("h",String.class):
                    nmsNBTTagCompoundClass.getDeclaredMethod("getInt",String.class);
            get.setAccessible(true);
            return (int) get.invoke(nmsNBTTagCompound,key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public boolean getBoolean(String key) {
        try {
            Method get = ReflectionUtils.supports(18) ?
                    nmsNBTTagCompoundClass.getDeclaredMethod("q",String.class):
                    nmsNBTTagCompoundClass.getDeclaredMethod("getBoolean",String.class);
            get.setAccessible(true);
            return (boolean) get.invoke(nmsNBTTagCompound,key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public float getFloat(String key) {
        try {
            Method get = ReflectionUtils.supports(18) ?
                    nmsNBTTagCompoundClass.getDeclaredMethod("j",String.class):
                    nmsNBTTagCompoundClass.getDeclaredMethod("getFloat",String.class);
            get.setAccessible(true);
            return (float) get.invoke(nmsNBTTagCompound,key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public double getDouble(String key) {
        try {
            Method get = ReflectionUtils.supports(18) ?
                    nmsNBTTagCompoundClass.getDeclaredMethod("k",String.class):
                    nmsNBTTagCompoundClass.getDeclaredMethod("getDouble",String.class);
            get.setAccessible(true);
            return (double) get.invoke(nmsNBTTagCompound,key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public void save() {
        try {
            Method setTag = ReflectionUtils.supports(18) ?
                    nmsItemStackClass.getDeclaredMethod("c",nmsNBTTagCompoundClass):
                    nmsItemStackClass.getDeclaredMethod("setTag",nmsNBTTagCompoundClass);
            setTag.setAccessible(true);
            setTag.invoke(nmsItemStack,nmsNBTTagCompound);
            setTag.setAccessible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
