
package com.android.juzbao.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.juzbao.activity.jifen.JifenConvertActivity_;
import com.android.juzbao.activity.me.MyMessageActivity_;
import com.android.juzbao.activity.order.OrderEnsureActivity_;
import com.android.juzbao.application.BaseApplication;
import com.android.juzbao.dao.jifendao.JiFenDao;
import com.android.juzbao.model.CartBusiness;
import com.android.juzbao.model.FavoriteBusiness.FavoriteHelper;
import com.android.juzbao.model.ProductBusiness;
import com.android.juzbao.model.ProductBusiness.ProductOptionSelect;
import com.android.juzbao.model.ProductBusiness.ProductOptionSelect.OnOptionSelectListener;
import com.android.juzbao.model.circle.DynamicDetailBean;
import com.android.juzbao.util.CommonValidate;
import com.android.mall.resource.R;
import com.android.video.surfaceview.VideoPlayView;
import com.android.zcomponent.http.api.host.Endpoint;
import com.android.zcomponent.http.api.model.MessageData;
import com.android.zcomponent.http.constant.ErrorCode;
import com.android.zcomponent.json.JsonSerializerFactory;
import com.android.zcomponent.util.ListUtil;
import com.android.zcomponent.util.ShowMsg;
import com.android.zcomponent.util.StringUtil;
import com.android.zcomponent.util.share.ShareDialog;
import com.android.zcomponent.util.share.ShareReqParams;
import com.android.zcomponent.util.share.WechatHandle;
import com.android.zcomponent.views.MeasureWebview;
import com.android.zcomponent.views.OverScrollView;
import com.android.zcomponent.views.OverScrollView.OnScrollListener;
import com.android.zcomponent.views.imageslider.SliderLayout;
import com.android.zcomponent.views.imageslider.SliderLayout.OnViewCreatedListener;
import com.android.zcomponent.views.swipebacklayout.lib.app.SwipeBackActivity;
import com.easemob.easeui.simple.EaseMessageNotify;
import com.server.api.model.CartItem;
import com.server.api.model.CartPageResult;
import com.server.api.model.CartTradeResult;
import com.server.api.model.CommonReturn;
import com.server.api.model.Image;
import com.server.api.model.Product;
import com.server.api.model.ProductItem;
import com.server.api.model.ProductOptions;
import com.server.api.service.CartService;
import com.server.api.service.ProductService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Description: 商品详情
 * </p>
 *
 * @ClassName:ProductDetailActivity
 * @author: wei
 * @date: 2015-11-10
 */
