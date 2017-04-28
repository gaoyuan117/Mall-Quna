
package com.android.juzbao.activity.wallet;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;

import com.android.mall.resource.R;
import com.android.juzbao.enumerate.WalletRecordType;
import com.android.juzbao.fragment.WalletRecordFragment_;
import com.android.zcomponent.adapter.FragmentViewPagerAdapter;
import com.android.zcomponent.common.uiframe.fragment.BaseFragment.OnFragmentCreatedListener;
import com.android.zcomponent.util.MyLayoutAdapter;
import com.android.zcomponent.views.PagerSlidingTabStrip;
import com.android.zcomponent.views.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * @ClassName:MyWalletRecordActivity
 * @Description: 我的账单
 * @author: WEI
 * @date: 2016-03-17
 * 
 */
@EActivity(R.layout.activity_my_order)
public class MyWalletRecordActivity extends SwipeBackActivity implements
		OnFragmentCreatedListener
{

	private final String TAG = "MyShopOrderActivity";

	@ViewById(R.id.common_viewpaper_show)
	ViewPager mViewPager;

	@ViewById(R.id.common_pager_slide_tab_show)
	PagerSlidingTabStrip mPagerSlidingTabStrip;

	private List<Fragment> mlistFragments;

	private List<String> mlistTitle;

	private int miCurSelectedFragmentPosition = 0;
	
	private boolean isExpense;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

	}

	@AfterViews
	void init()
	{
		isExpense = getIntent().getBooleanExtra("expense", false);
		if (isExpense)
		{
			getTitleBar().setTitleText("支出明细");
		}
		else
		{
			getTitleBar().setTitleText("收入明细");	
		}
		
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
		addFragment();
		FragmentViewPagerAdapter adapter =
				new FragmentViewPagerAdapter(getSupportFragmentManager(),
						mViewPager, mlistFragments);
		mViewPager.setAdapter(adapter);
		mPagerSlidingTabStrip.setTabPaddingLeftRight(10);
		mPagerSlidingTabStrip.setIndicatorColorResource(R.color.red);
		mPagerSlidingTabStrip.setTextSelectColorResource(R.color.red);
		mPagerSlidingTabStrip.setTabWidth((int) (MyLayoutAdapter
				.getInstance().getScreenWidth() / mlistTitle.size()));
		mPagerSlidingTabStrip.setViewPager(mViewPager, mlistTitle);
		mViewPager.setCurrentItem(miCurSelectedFragmentPosition);
	}

	private void addFragment()
	{
		List<WalletRecordType> orderStatus = new ArrayList<WalletRecordType>();

		if (isExpense)
		{
			orderStatus.add(WalletRecordType.OUT_PAYMENT);
			orderStatus.add(WalletRecordType.OUT_WITHDRAW);
			orderStatus.add(WalletRecordType.OUT_FREEZE);
		}
		else
		{
			orderStatus.add(WalletRecordType.IN_RECHARGE);
			orderStatus.add(WalletRecordType.IN_INCOME);
			orderStatus.add(WalletRecordType.IN_UNFREEZE);
		}

		mlistFragments = new ArrayList<Fragment>();
		mlistTitle = new ArrayList<String>();
		
		for (WalletRecordType status : orderStatus)
		{
			WalletRecordFragment_ takeoutFragment = new WalletRecordFragment_();
			takeoutFragment.setWalletRecordType(status);
			takeoutFragment.setOnFragmentCreatedListener(this);
			mlistFragments.add(takeoutFragment);
			mlistTitle.add(status.getName());
		}
	}

	private void refreshCurFragment()
	{
		getCurrentFragment().queryListData();
	}
	
	private WalletRecordFragment_ getCurrentFragment()
	{
		WalletRecordFragment_ fragment =
				(WalletRecordFragment_) mlistFragments
						.get(miCurSelectedFragmentPosition);
		return fragment;
	}

	@Override
	public void onFragmentCreated()
	{
		refreshCurFragment();
	}
}
