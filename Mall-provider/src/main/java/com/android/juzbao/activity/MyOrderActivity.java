
package com.android.juzbao.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.juzbao.constant.ProviderGlobalConst;
import com.android.juzbao.constant.ProviderResultActivity;
import com.android.juzbao.enumerate.ProviderOrderStatus;
import com.android.juzbao.fragment.OrderFragment_;
import com.android.juzbao.provider.R;
import com.android.zcomponent.adapter.FragmentViewPagerAdapter;
import com.android.zcomponent.common.uiframe.fragment.BaseFragment.OnFragmentCreatedListener;
import com.android.zcomponent.json.JsonSerializerFactory;
import com.android.zcomponent.util.LogEx;
import com.android.zcomponent.util.MyLayoutAdapter;
import com.android.zcomponent.views.PagerSlidingTabStrip;
import com.android.zcomponent.views.swipebacklayout.lib.app.SwipeBackActivity;
import com.server.api.model.Order;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 我的订单 (商家订单管理)
 */
@EActivity(resName = "activity_order")
public class MyOrderActivity extends SwipeBackActivity implements
        OnFragmentCreatedListener {
    private final String TAG = "PersonalMyReserveOrderActivity";

    @ViewById(resName = "viewpaper")
    ViewPager mViewPager;

    @ViewById(resName = "tvew_more_order")
    TextView mTvewMore;

    @ViewById(resName = "llayout_more_order")
    LinearLayout mLlayoutMore;

    @ViewById(resName = "order_select_table_titlebar_tabs")
    PagerSlidingTabStrip mPagerSlidingTabStrip;

    private List<Fragment> mlistFragments;

    private List<String> mlistTitle;

    private int miCurSelectedFragmentPosition = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @AfterViews void init() {
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
                        dismissOptionWindow();
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

    @Click(resName = "tvew_more_order")
    public void onClickTvewMore() {
        showMorePopWindow();
        if (isOptionWindShowing()) {
            dismissOptionWindow();
        } else {
            showOptionWindow();
        }
    }

    private PopupWindow mMorePopupWindow;

    private void showMorePopWindow() {
        if (null == mMorePopupWindow) {
            View view = LayoutInflater.from(this).inflate(R.layout.pop_more_order, null);
            mMorePopupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

            TextView tvewAuction =
                    (TextView) view.findViewById(R.id.tvew_auction_show);
            TextView tvewSended =
                    (TextView) view.findViewById(R.id.tvew_sended_show);

            TextView tvewReview = (TextView) view.findViewById(R.id.tvew_review_show);
            TextView tvewComplete = (TextView) view.findViewById(R.id.tvew_complete_show);
            TextView tvewClose = (TextView) view.findViewById(R.id.tvew_close_show);
            TextView tvewRefund = (TextView) view.findViewById(R.id.tvew_refund_show);
            TextView tvewRefunded = (TextView) view.findViewById(R.id.tvew_refunded_show);

//			tvewAuction.setOnClickListener(new OnClickListener()
//			{
//
//				@Override
//				public void onClick(View v)
//				{
//					dismissOptionWindow();
//					intentToMoreOrderActivity(ProviderOrderStatus.RECEIPT2);
//				}
//			});

//			tvewSended.setOnClickListener(new OnClickListener()
//			{
//
//				@Override
//				public void onClick(View v)
//				{
//					dismissOptionWindow();
//					intentToMoreOrderActivity(ProviderOrderStatus.RECEIPT3);
//				}
//			});
            tvewReview.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    dismissOptionWindow();
                    intentToMoreOrderActivity(ProviderOrderStatus.RECEIPT2);
                }
            });
            tvewComplete.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    dismissOptionWindow();
                    intentToMoreOrderActivity(ProviderOrderStatus.RECEIPT3);
                }
            });
            tvewClose.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    dismissOptionWindow();
                    intentToMoreOrderActivity(ProviderOrderStatus.ENSURE);
                }
            });

            tvewRefund.setOnClickListener(new OnClickListener() {
                @Override public void onClick(View v) {
                    dismissOptionWindow();
                    intentToMoreOrderActivity(ProviderOrderStatus.COMPLETE);
                }
            });

            tvewRefunded.setOnClickListener(new OnClickListener() {
                @Override public void onClick(View v) {
                    dismissOptionWindow();
                    intentToMoreOrderActivity(ProviderOrderStatus.CLOSE);
                }
            });

            view.setOnTouchListener(new OnTouchListener() {

                @Override
                public boolean onTouch(View view, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        mMorePopupWindow.dismiss();
                    }
                    return false;
                }
            });
        }
    }

    /**
     * 查询其他订单   3：已收货/待评价，4：交易完成，5：交易取消，6：订单提交后的所有状态查询
     *
     * @param orderStatus
     */
    private void intentToMoreOrderActivity(ProviderOrderStatus orderStatus) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("orderstatus", orderStatus);
        getIntentHandle().intentToActivity(bundle, MyMoreOrderActivity_.class);
    }

    /**
     * <p>
     * Description: 显示Popup筛选框
     * <p>
     *
     * @date 2013-6-26
     * @author zte
     */
    private void showOptionWindow() {
        if (!mMorePopupWindow.isShowing()) {
            mMorePopupWindow.showAsDropDown(mLlayoutMore, 0, 0);
            mMorePopupWindow.update();
        }
    }

    /**
     * <p>
     * Description: 判断筛选框是否显示
     * <p>
     *
     * @return
     * @date 2014-2-28
     * @author WEI
     */
    public boolean isOptionWindShowing() {
        if (null == mMorePopupWindow) {
            return false;
        }
        return mMorePopupWindow.isShowing();
    }

    /**
     * <p>
     * Description: 关闭筛选框
     * <p>
     *
     * @date 2014-2-28
     * @author WEI
     */
    public void dismissOptionWindow() {
        if (null != mMorePopupWindow && isOptionWindShowing()) {
            mMorePopupWindow.dismiss();
        }
    }

    public void initViewPager() {
        mlistFragments = new ArrayList<Fragment>();
        mlistTitle = new ArrayList<String>();

        addShopOrderFragment();
        FragmentViewPagerAdapter adapter =
                new FragmentViewPagerAdapter(getSupportFragmentManager(),
                        mViewPager, mlistFragments);
        mViewPager.setAdapter(adapter);

        mPagerSlidingTabStrip.setIndicatorColorResource(R.color.red);
        mPagerSlidingTabStrip.setTextSelectColorResource(R.color.red);

        int moreWidth = MyLayoutAdapter.dip2px(this, 75);
        int width = (int) MyLayoutAdapter.getInstance().getScreenWidth() - moreWidth;
        mPagerSlidingTabStrip.setTabWidth((int) (width / 4));
        mPagerSlidingTabStrip.setViewPager(mViewPager, mlistTitle);
        mViewPager.setCurrentItem(miCurSelectedFragmentPosition);
    }

    private void addShopOrderFragment() {
        Intent intent = getIntent();
        String strOrderStatus =
                intent.getStringExtra(ProviderGlobalConst.ORDER_STATUS);
        List<ProviderOrderStatus> orderStatus =
                new ArrayList<ProviderOrderStatus>();
        orderStatus.add(ProviderOrderStatus.ALL);  // all ->  全部订单
        orderStatus.add(ProviderOrderStatus.PAY);  // 0 ->待支付
        orderStatus.add(ProviderOrderStatus.DELIVERY); //1 -> 待发货
        orderStatus.add(ProviderOrderStatus.RECEIPT1); // 2 -> 已发货

        int position = 0;
        for (ProviderOrderStatus status : orderStatus) {
            OrderFragment_ takeoutFragment = new OrderFragment_();
            takeoutFragment.setOrderStatus(status);
            takeoutFragment.setOnFragmentCreatedListener(this);
            mlistFragments.add(takeoutFragment);
            mlistTitle.add(status.getName());
            if (strOrderStatus.equals(status.getValue())) {
                miCurSelectedFragmentPosition = position;
            }
            position++;
        }
    }

    private void refreshCurFragment() {
        OrderFragment_ fragment = (OrderFragment_) mlistFragments
                .get(miCurSelectedFragmentPosition);
        if (fragment.isCreated()) {
            fragment.queryListData();
        }
    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent data) {
    }

    /**
     * 消息返回
     */
    @Override
    public void onActivityResultCallBack(int resultCode, Intent intent) {
        LogEx.d(TAG, "onActivityResultCallBack resultCode " + resultCode);

        OrderFragment_ fragment = (OrderFragment_) mlistFragments.get
                (miCurSelectedFragmentPosition);

        if (resultCode == ProviderResultActivity.CODE_REFUND_SUCCESS) {
            fragment.refundSuccess();
        } else if (resultCode == ProviderResultActivity.CODE_PAY_ECO_SUCCESS) {
            fragment.payEcoSuccess(intent);
        } else if (resultCode == ProviderResultActivity.CODE_ADD_SHOP_REVIEW) {
            fragment.addShopReviewSuccess(intent);
        } else if (resultCode == ProviderResultActivity.CODE_UPDATE_ORDER) {
            String strOrder = intent.getStringExtra("order");
            if (!TextUtils.isEmpty(strOrder)) {
                Order order = JsonSerializerFactory.Create().decode(strOrder, Order.class);
                fragment.refreshItem(order);
            }
        } else if (resultCode == ProviderResultActivity.CODE_DISTINGUISH_STATUS) {
            fragment.refreshItem();
        } else if (resultCode == ProviderResultActivity.CODE_SEND_GOODS_SUCCESS) {
            fragment.refreshItem();
        } else if (resultCode == ProviderResultActivity.CODE_DEL_ORDER) {
            fragment.delItem();
        }
    }

    @Override
    public void onFragmentCreated() {
        refreshCurFragment();
    }
}
