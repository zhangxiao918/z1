
package com.bluestome.hzti.qry.persistence;

import android.content.Context;

/**
 * @ClassName: SettingsPreferences
 * @Description: 设置界面的配置文件
 * @author bluestome.zhang
 */
public class SettingsPreferences extends CommonPreferences {

    public SettingsPreferences(Context context) {
        super(context);
    }

    // 文件名
    private static final String SETTINGS_PREFERENCES = "settings_preferences";

    /**
     * 清除所有数据
     */
    public void clear() {
        clear(SETTINGS_PREFERENCES);
    }

    /**
     * 保存数据
     * 
     * @param key
     * @param status
     * @return boolean true:成功 false:失败
     */
    @Override
    public boolean saveData(String key, Object value) {
        return saveData(SETTINGS_PREFERENCES, key, value);
    }

    /**
     * 从文件中获取数据
     * 
     * @param key
     * @param defValue
     * @return
     */
    @Override
    public Object getData(String key, Object defValue) {
        return getData(SETTINGS_PREFERENCES, key, defValue);
    }

    /**
     * 移除空字符串"null"
     * 
     * @param str
     * @return
     */
    public String removeNULL(String str) {
        if (str == null || "null".equals(str)) {
            return "";
        }
        return str;
    }
}
