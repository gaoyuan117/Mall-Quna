
package com.android.juzbao.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.server.api.model.AuctionLogItem;
import com.android.mall.resource.R;
import com.android.zcomponent.adapter.CommonAdapter;

/**
 * <p>
 * Description: 拍品出价记录
 * </p>
 * 
 * @ClassName:AuctionLogAdapter
 * @author: wei
 * @date: 2015-11-10
 * 
 */
public class AuctionLogAdapter extends CommonAdapter
{

	public AuctionLogAdapter(Context context, List<?> list)
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
							.inflate(R.layout.adapter_auction_log, null);
		}
		
		TextView tvewName =
				findViewById(convertView, R.id.tvew_name_show);
		TextView tvewTime =
				findViewById(convertView, R.id.tvew_time_show);
		TextView tvewPrice =
				findViewById(convertView, R.id.tvew_price_show);

		final AuctionLogItem product = (AuctionLogItem) mList.get(position);

		tvewName.setText(product.nickname);
		tvewTime.setText(product.format_time);
		tvewPrice.setText("¥" + product.price);

		
		return convertView;
	}
}