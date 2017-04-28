
package com.android.juzbao.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.FrameLayout;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.juzbao.adapter.PanicBuyingAdapter;
import com.android.juzbao.adapter.PanicTimeAdapter;
import com.android.juzbao.enumerate.PaincBuyType;
import com.android.juzbao.enumerate.PaincTimeType;
import com.android.juzbao.model.ProductBusiness;
import com.android.juzbao.util.CommonValidate;
import com.android.mall.resource.R;
import com.android.zcomponent.common.uiframe.fragment.BaseFragment;
import com.android.zcomponent.http.api.host.Endpoint;
import com.android.zcomponent.http.api.model.MessageData;
import com.android.zcomponent.http.constant.ErrorCode;
import com.android.zcomponent.util.CommonUtil;
import com.android.zcomponent.util.ListUtil;
import com.android.zcomponent.util.MyLayoutAdapter;
import com.android.zcomponent.util.PageInditor;
import com.android.zcomponent.util.ValidateUtil;
import com.android.zcomponent.views.TimeTextView;
import com.android.zcomponent.views.pulltorefresh.PullToRefreshBase;
import com.android.zcomponent.views.pulltorefresh.PullToRefreshBase.OnFooterRefreshListener;
import com.android.zcomponent.views.pulltorefresh.PullToRefreshBase.OnHeaderRefreshListener;
import com.android.zcomponent.views.pulltorefresh.PullToRefreshScrollView;
import com.server.api.model.AdProduct;
import com.server.api.model.PaincTimes;
import com.server.api.model.ProductItem;
import com.server.api.model.Products;
import com.server.api.service.ProductService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Description: 抢购会
 * </p>
 *
 * @ClassName:PaincBuyingFragment
 * @author: wei
 * @date: 2015-11-10
 */
