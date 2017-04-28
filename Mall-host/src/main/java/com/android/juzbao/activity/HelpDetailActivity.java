
package com.android.juzbao.activity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.os.Bundle;
import android.widget.TextView;
import com.android.mall.resource.R;
import com.server.api.model.ArticleDetail;
import com.android.juzbao.model.HelpBusiness;
import com.android.juzbao.util.CommonValidate;
import com.android.zcomponent.http.api.model.MessageData;
import com.android.zcomponent.views.swipebacklayout.lib.app.SwipeBackActivity;
import com.server.api.service.HelpService;

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
public class HelpDetailActivity extends SwipeBackActivity {

    @ViewById(R.id.tvew_title_show)
    TextView mTvewTitle;

    @ViewById(R.id.tvew_desc_show)
    TextView mTvewDesc;

    private String mstrArticleId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    void initUI() {
        getTitleBar().setTitleText("问题详情");
        getDataEmptyView().showViewWaiting();
        mstrArticleId = getIntent().getStringExtra("id");
        HelpBusiness.queryArticlesDetail(getHttpDataLoader(), mstrArticleId);
    }

    @Override
    public void onDataEmptyClickRefresh() {
        HelpBusiness.queryArticlesDetail(getHttpDataLoader(), mstrArticleId);
    }

    @Override
    public void onRecvMsg(MessageData msg) throws Exception {
        if (msg.valiateReq(HelpService.ArticlesDetailRequest.class)){
            ArticleDetail response =
                    (ArticleDetail) msg.getRspObject();
            if (CommonValidate.validateQueryState(this, msg, response)) {
                mTvewTitle.setText(response.Data.title);
                mTvewDesc.setText(response.Data.description);

                getDataEmptyView().dismiss();
            } else {
                getDataEmptyView().showViewDataEmpty(true, false, msg, "");
            }
        }
    }
}