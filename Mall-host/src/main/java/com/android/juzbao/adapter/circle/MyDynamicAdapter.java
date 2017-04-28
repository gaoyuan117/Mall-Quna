package com.android.juzbao.adapter.circle;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.android.juzbao.activity.circle.DynamicDetailActivity;
import com.android.juzbao.model.circle.MyDynamicBean;
import com.android.quna.activity.R;
import com.android.zcomponent.http.api.host.Endpoint;
import com.android.zcomponent.views.CircleImageView;

import java.util.ArrayList;
import java.util.List;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * Created by admin on 2017/3/24.
 */

public class MyDynamicAdapter extends MyBaseAdapter {
    private String t;
    private List<Integer> list;
    public static int current = -1;

    public MyDynamicAdapter(Context context, List mList) {
        super(context, mList);
        list = new ArrayList<>();
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        MyDynamicBean.DataBean.ListBean bean0 = null;
        ViewHolder myViewHolder;
        if (view == null) {
            view = mInflater.inflate(R.layout.item_my_dynamic, viewGroup, false);
            myViewHolder = new ViewHolder(view);
            view.setTag(myViewHolder);
        } else {
            myViewHolder = (ViewHolder) view.getTag();
        }
        final MyDynamicBean.DataBean.ListBean bean = (MyDynamicBean.DataBean.ListBean) mList.get(i);

        if (i == 0) {
            String up_time = bean.getUp_time();
            if (up_time.startsWith("2017-")) {
                String replace = up_time.replace("2017-", "");
                myViewHolder.time.setText(replace);
            } else {
                myViewHolder.time.setText(up_time);
            }
            myViewHolder.time.setVisibility(View.VISIBLE);
        } else {
            if (i > 0) {
                bean0 = (MyDynamicBean.DataBean.ListBean) mList.get(i - 1);
            }

            if (bean0 != null) {
                if (bean.getUp_time().equals(bean0.getUp_time())) {
                    myViewHolder.time.setVisibility(View.INVISIBLE);
                } else {
                    myViewHolder.time.setVisibility(View.VISIBLE);
                    String time1 = bean.getUp_time();
                    if (time1.startsWith("2017-")) {
                        String replace = time1.replace("2017-", "");
                        myViewHolder.time.setText(replace);
                    } else {
                        myViewHolder.time.setText(bean.getUp_time());
                    }
                }
            } else {
                String time1 = bean.getUp_time();
                if (time1.startsWith("2017-")) {
                    String replace = time1.replace("2017-", "");
                    myViewHolder.time.setText(replace);
                } else {
                    myViewHolder.time.setText(bean.getUp_time());
                }
            }
        }


        myViewHolder.content.setText(bean.getContent());

        if (bean.getIs_img() == 1) {//图片
            myViewHolder.jcRlLayout.setVisibility(View.GONE);
            myViewHolder.gridView.setVisibility(View.VISIBLE);
            DynamicGvAdaper gvAdaper = new DynamicGvAdaper(context, bean.getImage_path());
            myViewHolder.gridView.setAdapter(gvAdaper);
        } else if (bean.getIs_img() == 2) {//视频
            myViewHolder.gridView.setVisibility(View.GONE);
            myViewHolder.jcRlLayout.setVisibility(View.VISIBLE);
            if (bean.getContent().length() > 8) {
                myViewHolder.videoView.setUp(bean.getMovie_path(), bean.getContent().substring(0, 8));
            } else {
                myViewHolder.videoView.setUp(bean.getMovie_path(), bean.getContent());
            }
            myViewHolder.imageView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    Log.e("gy", "当前位置：" + i);
                    current = i;
                    return false;
                }
            });
        } else {//纯文字
            myViewHolder.gridView.setVisibility(View.GONE);
            myViewHolder.jcRlLayout.setVisibility(View.GONE);
        }

        myViewHolder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DynamicDetailActivity.class);
                intent.putExtra("id", bean.getId());
                context.startActivity(intent);
            }
        });

        return view;
    }


    class ViewHolder {
        CircleImageView circleImageView;
        TextView time, content;
        GridView gridView;
        RelativeLayout relativeLayout,jcRlLayout;
        JCVideoPlayerStandard videoView;
        ImageView imageView;

        public ViewHolder(View view) {
            time = (TextView) view.findViewById(R.id.tv_my_dynamic_time);
            content = (TextView) view.findViewById(R.id.tv_my_dynamic_content);
            gridView = (GridView) view.findViewById(R.id.gv_my_dynamic);
            relativeLayout = (RelativeLayout) view.findViewById(R.id.rl_my_dynamic_lv);
            videoView = (JCVideoPlayerStandard) view.findViewById(R.id.video_view_my);
            imageView = (ImageView) view.findViewById(R.id.imgg);
            jcRlLayout = (RelativeLayout) view.findViewById(R.id.rl_my_dynamic);
        }
    }
}
