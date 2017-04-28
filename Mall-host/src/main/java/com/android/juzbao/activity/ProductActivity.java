
package com.android.juzbao.activity;

import android.content.Intent;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import com.android.juzbao.adapter.Category3Adapter;
import com.android.juzbao.fragment.ProductFragment_;
import com.android.juzbao.model.ProviderProductBusiness;
import com.android.juzbao.util.CommonValidate;
import com.android.mall.resource.R;
import com.android.zcomponent.http.api.model.MessageData;
import com.android.zcomponent.json.JsonSerializerFactory;
import com.android.zcomponent.util.CommonUtil;
import com.android.zcomponent.util.ShowMsg;
import com.android.zcomponent.util.StringUtil;
import com.android.zcomponent.views.HorizontialListView;
import com.android.zcomponent.views.swipebacklayout.lib.app.SwipeBackActivity;
import com.server.api.model.ProductCategory;
import com.server.api.service.ProductService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 商品
 */
@EActivity(R.layout.activity_product)
public class ProductActivity extends SwipeBackActivity {

    @ViewById(R.id.common_title_tvew_txt)
    EditText medtvewSearch;

    @ViewById(R.id.tvew_option_default_show)
    TextView mtvewDefault;

    @ViewById(R.id.tvew_option_brower_show)
    TextView mtvewBrower;

    @ViewById(R.id.tvew_option_filter_show)
    TextView mtvewFilter;

    @ViewById(R.id.hlivew_cateogry)
    HorizontialListView mhlvewCategory;

    private String mstrCategoryId;

    private List<ProductCategory.CategoryItem> mlistCategory;

    private ProductFragment_ productFragment;

    private Category3Adapter mCategoryAdapter;
    @AfterViews
    void initUI() {

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        medtvewSearch.setText(title);
        CommonUtil.setEditViewSelectionEnd(medtvewSearch);

        productFragment = new ProductFragment_();
        productFragment.setArguments(intent.getExtras());
        addFragment(R.id.flayout_product, productFragment);

        mstrCategoryId = getIntent().getStringExtra("category_id");
        String child = getIntent().getStringExtra("child");
        if (!TextUtils.isEmpty(child)){
            ProductCategory.CategoryItem[] items = JsonSerializerFactory.Create().decode(child, ProductCategory.CategoryItem[].class);
            if (null != items){
                mlistCategory = Arrays.asList(items);
            }
        }

        mhlvewCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCategoryAdapter.setSelectPosition(position);
                productFragment.headRefreshing();
                productFragment.setCurCategoryId(mlistCategory.get(position).id);
                productFragment.refreshData(true);
            }
        });

        medtvewSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String strMsg = s.toString();
                if (!StringUtil.isEmptyString(strMsg)) {
                    productFragment.searchData(true, strMsg);
                } else {
                    productFragment.clearCache();
                }
            }
        });
    }

    public void queryThirdCategory(){
        if (null == mlistCategory){
            ProviderProductBusiness.queryThirdCategory(getHttpDataLoader(), mstrCategoryId);
        }else{
            showCategory();
        }
    }

    private void showCategory() {
        if (null == mCategoryAdapter){
            mCategoryAdapter = new Category3Adapter(this, mlistCategory);
            mhlvewCategory.setAdapter(mCategoryAdapter);
        }else {
            mCategoryAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onRecvMsg(MessageData msg) throws Exception {
        if (msg.valiateReq(ProductService.ThirdCategoryTreeRequest.class)) {
            ProductCategory reaponse = msg.getRspObject();
            if (CommonValidate.validateQueryState(this, msg, reaponse)) {
                mlistCategory =
                        new ArrayList<ProductCategory.CategoryItem>(reaponse.Data);
                showCategory();
            }else {
                ShowMsg.showToast(this, "未查询到商品分类");
            }
        }
    }

    @Click(R.id.rlayout_option_default_click)
    void onClickRlayoutDefault() {
        initFilter(mtvewDefault);
        productFragment.setCurSort("");
        productFragment.refreshData();
        mhlvewCategory.setVisibility(View.GONE);
        if (null != mCategoryAdapter){
            mCategoryAdapter.setSelectPosition(-1);
        }
    }

    @Click(R.id.rlayout_option_brower_click)
    void onClickRlayoutBrower() {
        initFilter(mtvewBrower);
        productFragment.setCurSort("1");
        productFragment.refreshData();
        mhlvewCategory.setVisibility(View.GONE);
        if (null != mCategoryAdapter){
            mCategoryAdapter.setSelectPosition(-1);
        }
    }

    @Click(R.id.rlayout_option_filter_click)
    void onClickRlayoutFilter() {
        initFilter(mtvewFilter);
        CommonUtil.setDrawableRight(this, mtvewFilter, R.drawable.icon38_on);
        if (mhlvewCategory.getVisibility() == View.VISIBLE){
            mhlvewCategory.setVisibility(View.GONE);
        }else{
            mhlvewCategory.setVisibility(View.VISIBLE);
        }
        queryThirdCategory();
    }

    private void initFilter(TextView tvewClick){
        mtvewDefault.setTextColor(getResources().getColor(R.color.black));
        mtvewBrower.setTextColor(getResources().getColor(R.color.black));
        mtvewFilter.setTextColor(getResources().getColor(R.color.black));
        CommonUtil.setDrawableRight(this, mtvewFilter, R.drawable.icon37);
        tvewClick.setTextColor(Color.rgb(0xCD, 0x02, 0x02));
    }
}
