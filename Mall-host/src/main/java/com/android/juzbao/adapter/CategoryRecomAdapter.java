package com.android.juzbao.adapter;


import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.juzbao.model.ProductBusiness;
import com.android.zcomponent.http.api.host.Endpoint;
import com.android.zcomponent.util.ImageLoaderUtil;
import com.android.zcomponent.util.StringUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.server.api.model.AdProduct;

import java.util.ArrayList;
import java.util.List;

public class CategoryRecomAdapter extends BaseAdapter {

    protected ImageLoader mImageLoader;
    protected DisplayImageOptions options;

    private Context mContext;
    private List<AdProduct.AdItem> mList;
    private LayoutInflater mInflater;

    public CategoryRecomAdapter(Context mContext, List<AdProduct.AdItem> list) {
        this.mContext = mContext;
        mList = new ArrayList<>();
        mList.addAll(list);
        mInflater = LayoutInflater.from(mContext);
        mImageLoader = ImageLoader.getInstance();
        options = ImageLoaderUtil.getDisplayImageOptions(com.android.component.zframework.R.drawable.img_empty_logo_small);
    }

    public void setData(List<AdProduct.AdItem> list) {
        if (mList == null) {
            mList = new ArrayList<>();
        }
        if (mList.size() > 0) {
            mList.clear();
        }
        this.mList.addAll(list);
        this.notifyDataSetChanged();
    }

    public void addData(List<AdProduct.AdItem> list) {
        if (mList != null) {
            mList.addAll(list);
        } else {
            mList = new ArrayList<>();
            mList.addAll(list);
        }
        this.notifyDataSetChanged();
    }

    public void clear() {
        if (mList != null && mList.size() > 0) {
            mList.clear();
            this.notifyDataSetChanged();
        }
    }

    @Override public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override public Object getItem(int position) {
        return mList.get(position);
    }

    @Override public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            convertView = mInflater.inflate(com.android.mall.resource.R.layout.adapter_category_recom, null);
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
            tvewProductSales.setText("浏览量：" + adItem.product.view);
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

    public <T extends View> T findViewById(View view, int id) {

        SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();

        if (viewHolder == null) {
            viewHolder = new SparseArray<View>();
            view.setTag(viewHolder);
        }
        View childView = viewHolder.get(id);
        if (childView == null) {
            childView = view.findViewById(id);
            viewHolder.put(id, childView);
        }
        return (T) childView;
    }
}
