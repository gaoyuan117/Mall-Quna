
package com.android.juzbao.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.juzbao.model.ProductBusiness;
import com.android.mall.resource.R;
import com.android.zcomponent.adapter.CommonAdapter;
import com.android.zcomponent.http.api.host.Endpoint;
import com.android.zcomponent.util.StringUtil;
import com.server.api.model.ProductItem;

import java.util.List;

/**
 * <p>
 * Description: 公共商品列表项
 * </p>
 *
 * @ClassName:ProductAdapter
 * @author: wei
 * @date: 2015-11-10
 */
public class ProductAdapter extends CommonAdapter {

    public ProductAdapter(Context context, List<?> list) {
        super(context, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            convertView =
                    layoutInflater.inflate(R.layout.adapter_product, null);
        }

        ImageView imgvewPhoto =
                findViewById(convertView, R.id.imgvew_photo_show);
        TextView tvewProductName =
                findViewById(convertView, R.id.tvew_product_name_show);
        TextView tvewProductPrice =
                findViewById(convertView, R.id.tvew_product_price_show);
        TextView tvewProductSales =
                findViewById(convertView, R.id.tvew_product_sales_show);
        TextView tvewProductNowPrice =
                findViewById(convertView, R.id.tvew_time_title_show);

        final ProductItem product = (ProductItem) mList.get(position);
        tvewProductPrice.setVisibility(View.GONE);
        tvewProductName.setText(product.title);
        tvewProductNowPrice.setText("¥" + StringUtil.formatProgress(product.price, 2));

        if (TextUtils.isEmpty(product.sales)) {
            tvewProductSales.setVisibility(View.GONE);

            if (TextUtils.isEmpty(product.view)) {
                tvewProductSales.setVisibility(View.GONE);
            } else {
                tvewProductSales.setText("浏览量:" + product.view + "次");
                tvewProductSales.setVisibility(View.VISIBLE);
            }
        } else {
            tvewProductSales.setText(product.sales + "付款");
            tvewProductSales.setVisibility(View.VISIBLE);
        }

        if (ProductBusiness.validateImageUrl(product.image)) {
            mImageLoader.displayImage(Endpoint.HOST + product.image,
                    imgvewPhoto, options);
        } else if (ProductBusiness.validateImageUrl(product.image_path)) {
            mImageLoader.displayImage(Endpoint.HOST + product.image_path,
                    imgvewPhoto, options);
        }

        convertView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                ProductBusiness.intentToProductDetailActivity(mContext,
                        product, Integer.parseInt(product.id));
            }
        });
        return convertView;
    }
}