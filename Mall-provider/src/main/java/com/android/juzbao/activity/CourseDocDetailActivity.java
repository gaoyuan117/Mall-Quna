
package com.android.juzbao.activity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.os.Bundle;
import android.widget.TextView;

import com.server.api.model.CourseDetail;
import com.android.juzbao.model.ProviderShopBusiness;
import com.android.juzbao.util.CommonValidate;
import com.android.zcomponent.http.api.model.MessageData;
import com.android.zcomponent.views.swipebacklayout.lib.app.SwipeBackActivity;
import com.server.api.service.ShopService;

/**
 * <p>
 * Description: 课程文章详情
 * </p>
 *
 * @ClassName:CourseDocDetailActivity
 * @author: wei
 * @date: 2015-11-10
 */
@EActivity(resName = "activity_course_detail")
public class CourseDocDetailActivity extends SwipeBackActivity {

    @ViewById(resName = "tvew_title_show")
    TextView mTvewTitle;

    @ViewById(resName = "tvew_desc_show")
    TextView mTvewDesc;

    private String mstrArticleId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    void initUI() {
        getTitleBar().setTitleText("文章详情");

        getDataEmptyView().showViewWaiting();
        mstrArticleId = getIntent().getStringExtra("id");
        ProviderShopBusiness.queryCourseDetail(getHttpDataLoader(), mstrArticleId);
    }

    @Override
    public void onDataEmptyClickRefresh() {
        ProviderShopBusiness.queryCourseDetail(getHttpDataLoader(), mstrArticleId);
    }

    @Override
    public void onRecvMsg(MessageData msg) throws Exception {
        if (msg.valiateReq(ShopService.CourseDetailRequest.class)) {
            CourseDetail response =
                    (CourseDetail) msg.getRspObject();
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