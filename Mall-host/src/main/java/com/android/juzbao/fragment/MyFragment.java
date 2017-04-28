
package com.android.juzbao.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.juzbao.activity.HelpActivity_;
import com.android.juzbao.activity.MyShopActivity_;
import com.android.juzbao.activity.account.LoginActivity_;
import com.android.juzbao.activity.jifen.MyJiFenActivity_;
import com.android.juzbao.activity.me.MyBrowseProductActivity_;
import com.android.juzbao.activity.me.MyFavoriteProductActivity_;
import com.android.juzbao.activity.me.MyFavoriteShopActivity_;
import com.android.juzbao.activity.me.MyInfoActivity_;
import com.android.juzbao.activity.me.MyMessageActivity_;
import com.android.juzbao.activity.me.MySettingActivity_;
import com.android.juzbao.activity.order.MyShopOrderActivity_;
import com.android.juzbao.application.BaseApplication;
import com.android.juzbao.constant.GlobalConst;
import com.android.juzbao.constant.ResultActivity;
import com.android.juzbao.enumerate.OrderStatus;
import com.android.juzbao.model.AccountBusiness;
import com.android.juzbao.model.WalletBusiness;
import com.android.juzbao.util.CommonValidate;
import com.android.mall.resource.R;
import com.android.zcomponent.common.uiframe.fragment.BaseFragment;
import com.android.zcomponent.http.api.host.Endpoint;
import com.android.zcomponent.http.api.model.MessageData;
import com.android.zcomponent.util.CommonUtil;
import com.android.zcomponent.views.CircleImageView;
import com.easemob.easeui.simple.EaseMessageNotify;
import com.server.api.model.Head;
import com.server.api.model.MyWallet;
import com.server.api.model.Statistics;
import com.server.api.service.AccountService;
import com.server.api.service.WalletService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/**
 * <p>
 * Description: 我的信息
 * </p>
 */
@EFragment(R.layout.fragment_my)
public class MyFragment extends BaseFragment {

    @ViewById(R.id.imgvew_head_picture_show)
    CircleImageView mImgvewHeadPicture;

    @ViewById(R.id.personal_message_dot_show)
    ImageView mPersonalMessageDot;

    @ViewById(R.id.tvew_account_show)
    TextView mTvewAccount;

