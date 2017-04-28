
package com.android.juzbao.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.android.juzbao.activity.ProductActivity_;
import com.android.juzbao.adapter.Category1Adapter;
import com.android.juzbao.dao.jifendao.JiFenDao;
import com.android.juzbao.enumerate.CategoryType;
import com.android.juzbao.enumerate.ProductType;
import com.android.juzbao.model.ProductBusiness;
import com.android.juzbao.model.ProviderProductBusiness;
import com.android.juzbao.util.CommonValidate;
import com.android.mall.resource.R;
import com.android.zcomponent.common.uiframe.fragment.BaseFragment;
import com.android.zcomponent.communication.http.Context;
import com.android.zcomponent.communication.http.annotation.HttpPortal;
import com.android.zcomponent.http.api.host.Endpoint;
import com.android.zcomponent.http.api.model.MessageData;
import com.android.zcomponent.http.constant.ErrorCode;
import com.android.zcomponent.json.JsonSerializerFactory;
import com.android.zcomponent.json.WebFormEncoder;
import com.android.zcomponent.util.CommonUtil;
import com.google.gson.annotations.SerializedName;
import com.server.api.model.AdProduct;
import com.server.api.model.ProductCategory;
import com.server.api.model.jifenmodel.IndexBanner;
import com.server.api.service.JiFenService;
import com.server.api.service.ProductService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Description: 分类
 * </p>
 */
@EFragment(R.layout.fragment_categroy)
public class CategroyFragment extends BaseFragment implements BaseFragment.OnFragmentCreatedListener, ExpandableListView.OnChildClickListener {

    //分类描述
    @ViewById(R.id.tvew_category_name)
    TextView mTvewCategoryName;

    @ViewById(R.id.common_listview_show)
    ExpandableListView mListView;

    private Category1Adapter mCategory1Adapter;

    /**
     * 分类数据
     */
    private List<ProductCategory.CategoryItem> mlistCategory;

    /**
     * 当前的商品分类
     */
    private String mProductType = ProductType.PUTONG.getValue();

    /**
     * 当前选择的分类模块gid
     */
    private String mGroupId;

    /**
     * 广告Fragment
     */
    private AdCategoryFragment_ mAdCgFragment;

    /**
     * 用户判断分类推荐请求的，被后台坑了
     */
    private int groupCount = 0;
    private boolean isMoreCategoryTree = true;

