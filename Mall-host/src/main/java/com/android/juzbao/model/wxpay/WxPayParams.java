package com.android.juzbao.model.wxpay;


public class WxPayParams {
  public String appid;      //微信开放平台审核通过的应用APPID
  public String partnerid;  //微信支付分配的商户号
  public String prepayid;   //微信返回的支付交易会话ID
  //    public String package;  //扩展字段  暂填写固定值Sign=WXPay
  public String noncestr;   //随机字符串，不长于32位。推荐随机数生成算法
  public String timestamp;  //时间戳，请见接口规则-参数规定
  public String sign;       //签名，详见签名生成算法

  public WxPayParams(Builder builder) {
    this.appid = builder.appid;
    this.partnerid = builder.partnerid;
    this.prepayid = builder.prepayid;
    this.noncestr = builder.noncestr;
    this.timestamp = builder.timestamp;
    this.sign = builder.sign;
  }

  public static class Builder {
    public String appid;      //微信开放平台审核通过的应用APPID
    public String partnerid;  //微信支付分配的商户号
    public String prepayid;   //微信返回的支付交易会话ID
    //    public String package;  //扩展字段  暂填写固定值Sign=WXPay
    public String noncestr;   //随机字符串，不长于32位。推荐随机数生成算法
    public String timestamp;  //时间戳，请见接口规则-参数规定
    public String sign;       //签名，详见签名生成算法

    public Builder appid(String appid) {
      this.appid = appid;
      return this;
    }

    public Builder partnerid(String partnerid) {
      this.partnerid = partnerid;
      return this;
    }

    public Builder prepayid(String prepayid) {
      this.prepayid = prepayid;
      return this;
    }

    public Builder noncestr(String noncestr) {
      this.noncestr = noncestr;
      return this;
    }

    public Builder timestamp(String timestamp) {
      this.timestamp = timestamp;
      return this;
    }

    public Builder sign(String sign) {
      this.sign = sign;
      return this;
    }

    public WxPayParams build() {
      return new WxPayParams(this);
    }
  }
}
