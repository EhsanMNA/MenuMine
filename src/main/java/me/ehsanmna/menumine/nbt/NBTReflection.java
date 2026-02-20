package me.ehsanmna.menumine.nbt;

import com.cryptomorin.xseries.reflection.XReflection;
import me.ehsanmna.menumine.Managers.MenuManager;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class NBTReflection implements NBTItem{

    private static final Class<?> craftBukkitItemStack = XReflection.getCraftClass("inventory.CraftItemStack");
    private static final Class<?> nmsItemStackClass = XReflection.supports(17) ? XReflection.getNMSClass("world.item.ItemStack") : XReflection.getNMSClass("ItemStack");
    private static final Class<?> nmsNBTTagCompoundClass = XReflection.supports(17) ? XReflection.getNMSClass("nbt.NBTTagCompound") : XReflection.getNMSClass("NBTTagCompound");
    private Object nmsItemStack;
    private Object nmsNBTTagCompound;



    public NBTReflection(ItemStack item) {
        try {
            Method method = craftBukkitItemStack.getDeclaredMethod("asNMSCopy", ItemStack.class);
            method.setAccessible(true);
            nmsItemStack = method.invoke(null,item);

            Field tagComponent = XReflection.supports(19) ?
                    nmsItemStack.getClass().getDeclaredField("v"): XReflection.supports(17) ?
                    nmsItemStack.getClass().getDeclaredField("u"):
                    nmsItemStack.getClass().getDeclaredField("tag");
            tagComponent.setAccessible(true);

            Method getOrCreateTag = XReflection.supports(20) ?
                    nmsItemStack.getClass().getDeclaredMethod("w"):
                    XReflection.supports(19) ?
                    nmsItemStack.getClass().getDeclaredMethod("v"):
                    XReflection.supports(18) ?
                    nmsItemStack.getClass().getDeclaredMethod("u"):
                    XReflection.supports(16) ?nmsItemStack.getClass().getDeclaredMethod("getOrCreateTag"):
                            nmsItemStack.getClass().getDeclaredMethod("getTag");
            getOrCreateTag.setAccessible(true);


            nmsNBTTagCompound = getOrCreateTag.invoke(nmsItemStack);

        } catch (Exception e) {
            MenuManager.logError(e);
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
            MenuManager.logError(e);
        }
        return null;
    }

    @Override
    public boolean hasTag(String tag) {
        try {
            Method hasTag = XReflection.supports(18) ?
                    nmsNBTTagCompoundClass.getDeclaredMethod("e",String.class):
                    nmsNBTTagCompoundClass.getDeclaredMethod("hasKey",String.class);
            hasTag.setAccessible(true);
            return (boolean) hasTag.invoke(nmsNBTTagCompound,tag);
        } catch (Exception e) {
            MenuManager.logError(e);
        }
        return false;
    }

    @Override
    public void setTag(String key, String value) {
        try {
            Method setTag = XReflection.supports(18) ?
                    nmsNBTTagCompoundClass.getDeclaredMethod("a",String.class,String.class) :
                    nmsNBTTagCompoundClass.getDeclaredMethod("setString",String.class,String.class);
            setTag.setAccessible(true);
            setTag.invoke(nmsNBTTagCompound,key,value);
            setTag.setAccessible(false);
        } catch (Exception e) {
            MenuManager.logError(e);
        }
    }

    @Override
    public void setTag(String key, int value) {
        try {
            Method setTag = XReflection.supports(18) ?
                    nmsNBTTagCompoundClass.getDeclaredMethod("a",String.class,int.class):
                    nmsNBTTagCompoundClass.getDeclaredMethod("setInt",String.class,int.class);
            setTag.setAccessible(true);
            setTag.invoke(nmsNBTTagCompound,key,value);
            setTag.setAccessible(false);
        } catch (Exception e) {
            MenuManager.logError(e);
        }
    }

    @Override
    public void setTag(String key, boolean value) {
        try {
            Method setTag = XReflection.supports(18) ?
                    nmsNBTTagCompoundClass.getDeclaredMethod("a",String.class,boolean.class):
                    nmsNBTTagCompoundClass.getDeclaredMethod("setBoolean",String.class,boolean.class);
            setTag.setAccessible(true);
            setTag.invoke(nmsNBTTagCompound,key,value);
            setTag.setAccessible(false);
        } catch (Exception e) {
            MenuManager.logError(e);
        }
    }

    @Override
    public void setTag(String key, float value) {
        try {
            Method setTag = XReflection.supports(18) ?
                    nmsNBTTagCompoundClass.getDeclaredMethod("a",String.class,float.class):
                    nmsNBTTagCompoundClass.getDeclaredMethod("setFloat",String.class,float.class);
            setTag.setAccessible(true);
            setTag.invoke(nmsNBTTagCompound,key,value);
            setTag.setAccessible(false);
        } catch (Exception e) {
            MenuManager.logError(e);
        }
    }

    @Override
    public void setTag(String key, double value) {
        try {
            Method setTag = XReflection.supports(18) ?
                    nmsNBTTagCompoundClass.getDeclaredMethod("a",String.class,double.class):
                    nmsNBTTagCompoundClass.getDeclaredMethod("setDouble",String.class,double.class);
            setTag.setAccessible(true);
            setTag.invoke(nmsNBTTagCompound,key,value);
            setTag.setAccessible(false);
        } catch (Exception e) {
            MenuManager.logError(e);
        }
    }

    @Override
    public Object getTag(String key) {
        try {
            Method setTag = XReflection.supports(18) ?
                    nmsNBTTagCompoundClass.getDeclaredMethod("c",String.class):
                    nmsNBTTagCompoundClass.getDeclaredMethod("get",String.class);
            setTag.setAccessible(true);
            return setTag.invoke(nmsNBTTagCompound,key);
        } catch (Exception e) {
            MenuManager.logError(e);
        }
        return null;
    }

    @Override
    public String getString(String key) {
        try {
            Method get = XReflection.supports(18) ?
                    nmsNBTTagCompoundClass.getDeclaredMethod("l",String.class):
                    nmsNBTTagCompoundClass.getDeclaredMethod("getString",String.class);
            get.setAccessible(true);
            return (String) get.invoke(nmsNBTTagCompound,key);
        } catch (Exception e) {
            MenuManager.logError(e);
        }
        return null;
    }

    @Override
    public int getInt(String key) {
        try {
            Method get = XReflection.supports(18) ?
                    nmsNBTTagCompoundClass.getDeclaredMethod("h",String.class):
                    nmsNBTTagCompoundClass.getDeclaredMethod("getInt",String.class);
            get.setAccessible(true);
            return (int) get.invoke(nmsNBTTagCompound,key);
        } catch (Exception e) {
            MenuManager.logError(e);
        }
        return 0;
    }

    @Override
    public boolean getBoolean(String key) {
        try {
            Method get = XReflection.supports(18) ?
                    nmsNBTTagCompoundClass.getDeclaredMethod("q",String.class):
                    nmsNBTTagCompoundClass.getDeclaredMethod("getBoolean",String.class);
            get.setAccessible(true);
            return (boolean) get.invoke(nmsNBTTagCompound,key);
        } catch (Exception e) {
            MenuManager.logError(e);
        }
        return false;
    }

    @Override
    public float getFloat(String key) {
        try {
            Method get = XReflection.supports(18) ?
                    nmsNBTTagCompoundClass.getDeclaredMethod("j",String.class):
                    nmsNBTTagCompoundClass.getDeclaredMethod("getFloat",String.class);
            get.setAccessible(true);
            return (float) get.invoke(nmsNBTTagCompound,key);
        } catch (Exception e) {
            MenuManager.logError(e);
        }
        return 0;
    }

    @Override
    public double getDouble(String key) {
        try {
            Method get = XReflection.supports(18) ?
                    nmsNBTTagCompoundClass.getDeclaredMethod("k",String.class):
                    nmsNBTTagCompoundClass.getDeclaredMethod("getDouble",String.class);
            get.setAccessible(true);
            return (double) get.invoke(nmsNBTTagCompound,key);
        } catch (Exception e) {
            MenuManager.logError(e);
        }
        return 0;
    }

    @Override
    public void save() {
        try {
            Method setTag = XReflection.supports(18) ?
                    nmsItemStackClass.getDeclaredMethod("c",nmsNBTTagCompoundClass):
                    nmsItemStackClass.getDeclaredMethod("setTag",nmsNBTTagCompoundClass);
            setTag.setAccessible(true);
            setTag.invoke(nmsItemStack,nmsNBTTagCompound);
            setTag.setAccessible(false);
        } catch (Exception e) {
            MenuManager.logError(e);
        }
    }
}
