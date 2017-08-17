package cn.bluester.recordtask.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * 通过SharedPreference缓存数据到XML文件中
 * Created by JesseHu on 2016/4/16.
 */
public class CacheUtils {
    private static SharedPreferences mSp;

    //获取SharedPreferences
    private static SharedPreferences getPreferences(Context context) {
        if (mSp == null) {
            /**
             * 获取SharedPreferences，如果存在则直接获取，如果不存在则创建
             * getSharedPreferences(String name,int mode)
             * name:文件名
             * mode:操作模式
             */
            mSp = context.getSharedPreferences(MyConstants.CONFIG_SP, Context.MODE_PRIVATE);
        }
        return mSp;
    }

    /**
     * 保存数据
     *
     * @param context 上下文
     * @param key     键
     * @param value   值
     */
    public static void putValue(Context context, String key, Object value) {
        SharedPreferences sp = getPreferences(context);
        Editor edit = sp.edit();
        //判断传入value的类型，然后进行存储
        if (value instanceof String) {
            edit.putString(key, (String) value);
        } else if (value instanceof Integer) {
            edit.putInt(key, (Integer) value);
        } else if (value instanceof Boolean) {
            edit.putBoolean(key, (Boolean) value);
        } else if (value instanceof Long){
            edit.putLong(key, (Long) value);
        }
        edit.apply();
    }

    /**
     * 获取String类型数据，如果数据不存在，返回""
     *
     * @param context 上下文
     * @param key     键
     * @return 返回值，默认为""
     */
    public static String getString(Context context, String key) {
        SharedPreferences sp = getPreferences(context);
        return sp.getString(key, "");
    }

    /**
     * 获取String类型数据,如果数据不存在，返回自定义默认值
     *
     * @param context  上下文
     * @param key      键
     * @param defValue 默认值
     * @return 返回值，默认返回自定义默认值
     */
    public static String getString(Context context, String key, String defValue) {
        SharedPreferences sp = getPreferences(context);
        return sp.getString(key, defValue);
    }

    /**
     * 获取int类型数据，如果数据不存在，返回0
     *
     * @param context 上下文
     * @param key     键
     * @return 返回值，默认为0
     */
    public static int getInt(Context context, String key) {
        SharedPreferences sp = getPreferences(context);
        return sp.getInt(key, 0);
    }

    /**
     * 获取int类型数据,如果数据不存在，返回自定义默认值
     *
     * @param context  上下文
     * @param key      键
     * @param defValue 默认值
     * @return 返回值，默认返回自定义默认值
     */
    public static int getInt(Context context, String key, int defValue) {
        SharedPreferences sp = getPreferences(context);
        return sp.getInt(key, defValue);
    }

    /**
     * 获取boolean类型数据，如果数据不存在，返回false
     *
     * @param context 上下文
     * @param key     键
     * @return 返回值，默认为false
     */
    public static boolean getBoolean(Context context, String key) {
        SharedPreferences sp = getPreferences(context);
        return sp.getBoolean(key, false);
    }

    /**
     * 获取boolean类型数据,如果数据不存在，返回自定义默认值
     *
     * @param context  上下文
     * @param key      键
     * @param defValue 默认值
     * @return 返回值，默认返回自定义默认值
     */
    public static boolean getBoolean(Context context, String key, boolean defValue) {
        SharedPreferences sp = getPreferences(context);
        return sp.getBoolean(key, defValue);
    }
    /**
     * 获取long类型数据，如果数据不存在，返回false
     *
     * @param context 上下文
     * @param key     键
     * @return 返回值，默认为0
     */
    public static long getLong(Context context,String key){
        SharedPreferences sp = getPreferences(context);
        return sp.getLong(key,0L);
    }
    /**
     * 获取long类型数据,如果数据不存在，返回自定义默认值
     *
     * @param context  上下文
     * @param key      键
     * @param defValue 默认值
     * @return 返回值，默认返回自定义默认值
     */
    public static long getLong(Context context, String key, long defValue) {
        SharedPreferences sp = getPreferences(context);
        return sp.getLong(key, defValue);
    }

}
