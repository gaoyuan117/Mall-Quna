
package com.android.juzbao.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.juzbao.activity.NoticeDetailActivity_;
import com.android.juzbao.activity.PaincBuyingActivity;
import com.android.juzbao.activity.ProductDetailActivity_;
import com.android.juzbao.activity.TabHostActivity;
import com.android.juzbao.activity.account.LoginActivity_;
import com.android.juzbao.activity.jifen.JiFenProductActivity_;
import com.android.juzbao.activity.me.MyMessageActivity_;
import com.android.juzbao.adapter.HomeGroupAdapter;
import com.android.juzbao.adapter.IndexRecomAdapter;
import com.android.juzbao.adapter.NewsAdapter;
import com.android.juzbao.application.BaseApplication;
import com.android.juzbao.dao.jifendao.JiFenDao;
import com.android.juzbao.enumerate.CategoryType;
import com.android.juzbao.enumerate.MessageType;
import com.android.juzbao.model.MessageBusiness;
import com.android.juzbao.model.ProductBusiness;
import com.android.juzbao.util.CommonValidate;
import com.android.mall.resource.R;
import com.android.zcomponent.common.uiframe.FramewrokApplication;
import com.android.zcomponent.common.uiframe.fragment.BaseFragment;
import com.android.zcomponent.http.api.host.Endpoint;
import com.android.zcomponent.http.api.model.MessageData;
import com.android.zcomponent.http.constant.ErrorCode;
import com.android.zcomponent.json.JsonSerializerFactory;
import com.android.zcomponent.util.ImageLoaderUtil;
import com.android.zcomponent.util.LogEx;
import com.android.zcomponent.util.PageInditor;
import com.android.zcomponent.util.SettingsBase;
import com.android.zcomponent.util.SharedPreferencesUtil;
import com.android.zcomponent.views.MeasureGridView;
import com.android.zcomponent.views.MeasureListView;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.easemob.easeui.simple.EaseMessageNotify;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.server.api.model.AdProduct;
import com.server.api.model.HomeNotice;
import com.server.api.model.HomeProduct;
import com.server.api.model.MessageItem;
import com.server.api.model.MessagePageResult;
import com.server.api.model.NewsItem;
import com.server.api.model.NewsPageResult;
import com.server.api.model.ProductCategory;
import com.server.api.model.ShopInfo;
import com.server.api.model.jifenmodel.IndexBanner;
import com.server.api.service.JiFenService;
import com.server.api.service.MessageService;
import com.server.api.service.ProductService;
import com.server.api.service.ShopService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * Description: 首页
 * </p>
 *
 * @ClassName:HomeFragment
 * @author: wei
 * @date: 2015-11-10
 */
@EFragment(R.layout.fragment_home)
public class HomeFragment extends BaseFragment {

  @ViewById(R.id.editvew_search_show)
  TextView mEditvewSearch;

  @ViewById(R.id.imgvew_panic1_show)
  ImageView mImgvewPanic1;

  @ViewById(R.id.imgvew_panic2_show)
  ImageView mImgvewPanic2;

  @ViewById(R.id.imgvew_panic3_show)
  ImageView mImgvewPanic3;

  @ViewById(R.id.imgvew_panic4_show)
  ImageView mImgvewPanic4;

  @ViewById(R.id.imgvew_message_dot_show)
  ImageView mImgvewDot;

  @ViewById(R.id.tvew_bulletin1_show)
  TextView mTvewBulletin1;

  @ViewById(R.id.tvew_bulletin2_show)
  TextView mTvewBulletin2;

  @ViewById(R.id.tvew_local_news_show)
  TextView mTvewLocalNews;

  @ViewById(R.id.tvew_domestic_news_show)
  TextView mTvewDomesticNews;

  @ViewById(R.id.tvew_international_news_show)
  TextView mTvewInterNews;

  @ViewById(R.id.llayout_bulletin)
  LinearLayout mLlayoutBulletin;

  @ViewById(R.id.gvew_recommend_show)
  MeasureGridView mGridViewRecommend;

