
package com.android.juzbao.activity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;

import com.android.juzbao.constant.ProviderResultActivity;
import com.android.zcomponent.common.uiframe.FramewrokApplication;
import com.android.zcomponent.util.CommonUtil;
import com.android.zcomponent.views.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * <p>
 * Description: 店铺名称
 * </p>
 * 
 * @ClassName:ShopNameActivity
 * @author: wei
 * @date: 2015-11-11
 * 
 */
@EActivity(resName = "activity_shop_name")
public class ShopNameActivity extends SwipeBackActivity
{

	@ViewById(resName = "edittxt_shop_name_show")
	EditText mEdittxtShopName;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	@AfterViews
	void initUI()
	{
		getTitleBar().setTitleText("店铺名称");
		Intent intent = getIntent();
		String name = intent.getStringExtra("name");
		if (!TextUtils.isEmpty(name))
		{
			mEdittxtShopName.setText(name);
			CommonUtil.setEditViewSelectionEnd(mEdittxtShopName);
		}
	}

	@Click(resName = "btn_shop_name_save_click")
	void onClickBtnShopNameSave()
	{
		Intent intent = new Intent();
		intent.putExtra("name", mEdittxtShopName.getText().toString());
		FramewrokApplication.getInstance().setActivityResult(
				ProviderResultActivity.CODE_EDIT_SHOP_NAME, intent);
		finish();
	}

}