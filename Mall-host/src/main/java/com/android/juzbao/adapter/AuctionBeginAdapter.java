
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
 * Description: 拍真宝 - 即将开始、马上结束、0元起拍列表项
 * </p>
 * 
 * @ClassName:AuctionBeginAdapter
 * @author: wei
 * @date: 2015-11-10
 * 
 */
public class AuctionBeginAdapter extends CommonAdapter
{

	public AuctionBeginAdapter(Context context, List<?> list)
	{
		super(context, list);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		if (null == convertView)
		{
			convertView =
					layoutInflater
							.inflate(R.layout.adapter_auction_begin, null);
		}

		ImageView imgvewPhoto =
				findViewById(convertView, R.id.imgvew_photo_show);
		TextView tvewProductName =
				findViewById(convertView, R.id.tvew_product_name_show);
		TextView tvewProductNowPrice =
				findViewById(convertView, R.id.tvew_product_now_price_show);
		TextView tvewProductStatus =
				findViewById(convertView, R.id.tvew_product_status_show);

		final ProductItem product = (ProductItem) mList.get(position);

		tvewProductName.setText(product.title);
		
		tvewProductNowPrice.setText("¥" + product.maxprice);

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
				ProductBusiness.intentToPaipinProductDetailActivity(mContext,
						product, Integer.parseInt(product.id));
			}
		});
		return convertView;
	}
}