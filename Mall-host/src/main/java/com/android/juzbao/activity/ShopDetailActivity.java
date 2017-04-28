
package com.android.juzbao.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.juzbao.adapter.ProductAdapter;
import com.android.juzbao.application.BaseApplication;
import com.android.juzbao.model.FavoriteBusiness.FavoriteHelper;
import com.android.juzbao.model.ProductBusiness;
import com.android.juzbao.util.CommonValidate;
import com.android.mall.resource.R;
import com.android.zcomponent.http.api.host.Endpoint;
import com.android.zcomponent.http.api.model.MessageData;
import com.android.zcomponent.util.CommonUtil;
import com.android.zcomponent.util.PageInditor;
import com.android.zcomponent.util.StringUtil;
import com.android.zcomponent.util.ValidateUtil;
import com.android.zcomponent.views.CircleImageView;
import com.android.zcomponent.views.PullToRefreshView;
import com.android.zcomponent.views.PullToRefreshView.OnFooterRefreshListener;
import com.android.zcomponent.views.PullToRefreshView.OnHeaderRefreshListener;
import com.android.zcomponent.views.swipebacklayout.lib.app.SwipeBackActivity;
import com.server.api.model.ProductItem;
import com.server.api.model.Products;
import com.server.api.model.ShopInfo;
import com.server.api.service.ProductService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * <p>
 * Description:
 * </p>
 */
