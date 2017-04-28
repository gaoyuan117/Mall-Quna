package com.android.juzbao.activity.circle;

import android.app.ProgressDialog;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.juzbao.adapter.circle.MyDynamicAdapter;
import com.android.juzbao.model.circle.AddConcernBean;
import com.android.juzbao.model.circle.CommitDynamicBean;
import com.android.juzbao.model.circle.MyDynamicBean;
import com.android.quna.activity.R;
import com.android.zcomponent.common.uiframe.BaseActivity;
import com.android.zcomponent.http.api.host.Endpoint;
import com.android.zcomponent.http.api.model.MessageData;
import com.android.zcomponent.http.constant.ErrorCode;
import com.android.zcomponent.util.ShowMsg;
import com.bumptech.glide.Glide;
import com.server.api.service.CircleService;

import java.util.ArrayList;
import java.util.List;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;

public class MyDynamicActivity extends BaseActivity implements AbsListView.OnScrollListener, SwipeRefreshLayout.OnRefreshListener {
    private ListView mListView;
    private View headView;
    private ImageView headBack, headBg, headAvatar, gz;
    private TextView headName, headDt;
    private MyDynamicAdapter mAdapter;
    private List<MyDynamicBean.DataBean.ListBean> mList;
    private ProgressDialog progressDialog;
    private String uid;
    private boolean isScroll;
    private SwipeRefreshLayout refreshLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_dynamic);
        uid = getIntent().getStringExtra("uid");
        initView();
        progressDialog.show();
        loadMyDynamic();
    }

    private void initView() {
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.sr_my_dynamic);
        refreshLayout.setColorSchemeResources(android.R.color.holo_orange_light);
        refreshLayout.setOnRefreshListener(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("加载中");
        mListView = (ListView) findViewById(R.id.lv_my_dynamic);
        headView = View.inflate(this, R.layout.head_my_dynamic, null);
        mListView.addHeaderView(headView);

        headBack = (ImageView) headView.findViewById(R.id.img_my_dynamic_back);
        headBg = (ImageView) headView.findViewById(R.id.img_my_dynamic_bg);
        headAvatar = (ImageView) headView.findViewById(R.id.img_my_dynamic_avatar);
        headName = (TextView) headView.findViewById(R.id.tv_my_dynamic_name);
        headDt = (TextView) headView.findViewById(R.id.tv_my_dynamic_dt);
        gz = (ImageView) headView.findViewById(R.id.img_my_dynamic_gz);

        mList = new ArrayList<>();
        mAdapter = new MyDynamicAdapter(this, mList);
        mListView.setAdapter(mAdapter);
        mListView.setOnScrollListener(this);
        mListView.setDividerHeight(0);

        headBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        gz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                concern();
            }
        });
    }

    @Override
    public void onRecvMsg(MessageData msg) throws Exception {
        super.onRecvMsg(msg);
        if (msg.valiateReq(CircleService.myDynamic.class)) {
            refreshLayout.setRefreshing(false);
            MyDynamicBean response = msg.getRspObject();
            progressDialog.dismiss();
            if (null != response) {
                if (response.getCode() == ErrorCode.INT_QUERY_DATA_SUCCESS) {
                    if(response.getData().getIs_me()==1){
                        gz.setVisibility(View.GONE);
                    }else {
                        gz.setVisibility(View.VISIBLE);
                    }
                    setInfo(response.getData().getUser());
                    mList.clear();
                    mList.addAll(response.getData().getList());
                    mAdapter.notifyDataSetChanged();
                    if (response.getData().getIs_follow() == 1) {
                        gz.setImageResource(R.drawable.yiguanzhu);
                    } else {
                        gz.setImageResource(R.drawable.guanzhu);
                    }
                } else {
                    ShowMsg.showToast(getApplicationContext(), msg, response.getMessage());
                }
            }
        }
        if (msg.valiateReq(CircleService.concern.class)) {//关注
            AddConcernBean response = msg.getRspObject();
            if (null != response) {
                if (response.getCode() == ErrorCode.INT_QUERY_DATA_SUCCESS) {
                    //判断有没有关注过
                    if (response.getData().getIs_follow() == 1) {//已关注
                        gz.setImageResource(R.drawable.yiguanzhu);
                    } else {//未关注
                        gz.setImageResource(R.drawable.guanzhu);
                    }
                } else {
                    ShowMsg.showToast(getApplicationContext(), msg, response.getMessage());
                }
            }
        }
    }

    private void setInfo(MyDynamicBean.DataBean.UserBean bean) {
        Glide.with(this).load(Endpoint.IMAGE + bean.getAvatat_img()).error(R.drawable.ease_default_avatar).into(headAvatar);
        headName.setText(bean.getNickname());

    }

    private void loadMyDynamic() {
        CircleService.myDynamic dynamic = new CircleService.myDynamic();
        dynamic.uid = uid;
        getHttpDataLoader().doPostProcess(dynamic, MyDynamicBean.class);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {
        isScroll = i == SCROLL_STATE_FLING || i == SCROLL_STATE_TOUCH_SCROLL;
    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i1, int i2) {
        if (isScroll) {
            if (!(mAdapter.current >= i && mAdapter.current <= mListView.getLastVisiblePosition()) && mAdapter.current != -1) {
                JCVideoPlayer.releaseAllVideos();
                mAdapter.current = -1;
            }
        }
    }

    /**
     * 关注
     */
    private void concern() {
        CircleService.concern concern = new CircleService.concern();
        concern.to_uid = uid;
        getHttpDataLoader().doPostProcess(concern, AddConcernBean.class);
    }

    @Override
    public void onRefresh() {
        loadMyDynamic();
    }
}
