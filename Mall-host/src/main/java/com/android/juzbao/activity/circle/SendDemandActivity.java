package com.android.juzbao.activity.circle;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.juzbao.model.alipay.AliPay;
import com.android.juzbao.model.circle.AliPayBean;
import com.android.juzbao.model.circle.DemandBean;
import com.android.juzbao.utils.TitleBar;
import com.android.quna.activity.R;
import com.android.zcomponent.common.uiframe.BaseActivity;
import com.android.zcomponent.http.api.model.MessageData;
import com.android.zcomponent.http.constant.ErrorCode;
import com.android.zcomponent.util.ShowMsg;
import com.server.api.service.CircleService;


public class SendDemandActivity extends BaseActivity implements View.OnClickListener {
    private EditText needEt, countEt, timeEt, desEt, addressEt, moneyEt;
    private Button okBt;
    private String need, count, time, des, address, money, sendMoney;
    private ProgressDialog progressDialog;
    private View dialogView;
    private LinearLayout llWx, llAli;
    private CheckBox cbWx, cbAli;
    private ImageView cancleImg, okImg;
    private String type = "1";//支付方式
    private TextView dialogMoney;
    private int inviteId;
    private Dialog dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_demand);
        initView();
        setListener();
    }

    private void setListener() {
        okBt.setOnClickListener(this);

    }

    private void initView() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("请稍后..");
        new TitleBar(this).setTitle("发起需求");


        needEt = (EditText) findViewById(R.id.et_send_demand_need);
        timeEt = (EditText) findViewById(R.id.et_send_demand_time);
        desEt = (EditText) findViewById(R.id.et_send_demand_des);
        addressEt = (EditText) findViewById(R.id.et_send_demand_address);
        moneyEt = (EditText) findViewById(R.id.et_send_demand_money);
        countEt = (EditText) findViewById(R.id.et_send_demand_count);
        okBt = (Button) findViewById(R.id.bt_send_demand_ok);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_send_demand_ok://发起需求
                need = needEt.getText().toString().trim();
                time = timeEt.getText().toString().trim();
                des = desEt.getText().toString().trim();
                address = addressEt.getText().toString().trim();
                money = moneyEt.getText().toString().trim();
                count = countEt.getText().toString().trim();

                if (TextUtils.isEmpty(need) || TextUtils.isEmpty(time) || TextUtils.isEmpty(des)
                        || TextUtils.isEmpty(address) || TextUtils.isEmpty(money) || TextUtils.isEmpty(count)) {
                    ShowMsg.showToast(this, "请检查输入");
                    return;
                }
                progressDialog.show();
                sendDemand();
                break;

            case R.id.ll_pay_wx:
                type = "1";
                cbWx.setChecked(true);
                cbAli.setChecked(false);
                break;
            case R.id.ll_pay_ali:
                type = "2";
                cbAli.setChecked(true);
                cbWx.setChecked(false);
                break;
            case R.id.img_dialog_cancle2:
                dialog.dismiss();
                break;
            case R.id.img_dialog_ok2:
                if (TextUtils.isEmpty(type)) {
                    ShowMsg.showToast(this, "请选择支付类型");
                    return;
                }
                if (type.equals("2")) {
                    progressDialog.show();
                    getOrder();
                }
                if (type.equals("1")) {
                    Toast.makeText(this, "暂不支持", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    /**
     * 获取订单编号
     */
    private void getOrder() {
        CircleService.payAliMoney demand = new CircleService.payAliMoney();
        demand.invite_id = inviteId + "";
        getHttpDataLoader().doPostProcess(demand, AliPayBean.class);
    }

    /**
     * 发起需求
     */
    private void sendDemand() {
        CircleService.sendDemand demand = new CircleService.sendDemand();
        demand.demand = need;
        demand.description = des;
        demand.eff_time = time;
        demand.money = money;
        demand.number = count;
        demand.place = address;
        getHttpDataLoader().doPostProcess(demand, DemandBean.class);
    }

    @Override
    public void onRecvMsg(MessageData msg) throws Exception {
        super.onRecvMsg(msg);
        if (msg.valiateReq(CircleService.sendDemand.class)) {//发起需求
            progressDialog.dismiss();
            DemandBean response = msg.getRspObject();
            if (null != response) {
                if (response.getCode() == ErrorCode.INT_QUERY_DATA_SUCCESS) {
                    inviteId = response.getData().getInfo();
                    sendMoney = response.getData().getMoney();
                    showDialog();

                } else {
                    ShowMsg.showToast(getApplicationContext(), msg, response.getMessage());
                }
            }
        }

        if (msg.valiateReq(CircleService.payAliMoney.class)) {//获取支付宝订单号
            progressDialog.dismiss();
            AliPayBean response = msg.getRspObject();
            if (null != response) {
                if (response.getCode() == ErrorCode.INT_QUERY_DATA_SUCCESS) {
                    String orderNo = response.getData().getOrder_no();
                    if (!TextUtils.isEmpty(orderNo)) {
                        alipay(response.getData().getOrder_no());
                    }
                } else {
                    ShowMsg.showToast(getApplicationContext(), msg, response.getMessage());
                }
            }
        }
    }

    private void alipay(String orderNo) {
    /* *
     @param total_amount   订单总价
    * @param subject        标题
    * @param body           描述
    * @param order_trade_no 订单no
    **/
        new AliPay(this).payV2(sendMoney, "发布费用", "发布费用", orderNo, new AliPay.AlipayCallBack() {
            @Override
            public void onSuccess() {
                ShowMsg.showToast(SendDemandActivity.this, "发布成功");
                dialog.dismiss();
                finish();
            }

            @Override
            public void onDeeling() {

            }

            @Override
            public void onCancle() {

            }

            @Override
            public void onFailure(String msg) {

            }
        });
    }

    /**
     * 显示支付对话框
     */
    private void showDialog() {
        dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_demand, null);
        llWx = (LinearLayout) dialogView.findViewById(R.id.ll_pay_wx);
        llAli = (LinearLayout) dialogView.findViewById(R.id.ll_pay_ali);
        cbWx = (CheckBox) dialogView.findViewById(R.id.rb_pay_wx);
        cbAli = (CheckBox) dialogView.findViewById(R.id.rb_pay_ali);
        cancleImg = (ImageView) dialogView.findViewById(R.id.img_dialog_cancle2);
        okImg = (ImageView) dialogView.findViewById(R.id.img_dialog_ok2);
        dialogMoney = (TextView) dialogView.findViewById(R.id.tv_dialog_money);
        dialogMoney.setText(sendMoney);
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setView(dialogView);
//        dialog = builder.create();
//        dialog.show();
//        dialog.setCanceledOnTouchOutside(false);

        dialog = new Dialog(this, R.style.wx_dialog);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(dialogView);
        dialog.show();

        llAli.setOnClickListener(this);
        llWx.setOnClickListener(this);
        okImg.setOnClickListener(this);
        cancleImg.setOnClickListener(this);
    }


}