  @ViewById(R.id.gvew_group_show)
  MeasureGridView mGridViewGroup;

  @ViewById(R.id.lvew_news_show)
  MeasureListView mListViewNews;

  @ViewById(R.id.tvew_scan_click)
  TextView mLocation;

  private ImageLoader mImageLoader;

  private DisplayImageOptions mOptions;

  private AlphaAnimation alphaAnimation;

  private TranslateAnimation closeAnimation;

  private TranslateAnimation openAnimation;

  private boolean isOpenBulletin1;

  private int bulletinPosition;

  private MyBulletinHandler mBulletinHandler;

  private PageInditor<AdProduct.AdItem> mPageInditor = new PageInditor<AdProduct.AdItem>();

  //item
  private List<ProductCategory.CategoryItem> mCategoryItems = new ArrayList<>();

  private IndexRecomAdapter mAdapter;

  private HomeGroupAdapter mAdapterGroup;

  private NewsAdapter mNewsAdapter;

  @ViewById(R.id.igmgvew_jifen_show)
  ImageView mJifenImgShow;

  @ViewById(R.id.igmgvew_jifen_show_02)
  ImageView mJifenImgShow_02;

  @ViewById(R.id.textView2)
  TextView mTvewShowPrancAnim;

  private AdProduct.AdItem[] mJifenAdvertProduct;



  @AfterViews void initUI() {
    mImageLoader = ImageLoader.getInstance();
    mOptions = ImageLoaderUtil.getDisplayImageOptions(R.drawable.img_empty_logo_small);
    initTranslateAnimation();
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
//    initGroup();
    EaseMessageNotify.getInstance().addView(mImgvewDot);
    ProductBusiness.queryGroup(getHttpDataLoader(), CategoryType.PRODCUT.getValue());
    ProductBusiness.queryHomeProducts(getHttpDataLoader());
    ProductBusiness.queryNotice(getHttpDataLoader());
    ProductBusiness.queryIndexRecom(getHttpDataLoader(), "index_recom");
    // TODO: 2016/8/18 弹出商家积分不足
    JiFenDao.snedJifenHomeImg(getHttpDataLoader(), "score_show_img");
    JiFenDao.sendQueryIndexBanner(getHttpDataLoader(), "index_banner");
    if (FramewrokApplication.isLogin())
      MessageBusiness.queryMessageList(getHttpDataLoader(), MessageType.NOTIFY.getValue(), 1);

    mLocationClient = new LocationClient(getActivity());
    //声明LocationClient类
    mLocationClient.registerLocationListener( myListener );
    //注册监听函数
    initLocation();
    mLocationClient.start();
  }



  private void initGroup() {
    String group = SettingsBase.getInstance().readStringByKey("group");
    if (!TextUtils.isEmpty(group)) {
      ProductCategory.CategoryItem[] items = JsonSerializerFactory.Create().decode(group,
          ProductCategory.CategoryItem[].class);
      if (null != items) {
        mCategoryItems.addAll(Arrays.asList(items));
        mAdapterGroup = new HomeGroupAdapter(getActivity(), mCategoryItems);
        mGridViewGroup.setAdapter(mAdapterGroup);
      }
    }
  }

  private boolean isInit = false;

  @Override
  public void onResume() {
    super.onResume();
    EaseMessageNotify.getInstance().onResume();
    LogEx.e("HomeFragment", "oResume");
    startPrancAnim();
  }

  @Override public void onPause() {
    super.onPause();
    LogEx.e("HomeFragment", "onPause");
    if (!isInit) {
      isInit = true;
      return;
    }
    stopPrancAnim();
  }

  private Handler animHandler = new Handler() {
    @Override public void handleMessage(Message msg) {
      if (msg.what == 1) {
        YoYo.with(Techniques.Pulse).duration(1000).playOn(mTvewShowPrancAnim);
        this.sendEmptyMessageDelayed(1, 1000);
      } else {
        this.removeMessages(1);
      }
    }
  };