@EActivity(R.layout.activity_product_detail)
public class ProductDetailActivity extends SwipeBackActivity implements
    OnOptionSelectListener {

  @ViewById(R.id.llayout_modify)
  LinearLayout llayout_modify;

  @ViewById(R.id.llayout_jfien)
  LinearLayout llayout_jfien;

  @ViewById(R.id.personal_message_dot_show)
  ImageView mPersonalMessageDot;

  @ViewById(R.id.llayout_product_qi_show)
  LinearLayout mLlayoutProductQi;

  @ViewById(R.id.llayout_product_qiang_show)
  LinearLayout mLlayoutProductQiang;

  @ViewById(R.id.rlayout_product_option_click)
  RelativeLayout mRlayoutOptions;

  @ViewById(R.id.tvew_product_name_show)
  TextView mTvewProductName;

  @ViewById(R.id.tvew_product_price_show)
  TextView mTvewProductPrice;

  @ViewById(R.id.tvew_product_origin_price_show)
  TextView mTvewProductOriginPrice;

  @ViewById(R.id.tvew_product_score_show)
  TextView mTvewProdcutScore;

  @ViewById(R.id.tvew_product_qi_show)
  TextView mTvewProductQi;

  @ViewById(R.id.tvew_product_qiang_show)
  TextView mTvewProductQiang;

  @ViewById(R.id.btn_favorite_click)
  TextView mTvewFavorite;

  @ViewById(R.id.tvew_option_show)
  TextView mTvewOption;

  @ViewById(R.id.btn_shop_click)
  TextView mTvewShop;

  @ViewById(R.id.tvew_top_click)
  ImageButton mBtnToTop;

  @ViewById(R.id.scrollview)
  OverScrollView mScrollView;

  MeasureWebview mWebviewProductDetail;

  private VideoPlayView mVideoPlayView;

  /**
   * 海报图片显示
   */
  private SliderLayout mSliderLayout;


  private ProductItem mProduct;

  private int iSelectProductCount = 1;

  public static int mProductId;

  private boolean isJifenProduct = false;

  private FavoriteHelper mFavoriteHelper;

  private String mstrProductOptions = "0";

  private ProductOptionSelect mOptionSelect;
  private ProductBusiness.ProductNumSelect mProductNumSelect;


  private boolean isAddToOrder = false;

  /**
   * 分享对话框
   */
  private ShareDialog mShareCustomDialog;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @AfterViews void initUI() {
    mScrollView.setOnScrollListener(new OnScrollListener() {

      @Override
      public void onScroll(int l, int t, int oldl, int oldt) {
        int scrollY = mScrollView.getScrollY();

        if (scrollY > 600) {
          mBtnToTop.setVisibility(View.VISIBLE);
        } else {
          mBtnToTop.setVisibility(View.GONE);
        }
      }
    });

    mWebviewProductDetail =
        (MeasureWebview) findViewById(R.id.webview_product_detail_show);
    mWebviewProductDetail.getSettings().setDefaultTextEncodingName("utf-8");

    Intent intent = getIntent();
    mProductId = intent.getIntExtra("id", 0);
    isJifenProduct = intent.getBooleanExtra("jifen", false);

    controlShowProduct(isJifenProduct);

    ProductBusiness.queryProduct(getHttpDataLoader(), mProductId);

    mFavoriteHelper = new FavoriteHelper(this, getHttpDataLoader());

    mFavoriteHelper.queryIsProductFavorited(true, mProductId);
    mFavoriteHelper.setProductId(mProductId);
    mFavoriteHelper.favoriteStateView(mTvewFavorite, R.drawable.icon25, R.drawable.icon26);

    EaseMessageNotify.getInstance().addView(mPersonalMessageDot);
    String strProduct = intent.getStringExtra("product");
    if (!TextUtils.isEmpty(strProduct)) {
      ProductItem product = JsonSerializerFactory.Create().decode(strProduct, ProductItem.class);
      showProductInfo(product);
    }
  }

  private void controlShowProduct(boolean isJifenProduct) {
    if (isJifenProduct) {
      llayout_modify.setVisibility(View.GONE);
      ((RelativeLayout.LayoutParams) mBtnToTop.getLayoutParams()).addRule(RelativeLayout.ABOVE, R
          .id.llayout_jfien);
      llayout_jfien.setVisibility(View.VISIBLE);
      mRlayoutOptions.setVisibility(View.GONE);
    } else {
      llayout_modify.setVisibility(View.VISIBLE);
      llayout_jfien.setVisibility(View.GONE);
    }
  }

  @Override
  protected void onResume() {
    super.onResume();
    EaseMessageNotify.getInstance().onResume();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    EaseMessageNotify.getInstance().removeView(mPersonalMessageDot);
  }

  private void bindSliderLayout() {
    mSliderLayout =
        SliderLayout.bindSliderLayout(this, R.id.flayout_slider_image);
    mSliderLayout.setOnViewCreatedListener(new OnViewCreatedListener() {

      @Override
      public void onViewCreated() {
        if (null != mProduct && null != mProduct.images) {
          List<String> temp = new ArrayList<String>();
          for (Image image : mProduct.images) {
            temp.add(Endpoint.HOST + image.path);
          }
          mSliderLayout.setData(temp, true, true);
        }
      }
    });
  }

  private void showShareCustomDialog() {
    if (null == mProduct) {
      return;
    }
    if (null == mShareCustomDialog) {
      mShareCustomDialog = new ShareDialog(this, R.layout.dialog_share1_layout);
    }
    if (!mShareCustomDialog.isShowing()) {
      mShareCustomDialog.showDialog();
      ShareReqParams params = new ShareReqParams();
      params.summary = "【" + mProduct.title + "】";
      String[] imageUrls = null;
      if (null != mProduct.images) {
        imageUrls = new String[mProduct.images.length];
        for (int i = 0; i < imageUrls.length; i++) {
          imageUrls[i] = Endpoint.HOST + mProduct.images[i].path;
        }
      } else {
        imageUrls = new String[1];
        imageUrls[0] = mProduct.image;
      }

      params.imageUrls = ListUtil.arrayToList(imageUrls);
      params.title = mProduct.title;
      params.shareUrl = Endpoint.HOST + mProduct.share_href;
      params.appName = getString(R.string.app_name);
      params.type = "4";
      mShareCustomDialog.setShareParams(params);
    }
  }

  private void showProductInfo(ProductItem item) {
    if (null == item) {
      return;
    }
    mProduct = item;
    bindSliderLayout();

    if (TextUtils.isEmpty(mProduct.image) && null != mProduct.images
        && mProduct.images.length > 0) {
      mProduct.image = mProduct.images[0].path;
    }

    if (TextUtils.isEmpty(mProduct.shop_id)) {
      mTvewShop.setVisibility(View.GONE);
    } else {
      mTvewShop.setVisibility(View.VISIBLE);
    }

    if (mProduct.price == null && isJifenProduct) {
      mTvewProductPrice.setText("¥" + mProduct.use_score + " 积分");

    } else {
      mTvewProductPrice.setText("¥" + mProduct.price);
      if (mProduct.score != null)
        mTvewProdcutScore.setText("赠送积分" + mProduct.score);
      if ("1".equals(mProduct.in_special_panic) && mProduct.special_panic != null) {
        String discount = "满" + mProduct.special_panic[0].condition_price + "打" + (Integer
            .parseInt(mProduct.special_panic[0].discount)) + "折";
        mTvewProductOriginPrice.setText(discount);

        mTvewProdcutScore.setVisibility(View.GONE);
        mTvewProductOriginPrice.setVisibility(View.VISIBLE);
      } else {
        mTvewProdcutScore.setVisibility(View.VISIBLE);
        mTvewProductOriginPrice.setVisibility(View.GONE);
      }
    }

    mTvewProductName.setText(mProduct.title);
    ProductBusiness.showProductAttribute(this, mTvewProductQi,
        mProduct.security_7days, R.drawable.product_detail_icon_qi_un,
        R.drawable.product_detail_icon_qi);

    ProductBusiness.showProductAttribute(this, mTvewProductQiang,
        mProduct.in_special_panic,
        R.drawable.product_detail_icon_qiang_un,
        R.drawable.product_detail_icon_qiang);

    if (!TextUtils.isEmpty(mProduct.description)) {
      String html = ProductBusiness.getHtmlData(mProduct.description);
      html = JiFenDao.getHTML(html);
      mWebviewProductDetail.loadData(html, "text/html; charset=utf-8", "utf-8");
    }

    if (null != mProduct.movie && (!"0".equals(mProduct.movie.id) && !TextUtils.isEmpty(mProduct
        .movie.id))) {
      String path = Endpoint.HOST + mProduct.movie.path;
      String thumbnail = Endpoint.HOST + mProduct.movie.thumb_image_path;
      loadVideo(path, thumbnail);
    }
  }

  private void loadVideo(String videoUrl, String thumbnail) {
    if (!StringUtil.isEmptyString(videoUrl)) {
      mVideoPlayView = (VideoPlayView) findViewById(R.id.video_play_view);
      mVideoPlayView.setVideoPath(videoUrl, false);
      mVideoPlayView.setVisibility(View.VISIBLE);
      if (!StringUtil.isEmptyString(thumbnail)) {
        mVideoPlayView.setVideoThumbnail(thumbnail);
      }
    }
  }

  @Override
  public void onLoginSuccess() {
    if (null != mProduct) {
      mFavoriteHelper.queryIsProductFavorited(true,
          Integer.parseInt(mProduct.id));
    }
  }

  @Override
  protected void onPause() {
    super.onPause();

    if (null != mVideoPlayView) {
      mVideoPlayView.pause();
    }
  }

  @Override
  public void onRecvMsg(MessageData msg) throws Exception {
    if (null != mFavoriteHelper) {
      mFavoriteHelper.onRecvMsg(msg);
    }

    if (msg.valiateReq(ProductService.ProductRequest.class)) {
      Product response = (Product) msg.getRspObject();

      if (null != response) {
        if (response.code == ErrorCode.INT_QUERY_DATA_SUCCESS) {
          if (response.Data.use_score != null && Integer.parseInt(response.Data.use_score) > 0) {
            isJifenProduct = true;
            controlShowProduct(isJifenProduct);
          }
          showProductInfo(response.Data);
        }
      }
      if (!isJifenProduct) {
        ProductBusiness.queryProductOptions(getHttpDataLoader(),
            String.valueOf(mProductId));
      }
    } else if (msg.valiateReq(ProductService.ProductOptionRequest.class)) {
      ProductOptions response = (ProductOptions) msg.getRspObject();
      if (CommonValidate.validateQueryState(this, msg, response)) {
        mOptionSelect =
            new ProductOptionSelect(this, response, mProduct);
        mOptionSelect.setOnOptionSelectListener(this);
        mTvewOption.setText("请选择 " + mOptionSelect.getOptionTitle());
        mRlayoutOptions.setVisibility(View.VISIBLE);
      } else {
        mProductNumSelect = new ProductBusiness.ProductNumSelect(this, mProduct);
        mProductNumSelect.setOnProductNumSelectorListenre(getOnProductNumSelectorListener());
        mRlayoutOptions.setVisibility(View.GONE);
        mRlayoutOptions.setVisibility(View.GONE);
      }
    } else if (msg.valiateReq(CartService.AddToCartRequest.class)) {
      CommonReturn response = msg.getRspObject();
      if (null != response) {
        if (response.code == ErrorCode.INT_QUERY_DATA_SUCCESS) {
          if (isAddToOrder) {
            CartBusiness.queryCarts(getHttpDataLoader(), 1);
          } else {
            ShowMsg.showToast(getApplicationContext(), "添加成功");
          }
        } else {
          ShowMsg.showToast(getApplicationContext(), response.message);
        }
      } else {
        ShowMsg.showToast(getApplicationContext(), "添加失败");
      }
    } else if (msg.valiateReq(CartService.QueryCartsRequest.class)) {
      CartPageResult response = msg.getRspObject();
      if (CommonValidate.validateQueryState(this, msg, response)) {
        if (null != response.Data && response.Data.Result.length > 0) {
          ArrayList<CartItem> listCartItems = new ArrayList<CartItem>();
          for (int i = 0; i < response.Data.Result.length; i++) {
            if (response.Data.Result[i].product[0].product_id.equals(mProduct.id)) {
              response.Data.Result[i].isSelect = true;
              response.Data.Result[i].product[0].isSelect = true;
              listCartItems.add(response.Data.Result[i]);
            }
          }
          Integer[] cartIds = CartBusiness.getAllSelectCartId(listCartItems);
          if (null != cartIds) {
            CartBusiness.toTrade(getHttpDataLoader(), cartIds);
          }
        } else {
          ShowMsg.showToast(this, msg, "添加失败");
        }
      } else {
        ShowMsg.showToast(this, msg, "添加失败");
      }
    } else if (msg.valiateReq(CartService.ToTradeRequest.class)) {
      CartTradeResult response = (CartTradeResult) msg.getRspObject();
      if (CommonValidate.validateQueryState(this, msg, response)) {
        Bundle bundle = new Bundle();
        bundle.putString("product", JsonSerializerFactory.Create().encode(response.Data));
        getIntentHandle().intentToActivity(bundle, OrderEnsureActivity_.class);
      } else {
        ShowMsg.showToast(this, msg, "添加失败");
      }
    }
  }


  @Click(R.id.btn_jifen_convert_click)
  void onClickJifenConvert() {
    if (!BaseApplication.isLogin()) {
      BaseApplication.intentToLoginActivity(this);
      return;
    }
    Bundle bundle = new Bundle();
    String product = JsonSerializerFactory.Create().encode(mProduct);
    bundle.putInt("id", mProductId);
    bundle.putString("product", product);
    getIntentHandle().intentToActivity(bundle, JifenConvertActivity_.class);
  }

  @Click(R.id.tvew_top_click)
  void onClickToTop() {
    mScrollView.smoothScrollTo(0, 0);
  }

  @Click({R.id.rlayout_message_click, R.id.personal_message_bg}) void onClickRlayoutMessage() {
    if (!BaseApplication.isLogin()) {
      BaseApplication.intentToLoginActivity(this);
      return;
    }

    Intent intent = new Intent(this, MyMessageActivity_.class);
    startActivity(intent);
  }

  @Click(R.id.rlayout_product_option_click)
  void onClickRlayoutOption() {
    if (null != mOptionSelect) {
      mOptionSelect.showWindowBottom(mRlayoutOptions);
    }
  }

  @Click(R.id.btn_add_cart_click)
  void onClickBtnAddCart() {

    if (!BaseApplication.isLogin()) {
      BaseApplication.intentToLoginActivity(this);
      return;
    }

    if (null != mProduct) {
      if (null != mOptionSelect) {
        mOptionSelect.showWindowBottom(mRlayoutOptions);
      } else {
        isAddToOrder = false;
        addToCart(mstrProductOptions, iSelectProductCount);
      }
    }
  }

  @Click(R.id.btn_add_order_click)
  void onClickBtnAddOrder() {
    if (!BaseApplication.isLogin()) {
      BaseApplication.intentToLoginActivity(this);
      return;
    }

    if (null != mOptionSelect) {
      mOptionSelect.showWindowBottom(mRlayoutOptions);
    } else {
      isAddToOrder = true;
      if (mProductNumSelect != null) {
        mProductNumSelect.showWindowBottom(mRlayoutOptions);
      } else {
        addToCart(mstrProductOptions, iSelectProductCount);
        showWaitDialog(3, false, R.string.common_submit_data);
      }
//            addToOrder(mstrProductOptions, iSelectProductCount);
    }
  }

  @Click(R.id.btn_favorite_click) void onClickAddFavorite() {
    if (null == mProduct) {
      return;
    }
    if (!BaseApplication.isLogin()) {
      BaseApplication.intentToLoginActivity(this);
      return;
    }

    mFavoriteHelper.queryIsProductFavorited(false,
        Integer.parseInt(mProduct.id));
    showWaitDialog(2, false, R.string.common_submit_data);
  }

  @Click(R.id.btn_shop_click)
  void onClickBtnShop() {
    Bundle bundle = new Bundle();
    bundle.putInt("id", Integer.parseInt(mProduct.shop_id));
    bundle.putString("title", mProduct.shop_title);
    bundle.putString("im", mProduct.im_account);
    getIntentHandle().intentToActivity(bundle, ShopDetailActivity_.class);
  }

  @Click(R.id.btn_contract_click)
  void onClickBtnContract() {
    ProductBusiness.intentToChartActivity(this, mProduct.im_account);
  }

  @Click(R.id.btn_weixin_click)
  void onClickBtnWeixin() {
    if (!new WechatHandle(this).isWXAppInstalled()) {
      return;
    }
    Intent intent = new Intent();
    ComponentName cmp =
        new ComponentName("com.tencent.mm",
            "com.tencent.mm.ui.LauncherUI");
    intent.setAction(Intent.ACTION_MAIN);
    intent.addCategory(Intent.CATEGORY_LAUNCHER);
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    intent.setComponent(cmp);
    startActivity(intent);
  }

  @Click(R.id.rlayout_product_review_click)
  void onClickRlayoutProductReview() {
    Bundle bundle = new Bundle();
    bundle.putString("productid", mProduct.id);
    getIntentHandle().intentToActivity(bundle, ReviewActivity_.class);
  }

  @Click(R.id.tvew_back_click)
  void onClickTvewBack() {
    finish();
  }

  @Click(R.id.tvew_cart_click)
  void onClickTvewCart() {
    getIntentHandle().intentToActivity(CartActivity_.class);
  }

  @Click(R.id.tvew_share_click)
  void onClickTvewShare() {
    showShareCustomDialog();
  }

  private void addToCart(String optionId, int productNum) {

    if (null == mProduct) {
      return;
    }

    if (productNum > mProduct.allow_quantity && mProduct.allow_quantity != -1) {
      showToast("商家积分数量不足。请最多购买：" + mProduct.allow_quantity);
      return;
    }

    BigDecimal price;
    if (TextUtils.isEmpty(optionId) || "0".equals(optionId)) {
      price = mProduct.price;
    } else {
      price = mOptionSelect.getSelectOptionPrice();
    }
    CartBusiness.addToCart(getHttpDataLoader(), price, Integer.parseInt(mProduct.id), productNum,
        optionId);
  }

  private void addToOrder(String optionId, int productNum) {
    if (null == mProduct) {
      return;
    }

    CartItem[] cartItems = new CartItem[1];
    cartItems[0] = new CartItem();
    cartItems[0].shop_id = mProduct.shop_id;
    cartItems[0].shop_title = mProduct.shop_title;
    cartItems[0].product = new CartItem.Data[1];
    cartItems[0].product[0] = new CartItem.Data();

    if (null != mProduct.images) {
      cartItems[0].product[0].images = mProduct.images[0];
    } else {
      Image image = new Image();
      image.path = mProduct.image;
      cartItems[0].product[0].images = image;
    }

    if (TextUtils.isEmpty(optionId) || "0".equals(optionId)) {
      cartItems[0].product[0].price = mProduct.price;
    } else {
      cartItems[0].product[0].price = mOptionSelect.getSelectOptionPrice();
    }
    cartItems[0].product[0].identify_price = mProduct.identify_price;
    cartItems[0].product[0].product_id = mProduct.id;
    cartItems[0].product[0].quantity = String.valueOf(productNum);
    cartItems[0].product[0].title = mProduct.title;
    cartItems[0].product[0].option_ids = optionId;
    if (null != mOptionSelect) {
      cartItems[0].product[0].product_attr = mOptionSelect.getSelectOption();
    }
    Bundle bundle = new Bundle();
    bundle.putString("product",
        JsonSerializerFactory.Create().encode(cartItems));
    getIntentHandle().intentToActivity(bundle, OrderEnsureActivity_.class);
  }

  @Override
  public void onClickOptionAddCart(String optionId, int productNum) {
    isAddToOrder = false;
    addToCart(optionId, productNum);
  }

  @Override
  public void onClikcOptionAddOrder(String optionId, int productNum) {
//        addToOrder(optionId, productNum);
    isAddToOrder = true;
    addToCart(optionId, productNum);
    showWaitDialog(3, false, R.string.common_submit_data);
  }

  @Override
  protected void onNewIntent(Intent intent) {
    super.onNewIntent(intent);
    setIntent(intent);
    if (null != mShareCustomDialog) {
      mShareCustomDialog.onNewIntent(intent);
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (null != mShareCustomDialog) {
      mShareCustomDialog.onActivityResult(requestCode, resultCode, data);
    }
  }

  private ProductBusiness.ProductNumSelect.OnProductNumSelectorListenre
  getOnProductNumSelectorListener() {
    return new ProductBusiness.ProductNumSelect.OnProductNumSelectorListenre() {
      @Override public void onClickAddCart(int num) {
        isAddToOrder = false;
        addToCart(mstrProductOptions, num);
      }

      @Override public void onClickAddToOrder(int num) {
        isAddToOrder = true;
        addToCart(mstrProductOptions, num);
        showWaitDialog(3, false, R.string.common_submit_data);
      }
    };
  }
}