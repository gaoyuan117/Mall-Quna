package com.android.juzbao.activity.jifen;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.juzbao.activity.me.MyAddressActivity_;
import com.android.juzbao.constant.ResultActivity;
import com.android.juzbao.dao.AddressDao;
import com.android.juzbao.dao.jifendao.JiFenDao;
import com.android.quna.activity.R;
import com.android.zcomponent.http.api.host.Endpoint;
import com.android.zcomponent.http.api.model.MessageData;
import com.android.zcomponent.http.constant.ErrorCode;
import com.android.zcomponent.json.JsonSerializerFactory;
import com.android.zcomponent.views.swipebacklayout.lib.app.SwipeBackActivity;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.server.api.model.Address;
import com.server.api.model.ProductItem;
import com.server.api.model.jifenmodel.PlatformOrderReturn;
import com.server.api.service.AddressService;
import com.server.api.service.JiFenService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@EActivity(R.layout.activity_jifen_convert)
public class JifenConvertActivity extends SwipeBackActivity {

    @ViewById(R.id.tvew_convert_jifen_count)
    TextView mConvertJiFenTv;
    @ViewById(R.id.imgvew_jifen_img)
    ImageView mJiFenProductImg;
    @ViewById(R.id.tvew_jifen_title)
    TextView mJiFenProductTitleTv;
    @ViewById(R.id.tvew_jifen_product_count)
    TextView mJiFenProductCountTv;
    @ViewById(R.id.tvew_name_show)
    TextView mReceiverName;
    @ViewById(R.id.tvew_phone_number_show)
    TextView mReceiverNumber;
    @ViewById(R.id.tvew_adress_show)
    TextView mReceiverAddress;
    @ViewById(R.id.tvew_deliver_way)
    TextView mDeliverWay;

    private ProductItem mProductItem;

    private int mProductId;

    private List<Address.Data> mlistAddress;

    private Address.Data mAddress = null;

    @AfterViews void initUI() {
        getTitleBar().setTitleText("立即兑换");
        AddressDao.sendCmdQueryMyAddress(getHttpDataLoader());
        Intent intent = getIntent();
        mProductId = intent.getIntExtra("id", 0);
        mProductItem = JsonSerializerFactory.Create().decode(intent.getStringExtra("product"),
                ProductItem.class);
        if (mProductItem != null) {
            showProductInfo(mProductItem);
        } else {

        }
    }


    @Override
    public void onRecvMsg(MessageData msg) throws Exception {
        if (msg.valiateReq(JiFenService.JifenPayPlatformProductRequest.class)) {
            PlatformOrderReturn response = msg.getRspObject();
            dismissWaitDialog();
            if (response != null) {
                if (response.code == ErrorCode.INT_QUERY_DATA_SUCCESS) {
                    Bundle bundle = new Bundle();
                    getIntentHandle().intentToActivity(bundle, ConvertStateActivity_.class);
                    finish();
                } else {
                    showToast(response.message);
                }

            } else {
                showToast("兑换失败");
            }
        } else if (msg.valiateReq(AddressService.GetReceiverAddressRequest.class)) {
            Address response = msg.getRspObject();
            if (null != response && response.code == ErrorCode.INT_QUERY_DATA_SUCCESS) {
                if (null != response.Data && response.Data.length > 0) {
                    mlistAddress = new ArrayList<Address.Data>(Arrays.asList(response.Data));
                    for (int i = 0; i < mlistAddress.size(); i++) {
                        if (!TextUtils.isEmpty(mlistAddress.get(i).is_default) && Boolean
                                .parseBoolean(mlistAddress.get(i).is_default)) {
                            mAddress = mlistAddress.get(i);
                            break;
                        }
                    }
                    if (null == mAddress) {
                        mAddress = mlistAddress.get(0);
                    }
                    showAddressInfo(mAddress);
                }
            }
        }
    }


    private void showProductInfo(ProductItem mProductItem) {
        mConvertJiFenTv.setText(mProductItem.use_score + "积分");
        ImageLoader.getInstance().displayImage(Endpoint.HOST + mProductItem.image,
                mJiFenProductImg);
        mJiFenProductTitleTv.setText(mProductItem.title);
        mJiFenProductCountTv.setText("剩余：" + mProductItem.quantity + "件");
    }

    private void showAddressInfo(Address.Data address) {
        if (null == address) {
            return;
        }
        mAddress = address;
        mReceiverName.setText(address.realname);
        mReceiverNumber.setText(address.mobile);
        mReceiverAddress.setText(address.province + address.city + address.area + address.address);
    }

    @Click(R.id.btn_submit_click) void onClickBtnConvertSure() {
        //兑换接口
//        JiFenDao.sendJiFenSubmitPlatformRequest(getHttpDataLoader(), mProductId + "", mAddress
// .id);

        if (mAddress == null || mAddress.id.isEmpty()){
            showToast("请选择收获地址");
            return;
        }

        if (mProductItem != null) {
            JiFenDao.snedJifenPayPlatformRequest(getHttpDataLoader(),mProductItem.id,mAddress.id);
            showWaitDialog(2, false, "正在提交", true);
        }
    }

    @Click(R.id.llayout_jifen_address_click) void onClickAdress() {
        Bundle bundle = new Bundle();
        bundle.putBoolean("isSelectAddress", true);
        bundle.putString("address", JsonSerializerFactory.Create().encode(mlistAddress));
        getIntentHandle().intentToActivity(bundle, MyAddressActivity_.class);
    }

    @Override
    public void onActivityResultCallBack(int resultCode, Intent intent) {
        if (resultCode == ResultActivity.CODE_ADDRESS_SELECT) {
            com.server.api.model.Address.Data address = JsonSerializerFactory.Create().decode(
                    intent.getStringExtra("address"),
                    com.server.api.model.Address.Data.class);
            showAddressInfo(address);
        }
    }
}
