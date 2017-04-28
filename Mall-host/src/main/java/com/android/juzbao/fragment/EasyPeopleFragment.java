package com.android.juzbao.fragment;

import android.os.Bundle;
import android.widget.GridView;

import com.android.juzbao.adapter.EasyPeopleAdapter;
import com.android.juzbao.dao.jifendao.JiFenDao;
import com.android.quna.activity.R;
import com.android.zcomponent.common.uiframe.fragment.BaseFragment;
import com.android.zcomponent.http.api.model.MessageData;
import com.android.zcomponent.util.PageInditor;
import com.android.zcomponent.views.PullToRefreshView;
import com.server.api.model.jifenmodel.EasyPeople;
import com.server.api.service.JiFenService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/**
 * Created by Koterwong on 2016/7/28.
 * <p>
 * 便民信息。
 */
@EFragment(R.layout.fragment_easy_people)
public class EasyPeopleFragment extends BaseFragment implements
        PullToRefreshView.OnHeaderRefreshListener,
        PullToRefreshView.OnFooterRefreshListener {


    @ViewById(R.id.pull_to_refresh_gridview)
    PullToRefreshView mPullToRefreshView;

    @ViewById(R.id.easy)
    GridView mGridView;

    private EasyPeopleAdapter mAdapter;

    private PageInditor<EasyPeople.Data> mPageInditor = new PageInditor<EasyPeople.Data>();

    @AfterViews
    void initUI() {

        mPullToRefreshView.setFooterInvisible();
//        mPullToRefreshView.setOnFooterRefreshListener(this);
        mPullToRefreshView.setOnHeaderRefreshListener(this);
        getDataEmptyView().showViewWaiting();

    }

    @Override public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //获取便民信息
        JiFenDao.sendQueryEasyPeople(getHttpDataLoader());
    }

    @Override public void onRecvMsg(MessageData msg) throws Exception {
        if (msg.valiateReq(JiFenService.EasyPeopleRequest.class)) {
            EasyPeople response = msg.getRspObject();
            getDataEmptyView().dismiss();
            if (mPullToRefreshView.isHeaderRefresh()) {
                mPullToRefreshView.onHeaderRefreshComplete();
            }
            if (response != null) {
                if (response.data != null) {
                    mPageInditor.clear();
                    if (mAdapter == null) {
                        mAdapter = new EasyPeopleAdapter(getActivity(), mPageInditor.getAll());
                        mPageInditor.bindAdapter(mGridView, mAdapter);
                    }
                    mPageInditor.add(response.data);
                } else {
                    getDataEmptyView().showViewDataEmpty(false, false, msg, "为查询到便民信息");
                }
            } else {
                getDataEmptyView().showViewDataEmpty(false, false, msg, "为查询到便民信息");
            }
        }
    }

    public void resfreshData(boolean isPullRefresh) {
        if (isPullRefresh) {
            mPageInditor.setPullRefresh(isPullRefresh);
            JiFenDao.sendQueryEasyPeople(getHttpDataLoader());
        }
    }

    @Override public void onHeaderRefresh(PullToRefreshView view) {
        resfreshData(true);
    }

    @Override public void onFooterRefresh(PullToRefreshView view) {

    }
}