@EFragment(R.layout.fragment_painc_buying)
public class PaincBuyingFragment extends BaseFragment implements
    OnHeaderRefreshListener<ScrollView>, OnFooterRefreshListener<ScrollView> {

  private PaincBuyType mPaincBuyType;

  @ViewById(R.id.llayout_time_show)
  Gallery mGalleryTime;

  @ViewById(R.id.llayout_time_end_show)
  RelativeLayout mllayoutTimeEnd;

  @ViewById(R.id.common_listview_show)
  ListView mListView;

  @ViewById(R.id.common_pull_refresh_view_show)
  PullToRefreshScrollView mPullToRefreshView;

  @ViewById(R.id.tvew_time_show)
  TimeTextView mTvewTime;

  @ViewById(R.id.tvew_end_time_title_show)
  TextView mTvewTimeTitle;

  @ViewById(R.id.llayout_time_bg_show)
  LinearLayout mLlayoutTimeBg;

  @ViewById(R.id.imgvew_time_arrow_show)
  ImageView mImgvewTimeArrow;

  @ViewById(R.id.flayout_slider_image)
  FrameLayout mflayoutImage;

  private PageInditor<ProductItem> mPageInditor = new PageInditor<ProductItem>();
  private List<PaincTimes.Data> mlistPaincTimes = new ArrayList<PaincTimes.Data>();

  private PanicBuyingAdapter mPanicBuyingAdapter;
  private PanicTimeAdapter mPanicTimeAdapter;

  private boolean isPaincStarted = true;

  private long panicId;

  @AfterViews
  void initUI() {
    getDataEmptyView().setBackgroundResource(R.drawable.transparent);

    mPullToRefreshView.setOnHeaderRefreshListener(this);
    mPullToRefreshView.setOnFooterRefreshListener(this);
    if (mPaincBuyType == PaincBuyType.BUYING) {
      ProductBusiness.queryPaincTime(getHttpDataLoader(), PaincTimeType.CURRDAY.getValue());
    } else if (mPaincBuyType == PaincBuyType.COUPON) {
      dismissPaincTimeView();
    } else if (mPaincBuyType == PaincBuyType.TIME) {
      mflayoutImage.setVisibility(View.GONE);
      dismissPaincTimeView();
    }

    mGalleryTime.setCallbackDuringFling(false);

    mGalleryTime.setOnItemSelectedListener(new OnItemSelectedListener() {
      @Override public void onItemSelected(AdapterView<?> parent, View view, int position, long
          id) {
        setEndTime(position);
        mPanicTimeAdapter.setSelectPosition(position);
        refreshData(true);
      }

      @Override public void onNothingSelected(AdapterView<?> parent) {}
    });
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    if (mPaincBuyType == PaincBuyType.BUYING) {
      ProductBusiness.queryPaincTime(getHttpDataLoader(), PaincTimeType.CURRDAY.getValue());
    } else {
      refreshData(true);
    }

    if (mPaincBuyType == PaincBuyType.BUYING) {
      ProductBusiness
          .queryAdProducts(getHttpDataLoader(), "panic_banner");
    } else if (mPaincBuyType == PaincBuyType.COUPON) {
      ProductBusiness.queryAdProducts(getHttpDataLoader(), "panic_banner");
    }

    getDataEmptyView().showViewWaiting();
  }

  private void dismissPaincTimeView() {
    mllayoutTimeEnd.setVisibility(View.GONE);
    mLlayoutTimeBg.setVisibility(View.GONE);
    mImgvewTimeArrow.setVisibility(View.GONE);
    mGalleryTime.getLayoutParams().height = 1;
  }

  private void setEndTime(int position) {
    long serverTime = Endpoint.serverDate().getTime();
    long startTime = Long.parseLong(mlistPaincTimes.get(position).start_time) * 1000;
    long endTime = Long.parseLong(mlistPaincTimes.get(position).end_time) * 1000;

    int seconds = 0;

    if (serverTime >= startTime && serverTime < endTime) {
      // 正在进行
      seconds = ProductBusiness.getDiffTime(mlistPaincTimes.get(position).end_time);
      mTvewTimeTitle.setText("距离本场结束：");
      isPaincStarted = true;
    } else if (serverTime < startTime) {
      // 即将开始
      seconds = ProductBusiness.getDiffTime(mlistPaincTimes.get(position).start_time);
      mTvewTimeTitle.setText("距离本场开始：");
      isPaincStarted = false;
    } else if (serverTime >= endTime) {
      seconds = ProductBusiness.getDiffTime(mlistPaincTimes.get(position).start_time);
      mTvewTimeTitle.setText("本场已结束：");
      isPaincStarted = false;
    } else {
      // 已开始
      isPaincStarted = false;
    }
    panicId = Long.parseLong(mlistPaincTimes.get(position).id);

    mTvewTime.setTextColor(getResources().getColor(R.color.white), getResources().getColor(R
        .color.black));
    mTvewTime.setTimes(seconds);

    if (!mTvewTime.isRun()) {
      mTvewTime.run();
    }
  }

  private void refreshData(boolean isPullRefresh) {
    mPageInditor.setPullRefresh(isPullRefresh);

    if (mPaincBuyType == PaincBuyType.BUYING) {
      ProductBusiness.queryPanicProducts(getHttpDataLoader(), 0, 0, panicId, 0, mPageInditor
          .getPageNum());
    } else if (mPaincBuyType == PaincBuyType.COUPON) {
      ProductBusiness.queryPanicProducts(getHttpDataLoader(), 0, 1, 0, 0, mPageInditor.getPageNum
          ());
    } else if (mPaincBuyType == PaincBuyType.TIME) {
      ProductBusiness.queryPanicProducts(getHttpDataLoader(), 0, 0, 0, Endpoint.serverDate()
          .getTime() / 1000, mPageInditor.getPageNum());
    }
  }

  private void showViewDataEmpty(MessageData msg, String message) {
    if (mPaincBuyType == PaincBuyType.BUYING) {
      getDataEmptyView().getView().getLayoutParams().height = (int) (280 * MyLayoutAdapter
          .getInstance().getDensityRatio());
    }
    mPullToRefreshView.onRefreshComplete();
    getDataEmptyView().showViewDataEmpty(false, false, msg, message);
  }

  @Override
  public void onRecvMsg(MessageData msg) throws Exception {
    if (msg.valiateReq(ProductService.PanicTimesRequest.class)) {
      PaincTimes response = msg.getRspObject();
      if (null != response) {
        if (response.code == ErrorCode.INT_QUERY_DATA_SUCCESS && null != response.Data) {
          if (mPaincBuyType == PaincBuyType.BUYING) {
            mlistPaincTimes = ListUtil.arrayToList(response.Data);

            ProductBusiness.formatPaincingTime(mlistPaincTimes);

            mPanicTimeAdapter = new PanicTimeAdapter(getActivity(), mlistPaincTimes);
            mGalleryTime.setAdapter(mPanicTimeAdapter);

            int position = ProductBusiness.getCurrentPaincingTime(mlistPaincTimes);

            setEndTime(position);

            mGalleryTime.setSelection(position);
            mPanicTimeAdapter.setSelectPosition(position);

            ProductBusiness.queryPanicProducts(getHttpDataLoader(), 0, 0, panicId, 0,
                mPageInditor.getPageNum());

          } else if (mPaincBuyType == PaincBuyType.TIME) {

          }

        } else {
          showViewDataEmpty(msg, response.message);
        }
      } else {
        showViewDataEmpty(msg, getString(R.string.common_data_empty));
      }
    } else if (msg.valiateReq(ProductService.PanicProductsRequest.class)) {
      mPullToRefreshView.onRefreshComplete();
      mPageInditor.clear();
      Products responseProduct = msg.getRspObject();
      if (ProductBusiness.validateQueryProducts(responseProduct)) {
        mPageInditor.add(responseProduct.Data.Results);
        if (null == mPanicBuyingAdapter) {
          mPanicBuyingAdapter = new PanicBuyingAdapter(getActivity(), mPageInditor.getAll());
          mPageInditor.bindAdapter(mListView, mPanicBuyingAdapter);
        } else {
          mPanicBuyingAdapter.notifyDataSetChanged();
        }
        mPanicBuyingAdapter.setStartPanic(isPaincStarted);
        if (mPageInditor.getAll().size() == responseProduct.Data.Total) {
          mPullToRefreshView.setFooterRefreshComplete();
        } else {
          mPullToRefreshView.setFooterRefresh();
        }

        CommonUtil.setListViewHeightBasedOnChildren(mListView);
        getDataEmptyView().dismiss();
      } else {
        if (!ValidateUtil.isListEmpty(mPageInditor.getAll())) {
          getDataEmptyView().setVisibility(View.GONE);
        } else {
          String message =
              ProductBusiness.validateQueryState(responseProduct,
                  getString(R.string.common_data_empty));
          if (mPaincBuyType == PaincBuyType.BUYING) {
            getDataEmptyView().getView().getLayoutParams().height =
                (int) (280 * MyLayoutAdapter.getInstance()
                    .getDensityRatio());
          }
          getDataEmptyView().showViewDataEmpty(false, false, msg,
              message);
        }
      }
    } else if (msg.valiateReq(ProductService.AdvertRequest.class)) {
      AdProduct response = msg.getRspObject();
      if (CommonValidate.validateQueryState(getActivity(), msg, response)) {
        ProductBusiness.bindSliderLayout(this,
            R.id.flayout_slider_image, response.Data);
      } else {

      }
    }
  }

  public void setPaincBuyType(PaincBuyType paincBuyType) {
    mPaincBuyType = paincBuyType;
  }

  @Override
  public void onFooterRefresh(PullToRefreshBase<ScrollView> view) {
    refreshData(false);
  }

  @Override
  public void onHeaderRefresh(PullToRefreshBase<ScrollView> view) {
    refreshData(true);
  }

}