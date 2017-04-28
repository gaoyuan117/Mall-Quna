
package com.android.juzbao.model;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.juzbao.activity.AuctionDetailActivity_;
import com.android.juzbao.activity.ProductDetailActivity_;
import com.android.juzbao.activity.SearchActivity_;
import com.android.juzbao.activity.jifen.EasyPeopleDetailActivity_;
import com.android.juzbao.application.BaseApplication;
import com.android.juzbao.constant.ProviderGlobalConst;
import com.android.juzbao.dao.ProductDao;
import com.android.mall.resource.R;
import com.android.zcomponent.adapter.CommonAdapter;
import com.android.zcomponent.common.uiframe.BaseActivity;
import com.android.zcomponent.http.HttpDataLoader;
import com.android.zcomponent.http.api.host.Endpoint;
import com.android.zcomponent.http.api.model.MessageData;
import com.android.zcomponent.http.constant.ErrorCode;
import com.android.zcomponent.json.JsonSerializerFactory;
import com.android.zcomponent.util.CommonUtil;
import com.android.zcomponent.util.ImageLoaderUtil;
import com.android.zcomponent.util.ListUtil;
import com.android.zcomponent.util.LogEx;
import com.android.zcomponent.util.ShowMsg;
import com.android.zcomponent.util.ShowMsg.IConfirmDialog;
import com.android.zcomponent.util.StringUtil;
import com.android.zcomponent.util.TimeUtil;
import com.android.zcomponent.util.ValidateUtil;
import com.android.zcomponent.views.DataEmptyView;
import com.android.zcomponent.views.HorizontialListView;
import com.android.zcomponent.views.imageslider.SliderLayout;
import com.android.zcomponent.views.imageslider.SliderLayout.OnImageItemClickListener;
import com.android.zcomponent.views.imageslider.SliderLayout.OnViewCreatedListener;
import com.easemob.easeui.simple.EaseHelper;
import com.hyphenate.chatui.ChatActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.server.api.model.AdProduct;
import com.server.api.model.PaincTimes;
import com.server.api.model.PaincTimes.Data;
import com.server.api.model.ProductItem;
import com.server.api.model.ProductOptions;
import com.server.api.model.ProductOptions.ProductOption;
import com.server.api.model.ProductOptions.ProductOptionItem;
import com.server.api.model.ProductOptions.ProductPrices;
import com.server.api.model.jifenmodel.IndexBanner;
import com.server.api.service.ProductService;
import com.server.api.service.ProductService.ProductAddFreeRequest;
import com.server.api.service.ProductService.ProductAddPhotoBuyRequest;
import com.server.api.service.ProductService.ProductEditFreeRequest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ProductBusiness {
    public static void queryShopInfo(HttpDataLoader httpDataLoader,
                                     String shopId) {
        ProductDao.sendCmdQueryShopInfo(httpDataLoader, shopId);
    }

    public static void queryAdProducts(HttpDataLoader httpDataLoader,
                                       String identifier) {
        ProductDao.sendCmdQueryAdProducts(httpDataLoader, identifier);
    }


    public static void queryIndexRecom(HttpDataLoader httpDataLoader, String index_recom) {
        ProductDao.sendCmdQueryIndexRecomAdProducts(httpDataLoader, index_recom);
    }

    public static void queryRecomendProducts(HttpDataLoader httpDataLoader) {
        ProductService.RecommandProductRequest request =
                new ProductService.RecommandProductRequest();
        httpDataLoader.doPostProcess(request,
                com.server.api.model.ProductRecomend.class);
    }

    public static void queryNotice(HttpDataLoader httpDataLoader) {
        ProductService.NoticeRequest request =
                new ProductService.NoticeRequest();
        request.Page = 1;
        request.Pagesize = Integer.MAX_VALUE;
        httpDataLoader.doPostProcess(request,
                com.server.api.model.HomeNotice.class);
    }

    public static void queryNoticeDetail(HttpDataLoader httpDataLoader, String id) {
        ProductService.NoticeDetailRequest request =
                new ProductService.NoticeDetailRequest();
        request.Id = id;
        httpDataLoader.doPostProcess(request,
                com.server.api.model.HomeNoticeDetail.class);
    }

    public static void queryGroup(HttpDataLoader httpDataLoader, String group) {
        ProductService.GroupRequest request =
                new ProductService.GroupRequest();
        request.Group = group;
        httpDataLoader.doPostProcess(request,
                com.server.api.model.ProductCategory.class);
    }

    public static void queryHomeProducts(HttpDataLoader httpDataLoader) {
        ProductDao.sendCmdQueryHomeProducts(httpDataLoader);
    }

    public static void queryProducts(HttpDataLoader httpDataLoader,
                                     String categoryId, String type, String sort, int page) {
        ProductDao.sendCmdQueryProducts(httpDataLoader, categoryId, type, sort, page);
    }

    public static void queryProducts(HttpDataLoader httpDataLoader,
                                     String categoryId, String type, int page) {
        ProductDao.sendCmdQueryProducts(httpDataLoader, categoryId, type, "", page);
    }

    public static void queryShopProducts(HttpDataLoader httpDataLoader,
                                         int shopId, int page) {
        ProductDao.sendCmdQueryShopProducts(httpDataLoader, shopId, page);
    }

    public static void queryShopProductSearch(HttpDataLoader httpDataLoader,
                                              int shopId, int page, String keyword) {
        ProductDao.sendCmdQueryShopProductSearch(httpDataLoader, shopId, page, keyword);
    }

    public static void queryProductOptions(HttpDataLoader httpDataLoader,
                                           String productId) {
        ProductDao.sendCmdQueryProductOptions(httpDataLoader, productId);
    }

    public static void querySearchProducts(HttpDataLoader httpDataLoader,
                                           String type, String keyword, int page) {
        ProductDao.sendCmdQuerySearchProducts(httpDataLoader, type, keyword, page);
    }

    public static void queryGiftCategory(HttpDataLoader httpDataLoader) {
        ProductDao.sendCmdQueryGiftCategory(httpDataLoader);
    }

    public static void queryPaipinSignup(HttpDataLoader httpDataLoader,
                                         long productId) {
        ProductDao.sendCmdQueryPaipinSignup(httpDataLoader, productId);
    }

    public static void queryPaipinSignupMoney(HttpDataLoader httpDataLoader,
                                              long productId, BigDecimal money) {
        ProductDao.sendCmdQueryPaipinSignupMoney(httpDataLoader, productId, money);
    }

    public static void queryPaipinSignupLog(HttpDataLoader httpDataLoader,
                                            long productId, int page) {
        ProductDao.sendCmdQueryPaipinSignupLog(httpDataLoader, productId, page);
    }

    public static void queryPaipinBondNotice(HttpDataLoader httpDataLoader) {
        ProductDao.sendCmdQueryPaipinBondNotice(httpDataLoader);
    }

    public static void queryGiftProducts(HttpDataLoader httpDataLoader,
                                         int categoryId, int giftId, int page) {
        ProductDao.sendCmdQueryGiftProducts(httpDataLoader, categoryId, giftId,
                page);
    }

    public static void queryPanicProducts(HttpDataLoader httpDataLoader,
                                          int categoryId, int sortDiscount, long panicId, long
                                                  endTime,
                                          int page) {
        ProductDao.sendCmdQueryPanicProducts(httpDataLoader, categoryId,
                sortDiscount, panicId, endTime, page);
    }

    public static void queryAuctionProducts(HttpDataLoader httpDataLoader,
                                            int categoryId, int isDelicacy, int startTime, int
                                                    endTime,
                                            int priceStart, int page) {
        ProductDao.sendCmdQueryAuctionProducts(httpDataLoader, categoryId,
                isDelicacy, startTime, endTime, priceStart, page);
    }

    public static void queryProduct(HttpDataLoader httpDataLoader, int id) {
        ProductDao.sendCmdQueryProduct(httpDataLoader, id);
    }

    public static void queryPaincTime(HttpDataLoader httpDataLoader, String type) {
        ProductDao.sendCmdQueryPaincTime(httpDataLoader, type);
    }

    public static boolean addPhotoBuyProduct(Context context,
                                             HttpDataLoader httpDataLoader,
                                             ProductAddPhotoBuyRequest request) {
        if (null == request) {
            return false;
        }

        if (TextUtils.isEmpty(request.Title)) {
            ShowMsg.showToast(context, "请填写商品标题");
            return false;
        }

        if (request.CategoryId < 0) {
            ShowMsg.showToast(context, "请选择商品分类");
            return false;
        }

        if (request.PriceEnd <= 0 || request.PriceStart <= 0) {
            ShowMsg.showToast(context, "请输入价格区间");
            return false;
        }

        if (TextUtils.isEmpty(request.Description)) {
            ShowMsg.showToast(context, "请填写商品描述");
            return false;
        }

        if (request.SecurityDelivery <= 0) {
            ShowMsg.showToast(context, "请选择发货时间");
            return false;
        }

        ProductDao.sendCmdQueryAddPhotoByProduct(httpDataLoader, request);
        return true;
    }

    public static boolean addFreeProduct(Context context,
                                         HttpDataLoader httpDataLoader, ProductAddFreeRequest
                                                 request) {
        if (null == request) {
            return false;
        }

        if (TextUtils.isEmpty(request.Title)) {
            ShowMsg.showToast(context, "请填写需求标题");
            return false;
        }

        if (request.CategoryId <= 0) {
            ShowMsg.showToast(context, "请选择需求分类");
            return false;
        }

        if (request.Price <= 0) {
            ShowMsg.showToast(context, "请输入价格");
            return false;
        }

        if (TextUtils.isEmpty(request.Description)) {
            ShowMsg.showToast(context, "请填写需求描述");
            return false;
        }

//		if (request.ProvinceId <= 0)
//		{
//			ShowMsg.showToast(context, "请发货地区");
//			return false;
//		}
//
//		if (TextUtils.isEmpty(request.Address))
//		{
//			ShowMsg.showToast(context, "请输入详细地址");
//			return false;
//		}
//
//		if (request.SecurityDelivery <= 0)
//		{
//			ShowMsg.showToast(context, "请选择发货时间");
//			return false;
//		}

        ProductDao.sendCmdQueryAddFreeProduct(httpDataLoader, request);
        return true;
    }

    public static boolean editFreeProduct(Context context,
                                          HttpDataLoader httpDataLoader, ProductEditFreeRequest
                                                  request) {
        if (null == request) {
            return false;
        }

        if (TextUtils.isEmpty(request.Title)) {
            ShowMsg.showToast(context, "请填写需求标题");
            return false;
        }

        if (request.CategoryId < 0) {
            ShowMsg.showToast(context, "请选择需求分类");
            return false;
        }

        if (request.Price <= 0) {
            ShowMsg.showToast(context, "请输入价格");
            return false;
        }

        if (TextUtils.isEmpty(request.Description)) {
            ShowMsg.showToast(context, "请填写需求描述");
            return false;
        }

//		if (request.ProvinceId <= 0)
//		{
//			ShowMsg.showToast(context, "请发货地区");
//			return false;
//		}
//
//		if (TextUtils.isEmpty(request.Address))
//		{
//			ShowMsg.showToast(context, "请输入详细地址");
//			return false;
//		}
//
//		if (request.SecurityDelivery <= 0)
//		{
//			ShowMsg.showToast(context, "请选择发货时间");
//			return false;
//		}

        ProductDao.sendCmdQueryEditFreeProduct(httpDataLoader, request);
        return true;
    }

    public static SliderLayout bindSliderLayout(final FragmentActivity context,
                                                int sliderId, final AdProduct.AdItem[] adItems) {
        final SliderLayout mSliderLayout =
                SliderLayout.bindSliderLayout(context, sliderId);
        mSliderLayout.setOnViewCreatedListener(new OnViewCreatedListener() {

            @Override
            public void onViewCreated() {
                if (null == adItems) {
                    return;
                }
                List<String> temp = new ArrayList<String>();
                for (AdProduct.AdItem adItem : adItems) {
                    temp.add(Endpoint.HOST + adItem.image);
                }
                mSliderLayout.setData(temp, true, true);
            }
        });

        mSliderLayout
                .setOnImageItemClickListener(new OnImageItemClickListener() {

                    @Override
                    public void onImageItemClick(int postion, View imageView) {
                        if (null == adItems) {
                            return;
                        }
                        if (adItems[postion].id > 0) {
                            ProductBusiness.intentToProductDetailActivity(
                                    context, null, adItems[postion].id);
                        }
                    }
                });

        return mSliderLayout;
    }

    public static SliderLayout bindSliderLayout(final Fragment context,
                                                int sliderId, final AdProduct.AdItem[] adItems) {
        final SliderLayout mSliderLayout =
                SliderLayout.bindSliderLayout(context, sliderId);
        mSliderLayout.setOnViewCreatedListener(new OnViewCreatedListener() {

            @Override
            public void onViewCreated() {
                if (null == adItems) {
                    return;
                }
                List<String> temp = new ArrayList<String>();
                for (AdProduct.AdItem adItem : adItems) {
                    temp.add(Endpoint.HOST + adItem.image);
                }
                mSliderLayout.setData(temp, true, true);
            }
        });

        mSliderLayout
                .setOnImageItemClickListener(new OnImageItemClickListener() {

                    @Override
                    public void onImageItemClick(int postion, View imageView) {
                        if (null == adItems) {
                            return;
                        }
                        if (!TextUtils.isEmpty(adItems[postion].product_id)) {
                            ProductBusiness.intentToProductDetailActivity(
                                    context.getActivity(),
                                    null,
                                    Integer.parseInt(adItems[postion].product_id));
                        }
                    }
                });
        return mSliderLayout;
    }

    public static void bindIndexBannderLayout(final Fragment homeFragment,
                                              int flayout_slider_image, final IndexBanner.Data[]
                                                      data) {
        final SliderLayout sliderLayout = SliderLayout.bindSliderLayout(homeFragment,
                flayout_slider_image);
        sliderLayout.setOnViewCreatedListener(new OnViewCreatedListener() {
            @Override
            public void onViewCreated() {
                if (null == data) {
                    return;
                }
                List<String> temp = new ArrayList<String>();
                for (IndexBanner.Data data_temp : data) {
                    temp.add(data_temp.image);
                }
                sliderLayout.setData(temp, true, true);
            }
        });

        sliderLayout.setOnImageItemClickListener(new OnImageItemClickListener() {
            @Override
            public void onImageItemClick(int postion, View imageView) {
                LogEx.e("SlideLayout", "onImageItemClick");
                if (null == data) {
                    return;
                }
                if (!TextUtils.isEmpty(data[postion].id)) {
                    Bundle bundle = new Bundle();
                    bundle.putString("id", data[postion].id);
                    bundle.putBoolean("isFromBanner", true);
                    ((BaseActivity) homeFragment.getActivity()).getIntentHandle().intentToActivity(
                            bundle, EasyPeopleDetailActivity_.class
                    );
                }
            }
        });
    }

    public static String getHtmlData(String bodyHTML) {
        String head =
                "<head>"
                        + "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, " +
                        "user-scalable=no\"> "
                        + "<style>img{max-width: 100%; width:auto; height:auto;}</style>"
                        + "</head>";
        return "<html>" + head + "<body>" + bodyHTML + "</body></html>";
    }

    public static void formatPaincingTime(List<PaincTimes.Data> listPaincTimes)
            throws Exception {
        if (null == listPaincTimes) {
            return;
        }
        String currentDate =
                TimeUtil.transformTimeFormat(Endpoint.serverDate(),
                        TimeUtil.STR_FORMAT_DATE);

        for (int i = 0; i < listPaincTimes.size(); i++) {
            PaincTimes.Data paincTime = listPaincTimes.get(i);
            long startTime = Long.parseLong(paincTime.start_time) * 1000;
            long endTime = Long.parseLong(paincTime.end_time) * 1000;

            String strStartTime =
                    TimeUtil.transformLongTimeFormat(startTime,
                            TimeUtil.STR_FORMAT_HOUR_MINUTE);
            String strEndTime =
                    TimeUtil.transformLongTimeFormat(endTime,
                            TimeUtil.STR_FORMAT_HOUR_MINUTE);

            Date newStartDate =
                    TimeUtil.getDateByStrDate(currentDate + " " + strStartTime,
                            TimeUtil.STR_FORMAT_DATE_TIME2);
            Date newEndDate =
                    TimeUtil.getDateByStrDate(currentDate + " " + strEndTime,
                            TimeUtil.STR_FORMAT_DATE_TIME2);

            paincTime.start_time = "" + newStartDate.getTime() / 1000;
            paincTime.end_time = "" + newEndDate.getTime() / 1000;

            listPaincTimes.set(i, paincTime);
        }

        Collections.sort(listPaincTimes, new PaincTimeComparator());
    }

    private static class PaincTimeComparator implements
            Comparator<PaincTimes.Data> {

        @Override
        public int compare(Data lhs, Data rhs) {
            Long time1 = Long.parseLong(lhs.start_time);
            Long time2 = Long.parseLong(rhs.start_time);

            return time1.compareTo(time2);
        }
    }

    public static int getCurrentPaincingTime(List<PaincTimes.Data> response) {
        if (null == response || response.size() == 0) {
            return 0;
        }

        int current = 0;

        for (int i = 0; i < response.size(); i++) {
            PaincTimes.Data paincTime = response.get(i);
            try {
                long serverTime = Endpoint.serverDate().getTime();
                long startTime = Long.parseLong(paincTime.start_time) * 1000;
                long endTime = Long.parseLong(paincTime.end_time) * 1000;

                if (serverTime >= startTime) {
                    current = i;
                }

                if (serverTime >= startTime && serverTime < endTime) {
                    return i;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return current;
    }

    public static int getDiffTime(String strEndTime) {
        long serverTime = Endpoint.serverDate().getTime() / 1000;
        long endTime = Long.parseLong(strEndTime);

        if (serverTime > endTime) {
            return 0;
        }

        return (int) (endTime - serverTime);
    }

    public static long getAuctionPaincingStartTime(
            List<PaincTimes.Data> response) {
        long panicId = 0;
        if (null == response || response.size() == 0) {
            return panicId;
        }

        long dif = 0;
        for (int i = 0; i < response.size(); i++) {
            PaincTimes.Data paincTime = response.get(i);
            try {
                long serverTime = Endpoint.serverDate().getTime();
                long startTime = Long.parseLong(paincTime.start_time) * 1000;
                if (startTime > serverTime) {
                    long tempDif = startTime - serverTime;
                    if (tempDif < dif) {
                        dif = tempDif;
                        panicId = Long.parseLong(paincTime.id);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return panicId;
    }

    public static void intentToChartActivity(final Activity context, String account) {
        if (EaseHelper.getInstance().isLoggedIn()) {
            if (!TextUtils.isEmpty(account)) {
                ChatActivity.startChart(context, account);
            } else {
                ShowMsg.showConfirmDialog(context, new IConfirmDialog() {

                    @Override
                    public void onConfirm(boolean confirmValue) {
                        if (confirmValue) {
                            ChatActivity.startChart(context,
                                    ProviderGlobalConst.IM_ACCOUNT);
                        }
                    }
                }, "联系趣那客服", "取消", "该商户暂未设置在线客服，您可以与聚真宝客服联系，我们将尽快反馈给商户！");
            }
        } else {
            BaseApplication.intentToLoginActivity(context);
        }
    }

    public static void intentToSearchActivity(Context context, String type) {
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        ((BaseActivity) context).getIntentHandle().intentToActivity(bundle,
                SearchActivity_.class, true);
    }

    public static void intentToProductDetailActivity(Context context,
                                                     ProductItem product, int productId) {
        Bundle bundle = new Bundle();
        if (null != product) {
            bundle.putString("product", JsonSerializerFactory.Create().encode(product));
        }
        bundle.putInt("id", productId);
        ((BaseActivity) context).getIntentHandle().intentToActivity(bundle, ProductDetailActivity_
                .class);
    }

    public static void intentToPaipinProductDetailActivity(Context context,
                                                           ProductItem product, int productId) {
        Bundle bundle = new Bundle();
        if (null != product) {
            bundle.putString("product",
                    JsonSerializerFactory.Create().encode(product));
        }
        bundle.putInt("id", productId);
        ((BaseActivity) context).getIntentHandle().intentToActivity(bundle,
                AuctionDetailActivity_.class);
    }

    public static void showProductAttribute(Context context, TextView tvew, String state, int resOff,
                                            int resOn) {
        if (!"1".equals(state)) {
            tvew.setTextColor(context.getResources().getColor(R.color.gray));
            CommonUtil.setDrawableLeft(context, tvew, resOff);
        } else {
            tvew.setTextColor(context.getResources().getColor(R.color.black));
            CommonUtil.setDrawableLeft(context, tvew, resOn);
        }
    }

    public static boolean validateImageUrl(String imageUrl) {
        if (!TextUtils.isEmpty(imageUrl) && !"false".equals(imageUrl)) {
            return true;
        }

        return false;
    }

    public static boolean validateQueryProducts(
            com.server.api.model.Products responseProduct) {
        if (null != responseProduct) {
            if (null != responseProduct.Data
                    && null != responseProduct.Data.Results
                    && responseProduct.Data.Results.length > 0) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public static String validateQueryState(
            com.server.api.model.Products responseProduct,
            String faileMessage) {
        if (null != responseProduct) {
            if (responseProduct.code != ErrorCode.INT_QUERY_DATA_SUCCESS) {
                if (TextUtils.isEmpty(responseProduct.message)) {
                    return faileMessage;
                }
                return responseProduct.message;
            } else {
                return faileMessage;
            }
        } else {
            return faileMessage;
        }
    }

    public static boolean validateQueryTimes(PaincTimes response,
                                             DataEmptyView dataEmptyView, MessageData msg, String
                                                     emptyMessage) {
        if (null != response) {
            if (response.code == ErrorCode.INT_QUERY_DATA_SUCCESS) {
                if (!ValidateUtil.isArrayEmpty(response.Data)) {
                    return true;
                } else {
                    dataEmptyView.showViewDataEmpty(false, false, msg,
                            emptyMessage);
                    return false;
                }
            } else {
                dataEmptyView.showViewDataEmpty(false, false, msg,
                        response.message);
                return false;
            }
        } else {
            dataEmptyView.showViewDataEmpty(false, false, msg, emptyMessage);
            return false;
        }
    }

    public static class ProductNumSelect extends PopupWindow {
        private ProductItem mProduct;
        private Context mContext;
        private LinearLayout mLlayoutBg;
        private TextView mTvewProductPrice;

        private int iSelectProductCount = 1;

        private OnProductNumSelectorListenre mOnProductNumSelectorListenre;

        public interface OnProductNumSelectorListenre {
            void onClickAddCart(int num);

            void onClickAddToOrder(int num);
        }

        public void setOnProductNumSelectorListenre(OnProductNumSelectorListenre
                                                            onProductNumSelectorListenre) {
            mOnProductNumSelectorListenre = onProductNumSelectorListenre;
        }

        public ProductNumSelect(Context context, ProductItem product) {
            super(LayoutInflater.from(context).inflate(R.layout.pop_product_option, null),
                    LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);
            mContext = context;
            mProduct = product;
            initPricePopup();
        }

        private void initPricePopup() {
            this.setBackgroundDrawable(new BitmapDrawable());
            this.setFocusable(true);
            this.setTouchable(true);
            this.setOutsideTouchable(true);

            View view = this.getContentView();
            mLlayoutBg = (LinearLayout) view.findViewById(R.id.llayout_bg_show);
            mLlayoutBg.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
            this.setOnDismissListener(new OnDismissListener() {
                @Override
                public void onDismiss() {
                    mLlayoutBg.setBackgroundResource(R.drawable.transparent);
                }
            });
            Button btnAddCart =
                    (Button) view.findViewById(R.id.btn_add_cart_click);
            Button btnAddOrder =
                    (Button) view.findViewById(R.id.btn_add_order_click);

            btnAddCart.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnProductNumSelectorListenre != null) {
                        mOnProductNumSelectorListenre.onClickAddCart(getSelectCount());
                    }
                    dismiss();
                }
            });

            btnAddOrder.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnProductNumSelectorListenre != null) {
                        mOnProductNumSelectorListenre.onClickAddToOrder(getSelectCount());
                    }
                    dismiss();
                }
            });
            initProductNumSelect(view);
            initProduct(view, mProduct);
        }

        private void initProduct(View view, ProductItem product) {
            if (null == product) {
                return;
            }
            ImageView imgvewPhoto = (ImageView) view.findViewById(R.id.imgvew_photo_show);
            TextView tvewProductQuantity = (TextView) view.findViewById(R.id.tvew_product_quantity_show);
            mTvewProductPrice = (TextView) view.findViewById(R.id.tvew_product_now_price_show);

            DisplayImageOptions options = ImageLoaderUtil.getDisplayImageOptions(R.drawable
                    .img_empty_logo_small);
            ImageLoader.getInstance().displayImage(Endpoint.HOST + product.image, imgvewPhoto, options);

            tvewProductQuantity.setText("库存" + product.quantity + "件");
            mTvewProductPrice.setText("¥" + StringUtil.formatProgress(product.price));
        }

        private void initProductNumSelect(View view) {
            final TextView tvewSelectNum = (TextView) view.findViewById(R.id.tvew_select_num_show);

            TextView tvewSubProduct =
                    (TextView) view.findViewById(R.id.tvew_sub_product_show);
            TextView tvewAddProduct =
                    (TextView) view.findViewById(R.id.tvew_add_product_show);

            tvewSelectNum.setText("" + iSelectProductCount);

            tvewSubProduct.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (iSelectProductCount > 1) {
                        iSelectProductCount--;
                    }
                    tvewSelectNum.setText("" + iSelectProductCount);
                }
            });

            tvewAddProduct.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    iSelectProductCount++;
                    tvewSelectNum.setText("" + iSelectProductCount);
                }
            });

        }

        public void showWindowBottom(View view) {
            if (!this.isShowing()) {
                if (null != mLlayoutBg) {
                    mLlayoutBg.setBackgroundResource(R.drawable.transparent_background);
                }
                this.showAtLocation(view, Gravity.BOTTOM, 0, 0);
                this.update();
            } else {

            }
        }

        public void dismissWindow() {
            this.dismiss();
        }

        private int getSelectCount() {
            return iSelectProductCount;
        }
    }

    /**
     * 订单相关popupwindow
     */
    public static class ProductOptionSelect extends PopupWindow {

        private ProductOptions mProductOptions;

        private ProductItem mProduct;

        private Context mContext;

        private LinearLayout mLlayoutOptions;

        private LinearLayout mLlayoutBg;

        private TextView mTvewSelectOption;

        private TextView mTvewProductPrice;

        private List<OptionItemAdapter> mOptionAdapters;

        private int iSelectProductCount = 1;

        private Map<Integer, ProductOptionItem> mSelectOptions = new LinkedHashMap<>();

        private OnOptionSelectListener mOnOptionSelectListener;

        public ProductOptionSelect(Context context, ProductOptions options,
                                   ProductItem product) {
            super(LayoutInflater.from(context).inflate(R.layout.pop_product_option, null),
                    LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);
            this.mContext = context;
            mProductOptions = options;
            mProduct = product;
            initOptionWindow();
        }

        private void initOptionWindow() {
            this.setBackgroundDrawable(new BitmapDrawable());
            this.setFocusable(true);
            this.setTouchable(true);
            this.setOutsideTouchable(true);

            View view = this.getContentView();

            mLlayoutOptions =
                    (LinearLayout) view
                            .findViewById(R.id.llayout_product_option);
            mLlayoutBg = (LinearLayout) view.findViewById(R.id.llayout_bg_show);
            mLlayoutBg.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
            this.setOnDismissListener(new OnDismissListener() {

                @Override
                public void onDismiss() {
                    mLlayoutBg.setBackgroundResource(R.drawable.transparent);
                }
            });

            Button btnAddCart = (Button) view.findViewById(R.id.btn_add_cart_click);
            Button btnAddOrder = (Button) view.findViewById(R.id.btn_add_order_click);

            btnAddCart.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    String optionTitle = getUnSelectOptionTitle();

//          if (!TextUtils.isEmpty(optionTitle)) {
//            ShowMsg.showToast(mContext, "请选择 " + optionTitle);
//            return;
//          }
                    if (null != mOnOptionSelectListener) {
                        mOnOptionSelectListener.onClickOptionAddCart(getSelectOptionId(),
                                getSelectProductCount());
                    }

                    dismissWindow();
                }
            });

            btnAddOrder.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    String optionId = getUnSelectOptionTitle();

