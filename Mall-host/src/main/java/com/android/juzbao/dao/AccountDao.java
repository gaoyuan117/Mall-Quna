
package com.android.juzbao.dao;

import com.android.zcomponent.util.share.AccountInfo;
import com.server.api.model.Account;
import com.server.api.model.AppInfo;
import com.server.api.model.CommonReturn;
import com.server.api.model.Head;
import com.server.api.model.Login;
import com.server.api.model.Mobile;
import com.server.api.model.NickName;
import com.server.api.model.Sex;
import com.server.api.model.Statistics;
import com.server.api.model.VerifyCode;
import com.server.api.model.Version;
import com.server.api.service.AccountService;
import com.android.zcomponent.http.HttpDataLoader;

public class AccountDao
{
	
	public static void sendCmdQueryVersion(HttpDataLoader httpDataLoader)
	{
		AccountService.VersionRequest registerRequest =
				new AccountService.VersionRequest();
		httpDataLoader.doPostProcess(registerRequest,
				Version.class);
	}
	
	public static void sendCmdQueryAppInfo(HttpDataLoader httpDataLoader)
	{
		AccountService.AppBaseinfoRequest registerRequest =
				new AccountService.AppBaseinfoRequest();
		httpDataLoader.doPostProcess(registerRequest, AppInfo.class);
	}

	public static void sendCmdRegister(HttpDataLoader httpDataLoader,
			String user, String password, String verify,String referrer)
	{
		AccountService.RegisterRequest registerRequest =
				new AccountService.RegisterRequest();
		registerRequest.Pwd = password;
		registerRequest.Repwd = password;
		registerRequest.User = user;
		registerRequest.Verify = verify;
		registerRequest.Referrer = referrer;
		httpDataLoader.doPostProcess(registerRequest, CommonReturn.class);
	}
	
	public static void sendCmdResetPassword(HttpDataLoader httpDataLoader,
			String user, String password, String verify)
	{
		AccountService.ResetPasswordRequest request =
				new AccountService.ResetPasswordRequest();
		request.Pwd = password;
		request.Repwd = password;
		request.User = user;
		request.Verify = verify;

		httpDataLoader.doPostProcess(request, CommonReturn.class);
	}
	
	public static void sendCmdEditPassword(HttpDataLoader httpDataLoader,
			String passwordOld, String passwordNew, String passwordNewConfirm)
	{
		AccountService.EditPasswordRequest request =
				new AccountService.EditPasswordRequest();
		request.Oldpwd = passwordOld;
		request.Newpwd = passwordNew;
		request.Repwd = passwordNewConfirm;

		httpDataLoader.doPostProcess(request, CommonReturn.class);
	}

	public static void sendCmdGetCaptcha(HttpDataLoader httpDataLoader,
			String user, String type)
	{
		AccountService.GetCaptchaRequest request =
				new AccountService.GetCaptchaRequest();
		request.User = user;
		request.Type = type;
		httpDataLoader.doPostProcess(request, VerifyCode.class);
	}
	
	public static void sendCmdLogin(HttpDataLoader httpDataLoader,
			String user, String pwd)
	{
		AccountService.LoginRequest request =
				new AccountService.LoginRequest();
		request.User = user;
		request.Pwd = pwd;
		request.Driver = "android";
		httpDataLoader.doPostProcess(request, Login.class);
	}
	
	public static void sendCmdLoginVister(HttpDataLoader httpDataLoader,
			String openId, String type)
	{
		AccountService.LoginVisiterRequest request =
				new AccountService.LoginVisiterRequest();
		request.Openid = openId;
		request.Type = type;
		httpDataLoader.doPostProcess(request, Login.class);
	}
	
	public static void sendCmdBindVisiter(HttpDataLoader httpDataLoader,
			String openId, String type, String mobile, String verfity, String nickname)
	{
		AccountService.BindVisiterRequest request =
				new AccountService.BindVisiterRequest();
		request.Openid = openId;
		request.Type = type;
		request.Mobile = mobile;
		request.Verify = verfity;
		request.Nickname = nickname;
		httpDataLoader.doPostProcess(request, CommonReturn.class);
	}
	
	public static void sendCmdGetNickName(HttpDataLoader httpDataLoader)
	{
		AccountService.GetNickNameRequest request =
				new AccountService.GetNickNameRequest();
		httpDataLoader.doPostProcess(request, NickName.class);
	}
	
	public static void sendCmdEditNickName(HttpDataLoader httpDataLoader,
			String nickname)
	{
		AccountService.EditNickNameRequest request =
				new AccountService.EditNickNameRequest();
		request.Nickname = nickname;
		httpDataLoader.doPostProcess(request, CommonReturn.class);
	}
	
	public static void sendCmdGetSex(HttpDataLoader httpDataLoader)
	{
		AccountService.GetSexRequest request =
				new AccountService.GetSexRequest();
		httpDataLoader.doPostProcess(request, Sex.class);
	}
	
	public static void sendCmdEditSex(HttpDataLoader httpDataLoader,
			int sex)
	{
		AccountService.EditSexRequest request =
				new AccountService.EditSexRequest();
		request.Sex = sex;
		httpDataLoader.doPostProcess(request, CommonReturn.class);
	}
	
	public static void sendCmdGetMobile(HttpDataLoader httpDataLoader)
	{
		AccountService.GetMobileRequest request =
				new AccountService.GetMobileRequest();
		httpDataLoader.doPostProcess(request, Mobile.class);
	}
	
	public static void sendCmdGetHead(HttpDataLoader httpDataLoader)
	{
		AccountService.HeadPathRequest request =
				new AccountService.HeadPathRequest();
		httpDataLoader.doPostProcess(request, Head.class);
	}
	
	public static void sendCmdModifyHead(HttpDataLoader httpDataLoader, String id)
	{
		AccountService.ModifyHeadPathRequest request =
				new AccountService.ModifyHeadPathRequest();
		request.CoverId = id;
		httpDataLoader.doPostProcess(request, CommonReturn.class);
	}
	
	public static void sendCmdEditMobile(HttpDataLoader httpDataLoader, String mobile, String verify)
	{
		AccountService.EditMobileRequest request =
				new AccountService.EditMobileRequest();
		request.Mobile = mobile;
		request.Verify = verify;
		httpDataLoader.doPostProcess(request, CommonReturn.class);
	}
	
	public static void sendCmdStatistics(HttpDataLoader httpDataLoader)
	{
		AccountService.StatisticsRequest request =
				new AccountService.StatisticsRequest();
		httpDataLoader.doPostProcess(request, Statistics.class);
	}
	
	public static void sendCmdQueryBaseInfo(HttpDataLoader httpDataLoader)
	{
		AccountService.BaseInfoRequest request =
				new AccountService.BaseInfoRequest();
		httpDataLoader.doPostProcess(request, Account.class);
	}
}
