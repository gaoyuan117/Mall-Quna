package com.android.juzbao.activity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.zcomponent.views.swipebacklayout.lib.app.SwipeBackActivity;


/**
 * <p>
 * Description:  发货到趣那
 * </p>
 * 
 * @ClassName:ShipmentsTojuzhenbaoActivity
 * @author: wei
 * @date: 2015-11-11
 * 
 */
@EActivity(resName = "activity_shipments_tojuzhenbao")
public class ShipmentsTojuzhenbaoActivity extends SwipeBackActivity
{
    @ViewById(resName = "edittxt_shipments_logistics_company_show")
    EditText mEdittxtShipmentsLogisticsCompany;

    @ViewById(resName = "edittxt_shipments_logistics_id_show")
    EditText mEdittxtShipmentsLogisticsId;

    @ViewById(resName = "ivew_shipments_uploding_proof_show")
    ImageView mIvewShipmentsUplodingProof;

    @ViewById(resName = "tvew_shipments_serve_price_show")
    TextView mTvewShipmentsServePrice;

    @ViewById(resName = "tvew_shipments_name_show")
    TextView mTvewShipmentsName;

    @ViewById(resName = "tvew_shipments_phone_show")
    TextView mTvewShipmentsPhone;

    @ViewById(resName = "tvew_shipments_address_show")
    TextView mTvewShipmentsAddress;

    @ViewById(resName = "tvew_shipments_orderid_show")
    TextView mTvewShipmentsOrderid;



    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }
	
	@AfterViews
    void initUI()
    {
	    getTitleBar().setTitleText("发货到趣那");
	}
	
    @Click(resName = "btn_shipments_click")
    void onClickBtnShipments()
    {

    }
}