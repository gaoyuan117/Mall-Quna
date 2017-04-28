package com.android.juzbao.adapter.circle;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.juzbao.activity.circle.DynamicDetailActivity;
import com.android.juzbao.activity.circle.MyDynamicActivity;
import com.android.juzbao.model.circle.DynamicBean;
import com.android.quna.activity.R;
import com.android.zcomponent.http.api.host.Endpoint;
import com.android.zcomponent.views.CircleImageView;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import fm.jiecao.jcvideoplayer_lib.JCMediaManager;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;


/**
 * Created by admin on 2017/3/17.
 */

public class DynamicAdapter extends MyBaseAdapter {
    private List<ImageView> mImgs;
    public static int current = -1;

    public DynamicAdapter(Context context, List mList) {
        super(context, mList);
        mImgs = new ArrayList<>();
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final ViewHolder viewHolder;
        if (view == null) {
            view = mInflater.inflate(R.layout.item_dynamic, viewGroup, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        final DynamicBean.DataBean bean = (DynamicBean.DataBean) mList.get(i);
        viewHolder.name.setText(bean.getNickname());
        viewHolder.content.setText(bean.getContent());

        Glide.with(context).load(Endpoint.IMAGE + bean.getAvatat_img()).error(R.drawable.ease_default_avatar)
                .into(viewHolder.circleImageView);

        if (bean.getIs_img() == 1) {//图片
            viewHolder.jcRlLayout.setVisibility(View.GONE);
            viewHolder.gridView.setVisibility(View.VISIBLE);
            DynamicGvAdaper gvAdaper = new DynamicGvAdaper(context, bean.getImage_path());
            viewHolder.gridView.setAdapter(gvAdaper);
        } else if (bean.getIs_img() == 2) {//视频
            viewHolder.gridView.setVisibility(View.GONE);
            viewHolder.jcRlLayout.setVisibility(View.VISIBLE);
            if (bean.getContent().length() > 8) {
                viewHolder.player.setUp(bean.getMovie_path(), bean.getContent().substring(0, 8));
            } else {
                viewHolder.player.setUp(bean.getMovie_path(), bean.getContent());
            }
            viewHolder.startImg.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    Log.e("gy", "当前位置：" + i);
                    current = i;
                    return false;
                }
            });
            Log.e("gy", bean.getMovie_path());
        } else {//纯文字
            viewHolder.gridView.setVisibility(View.GONE);
            viewHolder.jcRlLayout.setVisibility(View.GONE);
        }

        viewHolder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DynamicDetailActivity.class);
                intent.putExtra("id", bean.getId());
                context.startActivity(intent);
            }
        });

        viewHolder.circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MyDynamicActivity.class);
                intent.putExtra("uid", bean.getUid());
                context.startActivity(intent);
            }
        });
        return view;
    }

    class ViewHolder {
        CircleImageView circleImageView;
        TextView name, content;
        GridView gridView;
        RelativeLayout relativeLayout,jcRlLayout;
        JCVideoPlayerStandard player;
        ImageView startImg;

        public ViewHolder(View view) {
            circleImageView = (CircleImageView) view.findViewById(R.id.img_dynamic_avatar);
            name = (TextView) view.findViewById(R.id.tv_dynamic_name);
            content = (TextView) view.findViewById(R.id.tv_dynamic_content);
            gridView = (GridView) view.findViewById(R.id.gv_dynamic);
            relativeLayout = (RelativeLayout) view.findViewById(R.id.rl_dynamic_lv);
//            videoView = (VideoView) view.findViewById(R.id.video_view);
            player = (JCVideoPlayerStandard) view.findViewById(R.id.videocontroller1);
            startImg = (ImageView) view.findViewById(R.id.imggg);
            jcRlLayout = (RelativeLayout) view.findViewById(R.id.rl_dynamic);
        }
    }

}
