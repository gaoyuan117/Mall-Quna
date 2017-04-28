
package com.android.juzbao.model;

import java.math.BigDecimal;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.server.api.model.MyWallet;
import com.android.mall.resource.R;
import com.android.juzbao.dao.WalletDao;
import com.android.juzbao.enumerate.WalletRecordType;
import com.android.zcomponent.http.HttpDataLoader;
import com.android.zcomponent.util.ShowMsg;
import com.android.zcomponent.util.StringUtil;

public class WalletBusiness
{

	public static void queryMyWallet(HttpDataLoader httpDataLoader)
	{
		WalletDao.sendCmdQueryMyWallet(httpDataLoader);
	}
	
	public static void queryMyWalletRecord(HttpDataLoader httpDataLoader, int page, WalletRecordType type)
	{
		switch (type)
		{
		case IN_INCOME:
		{
			WalletDao.sendCmdQueryIncomeRecord(httpDataLoader, page);
			break;
		}
		case IN_RECHARGE:
		{
			WalletDao.sendCmdQueryRechargeRecord(httpDataLoader, page);
			break;
		}
		case IN_UNFREEZE:
		{
			WalletDao.sendCmdQueryUnFreezeRecord(httpDataLoader, page);
			break;
		}
		case OUT_PAYMENT:
		{
			WalletDao.sendCmdQueryPaymentRecord(httpDataLoader, page);
			break;
		}
		case OUT_WITHDRAW:
		{
			WalletDao.sendCmdQueryWithdrawalRecord(httpDataLoader, page);
			break;
		}
		case OUT_FREEZE:
		{
			WalletDao.sendCmdQueryFreezeRecord(httpDataLoader, page);
			break;
		}
		default:
			break;
		}
	}

	public static boolean queryWithdraw(Context context,
			HttpDataLoader httpDataLoader, String bankcardId, String money)
	{
		if (TextUtils.isEmpty(money))
		{
			ShowMsg.showToast(context, "请输入提现金额");
			return false;
		}

		if (new BigDecimal(money).doubleValue() == 0)
		{
			ShowMsg.showToast(context, "请提现需大于零");
			return false;
		}
		
		if (TextUtils.isEmpty(bankcardId))
		{
			ShowMsg.showToast(context, "请选择银行卡");
			return false;
		}
		
		WalletDao.sendCmdQueryWithdraw(httpDataLoader, bankcardId,
				StringUtil.formatProgress(new BigDecimal(money)));
		return true;
	}
	
	public static void queryBankcardList(HttpDataLoader httpDataLoader)
	{
		WalletDao.sendCmdQueryBankcardList(httpDataLoader);
	}
	
	public static boolean queryAddBankcard(Context context,
			HttpDataLoader httpDataLoader, String cardNo, String name)
	{
		if (TextUtils.isEmpty(cardNo))
		{
			ShowMsg.showToast(context, "请输入持卡人姓名");
			return false;
		}
		
		if (TextUtils.isEmpty(name))
		{
			ShowMsg.showToast(context, "请输入银行卡号");
			return false;
		}
		
		WalletDao.sendCmdQueryAddBankcard(httpDataLoader, cardNo, name);
		
		return true;
	}

	public static void showMyWalletInfo(View view, MyWallet.Data wallet)
	{
		TextView mTvewAmount =
				(TextView) view.findViewById(R.id.tvew_wallet_amount_show);
		TextView mTvewFreezeAmount =
				(TextView) view
						.findViewById(R.id.tvew_wallet_freeze_amount_show);

		if (null != wallet)
		{
			mTvewAmount
					.setText("¥ " + StringUtil.formatProgress(wallet.amount));
			mTvewFreezeAmount.setText("¥ "
					+ StringUtil.formatProgress(wallet.freeze_amount));
		}
		else
		{
			mTvewAmount.setText("¥ 0.00");
			mTvewFreezeAmount.setText("¥ 0.00");
		}
	}
}
