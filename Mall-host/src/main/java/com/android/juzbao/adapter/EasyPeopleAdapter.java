package com.android.juzbao.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.juzbao.activity.jifen.EasyPeopleDetailActivity_;
import com.android.quna.activity.R;
import com.android.zcomponent.adapter.CommonAdapter;
import com.android.zcomponent.http.api.host.Endpoint;
import com.android.zcomponent.json.JsonSerializerFactory;
import com.server.api.model.jifenmodel.EasyPeople;

import java.util.List;


/**
 * Created by Koterwong on 2016/7/29.
 * 便民信息的Adapter
 */
public class EasyPeopleAdapter extends CommonAdapter {

    public EasyPeopleAdapter(Context context, List<?> list) {
        super(context, list);
    }

    @Override public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.adapter_easy_perple, null);
        }
        ImageView easyImgvew = findViewById(convertView, R.id.imgvew_easy_people);
        final EasyPeople.Data item = (EasyPeople.Data) mList.get(position);
        mImageLoader.displayImage(Endpoint.HOST + item.thumb, easyImgvew, options);
        //跳转到便民信息详情的Activity。
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                String info = JsonSerializerFactory.Create().encode(item);
                Bundle bundle = new Bundle();
                bundle.putString("info", info);
                intentToActivity(bundle, EasyPeopleDetailActivity_.class);
            }
        });
        return convertView;
    }
}
