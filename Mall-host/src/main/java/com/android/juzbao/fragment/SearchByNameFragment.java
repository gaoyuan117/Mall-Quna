
package com.android.juzbao.fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.juzbao.adapter.ProductAdapter;
import com.android.juzbao.enumerate.ProductType;
import com.android.juzbao.model.ProductBusiness;
import com.android.mall.resource.R;
import com.android.zcomponent.adapter.CommonAdapter;
import com.android.zcomponent.common.uiframe.fragment.BaseFragment;
import com.android.zcomponent.http.api.model.MessageData;
import com.android.zcomponent.util.ClientInfo;
import com.android.zcomponent.util.CommonUtil;
import com.android.zcomponent.util.PageInditor;
import com.android.zcomponent.util.ShowMsg;
import com.android.zcomponent.util.StringUtil;
import com.android.zcomponent.views.PullToRefreshView;
import com.android.zcomponent.views.PullToRefreshView.OnFooterRefreshListener;
import com.android.zcomponent.views.PullToRefreshView.OnHeaderRefreshListener;
import com.server.api.model.ProductItem;
import com.server.api.model.Products;
import com.server.api.service.ProductService;

import java.util.List;

public class SearchByNameFragment extends BaseFragment implements
    OnHeaderRefreshListener, OnFooterRefreshListener {

  private static final String TAG = "SearchByNameFragment";

  private ImageButton mBtnSearch;
  private ImageButton imavewClear;
  private GridView mlvewSearchResult;
  private PullToRefreshView mPullToRefreshView;
  private ProgressBar mProgressBar;
  private EditText metxtInput;
  private TextView mtvewBack;
  private ProductAdapter mHistoryAdapter;
  private String mstrShopName = "";
  private String mstrType;
  private PageInditor<ProductItem> mPageInditor = new PageInditor<ProductItem>();
  private View rootView;

  public SearchByNameFragment() {}

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    rootView = inflater.inflate(R.layout.fragment_search_layout, container, false);
    return rootView;
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    init();
  }

  private void init() {
    getDataEmptyView().setTitleColor(getResources().getColor(R.color.gray));
    getDataEmptyView().setBackgroundResource(R.drawable.transparent);
    mlvewSearchResult =
        (GridView) rootView.findViewById(R.id.common_gridview_show);
    mPullToRefreshView =
        (PullToRefreshView) rootView
            .findViewById(R.id.common_pull_refresh_view_show);

    mProgressBar =
        (ProgressBar) rootView.findViewById(R.id.refresh_progress);
    mtvewBack =
        (TextView) rootView
            .findViewById(R.id.search_distory_dialog_rlayout_back);
    mBtnSearch =
        (ImageButton) rootView.findViewById(R.id.search_dialog_btn);
    metxtInput = (EditText) rootView.findViewById(R.id.et_search_keyword);
    imavewClear =
        (ImageButton) rootView
            .findViewById(R.id.search_bar_clear_input);
    imavewClear.setVisibility(View.GONE);
    mPullToRefreshView.setHeaderInvisible();
    mPullToRefreshView.setOnFooterRefreshListener(this);
    mstrType = getActivity().getIntent().getStringExtra("type");
    setClickListener();
    initHistoryView();
    openSoftInput();
  }

  private void setClickListener() {
    mtvewBack.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View arg0) {
        ClientInfo.closeSoftInput(metxtInput, getActivity());
        getActivity().finish();
      }
    });
    mBtnSearch.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        onClickSearch();
      }
    });
    imavewClear.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        clearCache();
      }
    });
    metxtInput.addTextChangedListener(new TextWatcher() {

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
          setSearchBtnIsClose(false);
        } else {
          clearCache();
        }
        mstrShopName = strMsg;
        if (!StringUtil.isEmptyString(strMsg)) {
          refreshData(true);
        } else {
          clearCache();
        }
      }
    });
    metxtInput.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        if (StringUtil.isEmptyString(metxtInput.getText().toString())) {
        }
      }
    });
  }

  public void clearCache() {
    mPageInditor.setPullRefresh(true);
    if (null != mPageInditor) {
      mPageInditor.clear();
    }
    getDataEmptyView().removeAllViews();
    if (!StringUtil.isEmptyString(metxtInput.getText().toString())) {
      metxtInput.setText("");
      setSearchBtnIsClose(true);
    }
  }

  private void refreshData(boolean isPullRefresh) {
    mPageInditor.setPullRefresh(isPullRefresh);
    ProductBusiness.querySearchProducts(getHttpDataLoader(),
        mstrType, mstrShopName, mPageInditor.getPageNum());
  }

  public void closeSoftInput() {
    ClientInfo.closeSoftInput(metxtInput, getActivity());
  }

  public void openSoftInput() {
    metxtInput.setFocusable(true);
    metxtInput.setFocusableInTouchMode(true);
    metxtInput.requestFocus();
    ClientInfo.openSoftInput(metxtInput, getActivity());
  }

  @Override
  public void onDataEmptyClickRefresh() {
    if (!CommonUtil.isLeastSingleClick()) {
      return;
    }
    onClickSearch();
  }

  private void onClickSearch() {
    mstrShopName = metxtInput.getText().toString();
    if (StringUtil.isEmptyString(mstrShopName)) {
      ShowMsg.showToast(getActivity(), "请输入查询内容");
    } else {
      mProgressBar.setVisibility(View.VISIBLE);
      mBtnSearch.setVisibility(View.INVISIBLE);
      refreshData(true);
    }
  }

  private void initHistoryView() {
    mlvewSearchResult.setOnItemClickListener(new OnItemClickListener() {

      @Override
      public void onItemClick(AdapterView<?> arg0, View arg1,
                              int position, long arg3) {
        ProductItem product = mPageInditor.get(position);

        if (ProductType.PAIPIN.getValue().equals(product.type)) {
          ProductBusiness.intentToPaipinProductDetailActivity(
              getActivity(), product,
              Integer.parseInt(product.id));
        } else {
          ProductBusiness.intentToProductDetailActivity(
              getActivity(), product,
              Integer.parseInt(product.id));
        }
      }
    });
  }

  private void setSearchBtnIsClose(boolean isClose) {
    if (isClose) {
      imavewClear.setVisibility(View.GONE);
    } else {
      imavewClear.setVisibility(View.VISIBLE);
    }
  }

  @Override
  public void onFooterRefresh(PullToRefreshView view) {
    refreshData(false);
  }

  @Override
  public void onHeaderRefresh(PullToRefreshView view) {
  }

  @Override
  public void onRecvMsg(MessageData msg) {
    if (msg.valiateReq(ProductService.SearchRequest.class)) {
      mProgressBar.setVisibility(View.GONE);
      mBtnSearch.setVisibility(View.INVISIBLE);
      if (null != mPageInditor) {
        mPageInditor.clear();
      }
      Products rsp = (Products) msg.getRspObject();
      if (ProductBusiness.validateQueryProducts(rsp)) {
        mPageInditor.add(rsp.Data.Results);

        if (null != mHistoryAdapter) {
          mHistoryAdapter.notifyDataSetChanged();
        } else {
          mHistoryAdapter =
              new ProductAdapter(getActivity(),
                  mPageInditor.getAll());
          mPageInditor
              .bindAdapter(mlvewSearchResult, mHistoryAdapter);
        }

        if (mPageInditor.size() == rsp.Data.Total) {
          mPullToRefreshView.setFooterRefreshComplete();
        } else {
          mPullToRefreshView.setFooterVisible();
        }
        getDataEmptyView().dismiss();
      } else {
        getDataEmptyView().showViewDataEmpty(false, false, msg,
            "查询不到相关信息");
      }
    }
  }

  @Override
  public boolean onKeyBack(int iKeyCode, KeyEvent event) {
    if (event.getAction() == KeyEvent.ACTION_DOWN) {
      ClientInfo.closeSoftInput(metxtInput, getActivity());
    }
    return super.onKeyBack(iKeyCode, event);
  }

  private class SearchHistoryAdapter extends CommonAdapter {

    public SearchHistoryAdapter(Context context, List<?> list) {
      super(context, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      convertView =
          layoutInflater.inflate(R.layout.item_lvew_search_history,
              null);
      TextView tvewVipName =
          (TextView) convertView.findViewById(R.id.category_name);

      ProductItem item = (ProductItem) mList.get(position);
      tvewVipName.setText(item.title);
      tvewVipName.setGravity(Gravity.LEFT);
      return convertView;
    }
  }
}
