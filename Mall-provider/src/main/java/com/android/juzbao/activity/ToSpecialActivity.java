
package com.android.juzbao.activity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.juzbao.enumerate.SpecialType;
import com.android.zcomponent.views.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * <p>
 * Description: 发布到专题
 * </p>
 * 
 * @ClassName:ToSpecialActivity
 * @author: wei
 * @date: 2015-11-11
 * 
 */
@EActivity(resName = "activity_to_special")
public class ToSpecialActivity extends SwipeBackActivity
{

	@ViewById(resName = "check_to_special_qiang_click")
	CheckBox mCheckBoxQiang;

	@ViewById(resName = "check_to_special_xuan_click")
	CheckBox mCheckBoxXuan;

	@ViewById(resName = "tvew_gift_category")
	TextView mTvewGiftCategory;

	@ViewById(resName = "progressbar_categoryz_show")
	ProgressBar mProgressBar;

	private long mProductId;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	@AfterViews
	void initUI()
	{
		getTitleBar().setTitleText("发布到专题");
		Intent intent = getIntent();
		mProductId = intent.getLongExtra("id", 0);

		mCheckBoxQiang.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked)
			{
				if (isChecked)
				{
					intentToPanicBuyingActivity();
					mCheckBoxXuan.setChecked(false);
				}
			}
		});

		mCheckBoxXuan.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked)
			{
				if (isChecked)
				{
					intentToPanicBuyingActivity();
					mCheckBoxQiang.setChecked(false);
				}
			}
		});
	}

	@Click(resName = "btn_to_special_ok_click")
	void onClickBtnToSpecialOk()
	{
		if (mCheckBoxQiang.isChecked())
		{
			intentToPanicBuyingActivity();
		}
		else if (mCheckBoxXuan.isChecked())
		{
			intentToPanicBuyingActivity();
		}
	}

	private void intentToPanicBuyingActivity()
	{
		Bundle bundle = new Bundle();
		bundle.putLong("id", mProductId);
		if (mCheckBoxQiang.isChecked())
		{
			bundle.putString("type", SpecialType.PANIC.getValue());
		}
		else if (mCheckBoxXuan.isChecked())
		{
			bundle.putString("type", SpecialType.GIFT.getValue());
		}
		getIntentHandle().intentToActivity(bundle, PanicBuyingActivity_.class);
	}

	@Click(resName = "llayout_to_special_qianggou_click")
	void onClickLlayoutToSpecialQianggou()
	{
		mCheckBoxQiang.setChecked(!mCheckBoxQiang.isChecked());
	}

	@Click(resName = "llayout_to_special_liwu_click")
	void onClickLlayoutToSpecialLiwu()
	{
		mCheckBoxXuan.setChecked(!mCheckBoxXuan.isChecked());
	}
}