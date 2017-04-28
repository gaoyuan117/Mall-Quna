
package com.android.juzbao.activity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.mall.resource.R;
import com.android.juzbao.activity.order.OrderPayActivity_;
import com.android.zcomponent.http.api.host.Endpoint;
import com.server.api.model.AddressJZB;
import com.server.api.model.CommonReturn;
import com.server.api.model.DistinguishDetail;
import com.server.api.model.DistinguishItem;
import com.android.juzbao.application.BaseApplication;
import com.android.juzbao.model.AddressBusiness;
import com.android.juzbao.model.DistinguishBusiness;
import com.android.juzbao.constant.ProviderResultActivity;
import com.android.juzbao.constant.ResultActivity;
import com.android.juzbao.enumerate.DistinguishStatus;
import com.android.juzbao.util.CommonValidate;
import com.android.zcomponent.http.api.model.MessageData;
import com.android.zcomponent.http.constant.ErrorCode;
import com.android.zcomponent.json.JsonSerializerFactory;
import com.android.zcomponent.util.ImageLoaderUtil;
import com.android.zcomponent.util.ShowMsg;
import com.android.zcomponent.util.StringUtil;
import com.android.zcomponent.util.TimeUtil;
import com.android.zcomponent.views.swipebacklayout.lib.app.SwipeBackActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.server.api.service.AddressService;
import com.server.api.service.DistinguishService;

/**
 * <p>
 * Description: 鉴真宝订单发出
 * </p>
 *
 * @ClassName:DistinguishEnsureActivity
 * @author: wei
 * @date: 2015-11-10
 */
@EActivity(R.layout.activity_distinguish_ensure)
public class DistinguishEnsureActivity extends SwipeBackActivity {

    @ViewById(R.id.imgvew_product_show)
    ImageView mImgvewProduct;

    @ViewById(R.id.tvew_order_fee_show)
    TextView mTvewOrderFee;

    @ViewById(R.id.tvew_order_delivery_address_show)
    TextView mTvewOrderDeliveryAddress;

    @ViewById(R.id.tvew_order_id_show)
    TextView mTvewOrderId;

    @ViewById(R.id.tvew_order_time_show)
    TextView mTvewOrderTime;

    @ViewById(R.id.tvew_my_address_show)
    TextView mTvewMyAddress;

    @ViewById(R.id.tvew_address_name_show)
    TextView mTvewAddressName;

    @ViewById(R.id.tvew_address_phone_show)
    TextView mTvewAddressPhone;

    @ViewById(R.id.editvew_product_name_show)
    TextView mTvewProductName;

    @ViewById(R.id.editvew_product_num_show)
    TextView mTvewProductNum;

    @ViewById(R.id.editvew_product_desc_show)
    TextView mTvewProductDesc;

    @ViewById(R.id.btn_submit_click)
    Button mTvewSubmit;

    private DistinguishItem mDistinguish;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    void initUI() {
        getTitleBar().setTitleText("鉴定发出  ");

        Intent intent = getIntent();
        String strItem = intent.getStringExtra("item");
        String strId = intent.getStringExtra("id");

        if (!TextUtils.isEmpty(strItem)) {
            mDistinguish =
                    JsonSerializerFactory.Create().decode(strItem,
                            DistinguishItem.class);
            showDistinguishInfo(mDistinguish);
        } else {
            getDataEmptyView().showViewWaiting();
        }
        DistinguishBusiness.queryDistinguishDetail(getHttpDataLoader(), strId);
        AddressBusiness.queryJZBAddress(getHttpDataLoader());
    }

    @Override
    public void onRecvMsg(MessageData msg) throws Exception {
        if (msg.valiateReq(DistinguishService.DelDistinguishRequest.class)) {
            CommonReturn response = msg.getRspObject();
            if (null != response) {
                if (response.code == ErrorCode.INT_QUERY_DATA_SUCCESS) {
                    ShowMsg.showToast(this, msg, "订单已删除");
                    BaseApplication.getInstance().setActivityResult(
                            ResultActivity.CODE_DEL_SUCCESS, null);
                    finish();
                } else {
                    ShowMsg.showToast(this, response.message);
                }
            } else {
                ShowMsg.showToast(this, msg, "删除订单失败");
            }
        } else if (msg.valiateReq(AddressService.JZBAddressRequest.class)) {
            AddressJZB response = msg.getRspObject();
            if (CommonValidate.validateQueryState(this, msg, response)) {
                String address = "鉴真宝收货地址：" + response.Data;
                mTvewOrderDeliveryAddress.setText(StringUtil
                        .spanForegroundString(address, 8, address.length(),
                                getResources().getColor(R.color.gray)));
            }
        } else if (msg.valiateReq(DistinguishService.QueryDistinguishDetailRequest.class)) {
            DistinguishDetail response = msg.getRspObject();
            if (CommonValidate.validateQueryState(this, msg, response)) {
                if (null != mDistinguish && null != mDistinguish.image) {
                    response.Data.image = mDistinguish.image;
                    mDistinguish = response.Data;
                } else {
                    mDistinguish = response.Data;
                    if (null != response.Data.images
                            && response.Data.images.length > 0) {
                        String[] images =
                                new String[response.Data.images.length];

                        for (int i = 0; i < response.Data.images.length; i++) {
                            images[i] = response.Data.images[i].path;
                        }
                        mDistinguish.image = images;
                    }
                }
                showDistinguishInfo(mDistinguish);
                getDataEmptyView().dismiss();
            } else {
                getDataEmptyView().showViewDataEmpty(false, false, msg,
                        "查询数据失败");
            }
        }
    }