  private void startPrancAnim() {
    if (animHandler.hasMessages(1)) {
      return;
    }
    animHandler.sendEmptyMessage(1);
  }

  private void stopPrancAnim() {
    animHandler.sendEmptyMessage(1000000);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    EaseMessageNotify.getInstance().removeView(mImgvewDot);
  }

  @Override
  public void onRecvMsg(MessageData msg) throws Exception {
    if (msg.valiateReq(ProductService.HomeRequest.class)) {
      HomeProduct response = msg.getRspObject();
      if (CommonValidate.validateQueryState(getActivity(), msg, response)) {
        if (null != response) {
          showPanicProducts(response.Data.PanicProducts);
        }
      } else {

      }
    } else if (msg.valiateReq(ProductService.NoticeRequest.class)) {
      HomeNotice response = msg.getRspObject();
      if (CommonValidate.validateQueryState(getActivity(), msg, response)) {
        if (null != response) {
          showTopProducts(response.Data.Results);
        }
      } else {

      }
    } else if (msg.valiateReq(ProductService.AdvertRequest.class)) {
      AdProduct response = msg.getRspObject();
      if (CommonValidate.validateQueryState(getActivity(), msg, response)) {
        ProductBusiness.bindSliderLayout(this,
            R.id.flayout_slider_image, response.Data);
      } else {

      }
    } else if (msg.valiateReq(ProductService.GroupRequest.class)) {
      ProductCategory responseProduct = msg.getRspObject();
      if (CommonValidate.validateQueryState(getActivity(), msg, responseProduct)) {
        mCategoryItems.clear();
//        mCategoryItems.addAll(responseProduct.Data);
        if (responseProduct.Data.size() > 8 && responseProduct.Data.size() < 12) {
          mCategoryItems.addAll(responseProduct.Data.subList(0, 8));
        } else if (responseProduct.Data.size() > 12) {
          mCategoryItems.addAll(responseProduct.Data.subList(0, 12));
        } else {
          mCategoryItems.addAll(responseProduct.Data);
        }
        mAdapterGroup = new HomeGroupAdapter(getActivity(), mCategoryItems);
        mGridViewGroup.setAdapter(mAdapterGroup);
        mGridViewGroup.setOnItemClickListener(getOnItemClickListener());
        SettingsBase.getInstance().writeStringByKey("group", JsonSerializerFactory.Create
            ().encode(mCategoryItems));
      }
    } else if (msg.valiateReq(MessageService.NewsListRequest.class)) {
      NewsPageResult response = msg.getRspObject();
      if (CommonValidate.validateQueryState(getActivity(), msg, response)
          && null != response.Data.Result) {
        if (null == mNewsAdapter) {
          List<NewsItem> listNews = Arrays.asList(response.Data.Result);
          mNewsAdapter = new NewsAdapter(getActivity(), listNews);
          mListViewNews.setAdapter(mNewsAdapter);
        } else {
          mNewsAdapter.notifyDataSetChanged();
        }
      }
    } else if (msg.valiateReq(ProductService.AdIndexRecomRequest.class)) {
      AdProduct response = msg.getRspObject();
      if (CommonValidate.validateQueryState(getActivity(), msg, response)) {
        mPageInditor.add(response.Data);
        if (null == mAdapter) {
          mAdapter = new IndexRecomAdapter(getActivity(), mPageInditor.getAll());
          mPageInditor.bindAdapter(mGridViewRecommend, mAdapter);
        } else {
          mAdapter.notifyDataSetChanged();
        }
      } else {

      }
    } else if (msg.valiateReq(ProductService.AdScoreImgRequest.class)) {
      AdProduct response = msg.getRspObject();
      if (response != null) {
        if (response.Data != null && response.code == ErrorCode.INT_QUERY_DATA_SUCCESS) {
          mJifenAdvertProduct = response.Data;
          if (response.Data[0] != null) {
            mImageLoader.displayImage(Endpoint.HOST + mJifenAdvertProduct[0].image, mJifenImgShow);
          }
          if (response.Data.length > 1 && response.Data[1] != null) {
            mImageLoader.displayImage(Endpoint.HOST + mJifenAdvertProduct[1].image,
                mJifenImgShow_02);
          }
        }
      }
    } else if (msg.valiateReq(JiFenService.JifenIndexbannerRequest.class)) {
      IndexBanner response = msg.getRspObject();
      if (response != null) {
        if (response.data != null && response.code == ErrorCode.INT_QUERY_DATA_SUCCESS) {
          ProductBusiness.bindIndexBannderLayout(this, R.id.flayout_slider_image, response.data);
        }
      }
    } else if (msg.valiateReq(ShopService.ShopInfoRequest.class)) {
      if (msg.valiateReq(ShopService.ShopInfoRequest.class)) {
        ShopInfo response = msg.getRspObject();
        if (null != response) {
          if (response.code == ErrorCode.INT_QUERY_DATA_SUCCESS && null != response.data
              && !TextUtils.isEmpty(response.data.id)) {
            if (!SharedPreferencesUtil.getBoolean("promptapproved", false)) {
              mImgvewDot.setVisibility(View.VISIBLE);
              SharedPreferencesUtil.putBoolean("promptapproved", true);
            }
          }
        }
      }
    } else if (msg.valiateReq(MessageService.MessageListRequest.class)) {
      MessagePageResult response = (MessagePageResult) msg.getRspObject();
      if (response.Data != null && response.code == ErrorCode.INT_QUERY_DATA_SUCCESS) {
        for (MessageItem messageItme : response.Data.Result) {
          if ("0".equals(messageItme.is_read)) {
            mImgvewDot.setVisibility(View.VISIBLE);
            break;
          }
        }
      }
    }
  }


