package com.android.juzbao.fragment;

import android.widget.GridView;

import com.android.juzbao.adapter.CategoryRecomAdapter;
import com.android.zcomponent.common.uiframe.fragment.BaseFragment;
import com.server.api.model.AdProduct;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.asList;


/**
 * 分类推荐商品列表
 */
@EFragment(com.android.mall.resource.R.layout.fragment_ad_category)
public class AdCategoryFragment extends BaseFragment {

    @ViewById(com.android.mall.resource.R.id.gvew_recommend_show)
    GridView mGridView;

    private CategoryRecomAdapter mAdapter;

    private List<AdProduct.AdItem> mAdItems = new ArrayList<>();

    @AfterViews
    void initUI() {
        mAdapter = new CategoryRecomAdapter(getActivity(), mAdItems);
        mGridView.setAdapter(mAdapter);
    }

    public void refreshData(AdProduct.AdItem[] adItems) {
        if (adItems == null) {
            return;
        }

        if (adItems.length == 0) {
            this.removeData();
            return;
        }

        mAdItems.clear();
        mAdItems.addAll(Arrays.asList(adItems));

        if (null == mAdapter) {
            mAdapter = new CategoryRecomAdapter(getActivity(), mAdItems);
            mGridView.setAdapter(mAdapter);
        } else {
            mAdapter.setData(mAdItems);
        }
    }

    public void addData(AdProduct.AdItem[] adItems) {
        if (adItems == null) {
            return;
        }
        if (adItems.length == 0) {
            return;
        }
        if (null == mAdapter) {
            mAdapter = new CategoryRecomAdapter(getActivity(), mAdItems);
            mGridView.setAdapter(mAdapter);
        } else {
            mAdapter.addData(asList(adItems));
        }
    }

    public void removeData() {
        mAdapter.clear();
    }

    public void showWaitingDialog(boolean isShow) {
        if (isShow) {
            showWaitDialog(2, false, "正在查询", false);
//            getDataEmptyView().showViewWaiting();
        } else {
//            getDataEmptyView().dismiss();
            dismissWaitDialog();
        }
    }

    public boolean isWaitingDialogShow() {
        return isDialogShowing();
    }
}
