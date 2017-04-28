
package com.android.juzbao.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.android.mall.resource.R;
import com.android.juzbao.enumerate.PaincBuyType;
import com.android.juzbao.fragment.PaincBuyingFragment_;
import com.android.zcomponent.activity.BaseNavgationActivity;
import com.android.zcomponent.util.MyLayoutAdapter;

public class PaincBuyingActivity extends BaseNavgationActivity {

    private String[] titles = {"抢购会", "折扣最多", "最后疯抢"};

    private int[] drawables = {R.drawable.tabhost_painc_buying_selector,
            R.drawable.tabhost_painc_coupon_selector,
            R.drawable.tabhost_painc_timer_selector};

    private PaincBuyingFragment_[] fragment = {new PaincBuyingFragment_(),
            new PaincBuyingFragment_(), new PaincBuyingFragment_()};

    @Override
    public void onCreate(Bundle arg0) {
        fragment[0].setPaincBuyType(PaincBuyType.BUYING);
        fragment[1].setPaincBuyType(PaincBuyType.COUPON);
        fragment[2].setPaincBuyType(PaincBuyType.TIME);

        super.onCreate(arg0);
        getTitleBar().setTitleText("抢购会");
        setTitleBarVisibility(View.VISIBLE);
    }

    @Override
    public void showCurrentTab(int position) {
        super.showCurrentTab(position);

        if (position == 0) {
            getTitleBar().setTitleText("抢购会");
        } else if (position == 1) {
            getTitleBar().setTitleText("折扣最多");
        } else if (position == 2) {
            getTitleBar().setTitleText("最后疯抢");
        }
    }

    @Override
    public Fragment[] getFragments() {
        return fragment;
    }

    @Override
    public int[] getDrawables() {
        return drawables;
    }

    @Override
    public String[] getTitles() {
        return titles;
    }

    @Override
    public int getTabSelectedBackground() {
        return R.drawable.transparent;
    }

    @Override
    public int getTabSelectedTextColor() {
        return R.color.tabhost_tab_tv_color_selector1;
    }

    @Override
    public int getNavBackgroundResource() {
        return R.drawable.ltgray;
    }

    @Override
    public int getNavHeight() {
        return (int) (54 * MyLayoutAdapter.getInstance().getDensityRatio());
    }

    @Override
    public int getBackgroundResource() {
        return 0;
    }

    @Override
    public boolean isSetContentPadding() {
        return true;
    }

    @Override
    public int getTextDipSize() {
        return getResources().getDimensionPixelSize(R.dimen.dimen_text_sm);
    }
}
