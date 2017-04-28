
package com.android.juzbao.activity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import com.android.zcomponent.common.uiframe.FramewrokApplication;
import com.android.zcomponent.util.CommonUtil;
import com.android.zcomponent.views.swipebacklayout.lib.app.SwipeBackActivity;

import android.content.Intent;
import android.os.Bundle;

import com.android.juzbao.provider.R;
import com.android.juzbao.constant.ProviderResultActivity;

import android.text.TextUtils;
import android.widget.EditText;

/**
 * <p>
 * Description: 店铺介绍
 * </p>
 * 
 * @ClassName:ShopIntroduceActivity
 * @author: wei
 * @date: 2015-11-11
 * 
 */
@EActivity(resName = "activity_shop_introduce")
public class ShopIntroduceActivity extends SwipeBackActivity
{

	@ViewById(resName = "edittxt_shop_introduce_show")
	EditText mEdittxtShopIntroduce;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	@AfterViews
	void initUI()
	{
		getTitleBar().setTitleText("店铺介绍");
		Intent intent = getIntent();
		String desc = intent.getStringExtra("desc");
		if (!TextUtils.isEmpty(desc))
		{
			mEdittxtShopIntroduce.setText(desc);
			CommonUtil.setEditViewSelectionEnd(mEdittxtShopIntroduce);
		}
	}

	@Click(resName = "btn_shop_introduce_save_click")
	void onClickBtnShopIntroduceSave()
	{
		Intent intent = new Intent();
		intent.putExtra("desc", mEdittxtShopIntroduce.getText().toString());
		FramewrokApplication.getInstance().setActivityResult(
				ProviderResultActivity.CODE_EDIT_SHOP_DESC, intent);
		finish();
	}

}