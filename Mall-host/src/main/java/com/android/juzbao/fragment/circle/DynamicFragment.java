package com.android.juzbao.fragment.circle;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;

import com.android.juzbao.activity.circle.TieziActivity;
import com.android.juzbao.adapter.circle.DynamicAdapter;
import com.android.juzbao.base.MyBaseFragment;
import com.android.juzbao.model.circle.DynamicBean;
import com.android.quna.activity.R;
import com.android.zcomponent.http.api.model.MessageData;
import com.android.zcomponent.http.constant.ErrorCode;
import com.android.zcomponent.util.ShowMsg;
import com.server.api.service.CircleService;

import java.util.ArrayList;
import java.util.List;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;


/**
 * Created by admin on 2017/3/17.
 */

public class DynamicFragment extends MyBaseFragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, AbsListView.OnScrollListener {
    private ListView mListView;
    private DynamicAdapter mAdapter;
    private List<DynamicBean.DataBean> mList;
    private ImageView sendImageView;
    private SwipeRefreshLayout refreshLayout;
    private boolean isScroll;

    @Override
    protected View loadLayout(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_dynamic, null);
        return view;
    }

    @Override
    protected void init(View view) {
        mList = new ArrayList<>();

        mAdapter = new DynamicAdapter(getActivity(), mList);
        mListView = (ListView) view.findViewById(R.id.lv_dynamic);
        sendImageView = (ImageView) view.findViewById(R.id.img_dynamic_send);
        mListView.setAdapter(mAdapter);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.rf_dynamic);
        refreshLayout.setColorSchemeResources(android.R.color.holo_orange_light);
        refreshLayout.setOnRefreshListener(this);
        getAllDynamic();
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void set() {
        sendImageView.setOnClickListener(this);
        mListView.setOnScrollListener(this);
    }


    @Override
    public void onClick(View view) {
        startActivityForResult(new Intent(getActivity(), TieziActivity.class), 110);
    }

    /**
     * 获取全部动态
     */
    private void getAllDynamic() {
        CircleService.getAllDynamic getAllDynamic = new CircleService.getAllDynamic();
        getHttpDataLoader().doPostProcess(getAllDynamic, DynamicBean.class);
    }

    @Override
    public void onRecvMsg(MessageData msg) throws Exception {
        super.onRecvMsg(msg);
        if (msg.valiateReq(CircleService.getAllDynamic.class)) {
            DynamicBean response = msg.getRspObject();
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 111) {
            getAllDynamic();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }

    @Override
    public void onRefresh() {
        getAllDynamic();
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {
        isScroll = i==SCROLL_STATE_FLING||i==SCROLL_STATE_TOUCH_SCROLL;
    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i1, int i2) {
        Log.e("gy","这是什么："+i1);
        if(isScroll){
            if (!(mAdapter.current >= i && mAdapter.current <= mListView.getLastVisiblePosition())&&mAdapter.current!=-1) {
                JCVideoPlayer.releaseAllVideos();
                mAdapter.current = -1;
            }
        }
    }

}
