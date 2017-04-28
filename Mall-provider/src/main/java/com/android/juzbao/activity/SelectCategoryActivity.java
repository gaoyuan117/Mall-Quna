package com.android.juzbao.activity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import com.android.zcomponent.views.swipebacklayout.lib.app.SwipeBackActivity;
import android.os.Bundle;

import com.android.juzbao.provider.R;
import android.widget.TextView;


/**
 * <p>
 * Description:  选择分类 
 * </p>
 * 
 * @ClassName:SelectCategoryActivity
 * @author: wei
 * @date: 2015-11-11
 * 
 */
@EActivity(resName = "activity_select_category")
public class SelectCategoryActivity extends SwipeBackActivity
{
    @ViewById(resName = "ivew_category_one_show")
    TextView mIvewCategoryOne;

    @ViewById(resName = "ivew_category_two_show")
    TextView mIvewCategoryTwo;

    @ViewById(resName = "ivew_category_three_show")
    TextView mIvewCategoryThree;

    @ViewById(resName = "ivew_category_four_show")
    TextView mIvewCategoryFour;

    @ViewById(resName = "ivew_category_five_show")
    TextView mIvewCategoryFive;

    @ViewById(resName = "ivew_category_six_show")
    TextView mIvewCategorySix;

    @ViewById(resName = "ivew_category_seven_show")
    TextView mIvewCategorySeven;



    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }
	
	@AfterViews
    void initUI()
    {
	    getTitleBar().setTitleText("选择分类");
	}
	
    @Click(resName = "llayout_select_one_click")
    void onClickLlayoutSelectOne()
    {

    }

    @Click(resName = "llayout_select_two_click")
    void onClickLlayoutSelectTwo()
    {

    }

    @Click(resName = "llayout_select_three_click")
    void onClickLlayoutSelectThree()
    {

    }

    @Click(resName = "llayout_select_four_click")
    void onClickLlayoutSelectFour()
    {

    }

    @Click(resName = "llayout_select_five_click")
    void onClickLlayoutSelectFive()
    {

    }

    @Click(resName = "llayout_select_six_click")
    void onClickLlayoutSelectSix()
    {

    }

    @Click(resName = "llayout_select_seven_click")
    void onClickLlayoutSelectSeven()
    {

    }


	
}