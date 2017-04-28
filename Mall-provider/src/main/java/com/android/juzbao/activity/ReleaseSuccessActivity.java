package com.android.juzbao.activity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

import android.os.Bundle;

import com.android.zcomponent.views.swipebacklayout.lib.app.SwipeBackActivity;


/**
 * <p>
 * Description:  发布成功 
 * </p>
 * 
 * @ClassName:ReleaseSuccessActivity
 * @author: wei
 * @date: 2015-11-11
 * 
 */
@EActivity(resName = "activity_release_success")
public class ReleaseSuccessActivity extends SwipeBackActivity
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }
	
	@AfterViews
    void initUI()
    {
	    getTitleBar().setTitleText("发布成功");
	}
	
    @Click(resName = "btn_share_click")
    void onClickBtnShare()
    {

    }


	
}