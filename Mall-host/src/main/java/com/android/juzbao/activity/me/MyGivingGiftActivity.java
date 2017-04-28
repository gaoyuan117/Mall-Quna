
package com.android.juzbao.activity.me;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.juzbao.model.AccountBusiness;
import com.android.juzbao.util.CommonValidate;
import com.android.mall.resource.R;
import com.android.zcomponent.http.api.model.MessageData;
import com.android.zcomponent.util.ShowMsg;
import com.android.zcomponent.views.swipebacklayout.lib.app.SwipeBackActivity;
import com.server.api.model.CommonReturn;
import com.server.api.model.Gift;
import com.server.api.service.AccountService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * <p>
 * Description: 赠送礼品
 * </p>
 *
 * @ClassName:MyGivingGiftActivity
 * @author: wei
 * @date: 2016-6-29
 */
@EActivity(R.layout.activity_giving_gift)
public class MyGivingGiftActivity extends SwipeBackActivity {

    @ViewById(R.id.tvew_version_show)
    TextView mTvewVersion;

    @ViewById(R.id.editvew_mechant_show)
    EditText mEdvewShopId;

    @ViewById(R.id.editvew_give_count_show)
    EditText mEdvewQuantity;

    @ViewById(R.id.imgvew_review_better_show)
    ImageView mImgvewBetter;

    @ViewById(R.id.imgvew_review_bad_show)
    ImageView mImgvewBad;

    @ViewById(R.id.tvew_guzhang_click)
    TextView mTvewGuzhang;

    @ViewById(R.id.tvew_cai_click)
    TextView mTvewCai;

    private String gift;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    void initUI() {
        getTitleBar().setTitleText("赠送礼品");
        onClickLlayoutBetter();
        AccountBusiness.queryGift(getHttpDataLoader());
    }

    @Override
    public void onRecvMsg(MessageData msg) throws Exception {
        if (msg.valiateReq(AccountService.UseGiftRequest.class)) {
            CommonReturn response = msg.getRspObject();
            if (CommonValidate.validateQueryState(this, msg, response, "赠送失败")) {
                ShowMsg.showToast(getApplicationContext(), "赠送成功");
                finish();
            }
        } else if (msg.valiateReq(AccountService.GiftRequest.class)) {
            Gift response = msg.getRspObject();
            if (CommonValidate.validateQueryState(this, msg, response)) {
                mTvewGuzhang.setText(String.valueOf(response.Data.zhangsheng.unused));
                mTvewCai.setText(String.valueOf(response.Data.xiaomuzhi.unused));
            } else {

            }
        }
    }

    @Click(R.id.tvew_submit_click)
    void onClickTvewSubmit() {
        String shopId = mEdvewShopId.getText().toString();
        String quantity = mEdvewQuantity.getText().toString();

        if (TextUtils.isEmpty(gift)) {
            ShowMsg.showToast(getApplicationContext(), "请选择赠送类型");
            return;
        }

        if (TextUtils.isEmpty(shopId)) {
            ShowMsg.showToast(getApplicationContext(), "请输入赠送商户ID");
            return;
        }

        if (TextUtils.isEmpty(shopId)) {
            ShowMsg.showToast(getApplicationContext(), "请输入要赠送的数量");
            return;
        }
        AccountBusiness.queryUseGift(getHttpDataLoader(), shopId, gift, quantity);
        showWaitDialog(1, false);
    }

    @Click(R.id.llayout_review_better_click)
    void onClickLlayoutBetter() {
        gift = "1";
        mImgvewBetter.setBackgroundResource(R.drawable.cart_option_on);
        mImgvewBad.setBackgroundResource(R.drawable.cart_option);
    }

    @Click(R.id.llayout_review_bad_click)
    void onClickLlayoutBad() {
        gift = "2";
        mImgvewBetter.setBackgroundResource(R.drawable.cart_option);
        mImgvewBad.setBackgroundResource(R.drawable.cart_option_on);
    }
}