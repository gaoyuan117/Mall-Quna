package com.android.juzbao.adapter.circle;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.juzbao.activity.circle.DynamicDetailActivity;
import com.android.juzbao.model.circle.DynamicDetailBean;
import com.android.juzbao.utils.Util;
import com.android.juzbao.view.MyGridView;
import com.android.quna.activity.R;
import com.android.zcomponent.http.api.host.Endpoint;
import com.android.zcomponent.views.CircleImageView;
import com.bumptech.glide.Glide;
import com.server.api.model.Image;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/3/21.
 */

public class PingLunAdapter extends MyBaseAdapter {
    public static int length;
    private DynamicDetailActivity activity;
    private EditText editText;

    public PingLunAdapter(Context context, List mList, DynamicDetailActivity activity, EditText editText) {
        super(context, mList);
        this.activity = activity;
        this.editText = editText;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        PingLunHolder pingLunHolder;
        if (view == null) {
            view = mInflater.inflate(R.layout.item_pinglun, viewGroup, false);
            pingLunHolder = new PingLunHolder(view);
            view.setTag(pingLunHolder);
        } else {
            pingLunHolder = (PingLunHolder) view.getTag();
        }
        final DynamicDetailBean.DataBean.CommentBean bean = (DynamicDetailBean.DataBean.CommentBean) mList.get(i);

        Glide.with(context).load(Endpoint.IMAGE + bean.getAvatar_img()).error(R.drawable.ease_default_avatar)
                .placeholder(R.drawable.ease_default_avatar).into(pingLunHolder.circleImageView);
        pingLunHolder.name.setText(bean.getNickname());
        pingLunHolder.content.setText(bean.getContent());
        pingLunHolder.zanCount.setText(bean.getThumbs_up_cnt());
        pingLunHolder.time.setText(bean.getUp_time());
        pingLunHolder.plCount.setText(bean.getReply_cnt());
        //判断有没有点赞过 切换图片
        if (bean.getIs_thumbs_up() == 1) {
            pingLunHolder.imageView.setImageResource(R.drawable.yizan);
            pingLunHolder.zanCount.setTextColor(context.getResources().getColor(R.color.zan));
        } else {
            pingLunHolder.imageView.setImageResource(R.drawable.zan2);
            pingLunHolder.zanCount.setTextColor(context.getResources().getColor(R.color.black));
        }

//        PingLunGvAdapter pingLunGvAdapter = new PingLunGvAdapter(context, bean.getReply());
//        pingLunHolder.myGridView.setAdapter(pingLunGvAdapter);
        pingLunHolder.llReply.removeAllViews();
        if (bean.getReply() != null) {
            for (int i1 = 0; i1 < bean.getReply().size(); i1++) {
                View repltView = View.inflate(context, R.layout.item_pinglun_gv, null);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
                TextView name = (TextView) repltView.findViewById(R.id.tv_pinglun_gv_name);
                TextView pl = (TextView) repltView.findViewById(R.id.tv_pinglun_gv_pl);
                name.setText(bean.getReply().get(i1).getNickname() + ":");
                pl.setText(bean.getReply().get(i1).getContent());
                pingLunHolder.llReply.addView(repltView,params);
            }
        }
        pingLunHolder.ll1.setOnClickListener(new View.OnClickListener() {//点赞
            @Override
            public void onClick(View view) {
                activity.zan(bean.getId(), "2");
            }
        });

        pingLunHolder.ll2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.toggleSoftKeyboardState(context);
                activity.type = "2";
                activity.ID = bean.getId();
                editText.setHint("回复 "+bean.getNickname());
            }
        });

        return view;
    }

    class PingLunHolder {
        CircleImageView circleImageView;
        TextView name, content, zanCount, plCount, time;
        MyGridView myGridView;
        LinearLayout ll1, ll2, llReply;
        ImageView imageView;

        public PingLunHolder(View view) {
            circleImageView = (CircleImageView) view.findViewById(R.id.img_pinglun_avatar);
            name = (TextView) view.findViewById(R.id.tv_pinglun_name);
            content = (TextView) view.findViewById(R.id.tv_pinglun_content);
            zanCount = (TextView) view.findViewById(R.id.tv_pinglun_zan);
            plCount = (TextView) view.findViewById(R.id.tv_pinglun_pl);
            time = (TextView) view.findViewById(R.id.tv_pinglun_time);
            myGridView = (MyGridView) view.findViewById(R.id.gv_pinglun);
            ll1 = (LinearLayout) view.findViewById(R.id.ll_pinglun_zan);
            ll2 = (LinearLayout) view.findViewById(R.id.ll_pinglun_reply);
            llReply = (LinearLayout) view.findViewById(R.id.ll_dynamic_detail_reply);
            imageView = (ImageView) view.findViewById(R.id.img_pinglun_zan);
        }
    }


}
