package com.android.juzbao.adapter.circle;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.android.juzbao.utils.GlideUtil;
import com.android.juzbao.view.RoundImageView;
import com.android.quna.activity.R;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by admin on 2017/3/21.
 */

public class FaTieAdapter extends MyBaseAdapter{

    public FaTieAdapter(Context context, List mList) {
        super(context, mList);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Viewholder vh;
        if(view == null){
            view = mInflater.inflate(R.layout.item_dynamic_gv,viewGroup,false);
            vh = new Viewholder(view);
            view.setTag(vh);
        }else {
            vh = (Viewholder) view.getTag();
        }
        String path = (String) mList.get(i);
        Glide.with(context).load(path).error(R.drawable.tianjia).into(vh.imageView);
        return view;
    }

    class Viewholder{
        RoundImageView imageView;

        public Viewholder(View view) {
            imageView = (RoundImageView) view.findViewById(R.id.img_dynamic_gv);
        }
    }
}
