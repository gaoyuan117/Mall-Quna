package com.android.juzbao.fragment.circle;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.juzbao.activity.circle.DemandDetailActivity;
import com.android.juzbao.activity.circle.SendDemandActivity;
import com.android.juzbao.adapter.circle.InviteAdapter;
import com.android.juzbao.base.MyBaseFragment;
import com.android.juzbao.model.circle.AllDemandBean;
import com.android.juzbao.model.circle.CommitDynamicBean;
import com.android.juzbao.model.circle.DemandBean;
import com.android.quna.activity.R;
import com.android.zcomponent.http.api.model.MessageData;
import com.android.zcomponent.http.constant.ErrorCode;
import com.android.zcomponent.util.ShowMsg;
import com.server.api.service.CircleService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/3/17.
 * 约
 */

public class InviteFragment extends MyBaseFragment implements View.OnClickListener, AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {
    private ListView mListView;
    private InviteAdapter mAdapter;
    private List<AllDemandBean.DataBean> mList;
    private ImageView imageView;
    private SwipeRefreshLayout refreshLayout;

    @Override
    protected View loadLayout(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_invite, null);
        return view;
    }

    @Override
    protected void init(View view) {
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.rf_invite);
        mList = new ArrayList<>();
        mAdapter = new InviteAdapter(getActivity(), mList);
        mListView = (ListView) view.findViewById(R.id.lv_invite);
        imageView = (ImageView) view.findViewById(R.id.img_invite_send);
        mListView.setAdapter(mAdapter);
        refreshLayout.setColorSchemeResources(android.R.color.holo_orange_light);
        refreshLayout.setOnRefreshListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onResume() {
        super.onResume();
        getAllInvite();
    }

    @Override
    protected void set() {
        imageView.setOnClickListener(this);
        mListView.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View view) {
        startActivity(new Intent(getActivity(), SendDemandActivity.class));
    }

    private void getAllInvite() {
        CircleService.getAllDemand dynamic = new CircleService.getAllDemand();
        getHttpDataLoader().doPostProcess(dynamic, AllDemandBean.class);
    }

    @Override
    public void onRecvMsg(MessageData msg) throws Exception {
        super.onRecvMsg(msg);
        if (msg.valiateReq(CircleService.getAllDemand.class)) {//获取全部邀约
            AllDemandBean response = msg.getRspObject();
            refreshLayout.setRefreshing(false);
            if (null != response) {
                if (response.getCode() == ErrorCode.INT_QUERY_DATA_SUCCESS) {
                    mList.clear();
                    mList.addAll(response.getData());
                    mAdapter.notifyDataSetChanged();
                } else {
                    ShowMsg.showToast(getActivity(), msg, response.getMessage());
                }
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(getActivity(), DemandDetailActivity.class);
        intent.putExtra("invite_id",mList.get(i).getId());
        intent.putExtra("name",mList.get(i).getNickname());
        startActivity(intent);
    }

    @Override
    public void onRefresh() {
        getAllInvite();
    }
}
