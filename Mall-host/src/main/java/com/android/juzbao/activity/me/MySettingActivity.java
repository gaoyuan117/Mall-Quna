
package com.android.juzbao.activity.me;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.mall.resource.R;
import com.android.juzbao.activity.account.LoginActivity_;
import com.android.juzbao.application.BaseApplication;
import com.android.juzbao.model.AccountBusiness;
import com.android.zcomponent.views.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * <p>
 * Description: 设置
 * </p>
 * 
 * @ClassName:MySettingActivity
 * @author: wei
 * @date: 2015-11-10
 * 
 */
@EActivity(R.layout.activity_my_setting)
public class MySettingActivity extends SwipeBackActivity
{

	@ViewById(R.id.tvew_logout_show)
	TextView mtvewLogout;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	@AfterViews
	void initUI()
	{
		getTitleBar().setTitleText("设置");

		if (BaseApplication.isLogin())
		{
			mtvewLogout.setVisibility(View.VISIBLE);
		}
		else
		{
			mtvewLogout.setVisibility(View.GONE);
		}
	}

	@Click(R.id.rlayout_my_info_click)
	void onClickRlayoutMyInfo()
	{
        if (!BaseApplication.isLogin())
        {
        	getIntentHandle().intentToActivity(LoginActivity_.class);
        	return;
        }
        
        getIntentHandle().intentToActivity(MyInfoActivity_.class);
	}

	@Click(R.id.rlayout_my_account_click)
	void onClickRlayoutMyAccount()
	{
        getIntentHandle().intentToActivity(MyAccountActivity_.class);
	}

	@Click(R.id.rlayout_my_about_click)
	void onClickRlayoutMyAbout()
	{
        getIntentHandle().intentToActivity(MyAboutActivity_.class);
	}

	@Click(R.id.rlayout_my_message_click)
	void onClickRlayoutMySetting()
	{
		getIntentHandle().intentToActivity(MyMessageActivity_.class);
	}

	@Click(R.id.tvew_logout_show)
	void onClickTvewLogout()
	{
		AccountBusiness.doLogout();
		mtvewLogout.setVisibility(View.GONE);
	}

}