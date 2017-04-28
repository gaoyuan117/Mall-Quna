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
 * Description:  选择发货地址 
 * </p>
 * 
 * @ClassName:ShipAddressActivity
 * @author: wei
 * @date: 2015-11-11
 * 
 */
@EActivity(resName = "activity_ship_address")
public class ShipAddressActivity extends SwipeBackActivity
{
    @ViewById(resName = "tvew_ship_address_counytry_show")
    TextView mTvewShipAddressCounytry;

    @ViewById(resName = "tvew_ship_address_province_show")
    TextView mTvewShipAddressProvince;

    @ViewById(resName = "tvew_ship_address_city_show")
    TextView mTvewShipAddressCity;



    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }
	
	@AfterViews
    void initUI()
    {
	    getTitleBar().setTitleText("选择发货地址");
	}
	
    @Click(resName = "tvew_ship_address_add_picture_click")
    void onClickTvewShipAddressAddPicture()
    {

    }

    @Click(resName = "tvew_ship_address_ok_click")
    void onClickTvewShipAddressOk()
    {

    }


	
}