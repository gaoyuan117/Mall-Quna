package com.android.juzbao.activity.circle;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.juzbao.model.circle.AddInvitebean;
import com.android.juzbao.model.circle.JpushBean;
import com.android.quna.activity.R;
import com.android.zcomponent.common.uiframe.BaseActivity;
import com.android.zcomponent.http.api.model.MessageData;
import com.android.zcomponent.http.constant.ErrorCode;
import com.android.zcomponent.util.ShowMsg;
import com.android.zcomponent.util.share.ShareDialog;
import com.google.gson.Gson;
import com.server.api.model.CommonReturn;
import com.server.api.service.CircleService;

public class NotifyActivity extends BaseActivity implements View.OnClickListener {
    private TextView content;
    private Button cancle,ok;
    private JpushBean bean;
    private ProgressDialog progressDialog;
    private String type;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify);
        initView();
    }

    private void initView() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("请稍后");
        content = (TextView) findViewById(R.id.tv_notify_content);
        cancle = (Button) findViewById(R.id.bt_notify_cancle);
        ok = (Button) findViewById(R.id.bt_notify_ok);

        cancle.setOnClickListener(this);
        ok.setOnClickListener(this);

        Bundle extras = getIntent().getExtras();
        String json = extras.getString("json");
        content.setText(json);
        String extra = extras.getString("extra");
        if(!TextUtils.isEmpty(extra)){
            bean = new Gson().fromJson(extra,JpushBean.class);

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt_notify_cancle:
                type = "2";
                agreeOrRefuse(type);
                finish();
                break;
            case R.id.bt_notify_ok:
                type = "1";
                agreeOrRefuse(type);
                break;
        }


    }

    private void agreeOrRefuse(String type){
        if(bean==null){
            return;
        }
        progressDialog.show();
        CircleService.agreeOrRefuse addInvite = new CircleService.agreeOrRefuse();
        addInvite.inv_to_id = bean.getInv_to_id();
        addInvite.type = type;
        getHttpDataLoader().doPostProcess(addInvite, CommonReturn.class);

    }

    @Override
    public void onRecvMsg(MessageData msg) throws Exception {
        super.onRecvMsg(msg);
        if (msg.valiateReq(CircleService.agreeOrRefuse.class)) {//接受邀约
            CommonReturn response = msg.getRspObject();
            progressDialog.dismiss();
            if (null != response) {
                if (response.code == ErrorCode.INT_QUERY_DATA_SUCCESS) {
                    if(type.equals("2")){
                        ShowMsg.showToast(getApplicationContext(), "已拒绝");
                    }else if(type.equals("1")){
                        ShowMsg.showToast(getApplicationContext(), "已成功通知对方");
                    }
                    finish();
                } else {
                    ShowMsg.showToast(getApplicationContext(), response.message);
                }
            }
        }
    }
}
