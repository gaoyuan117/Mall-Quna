
package com.android.juzbao.activity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.mall.resource.R;
import com.android.juzbao.activity.account.LoginActivity_;
import com.android.zcomponent.http.api.host.Endpoint;
import com.server.api.model.BondNotice;
import com.server.api.model.CommonReturn;
import com.server.api.model.Image;
import com.server.api.model.Product;
import com.server.api.model.ProductItem;
import com.android.juzbao.application.BaseApplication;
import com.android.juzbao.model.ProductBusiness;
import com.android.juzbao.util.CommonValidate;
import com.android.zcomponent.activity.WebViewActivity;
import com.android.zcomponent.http.api.model.MessageData;
import com.android.zcomponent.http.constant.ErrorCode;
import com.android.zcomponent.util.CommonUtil;
import com.android.zcomponent.util.ShowMsg;
import com.android.zcomponent.util.ShowMsg.IConfirmDialogBool;
import com.android.zcomponent.util.StringUtil;
import com.android.zcomponent.util.TimeUtil;
import com.android.zcomponent.views.imageslider.SliderLayout;
import com.android.zcomponent.views.imageslider.SliderLayout.OnViewCreatedListener;
import com.android.zcomponent.views.swipebacklayout.lib.app.SwipeBackActivity;
import com.server.api.service.ProductService;

/**
 * <p>
 * Description: 拍品详情
 * </p>
 *
 * @ClassName:AuctionDetailActivity
 * @author: wei
 * @date: 2015-11-10
 */
@EActivity(R.layout.activity_auction_detail)
public class AuctionDetailActivity extends SwipeBackActivity {

    @ViewById(R.id.tvew_acution_status_show)
    TextView mTvewAcutionStatus;

    @ViewById(R.id.tvew_acution_endtime_show)
    TextView mTvewAcutionEndtime;

    @ViewById(R.id.tvew_product_name_show)
    TextView mTvewProductName;

    @ViewById(R.id.tvew_product_price_show)
    TextView mTvewProductPrice;

    @ViewById(R.id.tvew_start_price_show)
    TextView mTvewProductStartPrice;

    @ViewById(R.id.tvew_product_now_price_show)
    TextView mTvewProductNowPrice;

    @ViewById(R.id.tvew_bond_show)
    TextView mTvewBond;

    @ViewById(R.id.tvew_auction_count_show)
    TextView mTvewAuctionCount;

    @ViewById(R.id.tvew_product_qi_show)
    TextView mTvewProductQi;

    @ViewById(R.id.btn_submit_click)
    Button mBtnSubmit;

    @ViewById(R.id.editvew_acution_num_show)
    TextView mEditvewAcutionNum;

    @ViewById(R.id.tvew_acution_service_show)
    TextView mTvewAcutionService;

    WebView mWebviewProductDetail;

    /**
     * 海报图片显示
     */
    private SliderLayout mSliderLayout;

    private ProductItem mProduct;

    private int mProductId;

    private BondNotice mBondNotice;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    void initUI() {
        getTitleBar().setTitleText("拍品详情 ");

        mWebviewProductDetail =
                (WebView) findViewById(R.id.webview_product_detail_show);
        mWebviewProductDetail.getSettings().setDefaultTextEncodingName("utf-8");

        mProductId = getIntent().getIntExtra("id", 0);
        getDataEmptyView().showViewWaiting();
        ProductBusiness.queryProduct(getHttpDataLoader(), mProductId);

    }

    @Override
    public void onDataEmptyClickRefresh() {
        getDataEmptyView().showViewWaiting();
        ProductBusiness.queryProduct(getHttpDataLoader(), mProductId);
    }

    @Override
    public void onLoginSuccess() {
        ProductBusiness.queryProduct(getHttpDataLoader(), mProductId);
    }

    @Override
    public void onRecvMsg(MessageData msg) throws Exception {
        if (msg.valiateReq(ProductService.ProductRequest.class)) {
            Product response = msg.getRspObject();
            if (null != response) {
                if (response.code == ErrorCode.INT_QUERY_DATA_SUCCESS) {
                    showProductInfo(response.Data);

                    getDataEmptyView().dismiss();
                } else {
                    getDataEmptyView().showViewDataEmpty(true, false, msg,
                            response.message);
                }
            } else {
                getDataEmptyView().showViewDataEmpty(true, false, msg,
                        R.string.common_data_empty);
            }
        } else if (msg.valiateReq(ProductService.ProductSignupRequest.class)) {
            CommonReturn response = (CommonReturn) msg.getRspObject();

            if (CommonValidate
                    .validateQueryState(this, msg, response, "报名提交失败")) {
                showInputMoneyDialog();
            }
        } else if (msg.valiateReq(ProductService.ProductSignupMoneyRequest.class)) {
            CommonReturn response = (CommonReturn) msg.getRspObject();

            if (CommonValidate.validateQueryState(this, msg, response,
                    "提交竞拍价失败")) {
                ShowMsg.showToast(getApplicationContext(), "提交竞拍价成功");
            }
        }
    }

