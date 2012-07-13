package net.minecraft.src;

import java.lang.reflect.Field;

public class OldDaysProperty{
    public static int TYPE_BOOLEAN = 1;
    public static int TYPE_INTEGER = 2;
    public static int TYPE_STRING = 3;

    public static int GUI_TYPE_BUTTON = 1;
    public static int GUI_TYPE_DROPDOWN = 2;
    public static int GUI_TYPE_FIELD = 3;

    public int id;
    public String name;
    public String description;
    public int type;
    public int guitype;
    public Field field;
    public OldDaysModule module;
    public boolean error;
    public boolean allowedInSMP;
    public boolean allowedInFallback;

    public OldDaysProperty(OldDaysModule m, int i, String s, int t, String f){
        module = m;
        id = i;
        name = s;
        type = t;
        guitype = GUI_TYPE_BUTTON;
        error = false;
        allowedInSMP = true;
        allowedInFallback = true;
        try{
            field = module.getClass().getDeclaredField(f);
        }catch(Exception ex){
            disable();
        }
    }

    public String getButtonText(){
        return name;
    }

    public void onChange(){}

    public int getDisableReason(){
        if (error){
            return 1;
        }
        if (!allowedInFallback && mod_OldDays.texman.fallbacktex){
            return 2;
        }
        if (!allowedInSMP && mod_OldDays.getMinecraftInstance().theWorld!=null){
            if (mod_OldDays.getMinecraftInstance().theWorld.isRemote){
                return 3;
            }
        }
        return 0;
    }

    public boolean isDisabled(){
        return getDisableReason() > 0;
    }

    public void setFieldValue(){
        disable();
    }

    protected void disable(){
        System.out.println("Error in module "+module.name+", property "+name+", disabling");
        error = true;
    }

    public void incrementValue(){}

    public void setSMPValue(){}

    public void loadFromString(String str){}

    public String saveToString(){
        return "";
    }
}