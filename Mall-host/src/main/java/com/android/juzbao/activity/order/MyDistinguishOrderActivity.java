
package com.android.juzbao.activity.order;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;

import com.android.mall.resource.R;
import com.android.juzbao.constant.GlobalConst;
import com.android.juzbao.constant.ProviderResultActivity;
import com.android.juzbao.constant.ResultActivity;
import com.android.juzbao.enumerate.DistinguishStatus;
import com.android.juzbao.fragment.DistinguishOrderFragment_;
import com.android.zcomponent.adapter.FragmentViewPagerAdapter;
import com.android.zcomponent.common.uiframe.fragment.BaseFragment.OnFragmentCreatedListener;
import com.android.zcomponent.util.LogEx;
import com.android.zcomponent.util.MyLayoutAdapter;
import com.android.zcomponent.views.PagerSlidingTabStrip;
import com.android.zcomponent.views.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * @Description: 我的鉴定
 */
@EActivity(R.layout.activity_my_order)
public class MyDistinguishOrderActivity extends SwipeBackActivity implements
		OnFragmentCreatedListener
{

	private final String TAG = "PersonalMyReserveOrderActivity";

	@ViewById(R.id.common_viewpaper_show)
	ViewPager mViewPager;

	@ViewById(R.id.common_pager_slide_tab_show)
	PagerSlidingTabStrip mPagerSlidingTabStrip;

	private List<Fragment> mlistFragments;

	private List<String> mlistTitle;

	private int miCurSelectedFragmentPosition = 0;

	private DistinguishOrderFragment_ rechargeFragment;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

	}

	@AfterViews
	void init()
	{
		getTitleBar().setTitleText("我的鉴定");
		initUI();
	}

	private void initUI()
	{
		bindWidget();
		initViewPager();
	}

	private void bindWidget()
	{
		mPagerSlidingTabStrip
				.setOnPageChangeListener(new OnPageChangeListener()
				{

					@Override
					public void onPageSelected(int position)
					{
						miCurSelectedFragmentPosition = position;
						refreshCurFragment();
					}

					@Override
					public void onPageScrolled(int arg0, float arg1, int arg2)
					{

					}

					@Override
					public void onPageScrollStateChanged(int arg0)
					{

					}
				});
	}

	public void initViewPager()
	{
		mlistFragments = new ArrayList<Fragment>();
		mlistTitle = new ArrayList<String>();

		addDistinguishOrderFragment();
		FragmentViewPagerAdapter adapter =
				new FragmentViewPagerAdapter(getSupportFragmentManager(),
						mViewPager, mlistFragments);
		mViewPager.setAdapter(adapter);
		mPagerSlidingTabStrip.setTabPaddingLeftRight(10);
		mPagerSlidingTabStrip.setIndicatorColorResource(R.color.red);
		mPagerSlidingTabStrip.setTextSelectColorResource(R.color.red);
		if (MyLayoutAdapter.getInstance().getScreenWidth() >= 700)
		{
			mPagerSlidingTabStrip.setTabWidth((int) (MyLayoutAdapter
					.getInstance().getScreenWidth() / 4.5));
		}
		else
		{
			mPagerSlidingTabStrip.setTabWidth((int) (MyLayoutAdapter
					.getInstance().getScreenWidth() / 3.5));
		}

		mPagerSlidingTabStrip.setViewPager(mViewPager, mlistTitle);
		mViewPager.setCurrentItem(miCurSelectedFragmentPosition);
	}

	private void addDistinguishOrderFragment()
	{
		Intent intent = getIntent();
		String strOrderStatus = intent.getStringExtra(GlobalConst.ORDER_STATUS);
		List<DistinguishStatus> orderStatus = DistinguishStatus.toList();
		int position = 0;
		for (DistinguishStatus status : orderStatus)
		{
			DistinguishOrderFragment_ takeoutFragment = new DistinguishOrderFragment_();
			takeoutFragment.setOrderStatus(status);
			takeoutFragment.setOnFragmentCreatedListener(this);
			mlistFragments.add(takeoutFragment);
			mlistTitle.add(status.getName());
			if (strOrderStatus.equals(status.getValue()))
			{
				miCurSelectedFragmentPosition = position;
			}
			position++;
		}
	}

	private void refreshCurFragment()
	{
		DistinguishOrderFragment_ fragment =
				(DistinguishOrderFragment_) mlistFragments
						.get(miCurSelectedFragmentPosition);
		fragment.queryListViewData();
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent data)
	{
	}

	@Override
	public void onActivityResultCallBack(int resultCode, Intent intent)
	{
		LogEx.d(TAG, "onActivityResultCallBack resultCode " + resultCode);

		DistinguishOrderFragment_ fragment =
				(DistinguishOrderFragment_) mlistFragments
						.get(miCurSelectedFragmentPosition);
		if (resultCode == ResultActivity.CODE_REFUND_SUCCESS)
		{
			fragment.refundSuccess();
		}
		else if (resultCode == ResultActivity.CODE_PAY_ECO_SUCCESS)
		{
			fragment.payEcoSuccess(intent);
		}
		else if (resultCode == ResultActivity.CODE_DEL_SUCCESS)
		{
			fragment.delSuccess();
		}
		else if (resultCode == ProviderResultActivity.CODE_DISTINGUISH_STATUS)
		{
			fragment.refreshDistinguishStatus();
		}
	}

	@Override
	public void onFragmentCreated()
	{
		refreshCurFragment();
	}
}
