
package com.android.juzbao.activity.me;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.android.mall.resource.R;
import com.android.zcomponent.http.api.host.Endpoint;
import com.server.api.model.CommonReturn;
import com.server.api.model.Head;
import com.server.api.model.NickName;
import com.server.api.model.Sex;
import com.android.juzbao.application.BaseApplication;
import com.android.juzbao.model.AccountBusiness;
import com.android.juzbao.model.ProviderFileBusiness;
import com.android.juzbao.model.ProviderFileBusiness.OnUploadSuccessListener;
import com.android.juzbao.constant.ResultActivity;
import com.android.juzbao.util.CommonValidate;
import com.android.zcomponent.activity.EditActivity;
import com.android.zcomponent.http.api.model.MessageData;
import com.android.zcomponent.http.constant.ErrorCode;
import com.android.zcomponent.util.ShowMsg;
import com.android.zcomponent.views.CircleImageView;
import com.android.zcomponent.views.WheelViewPopupWindow;
import com.android.zcomponent.views.WheelViewPopupWindow.OnPopSureClickListener;
import com.android.zcomponent.views.swipebacklayout.lib.app.SwipeBackActivity;
import com.server.api.service.AccountService;

/**
 * <p>
 * Description: 个人资料
 * </p>
 *
 * @ClassName:MyInfoActivity
 * @author: wei
 * @date: 2015-11-10
 */
@EActivity(R.layout.activity_my_info)
public class MyInfoActivity extends SwipeBackActivity {

    @ViewById(R.id.imgvew_head_picture_show)
    CircleImageView mImgvewHeadPicture;

    @ViewById(R.id.tvew_nickname_show)
    TextView mTvewNickName;

    @ViewById(R.id.tvew_my_sex_show)
    TextView mTvewSex;

    private String mstrNickName;

    private String mstrSex;

    private WheelViewPopupWindow mPopupWindowSex;

    private String[] mArraySex = {"保密", "男", "女"};

    private ProviderFileBusiness mFileBusiness;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    void initUI() {
        getTitleBar().setTitleText("个人资料");

        mFileBusiness = new ProviderFileBusiness(this, getHttpDataLoader());
        mFileBusiness.setOutParams(1, 1, 400, 400);
        mFileBusiness.setOnUploadSuccessListener(new OnUploadSuccessListener() {

            @Override
            public void onUploadSuccess(String id) {
                AccountBusiness.editHead(getHttpDataLoader(), id);
            }
        });
        AccountBusiness.getNickName(getHttpDataLoader());
        AccountBusiness.getHead(getHttpDataLoader());
        AccountBusiness.getSex(getHttpDataLoader());
    }

    @Override
    public void onRecvMsg(MessageData msg) throws Exception {
        mFileBusiness.onRecvMsg(msg);

        if (msg.valiateReq(AccountService.GetNickNameRequest.class)) {
            NickName response = msg.getRspObject();
            if (null != response) {
                if (response.code == ErrorCode.INT_QUERY_DATA_SUCCESS) {
                    if (null != response.data && response.data.nickname != null) {
                        mTvewNickName.setText(response.data.nickname);
                    } else {
                        mTvewNickName.setText("");
                    }
                }
            }
        } else if (msg.valiateReq(AccountService.HeadPathRequest.class)) {
            Head response = msg.getRspObject();
            if (CommonValidate.validateQueryState(this, msg, response)) {
                loadImage(mImgvewHeadPicture, Endpoint.HOST
                        + response.data.avatar_path, R.drawable.transparent);
            } else {

            }
        } else if (msg.valiateReq(AccountService.EditSexRequest.class)) {
            CommonReturn response = (CommonReturn) msg.getRspObject();
            if (null != response) {
                if (response.code == ErrorCode.INT_QUERY_DATA_SUCCESS) {
                    mTvewSex.setText(mstrSex);
                    ShowMsg.showToast(this, msg, "性别修改成功");
                } else {
                    ShowMsg.showToast(this, msg, response.message);
                }
            } else {
                ShowMsg.showToast(this, msg, "性别修改失败");
            }
        } else if (msg.valiateReq(AccountService.ModifyHeadPathRequest.class)) {
            CommonReturn response = (CommonReturn) msg.getRspObject();
            if (CommonValidate
                    .validateQueryState(this, msg, response, "头像修改失败")) {
                BaseApplication.getInstance().setActivityResult(
                        ResultActivity.CODE_MODIFY_HEAD, null);
                ShowMsg.showToast(this, msg, "头像修改成功");
            }
        } else if (msg.valiateReq(AccountService.EditNickNameRequest.class)) {
            CommonReturn response = (CommonReturn) msg.getRspObject();
            if (null != response) {
                if (response.code == ErrorCode.INT_QUERY_DATA_SUCCESS) {
                    mTvewNickName.setText(mstrNickName);
                    ShowMsg.showToast(this, msg, "昵称修改成功");
                } else {
                    ShowMsg.showToast(this, msg, response.message);
                }
            } else {
                ShowMsg.showToast(this, msg, "昵称修改失败");
            }
        } else if (msg.valiateReq(AccountService.GetSexRequest.class)) {
            Sex response = (Sex) msg.getRspObject();
            if (null != response) {
                if (response.code == ErrorCode.INT_QUERY_DATA_SUCCESS) {
                    if (null != response.data) {
                        mstrSex = mArraySex[response.data.nickname];
                        mTvewSex.setText(mstrSex);
                    } else {
                        mTvewSex.setText("");
                    }
                }
            }
        }
    }

