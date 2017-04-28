package com.android.juzbao.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by admin on 2017/3/22.
 */

public abstract class BaseActivity extends AppCompatActivity {
    public static Bundle mBundle = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        mBundle = savedInstanceState;
        initView();
        initData();
        setData();
        setListener();
    }


    protected Bundle getBundle() {
        return mBundle;
    }

    /**
     * 返回一个布局的id
     */
    protected abstract int getLayoutId();

    /**
     * 返回一个状态栏的颜色
     */
    protected abstract int getStatusBarColor();

    /**
     * 返回是否使用侵入式
     */
    protected abstract boolean setImmersive();


    /**
     * 初始化布局
     */
    protected void initView() {

    }

    /**
     * 初始化数据
     */
    protected void initData() {

    }

    /**
     * 设置数据
     */
    protected void setData() {

    }

    /**
     * 设置监听
     */
    protected void setListener() {

    }


    /**
     * 跳转页面
     */
    protected void toActivity(Class<?> activity) {
        startActivity(new Intent(this, activity));
    }

    /**
     * 携带数据的跳转
     *
     * @param intent
     */
    protected void toIntentActivity(Intent intent) {
        startActivity(intent);
    }

    protected void finishActivity() {
        finish();
    }

}
