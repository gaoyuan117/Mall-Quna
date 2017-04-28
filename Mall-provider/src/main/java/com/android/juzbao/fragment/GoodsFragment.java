
package com.android.juzbao.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.android.juzbao.constant.ProviderResultActivity;
import com.android.juzbao.enumerate.GoodsStatus;
import com.android.juzbao.provider.R;
import com.android.zcomponent.common.uiframe.fragment.BaseFragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/**
 * <p>
 * Description: 我的购买订单列表
 * </p>
 * 
 * @ClassName:ShopOrderFragment
 * @author: wei
 * @date: 2015-11-10
 * 
 */
@EFragment(resName = "fragment_goods")
public class GoodsFragment extends BaseFragment
{
	private GoodsStatus mGoodsStatus;

	@ViewById(resName = "llayout_putong_goods_type")
	LinearLayout mLlayoutPutongType;

	@ViewById(resName = "rbtn_goods_manage_sell_click")
	RadioButton mRadionBtnSelling;

	@ViewById(resName = "rbtn_goods_manage_warehouse_click")
	RadioButton mRadionBtnWarehouse;

	@ViewById(resName = "radiogroup_show")
	RadioGroup mRadionGroup;

	@ViewById(resName = "flayout_putaway")
	FrameLayout mFlayoutPutaway;

	@ViewById(resName = "flayout_halt")
	FrameLayout mFlayoutHalt;

	private GoodStatusFragment mPutAwayFragment;

	private GoodStatusFragment_ mHaltFragment;
	
	private boolean isFragmentInstantiate = false;

	public GoodsFragment()
	{

	}

	public void setOrderStatus(GoodsStatus orderStatus)
	{
		mGoodsStatus = orderStatus;
	}

	@AfterViews
	void initUI()
	{
		mPutAwayFragment = new GoodStatusFragment_();
		mPutAwayFragment.setOrderStatus(mGoodsStatus, 1);
		mPutAwayFragment.setTotalCountView(mRadionBtnSelling, mRadionBtnWarehouse);
		mPutAwayFragment.setOnFragmentCreatedListener(new OnFragmentCreatedListener()
		{
			
			@Override
			public void onFragmentCreated()
			{
				mPutAwayFragment.queryListViewData();
			}
		});
		
		mHaltFragment = new GoodStatusFragment_();
		mHaltFragment.setOrderStatus(mGoodsStatus, 0);
		mHaltFragment.setTotalCountView(mRadionBtnSelling, mRadionBtnWarehouse);
		mHaltFragment.setOnFragmentCreatedListener(new OnFragmentCreatedListener()
		{
			
			@Override
			public void onFragmentCreated()
			{
				mHaltFragment.queryListViewData();
			}
		});
		addFragment(R.id.flayout_putaway, mPutAwayFragment);
		addFragment(R.id.flayout_halt, mHaltFragment);
		
		mFlayoutPutaway.setVisibility(View.VISIBLE);
		mFlayoutHalt.setVisibility(View.GONE);
		mLlayoutPutongType.setVisibility(View.VISIBLE);
		mRadionGroup.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId)
			{
				if (checkedId == R.id.rbtn_goods_manage_sell_click)
				{
					mFlayoutPutaway.setVisibility(View.VISIBLE);
					mFlayoutHalt.setVisibility(View.GONE);
				}
				else
				{
					mFlayoutPutaway.setVisibility(View.GONE);
					mFlayoutHalt.setVisibility(View.VISIBLE);
				}
			}
		});
	}
	
	public void refreshData(boolean isRefreshByPull)
	{
		if (mRadionBtnSelling.isChecked())
		{
			mPutAwayFragment.refreshData(isRefreshByPull);
		}
		else
		{
			mHaltFragment.refreshData(isRefreshByPull);
		}
	}
	
	public void queryListViewData()
	{
		if (!isFragmentInstantiate)
		{
			isFragmentInstantiate = true;
			
			if (mPutAwayFragment.isCreated())
			{
				mPutAwayFragment.queryListViewData();
			}
			
			if (mHaltFragment.isCreated())
			{
				mHaltFragment.queryListViewData();
			}
		}
	}
	
	@Override
	public void onActivityResultCallBack(int resultCode, Intent intent)
	{
		if (ProviderResultActivity.CODE_EDIT_PRODUCT_PUTAWAY == resultCode)
		{
			mPutAwayFragment.refreshData(true);
			mHaltFragment.refreshData(true);
		}
	}
}