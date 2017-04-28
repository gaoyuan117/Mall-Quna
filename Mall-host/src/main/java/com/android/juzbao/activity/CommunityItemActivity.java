package com.android.juzbao.activity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import com.android.mall.resource.R;
import android.os.Bundle;

import com.android.zcomponent.views.swipebacklayout.lib.app.SwipeBackActivity;


/**
 * <p>
 * Description:  社区 
 * </p>
 * 
 * @ClassName:CommunityItemActivity
 * @author: wei
 * @date: 2015-11-10
 * 
 */
@EActivity(R.layout.activity_community_item)
public class CommunityItemActivity extends SwipeBackActivity
{


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }
	
	@AfterViews
    void initUI()
    {
	
	}
	

	
}