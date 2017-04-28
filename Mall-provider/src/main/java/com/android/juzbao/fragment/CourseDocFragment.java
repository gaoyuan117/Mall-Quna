
package com.android.juzbao.fragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import android.widget.ListView;

import com.server.api.model.CourseItem;
import com.server.api.model.CoursePageResult;
import com.android.juzbao.provider.R;
import com.android.juzbao.adapter.CourseDocAdapter;
import com.android.juzbao.model.ProviderOrderBusiness.OrderHelper;
import com.android.juzbao.model.ProviderShopBusiness;
import com.android.juzbao.enumerate.CourseType;
import com.android.juzbao.util.CommonValidate;
import com.android.zcomponent.adapter.CommonAdapter;
import com.android.zcomponent.common.uiframe.fragment.BaseFragment;
import com.android.zcomponent.http.api.model.MessageData;
import com.android.zcomponent.util.PageInditor;
import com.android.zcomponent.views.PullToRefreshView;
import com.android.zcomponent.views.PullToRefreshView.OnFooterRefreshListener;
import com.android.zcomponent.views.PullToRefreshView.OnHeaderRefreshListener;
import com.server.api.service.ShopService;

/**
 * <p>
 * Description: 趣那学院
 * </p>
 *
 * @ClassName:CourseFragment
 * @author: wei
 * @date: 2015-11-10
 */
@EFragment(resName = "fragment_order")
public class CourseDocFragment extends BaseFragment implements
        OnHeaderRefreshListener, OnFooterRefreshListener {
    private CourseType mCourseType;

    private CommonAdapter mAdapter;

    @ViewById(resName = "common_listview_show")
    ListView mListView;

    @ViewById(resName = "common_pull_refresh_view_show")
    PullToRefreshView mPullToRefreshView;

    private boolean isInit = false;

    private PageInditor<CourseItem> mPageInditor;

    private OrderHelper mOrderHelper;

    public CourseDocFragment() {

    }

    public void setCourseType(CourseType courseType) {
        mCourseType = courseType;
    }

    @AfterViews
    void initUI() {
        mPullToRefreshView.setOnHeaderRefreshListener(this);
        mPullToRefreshView.setOnFooterRefreshListener(this);
        getDataEmptyView().setBackgroundResource(R.drawable.transparent);
        mPageInditor = new PageInditor<CourseItem>();
        mOrderHelper = new OrderHelper(getActivity(), getHttpDataLoader());
    }

    @Override
    public void onRecvMsg(MessageData msg) throws Exception {
        if (null != mOrderHelper) {
            mOrderHelper.onRecvMsg(msg);
        }

        if (msg.valiateReq(ShopService.CourseListRequest.class)) {
            if (null != mPageInditor) {
                mPageInditor.clear();
            }

            CoursePageResult response = (CoursePageResult) msg.getRspObject();
            if (CommonValidate.validateQueryState(getActivity(), msg, response)) {
                if (null != response.Data && null != response.Data.Result) {
                    mPageInditor.add(response.Data.Result);
                }
                if (null == mAdapter) {
                    mAdapter =
                            new CourseDocAdapter(getActivity(),
                                    mPageInditor.getAll());
                    mPageInditor.bindAdapter(mListView, mAdapter);
                }
                if (mPageInditor.getAll().size() == response.Data.Total) {
                    mPullToRefreshView.setFooterRefreshComplete();
                } else {
                    mPullToRefreshView.setFooterVisible();
                }

                getDataEmptyView().dismiss();
            } else {
                if (mPageInditor.getAll().size() == 0) {
                    getDataEmptyView().showViewDataEmpty(true, true, msg,
                            "还没有相关的课程");
                }
            }
            mPullToRefreshView.onFooterRefreshComplete();
            mPullToRefreshView.onHeaderRefreshComplete();
        }
    }

    @Override
    public void onDataEmptyClickRefresh() {
        getDataEmptyView().showViewWaiting();
        refreshData(true);
    }

    public void queryListData() {
        if (isInit) {
            return;
        }
        isInit = true;
        getDataEmptyView().showViewWaiting();
        refreshData(true);
    }

    public void refreshData(boolean isPullRefresh) {
        if (null == mPageInditor) {
            mPageInditor = new PageInditor<CourseItem>();
        }
        mPageInditor.setPullRefresh(isPullRefresh);

        ProviderShopBusiness.queryCourse(getHttpDataLoader(),
                CourseType.DOC.getValue(), mPageInditor.getPageNum());
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