
package com.android.juzbao.activity.me;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.juzbao.activity.CitySelectActivity_;
import com.android.mall.resource.R;
import com.server.api.model.Address;
import com.server.api.model.CommonReturn;
import com.android.juzbao.application.BaseApplication;
import com.android.juzbao.model.AddressBusiness;
import com.android.juzbao.constant.ProviderResultActivity;
import com.android.juzbao.constant.ResultActivity;
import com.android.juzbao.util.CommonValidate;
import com.android.zcomponent.http.api.model.MessageData;
import com.android.zcomponent.http.constant.ErrorCode;
import com.android.zcomponent.json.JsonSerializerFactory;
import com.android.zcomponent.util.ShowMsg;
import com.android.zcomponent.views.swipebacklayout.lib.app.SwipeBackActivity;
import com.server.api.service.AddressService;

/**
 * <p>
 * Description: 新增收货地址
 * </p>
 *
 * @ClassName:MyEditAddressActivity
 * @author: wei
 * @date: 2015-11-10
 */
@EActivity(R.layout.activity_my_edit_address)
public class MyAddressEditActivity extends SwipeBackActivity {

    @ViewById(R.id.editvew_address_show)
    EditText mEditvewAddress;

    @ViewById(R.id.editvew_name_show)
    EditText mEditvewName;

    @ViewById(R.id.editvew_phone_show)
    EditText mEditvewPhone;

    @ViewById(R.id.edit_post_show)
    EditText mEditPost;

    @ViewById(R.id.editvew_address_qinshi)
    EditText mQinShi;

    @ViewById(R.id.tvew_area_show)
    TextView mTvewArea;

    @ViewById(R.id.tvew_street_show)
    TextView mTvewStreet;

    @ViewById(R.id.tvew_address_show)
    TextView mTvewAddress;

    @ViewById(R.id.tvew_name_show)
    TextView mTvewName;

    @ViewById(R.id.tvew_phone_show)
    TextView mTvewPhone;

    @ViewById(R.id.tvew_post_show)
    TextView mTvewPost;

    @ViewById(R.id.tvew_default_show)
    TextView mTvewDefault;

    @ViewById(R.id.checkbox_default_show)
    CheckBox mCheckBoxDefault;

    @ViewById(R.id.btn_add_submit_click)
    Button mBtnAddSubmit;

    @ViewById(R.id.llayout_modify)
    LinearLayout mLlayoutModify;

    private Address.Data mAddress;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    void initUI() {
        Intent intent = getIntent();

        String address = intent.getStringExtra("address");
        if (!TextUtils.isEmpty(address)) {
            mAddress =
                    JsonSerializerFactory.Create().decode(address,
                            Address.Data.class);
        }

        if (null == mAddress) {
            mBtnAddSubmit.setVisibility(View.VISIBLE);
            mLlayoutModify.setVisibility(View.GONE);
            getTitleBar().setTitleText("新增收货地址");
        } else {
            mBtnAddSubmit.setVisibility(View.GONE);
            mLlayoutModify.setVisibility(View.VISIBLE);
            getTitleBar().setTitleText("修改收货地址");
            showAddressInfo();
        }

    }

