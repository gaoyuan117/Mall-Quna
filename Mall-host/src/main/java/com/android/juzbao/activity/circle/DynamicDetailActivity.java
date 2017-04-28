package com.android.juzbao.activity.circle;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.juzbao.adapter.circle.DynamicGvAdaper;
import com.android.juzbao.adapter.circle.PingLunAdapter;
import com.android.juzbao.model.circle.AddConcernBean;
import com.android.juzbao.model.circle.DynamicDetailBean;
import com.android.juzbao.model.circle.ZanBean;
import com.android.juzbao.utils.TitleBar;
import com.android.juzbao.utils.Util;
import com.android.juzbao.view.MyGridView;
import com.android.quna.activity.R;
import com.android.zcomponent.common.uiframe.BaseActivity;
import com.android.zcomponent.http.api.host.Endpoint;
import com.android.zcomponent.http.api.model.MessageData;
import com.android.zcomponent.http.constant.ErrorCode;
import com.android.zcomponent.util.ListUtil;
import com.android.zcomponent.util.ShowMsg;
import com.android.zcomponent.util.share.ShareDialog;
import com.android.zcomponent.util.share.ShareReqParams;
import com.android.zcomponent.views.CircleImageView;
import com.bumptech.glide.Glide;
import com.server.api.model.CommonReturn;
import com.server.api.service.CircleService;

import java.util.ArrayList;
import java.util.List;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

