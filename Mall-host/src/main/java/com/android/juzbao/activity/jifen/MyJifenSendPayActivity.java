package com.android.juzbao.activity.jifen;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;

import com.android.juzbao.dao.jifendao.JiFenDao;
import com.android.quna.activity.R;
import com.android.zcomponent.http.api.model.MessageData;
import com.android.zcomponent.http.constant.ErrorCode;
import com.android.zcomponent.views.swipebacklayout.lib.app.SwipeBackActivity;
import com.server.api.model.jifenmodel.JifenCommonReturn;
import com.server.api.service.JiFenService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by Koterwong on 2016/7/30.
 */
@EActivity(R.layout.activity_my_jifen_send_pay)
public class MyJifenSendPayActivity extends SwipeBackActivity {

    @ViewById(R.id.edtv_pay_jifen_account)
    EditText mPayAccountEt;
    @ViewById(R.id.edtv_pay_jifen_count)
    EditText mPayCountEt;

    @AfterViews
    void initUI() {
        getTitleBar().setTitleText("支付");
    }


    @Override public void onRecvMsg(MessageData msg) throws Exception {
        if (msg.valiateReq(JiFenService.JifenUserGiveScoreRequset.class)) {
            JifenCommonReturn response = msg.getRspObject();
            if (response != null) {
                if (response.code == ErrorCode.INT_QUERY_DATA_SUCCESS) {
                    Bundle bundle = new Bundle();
                    getIntentHandle().intentToActivity(bundle, ConvertStateActivity_.class);
                    dismissWaitDialog();
                } else {
                    showToast(response.data);
                    dismissWaitDialog();
                }
            } else {
                showToast("积分赠送失败");
                dismissWaitDialog();
            }
        }
    }

    /**
     * 向他人支付积分
     */
    @Click(R.id.btn_jifen_send_pay_click)
    void onClickJifenPayBtn() {

        String uid = mPayAccountEt.getText().toString();
        String score_num = mPayCountEt.getText().toString();

        if (TextUtils.isEmpty(uid) || TextUtils.isEmpty(score_num)) {
            showToast("输入内容不能为空");
            return;
        }

        showWaitDialog(2, false, "正在支付", true);
        JiFenDao.sendJiFenGiveScoreRequest(getHttpDataLoader(), uid, score_num);
    }
}