    private void showInputMoneyDialog() {
        View view =
                LayoutInflater.from(this).inflate(
                        R.layout.layout_auction_money, null);
        final EditText editvewMoney =
                (EditText) view.findViewById(R.id.editvew_money_show);

        editvewMoney.addTextChangedListener(new StringUtil.DecimalTextWatcher(
                editvewMoney, 2));

        ShowMsg.showConfirmDialog(this, new IConfirmDialogBool() {

            @Override
            public boolean onConfirm(boolean confirmValue) {
                if (confirmValue) {
                    String strMoney = editvewMoney.getText().toString();
                    if (TextUtils.isEmpty(strMoney)) {
                        return false;
                    }

                    ProductBusiness.queryPaipinSignupMoney(getHttpDataLoader(),
                            Long.parseLong(mProduct.id), new BigDecimal(
                                    strMoney));
                    showWaitDialog(1, false, R.string.common_submit_data);
                    return true;
                }

                return false;
            }
        }, "确定", "取消", view);
    }

    private void showProductInfo(ProductItem item) {
        if (null == item) {
            return;
        }
        mProduct = item;
        bindSliderLayout();

        mWebviewProductDetail.loadData(
                ProductBusiness.getHtmlData(mProduct.description),
                "text/html; charset=utf-8", "utf-8");
        mTvewAcutionEndtime.setText(TimeUtil.transformLongTimeFormat(
                Long.parseLong(mProduct.end_time) * 1000, "MM月dd号HH点") + "结束");

        long currentTime = Endpoint.serverDate().getTime() / 1000;
        long diffTime = Long.parseLong(mProduct.end_time) - currentTime;

        if (diffTime <= 0) {
            mTvewAcutionStatus.setText("已结束");
            mBtnSubmit.setVisibility(View.GONE);
        } else {
            mBtnSubmit.setVisibility(View.VISIBLE);
        }
        mTvewProductNowPrice.setText("¥" + mProduct.maxprice);
        mTvewProductStartPrice.setText("起拍价 ¥" + mProduct.price_start);

        mTvewBond.setText("保证金 ¥" + mProduct.cash_deposit);
        mTvewAuctionCount.setText("出价记录(" + mProduct.auction_count + ")");

        mTvewProductPrice.setText("¥" + mProduct.price);
        mTvewProductName.setText(mProduct.title);

        ProductBusiness.showProductAttribute(this, mTvewProductQi, mProduct.security_7days,
                R.drawable.product_detail_icon_qi_un,
                R.drawable.product_detail_icon_qi);

        if ("1".equals(mProduct.is_auaction_permission)) {
            mBtnSubmit.setText("竞拍出价");
        } else {
            mBtnSubmit.setText("报名提交保证金");
        }
    }

    private void bindSliderLayout() {
        mSliderLayout =
                SliderLayout.bindSliderLayout(this, R.id.flayout_slider_image);
        mSliderLayout.setOnViewCreatedListener(new OnViewCreatedListener() {

            @Override
            public void onViewCreated() {
                if (null != mProduct && null != mProduct.images) {
                    List<String> temp = new ArrayList<String>();
                    for (Image image : mProduct.images) {
                        temp.add(Endpoint.HOST + image.path);
                    }
                    mSliderLayout.setData(temp, true, true);
                }
            }
        });
    }

    @Click(R.id.btn_submit_click)
    void onClickBtnSubmit() {
        if (!CommonUtil.isLeastSingleClick()) {
            return;
        }

        if (!BaseApplication.isLogin()) {
            getIntentHandle().intentToActivity(LoginActivity_.class);
            return;
        }

        if ("1".equals(mProduct.is_auaction_permission)) {
            showInputMoneyDialog();
        } else {
            ProductBusiness.queryPaipinSignup(getHttpDataLoader(),
                    Long.parseLong(mProduct.id));
            showWaitDialog(1, false, R.string.common_submit_data);
        }
    }

    @Click(R.id.rlayout_product_params_click)
    void onClickRlayoutProductParams() {
        if (TextUtils.isEmpty(mProduct.description)) {
            ShowMsg.showToast(this, "暂无拍品参数");
        } else {
            WebViewActivity.openHtmlData(this,
                    ProductBusiness.getHtmlData(mProduct.description), "拍品参数");
        }
    }

    @Click(R.id.rlayout_product_acution_num_click)
    void onClickRlayoutProductAcutionNum() {
        Bundle bundle = new Bundle();
        bundle.putString("id", mProduct.id);
        getIntentHandle().intentToActivity(bundle, AuctionLogActivity_.class);
    }

    @Click(R.id.rlayout_product_desc_click)
    void onClickRlayoutProductDesc() {

    }

    @Click(R.id.rlayout_acution_service_click)
    void onClickRlayoutAcutionService() {
        if (mTvewProductQi.getVisibility() == View.VISIBLE) {
            mTvewProductQi.setVisibility(View.GONE);
        } else {
            mTvewProductQi.setVisibility(View.VISIBLE);
        }
    }

    @Click(R.id.rlayout_auction_notice_click)
    void onClickRlayoutAuctionNotice() {
        getIntentHandle().intentToActivity(BondNoticeActivity_.class);
    }

}