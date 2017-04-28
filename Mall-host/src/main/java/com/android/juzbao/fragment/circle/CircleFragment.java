package com.android.juzbao.fragment.circle;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.juzbao.adapter.circle.VpAdapter;
import com.android.quna.activity.R;
import com.android.zcomponent.adapter.FragmentViewPagerAdapter;
import com.android.zcomponent.util.MyLayoutAdapter;
import com.android.zcomponent.views.PagerSlidingTabStrip;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by admin on 2017/3/16.
 * 圈子
 */

public class CircleFragment extends Fragment {
    private PagerSlidingTabStrip mTab;
    private ViewPager mViewPager;
    private List<String> mTitles;
    private List<Fragment> mFragments;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_circle, null);
        initView(view);
        initData();
        setData();
        return view;
    }

    private void setData() {

    }

    private void initData() {
        mTitles.add("动态");
        mTitles.add("约");
        mTitles.add("关注");

        mFragments.add(new DynamicFragment());
        mFragments.add(new InviteFragment());
        mFragments.add(new ConcernFragment());

        VpAdapter adapter = new VpAdapter(getActivity().getSupportFragmentManager(), mFragments);
        mViewPager.setAdapter(adapter);
        mTab.setIndicatorColorResource(com.android.mall.resource.R.color.orange2);
        mTab.setTextSelectColorResource(com.android.mall.resource.R.color.orange2);
        mTab.setTabWidth((int) (MyLayoutAdapter.getInstance().getScreenWidth() / 4));

        mTab.setViewPager(mViewPager, mTitles);
        mViewPager.setOffscreenPageLimit(3);
    }

    private void initView(View view) {
        mTab = (PagerSlidingTabStrip) view.findViewById(R.id.tab_circle);
        mViewPager = (ViewPager) view.findViewById(R.id.vp_circle);
        mTitles = new ArrayList<>();
        mFragments = new ArrayList<>();
    }

}
