
package com.android.juzbao.activity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.android.juzbao.adapter.ReviewAdapter;
import com.android.juzbao.model.ReviewBusiness;
import com.android.juzbao.util.CommonValidate;
import com.android.mall.resource.R;
import com.android.zcomponent.http.api.model.MessageData;
import com.android.zcomponent.util.CommonUtil;
import com.android.zcomponent.util.PageInditor;
import com.android.zcomponent.views.PullToRefreshView;
import com.android.zcomponent.views.PullToRefreshView.OnFooterRefreshListener;
import com.android.zcomponent.views.PullToRefreshView.OnHeaderRefreshListener;
import com.android.zcomponent.views.swipebacklayout.lib.app.SwipeBackActivity;
import com.server.api.model.ReviewItem;
import com.server.api.model.ReviewPageResult;
import com.server.api.service.ReviewService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * <p>
 * Description: 商品详情  评价列表
 * </p>
 */
@EActivity(R.layout.activity_review)
public class ReviewActivity extends SwipeBackActivity implements
        OnHeaderRefreshListener, OnFooterRefreshListener {

    @ViewById(R.id.common_listview_show)
    ListView mListView;

    @ViewById(R.id.common_pull_refresh_view_show)
    PullToRefreshView mPullToRefreshView;

    private PageInditor<ReviewItem> mPageInditor =
            new PageInditor<ReviewItem>();

    private ReviewAdapter mAdapter;

    private String mProductId;

    private int rate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    void initUI() {
        mPullToRefreshView.setOnHeaderRefreshListener(this);
        mPullToRefreshView.setOnFooterRefreshListener(this);
        mProductId = getIntent().getStringExtra("productid");
        getTitleBar().setTitleText("评价");
        getDataEmptyView().showViewWaiting();
        selectReviewRate(1);
    }

    @Override
    public void onRecvMsg(MessageData msg) throws Exception {
        if (msg.valiateReq(ReviewService.GetProductReviewRequest.class)) {
            if (null != mPageInditor) {
                mPageInditor.clear();
            }

            ReviewPageResult response = (ReviewPageResult) msg.getRspObject();
            if (CommonValidate.validateQueryState(this, msg, response)) {
                if (null != response.Data && null != response.Data.Result) {
                    mPageInditor.add(response.Data.Result);
                }
                if (null == mAdapter) {
                    mAdapter = new ReviewAdapter(this, mPageInditor.getAll());
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
                    String message = "";

                    if (rate == 1) {
                        message = "暂无好评";
                    } else if (rate == 2) {
                        message = "没有中评";
                    } else if (rate == 3) {
                        message = "没有差评";
                    }
                    getDataEmptyView().showViewDataEmpty(false, true, msg,
                            message);
                }
            }
            mPullToRefreshView.onFooterRefreshComplete();
            mPullToRefreshView.onHeaderRefreshComplete();
        }
    }

    @Click(R.id.tvew_review_best)
    void onClickTvewBest() {
        selectReviewRate(1);
    }

    @Click(R.id.tvew_review_better)
    void onClickTvewBetter() {
        selectReviewRate(2);
    }

    @Click(R.id.tvew_review_bad)
    void onClickTvewBad() {
        selectReviewRate(3);
    }

    private void selectReviewRate(int rate) {
        this.rate = rate;
        TextView tvewRateBest = (TextView) findViewById(R.id.tvew_review_best);
        TextView tvewRateBetter = (TextView) findViewById(R.id.tvew_review_better);
        TextView tvewRateBad = (TextView) findViewById(R.id.tvew_review_bad);

        CommonUtil.setDrawableLeft(this, tvewRateBest, R.drawable.cart_option);
        CommonUtil.setDrawableLeft(this, tvewRateBetter, R.drawable.cart_option);
        CommonUtil.setDrawableLeft(this, tvewRateBad, R.drawable.cart_option);

        if (rate == 1) {
            CommonUtil.setDrawableLeft(this, tvewRateBest,
                    R.drawable.cart_option_on);
        } else if (rate == 2) {
            CommonUtil.setDrawableLeft(this, tvewRateBetter,
                    R.drawable.cart_option_on);
        } else if (rate == 3) {
            CommonUtil.setDrawableLeft(this, tvewRateBad,
                    R.drawable.cart_option_on);
        }

        refreshData(true);
    }

    public void refreshData(boolean isPullRefresh) {
        mPageInditor.setPullRefresh(isPullRefresh);
        ReviewBusiness.queryReviews(getHttpDataLoader(), mProductId, rate,
                mPageInditor.getPageNum());
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