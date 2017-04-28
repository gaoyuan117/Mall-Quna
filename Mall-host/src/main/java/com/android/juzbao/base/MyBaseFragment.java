package com.android.juzbao.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.zcomponent.common.uiframe.fragment.BaseFragment;

/**
 * Created by admin on 2017/3/17.
 */

public abstract class MyBaseFragment extends BaseFragment {

    public Activity mActivity;
    public static Bundle mBundle = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mActivity = getActivity();
        View view = loadLayout(inflater);
        mBundle = savedInstanceState;
        init(view);
        initData();
        set();
        return view;
    }

    /**
     * 加载布局
     */
    protected abstract View loadLayout(LayoutInflater inflater);

    /**
     * 返回savedInstanceState
     *
     * @return
     */
    protected Bundle getBundle() {
        return mBundle;
    }

    /**
     * 获取所有的主件
     */
    protected void init(View view) {
    }

    /**
     * 业务逻辑过程
     */
    protected void initData() {
    }

    /**
     * 设置数据
     */
    protected void set() {
    }



    /**
     * 跳转页面
     */
    protected void toActivity(Class<?> activity) {
        startActivity(new Intent(mActivity, activity));
    }

    /**
     * 携带数据的跳转
     *
     * @param intent
     */
    protected void toIntentActivity(Intent intent) {
        startActivity(intent);
    }
}