    @AfterViews
    void initUI() {
        AccountBusiness.showLoginState(mTvewAccount, null /*tvew_level_click*/, mImgvewHeadPicture);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        EaseMessageNotify.getInstance().addView(mPersonalMessageDot);

        if (BaseApplication.isLogin()) {
            AccountBusiness.queryBaseInfo(getHttpDataLoader());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EaseMessageNotify.getInstance().removeView(mPersonalMessageDot);
    }

    @Override
    public void onResume() {
        super.onResume();
        EaseMessageNotify.getInstance().onResume();
        if (BaseApplication.isLogin()) {
            AccountBusiness.statisticsCount(getHttpDataLoader());
            AccountBusiness.getHead(getHttpDataLoader());
//            AccountBusiness.queryIsSign(getHttpDataLoader());
        }
    }

    @Override
    public void onRecvMsg(MessageData msg) throws Exception {
        if (msg.valiateReq(AccountService.StatisticsRequest.class)) {
            Statistics response = msg.getRspObject();
            if (CommonValidate.validateQueryState(getActivity(), msg, response)) {
                AccountBusiness.showStatisticsCount(getView(), response.Data);
            } else {

            }
        } else if (msg.valiateReq(AccountService.HeadPathRequest.class)) {
            Head response = msg.getRspObject();
            if (CommonValidate.validateQueryState(getActivity(), msg, response)) {
                loadImage(mImgvewHeadPicture, Endpoint.HOST
                        + response.data.avatar_path, R.drawable.transparent);
            } else {

            }
        } else if (msg.valiateReq(WalletService.MyWalletRequest.class)) {
            MyWallet response = msg.getRspObject();
            if (CommonValidate.validateQueryState(getActivity(), msg, response)) {
                WalletBusiness.showMyWalletInfo(getView(), response.Data);
            } else {
            }
        }
    }

    @Override
    public void onLoginSuccess() {
        WalletBusiness.queryMyWallet(getHttpDataLoader());
        AccountBusiness.showLoginState(mTvewAccount, null /*tvew_level_click*/, mImgvewHeadPicture);
        AccountBusiness.statisticsCount(getHttpDataLoader());
        AccountBusiness.getHead(getHttpDataLoader());
        AccountBusiness.queryBaseInfo(getHttpDataLoader());
    }

    @Override
    public void onLoginOut() {
//        WalletBusiness.showMyWalletInfo(getView(), null);
        AccountBusiness.showStatisticsCount(getView(), null);
        AccountBusiness.showLoginState(mTvewAccount, null /*tvew_level_click*/, mImgvewHeadPicture);
    }

    @Click(R.id.rlayout_schedule_favorite_product_click)
    void onClickRlayoutScheduleFavoriteProduct() {
        if (!BaseApplication.isLogin()) {
            getIntentHandle().intentToActivity(LoginActivity_.class);
            return;
        }
        getIntentHandle().intentToActivity(MyFavoriteProductActivity_.class);
    }

    @Click(R.id.rlayout_my_favorite_shop_intent_click)
    void onClickRlayoutScheduleFavoriteShop() {
        if (!CommonUtil.isLeastSingleClick()) {
            return;
        }
        if (!BaseApplication.isLogin()) {
            getIntentHandle().intentToActivity(LoginActivity_.class);
            return;
        }
        getIntentHandle().intentToActivity(MyFavoriteShopActivity_.class);
    }

    @Click(R.id.rlayout_footprint_click)
    void onClickRlayoutFootprint() {
        if (!BaseApplication.isLogin()) {
            getIntentHandle().intentToActivity(LoginActivity_.class);
            return;
        }
        getIntentHandle().intentToActivity(MyBrowseProductActivity_.class);
    }

    @Click(R.id.llayout_head_picture_click)
    void onClickLlayoutHeadPicture() {
        if (!BaseApplication.isLogin()) {
            getIntentHandle().intentToActivity(LoginActivity_.class);
            return;
        }
        getIntentHandle().intentToActivity(MyInfoActivity_.class);
    }

    @Click(R.id.rlayout_message_click)
    void onClickRlayoutMessage() {
        if (!BaseApplication.isLogin()) {
            getIntentHandle().intentToActivity(LoginActivity_.class);
            return;
        }
        getIntentHandle().intentToActivity(MyMessageActivity_.class);
    }

    @Click(R.id.rlayout_my_order_click)
    void onClickRlayoutMyOrder() {
        if (!BaseApplication.isLogin()) {
            getIntentHandle().intentToActivity(LoginActivity_.class);
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putString(GlobalConst.ORDER_STATUS, OrderStatus.ALL.getValue());
        getIntentHandle().intentToActivity(bundle, MyShopOrderActivity_.class);
    }

    @Click(R.id.rlayout_my_jifen_click)
    void  onClickRlayoutMyJifen()
    {
        if (!BaseApplication.isLogin()) {
            getIntentHandle().intentToActivity(LoginActivity_.class);
            return;
        }
        Bundle bundle = new Bundle();
        getIntentHandle().intentToActivity(bundle, MyJiFenActivity_.class);
    }

    @Click(R.id.rlayout_my_merchant_click)
    void onClickRlayoutMyMerchant() {
        if (!BaseApplication.isLogin()) {
            getIntentHandle().intentToActivity(LoginActivity_.class);
            return;
        }
        getIntentHandle().intentToActivity(MyShopActivity_.class);
    }

    @Click(R.id.rlayout_my_help_click)
    void onClickRlayoutMyHelp() {
        getIntentHandle().intentToActivity(HelpActivity_.class);
    }

    @Click(R.id.tvew_setting_click)
    void onClickTvewSetting() {
        getIntentHandle().intentToActivity(MySettingActivity_.class);
    }

    @Override
    public void onActivityResultCallBack(int resultCode, Intent intent) {
        if (resultCode == ResultActivity.CODE_WHITDRAW) {
            WalletBusiness.queryMyWallet(getHttpDataLoader());
        } else if (resultCode == ResultActivity.CODE_PAY_ECO_SUCCESS) {
//            queryMyWalletDelay();
        } else if (resultCode == ResultActivity.CODE_MODIFY_HEAD) {
            AccountBusiness.getHead(getHttpDataLoader());
        }
    }
}