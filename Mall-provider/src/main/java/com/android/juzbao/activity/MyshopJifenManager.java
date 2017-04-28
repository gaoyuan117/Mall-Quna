package com.android.juzbao.activity;

import android.widget.TextView;

import com.android.juzbao.dao.JifenDao;
import com.android.zcomponent.http.api.model.MessageData;
import com.android.zcomponent.http.constant.ErrorCode;
import com.android.zcomponent.views.swipebacklayout.lib.app.SwipeBackActivity;
import com.server.api.model.jifenmodel.ShopScore;
import com.server.api.service.JiFenService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(resName = "activity_myshop_jifen_manager")
public class MyshopJifenManager extends SwipeBackActivity {

    @ViewById(resName = "tvew_shop_jifen_count")
    TextView mShopTotleJifenTv;

    @ViewById(resName = "tvew_shop_jifen_send_count")
    TextView mShopSendJifenTv;

    @ViewById(resName = "tvew_shop_jifen_recycle_count")
    TextView mShopRecycleJifen;

    private String shop_id;

    @AfterViews
    void initUI() {
        getTitleBar().setTitleText("积分管理");
        shop_id = getIntent().getStringExtra("shop_id");
        if (shop_id != null){
            JifenDao.sendQueryShopScore(getHttpDataLoader(),shop_id);
        }
    }

    @Override public void onRecvMsg(MessageData msg) throws Exception {

        if (msg.valiateReq(JiFenService.JifenShopScoreRequest.class)){
            ShopScore response = msg.getRspObject();
            if (response != null || response.code == ErrorCode.INT_QUERY_DATA_SUCCESS) {
                if (response.data != null){
                    showShopScoreInfo(response.data);
                }
            }
        }
    }

    private void showShopScoreInfo(ShopScore.Data data) {
        mShopTotleJifenTv.setText(data.score);
        mShopSendJifenTv.setText(data.give_score);
        mShopRecycleJifen.setText(data.recover_score);
    }
}
