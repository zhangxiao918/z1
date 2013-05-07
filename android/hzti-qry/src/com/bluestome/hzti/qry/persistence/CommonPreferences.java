
package com.bluestome.hzti.qry.persistence;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * @ClassName: CommonPreferences
 * @Description: 通用的配置文件类
 * @author bluestome.zhang
 */

public abstract class CommonPreferences {

    private final Context context;

    public CommonPreferences(Context context) {
        this.context = context;
    }

    /**
     * 将指定KEY的value写入配置文件
     * 
     * @param key
     * @param status
     */
    public abstract boolean saveData(String key, Object value);

    /**
     * 从 配置中获取指定KEY的值
     * 
     * @param key
     * @param defValue
     * @return
     */
    public abstract Object getData(String key, Object defValue);

    /**
     * 保存数据
     * 
     * @param context
     * @param fileName
     * @param key
     * @param value
     */
    boolean saveData(String fileName, String key, Object value) {
        SharedPreferences sp = context.getSharedPreferences(fileName,
                Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
        } else if (value instanceof Integer) {
            editor.putInt(key, (Integer) value);
        } else if (value instanceof Long) {
            editor.putLong(key, (Long) value);
        } else if (value instanceof Float) {
            editor.putFloat(key, (Float) value);
        } else {
            if (value == null) {
                editor.putString(key, "");
            } else {
                editor.putString(key, String.valueOf(value));
            }
        }
        return editor.commit();
    }

    /**
     * 取值
     * 
     * @param context
     * @param fileName
     * @param key
     * @param defValue
     * @return
     */
    Object getData(String fileName, String key, Object defValue) {
        SharedPreferences sp = context.getSharedPreferences(fileName,
                Context.MODE_PRIVATE);
        if (defValue instanceof Boolean) {
            return sp.getBoolean(key, (Boolean) defValue);
        } else if (defValue instanceof Integer) {
            return sp.getInt(key, (Integer) defValue);
        } else if (defValue instanceof Long) {
            return sp.getLong(key, (Long) defValue);
        } else if (defValue instanceof Float) {
            return sp.getFloat(key, (Float) defValue);
        } else {
            if (defValue == null) {
                return sp.getString(key, "");
            }
            return sp.getString(key, String.valueOf(defValue));
        }
    }

    /**
     * 清空
     * 
     * @param fileName
     */
    void clear(String fileName) {
        SharedPreferences sp = context.getSharedPreferences(fileName,
                Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.clear();
        editor.commit();
    }

}
