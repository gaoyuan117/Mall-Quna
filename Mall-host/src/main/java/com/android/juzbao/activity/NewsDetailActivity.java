
package com.android.juzbao.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.android.juzbao.model.MessageBusiness;
import com.android.juzbao.util.CommonValidate;
import com.android.mall.resource.R;
import com.android.zcomponent.http.api.model.MessageData;
import com.android.zcomponent.views.swipebacklayout.lib.app.SwipeBackActivity;
import com.server.api.model.NewsDetail;
import com.server.api.service.MessageService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * <p>
 * Description: 帮助与反馈
 * </p>
 *
 * @ClassName:HelpActivity
 * @author: wei
 * @date: 2015-11-10
 */
@EActivity(R.layout.activity_help_detail)
public class NewsDetailActivity extends SwipeBackActivity {

    @ViewById(R.id.tvew_title_show)
    TextView mTvewTitle;

    @ViewById(R.id.tvew_desc_show)
    TextView mTvewDesc;

    private String mstrNewsId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    void initUI() {
        getTitleBar().setTitleText("资讯详情");
        getDataEmptyView().showViewWaiting();
        mstrNewsId = getIntent().getStringExtra("id");
        MessageBusiness.queryNewsDetail(getHttpDataLoader(), mstrNewsId);
    }

    @Override
    public void onDataEmptyClickRefresh() {
        MessageBusiness.queryNewsDetail(getHttpDataLoader(), mstrNewsId);
    }

    @Override
    public void onRecvMsg(MessageData msg) throws Exception {
        if (msg.valiateReq(MessageService.NewsDetailRequest.class)){
            NewsDetail response =
                    (NewsDetail) msg.getRspObject();
            if (CommonValidate.validateQueryState(this, msg, response)) {
                mTvewTitle.setText(response.Data.title);
                mTvewDesc.setText(response.Data.content);

                getDataEmptyView().dismiss();
            } else {
                getDataEmptyView().showViewDataEmpty(true, false, msg, "");
            }
        }
    }
}