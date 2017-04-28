package com.server.api.service;

import com.android.zcomponent.communication.http.Context.Method;
import com.android.zcomponent.communication.http.annotation.HttpPortal;
import com.android.zcomponent.http.api.host.Endpoint;
import com.android.zcomponent.json.WebFormEncoder;
import com.google.gson.annotations.SerializedName;

public class AccountService {

  @HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Public/appVersion", method = Method.Post,
      encoder = WebFormEncoder.class)
  public static class VersionRequest extends Endpoint {
  }

  @HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/User/sign", method = Method.Post, encoder =
      WebFormEncoder.class)
  public static class SignRequest extends Endpoint {
  }

  @HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Sign/check", method = Method.Post, encoder =
      WebFormEncoder.class)
  public static class IsSignRequest extends Endpoint {

  }

  @HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/User/gift", method = Method.Post, encoder =
      WebFormEncoder.class)
  public static class GiftRequest extends Endpoint {

  }

  @HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/User/useGift", method = Method.Post, encoder
      = WebFormEncoder.class)
  public static class UseGiftRequest extends Endpoint {
    @SerializedName("shopId")
    public String ShopId;

    @SerializedName("gift")
    public String Gift;

    @SerializedName("quantity")
    public String Quantity;
  }

  @HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Public/appBaseinfo", method = Method.Post,
      encoder = WebFormEncoder.class)
  public static class AppBaseinfoRequest extends Endpoint {

  }

  @HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Public/verify", method = Method.Post, encoder
      = WebFormEncoder.class)
  public static class GetCaptchaRequest extends Endpoint {

    @SerializedName("user")
    public String User;

    @SerializedName("type")
    public String Type;
  }

  @HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/User/register", method = Method.Post, encoder
      = WebFormEncoder.class)
  public static class RegisterRequest extends Endpoint {

    @SerializedName("user")
    public String User;

    @SerializedName("pwd")
    public String Pwd;

    @SerializedName("repwd")
    public String Repwd;

    @SerializedName("verify")
    public String Verify;

    @SerializedName("referrer")
    public String Referrer;

  }

  @HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/User/Login", method = Method.Post, encoder =
      WebFormEncoder.class)
  public static class LoginRequest extends Endpoint {

    @SerializedName("user")
    public String User;

    @SerializedName("pwd")
    public String Pwd;

    @SerializedName("driver")
    public String Driver;

  }

  @HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/User/OauthLogin", method = Method.Post,
      encoder = WebFormEncoder.class)
  public static class LoginVisiterRequest extends Endpoint {

    @SerializedName("openid")
    public String Openid;

    @SerializedName("type")
    public String Type;

  }

  @HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/User/Oauthbind", method = Method.Post,
      encoder = WebFormEncoder.class)
  public static class BindVisiterRequest extends Endpoint {

    @SerializedName("openid")
    public String Openid;

    @SerializedName("type")
    public String Type;

    @SerializedName("mobile")
    public String Mobile;

    @SerializedName("verify")
    public String Verify;

    @SerializedName("nickname")
    public String Nickname;

  }

  @HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/User/resetPassword", method = Method.Post,
      encoder = WebFormEncoder.class)
  public static class ResetPasswordRequest extends Endpoint {

    @SerializedName("user")
    public String User;

    @SerializedName("pwd")
    public String Pwd;

    @SerializedName("repwd")
    public String Repwd;

    @SerializedName("verify")
    public String Verify;

  }

  @HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/User/editPassword", method = Method.Post,
      encoder = WebFormEncoder.class)
  public static class EditPasswordRequest extends Endpoint {

    @SerializedName("oldpwd")
    public String Oldpwd;

    @SerializedName("newpwd")
    public String Newpwd;

    @SerializedName("repwd")
    public String Repwd;
  }

  @HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/User/getSex", method = Method.Post, encoder =
      WebFormEncoder.class)
  public static class GetSexRequest extends Endpoint {

  }

  @HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/User/editSex", method = Method.Post, encoder
      = WebFormEncoder.class)
  public static class EditSexRequest extends Endpoint {

    /** 0:保密，1:男，2:女 */
    @SerializedName("sex")
    public int Sex;
  }

  @HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/User/getMobile", method = Method.Post,
      encoder = WebFormEncoder.class)
  public static class GetMobileRequest extends Endpoint {

  }

  @HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/User/editMobile", method = Method.Post,
      encoder = WebFormEncoder.class)
  public static class EditMobileRequest extends Endpoint {

    @SerializedName("mobile")
    public String Mobile;

    @SerializedName("verify")
    public String Verify;
  }

  @HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/User/editNickname", method = Method.Post,
      encoder = WebFormEncoder.class)
  public static class EditNickNameRequest extends Endpoint {

    @SerializedName("nickname")
    public String Nickname;

  }

  @HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/User/getNickname", method = Method.Post,
      encoder = WebFormEncoder.class)
  public static class GetNickNameRequest extends Endpoint {

  }

  @HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/User/statistics", method = Method.Post,
      encoder = WebFormEncoder.class)
  public static class StatisticsRequest extends Endpoint {

  }

  @HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/User/BaseInfo", method = Method.Post, encoder
      = WebFormEncoder.class)
  public static class BaseInfoRequest extends Endpoint {

  }

  @HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/User/avatarPath", method = Method.Post,
      encoder = WebFormEncoder.class)
  public static class HeadPathRequest extends Endpoint {

  }

  @HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/User/modifyAvatar", method = Method.Post,
      encoder = WebFormEncoder.class)
  public static class ModifyHeadPathRequest extends Endpoint {

    @SerializedName("cover_id")
    public String CoverId;
  }
}