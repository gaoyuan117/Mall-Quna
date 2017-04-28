package com.server.api.service;

import java.math.BigDecimal;

import com.android.zcomponent.http.api.host.Endpoint;
import com.android.zcomponent.json.WebFormEncoder;
import com.android.zcomponent.communication.http.Context.Method;
import com.android.zcomponent.communication.http.annotation.HttpPortal;
import com.google.gson.annotations.SerializedName;


public class WalletService
{
	@HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/User/Mywallet", method = Method.Post, encoder = WebFormEncoder.class)
	public static class MyWalletRequest extends Endpoint {

	}
	
	@HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Fund/withdraw", method = Method.Post, encoder = WebFormEncoder.class)
	public static class WithdrawRequest extends Endpoint {
		
		@SerializedName("bankcard_id")
		public String BankcardId;
		
		@SerializedName("money")
		public BigDecimal Money;
	}
	
	@HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/User/bankcardList", method = Method.Post, encoder = WebFormEncoder.class)
	public static class BankcardListRequest extends Endpoint {
		
	}
	
	@HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/User/addBankCard", method = Method.Post, encoder = WebFormEncoder.class)
	public static class AddBankcardRequest extends Endpoint {
		
		@SerializedName("card_no")
		public String CardNo;
		
		@SerializedName("true_name")
		public String Name;
	}
	
	@HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Fund/recharegeRecord", method = Method.Post, encoder = WebFormEncoder.class)
	public static class RechargeRecordRequest extends Endpoint {
		
		@SerializedName("page")
		public int Page;
		
		@SerializedName("pagesize")
		public int Pagesize;
	}
	
	@HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Fund/freezeRecord", method = Method.Post, encoder = WebFormEncoder.class)
	public static class FreezeRecordRequest extends Endpoint {
		
		@SerializedName("page")
		public int Page;
		
		@SerializedName("pagesize")
		public int Pagesize;
	}
	
	@HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Fund/unfreezeRecord", method = Method.Post, encoder = WebFormEncoder.class)
	public static class UnFreezeRecordRequest extends Endpoint {
		
		@SerializedName("page")
		public int Page;
		
		@SerializedName("pagesize")
		public int Pagesize;
	}
	
	@HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Fund/PaymentRecord", method = Method.Post, encoder = WebFormEncoder.class)
	public static class PaymentRecordRequest extends Endpoint {
		
		@SerializedName("page")
		public int Page;
		
		@SerializedName("pagesize")
		public int Pagesize;
	}
	
	@HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Fund/IncomeRecord", method = Method.Post, encoder = WebFormEncoder.class)
	public static class IncomeRecordRequest extends Endpoint {
		
		@SerializedName("page")
		public int Page;
		
		@SerializedName("pagesize")
		public int Pagesize;
	}
	
	@HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Fund/WithdrawalRecord", method = Method.Post, encoder = WebFormEncoder.class)
	public static class WithdrawalRecordRequest extends Endpoint {
		
		@SerializedName("page")
		public int Page;
		
		@SerializedName("pagesize")
		public int Pagesize;
	}
}
