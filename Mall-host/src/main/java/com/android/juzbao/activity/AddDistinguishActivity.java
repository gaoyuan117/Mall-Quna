
package com.android.juzbao.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.juzbao.activity.account.LoginActivity_;
import com.android.juzbao.application.BaseApplication;
import com.android.juzbao.enumerate.CategoryType;
import com.android.juzbao.model.DistinguishBusiness;
import com.android.juzbao.util.CommonValidate;
import com.android.mall.resource.R;
import com.android.zcomponent.http.api.model.MessageData;
import com.server.api.model.DistinguishAdd;
import com.server.api.service.DistinguishService.AddDistinguishRequest;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * <p>
 * Description: 鉴真宝
 * </p>
 *
 * @ClassName:DistinguishActivity
 * @author: wei
 * @date: 2015-11-10
 */
@EActivity(R.layout.activity_add_distinguish)
public class AddDistinguishActivity extends AddProductActivity {

    @ViewById(R.id.checkbox_support_show)
    CheckBox mCheckboxSupport;

    @ViewById(R.id.editvew_product_num_show)
    EditText mEditvewNum;

    @ViewById(R.id.edittxt_goods_title_show)
    EditText mEditvewTitle;

    @ViewById(R.id.editvew_address_phone_show)
    EditText mEditvewAddressPhone;

    @ViewById(R.id.editvew_address_name_show)
    EditText mEditvewAddressName;

    @ViewById(R.id.tvew_product_category_show)
    TextView mTvewProductCategory;

    @ViewById(R.id.tvew_product_num_show)
    TextView mTvewProductNum;

    @ViewById(R.id.progressbar_categoryz_show)
    ProgressBar mProgressBar;

    @ViewById(R.id.editvew_product_desc_show)
    TextView mTvewProductDesc;

    private AddDistinguishRequest mRequest;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    protected void initUI() {
        super.initUI();
        getTitleBar().setTitleText("鉴真宝 ");
        mRequest = new AddDistinguishRequest();
    }

    @Override
    public void onRecvMsg(MessageData msg) throws Exception {
        super.onRecvMsg(msg);

        if (msg.valiateReq(AddDistinguishRequest.class)) {
            DistinguishAdd response = (DistinguishAdd) msg.getRspObject();
            if (CommonValidate.validateQueryState(this, msg, response, "提交失败")) {
                DistinguishBusiness.intentToDistinguishEnsureActivity(this,
                        null, response.Data);
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

        if (mCheckboxSupport.isChecked()) {
            mRequest.Title = mEditvewTitle.getText().toString();
            mRequest.CategoryId = mCategoryId;
            mRequest.Address = mstrAddress;
            mRequest.Pics = getUploadFile();
            mRequest.Movie = getVideoUploadFile();
            mRequest.MovieThumbId = getVideoThumbnailFile();
            mRequest.Description = mstrDesc;
            if (null != mAddress) {
                mRequest.ProvinceId = Integer.parseInt(mAddress.province_id);
                mRequest.CityId = Integer.parseInt(mAddress.city_id);
                mRequest.AreaId = Integer.parseInt(mAddress.area_id);
            }

            mRequest.Mobile = mEditvewAddressPhone.getText().toString();
            mRequest.Realname = mEditvewAddressName.getText().toString();

            try {
                if (!TextUtils.isEmpty(mEditvewNum.getText().toString())) {
                    mRequest.Quantity =
                            Integer.parseInt(mEditvewNum.getText().toString());
                }
            } catch (Exception e) {
            }

            boolean isSend =
                    DistinguishBusiness.addDistinguish(this,
                            getHttpDataLoader(), mRequest);
            if (isSend) {
                showWaitDialog(1, false, R.string.common_submit_data);
            }
        }
    }

    @Click(R.id.rlayout_product_category_click)
    protected void onClickRlayoutProductCategory() {
        super.onClickRlayoutProductCategory();
    }

    @Click(R.id.rlayout_product_desc_click)
    void onClickRlayoutProductDesc() {
        super.onClickEditDescription();
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
        return mTvewProductDesc;
    }

    @Override
    public String getCategoryType() {
        return CategoryType.PRODCUT.getValue();
    }
}