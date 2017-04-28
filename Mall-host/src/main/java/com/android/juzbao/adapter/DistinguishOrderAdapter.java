
package com.android.juzbao.adapter;

import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.zcomponent.http.api.host.Endpoint;
import com.server.api.model.DistinguishItem;
import com.android.juzbao.activity.ExpressDetailActivity_;
import com.android.mall.resource.R;
import com.android.juzbao.activity.SubmitExpressActivity_;
import com.android.juzbao.model.DistinguishBusiness;
import com.android.juzbao.enumerate.DistinguishStatus;
import com.android.zcomponent.adapter.CommonAdapter;
import com.android.zcomponent.util.CommonUtil;

/**
 * <p>
 * Description: 我的鉴定列表项
 * </p>
 * 
 * @ClassName:DistinguishOrderAdapter
 * @author: wei
 * @date: 2015-11-10
 * 
 */
public class DistinguishOrderAdapter extends CommonAdapter
{

	public DistinguishOrderAdapter(Context context, List<?> list)
	{
		super(context, list);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent)
	{
		if (null == convertView)
		{
			convertView =
					layoutInflater.inflate(R.layout.adapter_distinguish_order,
							null);
		}

		ImageView imgvewPhoto =
				findViewById(convertView, R.id.imgvew_photo_show);
		TextView tvewOrderId =
				findViewById(convertView, R.id.tvew_order_id_show);
		TextView tvewOrderStatus =
				findViewById(convertView, R.id.tvew_order_status_show);
		TextView tvewProductName =
				findViewById(convertView, R.id.tvew_product_name_show);
		TextView tvewProductNum =
				findViewById(convertView, R.id.tvew_product_num_show);
		TextView tvewOrderPay =
				findViewById(convertView, R.id.tvew_order_pay_show_click);

		TextView tvewAddShippment =
				findViewById(convertView,
						R.id.tvew_order_add_shippment_show_click);
		TextView tvewShippment =
				findViewById(convertView, R.id.tvew_order_shippment_show_click);
		TextView tvewDel =
				findViewById(convertView, R.id.tvew_order_del_show_click);

		tvewOrderPay.setVisibility(View.GONE);
		tvewAddShippment.setVisibility(View.GONE);
		tvewShippment.setVisibility(View.GONE);
		tvewDel.setVisibility(View.GONE);

		final DistinguishItem item = (DistinguishItem) mList.get(position);

		tvewOrderId.setText("订单号：" + item.checkup_no);
		tvewOrderStatus.setText(item.status_text);
		tvewProductNum.setText("数量：" + item.quantity);
		tvewProductName.setText(item.title);

		mImageLoader.displayImage(Endpoint.HOST + item.image[0], imgvewPhoto,
				options);

		if (DistinguishStatus.PAY.getValue().equals(item.status))
		{
			tvewOrderPay.setVisibility(View.VISIBLE);
		}
		else if (DistinguishStatus.DELIVERY.getValue().equals(item.status))
		{
			tvewAddShippment.setVisibility(View.VISIBLE);
		}
		else if (DistinguishStatus.SENDED.getValue().equals(item.status)
				|| DistinguishStatus.DISTINGUISH.getValue().equals(item.status)
				|| DistinguishStatus.RECEIPT.getValue().equals(item.status))
		{
			tvewShippment.setVisibility(View.VISIBLE);
		}
		else if (DistinguishStatus.COMPLETE.getValue().equals(item.status))
		{
			tvewDel.setVisibility(View.VISIBLE);
		}

		tvewAddShippment.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				if (!CommonUtil.isLeastSingleClick())
				{
					return;
				}
				if (null != face)
				{
					face.onClickUpdate(position);
				}
				Bundle bundle = new Bundle();
				bundle.putString("id", item.id);
				intentToActivity(bundle, SubmitExpressActivity_.class);
			}
		});

		tvewShippment.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				if (!CommonUtil.isLeastSingleClick())
				{
					return;
				}
				if (null != face)
				{
					face.onClickUpdate(position);
				}
				Bundle bundle = new Bundle();
				bundle.putString("id", item.id);
				bundle.putString("image", item.image[0]);
				intentToActivity(bundle, ExpressDetailActivity_.class);
			}
		});

		tvewDel.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				if (!CommonUtil.isLeastSingleClick())
				{
					return;
				}
				if (null != face)
				{
					face.onClickCancel(position);
					face.onClickUpdate(position);
				}
			}
		});

		tvewOrderPay.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View view)
			{
				if (!CommonUtil.isLeastSingleClick())
				{
					return;
				}
				if (null != face)
				{
					face.onClickUpdate(position);
				}
				DistinguishBusiness.intentToDistinguishEnsureActivity(mContext,
						item, item.id);
			}
		});

		convertView.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				if (!CommonUtil.isLeastSingleClick())
				{
					return;
				}
				
				if (null != face)
				{
					face.onClickUpdate(position);
				}
				DistinguishBusiness.intentToDistinguishEnsureActivity(mContext,
						item, item.id);
			}
		});
		return convertView;
	}

	CallBackInteface face;

	public void setCallBackInteface(CallBackInteface cbif)
	{
		this.face = cbif;
	}

	public interface CallBackInteface
	{

		public void onClickCancel(int position);
		
		public void onClickUpdate(int position);

	}
}