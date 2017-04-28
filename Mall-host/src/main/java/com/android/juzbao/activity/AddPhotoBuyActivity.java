
package com.android.juzbao.activity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.juzbao.enumerate.CategoryType;
import com.android.mall.resource.R;
import com.android.juzbao.activity.account.LoginActivity_;
import com.server.api.model.AddProduct;
import com.server.api.service.ProductService.ProductAddPhotoBuyRequest;
import com.android.juzbao.application.BaseApplication;
import com.android.juzbao.model.ProductBusiness;
import com.android.juzbao.enumerate.ProductType;
import com.android.juzbao.util.CommonValidate;
import com.android.zcomponent.http.api.model.MessageData;
import com.android.zcomponent.util.ShowMsg;
import com.android.zcomponent.util.StringUtil;

/**
 * <p>
 * Description: 拍照购
 * </p>
 *
 * @ClassName:PhotoBuyActivity
 * @author: wei
 * @date: 2015-11-10
 */
@EActivity(R.layout.activity_add_photo_buy)
public class AddPhotoBuyActivity extends AddProductActivity {

    @ViewById(R.id.editvew_product_desc_show)
    EditText mEditvewProductDesc;

    @ViewById(R.id.editvew_product_category_show)
    TextView mTvewProductCategory;

    @ViewById(R.id.editvew_product_price_show)
    TextView mEditvewProductPrice;

    @ViewById(R.id.progressbar_categoryz_show)
    ProgressBar mProgressBar;

    private ProductAddPhotoBuyRequest mRequest;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    protected void initUI() {
        super.initUI();
        getTitleBar().setTitleText("拍照购 ");
        mRequest = new ProductAddPhotoBuyRequest();
    }

    @Override
    public void onRecvMsg(MessageData msg) throws Exception {
        super.onRecvMsg(msg);

        if (msg.valiateReq(ProductAddPhotoBuyRequest.class)) {
            AddProduct response = (AddProduct) msg.getRspObject();
            if (CommonValidate.validateQueryState(this, msg, response, "发布失败")) {
                ShowMsg.showToast(this, "发布成功");
                finish();
            }
        }
    }

    @Click(R.id.btn_submit_click)
    void onClickBtnSubmit() {
        if (!BaseApplication.isLogin()) {
            getIntentHandle().intentToActivity(LoginActivity_.class);
            return;
        }

        mRequest.CategoryId = mCategoryId;
        mRequest.Title = mEdittxtTitle.getText().toString();
        mRequest.Description = mEditvewProductDesc.getText().toString();
        mRequest.Type = ProductType.PAIZHAO.getValue();
        mRequest.Pics = getUploadFile();
        mRequest.Movie = getVideoUploadFile();
        mRequest.MovieThumbId = getVideoThumbnailFile();
        mRequest.SecurityDelivery = getSecurityDelivery();
        mRequest.Security7days = getSecurity7days();

        boolean isSend =
                ProductBusiness.addPhotoBuyProduct(this, getHttpDataLoader(),
                        mRequest);
        if (isSend) {
            showWaitDialog(1, false, R.string.common_submit_data);
        }
    }

    @Click(R.id.rlayout_product_category_click)
    protected void onClickRlayoutProductCategory() {
        super.onClickRlayoutProductCategory();
    }

    @Click(R.id.rlayout_product_price_click)
    protected void onClickRlayoutProductPrice() {
        getIntentHandle().intentToActivity(PriceInputActivity_.class);
    }

    @Override
    public void onActivityResultCallBack(int resultCode, Intent intent) {
        mRequest.PriceStart = intent.getDoubleExtra("priceStart", 0);
        mRequest.PriceEnd = intent.getDoubleExtra("priceEnd", 0);

        mEditvewProductPrice.setText("¥"
                + StringUtil.formatProgress(mRequest.PriceStart) + " - ¥"
                + StringUtil.formatProgress(mRequest.PriceEnd));
    }

    @Override
    public TextView getCategoryEditvew() {
        return mTvewProductCategory;
    }

    @Override
    public ProgressBar getCategoryProgressBar() {
        return mProgressBar;
    }

    @Override
    public TextView getDescriptionTextView() {
        return null;
    }

    @Override
    public String getCategoryType() {
        return CategoryType.PRODCUT.getValue();
    }
}