    @Override
    public void onRecvMsg(MessageData msg) throws Exception {
        if (msg.valiateReq(AddressService.AddReceiverAddressRequest.class)) {
            CommonReturn response = msg.getRspObject();
            if (null != response) {
                if (response.code == ErrorCode.INT_QUERY_DATA_SUCCESS) {
                    ShowMsg.showToast(this, msg,
                            R.string.personal_add_address_success);
                    BaseApplication.getInstance().setActivityResult(
                            ResultActivity.CODE_ADDRESS_ADD_SUCCESS, null);
                    finish();
                } else {
                    ShowMsg.showToast(this, response.message);
                }
            } else {
                ShowMsg.showToast(this, msg,
                        R.string.personal_add_address_failed);
            }
        } else if (msg.valiateReq(AddressService.EditReceiverAddressRequest.class)) {
            CommonReturn response = (CommonReturn) msg.getRspObject();
            if (null != response) {
                if (response.code == ErrorCode.INT_QUERY_DATA_SUCCESS) {
                    ShowMsg.showToast(this, msg,
                            R.string.personal_modify_address_success);
                    BaseApplication.getInstance().setActivityResult(
                            ResultActivity.CODE_ADDRESS_ADD_SUCCESS, null);
                    finish();
                } else {
                    ShowMsg.showToast(this, response.message);
                }
            } else {
                ShowMsg.showToast(this, msg,
                        R.string.personal_modify_address_failed);
            }
        } else if (msg.valiateReq(AddressService.DelReceiverAddressRequest.class)) {
            CommonReturn response = (CommonReturn) msg.getRspObject();
            if (CommonValidate
                    .validateQueryState(this, msg, response, "删除地址失败")) {
                ShowMsg.showToast(this, msg, "删除地址成功");
                BaseApplication.getInstance().setActivityResult(
                        ResultActivity.CODE_ADDRESS_DEL_SUCCESS, null);
                finish();
            }
        }
    }

    @Click(R.id.rlayout_area_show)
    void onClickRlayoutArea() {
        getIntentHandle().intentToActivity(CitySelectActivity_.class);
    }

    @Click(R.id.rlayout_address_show)
    void onClickRlayoutAddress() {
    }

    @Click(R.id.btn_del_click)
    void onClickBtnDel() {
        if (null != mAddress) {
            AddressBusiness.queryDelAddress(getHttpDataLoader(), mAddress.id);
            showWaitDialog(1, false, R.string.common_submit_data);
        }
    }

    @Click(R.id.btn_modify_submit_click)
    void onClickBtnModifySubmit() {

        if (null == mAddress) {
            ShowMsg.showToast(this, "请选择地区");
            return;
        }

        boolean isSend =
                AddressBusiness.modifyMyAddress(this, getHttpDataLoader(),
                        Integer.parseInt(mAddress.id), mEditvewPhone.getText()
                                .toString(), mEditvewName.getText().toString(),
                        mAddress.province_id, mAddress.city_id,
                        mAddress.area_id, mEditvewAddress.getText().toString(),
                        mQinShi.getText().toString(), mCheckBoxDefault.isChecked());
        if (isSend) {
            showWaitDialog(1, false,
                    R.string.personal_modify_address_loading__msg);
        }
    }

    @Click(R.id.btn_add_submit_click)
    void onClickBtnAddSubmit() {

        if (null == mAddress) {
            ShowMsg.showToast(this, "请选择地区");
            return;
        }
        boolean isSend =
                AddressBusiness.addMyAddress(this, getHttpDataLoader(),
                        mEditvewPhone.getText().toString(), mEditvewName
                                .getText().toString(), mAddress.province_id,
                        mAddress.city_id, mAddress.area_id, mEditvewAddress
                                .getText().toString(), mQinShi.getText().toString(),
                        mCheckBoxDefault.isChecked());

        if (isSend) {
            showWaitDialog(1, false, R.string.personal_add_address_loading_msg);
        }
    }

    private void showAddressInfo() {
        if (null == mAddress) {
            return;
        }

        mTvewArea.setText(mAddress.province + mAddress.city + mAddress.area);
        mEditvewAddress.setText(mAddress.address);
        mEditvewName.setText(mAddress.realname);
        mEditvewPhone.setText(mAddress.mobile);

        if ("1".equals(mAddress.is_default)) {
            mCheckBoxDefault.setChecked(true);
        }
    }

    @Override
    public void onActivityResultCallBack(int resultCode, Intent data) {
        if (resultCode == ProviderResultActivity.CODE_CITY_SELECT) {
            Address.Data address =
                    JsonSerializerFactory.Create().decode(
                            data.getStringExtra("address"), Address.Data.class);
            if (null == mAddress) {
                mAddress = address;
            } else {
                mAddress.province = address.province;
                mAddress.province_id = address.province_id;
                mAddress.city = address.city;
                mAddress.city_id = address.city_id;
                mAddress.area = address.area;
                mAddress.area_id = address.area_id;
            }
            showAddressInfo();
        }
    }
}