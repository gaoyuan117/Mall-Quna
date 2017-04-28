
package com.android.juzbao.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.android.mall.resource.R;
import com.android.juzbao.enumerate.AuctionType;
import com.android.juzbao.fragment.AuctionBeginFragment_;
import com.android.juzbao.fragment.AuctionSelectedFragment_;
import com.android.zcomponent.activity.BaseNavgationActivity;
import com.android.zcomponent.util.MyLayoutAdapter;

public class AuctionActivity extends BaseNavgationActivity
{

	private String[] titles = { "拍卖精选", "即将开始", "马上结束", "0元起拍" };

	private int[] drawables = { R.drawable.tabhost_auction1_bg_selector,
			R.drawable.tabhost_auction2_bg_selector,
			R.drawable.tabhost_auction3_bg_selector,
			R.drawable.tabhost_auction4_bg_selector };

	private Fragment[] fragment = { new AuctionSelectedFragment_(),
			new AuctionBeginFragment_(), new AuctionBeginFragment_(),
			new AuctionBeginFragment_() };

	@Override
	public void onCreate(Bundle arg0)
	{
		((AuctionBeginFragment_) fragment[1]).setAuctionType(AuctionType.START);
		((AuctionBeginFragment_) fragment[2]).setAuctionType(AuctionType.END);
		((AuctionBeginFragment_) fragment[3]).setAuctionType(AuctionType.ZERO);

		super.onCreate(arg0);
		setTitleBarVisibility(View.VISIBLE);
		getTitleBar().setTitleText("拍卖精选 ");
	}

	@Override
	public void showCurrentTab(int position)
	{
		super.showCurrentTab(position);

		if (position == 0)
		{
			getTitleBar().setTitleText("拍卖精选");
		}
		else if (position == 1)
		{
			getTitleBar().setTitleText("即将开始");
		}
		else if (position == 2)
		{
			getTitleBar().setTitleText("马上结束");
		}
		else if (position == 3)
		{
			getTitleBar().setTitleText("0元起拍");
		}
	}

	@Override
	public int[] getDrawables()
	{
		return drawables;
	}

	@Override
	public String[] getTitles()
	{
		return titles;
	}

	@Override
	public int getTabSelectedBackground()
	{
		return R.drawable.transparent;
	}

	@Override
	public int getTabSelectedTextColor()
	{
		return R.color.tabhost_tab_tv_color_selector;
	}

	@Override
	public Fragment[] getFragments()
	{
		return fragment;
	}

	@Override
	public int getNavBackgroundResource()
	{
		return R.drawable.ltgray;
	}

	@Override
	public int getNavHeight()
	{
		return (int) (54 * MyLayoutAdapter.getInstance().getDensityRatio());
	}

	@Override
	public int getBackgroundResource()
	{
		return R.drawable.common_bg;
	}

	@Override
	public boolean isSetContentPadding()
	{
		return true;
	}

	@Override
	public int getTextDipSize()
	{
		return getResources().getDimensionPixelSize(R.dimen.dimen_text_sm);
	}

}