    private void showDistinguishInfo(DistinguishItem distinguishItem) {
        mTvewOrderFee.setText("¥"
                + StringUtil.formatProgress(distinguishItem.total));

        mTvewOrderId.setText(distinguishItem.checkup_no);
        mTvewOrderTime.setText(TimeUtil.transformLongTimeFormat(
                Long.parseLong(distinguishItem.create_time) * 1000,
                TimeUtil.STR_FORMAT_DATE_TIME));

        mTvewAddressName.setText(distinguishItem.realname);
        mTvewAddressPhone.setText(distinguishItem.mobile);
        mTvewProductName.setText(distinguishItem.title);
        mTvewProductNum.setText("数量：" + distinguishItem.quantity);
        mTvewProductDesc.setText("商品描述：" + distinguishItem.description);

        if (TextUtils.isEmpty(distinguishItem.address_full)) {
            mTvewMyAddress.setText("我的收货地址：");
        } else {
            String strAddress = "我的收货地址：" + distinguishItem.address_full;
            mTvewMyAddress.setText(StringUtil.spanForegroundString(strAddress, 7,
                    strAddress.length(), getResources().getColor(R.color.gray)));
        }

        DisplayImageOptions options =
                ImageLoaderUtil
                        .getDisplayImageOptions(R.drawable.img_empty_logo_small);
        ImageLoader.getInstance().displayImage(
                Endpoint.HOST + distinguishItem.image[0], mImgvewProduct,
                options);

        if (DistinguishStatus.DELIVERY.getValue()
                .equals(distinguishItem.status)) {
            mTvewSubmit.setText("上传物流凭证");
        } else if (DistinguishStatus.SENDED.getValue().equals(
                distinguishItem.status)
                || DistinguishStatus.DISTINGUISH.getValue().equals(
                distinguishItem.status)
                || DistinguishStatus.RECEIPT.getValue().equals(
                distinguishItem.status)) {
            mTvewSubmit.setText("查看物流信息");
        } else if (DistinguishStatus.COMPLETE.getValue().equals(
                distinguishItem.status)) {
            mTvewSubmit.setText("删除订单");
        }
    }

    @Click(R.id.btn_submit_click)
    void onClickBtnSubmit() {
        if (DistinguishStatus.DELIVERY.getValue().equals(mDistinguish.status)) {
            Bundle bundle = new Bundle();
            bundle.putString("id", mDistinguish.id);
            getIntentHandle().intentToActivity(bundle,
                    SubmitExpressActivity_.class);
        } else if (DistinguishStatus.SENDED.getValue()
                .equals(mDistinguish.status)
                || DistinguishStatus.DISTINGUISH.getValue().equals(
                mDistinguish.status)
                || DistinguishStatus.RECEIPT.getValue().equals(
                mDistinguish.status)) {
            Bundle bundle = new Bundle();
            bundle.putString("id", mDistinguish.id);
            bundle.putString("image", mDistinguish.image[0]);
            getIntentHandle().intentToActivity(bundle,
                    ExpressDetailActivity_.class);
        } else if (DistinguishStatus.COMPLETE.getValue().equals(
                mDistinguish.status)) {
            DistinguishBusiness.queryDelDistinguish(getHttpDataLoader(),
                    mDistinguish.id);
            showWaitDialog(1, false, R.string.common_submit_data);
        } else {
            Bundle bundle = new Bundle();
            bundle.putString("order",
                    JsonSerializerFactory.Create().encode(mDistinguish));
            getIntentHandle().intentToActivity(bundle, OrderPayActivity_.class);
        }
    }

    @Click(R.id.rlayout_product_desc_click)
    void onClickRlayoutProductDesc() {

    }

    @Override
    public void onActivityResultCallBack(int resultCode, Intent intent) {
        if (ResultActivity.CODE_PAY_ECO_SUCCESS == resultCode) {
            mDistinguish.status = DistinguishStatus.DELIVERY.getValue();
            mTvewSubmit.setText("支付成功");
        } else if (resultCode == ProviderResultActivity.CODE_DISTINGUISH_STATUS) {
            DistinguishBusiness.queryDistinguishDetail(getHttpDataLoader(),
                    mDistinguish.id);
        }
    }

}