    /**
     * 判断Fragment是否第一次添加到布局
     */
    private boolean isPrepare = false;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDataEmptyView().showViewWaiting();

//        ProductBusiness.queryAdProducts(getHttpDataLoader(), "category_header");
        ProductBusiness.queryGroup(getHttpDataLoader(), CategoryType.PRODCUT.getValue());
        JiFenDao.sendQueryCategoryBanner(getHttpDataLoader(),"category_header");
    }

    @AfterViews
    void initUI() {
        mListView.setGroupIndicator(null);

        mListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

                if (!CommonUtil.isLeastSingleClick(500)) {
                    return false;
                }
                //如果数据没有请求完成，不能点击ListView。
                if (mAdCgFragment.isWaitingDialogShow()) {
                    return false;
                }

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
        //在这里添加Fragment，防止没初始化，不能设置数据。
        mAdCgFragment = new AdCategoryFragment_();
        addFragment(R.id.flayout_product, mAdCgFragment);
    }

    @Override
    public void onRecvMsg(MessageData msg) throws Exception {
        if (msg.valiateReq(ProductService.GroupRequest.class)) {
            //查询分类模块
            ProductCategory reaponse = msg.getRspObject();
            if (CommonValidate.validateQueryState(getActivity(), msg, reaponse)) {
                getDataEmptyView().dismiss();

                mlistCategory = new ArrayList<>(reaponse.Data);
                mCategory1Adapter = new Category1Adapter(getActivity(), mlistCategory);
                mCategory1Adapter.setOnChildClickListener(this);
                mListView.setAdapter(mCategory1Adapter);

                if (TextUtils.isEmpty(mGroupId)) {
                    /*
                    展开第一个gid,这里由于Fragment没有初始化完成，所以是不能展开第一项的。
                     */
                    for (int i = 0; i < mlistCategory.size(); i++) {
                        if ("product".equals(mlistCategory.get(i).group)) {
                            ProviderProductBusiness.queryCategory(getHttpDataLoader(), mlistCategory.get(i).id);
                            break;
                        }
                    }
                    mListView.expandGroup(0);
                } else {
                    for (int i = 0; i < mlistCategory.size(); i++) {
                        ProductCategory.CategoryItem item = mlistCategory.get(i);
                        if (item.id.equals(mGroupId)) {
                            //查询当前分类并展开
                            ProviderProductBusiness.queryCategory(getHttpDataLoader(), item.id);
                            mListView.expandGroup(i);
                        }
                    }
                }
            }
        } else if (msg.valiateReq(ProductService.CategoryTreeRequest.class)) {
            //查询二级分类，走这里
            ProductCategory reaponse = msg.getRspObject();
            if (null != reaponse) {
                if (reaponse.code == ErrorCode.INT_QUERY_DATA_SUCCESS && null != reaponse.Data) {

                    //找到自己的孩子。
                    for (int i = 0; i < mlistCategory.size(); i++) {
                        ProductCategory.CategoryItem item = mlistCategory.get(i);
                        if (item.id.equals(reaponse.Data.get(0).gid)) {
                            mlistCategory.get(i)._child = reaponse.Data;
                        }
                    }
                    mCategory1Adapter.notifyDataSetChanged();

                    //判断ListView是否有展开项
                    boolean hasExpanded = false;
                    for (int i = 0; i < mlistCategory.size(); i++) {
                        hasExpanded = mListView.isGroupExpanded(i);
                        if (hasExpanded) {
                            break;
                        }
                    }
                    /*
                    在这里，需要查询每个分类的推荐推荐商品
                     */
                    if (hasExpanded) {
                        if (reaponse.Data.size() > 0) {
                            groupCount = 0;
                            if (reaponse.Data.size() > 1) {
                                if (mAdCgFragment != null) {
                                    mAdCgFragment.showWaitingDialog(true);
                                    //需要先把数据清楚，避免数据叠加
                                    mAdCgFragment.removeData();
                                }
                                //有多个分类商品
                                isMoreCategoryTree = true;
                                for (int i = 0; i < reaponse.Data.size(); i++) {
                                    RecomCategoryRequest req = new RecomCategoryRequest();
                                    req.Position = "category_recom";
                                    req.categoryId = reaponse.Data.get(i).id;
                                    getHttpDataLoader().doPostProcess(req, AdProduct.class);
                                    groupCount++;
                                }
                            } else {
                                //只有一个分类商品
                                isMoreCategoryTree = false;
                                mAdCgFragment.showWaitingDialog(true);
                                RecomCategoryRequest req = new RecomCategoryRequest();
                                req.Position = "category_recom";
                                req.categoryId = reaponse.Data.get(0).id;
                                getHttpDataLoader().doPostProcess(req, AdProduct.class);
                            }
                        }
                    }
                }
            } else {
                //没有要查询的分类推荐
                if (mAdCgFragment != null) {
                    mAdCgFragment.removeData();
                    mAdCgFragment.showWaitingDialog(false);
                }
            }
        } else if (msg.valiateReq(ProductService.AdvertRequest.class)) {
            AdProduct response = (AdProduct) msg.getRspObject();
            if (CommonValidate.validateQueryState(getActivity(), msg, response)) {
                ProductBusiness.bindSliderLayout(this, R.id.flayout_slider_image, response.Data);
            } else {

            }
        } else if (msg.valiateReq(RecomCategoryRequest.class)) {
            //分类推荐
            AdProduct response = msg.getRspObject();
            if (response != null) {
                if (response.code == ErrorCode.INT_QUERY_DATA_SUCCESS && response.Data != null) {
                    /*
                    这里可能会多次调用，怎么办呢？
                     */
                    if (isMoreCategoryTree) {
                        //多个二级分类
                        mAdCgFragment.addData(response.Data);
                        mAdCgFragment.showWaitingDialog(false);
                    } else {
                        //只有一个二级分类
                        mAdCgFragment.refreshData(response.Data);
                        mAdCgFragment.showWaitingDialog(false);
                    }
                } else {

                }
            } else {
                if (isMoreCategoryTree) {
                    groupCount--;
                    //要查询多个分类，但是一个都没有推荐。
                    if (groupCount == 0) {
                        if (mAdCgFragment != null) {
                            mAdCgFragment.removeData();
                            mAdCgFragment.showWaitingDialog(false);
                        }
                    }
                } else {
                    //至查询了一个分类，但是没有推荐。
                    if (mAdCgFragment != null) {
                        mAdCgFragment.removeData();
                        mAdCgFragment.showWaitingDialog(false);
                    }
                }
            }
        }else if (msg.valiateReq(JiFenService.JifenCategorybannerRequest.class)){
            IndexBanner response =  msg.getRspObject();
            if (CommonValidate.validateQueryState(getActivity(), msg, response)) {
                ProductBusiness.bindIndexBannderLayout(this, R.id.flayout_slider_image, response.data);
            }
        }
    }

    /**
     * 设置当前gid，从HomeFragment跳转
     */
    public void selectCategory1(String gid) {

        if (null == mlistCategory) {
            //第一次点击分类模块走这里
            mGroupId = gid;
            return;
        }

        int position = 0;
        for (int i = 0; i < mlistCategory.size(); i++) {
            if (mlistCategory.get(i).id.equals(gid)) {
                position = i;
                break;
            }
        }
        onCategory1ItemClick(position);
        mListView.expandGroup(position);
    }

    /**
     * 点击一级分类
     */
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

        ProductCategory.CategoryItem child = mlistCategory.get(position);
        if (null != child) {
            String categoryId = child.id;
            ProviderProductBusiness.queryCategory(CategroyFragment.this.getHttpDataLoader(), categoryId);
        }
    }

    public void onCategory2ItemClick(int group, int position) {

        mCategory1Adapter.setChildSelectPosition(position);

        List<ProductCategory.CategoryItem> child = mlistCategory.get(group)._child;
        if (null != child && child.size() > position) {
            String categoryId = mlistCategory.get(group)._child.get(position).id;
            String categoryName = mlistCategory.get(group)._child.get(position).title;
            Bundle bundle = new Bundle();
            bundle.putString("child", JsonSerializerFactory.Create().encode(mlistCategory.get(group)._child.get(position)._child));
            bundle.putString("title", categoryName);
            bundle.putString("category_id", categoryId);
            bundle.putString("type", mProductType);
            getIntentHandle().intentToActivity(bundle, ProductActivity_.class);
        }
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

    /**
     * 在onActivityCreate回调该方法。
     */
    @Override
    public void onFragmentCreated() {
        if (!TextUtils.isEmpty(mGroupId)) {
//            selectCategory1(mGroupId);
        } else {
            onCategory1ItemClick(0);
        }
    }

    /**
     * 在这里定义请求对象试试
     */
    @HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Ad/getList", method = Context.Method.Post, encoder = WebFormEncoder.class)
    public static class RecomCategoryRequest extends Endpoint {

        @SerializedName("position")
        public String Position;

        @SerializedName("category_id")
        public String categoryId;
    }
}