package com.android.juzbao.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.juzbao.activity.ProductDetailActivity_;
import com.android.juzbao.activity.jifen.JifenConvertActivity_;
import com.android.juzbao.application.BaseApplication;
import com.android.quna.activity.R;
import com.android.zcomponent.adapter.CommonAdapter;
import com.android.zcomponent.http.api.host.Endpoint;
import com.android.zcomponent.json.JsonSerializerFactory;
import com.server.api.model.ProductItem;

import java.util.List;


public class JiFenAdapter extends CommonAdapter {

  public JiFenAdapter(Context context, List<?> list) {
    super(context, list);
  }

  @Override public View getView(int position, View convertView, ViewGroup parent) {

    if (convertView == null) {
      convertView = layoutInflater.inflate(R.layout.adapter_jifen, null);
    }
    ImageView imgvew_jifen_img = findViewById(convertView, R.id.imgvew_jifen_img);
    TextView tvew_jifen_title = findViewById(convertView, R.id.tvew_jifen_title);
    TextView tvew_jifen_number = findViewById(convertView, R.id.tvew_jifen_number);
    TextView tvew_jifen_count = findViewById(convertView, R.id.tvew_jifen_count);
    Button btn_jifen_convert_click = findViewById(convertView, R.id.btn_jifen_convert_click);

    final ProductItem item = (ProductItem) mList.get(position);

    mImageLoader.displayImage(Endpoint.HOST + item.image, imgvew_jifen_img);
    tvew_jifen_title.setText(item.title);
    tvew_jifen_number.setText("积分：" + item.use_score);
    tvew_jifen_count.setText("剩余：" + item.quantity);
    btn_jifen_convert_click.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (!BaseApplication.isLogin()) {
          BaseApplication.intentToLoginActivity((Activity) mContext);
          return;
        }
        Bundle bundle = new Bundle();
        String product = JsonSerializerFactory.Create().encode(item);
        bundle.putInt("id", Integer.valueOf(item.id));
        bundle.putString("product", product);
        intentToActivity(bundle, JifenConvertActivity_.class);
      }
    });

    convertView.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        Bundle bundle = new Bundle();
        bundle.putInt("id", Integer.parseInt(item.id));
        bundle.putBoolean("jifen", true);
        intentToActivity(bundle, ProductDetailActivity_.class);
      }
    });

    return convertView;
  }
}
