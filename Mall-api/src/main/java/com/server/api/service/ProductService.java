package com.server.api.service;

import com.android.zcomponent.communication.http.Context.Method;
import com.android.zcomponent.communication.http.annotation.HttpPortal;
import com.android.zcomponent.http.api.host.Endpoint;
import com.android.zcomponent.json.WebFormEncoder;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;


public class ProductService {
    @HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Shop/ShopInfo", method = Method.Post, encoder = WebFormEncoder.class)
    public static class ShopInfoRequest extends Endpoint {

        @SerializedName("shop_id")
        public String shop_id;
    }

    @Deprecated
    @HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Product/getHots", method = Method.Post, encoder = WebFormEncoder.class)
    public static class RecommandProductRequest extends Endpoint {

    }

    /**
     * 官方公告列表
     */
    @HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Notice/getList", method = Method.Post, encoder = WebFormEncoder.class)
    public static class NoticeRequest extends Endpoint {
        @SerializedName("page")
        public int Page;

        @SerializedName("pagesize")
        public int Pagesize;
    }

    /**
     * 官方公告详情
     */
    @HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Notice/getDetail", method = Method.Post, encoder = WebFormEncoder.class)
    public static class NoticeDetailRequest extends Endpoint {
        @SerializedName("id")
        public String Id;
    }

    /**
     * 获取广告
     */
    @HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Ad/getList", method = Method.Post, encoder = WebFormEncoder.class)
    public static class AdvertRequest extends Endpoint {

        @SerializedName("position")
        public String Identifier;
    }

    /**
     * 广告，首页推荐
     */
    @HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Ad/getList", method = Method.Post, encoder = WebFormEncoder.class)
    public static class AdIndexRecomRequest extends Endpoint {

        @SerializedName("position")
        public String Identifier;
    }

    /**
     * 广告，分类推荐
     */
    @HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Ad/getList", method = Method.Post, encoder = WebFormEncoder.class)
    public static class AdCgRecomRequest extends Endpoint {

        @SerializedName("position")
        public String Identifier;

//        @SerializedName("category_id")
//        public String id;

    }

    /**
     * 广告，分类推荐
     */
    @HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Ad/getList", method = Method.Post, encoder = WebFormEncoder.class)
    public static class AdScoreImgRequest extends Endpoint {

        @SerializedName("position")
        public String Identifier;

    }

    /**
     * 首页数据
     */
    @HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Home/homepage", method = Method.Post, encoder = WebFormEncoder.class)
    public static class HomeRequest extends Endpoint {

    }

    /**
     * 获取分类模块
     */
    @HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Product/getGroups", method = Method.Post, encoder = WebFormEncoder.class)
    public static class GroupRequest extends Endpoint {
        @SerializedName("group")
        public String Group;
    }

    /**
     * 获取分类列表
     */
    @HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Product/getCategoryTree", method = Method.Post, encoder = WebFormEncoder.class)
    public static class CategoryTreeRequest extends Endpoint {

        @SerializedName("gid")
        public String Gid;
    }

    /**
     * 获取分类别表
     */
    @HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Product/getCategoryTree", method = Method.Post, encoder = WebFormEncoder.class)
    public static class ThirdCategoryTreeRequest extends Endpoint {

        @SerializedName("gid")
        public String Gid;
    }

    @HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Product/productOption", method = Method.Post, encoder = WebFormEncoder.class)
    public static class ProductOptionRequest extends Endpoint {

        @SerializedName("product_id")
        public String ProductId;

    }

    @HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Shop/Products", method = Method.Post, encoder = WebFormEncoder.class)
    public static class ShopProductsRequest extends Endpoint {

        @SerializedName("shop_id")
        public int ShopId;

        @SerializedName("page")
        public int Page;

        @SerializedName("pagesize")
        public int Pagesize;

    }

    @HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Shop/produtSearch", method = Method.Post, encoder = WebFormEncoder.class)
    public static class ShopProductSearchRequest extends Endpoint {

        @SerializedName("keyword")
        public String Keyword;

        @SerializedName("shop_id")
        public int ShopId;

        @SerializedName("page")
        public int Page;

        @SerializedName("pagesize")
        public int Pagesize;

    }

    /**
     * 获取商品
     */
    @HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Product/getProducts", method = Method.Post, encoder = WebFormEncoder.class)
    public static class ProductsRequest extends Endpoint {

