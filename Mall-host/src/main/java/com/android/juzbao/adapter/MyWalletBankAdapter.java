
package com.android.juzbao.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.server.api.model.Bankcard;
import com.android.mall.resource.R;
import com.android.zcomponent.adapter.CommonAdapter;

/**
 * <p>
 * Description: 提现银行卡列表项
 * </p>
 * 
 * @ClassName:MyWalletBankAdapter
 * @author: wei
 * @date: 2015-11-10
 * 
 */
public class MyWalletBankAdapter extends CommonAdapter
{
	private int miSelectPositon;

	public MyWalletBankAdapter(Context context, List<?> list)
	{
		super(context, list);
	}
	
	public int getSelectPosition()
	{
		return miSelectPositon;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent)
	{
		if (null == convertView)
		{
			convertView =
					layoutInflater.inflate(R.layout.adapter_my_wallet_bank,
							null);
		}

		TextView tvewRedrawBankName =
				findViewById(convertView, R.id.tvew_redraw_bank_name_show);
		TextView tvewRedrawBankNum =
				findViewById(convertView, R.id.tvew_redraw_bank_num_show);
		TextView tvewRedrawBankCheck =
				findViewById(convertView, R.id.tvew_redraw_bank_check_show);

		Bankcard bankcard = (Bankcard) mList.get(position);

		tvewRedrawBankName.setText(bankcard.bank_title);
		tvewRedrawBankNum.setText("尾号"
				+ bankcard.card_no.substring(bankcard.card_no.length() - 4,
						bankcard.card_no.length()));
		
		if (miSelectPositon == position)
		{
			tvewRedrawBankCheck.setVisibility(View.VISIBLE);
		}
		else
		{
			tvewRedrawBankCheck.setVisibility(View.GONE);
		}
		convertView.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				miSelectPositon = position;
				notifyDataSetChanged();
			}
		});
		return convertView;
	}
}