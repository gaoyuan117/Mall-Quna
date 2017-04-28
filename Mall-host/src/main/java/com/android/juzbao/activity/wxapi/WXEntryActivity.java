package com.android.juzbao.activity.wxapi;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.util.Log;

import com.android.mall.resource.R;
import com.android.zcomponent.util.ShowMsg;
import com.android.zcomponent.util.StringUtil;
import com.android.zcomponent.views.swipebacklayout.lib.app.SwipeBackActivity;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXEntryActivity extends SwipeBackActivity implements
		IWXAPIEventHandler
{
	private static final String TAG = "WXEntryActivity";

	private IWXAPI api;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wxentry);
		// 通过WXAPIFactory工厂，获取IWXAPI的实例
		try
		{
			ApplicationInfo appInfo =
					getPackageManager().getApplicationInfo(
							getPackageName(),
							PackageManager.GET_META_DATA);
			String appId = appInfo.metaData.getString("WECHAT_APPKEY");
			if (!StringUtil.isEmptyString(appId))
			{
				api = WXAPIFactory.createWXAPI(this, appId, false);
				api.handleIntent(getIntent(), this);
			}
		}
		catch (NameNotFoundException e)
		{
			e.printStackTrace();
		}
		
	}

	@Override
	public void onReq(BaseReq arg0)
	{
		Log.d(TAG, "onReq");
	}

	@Override
	public void onResp(BaseResp resp)
	{
		Log.d(TAG, "onResp");
		int result = 0;
		switch (resp.errCode)
		{
		case BaseResp.ErrCode.ERR_OK:
		{			
			result = R.string.errcode_success;
			break;
		}
		case BaseResp.ErrCode.ERR_USER_CANCEL:
			result = R.string.errcode_cancel;
			break;
		case BaseResp.ErrCode.ERR_AUTH_DENIED:
			result = R.string.errcode_deny;
			break;
		default:
			result = R.string.errcode_unknown;
			break;
		}
		ShowMsg.showToast(getApplicationContext(), result);
		finish();
	}

	@Override
	protected void onNewIntent(Intent intent)
	{
		super.onNewIntent(intent);
		setIntent(intent);
		api.handleIntent(intent, this);
	}
}
