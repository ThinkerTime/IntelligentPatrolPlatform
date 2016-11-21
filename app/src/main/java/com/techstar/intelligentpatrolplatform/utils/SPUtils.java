package com.techstar.intelligentpatrolplatform.utils;

import android.content.Context;
import android.content.SharedPreferences;


public class SPUtils {
    private static SharedPreferences sp;
    public static void saveBooleanSP(String key, Boolean value){
        if (sp == null){
            sp = UIUtils.getContext().getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        sp.edit().putBoolean(key,value).apply();
    }
    public static boolean getBooleanSP(String key, Boolean value){
        if (sp == null)
            sp = UIUtils.getContext().getSharedPreferences("config", Context.MODE_PRIVATE);
        return sp.getBoolean(key,value);
    }

    public static void saveStringSP(String key, String value){
        if (sp == null){
            sp = UIUtils.getContext().getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        sp.edit().putString(key, value).apply();
    }
    public static String getStringSP(String key, String value){
        if (sp == null) {
            sp = UIUtils.getContext().getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        return  sp.getString(key, value);
    }

    public static void saveIntSP(String key, int value){
        if (sp == null){
            sp = UIUtils.getContext().getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        sp.edit().putInt(key, value).apply();
    }
    public static int getIntSP(String key, int value){
        if (sp == null) {
            sp = UIUtils.getContext().getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        return  sp.getInt(key, value);
    }


    public static void removeKey(String key){
        if (sp == null) {
            sp = UIUtils.getContext().getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        sp.edit().remove(key).apply();
    }

    public static void clearSP(){
        if (sp == null) {
            sp = UIUtils.getContext().getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        sp.edit().clear().apply();
    }


}
