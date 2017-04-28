
package com.android.juzbao.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.zcomponent.http.api.host.Endpoint;
import com.server.api.model.ProductItem;
import com.android.mall.resource.R;
import com.android.juzbao.model.ProductBusiness;
import com.android.zcomponent.adapter.CommonAdapter;

/**
 * <p>
 * Description: 选礼物列表项
 * </p>
 * 
 * @ClassName:SelectGiftAdapter
 * @author: wei
 * @date: 2015-11-10
 * 
 */
public class SelectGiftAdapter extends CommonAdapter
{

	public SelectGiftAdapter(Context context, List<?> list)
	{
		super(context, list);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		if (null == convertView)
		{
			convertView =
					layoutInflater.inflate(R.layout.adapter_select_gift, null);
		}

		ImageView imgvewPhoto =
				findViewById(convertView, R.id.imgvew_photo_show);
		TextView tvewProductName =
				findViewById(convertView, R.id.tvew_product_name_show);
		TextView tvewProductPrice =
				findViewById(convertView, R.id.tvew_product_price_show);
		TextView tvewProductNowPrice =
				findViewById(convertView, R.id.tvew_time_title_show);

		final ProductItem product = (ProductItem) mList.get(position);
		tvewProductPrice.setVisibility(View.GONE);
		tvewProductName.setText(product.title);
		tvewProductNowPrice.setText("¥" + product.price);

		if (ProductBusiness.validateImageUrl(product.image))
		{
			mImageLoader.displayImage(Endpoint.HOST + product.image,
					imgvewPhoto, options);
		}

		convertView.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				ProductBusiness.intentToProductDetailActivity(mContext,
						product, Integer.parseInt(product.id));
			}
		});
		return convertView;
	}
}