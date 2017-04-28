package com.android.juzbao.fragment.circle;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.juzbao.activity.circle.MyDynamicActivity;
import com.android.juzbao.adapter.circle.ConcernAdapter;
import com.android.juzbao.base.MyBaseFragment;
import com.android.juzbao.model.circle.AddConcernBean;
import com.android.juzbao.model.circle.ConcernBean;
import com.android.quna.activity.R;
import com.android.zcomponent.http.api.model.MessageData;
import com.android.zcomponent.http.constant.ErrorCode;
import com.android.zcomponent.util.ShowMsg;
import com.server.api.service.CircleService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/3/17.
 * 关注
 */

public class ConcernFragment extends MyBaseFragment implements AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {
    private ListView mListView;
    private ConcernAdapter mAdapter;
    private List<ConcernBean.DataBean> mList;
    private SwipeRefreshLayout refreshLayout;

    @Override
    protected View loadLayout(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_concern, null);
        return view;
    }

    @Override
    protected void init(View view) {
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.sr_concern);
        mList = new ArrayList<>();
        mAdapter = new ConcernAdapter(getActivity(),mList);
        mListView = (ListView) view.findViewById(R.id.lv_concern);
        mListView.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void set() {
        mListView.setOnItemClickListener(this);
        refreshLayout.setColorSchemeResources(android.R.color.holo_orange_light);
        refreshLayout.setOnRefreshListener(this);
    }

    /**
     * 获取关注列表
     */
    private void getConcern(){
        CircleService.getConcern concern = new CircleService.getConcern();
        getHttpDataLoader().doPostProcess(concern, ConcernBean.class);
    }

    @Override
    public void onResume() {
        super.onResume();
        getConcern();
    }

    @Override
    public void onRecvMsg(MessageData msg) throws Exception {
        super.onRecvMsg(msg);
        if (msg.valiateReq(CircleService.getConcern.class)) {//获取关注人
            refreshLayout.setRefreshing(false);
            ConcernBean response = msg.getRspObject();
            if (null != response) {
                if (response.getCode() == ErrorCode.INT_QUERY_DATA_SUCCESS) {
                    mList.clear();
                    mList.addAll(response.getData());
                    mAdapter.notifyDataSetChanged();
                } else {
                    mList.clear();
                    mAdapter.notifyDataSetChanged();
//                    ShowMsg.showToast(getActivity(), msg, response.getMessage());
                }
            }
        }

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(getActivity(), MyDynamicActivity.class);
        intent.putExtra("uid",mList.get(i).getTo_uid());
        startActivity(intent);
    }

    @Override
    public void onRefresh() {
        getConcern();
    }
}
