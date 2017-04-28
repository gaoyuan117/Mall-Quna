
package com.android.juzbao.activity;

import com.android.juzbao.enumerate.ProviderOrderStatus;
import com.android.juzbao.fragment.OrderFragment_;
import com.android.juzbao.provider.R;
import com.android.zcomponent.common.uiframe.fragment.BaseFragment.OnFragmentCreatedListener;
import com.android.zcomponent.views.swipebacklayout.lib.app.SwipeBackActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

@EActivity(resName = "activity_my_order_more")
public class MyMoreOrderActivity extends SwipeBackActivity {

    private OrderFragment_ mOrderFragment;

    @AfterViews void initUI() {
        ProviderOrderStatus orderStatus = (ProviderOrderStatus) getIntent().getSerializableExtra
                ("orderstatus");
        getTitleBar().setTitleText(orderStatus.getName() + "订单");
        mOrderFragment = new OrderFragment_();
        mOrderFragment.setOrderStatus(orderStatus);
        mOrderFragment.setOnFragmentCreatedListener(new OnFragmentCreatedListener() {

            @Override
            public void onFragmentCreated() {
                mOrderFragment.queryListData();
            }
        });
        addFragment(R.id.flayout_more_order, mOrderFragment);
    }
}