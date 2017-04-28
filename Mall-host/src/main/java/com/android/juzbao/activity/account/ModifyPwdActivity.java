package com.android.juzbao.activity.account;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.os.Bundle;

import com.android.mall.resource.R;
import com.server.api.model.CommonReturn;
import com.android.juzbao.model.AccountBusiness;
import com.android.zcomponent.http.api.model.MessageData;
import com.android.zcomponent.http.constant.ErrorCode;
import com.android.zcomponent.util.ShowMsg;
import com.android.zcomponent.views.PasswordTransforEditView;
import com.android.zcomponent.views.swipebacklayout.lib.app.SwipeBackActivity;
import com.server.api.service.AccountService;


/**
 * <p>
 * Description:
 * </p>
 *
 * @ClassName:MyModifyPwdActivity
 * @author: wei
 * @date: 2015-11-10
 */
@EActivity(R.layout.activity_my_modify_pwd)
public class ModifyPwdActivity extends SwipeBackActivity {
    @ViewById(R.id.editvew_password_old_show)
    PasswordTransforEditView mEditvewPasswordOld;

    @ViewById(R.id.editvew_password_new_show)
    PasswordTransforEditView mEditvewPassword;

    @ViewById(R.id.editvew_password_confirm_show)
    PasswordTransforEditView mEditvewPasswordConfirm;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    void initUI() {
        getTitleBar().setTitleText("修改登录密码");

        mEditvewPasswordOld.setHint("请输入原密码");
        mEditvewPassword.setHint("请输入新密码");
        mEditvewPasswordConfirm.setHint("请再输入一次新密码");
    }

    @Override
    public void onRecvMsg(MessageData msg) throws Exception {
        if (msg.valiateReq(AccountService.EditPasswordRequest.class)){
            CommonReturn response = (CommonReturn) msg.getRspObject();
            if (null != response) {
                if (response.code == ErrorCode.INT_QUERY_DATA_SUCCESS) {
                    ShowMsg.showToast(getApplicationContext(),
                            "修改密码成功");
                    finish();
                } else {
                    ShowMsg.showToast(getApplicationContext(), response.message);
                }
            } else {
                ShowMsg.showToast(getApplicationContext(),
                        "修改密码失败");
            }
        }
    }

    @Click(R.id.btn_register_click)
    void onClickBtnRegister() {
        String passwordOld = mEditvewPasswordOld.getText().toString();
        String passwordNew = mEditvewPassword.getText().toString();
        String passwordNewConfirm = mEditvewPasswordConfirm.getText().toString();
        boolean isSend = AccountBusiness.editPassword(getApplicationContext(), getHttpDataLoader(),
                passwordOld, passwordNew, passwordNewConfirm);
        if (isSend) {
            showWaitDialog(1, false, R.string.common_submit_data);
        }
    }

}