package com.android.juzbao.activity.circle;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.juzbao.adapter.circle.DemandDetailAdapter;
import com.android.juzbao.model.circle.AddInvitebean;
import com.android.juzbao.model.circle.CommitDynamicBean;
import com.android.juzbao.model.circle.DemandDetailBean;
import com.android.juzbao.utils.TitleBar;
import com.android.quna.activity.R;
import com.android.zcomponent.common.uiframe.BaseActivity;
import com.android.zcomponent.http.api.host.Endpoint;
import com.android.zcomponent.http.api.model.MessageData;
import com.android.zcomponent.http.constant.ErrorCode;
import com.android.zcomponent.util.ShowMsg;
import com.android.zcomponent.views.CircleImageView;
import com.bumptech.glide.Glide;
import com.server.api.model.CommonReturn;
import com.server.api.service.CircleService;

import java.util.ArrayList;
import java.util.List;

public class DemandDetailActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {
    private ListView mListView;
    private View headView, dialogView;
    private TextView yingYao, nmYingYao;
    private CircleImageView headAvatar;
    private TextView headName, headMoney, headNeed, headCount, headTime, headAddress, headDes, headInviter;
    private String inviteId, name;
    private List<DemandDetailBean.DataBean.YingListBean> mList;
    private DemandDetailAdapter mAdapter;
    private String type, id;
    private DemandDetailBean.DataBean.DetailBean detailBean;
    private EditText dialogInput;
    private ProgressDialog progressDialog;
    private LinearLayout layout;
    private Dialog dialog;
    private Dialog dialog2;
    private SwipeRefreshLayout refreshLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demand_detail);
        inviteId = getIntent().getStringExtra("invite_id");
        name = getIntent().getStringExtra("name");
        initView();
        setListener();
        loadDemandDetail();
    }

    private void setListener() {
        yingYao.setOnClickListener(this);
        nmYingYao.setOnClickListener(this);
        mListView.setOnItemClickListener(this);
    }

    private void initView() {
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.sr_demand_detail);
        refreshLayout.setColorSchemeResources(android.R.color.holo_orange_light);
        refreshLayout.setOnRefreshListener(this);
        layout = (LinearLayout) findViewById(R.id.ll_demand_detail);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("请稍后");
        progressDialog.setCanceledOnTouchOutside(false);
        new TitleBar(this).setTitle(name + "的需求");
        mListView = (ListView) findViewById(R.id.lv_demand_detail);
        headView = View.inflate(this, R.layout.head_demand_detail, null);
        mListView.addHeaderView(headView);
        yingYao = (TextView) findViewById(R.id.tv_demand_detail_yy);
        nmYingYao = (TextView) findViewById(R.id.tv_demand_detail_nmyy);

        headAvatar = (CircleImageView) headView.findViewById(R.id.img_demand_detail_avatar);
        headName = (TextView) headView.findViewById(R.id.tv_demand_detail_name);
        headMoney = (TextView) headView.findViewById(R.id.tv_demand_detail_money);
        headNeed = (TextView) headView.findViewById(R.id.tv_demand_detail_need);
        headCount = (TextView) headView.findViewById(R.id.tv_demand_detail_count);
        headTime = (TextView) headView.findViewById(R.id.tv_demand_detail_time);
        headAddress = (TextView) headView.findViewById(R.id.tv_demand_detail_address);
        headDes = (TextView) headView.findViewById(R.id.tv_demand_detail_des);
        headInviter = (TextView) headView.findViewById(R.id.tv_demand_detail_invite);

        mList = new ArrayList<>();
        mAdapter = new DemandDetailAdapter(this, mList);
        mListView.setAdapter(mAdapter);
    }

    private void loadDemandDetail() {
        CircleService.demandDetail demandDetail = new CircleService.demandDetail();
        demandDetail.inv_id = inviteId;
        getHttpDataLoader().doPostProcess(demandDetail, DemandDetailBean.class);
    }

    @Override
    public void onRecvMsg(MessageData msg) throws Exception {
        super.onRecvMsg(msg);
        if (msg.valiateReq(CircleService.demandDetail.class)) {
            refreshLayout.setRefreshing(false);
            DemandDetailBean response = msg.getRspObject();
            if (null != response) {
                if (response.getCode() == ErrorCode.INT_QUERY_DATA_SUCCESS) {
                    detailBean = response.getData().getDetail();
                    setInfo(response.getData().getDetail());
                    mList.clear();
                    mList.addAll(response.getData().getYing_list());
                    mAdapter.notifyDataSetChanged();
                } else {
                    ShowMsg.showToast(getApplicationContext(), response.getMessage());
                }
            }
        }

        if (msg.valiateReq(CircleService.addInvite.class)) {
            AddInvitebean response = msg.getRspObject();
            progressDialog.dismiss();
            if (null != response) {
                if (response.getCode() == ErrorCode.INT_QUERY_DATA_SUCCESS) {
                    ShowMsg.showToast(this, "应邀成功");
                    loadDemandDetail();
                    dialog2.dismiss();
                } else {
                    ShowMsg.showToast(getApplicationContext(), response.getMessage());
                }
            }
        }

        if (msg.valiateReq(CircleService.agreeInvite.class)) {//接受邀约
            CommonReturn response = msg.getRspObject();
            progressDialog.dismiss();
            if (null != response) {
                if (response.code == ErrorCode.INT_QUERY_DATA_SUCCESS) {

                    ShowMsg.showToast(getApplicationContext(), "已成功通知对方");
                    loadDemandDetail();
                    dialog.dismiss();
                } else {
                    ShowMsg.showToast(getApplicationContext(), response.message);
                }
            }
        }
    }

    private void setInfo(DemandDetailBean.DataBean.DetailBean bean) {
        if (bean.getIs_own() == 1) {
            layout.setVisibility(View.GONE);
        }

        Glide.with(this).load(Endpoint.IMAGE + bean.getAvatar_img()).error(R.drawable.ease_default_avatar)
                .placeholder(R.drawable.ease_default_avatar).into(headAvatar);

        headName.setText(bean.getNickname());
        headAddress.setText(bean.getPlace());
        headCount.setText(bean.getNumber() + "人");
        headNeed.setText(bean.getDemand());
        headTime.setText(bean.getEnd_date());
        headDes.setText(bean.getDescription());
        headInviter.setText(bean.getYing_cnt() + "人已应邀");
        headMoney.setText(bean.getMoney());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_demand_detail_yy:
                if (detailBean.getIs_invite() == 1) {
                    ShowMsg.showToast(this, "不可以重复应邀");
                    return;
                }
                type = "1";
                inviteDialog();
                break;
            case R.id.tv_demand_detail_nmyy:
                if (detailBean.getIs_invite() == 1) {
                    ShowMsg.showToast(this, "不可以重复应邀");
                    return;
                }
                type = "2";
                inviteDialog();
                break;
            case R.id.tv_dialog_demand_detail_ok:
                if (TextUtils.isEmpty(type)) {
                    ShowMsg.showToast(this, "请选择应邀类型");
                    return;
                }
                String content = dialogInput.getText().toString().trim();
//                if (TextUtils.isEmpty(content)) {
//                    ShowMsg.showToast(this, "请填写您的技能");
//                    return;
//                }
                progressDialog.show();
                addInvite(content);
                break;
            case R.id.tv_dialog_demand_detail_cancle:
                dialog2.dismiss();
                break;
            case R.id.bt_agree_ok:
                progressDialog.show();
                agreeInvite();
                break;
            case R.id.bt_agree_cancle:
                dialog.dismiss();
                break;
        }
    }

    /**
     * 应邀
     *
     * @param content
     */
    private void addInvite(String content) {
        CircleService.addInvite addInvite = new CircleService.addInvite();
        addInvite.inv_id = inviteId;
        addInvite.content = content;
        addInvite.type = type;
        getHttpDataLoader().doPostProcess(addInvite, AddInvitebean.class);
    }

    /**
     * 应邀Dialog
     */
    private void inviteDialog() {
        dialogView = View.inflate(this, R.layout.dialog_demand_detail, null);
        CircleImageView dialogAvatar = (CircleImageView) dialogView.findViewById(R.id.img_dialog_demand_detail_avatar);
        TextView dialogTitle = (TextView) dialogView.findViewById(R.id.tv_dialog_demand_detail_title);
        TextView dialogName = (TextView) dialogView.findViewById(R.id.tv_dialog_demand_detail_name);
        TextView dialogMoney = (TextView) dialogView.findViewById(R.id.tv_dialog_demand_detail_money);
        TextView dialogDeadLine = (TextView) dialogView.findViewById(R.id.tv_dialog_demand_detail_deadline);
        TextView dialogTime = (TextView) dialogView.findViewById(R.id.tv_dialog_demand_detail_time);
        TextView dialogAddress = (TextView) dialogView.findViewById(R.id.tv_dialog_demand_detail_address);
        dialogInput = (EditText) dialogView.findViewById(R.id.et_dialog_demand_detail_input);
        TextView dialogOk = (TextView) dialogView.findViewById(R.id.tv_dialog_demand_detail_ok);
        TextView dialogCancle = (TextView) dialogView.findViewById(R.id.tv_dialog_demand_detail_cancle);
        dialogOk.setOnClickListener(this);
        dialogCancle.setOnClickListener(this);

        if (detailBean == null) {
            return;
        }
        dialogTitle.setText("邀约地点"+detailBean.getPlace()+"需求"+detailBean.getDemand());
        dialogName.setText(detailBean.getNickname());
        dialogAddress.setText(detailBean.getPlace());
        dialogDeadLine.setText(detailBean.getEff_time() + "天");
        dialogTime.setText(detailBean.getEnd_date());
        dialogMoney.setText(detailBean.getMoney());
        Glide.with(this).load(Endpoint.IMAGE + detailBean.getAvatar_img()).error(R.drawable.ease_default_avatar)
                .placeholder(R.drawable.ease_default_avatar).into(dialogAvatar);

        dialog2 = new Dialog(this, R.style.wx_dialog);
        dialog2.setCanceledOnTouchOutside(false);
        dialog2.setContentView(dialogView);
        dialog2.show();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (detailBean.getIs_own() != 1) {
            return;
        }
        Log.e("gyy","当前位置："+i);
        Log.e("gyy","数组长度："+mList.size());
        if (mList.get(i-1 ).getStatus().equals("1") || mList.get(i -1).getStatus().equals("2")) {
            return;
        }
        id = mList.get(i-1 ).getId();
        String agreeContent = "是否接受 " + mList.get(i - 1).getNickname() + " 的应邀,陪您一起" + detailBean.getDemand();
        agreeDialog(agreeContent);

    }

    /**
     * 接收应邀Dialog
     */
    private void agreeDialog(String content) {

        View agreeView = View.inflate(this, R.layout.dialog_demand_detail_agree, null);
        TextView agreeContent = (TextView) agreeView.findViewById(R.id.tv_agree_content);
        Button agreeOk = (Button) agreeView.findViewById(R.id.bt_agree_ok);
        Button agreeCancle = (Button) agreeView.findViewById(R.id.bt_agree_cancle);
        agreeOk.setOnClickListener(this);
        agreeCancle.setOnClickListener(this);
        agreeContent.setText(content);
        dialog = new Dialog(this, R.style.wx_dialog);
        dialog.setContentView(agreeView);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void agreeInvite() {
        CircleService.agreeInvite addInvite = new CircleService.agreeInvite();
        addInvite.id = id;
        getHttpDataLoader().doPostProcess(addInvite, CommonReturn.class);
    }

    @Override
    public void onRefresh() {
        loadDemandDetail();
    }
}
