
package com.android.juzbao.activity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.os.Bundle;
import android.widget.TextView;
import com.android.mall.resource.R;
import com.server.api.model.BondNotice;
import com.android.juzbao.model.ProductBusiness;
import com.android.juzbao.util.CommonValidate;
import com.android.zcomponent.http.api.model.MessageData;
import com.android.zcomponent.views.swipebacklayout.lib.app.SwipeBackActivity;
import com.server.api.service.ProductService;

/**
 * <p>
 * Description: 保证金须知
 * </p>
 *
 * @ClassName:BondNoticeActivity
 * @author: wei
 * @date: 2015-11-10
 */
@EActivity(R.layout.activity_bond_notice)
public class BondNoticeActivity extends SwipeBackActivity {

    @ViewById(R.id.tvew_desc_show)
    TextView mTvewDesc;

    private String mstrArticleId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    void initUI() {
        getTitleBar().setTitleText("保证金须知");

        getDataEmptyView().showViewWaiting();
        ProductBusiness.queryPaipinBondNotice(getHttpDataLoader());
    }

    @Override
    public void onDataEmptyClickRefresh() {
        ProductBusiness.queryPaipinBondNotice(getHttpDataLoader());
    }

    @Override
    public void onRecvMsg(MessageData msg) throws Exception {
        if (msg.valiateReq(ProductService.BondNoticeRequest.class)) {
            BondNotice response = (BondNotice) msg.getRspObject();
            if (CommonValidate.validateQueryState(this, msg, response)) {
                mTvewDesc.setText(response.Data);
                getDataEmptyView().dismiss();
            } else {
                getDataEmptyView().showViewDataEmpty(true, false, msg,
                        R.string.common_data_empty);
            }
        }
    }
}