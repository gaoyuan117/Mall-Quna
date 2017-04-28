
package com.android.juzbao.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;

import com.android.juzbao.application.BaseApplication;
import com.android.juzbao.constant.ResultActivity;
import com.android.mall.resource.R;
import com.android.zcomponent.util.ClientInfo;
import com.android.zcomponent.util.ShowMsg;
import com.android.zcomponent.util.StringUtil;
import com.android.zcomponent.views.swipebacklayout.lib.app.SwipeBackActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * <p>
 * Description:
 * </p>
 * 
 * @ClassName:PriceInputActivity
 * @author: wei
 * @date: 2015-11-10
 * 
 */
@EActivity(R.layout.activity_input_price)
public class PriceInputActivity extends SwipeBackActivity
{

	@ViewById(R.id.editvew_price_start_show)
	EditText mEditTvewStart;

	@ViewById(R.id.editvew_price_end_show)
	EditText mEditTvewEnd;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	@AfterViews
	void initUI()
	{
		getTitleBar().setTitleText("设置价格区间");

		mEditTvewStart
				.addTextChangedListener(new StringUtil.DecimalTextWatcher(
						mEditTvewStart, 2));
		mEditTvewEnd.addTextChangedListener(new StringUtil.DecimalTextWatcher(
				mEditTvewEnd, 2));
	}

	@Click(R.id.tvew_submit_click)
	void onClickTvewSubmit()
	{
		if (TextUtils.isEmpty(mEditTvewStart.getText().toString()))
		{
			ShowMsg.showToast(this, "请输入最低价格");
			return;
		}

		if (TextUtils.isEmpty(mEditTvewEnd.getText().toString()))
		{
			ShowMsg.showToast(this, "请输入最高价格");
			return;
		}

		try
		{
			double priceStart =
					Double.parseDouble(mEditTvewStart.getText().toString());
			double priceEnd = Double.parseDouble(mEditTvewEnd.getText().toString());

			if (priceStart > priceEnd)
			{
				ShowMsg.showToast(this, "最低价格应小于最高价格");
				return;
			}

			ClientInfo.closeSoftInput(mEditTvewEnd, this);
			Intent intent = new Intent();
			intent.putExtra("priceStart", priceStart);
			intent.putExtra("priceEnd", priceEnd);
			BaseApplication.getInstance().setActivityResult(
					ResultActivity.CODE_EDIT_PRICE, intent);
			finish();
		}
		catch (Exception e)
		{
		}
	}
}