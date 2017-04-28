package com.android.juzbao.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.juzbao.model.ProductBusiness;
import com.android.zcomponent.adapter.CommonAdapter;
import com.android.zcomponent.http.api.host.Endpoint;
import com.android.zcomponent.util.StringUtil;
import com.server.api.model.AdProduct;

import java.util.List;

public class IndexRecomAdapter extends CommonAdapter {

    public IndexRecomAdapter(Context context, List<?> list) {
        super(context, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            convertView = layoutInflater.inflate(com.android.mall.resource.R.layout.adapter_product, null);
        }
        ImageView imgvewPhoto =
                findViewById(convertView, com.android.mall.resource.R.id.imgvew_photo_show);
        TextView tvewProductName =
                findViewById(convertView, com.android.mall.resource.R.id.tvew_product_name_show);
        TextView tvewProductPrice =
                findViewById(convertView, com.android.mall.resource.R.id.tvew_product_price_show);
        TextView tvewProductSales =
                findViewById(convertView, com.android.mall.resource.R.id.tvew_product_sales_show);
        TextView tvewProductNowPrice =
                findViewById(convertView, com.android.mall.resource.R.id.tvew_time_title_show);

        final AdProduct.AdItem adItem = (AdProduct.AdItem) mList.get(position);
        tvewProductPrice.setVisibility(View.GONE);
        tvewProductName.setText(adItem.title);
        tvewProductNowPrice.setText("¥" + StringUtil.formatProgress(adItem.product.price, 2));
        if (null != adItem.product.view) {
            tvewProductSales.setText("浏览：" + adItem.product.view);
        } else {
            tvewProductSales.setVisibility(View.GONE);
        }

        if (ProductBusiness.validateImageUrl(adItem.image)) {
            mImageLoader.displayImage(Endpoint.HOST + adItem.image, imgvewPhoto, options);
        }
        /**
         *  -> 商品详情
         */
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductBusiness.intentToProductDetailActivity(mContext, null, Integer.parseInt(adItem.product_id));
            }
        });
        return convertView;
    }

}
