
package com.android.juzbao.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;

import com.android.juzbao.fragment.CourseDocFragment_;
import com.android.juzbao.fragment.CourseVideoFragment_;
import com.android.juzbao.provider.R;
import com.android.zcomponent.adapter.FragmentViewPagerAdapter;
import com.android.zcomponent.common.uiframe.fragment.BaseFragment.OnFragmentCreatedListener;
import com.android.zcomponent.util.MyLayoutAdapter;
import com.android.zcomponent.views.PagerSlidingTabStrip;
import com.android.zcomponent.views.swipebacklayout.lib.app.SwipeBackActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName:CourseActivity
 * @Description: 趣那学院
 * @author: WEI
 * @date: 2016-3-7
 * 
 */
@EActivity(resName = "activity_course")
public class CourseActivity extends SwipeBackActivity implements
		OnFragmentCreatedListener
{

	private final String TAG = "CourseActivity";

	@ViewById(resName = "viewpaper")
	ViewPager mViewPager;

	@ViewById(resName = "order_select_table_titlebar_tabs")
	PagerSlidingTabStrip mPagerSlidingTabStrip;

	private List<Fragment> mlistFragments;

	private List<String> mlistTitle;

	private int miCurSelectedFragmentPosition = 0;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

	}

	@AfterViews
	void init()
	{
		getTitleBar().setTitleText("小元学院");
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

		addShopOrderFragment();
		FragmentViewPagerAdapter adapter =
				new FragmentViewPagerAdapter(getSupportFragmentManager(),
						mViewPager, mlistFragments);
		mViewPager.setAdapter(adapter);

		mPagerSlidingTabStrip.setIndicatorColorResource(R.color.red);
		mPagerSlidingTabStrip.setTextSelectColorResource(R.color.red);
		mPagerSlidingTabStrip.setTabWidth((int) MyLayoutAdapter
				.getInstance().getScreenWidth());
		mPagerSlidingTabStrip.setViewPager(mViewPager, mlistTitle);
		mViewPager.setCurrentItem(miCurSelectedFragmentPosition);
	}

	private void addShopOrderFragment()
	{
		CourseDocFragment_ docFragment = new CourseDocFragment_();
		CourseVideoFragment_ videoFragment = new CourseVideoFragment_();

		docFragment.setOnFragmentCreatedListener(this);
		videoFragment.setOnFragmentCreatedListener(this);

		mlistFragments.add(docFragment);
//		mlistFragments.add(videoFragment);
		mlistTitle.add("课程问题");
//		mlistTitle.add("课程视频");
	}

	private void refreshCurFragment()
	{
		if (mlistFragments.get(miCurSelectedFragmentPosition) instanceof CourseDocFragment_)
		{
			CourseDocFragment_ fragment =
					(CourseDocFragment_) mlistFragments
							.get(miCurSelectedFragmentPosition);
			fragment.queryListData();
		}
		else
		{
			CourseVideoFragment_ fragment =
					(CourseVideoFragment_) mlistFragments
							.get(miCurSelectedFragmentPosition);
			fragment.queryListData();
		}

	}

	@Override
	public void onFragmentCreated()
	{
		refreshCurFragment();
	}
}
