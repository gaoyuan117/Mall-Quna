package com.android.juzbao.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.juzbao.constant.ProviderResultActivity;
import com.android.juzbao.provider.R;
import com.android.zcomponent.common.uiframe.FramewrokApplication;
import com.android.zcomponent.http.api.model.MessageData;
import com.android.zcomponent.http.constant.ErrorCode;
import com.android.zcomponent.json.JsonSerializerFactory;
import com.android.zcomponent.util.ShowMsg;
import com.android.zcomponent.views.swipebacklayout.lib.app.SwipeBackActivity;
import com.server.api.model.Area;
import com.server.api.model.City;
import com.server.api.model.CommonReturn;
import com.server.api.model.Order;
import com.server.api.model.Province;
import com.server.api.service.AddressService;
import com.server.api.service.OrderService;

public class SendGoodsActivity extends SwipeBackActivity {

    private Order mOrder;
    private String mFullAddress = "";

    private TextView tvew_personal_show, tvew_phone_show, tvew_address_show, tvew_order_id_show, tvew_person_show;

    private Button btn_submit_click;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.initView();
        this.initData();
        this.initEvent();
    }

    private void initView() {
        setContentView(R.layout.activity_send_goods);
        getTitleBar().setTitleText("发货");
        tvew_personal_show = (TextView) findViewById(R.id.tvew_personal_show);
        tvew_phone_show = (TextView) findViewById(R.id.tvew_phone_show);
        tvew_address_show = (TextView) findViewById(R.id.tvew_address_show);
        tvew_order_id_show = (TextView) findViewById(R.id.tvew_order_id_show);
        tvew_person_show = (TextView) findViewById(R.id.tvew_person_show);
        btn_submit_click = (Button) findViewById(R.id.btn_submit_click);
    }

    private void initData() {
        Intent intent = getIntent();
        String order = intent.getStringExtra("order");
        String fullAddress = intent.getStringExtra("address");
        mOrder = JsonSerializerFactory.Create().decode(order, Order.class);

        if (!TextUtils.isEmpty(mFullAddress)) {
            mFullAddress = fullAddress;
            this.showTextInfo();
        } else {
            getDataEmptyView().showViewWaiting();
            this.queryProvince();
        }
    }

    private void initEvent() {
        btn_submit_click.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                OrderService.ShippingOrderRequest request = new OrderService.ShippingOrderRequest();
                request.id = mOrder.order_id;
                getHttpDataLoader().doPostProcess(request, CommonReturn.class);
            }
        });
    }

    private void showTextInfo() {
        if (null == mOrder) {
            return;
        }
        tvew_personal_show.setText(mOrder.consignee);
        tvew_phone_show.setText(mOrder.mobile);
        tvew_order_id_show.setText("订单号：" + mOrder.order_no);
        tvew_address_show.setText("收货地址："+mFullAddress + mOrder.address);
        tvew_person_show.setText("收货人：");
        getDataEmptyView().setBackgroundResource(R.drawable.transparent);
    }

    @Override public void onRecvMsg(MessageData msg) throws Exception {

        if (msg.valiateReq(AddressService.GetProvincesRequest.class)) {
            Province response = (Province) msg.getRspObject();
            if (null != response) {
                if (response.code == ErrorCode.INT_QUERY_DATA_SUCCESS) {
                    if (null != mOrder) {
                        for (int i = 0; i < response.Data.length; i++) {
                            if (mOrder.province_id.equals(response.Data[i].province_id)) {
                                mFullAddress += response.Data[i].province;
                                this.queryCity(mOrder.province_id);
                                break;
                            }
                        }
                    }
                }
            }
        } else if (msg.valiateReq(AddressService.GetCitiesRequest.class)) {
            City response = (City) msg.getRspObject();
            if (null != response) {
                if (response.code == ErrorCode.INT_QUERY_DATA_SUCCESS) {
                    if (null != mOrder) {
                        for (int i = 0; i < response.Data.length; i++) {
                            if (mOrder.city_id.equals(response.Data[i].city_id)) {
                                mFullAddress += response.Data[i].city;
                                this.queryAres(mOrder.city_id);
                                break;
                            }
                        }
                    }
                }
            }
        } else if (msg.valiateReq(AddressService.GetAreasRequest.class)) {
            Area response = (Area) msg.getRspObject();
            if (null != response) {
                if (response.code == ErrorCode.INT_QUERY_DATA_SUCCESS) {
                    if (null != mOrder) {
                        for (int i = 0; i < response.Data.length; i++) {
                            if (mOrder.area_id.equals(response.Data[i].area_id)) {
                                mFullAddress += response.Data[i].area;
                                getDataEmptyView().dismiss();
                                this.showTextInfo();
                                break;
                            }
                        }
                    }
                }
            }
        } else if (msg.valiateReq(OrderService.ShippingOrderRequest.class)) {
            //订单发货
            CommonReturn response = (CommonReturn) msg.getRspObject();
            if (null != response) {
                if (response.code == ErrorCode.INT_QUERY_DATA_SUCCESS) {
                    ShowMsg.showToast(SendGoodsActivity.this, "发货成功");
                    FramewrokApplication.getInstance().setActivityResult(ProviderResultActivity.CODE_SEND_GOODS_SUCCESS,null);
                    this.finish();
                }
            }
        }
    }

    /**
     * 获取省份列表
     */
    private void queryProvince() {
        AddressService.GetProvincesRequest request = new AddressService.GetProvincesRequest();
        getHttpDataLoader().doPostProcess(request, Province.class);
    }

    /**
     * 获取城市列表
     */
    private void queryCity(String province_id) {
        AddressService.GetCitiesRequest request = new AddressService.GetCitiesRequest();
        request.ProvinceId = province_id;
        getHttpDataLoader().doPostProcess(request, City.class);
    }

    /**
     * 获取城市列表
     */
    private void queryAres(String city_id) {
        AddressService.GetAreasRequest request = new AddressService.GetAreasRequest();
        request.CityId = city_id;
        getHttpDataLoader().doPostProcess(request, Area.class);
    }
}