        @SerializedName("type")
        public String Type;

        @SerializedName("page")
        public int Page;

        @SerializedName("pagesize")
        public int Pagesize;

        @SerializedName("category_id")
        public String CategoryId;

        @SerializedName("sortview")
        public String Sortview;

    }

    @HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Product/getGiftCategory", method = Method.Post, encoder = WebFormEncoder.class)
    public static class GiftCagetoryRequest extends Endpoint {

    }

    @HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Product/getProductGift", method = Method.Post, encoder = WebFormEncoder.class)
    public static class ProductGiftRequest extends Endpoint {
        @SerializedName("product_id")
        public long ProductId;
    }

    @HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Product/paipinSignup", method = Method.Post, encoder = WebFormEncoder.class)
    public static class ProductSignupRequest extends Endpoint {
        @SerializedName("product_id")
        public long ProductId;
    }

    @HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Product/bid", method = Method.Post, encoder = WebFormEncoder.class)
    public static class ProductSignupMoneyRequest extends Endpoint {
        @SerializedName("product_id")
        public long ProductId;

        @SerializedName("money")
        public BigDecimal Money;
    }

    @HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Product/bidLog", method = Method.Post, encoder = WebFormEncoder.class)
    public static class ProductSignupLogRequest extends Endpoint {
        @SerializedName("product_id")
        public long ProductId;

        @SerializedName("page")
        public int Page;

        @SerializedName("pagesize")
        public int Pagesize;
    }

    @HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Product/bidClause", method = Method.Post, encoder = WebFormEncoder.class)
    public static class BondNoticeRequest extends Endpoint {

    }

    @HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Public/search", method = Method.Post, encoder = WebFormEncoder.class)
    public static class SearchRequest extends Endpoint {
        @SerializedName("type")
        public String Type;

        @SerializedName("keyword")
        public String Keyword;

        @SerializedName("page")
        public int Page;

        @SerializedName("pagesize")
        public int Pagesize;
    }

    @HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Product/getProducts", method = Method.Post, encoder = WebFormEncoder.class)
    public static class GiftProductsRequest extends Endpoint {
        @SerializedName("type")
        public String Type;

        @SerializedName("page")
        public int Page;

        @SerializedName("pagesize")
        public int Pagesize;

        @SerializedName("category_id")
        public int CategoryId;

        @SerializedName("in_special_gift")
        public int InSpecialGift;

        @SerializedName("gift_id")
        public int GiftId;
    }

    @HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Product/getProducts", method = Method.Post, encoder = WebFormEncoder.class)
    public static class PanicProductsRequest extends Endpoint {
        @SerializedName("type")
        public String Type;

        @SerializedName("page")
        public int Page;

        @SerializedName("pagesize")
        public int Pagesize;

        @SerializedName("category_id")
        public int CategoryId;

        @SerializedName("in_special_panic")
        public int InSpecialPanic;

        @SerializedName("panic_id")
        public long PanicId;

        @SerializedName("panic_sortdiscount")
        public int SortDiscount;

        @SerializedName("panic_endtime")
        public long EndTime;
    }

    @HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Product/getpaipinList", method = Method.Post, encoder = WebFormEncoder.class)
    public static class PaipinProductsRequest extends Endpoint {
        @SerializedName("page")
        public int Page;

        @SerializedName("pagesize")
        public int Pagesize;

        @SerializedName("category_id")
        public int CategoryId;

        @SerializedName("is_delicacy")
        public int IsDelicacy;

        @SerializedName("starttime")
        public int Starttime;

        @SerializedName("endtime")
        public int Endtime;

        @SerializedName("price_start")
        public int PriceStart;

    }

    @HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Public/getStatus", method = Method.Post, encoder = WebFormEncoder.class)
    public static class GetStatusRequest extends Endpoint {
        @SerializedName("model")
        public String Model;

        @SerializedName("id")
        public int Id;
    }

    @HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Product/getMyProducts", method = Method.Post, encoder = WebFormEncoder.class)
    public static class MyProductsRequest extends Endpoint {
        @SerializedName("type")
        public String Type;

        @SerializedName("page")
        public int Page;

        @SerializedName("pagesize")
        public int Pagesize;

        @SerializedName("status")
        public int Status;
    }

    @HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Product/getProduct", method = Method.Post, encoder = WebFormEncoder.class)
    public static class ProductRequest extends Endpoint {

