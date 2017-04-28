
package com.android.juzbao.activity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.juzbao.enumerate.CategoryType;
import com.android.juzbao.provider.R;
import com.server.api.model.AddProduct;
import com.server.api.service.ProductService;
import com.server.api.service.ProductService.ProductAddDingzhiRequest;
import com.android.juzbao.model.ProviderProductBusiness;
import com.android.juzbao.enumerate.ProductType;
import com.android.juzbao.util.CommonValidate;
import com.android.zcomponent.http.api.model.MessageData;
import com.android.zcomponent.util.ShowMsg;

/**
 * <p>
 * Description: 发布定制商品
 * </p>
 *
 * @ClassName:ReleaseCustomizationGoodsActivity
 * @author: wei
 * @date: 2015-11-11
 */
@EActivity(resName = "activity_release_customization_goods")
public class ReleaseCustomizationGoodsActivity extends AddProductActivity {

    @ViewById(resName = "edittxt_auction_goods_code_show")
    EditText mEdittxtCustomizationGoodsCode;

    @ViewById(resName = "tvew_auction_goods_description_show")
    TextView mEdittxtCustomizationGoodsDesc;

    @ViewById(resName = "edittxt_customization_goods_price_show")
    EditText mEdittxtCustomizationGoodsPrice;

    @ViewById(resName = "edittxt_customization_goods_deadline_show")
    TextView mEdittxtCustomizationGoodsDeadline;

    @ViewById(resName = "edittxt_customization_goods_overdue_show")
    TextView mEdittxtCustomizationGoodsOverdue;

    @ViewById(resName = "edittxt_customization_goods_period_show")
    EditText mEdittxtCustomizationGoodsPeriod;

    @ViewById(resName = "edittxt_customization_goods_percentage_show")
    EditText mEdittxtCustomizationGoodsPercentage;

    @ViewById(resName = "tvew_customization_day_show")
    TextView mTvewCustomizationDay;

    @ViewById(resName = "tvew_customization_buyer_percentage_show")
    TextView mTvewCustomizationBuyerPercentage;

    @ViewById(resName = "tvew_auction_goods_category_show")
    TextView mTvewCommodityGoodsCategory;

    @ViewById(resName = "progressbar_categoryz_show")
    ProgressBar mProgressBar;

    private ProductAddDingzhiRequest mRequest;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    protected void initUI() {
        super.initUI();

        getTitleBar().setTitleText("发布定制商品");
        mRequest = new ProductAddDingzhiRequest();
        mEdittxtCustomizationGoodsPeriod
                .addTextChangedListener(new TextWatcher() {

                    @Override
                    public void onTextChanged(CharSequence s, int start,
                                              int before, int count) {

                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start,
                                                  int count, int after) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        mTvewCustomizationDay.setText(s.toString());
                    }
                });
        mEdittxtCustomizationGoodsPercentage
                .addTextChangedListener(new TextWatcher() {

                    @Override
                    public void onTextChanged(CharSequence s, int start,
                                              int before, int count) {

                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start,
                                                  int count, int after) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        mTvewCustomizationBuyerPercentage.setText(s.toString());
                    }
                });
    }

    @Override
    public void onRecvMsg(MessageData msg) throws Exception {
        super.onRecvMsg(msg);
        if (msg.valiateReq(ProductService.ProductAddDingzhiRequest.class)) {
            AddProduct response = (AddProduct) msg.getRspObject();
            if (CommonValidate.validateQueryState(this, msg, response, "发布失败")) {
                ShowMsg.showToast(this, "发布成功");
                finish();
            }
        }
    }

    @Click(resName = "llayout_category_click")
    protected void onClickRlayoutProductCategory() {
        super.onClickRlayoutProductCategory();
    }

    @Click(resName = "llayout_description_click")
    protected void onClickEditDescription() {
        super.onClickEditDescription();
    }

    @Click(resName = "llayout_goods_deadline_click")
    protected void onClickDeadline() {
        showTimePopupWindow(mEdittxtCustomizationGoodsDeadline);
    }

    @Click(resName = "tvew_auction_now_release_click")
    void onClickTvewAuctionNowRelease() {
        mRequest.Type = ProductType.DINGZHI.getValue();
        mRequest.Verify = mEdittxtCustomizationGoodsCode.getText().toString();
        mRequest.Title = mEdittxtTitle.getText().toString();

        mRequest.EndTime =
                mEdittxtCustomizationGoodsDeadline.getText().toString();
        mRequest.CategoryId = mCategoryId;
        mRequest.Description = mstrDesc;
        mRequest.Address = mstrAddress;
        mRequest.Pics = getUploadFile();
        mRequest.Movie = getVideoUploadFile();
        mRequest.MovieThumbId = getVideoThumbnailFile();
        mRequest.SecurityDelivery = getSecurityDelivery();
        mRequest.Security7days = getSecurity7days();
        try {
            if (!TextUtils.isEmpty(mEdittxtCustomizationGoodsPrice.getText()
                    .toString())) {
                mRequest.Price =
                        Double.parseDouble(mEdittxtCustomizationGoodsPrice
                                .getText().toString());
            }

            String overDay =
                    mEdittxtCustomizationGoodsPeriod.getText().toString();
            if (!TextUtils.isEmpty(overDay)) {
                mRequest.OverDay = Integer.parseInt(overDay);
            }

            String overPercent =
                    mEdittxtCustomizationGoodsPercentage.getText().toString();
            if (!TextUtils.isEmpty(overDay)) {
                mRequest.OverPersent = Integer.parseInt(overPercent);
            }

            if (null != mAddress) {
                mRequest.ProvinceId = Integer.parseInt(mAddress.province_id);
                mRequest.CityId = Integer.parseInt(mAddress.city_id);
                mRequest.AreaId = Integer.parseInt(mAddress.area_id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        boolean isSend =
                ProviderProductBusiness.addDingzhiProduct(this,
                        getHttpDataLoader(), mRequest);
        if (isSend) {
            showWaitDialog(1, false, R.string.common_submit_data);
        }
    }

    @Override
    public TextView getDescriptionTextView() {
        return mEdittxtCustomizationGoodsDesc;
    }

    @Override
    public TextView getCategoryEditvew() {
        return mTvewCommodityGoodsCategory;
    }

    @Override
    public ProgressBar getCategoryProgressBar() {
        return mProgressBar;
    }

    @Override
    public String getCategoryType() {
        return CategoryType.PRODCUT.getValue();
    }
}