
package com.android.juzbao.activity.order;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.juzbao.application.BaseApplication;
import com.android.juzbao.constant.ResultActivity;
import com.android.juzbao.model.OrderBusiness;
import com.android.juzbao.model.OrderBusiness.OrderHelper;
import com.android.juzbao.model.ProviderFileBusiness;
import com.android.mall.resource.R;
import com.android.zcomponent.http.api.model.MessageData;
import com.android.zcomponent.http.constant.ErrorCode;
import com.android.zcomponent.json.JsonSerializerFactory;
import com.android.zcomponent.util.ShowMsg;
import com.android.zcomponent.util.StringUtil;
import com.android.zcomponent.views.swipebacklayout.lib.app.SwipeBackActivity;
import com.server.api.model.CommonReturn;
import com.server.api.model.Order;
import com.server.api.service.OrderService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.math.BigDecimal;

/**
 * <p>
 * Description: 我的退款申请
 * </p>
 */
@EActivity(R.layout.activity_my_order_refund)
public class MyOrderRefundActivity extends SwipeBackActivity {

    @ViewById(R.id.editvew_refund_reason_show)
    EditText mEditvewRefundReason;

    @ViewById(R.id.editvew_refund_total_show)
    EditText mEditvewRefundTotal;

    @ViewById(R.id.editvew_refund_desc_show)
    EditText mEditvewRefundDesc;

    @ViewById(R.id.imgvew_refund_money_show)
    ImageView mImgvewRefundMoney;

    @ViewById(R.id.imgvew_refund_product_show)
    ImageView mImgvewRefundProduct;

    @ViewById(R.id.imageview1)
    ImageView mImgvew1;

    @ViewById(R.id.imageview2)
    ImageView mImgvew2;

    @ViewById(R.id.imageview3)
    ImageView mImgvew3;

    @ViewById(R.id.tvew_refund_money_show)
    TextView mTvewRefundMoney;

    @ViewById(R.id.llayout_order_item)
    LinearLayout mLlayoutOrderItem;

    @ViewById(R.id.llayout_order_item_parent_show)
    LinearLayout mLlayoutOrderItemParent;

    private boolean isOnlyRefundMoney = true;

    private Order mOrder;

    private ProviderFileBusiness mFileBusiness;

    private OrderHelper mOrderHelper = new OrderHelper();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    void initUI() {
        getTitleBar().setTitleText("退款申请 ");

        mFileBusiness = new ProviderFileBusiness(this, getHttpDataLoader());
        mFileBusiness.setOutParams(4, 3, 1024, 768);

        Intent intent = getIntent();
        String order = intent.getStringExtra("order");
        mOrder = JsonSerializerFactory.Create().decode(order, Order.class);

        showOrderInfo(mOrder);
    }

    private void showOrderInfo(Order order) {
        mTvewRefundMoney.setText(" 最多退款¥"
                + StringUtil.formatProgress(order.total_price));
        mOrderHelper.showOrderItem(this, mLlayoutOrderItem, order);
    }

    private void selectRefundType(boolean isOnlyRefundMoney) {
        this.isOnlyRefundMoney = isOnlyRefundMoney;
        if (isOnlyRefundMoney) {
            mImgvewRefundMoney.setVisibility(View.VISIBLE);
            mImgvewRefundProduct.setVisibility(View.GONE);
        } else {
            mImgvewRefundMoney.setVisibility(View.GONE);
            mImgvewRefundProduct.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onRecvMsg(MessageData msg) throws Exception {
        mFileBusiness.onRecvMsg(msg);

        if (msg.valiateReq(OrderService.ReturnOrderRequest.class)) {
            CommonReturn response = msg.getRspObject();
            if (null != response) {
                if (response.code == ErrorCode.INT_QUERY_DATA_SUCCESS) {
                    ShowMsg.showToast(this, msg, "申请退款成功");
                    BaseApplication.getInstance().setActivityResult(
                            ResultActivity.CODE_REFUND_SUCCESS, null);
                    finish();
                } else {
                    ShowMsg.showToast(this, response.message);
                }
            } else {
                ShowMsg.showToast(this, msg, "申请退款失败");
            }
        }
    }

    @Click(R.id.rlayout_refund_money_click)
    void onClickRlayoutRefundMoney() {
        selectRefundType(true);
    }

    @Click(R.id.rlayout_refund_product_click)
    void onClickRlayoutRefundProduct() {
        selectRefundType(false);
    }

    @Click(R.id.rlayout_take_photo_click)
    void onClickRlayoutTakePhoto() {
        if (mFileBusiness.getImageFiles().size() > 2) {
            ShowMsg.showToast(getApplicationContext(), "最多3张凭证");
            return;
        }
        mFileBusiness.selectPicture();
    }

    @Click(R.id.tvew_submit_click)
    void onClickTvewSubmit() {
        BigDecimal money = new BigDecimal(0);
        if (!TextUtils.isEmpty(mEditvewRefundTotal.getText().toString())) {
            money = new BigDecimal(mEditvewRefundTotal.getText().toString());
        }

        String productId = "";
        if (-1 != mOrderHelper.getSelectPosition()) {
            productId =
                    mOrder.products[mOrderHelper.getSelectPosition()].product_id;
        }

        String type = "";
        if (isOnlyRefundMoney) {
            type = "1";
        } else {
            type = "2";
        }

        String explain = mEditvewRefundDesc.getText().toString();
        String reason = mEditvewRefundReason.getText().toString();
        String[] coverIds = mFileBusiness.getImageFileIds();
        boolean isSend =
                OrderBusiness.queryRefundOrder(getApplicationContext(),
                        getHttpDataLoader(), type, mOrder.order_id, productId,
                        money, reason, explain, coverIds);
        if (isSend) {
            showWaitDialog(1, false, R.string.common_submit_data);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bitmap bitmap =
                mFileBusiness.onActivityResult(requestCode, resultCode, data);
        if (mFileBusiness.getImageFiles().size() == 0) {
            mImgvew1.setImageBitmap(bitmap);
        } else if (mFileBusiness.getImageFiles().size() == 1) {
            mImgvew2.setImageBitmap(bitmap);
        } else if (mFileBusiness.getImageFiles().size() == 2) {
            mImgvew3.setImageBitmap(bitmap);
        }
    }
}