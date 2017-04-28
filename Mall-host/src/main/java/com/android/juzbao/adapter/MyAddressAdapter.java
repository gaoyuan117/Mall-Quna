
package com.android.juzbao.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.juzbao.activity.jifen.JifenConvertActivity_;
import com.android.juzbao.activity.me.MyAddressEditActivity_;
import com.android.juzbao.activity.order.OrderEnsureActivity_;
import com.android.juzbao.application.BaseApplication;
import com.android.juzbao.constant.ResultActivity;
import com.android.mall.resource.R;
import com.android.zcomponent.adapter.CommonAdapter;
import com.android.zcomponent.common.uiframe.BaseActivity;
import com.android.zcomponent.json.JsonSerializerFactory;
import com.server.api.model.Address;

import java.util.List;

/**
 * <p>
 * Description: 收货地址列表项
 * </p>
 * 
 * @ClassName:MyAddressAdapter
 * @author: wei
 * @date: 2015-11-10
 * 
 */
public class MyAddressAdapter extends CommonAdapter
{

	public MyAddressAdapter(Context context, List<?> list)
	{
		super(context, list);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		if (null == convertView)
		{
			convertView =
					layoutInflater.inflate(R.layout.adapter_my_address, null);
		}

		final Address.Data address = (Address.Data) mList.get(position);

		TextView tvewPersonal = findViewById(convertView, R.id.tvew_personal_show);
		TextView tvewPhone = findViewById(convertView, R.id.tvew_phone_show);

		//收货地址
		TextView tvewAddress = findViewById(convertView, R.id.tvew_address_show);

		tvewPersonal.setText(address.realname);
		tvewPhone.setText(address.mobile);
		
		String addressInfo = address.province + address.city + address.area
				+ address.address;
		
		if ("1".equals(address.is_default))
		{
			tvewAddress.setText("[默认] " + addressInfo);
		}
		else
		{
			tvewAddress.setText(addressInfo);
		}

		convertView.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				if (((BaseActivity) mContext).getIntentHandle().isIntentFrom(OrderEnsureActivity_.class) ||
						((BaseActivity) mContext).getIntentHandle().isIntentFrom(JifenConvertActivity_.class))
				{
					Intent intent = new Intent();
					intent.putExtra("address", JsonSerializerFactory.Create()
							.encode(address));
					BaseApplication.getInstance().setActivityResult(
							ResultActivity.CODE_ADDRESS_SELECT, intent);
					((BaseActivity) mContext).finish();
				}
				else
				{
					Bundle bundle = new Bundle();
					bundle.putString("address", JsonSerializerFactory.Create()
							.encode(address));
					intentToActivity(bundle, MyAddressEditActivity_.class);

				}
			}
		});

		return convertView;
	}
}