public class DynamicDetailActivity extends BaseActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    public static String type = "1";//评论类型 1：评论帖子  2：回复评论
    public static String ID;//评论id 1：评论帖子  2：回复评论

    private CircleImageView circleImageView;
    private TextView name, content, zanCount, plCount, allCount;
    private EditText editText;
    private ImageView guanZhu, send, imgZan;
    private LinearLayout dianZan, fenXiang, pingLun;
    private MyGridView imgGridView;
    private DynamicGvAdaper imgAdapter;
    private PingLunAdapter pingLunAdapter;
    private List<String> mImgs;
    private List<DynamicDetailBean.DataBean.CommentBean> mComments;
    private String id, uid, input;
    private ListView mListView;
    private View headView;
    private DynamicDetailBean.DataBean data;
    private ShareDialog mShareCustomDialog;
    private JCVideoPlayerStandard player;
    private SwipeRefreshLayout refreshLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_detail);
        id = getIntent().getStringExtra("id");
        ID = id;
        initView();
        initData();
        setListener();
    }

    private void setListener() {
        dianZan.setOnClickListener(this);
        pingLun.setOnClickListener(this);
        send.setOnClickListener(this);
        editText.setOnClickListener(this);
        guanZhu.setOnClickListener(this);
        fenXiang.setOnClickListener(this);
        refreshLayout.setColorSchemeResources(android.R.color.holo_orange_light);
        refreshLayout.setOnRefreshListener(this);
        circleImageView.setOnClickListener(this);
        name.setOnClickListener(this);

    }

    private void initData() {
        mImgs = new ArrayList<>();
        mComments = new ArrayList<>();

        imgAdapter = new DynamicGvAdaper(this, mImgs);
        pingLunAdapter = new PingLunAdapter(this, mComments, this, editText);
        mListView.setAdapter(pingLunAdapter);
        imgGridView.setAdapter(imgAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDynamicDeail();
    }

    private void initView() {
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.sr_dynamic_detail);
        headView = View.inflate(this, R.layout.head_list, null);

        imgZan = (ImageView) headView.findViewById(R.id.img_dynamic_detail_zan);
        send = (ImageView) findViewById(R.id.img_dynamic_detail_send);
        circleImageView = (CircleImageView) headView.findViewById(R.id.img_dynamic_detail_avatar);
        name = (TextView) headView.findViewById(R.id.tv_dynamic_detail_name);
        content = (TextView) headView.findViewById(R.id.tv_dynamic_detail_content);
        zanCount = (TextView) headView.findViewById(R.id.tv_dynamic_detail_dz);
        allCount = (TextView) headView.findViewById(R.id.tv_dynamic_detail_allpl);
        plCount = (TextView) headView.findViewById(R.id.tv_dynamic_detail_pl);
        guanZhu = (ImageView) headView.findViewById(R.id.img_dynamic_detail_gz);
        dianZan = (LinearLayout) headView.findViewById(R.id.ll_dynamic_detail_dz);
        fenXiang = (LinearLayout) headView.findViewById(R.id.ll_dynamic_detail_fx);
        pingLun = (LinearLayout) headView.findViewById(R.id.ll_dynamic_detail_pl);
        imgGridView = (MyGridView) headView.findViewById(R.id.gv_dynamic_detail);
        player = (JCVideoPlayerStandard) headView.findViewById(R.id.jc_demand_detail_player);
        mListView = (ListView) findViewById(R.id.gv_dynamic_detail_pl);
        mListView.addHeaderView(headView);
        editText = (EditText) findViewById(R.id.et_dynamic_detail_input);
        editText.requestFocus();//主动获取一次焦点
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mShareCustomDialog != null && mShareCustomDialog.isShowing()) {
            mShareCustomDialog.dismiss();
        }
    }

    /**
     * 加载动态详情
     */
    private void loadDynamicDeail() {
        if (TextUtils.isEmpty(id)) {
            return;
        }
        CircleService.loadDynamicDetail dynamicDetail = new CircleService.loadDynamicDetail();
        dynamicDetail.id = id;
        getHttpDataLoader().doPostProcess(dynamicDetail, DynamicDetailBean.class);
    }

    @Override
    public void onRecvMsg(MessageData msg) throws Exception {
        super.onRecvMsg(msg);
        if (msg.valiateReq(CircleService.loadDynamicDetail.class)) {//获取详情
            refreshLayout.setRefreshing(false);
            DynamicDetailBean response = msg.getRspObject();
            if (null != response) {
                if (response.getCode() == ErrorCode.INT_QUERY_DATA_SUCCESS) {
                    data = response.getData();
                    setInfo(response.getData().getPost_content(), response.getData().getComment());
                } else {
                    ShowMsg.showToast(getApplicationContext(), msg, response.getMessage());
                }
            }
        }

        if (msg.valiateReq(CircleService.zan.class)) {//点赞
            ZanBean response = msg.getRspObject();
            if (null != response) {
                if (response.getCode() == ErrorCode.INT_QUERY_DATA_SUCCESS) {
                    loadDynamicDeail();
                } else {
                    ShowMsg.showToast(getApplicationContext(), msg, response.getMessage());
                }
            }
        }

        if (msg.valiateReq(CircleService.comment.class)) {//评论
            CommonReturn response = msg.getRspObject();
            if (null != response) {
                if (response.code == ErrorCode.INT_QUERY_DATA_SUCCESS) {
                    editText.setText("");
                    loadDynamicDeail();
                    editText.setHint("写评论");

                } else {
                    ShowMsg.showToast(getApplicationContext(), msg, response.message);
                }
            }
        }

        if (msg.valiateReq(CircleService.reply.class)) {//回复评论
            CommonReturn response = msg.getRspObject();
            if (null != response) {
                if (response.code == ErrorCode.INT_QUERY_DATA_SUCCESS) {
                    editText.setText("");
                    type = "1";
                    ID = id;
                    editText.setHint("写评论");
                    loadDynamicDeail();
                } else {
                    ShowMsg.showToast(getApplicationContext(), msg, response.message);
                }
            }
        }

        if (msg.valiateReq(CircleService.concern.class)) {//关注
            AddConcernBean response = msg.getRspObject();
            if (null != response) {
                if (response.getCode() == ErrorCode.INT_QUERY_DATA_SUCCESS) {
                    //判断有没有关注过
                    if (response.getData().getIs_follow() == 1) {//已关注
                        guanZhu.setImageResource(R.drawable.yiguanzhu);
                    } else {//未关注
                        guanZhu.setImageResource(R.drawable.guanzhu);
                    }
                } else {
                    ShowMsg.showToast(getApplicationContext(), msg, response.getMessage());
                }
            }
        }
    }

    private void setInfo(DynamicDetailBean.DataBean.PostContentBean bean, List<DynamicDetailBean.DataBean.CommentBean> bean2) {
        if (bean.getContent().length() > 7) {
            new TitleBar(this).setTitle(bean.getContent().substring(0, 7) + "......");
        } else {
            new TitleBar(this).setTitle(bean.getContent());
        }
        uid = bean.getUid();
        Glide.with(this).load(Endpoint.IMAGE + bean.getAvatar_img())
                .error(R.drawable.ease_default_avatar)
                .into(circleImageView);
        name.setText(bean.getNickname());
        content.setText(bean.getContent());
        zanCount.setText(bean.getThumbs_up_cnt());
        plCount.setText(bean.getComment_cnt());
        allCount.setText("所有评论(" + bean.getComment_cnt() + ")");

        //判断有没有关注过
        if (bean.getIs_follow() == 1) {//已关注
            guanZhu.setImageResource(R.drawable.yiguanzhu);
        } else if (bean.getIs_follow() == 0) {//未关注
            guanZhu.setImageResource(R.drawable.guanzhu);
        } else if (bean.getIs_follow() == 2) {
            guanZhu.setVisibility(View.GONE);
        }
        //判断是否点赞过
        if (bean.getIs_thumbs_up() == 1) {//已赞
            imgZan.setImageResource(R.drawable.dianzan);
            zanCount.setTextColor(getResources().getColor(R.color.zan2));
        } else {
            imgZan.setImageResource(R.drawable.dianzan2);
            zanCount.setTextColor(getResources().getColor(R.color.black));
        }

        //判断显示
        if (bean.getIs_img() == 1) {//图片
            player.setVisibility(View.GONE);
            imgGridView.setVisibility(View.VISIBLE);
            mImgs.clear();
            mImgs.addAll(bean.getImage_path());
            imgAdapter.notifyDataSetChanged();
        } else if (bean.getIs_img() == 2) {//视频
            imgGridView.setVisibility(View.GONE);
            player.setVisibility(View.VISIBLE);
            if (bean.getContent().length() > 8) {
                player.setUp(bean.getMovie_path(), bean.getContent().substring(0, 8));
            } else {
                player.setUp(bean.getMovie_path(), bean.getContent());
            }
        } else {//纯文字
            imgGridView.setVisibility(View.GONE);
            player.setVisibility(View.GONE);
        }

        mComments.clear();
        if (bean2 != null) {
            mComments.addAll(bean2);
        }
        pingLunAdapter.notifyDataSetChanged();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_dynamic_detail_dz:
                zan(id, "1");//帖子点赞
                break;
            case R.id.ll_dynamic_detail_fx:
                setShareContent();
                break;
            case R.id.ll_dynamic_detail_pl://评论帖子
                type = "1";
                ID = id;
                editText.setHint("写评论");
                Util.toggleSoftKeyboardState(this);
                break;
            case R.id.img_dynamic_detail_send:
                input = editText.getText().toString().trim();
                if (TextUtils.isEmpty(input)) {
                    ShowMsg.showToast(this, "请输入评论内容");
                    return;
                }
                Util.toggleSoftKeyboardState(this);
                addComment();
                break;
            case R.id.img_dynamic_detail_gz:
                concern();
                break;
            case R.id.img_dynamic_detail_avatar:
                Intent intent = new Intent(this, MyDynamicActivity.class);
                intent.putExtra("uid", uid);
                startActivity(intent);
                break;
            case R.id.tv_dynamic_detail_name:
                Intent intent2 = new Intent(this, MyDynamicActivity.class);
                intent2.putExtra("uid", uid);
                startActivity(intent2);
                break;
        }
    }

    /**
     * 设置分享内容
     */
    private void setShareContent() {
        if (null == mShareCustomDialog) {
            mShareCustomDialog = new ShareDialog(this);
        }
        if (!mShareCustomDialog.isShowing()) {
            mShareCustomDialog.showDialog();
            ShareReqParams params = new ShareReqParams();
            params.summary = data.getPost_content().getContent();
            params.isEditable = false;
            params.imageUrl = "http://shop.jzbwlkj.com/logo.png";
            params.title = data.getPost_content().getContent();
            params.shareUrl = "http://shop.jzbwlkj.com/logo.png";
            params.appName = getString(com.android.mall.resource.R.string.app_name);
            params.type = "4";
            mShareCustomDialog.setShareParams(params);
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

    /**
     * 添加评论 回复评论
     */
    public void addComment() {
        if (type.equals("1")) {
            CircleService.comment comment = new CircleService.comment();
            comment.content = input;
            comment.post_id = ID;
            getHttpDataLoader().doPostProcess(comment, CommonReturn.class);
        } else if (type.equals("2")) {
            CircleService.reply reply = new CircleService.reply();
            reply.content = input;
            reply.com_id = ID;
            getHttpDataLoader().doPostProcess(reply, CommonReturn.class);
        }
    }

    /**
     * 点赞
     */
    public void zan(String id, String type) {
        CircleService.zan zan = new CircleService.zan();
        zan.id = id;
        zan.type = type;
        getHttpDataLoader().doPostProcess(zan, ZanBean.class);
    }

    @Override
    public void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }

    @Override
    public void onRefresh() {
        loadDynamicDeail();
    }
}
