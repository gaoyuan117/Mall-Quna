
package com.android.juzbao.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.TextView;

import com.android.juzbao.activity.SpareActivity_;
import com.android.juzbao.adapter.Category1Adapter;
import com.android.juzbao.adapter.Category2Adapter;
import com.android.juzbao.enumerate.CategoryType;
import com.android.juzbao.enumerate.ProductType;
import com.android.juzbao.model.ProductBusiness;
import com.android.juzbao.model.ProviderProductBusiness;
import com.android.juzbao.util.CommonValidate;
import com.android.mall.resource.R;
import com.android.zcomponent.common.uiframe.fragment.BaseFragment;
import com.android.zcomponent.http.api.model.MessageData;
import com.android.zcomponent.http.constant.ErrorCode;
import com.server.api.model.AdProduct;
import com.server.api.model.ProductCategory;
import com.server.api.model.ProductItem;
import com.server.api.service.ProductService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Description: 需求分类
 * </p>
 */
@EFragment(R.layout.fragment_categroy_free)
public class CategroyFreeFragment extends BaseFragment implements
        BaseFragment.OnFragmentCreatedListener,
        ExpandableListView.OnChildClickListener {

    @ViewById(R.id.tvew_category_name)
    TextView mTvewCategoryName;

    @ViewById(R.id.common_listview_show)
    ExpandableListView mListView;

    @ViewById(R.id.gvew_category_2_show)
    GridView mGvewCategory2;

    private Category1Adapter mCategory1Adapter;

    private List<ProductCategory.CategoryItem> mlistCategory;

    private List<ProductItem> mlistProduct;

    private List<ProductCategory.CategoryItem> mlistCategory2;

    private String mProductType = ProductType.PUTONG.getValue();
    private String mGroupId;

    @AfterViews
    void initUI() {
        mListView.setGroupIndicator(null);
        Intent intent = getActivity().getIntent();
        String type = intent.getStringExtra("type");
        if (!TextUtils.isEmpty(type)){
            mProductType = type;
        }
        String gid = intent.getStringExtra("gid");
        if (!TextUtils.isEmpty(gid)){
            mGroupId = gid;
        }
        mListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                onCategory1ItemClick(groupPosition);
                return false;
            }
        });

        mListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                for (int i = 0; i < mCategory1Adapter.getGroupCount(); i++) {
                    if (groupPosition != i) {
                        mListView.collapseGroup(i);
                    }
                }
            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ProductBusiness
                .queryAdProducts(getHttpDataLoader(), "category_header");
        ProductBusiness.queryGroup(getHttpDataLoader(), CategoryType.FREE.getValue());
        if (!TextUtils.isEmpty(mGroupId)){
            ProviderProductBusiness.queryCategory(CategroyFreeFragment.this.getHttpDataLoader(), mGroupId);
        }
    }

    @Override
    public void onRecvMsg(MessageData msg) throws Exception {
        if (msg.valiateReq(ProductService.GroupRequest.class)) {
            ProductCategory reaponse = msg.getRspObject();
            if (CommonValidate.validateQueryState(getActivity(), msg, reaponse)) {
                mlistCategory =
                        new ArrayList<>(reaponse.Data);
                mCategory1Adapter =
                        new Category1Adapter(getActivity(), mlistCategory);
                mCategory1Adapter.setOnChildClickListener(this);
                mListView.setAdapter(mCategory1Adapter);

                if (!TextUtils.isEmpty(mGroupId)){
                    for (int i = 0; i < mlistCategory.size(); i++) {
                        ProductCategory.CategoryItem item = mlistCategory.get(i);
                        if (item.id.equals(mGroupId)) {
                            mListView.expandGroup(i);
                            onCategory1ItemClick(i);
                        }
                    }
                }else{
                    onCategory1ItemClick(0);
                }
                mListView.expandGroup(0);
            }
        } else if (msg.valiateReq(ProductService.CategoryTreeRequest.class)) {
            ProductCategory reaponse = msg.getRspObject();
            if (null != reaponse) {
                if (reaponse.code == ErrorCode.INT_QUERY_DATA_SUCCESS
                        && null != reaponse.Data) {
                    for (int i = 0; i < mlistCategory.size(); i++) {
                        ProductCategory.CategoryItem item = mlistCategory.get(i);
                        if (item.id.equals(reaponse.Data.get(0).gid)) {
                            mlistCategory.get(i)._child = reaponse.Data;
                        }
                    }
                    mCategory1Adapter.notifyDataSetChanged();
                    onCategory2ItemClick(mCategory1Adapter.getSelectPosition(), 0);
                }
            }
        }else if (msg.valiateReq(ProductService.ThirdCategoryTreeRequest.class)) {
            ProductCategory reaponse = msg.getRspObject();
            if (null != reaponse) {
                if (reaponse.code == ErrorCode.INT_QUERY_DATA_SUCCESS
                        && null != reaponse.Data) {

                    for (int i = 0; i < mlistCategory.size(); i++) {
                        List<ProductCategory.CategoryItem > items = mlistCategory.get(i)._child;
                        if (null != items){
                            for (int j = 0; j < items.size(); j++) {
                                if (items.get(j).id.equals(reaponse.Data.get(0).gid)) {
                                    mlistCategory.get(i)._child.get(j)._child = reaponse.Data;
                                }
                            }
                        }
                    }
                    mlistCategory2 = reaponse.Data;
                    mGvewCategory2.setAdapter(new Category2Adapter(getActivity(),
                            mlistCategory2));
                }
            }
        }else if (msg.valiateReq(ProductService.AdvertRequest.class)) {
            AdProduct response = (AdProduct) msg.getRspObject();
            if (CommonValidate.validateQueryState(getActivity(), msg, response)) {
                ProductBusiness.bindSliderLayout(this,
                        R.id.flayout_slider_image, response.Data);
            } else {

            }
        }
    }

    public void selectCategory1(String gid){
        if (null == mlistCategory){
            mGroupId = gid;
            return;
        }
        int position = 0;
        for (int i = 0; i < mlistCategory.size(); i++) {
            if (mlistCategory.get(i).id.equals(gid)){
                position = i;
                break;
            }
        }
        onCategory1ItemClick(position);
        mListView.expandGroup(position);
    }

    public void onCategory1ItemClick(int position) {

        if (null != mCategory1Adapter) {
            if (position == mCategory1Adapter.getSelectPosition()) {
                return;
            }
            mCategory1Adapter.setSelectPosition(position);
            mCategory1Adapter.setChildSelectPosition(-1);
        }
        if (mlistCategory.size() > position) {
            mTvewCategoryName.setText(mlistCategory.get(position).title);
        }

        if (null != mlistCategory.get(position)._child){
            onCategory2ItemClick(position, 0);
        }else {
            ProviderProductBusiness.queryCategory(CategroyFreeFragment.this.getHttpDataLoader(),
                    mlistCategory.get(position).id);
        }
    }

    public void onCategory2ItemClick(int group, int position) {

        mCategory1Adapter.setChildSelectPosition(position);
        String id = mlistCategory.get(group)._child.get(position).id;
        List<ProductCategory.CategoryItem> child = mlistCategory.get(group)._child.get(position)._child;
        mTvewCategoryName.setText(mlistCategory.get(group)._child.get(position).title);
        if (null == child){
            ProviderProductBusiness.queryThirdCategory(getHttpDataLoader(), id);
        }else {
            mlistCategory2 = child;
            mGvewCategory2.setAdapter(new Category2Adapter(getActivity(),
                    mlistCategory2));
        }
    }

    @ItemClick(R.id.gvew_category_2_show)
    void onCategory3ItemClick(int position) {
       String id = mlistCategory2.get(position).id;
        ((SpareActivity_)getActivity()).refreshData(id);
    }

    @Click(R.id.rlayout_search_click)
    void onClickRlayoutSearch() {
        ProductBusiness.intentToSearchActivity(getActivity(), mProductType);
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        onCategory2ItemClick(groupPosition, childPosition);
        return false;
    }

    @Override
    public void onFragmentCreated() {
        if (!TextUtils.isEmpty(mGroupId)){
            selectCategory1(mGroupId);
        }else{
            onCategory1ItemClick(0);
        }
    }
}