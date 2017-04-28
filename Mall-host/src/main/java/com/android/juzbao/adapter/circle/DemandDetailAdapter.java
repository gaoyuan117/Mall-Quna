package com.android.juzbao.adapter.circle;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.juzbao.model.circle.DemandDetailBean;
import com.android.juzbao.view.CircleImageView;
import com.android.quna.activity.R;
import com.android.zcomponent.http.api.host.Endpoint;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by admin on 2017/3/25.
 */

public class DemandDetailAdapter extends MyBaseAdapter {

    public DemandDetailAdapter(Context context, List mList) {
        super(context, mList);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        DetailHolder detailHolder;
        if (view == null) {
            view = mInflater.inflate(R.layout.item_demand_detail, viewGroup, false);
            detailHolder = new DetailHolder(view);
            view.setTag(detailHolder);
        } else {
            detailHolder = (DetailHolder) view.getTag();
        }
        DemandDetailBean.DataBean.YingListBean bean = (DemandDetailBean.DataBean.YingListBean) mList.get(i);
        detailHolder.des.setText(bean.getContent());
        if (bean.getIs_name() == 1) {
            detailHolder.name.setText(bean.getNickname());
            Glide.with(context).load(Endpoint.IMAGE + bean.getAvatar_img()).error(R.drawable.ease_default_avatar)
                    .placeholder(R.drawable.ease_default_avatar).into(detailHolder.avatar);
        } else {
            detailHolder.name.setText("匿名用户");
            detailHolder.avatar.setImageResource(R.drawable.ease_default_avatar);
        }
        if(bean.getStatus().equals("0")){
            detailHolder.status.setVisibility(View.GONE);
        }else if(bean.getStatus().equals("1")){
            detailHolder.status.setVisibility(View.VISIBLE);
            detailHolder.status.setText("已同意");
            detailHolder.status.setTextColor(context.getResources().getColor(R.color.orange2));
        }else if(bean.getStatus().equals("2")){
            detailHolder.status.setVisibility(View.VISIBLE);
            detailHolder.status.setText("已拒绝");
            detailHolder.status.setTextColor(context.getResources().getColor(R.color.red));
        }

        return view;
    }

    class DetailHolder {
        CircleImageView avatar;
        TextView name, des,status;

        public DetailHolder(View view) {
            avatar = (CircleImageView) view.findViewById(R.id.img_demand_detail_gv_avatar);
            name = (TextView) view.findViewById(R.id.tv_demand_detail_gv_name);
            des = (TextView) view.findViewById(R.id.tv_demand_detail_gv_des);
            status = (TextView) view.findViewById(R.id.tv_demand_detail_status);
        }
    }
}