  /**
   * @SerializedName("top_products") //今日头条商品
   * @SerializedName("paipin_products") //拍真宝商品
   * @SerializedName("panic_products") //抢购会商品
   * @SerializedName("gift_products") //选礼物商品
   */
  private void showPanicProducts(HomeProduct.HomeItem[] datas) {
    if (null == datas) {
      return;
    }
    showProductImage(mImgvewPanic1, 0, datas);
    showProductImage(mImgvewPanic2, 1, datas);
    showProductImage(mImgvewPanic3, 2, datas);
    showProductImage(mImgvewPanic4, 3, datas);
  }

  private void showTopProducts(HomeNotice.NoticeItem[] datas) {
    mBulletinHandler = new MyBulletinHandler(datas);
    mBulletinHandler.sendEmptyMessageDelayed(0, 5000);
    mTvewBulletin1.setText(datas[bulletinPosition].title);
    mLlayoutBulletin.setOnClickListener(new NoticeClickListener(
        bulletinPosition, datas));
  }

  private void initTranslateAnimation() {
    alphaAnimation = new AlphaAnimation(1, 0f);
    alphaAnimation.setDuration(300);

    closeAnimation =
        new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0,
            Animation.RELATIVE_TO_SELF, 0,
            Animation.RELATIVE_TO_SELF, 0,
            Animation.RELATIVE_TO_SELF, -1);
    closeAnimation.setDuration(300);
    openAnimation =
        new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0,
            Animation.RELATIVE_TO_SELF, 0,
            Animation.RELATIVE_TO_SELF, 1,
            Animation.RELATIVE_TO_SELF, 0);
    closeAnimation.setAnimationListener(new AnimationListener() {

      @Override
      public void onAnimationStart(Animation animation) {

      }

      @Override
      public void onAnimationRepeat(Animation animation) {

      }

      @Override
      public void onAnimationEnd(Animation animation) {

      }
    });
    openAnimation.setAnimationListener(new AnimationListener() {

      @Override
      public void onAnimationStart(Animation animation) {

      }

      @Override
      public void onAnimationRepeat(Animation animation) {

      }

      @Override
      public void onAnimationEnd(Animation animation) {
        if (isOpenBulletin1) {
          mTvewBulletin2.setVisibility(View.GONE);
        } else {
          mTvewBulletin1.setVisibility(View.GONE);
        }
      }
    });
  }

  private class MyBulletinHandler extends Handler {

    private HomeNotice.NoticeItem[] datas;

    public MyBulletinHandler(HomeNotice.NoticeItem[] datas) {
      this.datas = datas;
    }

    @Override
    public void handleMessage(Message msg) {
      if (bulletinPosition < datas.length - 1) {
        bulletinPosition++;
      } else {
        bulletinPosition = 0;
      }

      AnimationSet set = new AnimationSet(false);
      set.addAnimation(closeAnimation);
      set.addAnimation(alphaAnimation);

      if (mTvewBulletin1.getVisibility() == View.VISIBLE) {
        isOpenBulletin1 = false;
        mTvewBulletin2.setText(this.datas[bulletinPosition].title);
        mTvewBulletin2.setVisibility(View.VISIBLE);

        mTvewBulletin1.startAnimation(set);
        mTvewBulletin2.startAnimation(openAnimation);
      } else {
        isOpenBulletin1 = true;
        mTvewBulletin1.setText(this.datas[bulletinPosition].title);
        mTvewBulletin1.setVisibility(View.VISIBLE);

        mTvewBulletin1.startAnimation(openAnimation);
        mTvewBulletin2.startAnimation(set);
      }
      mLlayoutBulletin.setOnClickListener(new NoticeClickListener(
          bulletinPosition, datas));
      sendEmptyMessageDelayed(0, 5000);
    }
  }

  private void showProductImage(ImageView imageView, int position,
                                HomeProduct.HomeItem[] datas) {
    if (datas.length > position) {
      mImageLoader.displayImage(Endpoint.HOST + datas[position].image, imageView, mOptions);
      imageView.setOnClickListener(new ImageClickListener(position, datas));
    }
  }


  private AdapterView.OnItemClickListener getOnItemClickListener() {
    return new AdapterView.OnItemClickListener() {
      @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TabHostActivity tabHostActivity = BaseApplication.getInstance().getActivity
            (TabHostActivity.class);
        tabHostActivity.setSelectedTab(1);
        final CategroyFragment_ categroyFragment = (CategroyFragment_) tabHostActivity
            .getFragments()[1];
        if (null != categroyFragment) {
          categroyFragment.selectCategory1(mCategoryItems.get(position).id);
        }
      }
    };
  }

  @Click(R.id.llayout_painc_buy_click)
  void onClickLlayoutPaincBut() {
    // 抢购
    getIntentHandle().intentToActivity(PaincBuyingActivity.class);
  }

  @Click(R.id.igmgvew_jifen_show)
  void onClick_igmgvew_jifen_show() {
    if (mJifenAdvertProduct == null) {
      getIntentHandle().intentToActivity(JiFenProductActivity_.class);
    } else {
      if (mJifenAdvertProduct[0] != null && mJifenAdvertProduct[0].position.equals
          ("score_show_img")) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("jifen", true);
        bundle.putInt("id", Integer.parseInt(mJifenAdvertProduct[0].product_id));
        getIntentHandle().intentToActivity(bundle, ProductDetailActivity_.class);
      } else {
        getIntentHandle().intentToActivity(JiFenProductActivity_.class);
      }
    }
  }

  @Click(R.id.igmgvew_jifen_show_02)
  void onClick_igmgvew_jifen_show_02() {
    if (mJifenAdvertProduct == null) {
      getIntentHandle().intentToActivity(JiFenProductActivity_.class);
    } else {
      if (mJifenAdvertProduct.length > 1 && mJifenAdvertProduct[1] != null &&
          mJifenAdvertProduct[1].position.equals("score_show_img")) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("jifen", true);
        bundle.putInt("id", Integer.parseInt(mJifenAdvertProduct[1].product_id));
        getIntentHandle().intentToActivity(bundle, ProductDetailActivity_.class);
      } else {
        getIntentHandle().intentToActivity(JiFenProductActivity_.class);
      }
    }
  }

  @Click(R.id.tvew_jifen_more_click) void onClick_tvew_jifen_more_click() {
    getIntentHandle().intentToActivity(JiFenProductActivity_.class);
  }

  @Click(R.id.imgvew_clear_icon_click) void onClickImgvewClearIcon() {
    mEditvewSearch.setText("");
  }

