package com.android.juzbao.activity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import com.android.zcomponent.views.swipebacklayout.lib.app.SwipeBackActivity;
import android.os.Bundle;

import com.android.juzbao.provider.R;


/**
 * <p>
 * Description:  选择商品 
 * </p>
 * 
 * @ClassName:SelectCommodityActivity
 * @author: wei
 * @date: 2015-11-11
 * 
 */
@EActivity(resName = "activity_select_commodity")
public class SelectCommodityActivity extends SwipeBackActivity
{


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }
	
	@AfterViews
    void initUI()
    {
	    getTitleBar().setTitleText("选择商品");
	}
	
    @Click(resName = "btn_select_commodity_oksubmit_click")
    void onClickBtnSelectCommodityOksubmit()
    {

    }

    @Click(resName = "check_select_commodity_all_click")
    void onClickCheckSelectCommodityAll()
    {

    }

    @Click(resName = "check_select_commodity_title_one_click")
    void onClickCheckSelectCommodityTitleOne()
    {

    }

    @Click(resName = "check_select_commodity_title_two_click")
    void onClickCheckSelectCommodityTitleTwo()
    {

    }

    @Click(resName = "check_select_commodity_title_three_click")
    void onClickCheckSelectCommodityTitleThree()
    {

    }

    @Click(resName = "check_select_commodity_title_four_click")
    void onClickCheckSelectCommodityTitleFour()
    {

    }

    @Click(resName = "llayout_select_commodity_all_click")
    void onClickLlayoutSelectCommodityAll()
    {

    }

    @Click(resName = "llayout_select_commodity_title_one_click")
    void onClickLlayoutSelectCommodityTitleOne()
    {

    }

    @Click(resName = "llayout_select_commodity_title_two_click")
    void onClickLlayoutSelectCommodityTitleTwo()
    {

    }

    @Click(resName = "llayout_select_commodity_title_three_click")
    void onClickLlayoutSelectCommodityTitleThree()
    {

    }

    @Click(resName = "llayout_select_commodity_title_four_click")
    void onClickLlayoutSelectCommodityTitleFour()
    {

    }


	
}