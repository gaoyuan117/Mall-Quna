
package com.android.juzbao.dao;

import java.math.BigDecimal;

import com.server.api.service.WalletService;
import com.android.juzbao.constant.GlobalConst;
import com.android.zcomponent.http.HttpDataLoader;

public class WalletDao
{

	public static void sendCmdQueryMyWallet(HttpDataLoader httpDataLoader)
	{
		WalletService.MyWalletRequest request =
				new WalletService.MyWalletRequest();
		httpDataLoader.doPostProcess(request,
				com.server.api.model.MyWallet.class);
	}

	public static void sendCmdQueryWithdraw(HttpDataLoader httpDataLoader,
			String bankcardId, BigDecimal money)
	{
		WalletService.WithdrawRequest request =
				new WalletService.WithdrawRequest();
		request.BankcardId = bankcardId;
		request.Money = money;
		httpDataLoader.doPostProcess(request, com.server.api.model.CommonReturn.class);
	}

	public static void sendCmdQueryBankcardList(HttpDataLoader httpDataLoader)
	{
		WalletService.BankcardListRequest request =
				new WalletService.BankcardListRequest();
		httpDataLoader.doPostProcess(request, com.server.api.model.BankcardPageResult.class);
	}

	public static void sendCmdQueryRechargeRecord(HttpDataLoader httpDataLoader, int page)
	{
		WalletService.RechargeRecordRequest request =
				new WalletService.RechargeRecordRequest();
		
		request.Page = page;
		request.Pagesize = GlobalConst.INT_NUM_PAGE;
		httpDataLoader.doPostProcess(request, com.server.api.model.WalletRecordPageResult.class);
	}
	
	public static void sendCmdQueryFreezeRecord(HttpDataLoader httpDataLoader, int page)
	{
		WalletService.FreezeRecordRequest request =
				new WalletService.FreezeRecordRequest();
		
		request.Page = page;
		request.Pagesize = GlobalConst.INT_NUM_PAGE;
		httpDataLoader.doPostProcess(request, com.server.api.model.WalletRecordPageResult.class);
	}
	
	public static void sendCmdQueryUnFreezeRecord(HttpDataLoader httpDataLoader, int page)
	{
		WalletService.UnFreezeRecordRequest request =
				new WalletService.UnFreezeRecordRequest();
		
		request.Page = page;
		request.Pagesize = GlobalConst.INT_NUM_PAGE;
		httpDataLoader.doPostProcess(request, com.server.api.model.WalletRecordPageResult.class);
	}
	
	public static void sendCmdQueryIncomeRecord(HttpDataLoader httpDataLoader, int page)
	{
		WalletService.IncomeRecordRequest request =
				new WalletService.IncomeRecordRequest();
		
		request.Page = page;
		request.Pagesize = GlobalConst.INT_NUM_PAGE;
		httpDataLoader.doPostProcess(request, com.server.api.model.WalletRecordPageResult.class);
	}
	
	public static void sendCmdQueryPaymentRecord(HttpDataLoader httpDataLoader, int page)
	{
		WalletService.PaymentRecordRequest request =
				new WalletService.PaymentRecordRequest();
		
		request.Page = page;
		request.Pagesize = GlobalConst.INT_NUM_PAGE;
		httpDataLoader.doPostProcess(request, com.server.api.model.WalletRecordPageResult.class);
	}
	
	public static void sendCmdQueryWithdrawalRecord(HttpDataLoader httpDataLoader, int page)
	{
		WalletService.WithdrawalRecordRequest request =
				new WalletService.WithdrawalRecordRequest();
		
		request.Page = page;
		request.Pagesize = GlobalConst.INT_NUM_PAGE;
		httpDataLoader.doPostProcess(request, com.server.api.model.WalletRecordPageResult.class);
	}
	
	public static void sendCmdQueryAddBankcard(HttpDataLoader httpDataLoader,
			String cardNo, String name)
	{
		WalletService.AddBankcardRequest request =
				new WalletService.AddBankcardRequest();
		request.CardNo = cardNo;
		request.Name = name;
		httpDataLoader.doPostProcess(request, com.server.api.model.CommonReturn.class);
	}
}
