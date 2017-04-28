
package com.android.juzbao.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.juzbao.constant.ProviderGlobalConst;
import com.android.juzbao.constant.ProviderResultActivity;
import com.android.juzbao.enumerate.GoodsStatus;
import com.android.juzbao.enumerate.ProviderOrderStatus;
import com.android.juzbao.model.ProviderShopBusiness;
import com.android.zcomponent.http.api.host.Endpoint;
import com.android.zcomponent.http.api.model.MessageData;
import com.android.zcomponent.http.constant.ErrorCode;
import com.android.zcomponent.json.JsonSerializerFactory;
import com.android.zcomponent.util.ShowMsg;
import com.android.zcomponent.util.ShowMsg.IConfirmDialog;
import com.android.zcomponent.util.StringUtil;
import com.android.zcomponent.views.swipebacklayout.lib.app.SwipeBackActivity;
import com.server.api.model.OnlineService;
import com.server.api.model.ShopInfo;
import com.server.api.service.ShopService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * <p>
 * Description: 我的店铺
 * </p>
 */
@EActivity(resName = "activity_myshop")
public class MyShopActivity extends SwipeBackActivity {

    @ViewById(resName = "imgvew_shop_logo_show")
    ImageView mImgvewShopLogo;

    @ViewById(resName = "imgvew_signpic_show")
    ImageView mImgvewShopSignpic;

    @ViewById(resName = "tvew_shopname_show")
    TextView mTvewShopname;

    @ViewById(resName = "tvew_allmoeny_show")
    TextView mTvewAllmoeny;

    @ViewById(resName = "tvew_visitorcount_show")
    TextView mTvewVisitorcount;

    @ViewById(resName = "tvew_ordercount_show")
    TextView mTvewOrdercount;

    private OnlineService mOnlineService;

    private ShopInfo.Data mShopInfo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    void initUI() {
        getTitleBar().setTitleText("我的店铺 ");

        ProviderShopBusiness.queryShopInfo(getHttpDataLoader());
        ProviderShopBusiness.queryShopOnlineSerivce(getHttpDataLoader());
        getDataEmptyView().showViewWaiting();
    }

    @Override
    public void onRecvMsg(MessageData msg) throws Exception {
        if (msg.valiateReq(ShopService.ShopInfoRequest.class)) {
            ShopInfo response = msg.getRspObject();
            if (null != response) {
                if (response.code == ErrorCode.INT_QUERY_DATA_SUCCESS
                        && null != response.data
                        && !TextUtils.isEmpty(response.data.id)) {
                    showShopInfo(response.data);
                    getDataEmptyView().dismiss();
                } else {
                    if (response.code == -2) {
                        showCreateShopConfirmDialog();
                    } else {
                        showToast(response.message);
                        finish();
//                        getDataEmptyView().showViewDataEmpty(false,false,2,response.message);
                    }
                }
            } else {
                if (null == mShopInfo) {
                    getDataEmptyView().showViewDataEmpty(false, false, msg, "");
                }
            }
        } else if (msg.valiateReq(ShopService.OnlineServiceRequest.class)) {
            mOnlineService = msg.getRspObject();
        }
    }

    private void showShopInfo(ShopInfo.Data shopInfo) {
        mShopInfo = shopInfo;
        if (!TextUtils.isEmpty(shopInfo.headpic_path)) {
            loadImage(mImgvewShopLogo, Endpoint.HOST + shopInfo.headpic_path, 0);
        }
        if (!TextUtils.isEmpty(shopInfo.signpic_path)) {
            loadImage(mImgvewShopSignpic, Endpoint.HOST + shopInfo.signpic_path, 0);
        }
        mTvewShopname.setText(shopInfo.title);
        mTvewAllmoeny.setText("¥" + StringUtil.formatProgress(shopInfo.amount));
        mTvewVisitorcount.setText(shopInfo.visitor_count);
        mTvewOrdercount.setText(shopInfo.order_count);
    }

    private void showCreateShopConfirmDialog() {
        getDataEmptyView().removeAllViews();
        ShowMsg.showConfirmDialog(this, new IConfirmDialog() {

            @Override
            public void onConfirm(boolean confirmValue) {
                if (confirmValue) {
                    getIntentHandle().intentToActivity(ProtocolActivity_.class);
                }
                finish();
            }
        }, "创建", "取消", "您还没有创建店铺，快快去开店吧！");
    }


    @Click(resName = "llayout_myshop_goods_release_click")
    void onClickLlayoutMyshopGoodsRelease() {
//       getIntentHandle().intentToActivity(ReleaseActivity_.class);
        getIntentHandle().intentToActivity(ReleaseCommodityGoodsActivity_.class);
    }

    @Click(resName = "llayout_myshop_goods_manage_click")
    void onClickLlayoutMyshopGoodsManage() {
        Bundle bundle = new Bundle();
        bundle.putString("type", GoodsStatus.PUTONG.getValue());
        getIntentHandle().intentToActivity(bundle, GoodsManageActivity_.class);
    }

    @Click(resName = "llayout_myshop_order_manage_click")
    void onClickLlayoutMyshopOrderManage() {
        Bundle bundle = new Bundle();
        bundle.putString(ProviderGlobalConst.ORDER_STATUS,
                ProviderOrderStatus.ALL.getValue());
        getIntentHandle().intentToActivity(bundle, MyOrderActivity_.class);
    }

    @Click(resName = "llayout_myshop_jifen_manager_click")
    void onClickLlayoutMyshopJifenManager() {
        Bundle bundle = new Bundle();
        bundle.putString("shop_id", mShopInfo.id);
        getIntentHandle().intentToActivity(bundle, MyshopJifenManager_.class);
    }

    @Click(resName = "llayout_myshop_shop_set_click")
    void onClickLlayoutMyshopShopSet() {
        if (null != mShopInfo) {
            Bundle bundle = new Bundle();
            bundle.putString("shop", JsonSerializerFactory.Create().encode(mShopInfo));
            getIntentHandle().intentToActivity(bundle, ShopSetActivity_.class);
        } else {
            getIntentHandle().intentToActivity(ShopSetActivity_.class);
        }
    }

    @Click(resName = "llayout_myshop_serve_hall_click")
    void onClickLlayoutMyshopServeHall() {
        Bundle bundle = new Bundle();
        bundle.putString("phone",
                JsonSerializerFactory.Create().encode(mOnlineService));
        getIntentHandle().intentToActivity(bundle, ServeHallActivity_.class);
    }

    @Click(resName = "llayout_myshop_shop_fitment_click")
    void onClickLlayoutMyshopShopFitment() {
        Bundle bundle = new Bundle();
        bundle.putString("shop",
                JsonSerializerFactory.Create().encode(mShopInfo));
        getIntentHandle().intentToActivity(bundle, ShopFitmentActivity_.class);
    }

    @Click(resName = "llayout_myshop_study_click")
    void onClickLlayoutMyshopStudy() {
        getIntentHandle().intentToActivity(CourseActivity_.class);
    }

    @Override
    public void onActivityResultCallBack(int resultCode, Intent intent) {
        if (ProviderResultActivity.CODE_EDIT_SHOP == resultCode) {
            String strShopInfo = intent.getStringExtra("shop");
            if (!TextUtils.isEmpty(strShopInfo)) {
                mShopInfo =
                        JsonSerializerFactory.Create().decode(strShopInfo,
                                ShopInfo.Data.class);
                showShopInfo(mShopInfo);
                ProviderShopBusiness.queryShopInfo(getHttpDataLoader());
            }
        }
    }
}