        @SerializedName("id")
        public long Id;
    }

    @HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Product/getMyProduct", method = Method.Post, encoder = WebFormEncoder.class)
    public static class MyProductRequest extends Endpoint {

        @SerializedName("id")
        public long Id;
    }

    @HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Product/editOption", method = Method.Post, encoder = WebFormEncoder.class)
    public static class EditOptionRequest extends Endpoint {

        @SerializedName("id")
        public String Id;

        @SerializedName("pid")
        public String PId;

        @SerializedName("product_id")
        public String ProductId;

        @SerializedName("text")
        public String Text;

    }

    @HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Product/editOption", method = Method.Post, encoder = WebFormEncoder.class)
    public static class EditChildOptionRequest extends Endpoint {

        @SerializedName("id")
        public String Id;

        @SerializedName("pid")
        public String PId;

        @SerializedName("product_id")
        public String ProductId;

        @SerializedName("text")
        public String Text;

    }

    @HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Product/deleteOption", method = Method.Post, encoder = WebFormEncoder.class)
    public static class DelOptionRequest extends Endpoint {

        @SerializedName("id")
        public String Id;
    }

    @HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Product/addOptionPrice", method = Method.Post, encoder = WebFormEncoder.class)
    public static class AddOptionPriceRequest extends Endpoint {

        @SerializedName("product_id")
        public String ProductId;

        @SerializedName("ids")
        public String Ids;

        @SerializedName("price")
        public BigDecimal Price;
    }

    @HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Product/editOptionPrice", method = Method.Post, encoder = WebFormEncoder.class)
    public static class EditOptionPriceRequest extends Endpoint {

        @SerializedName("id")
        public long Id;

        @SerializedName("price")
        public String Price;
    }

    @HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Product/deleteOptionPrice", method = Method.Post, encoder = WebFormEncoder.class)
    public static class DelOptionPriceRequest extends Endpoint {

        @SerializedName("id")
        public String Id;
    }

    @HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Product/getPanicTimes", method = Method.Post, encoder = WebFormEncoder.class)
    public static class PanicTimesRequest extends Endpoint {

        @SerializedName("type")
        public String Type;
    }

    @HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Product/myproductPutaway", method = Method.Post, encoder = WebFormEncoder.class)
    public static class PutawayProductRequest extends Endpoint {

        @SerializedName("id")
        public String Id;
    }

    @HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Product/myproductHalt", method = Method.Post, encoder = WebFormEncoder.class)
    public static class HaltProductRequest extends Endpoint {

        @SerializedName("id")
        public String Id;
    }

    @HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Product/myproductDel", method = Method.Post, encoder = WebFormEncoder.class)
    public static class DelProductRequest extends Endpoint {

        @SerializedName("id")
        public String Id;
    }

    @HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Product/addProductGift", method = Method.Post, encoder = WebFormEncoder.class)
    public static class AddProductGiftRequest extends Endpoint {

        @SerializedName("product_id")
        public long ProductId;

        @SerializedName("special_gift_id[]")
        public Long[] SpecialGiftId;
    }

    @HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Product/addProductPanic", method = Method.Post, encoder = WebFormEncoder.class)
    public static class AddProductPanicRequest extends Endpoint {

        @SerializedName("product_id")
        public long ProductId;

        @SerializedName("special_panic_id")
        public long SpecialPanicId;

        @SerializedName("condition_price")
        public BigDecimal ConditionPrice;

        @SerializedName("discount")
        public int Discount;
    }

    /**
     * 添加普通商品
     */
    @HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Product/add", method = Method.Post, encoder = WebFormEncoder.class)
    public static class ProductAddPutongRequest extends Endpoint {

        //商家赠送积分。
        @SerializedName("score")
        public int Score;

        @SerializedName("type")
        public String Type;

        @SerializedName("title")
        public String Title;

        @SerializedName("category_id")
        public int CategoryId;

        @SerializedName("price")
        public double Price;

        @SerializedName("quantity")
        public int Quantity;

        @SerializedName("description")
        public String Description;

        @SerializedName("province_id")
        public String ProvinceId;

        @SerializedName("city_id")
        public String CityId;

        @SerializedName("area_id")
        public String AreaId;

        @SerializedName("address")
        public String Address;

        @SerializedName("security_7days")
        public int Security7days;

