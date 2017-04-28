
package com.android.juzbao.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.server.api.model.ShippingInfoItem;
import com.android.mall.resource.R;
import com.android.zcomponent.adapter.CommonAdapter;
import com.android.zcomponent.util.MyLayoutAdapter;

/**
 * <p>
 * Description: 物流详情列表项
 * </p>
 * 
 * @ClassName:ExpressStatusAdapter
 * @author: wei
 * @date: 2015-11-10
 * 
 */
public class ExpressStatusAdapter extends CommonAdapter
{

	public ExpressStatusAdapter(Context context, List<?> list)
	{
		super(context, list);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		if (null == convertView)
		{
			convertView =
					layoutInflater.inflate(R.layout.adapter_express_status,
							null);
		}

		ImageView tvewExpressStatusMark =
				findViewById(convertView, R.id.tvew_express_status_mark_show);
		TextView tvewExpressLine =
				findViewById(convertView, R.id.tvew_express_line_show);
		TextView tvewExpressName =
				findViewById(convertView, R.id.tvew_express_name_show);
		TextView tvewExpressTime =
				findViewById(convertView, R.id.tvew_express_time_show);

		ShippingInfoItem item = (ShippingInfoItem) mList.get(position);

		tvewExpressName.setText(item.content);
		tvewExpressTime.setText(item.time);

		if (position == 0)
		{
			tvewExpressName.setTextColor(Color.rgb(0x23, 0xae, 0x5d));
			tvewExpressTime.setTextColor(Color.rgb(0x23, 0xae, 0x5d));
			tvewExpressStatusMark
					.setBackgroundResource(R.drawable.express_status_new);

			LayoutParams params =
					(LayoutParams) tvewExpressLine.getLayoutParams();
			params.topMargin =
					(int) (MyLayoutAdapter.getInstance().getDensityRatio() * 20);
			tvewExpressLine.setLayoutParams(params);
		}
		else
		{
			LayoutParams params =
					(LayoutParams) tvewExpressLine.getLayoutParams();
			params.topMargin = 0;
			tvewExpressLine.setLayoutParams(params);
			
			tvewExpressName.setTextColor(mContext.getResources().getColor(
					R.color.gray));
			tvewExpressTime.setTextColor(mContext.getResources().getColor(
					R.color.gray));
			tvewExpressStatusMark
					.setBackgroundResource(R.drawable.express_status_old);
		}
		return convertView;
	}
}