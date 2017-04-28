package com.android.juzbao.activity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import com.android.mall.resource.R;
import com.android.juzbao.activity.account.LoginActivity_;
import com.server.api.model.CommonReturn;
import com.android.juzbao.application.BaseApplication;
import com.android.juzbao.model.HelpBusiness;
import com.android.juzbao.util.CommonValidate;
import com.android.zcomponent.annotation.ZTitleMore;
import com.android.zcomponent.http.api.model.MessageData;
import com.android.zcomponent.util.ShowMsg;
import com.android.zcomponent.views.swipebacklayout.lib.app.SwipeBackActivity;
import com.server.api.service.HelpService;


/**
 * <p>
 * Description:  意见反馈 
 * </p>
 * 
 * @ClassName:OptionActivity
 * @author: wei
 * @date: 2015-11-10
 * 
 */
@EActivity(R.layout.activity_feedback)
@ZTitleMore(false)
public class OptionActivity extends SwipeBackActivity
{
	@ViewById(R.id.editvew_content_show)
    EditText mEditTvewContent;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }
	
	@AfterViews
    void initUI()
    {
	    getTitleBar().setTitleText("意见反馈");
	}
	
	@Override
	public void onRecvMsg(MessageData msg) throws Exception
	{
		if (msg.valiateReq(HelpService.FeedbackRequest.class)){
			CommonReturn response = (CommonReturn) msg.getRspObject();

			if (CommonValidate.validateQueryState(this, msg, response, "提交失败"))
			{
				ShowMsg.showToast(getApplicationContext(), "提交成功");
				finish();
			}
		}
	}
	
    @Click(R.id.tvew_submit_click)
    void onClickTvewSubmit()
    {
    	if (!BaseApplication.isLogin())
		{
			getIntentHandle().intentToActivity(LoginActivity_.class);
			return;
		}
    	
    	String content = mEditTvewContent.getText().toString();
    	
        if (!TextUtils.isEmpty(content))
        {
        	HelpBusiness.queryFeedback(getHttpDataLoader(), content);
        	showWaitDialog(1, false, R.string.common_submit_data);
        }
        else
        {
        	ShowMsg.showToast(getApplicationContext(), "请输入意见内容");
        }
    }
	
}