
package com.android.juzbao.activity.me;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.android.mall.resource.R;
import com.android.juzbao.activity.ShopDetailActivity_;
import com.server.api.model.CommonReturn;
import com.server.api.model.ShopFavorite;
import com.server.api.model.ShopFavoritePageResult;
import com.android.juzbao.adapter.FavoriteShopAdapter;
import com.android.juzbao.adapter.FavoriteShopAdapter.OnItemActionClickListener;
import com.android.juzbao.model.FavoriteBusiness;
import com.android.juzbao.constant.ResultActivity;
import com.android.juzbao.util.CommonValidate;
import com.android.zcomponent.http.api.model.MessageData;
import com.android.zcomponent.http.constant.ErrorCode;
import com.android.zcomponent.util.PageInditor;
import com.android.zcomponent.util.ShowMsg;
import com.android.zcomponent.views.PullToRefreshView;
import com.android.zcomponent.views.PullToRefreshView.OnFooterRefreshListener;
import com.android.zcomponent.views.PullToRefreshView.OnHeaderRefreshListener;
import com.android.zcomponent.views.swipebacklayout.lib.app.SwipeBackActivity;
import com.server.api.service.FavoriteService;

/**
 * <p>
 * Description: 收藏的店铺
 * </p>
 *
 * @ClassName:MyFavoriteShopActivity
 * @author: wei
 * @date: 2015-11-10
 */
@EActivity(R.layout.activity_my_favorite_shop)
public class MyFavoriteShopActivity extends SwipeBackActivity implements
        OnHeaderRefreshListener, OnFooterRefreshListener,
        OnItemActionClickListener {

    @ViewById(R.id.common_listview_show)
    ListView mListView;

    @ViewById(R.id.common_pull_refresh_view_show)
    PullToRefreshView mPullToRefreshView;

    FavoriteShopAdapter mAdapter;

    private PageInditor<ShopFavorite> mPageInditor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    void initUI() {
        getTitleBar().setTitleText("收藏的店铺");

        mPullToRefreshView.setOnHeaderRefreshListener(this);
        mPullToRefreshView.setOnFooterRefreshListener(this);
        mPageInditor = new PageInditor<ShopFavorite>();

        getDataEmptyView().showViewWaiting();
        getDataEmptyView().setBackgroundResource(R.drawable.transparent);
        refreshData(true);
    }

    @Override
    public void onRecvMsg(MessageData msg) throws Exception {
        if (msg.valiateReq(FavoriteService.FavoriteShopsRequest.class)) {
            if (null != mPageInditor) {
                mPageInditor.clear();
            }

            ShopFavoritePageResult response =
                    (ShopFavoritePageResult) msg.getRspObject();
            if (CommonValidate.validateQueryState(this, msg, response)) {
                if (null != response.data && null != response.data.Result) {
                    mPageInditor.add(response.data.Result);
                }
                if (null == mAdapter) {
                    mAdapter =
                            new FavoriteShopAdapter(getApplicationContext(),
                                    mPageInditor.getAll());
                    mAdapter.setOnItemActionClickListener(this);
                    mPageInditor.bindAdapter(mListView, mAdapter);
                }

                if (mPageInditor.getAll().size() == response.data.Total) {
                    mPullToRefreshView.setFooterRefreshComplete();
                } else {
                    mPullToRefreshView.setFooterVisible();
                }

                getTitleBar().showRightTextView("编辑");
                getDataEmptyView().dismiss();
            } else {
                if (mPageInditor.getAll().size() == 0) {
                    getTitleBar().getRightTextView().setVisibility(View.GONE);
                    getDataEmptyView().showViewDataEmpty(false, true, msg,
                            "还没有收藏的店铺");
                }
            }
            mPullToRefreshView.onFooterRefreshComplete();
            mPullToRefreshView.onHeaderRefreshComplete();
        } else if (msg.valiateReq(FavoriteService.DelShopFavoriteRequest.class)) {
            CommonReturn response = (CommonReturn) msg.getRspObject();
            if (null != response) {
                if (response.code == ErrorCode.INT_QUERY_DATA_SUCCESS) {
                    ShowMsg.showToast(getApplicationContext(), "删除收藏成功");
                    mPageInditor.remove(mPageInditor.getSelectPosition());
                    if (mPageInditor.getAll().size() == 0) {
                        getTitleBar().getRightTextView().setVisibility(
                                View.GONE);
                        getDataEmptyView().showViewDataEmpty(false, true, msg,
                                "还没有收藏的店铺");
                    }
                } else {
                    ShowMsg.showToast(getApplicationContext(), response.message);
                }
            } else {
                ShowMsg.showToast(getApplicationContext(), "删除收藏失败");
            }
        }
    }

    @Override
    public void onTitleBarRightFirstViewClick(View view) {
        if (null != mAdapter) {
            mAdapter.setEdit();
            if (mAdapter.isEdit()) {
                getTitleBar().showRightTextView("完成");
            } else {
                getTitleBar().showRightTextView("编辑");
            }
        }

    }

    private void refreshData(boolean isPullRefresh) {
        mPageInditor.setPullRefresh(isPullRefresh);
        FavoriteBusiness.queryShopFavorite(getHttpDataLoader(),
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

    @Override
    public void onClickDelete(int position) {
        FavoriteBusiness.delShopFavorite(getHttpDataLoader(),
                Integer.parseInt(mPageInditor.get(position).id));
        showWaitDialog(1, false, R.string.common_submit_data);
    }

    @Override
    public void onClickSelect(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt("id", Integer.parseInt(mPageInditor.get(position).shop_id));
        bundle.putString("title", mPageInditor.get(position).shop_title);
        getIntentHandle().intentToActivity(bundle, ShopDetailActivity_.class);
    }

    @Override
    public void onActivityResultCallBack(int resultCode, Intent intent) {
        if (resultCode == ResultActivity.CODE_EDIT_FAVORITE) {
            mPullToRefreshView.headerRefreshing();
            refreshData(true);
        }
    }
}