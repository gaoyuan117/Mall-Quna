
package com.android.juzbao.activity.me;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.android.mall.resource.R;
import com.android.juzbao.activity.account.ModifyPwdActivity_;
import com.server.api.model.Account;
import com.server.api.model.Mobile;
import com.android.juzbao.model.AccountBusiness;
import com.android.juzbao.constant.Config;
import com.android.juzbao.constant.ResultActivity;
import com.android.juzbao.util.CommonValidate;
import com.android.zcomponent.http.api.model.MessageData;
import com.android.zcomponent.http.constant.ErrorCode;
import com.android.zcomponent.util.SettingsBase;
import com.android.zcomponent.util.ShowMsg;
import com.android.zcomponent.util.StringUtil;
import com.android.zcomponent.views.swipebacklayout.lib.app.SwipeBackActivity;
import com.server.api.service.AccountService;

/**
 * <p>
 * Description: 账户与安全
 * </p>
 * 
 * @ClassName:MyAccountActivity
 * @author: wei
 * @date: 2015-11-10
 * 
 */
@EActivity(R.layout.activity_my_account)
public class MyAccountActivity extends SwipeBackActivity
{

	@ViewById(R.id.tvew_account_name_show)
	TextView mTvewAccountName;

	@ViewById(R.id.tvew_my_phone_show)
	TextView mTvewMyPhone;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	@AfterViews
	void initUI()
	{
		getTitleBar().setTitleText("账户与安全");
		
		String isWeiboLogin =
				SettingsBase.getInstance().readStringByKey(Config.WEIBO_LOGIN);
		if ("true".equals(isWeiboLogin))
		{
			View view = findViewById(R.id.rlayout_my_password_click);
			view.setVisibility(View.GONE);
		}
		
		AccountBusiness.getMobile(getHttpDataLoader());
		AccountBusiness.queryBaseInfo(getHttpDataLoader());
	}

	@Override
	public void onRecvMsg(MessageData msg) throws Exception
	{
		if (msg.valiateReq(AccountService.GetMobileRequest.class)){
			Mobile response = (Mobile) msg.getRspObject();
			if (null != response)
			{
				if (response.code == ErrorCode.INT_QUERY_DATA_SUCCESS)
				{
					if (null != response.data
							&& !TextUtils.isEmpty(response.data.mobile))
					{
						mTvewMyPhone.setText(StringUtil
								.formatMobile(response.data.mobile));
					}
				}
			}
			else
			{
				ShowMsg.showToast(this, msg, "获取手机号码失败");
			}
		}else if (msg.valiateReq(AccountService.BaseInfoRequest.class)){
			Account response = (Account) msg.getRspObject();
			if (CommonValidate.validateQueryState(this, msg, response))
			{
				mTvewAccountName.setText(response.Data.username);
			}
		}
	}

	@Click(R.id.rlayout_my_account_click)
	void onClickRlayoutMyAccount()
	{
    
	}

	@Click(R.id.rlayout_my_phone_click)
	void onClickRlayoutMyPhone()
	{
        getIntentHandle().intentToActivity(MyModifyMobileActivity_.class);
	}

	@Click(R.id.rlayout_my_password_click)
	void onClickRlayoutMyPassword()
	{
        getIntentHandle().intentToActivity(ModifyPwdActivity_.class);
	}
	
    @Override
    public void onActivityResultCallBack(int resultCode, Intent intent)
    {
    	if (resultCode == ResultActivity.CODE_EDIT_MOBILE_SUCCESS)
    	{
    		AccountBusiness.getMobile(getHttpDataLoader());
    	}
    }
}