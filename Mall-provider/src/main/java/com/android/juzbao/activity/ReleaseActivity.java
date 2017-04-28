package com.android.juzbao.activity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

import android.os.Bundle;

import com.android.zcomponent.views.swipebacklayout.lib.app.SwipeBackActivity;


/**
 * <p>
 * Description:  发布宝贝 
 * </p>
 * 
 * @ClassName:ReleaseActivity
 * @author: wei
 * @date: 2015-11-10
 * 
 */
@EActivity(resName = "activity_release")
public class ReleaseActivity extends SwipeBackActivity
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }
	
	@AfterViews
    void initUI()
    {
	    getTitleBar().setTitleText("发布宝贝 ");
	}
	
    @Click(resName = "llayout_release_goods_one_click")
    void onClickLlayoutReleaseGoodsOne()
    {
    	getIntentHandle().intentToActivity(ReleaseCommodityGoodsActivity_.class);
    }

    @Click(resName = "llayout_release_goods_two_click")
    void onClickLlayoutReleaseGoodsTwo()
    {
    	getIntentHandle().intentToActivity(ReleaseAuctionGoodsActivity_.class);
    }

    @Click(resName = "llayout_release_goods_three_click")
    void onClickLlayoutReleaseGoodsThree()
    {
    	getIntentHandle().intentToActivity(ReleaseCustomizationGoodsActivity_.class);
    }
	
}