    @Click(R.id.llayout_head_picture_click)
    void onClickLlayoutHeadPicture() {

    }

    @Click(R.id.rlayout_my_picture_click)
    void onClickRlayoutMyPicture() {
        mFileBusiness.selectPicture();
    }

    @Click(R.id.rlayout_my_sex_click)
    void onClickRlayoutMySex() {
        if (null == mPopupWindowSex) {
            mPopupWindowSex = new WheelViewPopupWindow(this);
            mPopupWindowSex.dismissWheelViewSecond();
            mPopupWindowSex.dismissWheelViewThird();
            mPopupWindowSex.setFirstViewAdapter(mArraySex);
            mPopupWindowSex
                    .setOnPopSureClickListener(new OnPopSureClickListener() {

                        @Override
                        public void onPopSureClick(Object firstObj,
                                                   Object secondObj, Object thirdObj) {
                            if (null != firstObj) {
                                int sex = -1;
                                mstrSex = (String) firstObj;
                                if (mstrSex.equals(mArraySex[0])) {
                                    sex = 0;
                                } else if (mstrSex.equals(mArraySex[1])) {
                                    sex = 1;
                                } else if (mstrSex.equals(mArraySex[2])) {
                                    sex = 2;
                                }
                                AccountBusiness.editSex(getHttpDataLoader(),
                                        sex);
                                showWaitDialog(1, false,
                                        R.string.common_submit_data);
                            }
                        }
                    });
        }
        mPopupWindowSex.showWindowBottom(mTvewSex);
    }

    @Click(R.id.rlayout_my_nick_click)
    void onClickRlayoutMyNick() {
        Bundle bundle = new Bundle();
        bundle.putString(EditActivity.TITLE, "编辑昵称");
        bundle.putString(EditActivity.HINT, "请输入昵称");
        bundle.putInt(EditActivity.MIN_LINE, 1);
        getIntentHandle().intentToActivity(bundle, EditActivity.class);
    }

    @Click(R.id.rlayout_my_help_click)
    void onClickRlayoutMyHelp() {

    }

    @Click(R.id.rlayout_my_mingpian_click)
    void onClickRlayoutMyMingpian() {

    }

    @Click(R.id.rlayout_my_address_click)
    void onClickRlayoutMyAddress() {
        getIntentHandle().intentToActivity(MyAddressActivity_.class);
    }

    @Override
    public void onActivityResultCallBack(int resultCode, Intent intent) {
        if (resultCode == EditActivity.CODE_EDIT_COMPLETE) {
            if (!TextUtils.isEmpty(intent.getStringExtra("content"))) {
                mstrNickName = intent.getStringExtra("content");
                AccountBusiness.editNickName(getHttpDataLoader(), mstrNickName);
                showWaitDialog(1, false, R.string.common_submit_data);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mFileBusiness.clear();
        Bitmap bitmap =
                mFileBusiness.onActivityResult(requestCode, resultCode, data);

        if (null != bitmap) {
            mImgvewHeadPicture.setImageBitmap(bitmap);
        }
    }
}