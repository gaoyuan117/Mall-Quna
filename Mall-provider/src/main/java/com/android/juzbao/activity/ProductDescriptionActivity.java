package com.android.juzbao.activity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import com.android.zcomponent.views.swipebacklayout.lib.app.SwipeBackActivity;
import android.os.Bundle;

import com.android.juzbao.provider.R;
import android.widget.EditText;


/**
 * <p>
 * Description:  商品描述 
 * </p>
 * 
 * @ClassName:ProductDescriptionActivity
 * @author: wei
 * @date: 2015-11-11
 * 
 */
@EActivity(resName = "activity_product_description")
public class ProductDescriptionActivity extends SwipeBackActivity
{
    @ViewById(resName = "edittxt_product_description_input_show")
    EditText mEdittxtProductDescriptionInput;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }
	
	@AfterViews
    void initUI()
    {
	    getTitleBar().setTitleText("商品描述");
	}
	
    @Click(resName = "tvew_product_description_add_picture_click")
    void onClickTvewProductDescriptionAddPicture()
    {

    }

    @Click(resName = "tvew_product_description_ok_click")
    void onClickTvewProductDescriptionOk()
    {

    }


	
}