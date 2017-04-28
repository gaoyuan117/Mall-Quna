
package com.android.juzbao.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.juzbao.activity.account.LoginActivity_;
import com.android.juzbao.application.BaseApplication;
import com.android.juzbao.constant.ProviderResultActivity;
import com.android.juzbao.enumerate.CategoryType;
import com.android.juzbao.enumerate.ProductType;
import com.android.juzbao.model.ProductBusiness;
import com.android.juzbao.util.CommonValidate;
import com.android.mall.resource.R;
import com.android.zcomponent.common.uiframe.FramewrokApplication;
import com.android.zcomponent.http.api.model.MessageData;
import com.android.zcomponent.util.ShowMsg;
import com.android.zcomponent.util.StringUtil;
import com.server.api.model.AddProduct;
import com.server.api.model.CommonReturn;
import com.server.api.model.ProductItem;
import com.server.api.service.ProductService.ProductAddFreeRequest;
import com.server.api.service.ProductService.ProductEditFreeRequest;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * <p>
 * Description: 发布闲置
 * </p>
 *
 * @ClassName:ReleaseFreeActivity
 * @author: wei
 * @date: 2015-11-10
 */
@EActivity(R.layout.activity_add_free)
public class AddFreeActivity extends AddProductActivity {

    @ViewById(R.id.edittxt_free_startprice_show)
    EditText mEdittxtFreeStartprice;

    @ViewById(R.id.edittxt_free_stock_show)
    EditText mEdittxtFreeStock;

    @ViewById(R.id.edittxt_free_indentify_show)
    EditText mEdittxtFreeIndentify;

    @ViewById(R.id.tvew_free_category_show)
    TextView mTvewFreeCategory;

    @ViewById(R.id.tvew_free_description_show)
    TextView mTvewFreeDescription;

    @ViewById(R.id.progressbar_categoryz_show)
    ProgressBar mProgressBar;

    @ViewById(R.id.btn_submit_click)
    Button mBtnSubmit;

    private ProductAddFreeRequest mRequest;

    private ProductEditFreeRequest mFreeRequest;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    public void initUI() {
        super.initUI();
        if (mProductId > 0) {
            mBtnSubmit.setText("保存");
            getTitleBar().setTitleText("编辑需求");
        } else {
            getTitleBar().setTitleText("发布需求");
        }
        mRequest = new ProductAddFreeRequest();
        mEdittxtFreeStartprice
                .addTextChangedListener(new StringUtil.DecimalTextWatcher(
                        mEdittxtFreeStartprice, 2));
        mEdittxtFreeIndentify
                .addTextChangedListener(new StringUtil.DecimalTextWatcher(
                        mEdittxtFreeIndentify, 2));
    }

    @Override
    public void onRecvMsg(MessageData msg) throws Exception {
        super.onRecvMsg(msg);
        if (msg.valiateReq(ProductAddFreeRequest.class)) {
            final AddProduct response = (AddProduct) msg.getRspObject();
            if (CommonValidate.validateQueryState(this, msg, response, "发布失败")) {
                mProductId = response.data.id;
                ShowMsg.showToast(this, "发布成功");
                ShowMsg.showConfirmDialog(this, new ShowMsg.IConfirmDialogKeyBack() {

                    @Override
                    public void onConfirm(boolean confirmValue,
                                          boolean onKeyBack) {
                        if (onKeyBack) {
                            finish();
                            return;
                        }

                        if (!confirmValue) {
                            Bundle bundle = new Bundle();
                            bundle.putString("id", String.valueOf(response.data.id));
                            getIntentHandle().intentToActivity(bundle,
                                    EditOptionActivity_.class);
                        }
                        finish();
                    }
                }, "否", "是", "是否需要添加商品选项？");
            }
        } else if (msg.valiateReq(ProductEditFreeRequest.class)) {
            final CommonReturn response = (CommonReturn) msg.getRspObject();

            if (CommonValidate.validateQueryState(this, msg, response, "编辑失败")) {
                ShowMsg.showToast(this, "编辑成功");
                FramewrokApplication.getInstance().setActivityResult(
                        ProviderResultActivity.CODE_EDIT_PRODUCT, null);
                finish();
            }
        }
    }

    @Override
    protected void showProductInfo(ProductItem productItem) {
        super.showProductInfo(productItem);

        mEdittxtFreeStock.setText(productItem.quantity);
        mEdittxtFreeStartprice.setText(""
                + StringUtil.formatProgress(productItem.price));
    }