@EActivity(R.layout.activity_shop_detail)
public class ShopDetailActivity extends SwipeBackActivity implements
        OnHeaderRefreshListener, OnFooterRefreshListener {

    @ViewById(R.id.imgvew_shop_logo_show)
    CircleImageView mImgvewShopLogo;

    @ViewById(R.id.imgvew_signpic_show)
    ImageView mImgvewShopSign;

    @ViewById(R.id.common_gridview_show)
    GridView mCommonGridview;

    @ViewById(R.id.common_pull_refresh_view_show)
    PullToRefreshView mCommonPullRefreshView;

    @ViewById(R.id.rlayout_product_info_show)
    RelativeLayout mRlayoutProductInfo;

    @ViewById(R.id.rlayout_shop_info_show)
    LinearLayout mLlayoutShopInfo;

    @ViewById(R.id.tvew_shopname_show)
    TextView mTvewShopname;

    @ViewById(R.id.tvew_product_show_click)
    TextView mTvewProduct;

    @ViewById(R.id.tvew_shop_show_click)
    TextView mTvewShop;

    @ViewById(R.id.tvew_favorite_show_click)
    TextView mTvewShopFavorite;

    @ViewById(R.id.tvew_contact_show_click)
    TextView mTvewContact;

    @ViewById(R.id.tvew_shop_starttime_show)
    TextView mTvewShopStarttime;

    /**
     * 店铺等级
     */
    @ViewById(R.id.tvew_level_click)
    TextView mTvewLevel;

    @ViewById(R.id.tvew_shopid_show)
    TextView mTvewShopId;

    @ViewById(R.id.tvew_shop_review_show)
    TextView mTvewShopReview;

    @ViewById(R.id.tvew_shop_desc_show)
    TextView mTvewShopDesc;

    @ViewById(R.id.et_search_keyword_show)
    EditText mEdvewInput;

    @ViewById(R.id.search_bar_clear_input_show)
    ImageButton mBtnClear;

    /**
     * 鼓掌
     */
    @ViewById(R.id.tvew_guzhang_click)
    TextView mTvewGuzhang;

    /**
     * 踩
     */
    @ViewById(R.id.tvew_cai_click)
    TextView mTvewCai;

    public static int mShopId;

    private String mstrImAccount;

    private String mstrShopName = "";

    private FavoriteHelper mFavoriteHelper;

    private ProductAdapter mAdapter;

    private PageInditor<ProductItem> mPageInditor =
            new PageInditor<ProductItem>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    void initUI() {
        getTitleBar().setTitleText("店铺详情");
        getDataEmptyView().setBackgroundResource(R.drawable.transparent);
        mCommonPullRefreshView.setOnHeaderRefreshListener(this);
        mCommonPullRefreshView.setOnFooterRefreshListener(this);

        Intent intent = getIntent();
        mShopId = intent.getIntExtra("id", 0);
        mstrImAccount = intent.getStringExtra("im");
        String shopName = intent.getStringExtra("title");

        if (TextUtils.isEmpty(shopName)) {
            mTvewShopname.setText(shopName);
        }

        initSearch();

        mFavoriteHelper = new FavoriteHelper(this, getHttpDataLoader());
        mFavoriteHelper.queryIsShopFavorited(true, mShopId);
        mFavoriteHelper.setShopId(mShopId);
        mFavoriteHelper.favoriteStateView(mTvewShopFavorite, 0, 0);
        ProductBusiness.queryShopInfo(getHttpDataLoader(),
                String.valueOf(mShopId));
        queryListData();
    }

    @Click(R.id.search_distory_dialog_rlayout_back)
    void onClickBack() {
        finish();
    }

    private void initSearch() {

        mBtnClear.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                clearCache();
                refreshData(true);
            }
        });
        mEdvewInput.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String strMsg = s.toString();
                if (!StringUtil.isEmptyString(strMsg)) {
                    setSearchBtnIsClose(false);
                } else {
                    clearCache();
                }
                mstrShopName = strMsg;
                showProductInfo();
                refreshData(true);
            }
        });
    }

    public void clearCache() {
        mPageInditor.setPullRefresh(true);
        if (null != mPageInditor) {
            mPageInditor.clear();
        }
        mstrShopName = null;
        getDataEmptyView().removeAllViews();
        if (!StringUtil.isEmptyString(mEdvewInput.getText().toString())) {
            mEdvewInput.setText("");
            setSearchBtnIsClose(true);
        }
    }

    private void setSearchBtnIsClose(boolean isClose) {
        if (isClose) {
            mBtnClear.setVisibility(View.GONE);
        } else {
            mBtnClear.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onRecvMsg(MessageData msg) throws Exception {
        if (null != mFavoriteHelper) {
            mFavoriteHelper.onRecvMsg(msg);
        }

        if (msg.valiateReq(ProductService.ShopInfoRequest.class)) {
            //查询店铺详情
            ShopInfo response = (ShopInfo) msg.getRspObject();
            if (CommonValidate.validateQueryState(this, msg, response)) {
                showShopInfo(response.data);
            }
        } else if (msg.valiateReq(ProductService.ShopProductsRequest.class)
                || msg.valiateReq(ProductService.ShopProductSearchRequest.class)) {
            mCommonPullRefreshView.onFooterRefreshComplete();
            mCommonPullRefreshView.onHeaderRefreshComplete();

            mPageInditor.clear();

            Products responseProduct = (Products) msg.getRspObject();
            if (ProductBusiness.validateQueryProducts(responseProduct)) {
                mPageInditor.add(responseProduct.Data.Results);

                if (null == mAdapter) {
                    mAdapter = new ProductAdapter(this, mPageInditor.getAll());
                    mPageInditor.bindAdapter(mCommonGridview, mAdapter);
                } else {
                    mAdapter.notifyDataSetChanged();
                }

                if (mPageInditor.getAll().size() == responseProduct.Data.Total) {
                    mCommonPullRefreshView.setFooterRefreshComplete();
                } else {
                    mCommonPullRefreshView.setFooterVisible();
                }
                getDataEmptyView().dismiss();
            } else {
                if (!ValidateUtil.isListEmpty(mPageInditor.getAll())) {
                    getDataEmptyView().dismiss();
                } else {
                    String message =
                            ProductBusiness.validateQueryState(responseProduct,
                                    getString(R.string.common_data_empty));
                    getDataEmptyView().showViewDataEmpty(false, false, msg,
                            message);
                }
            }
        }
    }

    @Override
    public void onLoginSuccess() {
        if (mShopId > 0) {
            mFavoriteHelper.queryIsShopFavorited(true, mShopId);
        }
    }

    @Click(R.id.tvew_favorite_show_click)
    void onClickTvewFavorite() {
        if (!CommonUtil.isLeastSingleClick()) {
            return;
        }

        if (!BaseApplication.isLogin()) {
            BaseApplication.intentToLoginActivity(this);
            return;
        }
        Log.e("gy", "店铺id：" + mShopId);
        mFavoriteHelper.queryIsShopFavorited(false, mShopId);
        showWaitDialog(2, false, R.string.common_submit_data);
    }

    @Click(R.id.tvew_product_show_click)
    void onClickTvewProduct() {
        showProductInfo();
    }

    @Click(R.id.tvew_shop_show_click)
    void onClickTvewShop() {
        showShopInfo();
    }

    @Click(R.id.tvew_contact_show_click)
    void onClickTvewContact() {
        if (!CommonUtil.isLeastSingleClick()) {
            return;
        }
        ProductBusiness.intentToChartActivity(this, mstrImAccount);
    }

    @Override
    public void onDataEmptyClickRefresh() {
        getDataEmptyView().showViewWaiting();
        ProductBusiness.queryShopInfo(getHttpDataLoader(),
                String.valueOf(mShopId));
        refreshData(true);
    }

    private void showShopInfo(ShopInfo.Data shopInfo) {
        if (null == shopInfo) {
            return;
        }
        mTvewShopname.setText(shopInfo.title);
        mTvewShopReview.setText(shopInfo.applause_Rate);
        mTvewShopDesc.setText(shopInfo.description);
        mTvewShopStarttime.setText(shopInfo.create_time_format);

        if (!TextUtils.isEmpty(shopInfo.level)) {
            mTvewLevel.setText(shopInfo.level + " V");
        }

        if (!TextUtils.isEmpty(shopInfo.id)) {
            mTvewShopId.setText("商户ID：" + shopInfo.id);
        }

        if (!TextUtils.isEmpty(shopInfo.gift_zhangsheng)) {
            mTvewGuzhang.setText(shopInfo.gift_zhangsheng);
        }

        if (!TextUtils.isEmpty(shopInfo.gift_xiaomuzhi)) {
            mTvewCai.setText(shopInfo.gift_xiaomuzhi);
        }

        if (!TextUtils.isEmpty(shopInfo.headpic_path)) {
            loadImage(mImgvewShopLogo, Endpoint.HOST + shopInfo.headpic_path,
                    R.drawable.img_empty_logo_small);
        }
        if (!TextUtils.isEmpty(shopInfo.signpic_path)) {
            loadImage(mImgvewShopSign, Endpoint.HOST + shopInfo.signpic_path,
                    R.drawable.img_empty_logo_small);
        }
    }

    private void showProductInfo() {
        mTvewProduct.setTextColor(getResources().getColorStateList(
                R.color.common_btn_text_red_color_selector));
        mTvewShop.setTextColor(getResources().getColorStateList(
                R.color.common_btn_text_black_color_selector));
        mRlayoutProductInfo.setVisibility(View.VISIBLE);
        mLlayoutShopInfo.setVisibility(View.GONE);
    }

    private void showShopInfo() {
        mTvewProduct.setTextColor(getResources().getColorStateList(
                R.color.common_btn_text_black_color_selector));
        mTvewShop.setTextColor(getResources().getColorStateList(
                R.color.common_btn_text_red_color_selector));
        mRlayoutProductInfo.setVisibility(View.GONE);
        mLlayoutShopInfo.setVisibility(View.VISIBLE);
    }

    public void queryListData() {
        getDataEmptyView().showViewWaiting();
        refreshData(true);
    }

    public void refreshData(boolean isPullRefresh) {
        mPageInditor.setPullRefresh(isPullRefresh);

        if (TextUtils.isEmpty(mstrShopName)) {
            ProductBusiness.queryShopProducts(getHttpDataLoader(), mShopId,
                    mPageInditor.getPageNum());
        } else {
            ProductBusiness.queryShopProductSearch(getHttpDataLoader(), mShopId,
                    mPageInditor.getPageNum(), mstrShopName);
        }
    }

    @Override
    public void onFooterRefresh(PullToRefreshView view) {
        refreshData(false);
    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {
        refreshData(true);
    }

}