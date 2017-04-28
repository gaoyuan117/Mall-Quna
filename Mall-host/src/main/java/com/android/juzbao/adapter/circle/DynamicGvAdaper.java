package com.android.juzbao.adapter.circle;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.juzbao.activity.circle.ImagePagerActivity;
import com.android.juzbao.utils.GlideUtil;
import com.android.juzbao.view.RoundImageView;
import com.android.quna.activity.R;

import java.util.List;

/**
 * Created by admin on 2017/3/17.
 */

public class DynamicGvAdaper extends MyBaseAdapter{

    public DynamicGvAdaper(Context context, List mList) {
        super(context, mList);
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        Viewholder vh;
        if(view == null){
            view = mInflater.inflate(R.layout.item_dynamic_gv,viewGroup,false);
            vh = new Viewholder(view);
            view.setTag(vh);
        }else {
            vh = (Viewholder) view.getTag();
        }
        String path = (String) mList.get(i);
        GlideUtil.glide(path,vh.imageView);

        vh.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageBrower(i,mList);
            }
        });
        return view;
    }

    class Viewholder{
        RoundImageView imageView;

        public Viewholder(View view) {
            imageView = (RoundImageView) view.findViewById(R.id.img_dynamic_gv);
        }
    }

    /**
     * 这里跳转到图片的预览界面
     *
     * @param position
     * @param urls
     */
    private void imageBrower(int position, List<String> urls) {
        Intent intent = new Intent(context, ImagePagerActivity.class);
        // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
        int size = urls.size();
        //携带点击位置，和路径的集合进行跳转
        String[] arr = urls.toArray(new String[size]);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, arr);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
        intent.putExtra("type", "net");
        context.startActivity(intent);
    }
}
