package com.android.juzbao.adapter.circle;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.juzbao.model.circle.ConcernBean;
import com.android.juzbao.utils.GlideUtil;
import com.android.quna.activity.R;
import com.android.zcomponent.http.api.host.Endpoint;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by admin on 2017/3/18.
 */

public class ConcernAdapter extends MyBaseAdapter{

    public ConcernAdapter(Context context, List mList) {
        super(context, mList);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if(view==null){
            view = mInflater.inflate(R.layout.item_concern_lv,viewGroup,false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) view.getTag();
        }
        ConcernBean.DataBean bean = (ConcernBean.DataBean) mList.get(i);
        viewHolder.name.setText(bean.getNickname());
        viewHolder.des.setText(bean.getContent());
        Glide.with(context).load(Endpoint.IMAGE + bean.getAvatar_img()).error(R.drawable.ease_default_avatar).into(viewHolder.avatar);
        return view;
    }

    class ViewHolder{
        ImageView avatar;
        TextView name,des;
        public ViewHolder(View view) {
            avatar = (ImageView) view.findViewById(R.id.img_concern_avatar);
            name = (TextView) view.findViewById(R.id.tv_concern_name);
            des = (TextView) view.findViewById(R.id.tv_concern_des);
        }
    }
}
