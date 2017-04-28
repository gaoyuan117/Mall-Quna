package com.android.juzbao.adapter.circle;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.juzbao.model.circle.AllDemandBean;
import com.android.quna.activity.R;
import com.android.zcomponent.http.api.host.Endpoint;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by admin on 2017/3/18.
 */

public class InviteAdapter extends MyBaseAdapter {

    public InviteAdapter(Context context, List mList) {
        super(context, mList);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        InviteHolder inviteHolder;
        if (view == null) {
            view = mInflater.inflate(R.layout.item_invite_lv, viewGroup, false);
            inviteHolder = new InviteHolder(view);
            view.setTag(inviteHolder);
        } else {
            inviteHolder = (InviteHolder) view.getTag();
        }
        AllDemandBean.DataBean bean = (AllDemandBean.DataBean) mList.get(i);
        inviteHolder.name.setText(bean.getNickname());
        inviteHolder.content.setText("需求详情：" + bean.getDescription());
        inviteHolder.mongey.setText(bean.getMoney());
        inviteHolder.count.setText(bean.getNumber()+"人");
        inviteHolder.inviter.setText(bean.getYing_cnt()+"人已应邀");
        inviteHolder.need.setText(bean.getDemand());

        Glide.with(context).load(Endpoint.IMAGE + bean.getAvatar_img()).error(R.drawable.ease_default_avatar).into(inviteHolder.avatar);
        return view;
    }

    class InviteHolder {
        ImageView avatar;
        TextView need, count, mongey, content, name, inviter;

        public InviteHolder(View view) {
            avatar = (ImageView) view.findViewById(R.id.img_invite_avatar);
            need = (TextView) view.findViewById(R.id.tv_invite_need);
            count = (TextView) view.findViewById(R.id.tv_invite_count);
            mongey = (TextView) view.findViewById(R.id.tv_invite_money);
            content = (TextView) view.findViewById(R.id.tv_invite_content);
            name = (TextView) view.findViewById(R.id.tv_invite_name);
            inviter = (TextView) view.findViewById(R.id.tv_invite_inviter);
        }
    }
}
