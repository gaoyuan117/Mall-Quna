
package com.android.juzbao.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.server.api.model.WalletRecordItem;
import com.android.mall.resource.R;
import com.android.zcomponent.adapter.CommonAdapter;
import com.android.zcomponent.util.StringUtil;
import com.android.zcomponent.util.TimeUtil;

/**
 * <p>
 * Description: 收入支出明细
 * </p>
 * 
 * @ClassName:WalletRecordAdapter
 * @author: wei
 * @date: 2015-11-10
 * 
 */
public class WalletRecordAdapter extends CommonAdapter
{

	public WalletRecordAdapter(Context context, List<?> list)
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
							.inflate(R.layout.adapter_wallet_record, null);
		}

		TextView tvewName = findViewById(convertView, R.id.tvew_name_show);
		TextView tvewTime = findViewById(convertView, R.id.tvew_time_show);
		TextView tvewPrice = findViewById(convertView, R.id.tvew_price_show);
		TextView tvewStatus = findViewById(convertView, R.id.tvew_status_show);

		final WalletRecordItem product = (WalletRecordItem) mList.get(position);

		tvewName.setText(product.desc);
		tvewTime.setText(TimeUtil.transformLongTimeFormat(
				Long.parseLong(product.create_time) * 1000,
				TimeUtil.STR_FORMAT_DATE_TIME2));
		tvewPrice.setText("¥" + StringUtil.formatProgress(product.money));
		tvewStatus.setText(product.status_text);

		return convertView;
	}
}