        @SerializedName("security_delivery")
        public int SecurityDelivery;

        @SerializedName("status")
        public int Status;

        @SerializedName("pics[]")
        public Integer[] Pics;

        @SerializedName("movie")
        public int Movie;

        @SerializedName("movie_thumb_id")
        public int MovieThumbId;
    }

    @HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Product/add", method = Method.Post, encoder = WebFormEncoder.class)
    public static class ProductAddAuctionRequest extends Endpoint {

        @SerializedName("type")
        public String Type;

        @SerializedName("title")
        public String Title;

        @SerializedName("category_id")
        public int CategoryId;

        @SerializedName("price")
        public double Price;

        @SerializedName("maxprice")
        public double Maxprice;

        @SerializedName("quantity")
        public int Quantity;

        @SerializedName("description")
        public String Description;

        @SerializedName("province_id")
        public int ProvinceId;

        @SerializedName("city_id")
        public int CityId;

        @SerializedName("area_id")
        public int AreaId;

        @SerializedName("address")
        public String Address;

        @SerializedName("start_date")
        public String StartDate;

        @SerializedName("start_hour")
        public int StartTime;

        @SerializedName("end_date")
        public String EndDate;

        @SerializedName("end_hour")
        public int EndTime;

        @SerializedName("security_7days")
        public int Security7days;

        @SerializedName("security_delivery")
        public int SecurityDelivery;

        @SerializedName("status")
        public int Status;

        @SerializedName("pics[]")
        public Integer[] Pics;

        @SerializedName("movie")
        public int Movie;

        @SerializedName("movie_thumb_id")
        public int MovieThumbId;
    }

    @HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Product/add", method = Method.Post, encoder = WebFormEncoder.class)
    public static class ProductAddDingzhiRequest extends Endpoint {

        @SerializedName("type")
        public String Type;

        @SerializedName("verify")
        public String Verify;

        @SerializedName("title")
        public String Title;

        @SerializedName("category_id")
        public int CategoryId;

        @SerializedName("price")
        public double Price;

        @SerializedName("end_time")
        public String EndTime;

        @SerializedName("over_day")
        public int OverDay;

        @SerializedName("over_persent")
        public int OverPersent;

        @SerializedName("description")
        public String Description;

        @SerializedName("province_id")
        public int ProvinceId;

        @SerializedName("city_id")
        public int CityId;

        @SerializedName("area_id")
        public int AreaId;

        @SerializedName("address")
        public String Address;

        @SerializedName("security_7days")
        public int Security7days;

        @SerializedName("security_delivery")
        public int SecurityDelivery;

        @SerializedName("pics[]")
        public Integer[] Pics;

        @SerializedName("movie")
        public int Movie;

        @SerializedName("movie_thumb_id")
        public int MovieThumbId;
    }


    @HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Product/add", method = Method.Post, encoder = WebFormEncoder.class)
    public static class ProductAddFreeRequest extends Endpoint {

        @SerializedName("type")
        public String Type;

        @SerializedName("title")
        public String Title;

        @SerializedName("category_id")
        public int CategoryId;

        @SerializedName("price")
        public double Price;

        @SerializedName("quantity")
        public int Quantity;

        @SerializedName("province_id")
        public int ProvinceId;

        @SerializedName("city_id")
        public int CityId;

        @SerializedName("area_id")
        public int AreaId;

        @SerializedName("address")
        public String Address;

        @SerializedName("description")
        public String Description;

        @SerializedName("security_7days")
        public int Security7days;

        @SerializedName("security_delivery")
        public int SecurityDelivery;

        @SerializedName("status")
        public int Status;

        @SerializedName("pics[]")
        public Integer[] Pics;

        @SerializedName("movie")
        public int Movie;

        @SerializedName("movie_thumb_id")
        public int MovieThumbId;
    }

    @HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Product/add", method = Method.Post, encoder = WebFormEncoder.class)
    public static class ProductAddPhotoBuyRequest extends Endpoint {

        @SerializedName("type")
        public String Type;

        @SerializedName("title")
        public String Title;

        @SerializedName("category_id")
        public int CategoryId;

        @SerializedName("price_start")
        public double PriceStart;

        @SerializedName("price_end")
        public double PriceEnd;

        @SerializedName("description")
        public String Description;

        @SerializedName("security_7days")
        public int Security7days;