//  @Click(R.id.rlayout_sort1_click) void onClickRlayoutSort1() {
//
//  }
//
//  @Click(R.id.rlayout_sort2_click) void onClickRlayoutSort2() {
//
//  }
//
//  @Click(R.id.rlayout_sort3_click) void onClickRlayoutSort3() {
//
//  }
//
//  @Click(R.id.rlayout_sort4_click) void onClickRlayoutSort4() {
//
//  }
//
//  @Click(R.id.rlayout_sort5_click) void onClickRlayoutSort5() {
//
//  }
//
//  @Click(R.id.rlayout_sort6_click) void onClickRlayoutSort6() {
//
//  }
//
//  @Click(R.id.rlayout_sort7_click) void onClickRlayoutSort7() {
//
//  }
//
//  @Click(R.id.rlayout_sort8_click) void onClickRlayoutSort8() {
//
//  }

  @Click(R.id.tvew_local_news_show) void onClictTvewLocalNews() {
    mTvewLocalNews.setBackgroundColor(getResources().getColor(R.color.gray_white));
    mTvewInterNews.setBackgroundColor(getResources().getColor(R.color.transparent));
    mTvewDomesticNews.setBackgroundColor(getResources().getColor(R.color.transparent));
    MessageBusiness.queryNewsList(getHttpDataLoader(), "local");
  }

  @Click(R.id.tvew_domestic_news_show) void onClictTvewDomesticNews() {
    mTvewLocalNews.setBackgroundColor(getResources().getColor(R.color.transparent));
    mTvewInterNews.setBackgroundColor(getResources().getColor(R.color.transparent));
    mTvewDomesticNews.setBackgroundColor(getResources().getColor(R.color.gray_white));
    MessageBusiness.queryNewsList(getHttpDataLoader(), "domestic");
  }

  @Click(R.id.tvew_international_news_show) void onClictTvewInterNews() {
    mTvewLocalNews.setBackgroundColor(getResources().getColor(R.color.transparent));
    mTvewInterNews.setBackgroundColor(getResources().getColor(R.color.gray_white));
    mTvewDomesticNews.setBackgroundColor(getResources().getColor(R.color.transparent));
    MessageBusiness.queryNewsList(getHttpDataLoader(), "international");
  }

  @Click(R.id.llayout_search_click) void onClickRlayoutSearch() {
    ProductBusiness.intentToSearchActivity(getActivity(), "");
  }

