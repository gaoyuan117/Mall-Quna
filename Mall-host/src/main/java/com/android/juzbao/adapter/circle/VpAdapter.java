package com.android.juzbao.adapter.circle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.View;

import java.util.List;

/**
 * Created by admin on 2017/3/17.
 */

public class VpAdapter extends FragmentPagerAdapter {
    private List<Fragment> list;
    public VpAdapter(FragmentManager fm, List<Fragment> list) {
        super(fm);
        this.list=list;
    }
    //获取指定位置上的碎片对象
    @Override

    public Fragment getItem(int position) {
        return list.get(position);
    }
    //获取碎片对象的个数
    @Override
    public int getCount() {
        return list.size();
    }
}