//          if (!TextUtils.isEmpty(optionId)) {
//            ShowMsg.showToast(mContext, "请选择 " + optionId);
//            return;
//          }

                    if (null != mOnOptionSelectListener) {
                        mOnOptionSelectListener.onClikcOptionAddOrder(getSelectOptionId(), getSelectProductCount());
                    }
                    dismissWindow();
                }
            });

            initProductOption(mProductOptions.data.option,
                    mProductOptions.data.option_price);
            initProductNumSelect(view);
            initProduct(view, mProduct);
        }

        public interface OnOptionSelectListener {

            public void onClickOptionAddCart(String optionId, int productNum);

            public void onClikcOptionAddOrder(String optionId, int productNum);
        }

        public void setOnOptionSelectListener(OnOptionSelectListener onOptionSelectListener) {
            mOnOptionSelectListener = onOptionSelectListener;
        }

        private void initProductNumSelect(View view) {
            final TextView tvewSelectNum = (TextView) view.findViewById(R.id.tvew_select_num_show);

            TextView tvewSubProduct = (TextView) view.findViewById(R.id.tvew_sub_product_show);
            TextView tvewAddProduct = (TextView) view.findViewById(R.id.tvew_add_product_show);

            tvewSelectNum.setText("" + iSelectProductCount);

            tvewSubProduct.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (iSelectProductCount > 1) {
                        iSelectProductCount--;
                    }
                    tvewSelectNum.setText("" + iSelectProductCount);
                }
            });

            tvewAddProduct.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    iSelectProductCount++;
                    tvewSelectNum.setText("" + iSelectProductCount);
                }
            });

        }

        private void initProductOption(
                ProductOptions.ProductOption[] productOption,
                ProductOptions.ProductPrices[] productPrices) {
            if (null == productOption) {
                return;
            }

            mOptionAdapters = new ArrayList<OptionItemAdapter>();

            //显示每个属性组合，每个属性组合对应不同子属性.
            for (int i = 0; i < productOption.length; i++) {
                View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_product_option, null);
                TextView tvewTitle = (TextView) view.findViewById(R.id.tvew_option_title_show);
                HorizontialListView gvewOptions = (HorizontialListView) view.findViewById(R.id
                        .gridview_options_show);
                tvewTitle.setText(productOption[i].title);

                final List<ProductOptionItem> optionItems = ListUtil.arrayToList(productOption[i]._child);
                initOptionItemEnableState(i, optionItems, productPrices);

                final OptionItemAdapter adapter = new OptionItemAdapter(mContext, optionItems);
                gvewOptions.setAdapter(adapter);
                gvewOptions.setOnItemClickListener(new OptionsItemClickListener(adapter, i));
                mOptionAdapters.add(adapter);
                mLlayoutOptions.addView(view);
            }
        }

        private void initProduct(View view, ProductItem product) {
            if (null == product) {
                return;
            }
            ImageView imgvewPhoto = (ImageView) view.findViewById(R.id.imgvew_photo_show);

            TextView tvewProductQuantity = (TextView) view.findViewById(R.id.tvew_product_quantity_show);
            mTvewProductPrice = (TextView) view.findViewById(R.id.tvew_product_now_price_show);

            mTvewSelectOption = (TextView) view.findViewById(R.id.tvew_product_option_show);
            DisplayImageOptions options = ImageLoaderUtil.getDisplayImageOptions(R.drawable
                    .img_empty_logo_small);
            ImageLoader.getInstance().displayImage(Endpoint.HOST + product.image, imgvewPhoto, options);

            tvewProductQuantity.setText("库存" + product.quantity + "件");
            mTvewProductPrice.setText("¥" + StringUtil.formatProgress(product.price));
            mTvewSelectOption.setText("选择  " + getOptionTitle());
        }

        private void initOptionItemEnableState(int groupPosition, List<ProductOptionItem>
                optionItems, ProductPrices[] productPrices) {

            if (null == productPrices || null == optionItems) {
                return;
            }

            //遍历每个子属性。
            for (int i = 0; i < optionItems.size(); i++) {
                ProductOptionItem productOption = optionItems.get(i);
                productOption.isEnable = false;
                //遍历所有属性价格。
                for (int j = 0; j < productPrices.length; j++) {
                    String[] optionId = productPrices[j].product_option_id.split(",");
                    //保证这个属性是对的。如果有三个选项分类，那么属性价格的长度必须大于等于三。
                    if (optionId.length > groupPosition) {
                        if (productOption.id.equals(optionId[groupPosition])) {
                            productOption.isEnable = true;
                        }
                    }
                }
            }
        }

        private void setOptionItemEnableState(int groupPosition,
                                              List<OptionItemAdapter> optionAdapters,
                                              ProductPrices[] productPrices, ProductOptionItem
                                                      selectOption) {

            for (int i = 0; i < optionAdapters.size(); i++) {
                if (i != groupPosition) {
                    List<ProductOptionItem> optionItems = (List<ProductOptionItem>) optionAdapters.get(i)
                            .getData();

                    for (int j = 0; j < optionItems.size(); j++) {
                        ProductOptionItem productOption = optionItems.get(j);
                        productOption.isEnable = false;

                        boolean isEnable = false;

                        for (int k = 0; k < productPrices.length; k++) {
                            String[] optionId = productPrices[k].product_option_id.split(",");

                            if (null != selectOption) {
                                if (optionId.length > groupPosition && optionId[groupPosition].equals
                                        (selectOption.id)) {
                                    if (optionId.length > i && optionId[i].equals(productOption.id)) {
                                        isEnable = true;
                                        break;
                                    }
                                }
                            } else {
                                if (optionId[i].equals(productOption.id)) {
                                    isEnable = true;
                                    break;
                                }
                            }
                        }
                        productOption.isEnable = isEnable;
                    }
                }
                optionAdapters.get(i).notifyDataSetChanged();
            }
        }

        private class OptionsItemClickListener implements OnItemClickListener {

            private OptionItemAdapter mAdapter;

            private int groupPosition;

            public OptionsItemClickListener(OptionItemAdapter adapter,
                                            int groupPosition) {
                this.mAdapter = adapter;
                this.groupPosition = groupPosition;
            }

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (!mAdapter.isOptionbEnable(position)) {
                    return;
                }

                ProductOptionItem optionItem = (ProductOptionItem) mAdapter.getItem(position);

                if (mAdapter.getSelectPosition() == position) {
                    mAdapter.setSelectPosition(-1);
                    mSelectOptions.remove(groupPosition);
                    setOptionItemEnableState(groupPosition, mOptionAdapters, mProductOptions.data.option_price, null);
                } else {
                    mAdapter.setSelectPosition(position);
                    mSelectOptions.put(groupPosition, optionItem);
                    setOptionItemEnableState(groupPosition, mOptionAdapters, mProductOptions.data.option_price, optionItem);
                }
                showSelectOption(mSelectOptions);
            }
        }

        private String getSelectOptionParentTitle(
                Map<Integer, ProductOptionItem> selectOptions) {
            StringBuilder buffer = new StringBuilder();

            for (Iterator iterator = selectOptions.keySet().iterator(); iterator
                    .hasNext(); ) {
                ProductOptionItem optionItem = selectOptions.get(iterator.next());

                for (int i = 0; i < mProductOptions.data.option.length; i++) {
                    ProductOptionItem[] item = mProductOptions.data.option[i]._child;

                    for (int j = 0; j < item.length; j++) {
                        if (optionItem.id.equals(item[j].id)) {
                            if (null != optionItem) {
                                buffer.append(mProductOptions.data.option[i].title);
                                buffer.append(" ");
                            }
                        }
                    }
                }
            }

            return buffer.toString();
        }

        public String getSelectOption() {
            return getSelectOptionTitle(mSelectOptions);
        }

        private String getSelectOptionTitle(
                Map<Integer, ProductOptionItem> selectOptions) {
            StringBuilder buffer = new StringBuilder();
            for (Iterator iterator = selectOptions.keySet().iterator(); iterator.hasNext(); ) {
                ProductOptionItem optionItem = selectOptions.get(iterator.next());
                if (null != optionItem) {
                    buffer.append(optionItem.title);
                    buffer.append(" ");
                }
            }

            return buffer.toString();
        }

        private void showSelectOption(Map<Integer, ProductOptionItem> selectOptions) {
            String selectOptionTitle = getSelectOptionTitle(selectOptions);

            if (TextUtils.isEmpty(selectOptionTitle)) {
                mTvewSelectOption.setText("选择  " + getOptionTitle());
            } else {
                mTvewSelectOption.setText("已选 " + selectOptionTitle);
            }

            showSelectOptionPrice();
        }

        private String getUnSelectOptionTitle() {
            String selectOptionTitle = getSelectOptionParentTitle(mSelectOptions);

            String optionTitles = getOptionTitle();

            return optionTitles.replace(selectOptionTitle, "");
        }

        private void showSelectOptionPrice() {
            BigDecimal optionPrice = getSelectOptionPrice();

            if (null != optionPrice) {
                mTvewProductPrice.setText("¥" + StringUtil.formatProgress(optionPrice));
            } else {
                if (null != mProduct) {
                    mTvewProductPrice.setText("¥" + StringUtil.formatProgress(mProduct.price));
                }
            }
        }

        private boolean isSelectOptionComplete(
                Map<Integer, ProductOptionItem> selectOptions) {
            if (selectOptions.size() == mProductOptions.data.option.length) {
                return true;
            }

            return false;
        }

        private String getSelectOptionId(
                Map<Integer, ProductOptionItem> selectOptions) {
            StringBuilder buffer = new StringBuilder();

            int count = 0;
            Iterator<Integer> keySet = selectOptions.keySet().iterator();
            for (Iterator iterator = selectOptions.keySet().iterator(); iterator.hasNext(); ) {
                count++;
                ProductOptionItem optionItem = selectOptions.get(iterator.next());
                if (null != optionItem) {
                    buffer.append(optionItem.id);
                    if (count != selectOptions.size()) {
                        buffer.append(",");
                    }
                }
            }
            return buffer.toString();
        }

        public int getSelectOptionPosition() {
            String optionId = getSelectOptionId(mSelectOptions);

            ProductOptions.ProductPrices[] productPrices = mProductOptions.data.option_price;
            if(productPrices==null){
                return -1;
            }
            for (int i = 0; i < productPrices.length; i++) {
                if (optionId.equals(productPrices[i].product_option_id)) {
                    return i;
                }
            }
            return -1;
        }

        public String getSelectOptionId() {
            int position = getSelectOptionPosition();

            if (-1 != position) {
                return mProductOptions.data.option_price[position].product_option_id;
            }
            return "0";
        }

        public BigDecimal getSelectOptionPrice() {
            int position = getSelectOptionPosition();

            if (-1 != position) {
                return mProductOptions.data.option_price[position].price;
            }
            return null;
        }

        public int getSelectProductCount() {
            return iSelectProductCount;
        }

        public void showWindow(View view) {
            if (!this.isShowing()) {
                if (null != mLlayoutBg) {
                    mLlayoutBg
                            .setBackgroundResource(R.drawable.transparent_background);
                }

                this.showAtLocation(view, Gravity.CENTER, 0, 0);
                this.update();
            } else {

            }
        }

        /**
         * <p>
         * Description: 显示页面
         * <p>
         *
         * @param view
         * @date 2015-3-23
         * @author wei
         */
        public void showWindowBottom(View view) {
            if (!this.isShowing()) {
                if (null != mLlayoutBg) {
                    mLlayoutBg.setBackgroundResource(R.drawable.transparent_background);
                }
                this.showAtLocation(view, Gravity.BOTTOM, 0, 0);
                this.update();
            } else {

            }
        }

        /**
         * <p>
         * Description: 关不页面
         * <p>
         *
         * @date 2015-3-23
         * @author wei
         */
        public void dismissWindow() {
            this.dismiss();
        }

        public String getOptionTitle() {
            if (null == mProductOptions || null == mProductOptions.data
                    || null == mProductOptions.data.option) {
                return "";
            }

            StringBuffer buffer = new StringBuffer();

            for (int i = 0; i < mProductOptions.data.option.length; i++) {
                ProductOption productOption = mProductOptions.data.option[i];

                buffer.append(productOption.title);
                buffer.append(" ");
            }

            return buffer.toString();
        }
    }

    public static class OptionItemAdapter extends CommonAdapter {

        public OptionItemAdapter(Context context, List<?> list) {
            super(context, list);
        }

        @Override
        public View getView(final int position, View convertView,
                            ViewGroup parent) {
            if (null == convertView) {
                convertView =
                        layoutInflater.inflate(
                                R.layout.adapter_product_option_item, null);
            }

            TextView tvewTitle =
                    (TextView) convertView
                            .findViewById(R.id.tvew_option_title_show);

            ProductOptionItem optionItem =
                    (ProductOptionItem) mList.get(position);
            tvewTitle.setText(optionItem.title);

            if (optionItem.isEnable) {
                if (getSelectPosition() == position) {
                    tvewTitle
                            .setBackgroundResource(R.drawable.common_round_red_bg);
                    tvewTitle.setTextColor(getColor(R.color.white));
                } else {
                    tvewTitle
                            .setBackgroundResource(R.drawable.common_round_gray_bg);
                    tvewTitle.setTextColor(getColor(R.color.black));
                }
            } else {
                tvewTitle
                        .setBackgroundResource(R.drawable.common_round_gray_bg);
                tvewTitle.setTextColor(getColor(R.color.gray_pressed));
            }

            return convertView;
        }

        public boolean isOptionbEnable(int position) {
            return ((ProductOptionItem) mList.get(position)).isEnable;
        }
    }
}
