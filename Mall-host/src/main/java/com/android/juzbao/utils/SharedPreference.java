package com.android.juzbao.utils;

import android.content.SharedPreferences;

import com.android.juzbao.application.BaseApplication;

/**
 * Created by dn on 2017/2/20.
 * <p>
 * SharedPreferences 工具类
 */

public class SharedPreference {
    private SharedPreferences sharedPreference;
    private SharedPreferences.Editor edit;

    public SharedPreference(String key) {
        sharedPreference = BaseApplication.app.getSharedPreferences(key, BaseApplication.app.MODE_PRIVATE);
    }

    /**
     * 初始化edit()
     *
     * @return
     */
    public SharedPreference edit() {
        edit = sharedPreference.edit();
        return this;
    }

    /**
     * 保存
     *
     * @param key
     * @param value
     * @return
     */
    public SharedPreference putString(String key, String value) {
        edit.putString(key, value);
        return this;
    }

    /**
     * 提交
     *
     * @return
     */
    public SharedPreference commit() {
        edit.commit();
        return this;
    }

    /**
     * 获取数据
     * @param key
     * @return
     */
    public String get(String key) {
        String value = sharedPreference.getString(key,"");
        return value;
    }

}
