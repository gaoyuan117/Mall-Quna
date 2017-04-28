
package com.android.juzbao.activity.me;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.os.Bundle;
import android.widget.TextView;

import com.android.mall.resource.R;
import com.server.api.model.MessageDetail;
import com.android.juzbao.model.MessageBusiness;
import com.android.juzbao.util.CommonValidate;
import com.android.zcomponent.annotation.ZTitleMore;
import com.android.zcomponent.http.api.model.MessageData;
import com.android.zcomponent.views.swipebacklayout.lib.app.SwipeBackActivity;
import com.server.api.service.MessageService;

/**
 * <p>
 * Description: 帮助与反馈
 * </p>
 *
 * @ClassName:HelpActivity
 * @author: wei
 * @date: 2015-11-10
 */
@EActivity(R.layout.activity_my_message_detail)
@ZTitleMore(false)
public class MyMessageDetailActivity extends SwipeBackActivity {

    @ViewById(R.id.tvew_title_show)
    TextView mTvewTitle;

    @ViewById(R.id.tvew_desc_show)
    TextView mTvewDesc;

    private String mstrMessageId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    void initUI() {
        getTitleBar().setTitleText("消息详情");

        getDataEmptyView().showViewWaiting();
        mstrMessageId = getIntent().getStringExtra("id");
        MessageBusiness.queryMessageDetail(getHttpDataLoader(), mstrMessageId);
    }

    @Override
    public void onDataEmptyClickRefresh() {
        MessageBusiness.queryMessageDetail(getHttpDataLoader(), mstrMessageId);
    }

    @Override
    public void onRecvMsg(MessageData msg) throws Exception {
        if (msg.valiateReq(MessageService.MessageDetailRequest.class)){
            MessageDetail response =
                    (MessageDetail) msg.getRspObject();
            if (CommonValidate.validateQueryState(this, msg, response)) {
                mTvewTitle.setText(response.Data.title);
                mTvewDesc.setText(response.Data.message);

                getDataEmptyView().dismiss();
            } else {
                getDataEmptyView().showViewDataEmpty(true, false, msg, "");
            }
        }
    }
}