        @SerializedName("security_delivery")
        public int SecurityDelivery;

        @SerializedName("pics[]")
        public Integer[] Pics;

        @SerializedName("movie")
        public int Movie;

        @SerializedName("movie_thumb_id")
        public int MovieThumbId;
    }

    /**
     * 编辑普通商品
     */
    @HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Product/edit", method = Method.Post, encoder = WebFormEncoder.class)
    public static class ProductEditPutongRequest extends Endpoint {

        //商家赠送积分。
        @SerializedName("score")
        public int Score;

        @SerializedName("type")
        public String Type;

        @SerializedName("title")
        public String Title;

        @SerializedName("in_special_panic")
        public String InSpecialPanic;

        @SerializedName("in_special_gift")
        public String InSpecialGift;

        @SerializedName("id")
        public long Id;

        @SerializedName("category_id")
        public int CategoryId;

        @SerializedName("price")
        public double Price;

        @SerializedName("quantity")
        public int Quantity;

        @SerializedName("description")
        public String Description;

        @SerializedName("province_id")
        public String ProvinceId;

        @SerializedName("city_id")
        public String CityId;

        @SerializedName("area_id")
        public String AreaId;

        @SerializedName("address")
        public String Address;

        @SerializedName("security_7days")
        public int Security7days;

        @SerializedName("security_delivery")
        public int SecurityDelivery;

        @SerializedName("status")
        public int Status;

        @SerializedName("pics[]")
        public Integer[] Pics;

        @SerializedName("movie")
        public int Movie;

        @SerializedName("movie_thumb_id")
        public int MovieThumbId;
    }

    @HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Product/edit", method = Method.Post, encoder = WebFormEncoder.class)
    public static class ProductEditAuctionRequest extends Endpoint {

        @SerializedName("type")
        public String Type;

        @SerializedName("title")
        public String Title;

        @SerializedName("in_special_panic")
        public String InSpecialPanic;

        @SerializedName("in_special_gift")
        public String InSpecialGift;

        @SerializedName("id")
        public long Id;

        @SerializedName("category_id")
        public int CategoryId;

        @SerializedName("price")
        public double Price;

        @SerializedName("maxprice")
        public double Maxprice;

        @SerializedName("quantity")
        public int Quantity;

        @SerializedName("description")
        public String Description;

        @SerializedName("province_id")
        public int ProvinceId;

        @SerializedName("city_id")
        public int CityId;

        @SerializedName("area_id")
        public int AreaId;

        @SerializedName("address")
        public String Address;

        @SerializedName("start_date")
        public String StartDate;

        @SerializedName("start_hour")
        public int StartTime;

        @SerializedName("end_date")
        public String EndDate;

        @SerializedName("end_hour")
        public int EndTime;

        @SerializedName("security_7days")
        public int Security7days;

        @SerializedName("security_delivery")
        public int SecurityDelivery;

        @SerializedName("status")
        public int Status;

        @SerializedName("pics[]")
        public Integer[] Pics;

        @SerializedName("movie")
        public int Movie;

        @SerializedName("movie_thumb_id")
        public int MovieThumbId;
    }

    @HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Product/edit", method = Method.Post, encoder = WebFormEncoder.class)
    public static class ProductEditFreeRequest extends Endpoint {

        @SerializedName("type")
        public String Type;

        @SerializedName("title")
        public String Title;

        @SerializedName("in_special_panic")
        public String InSpecialPanic;

        @SerializedName("in_special_gift")
        public String InSpecialGift;

        @SerializedName("id")
        public long Id;

        @SerializedName("category_id")
        public int CategoryId;

        @SerializedName("price")
        public double Price;

        @SerializedName("quantity")
        public int Quantity;

        @SerializedName("province_id")
        public int ProvinceId;

        @SerializedName("city_id")
        public int CityId;

        @SerializedName("area_id")
        public int AreaId;

        @SerializedName("address")
        public String Address;

        @SerializedName("description")
        public String Description;

        @SerializedName("security_7days")
        public int Security7days;

        @SerializedName("security_delivery")
        public int SecurityDelivery;

        @SerializedName("status")
        public int Status;

        @SerializedName("pics[]")
        public Integer[] Pics;

        @SerializedName("movie")
        public int Movie;

        @SerializedName("movie_thumb_id")
        public int MovieThumbId;
    }
}
