
package com.android.juzbao.activity.order;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;

import com.android.juzbao.constant.GlobalConst;
import com.android.juzbao.constant.ResultActivity;
import com.android.juzbao.enumerate.OrderStatus;
import com.android.juzbao.fragment.ShopOrderFragment_;
import com.android.mall.resource.R;
import com.android.zcomponent.adapter.FragmentViewPagerAdapter;
import com.android.zcomponent.common.uiframe.fragment.BaseFragment.OnFragmentCreatedListener;
import com.android.zcomponent.util.LogEx;
import com.android.zcomponent.util.MyLayoutAdapter;
import com.android.zcomponent.views.PagerSlidingTabStrip;
import com.android.zcomponent.views.swipebacklayout.lib.app.SwipeBackActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 我的账单
 * @date: 2013-12-10
 */
@EActivity(R.layout.activity_my_order)
public class MyShopOrderActivity extends SwipeBackActivity implements
        OnFragmentCreatedListener {

    private final String TAG = "MyShopOrderActivity";

    @ViewById(R.id.common_viewpaper_show)
    ViewPager mViewPager;

    @ViewById(R.id.common_pager_slide_tab_show)
    PagerSlidingTabStrip mPagerSlidingTabStrip;

    private List<Fragment> mlistFragments;

    private List<String> mlistTitle;

    private int miCurSelectedFragmentPosition = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @AfterViews
    void init() {
        getTitleBar().setTitleText("我的订单");
        initUI();
    }

    private void initUI() {
        bindWidget();
        initViewPager();
    }

    private void bindWidget() {
        mPagerSlidingTabStrip
                .setOnPageChangeListener(new OnPageChangeListener() {

                    @Override
                    public void onPageSelected(int position) {
                        miCurSelectedFragmentPosition = position;
                        refreshCurFragment();
                    }

                    @Override
                    public void onPageScrolled(int arg0, float arg1, int arg2) {

                    }

                    @Override
                    public void onPageScrollStateChanged(int arg0) {

                    }
                });
    }

    public void initViewPager() {
        mlistFragments = new ArrayList<Fragment>();
        mlistTitle = new ArrayList<String>();

        addShopOrderFragment();
        FragmentViewPagerAdapter adapter =
                new FragmentViewPagerAdapter(getSupportFragmentManager(),
                        mViewPager, mlistFragments);
        mViewPager.setAdapter(adapter);
        mPagerSlidingTabStrip.setTabPaddingLeftRight(10);
        mPagerSlidingTabStrip.setIndicatorColorResource(R.color.red);
        mPagerSlidingTabStrip.setTextSelectColorResource(R.color.red);
        mPagerSlidingTabStrip.setTabWidth((int) (MyLayoutAdapter.getInstance().getScreenWidth() / 4));

        mPagerSlidingTabStrip.setViewPager(mViewPager, mlistTitle);
        mViewPager.setCurrentItem(miCurSelectedFragmentPosition);
    }

    private void addShopOrderFragment() {
        Intent intent = getIntent();
        String strOrderStatus = intent.getStringExtra(GlobalConst.ORDER_STATUS);
        List<OrderStatus> orderStatus = new ArrayList<OrderStatus>();

        orderStatus.add(OrderStatus.ALL);
        orderStatus.add(OrderStatus.SUBMIT);
        orderStatus.add(OrderStatus.SUBMITED);
        orderStatus.add(OrderStatus.REFUND);

        mlistTitle.add("全部");
        mlistTitle.add("待支付");
        mlistTitle.add("已支付");
        mlistTitle.add("退款");

        int position = 0;
        for (OrderStatus status : orderStatus) {
            ShopOrderFragment_ takeoutFragment = new ShopOrderFragment_();
            takeoutFragment.setOrderStatus(status);
            takeoutFragment.setOnFragmentCreatedListener(this);
            mlistFragments.add(takeoutFragment);

            if (strOrderStatus.equals(status.getValue())) {
                miCurSelectedFragmentPosition = position;
            }
            position++;
        }
    }

    private void refreshCurFragment() {
        if (getCurrentFragment().isCreated()) {
            getCurrentFragment().queryListData();
        }
    }

    private ShopOrderFragment_ getCurrentFragment() {
        ShopOrderFragment_ fragment =
                (ShopOrderFragment_) mlistFragments
                        .get(miCurSelectedFragmentPosition);
        return fragment;
    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent data) {
    }

    @Override
    public void onActivityResultCallBack(int resultCode, Intent intent) {
        LogEx.d(TAG, "onActivityResultCallBack resultCode " + resultCode);

        ShopOrderFragment_ fragment = getCurrentFragment();
        if (resultCode == ResultActivity.CODE_REFUND_SUCCESS) {
            fragment.refundSuccess();
        } else if (resultCode == ResultActivity.CODE_PAY_ECO_SUCCESS) {
            fragment.payEcoSuccess();
        } else if (resultCode == ResultActivity.CODE_ADD_SHOP_REVIEW) {
            fragment.addShopReviewSuccess(intent);
        } else if (resultCode == ResultActivity.CODE_UPDATE_ORDER) {
            fragment.refreshItem(intent, false);
        } else if (resultCode == ResultActivity.CODE_DEL_ORDER) {
            fragment.refreshData(true);
        }
    }

    @Override
    public void onFragmentCreated() {
        refreshCurFragment();
    }

//    @Override protected void onRestart() {
//        super.onRestart();
//        ((ShopOrderFragment_) mlistFragments.get(miCurSelectedFragmentPosition)).refreshData(true);
//    }
}
