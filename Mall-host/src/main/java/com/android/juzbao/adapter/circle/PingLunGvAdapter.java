package com.android.juzbao.adapter.circle;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.juzbao.model.circle.DynamicDetailBean;
import com.android.quna.activity.R;

import java.util.List;

/**
 * Created by admin on 2017/3/21.
 */

public class PingLunGvAdapter extends MyBaseAdapter{

    public PingLunGvAdapter(Context context, List mList) {
        super(context, mList);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if(view == null){
            view = mInflater.inflate(R.layout.item_pinglun_gv,viewGroup,false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) view.getTag();
        }

        DynamicDetailBean.DataBean.CommentBean.ReplyBean bean = (DynamicDetailBean.DataBean.CommentBean.ReplyBean) mList.get(i);
        viewHolder.name.setText(bean.getNickname()+"：");
        viewHolder.pl.setText(bean.getContent());

        return view;
    }

    class ViewHolder{
        TextView name,pl;
        public ViewHolder( View view) {
            name = (TextView) view.findViewById(R.id.tv_pinglun_gv_name);
            pl = (TextView) view.findViewById(R.id.tv_pinglun_gv_pl);
        }
    }

}