    @Click(R.id.btn_submit_click)
    void onClickBtnSubmit() {
        if (!BaseApplication.isLogin()) {
            getIntentHandle().intentToActivity(LoginActivity_.class);
            return;
        }

        if (mProductId > 0) {
            editProduct();
        } else {
            addProduct();
        }
    }

    private void addProduct() {
        mRequest.Title = mEdittxtTitle.getText().toString();
        mRequest.CategoryId = mCategoryId;
        mRequest.Address = mstrAddress;
        mRequest.Description = mstrDesc;
        mRequest.Type = ProductType.XIANZHI.getValue();
        mRequest.Pics = getUploadFile();
        mRequest.Movie = getVideoUploadFile();
        mRequest.MovieThumbId = getVideoThumbnailFile();
        mRequest.SecurityDelivery = getSecurityDelivery();
        mRequest.Security7days = getSecurity7days();
        try {
            if (!TextUtils.isEmpty(mEdittxtFreeStock.getText().toString())) {
                mRequest.Quantity =
                        Integer.parseInt(mEdittxtFreeStock.getText().toString());
            }

            if (!TextUtils.isEmpty(mEdittxtFreeStartprice.getText().toString())) {
                mRequest.Price =
                        Double.parseDouble(mEdittxtFreeStartprice.getText()
                                .toString());
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
                ProductBusiness.addFreeProduct(this, getHttpDataLoader(),
                        mRequest);
        if (isSend) {
            showWaitDialog(1, false, R.string.common_submit_data);
        }
    }

    private void editProduct() {
        mFreeRequest = new ProductEditFreeRequest();

        mFreeRequest.Id = mProductId;
        mFreeRequest.Title = mEdittxtTitle.getText().toString();
        mFreeRequest.CategoryId = mCategoryId;
        mFreeRequest.Address = mstrAddress;
        mFreeRequest.Description = mstrDesc;
        mFreeRequest.Type = ProductType.XIANZHI.getValue();
        mFreeRequest.Pics = getUploadFile();
        mFreeRequest.Movie = getVideoUploadFile();
        mFreeRequest.MovieThumbId = getVideoThumbnailFile();
        mFreeRequest.SecurityDelivery = getSecurityDelivery();
        mFreeRequest.Security7days = getSecurity7days();
        mFreeRequest.InSpecialPanic = getInSpecialPainc();
        mFreeRequest.InSpecialGift = getInSpecialGift();
        try {
            if (!TextUtils.isEmpty(mEdittxtFreeStock.getText().toString())) {
                mFreeRequest.Quantity =
                        Integer.parseInt(mEdittxtFreeStock.getText().toString());
            }

            if (!TextUtils.isEmpty(mEdittxtFreeStartprice.getText().toString())) {
                mFreeRequest.Price =
                        Double.parseDouble(mEdittxtFreeStartprice.getText()
                                .toString());
            }

            if (null != mAddress) {
                mFreeRequest.ProvinceId = Integer.parseInt(mAddress.province_id);
                mFreeRequest.CityId = Integer.parseInt(mAddress.city_id);
                mFreeRequest.AreaId = Integer.parseInt(mAddress.area_id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        boolean isSend =
                ProductBusiness.editFreeProduct(this, getHttpDataLoader(),
                        mFreeRequest);
        if (isSend) {
            showWaitDialog(1, false, R.string.common_submit_data);
        }
    }

    @Click(R.id.llayout_category_click)
    protected void onClickRlayoutProductCategory() {
        super.onClickRlayoutProductCategory();
    }

    @Click(R.id.rlayout_product_price_click)
    protected void onClickRlayoutProductPrice() {
        getIntentHandle().intentToActivity(PriceInputActivity_.class);
    }

    @Click(resName = "llayout_description_click")
    protected void onClickEditDescription() {
        super.onClickEditDescription();
    }

    @Override
    public TextView getCategoryEditvew() {
        return mTvewFreeCategory;
    }

    @Override
    public String getCategoryType() {
        return CategoryType.FREE.getValue();
    }

    @Override
    public ProgressBar getCategoryProgressBar() {
        return mProgressBar;
    }

    @Override
    public TextView getDescriptionTextView() {
        return mTvewFreeDescription;
    }
}