//  @Click(R.id.tvew_scan_click) void onClickTvewScan() {
//    getIntentHandle().intentToActivity(BarcodeActivity.class);
//  }

  @Click(R.id.tvew_message_click) void onClickTvewMessage() {
    if (!BaseApplication.isLogin()) {
      getIntentHandle().intentToActivity(LoginActivity_.class);
      return;
    }
    getIntentHandle().intentToActivity(MyMessageActivity_.class);
  }

  public class NoticeClickListener implements OnClickListener {

    private HomeNotice.NoticeItem[] datas;

    private int position;

    public NoticeClickListener(int position, HomeNotice.NoticeItem[] datas) {
      this.datas = datas;
      this.position = position;
    }

    @Override
    public void onClick(View v) {
      Bundle bundle = new Bundle();
      bundle.putString("id", datas[position].id);
      getIntentHandle().intentToActivity(bundle, NoticeDetailActivity_.class);
    }
  }

  public class ImageClickListener implements OnClickListener {

    private HomeProduct.HomeItem[] datas;

    private int position;

    public ImageClickListener(int position, HomeProduct.HomeItem[] datas) {
      this.datas = datas;
      this.position = position;
    }

    @Override
    public void onClick(View v) {
      if (datas.length > position) {
//                if (ProductType.PAIPIN.getValue().equals(datas[position].type)) {
//                    ProductBusiness.intentToPaipinProductDetailActivity(
//                            getActivity(), null,
//                            Integer.parseInt(datas[position].product_id));
//                } else {
//                if (position == 0) {
//                    getIntentHandle().intentToActivity(PaincBuyingActivity.class);
//                    return;
//                }
        ProductBusiness.intentToProductDetailActivity(
            getActivity(), null,
            Integer.parseInt(datas[position].product_id));
//                }
      }
    }
  }


  public LocationClient mLocationClient = null;
  public BDLocationListener myListener = new MyLocationListener();

  private void initLocation(){
    LocationClientOption option = new LocationClientOption();
    option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
    //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备

    option.setCoorType("bd09ll");
    //可选，默认gcj02，设置返回的定位结果坐标系

    int span=1000;
    option.setScanSpan(2000);
    //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的

    option.setIsNeedAddress(true);
    //可选，设置是否需要地址信息，默认不需要

    option.setOpenGps(true);
    //可选，默认false,设置是否使用gps

    option.setLocationNotify(true);
    //可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果

    option.setIgnoreKillProcess(false);
    //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死

    option.SetIgnoreCacheException(false);
    //可选，默认false，设置是否收集CRASH信息，默认收集

    mLocationClient.setLocOption(option);
  }

   class MyLocationListener implements BDLocationListener {

    @Override
    public void onReceiveLocation(BDLocation location) {

      //获取定位结果
      StringBuffer sb = new StringBuffer(256);

      sb.append("time : ");
      sb.append(location.getTime());    //获取定位时间

      sb.append("\nerror code : ");
      sb.append(location.getLocType());    //获取类型类型

      sb.append("\nlatitude : ");
      sb.append(location.getLatitude());    //获取纬度信息

      sb.append("\nlontitude : ");
      sb.append(location.getLongitude());    //获取经度信息

      sb.append("\nradius : ");
      sb.append(location.getRadius());    //获取定位精准度

      if (location.getLocType() == BDLocation.TypeGpsLocation){

        // GPS定位结果
        sb.append("\nspeed : ");
        sb.append(location.getSpeed());    // 单位：公里每小时

        sb.append("\nsatellite : ");
        sb.append(location.getSatelliteNumber());    //获取卫星数

        sb.append("\nheight : ");
        sb.append(location.getAltitude());    //获取海拔高度信息，单位米

        sb.append("\ndirection : ");
        sb.append(location.getDirection());    //获取方向信息，单位度

        sb.append("\naddr : ");
        sb.append(location.getAddrStr());    //获取地址信息

        sb.append("\ndescribe : ");
        sb.append("gps定位成功");

      } else if (location.getLocType() == BDLocation.TypeNetWorkLocation){

        // 网络定位结果
        sb.append("\naddr : ");
        sb.append(location.getAddrStr());    //获取地址信息

        sb.append("\noperationers : ");
        sb.append(location.getOperators());    //获取运营商信息

        sb.append("\ndescribe : ");
        sb.append("网络定位成功");

      } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {

        // 离线定位结果
        sb.append("\ndescribe : ");
        sb.append("离线定位成功，离线定位结果也是有效的");

      } else if (location.getLocType() == BDLocation.TypeServerError) {

        sb.append("\ndescribe : ");
        sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");

      } else if (location.getLocType() == BDLocation.TypeNetWorkException) {

        sb.append("\ndescribe : ");
        sb.append("网络不同导致定位失败，请检查网络是否通畅");

      } else if (location.getLocType() == BDLocation.TypeCriteriaException) {

        sb.append("\ndescribe : ");
        sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");

      }
      sb.append("\nlocationdescribe : ");
      mLocation.setText(location.getAddrStr());
      Log.e("BaiduLocationApiDem", sb.toString());
    }
  }
}
