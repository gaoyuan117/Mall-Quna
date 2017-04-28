
package com.android.juzbao.fragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.android.zcomponent.http.api.host.Endpoint;
import com.server.api.model.ProductItem;
import com.server.api.model.Products;
import com.android.mall.resource.R;
import com.android.juzbao.adapter.AuctionBeginAdapter;
import com.android.juzbao.model.ProductBusiness;
import com.android.juzbao.enumerate.AuctionType;
import com.android.zcomponent.common.uiframe.fragment.BaseFragment;
import com.android.zcomponent.http.api.model.MessageData;
import com.android.zcomponent.util.PageInditor;
import com.android.zcomponent.util.ValidateUtil;
import com.android.zcomponent.views.PullToRefreshView;
import com.android.zcomponent.views.PullToRefreshView.OnFooterRefreshListener;
import com.android.zcomponent.views.PullToRefreshView.OnHeaderRefreshListener;
import com.server.api.service.ProductService;

/**
 * <p>
 * Description: 拍真宝 - 即将开始、马上结束、0元起
 * </p>
 * 
 * @ClassName:AuctionBeginFragment
 * @author: wei
 * @date: 2015-11-10
 * 
 */
@EFragment(R.layout.fragment_auction_begin)
public class AuctionBeginFragment extends BaseFragment implements
		OnHeaderRefreshListener, OnFooterRefreshListener
{

	@ViewById(R.id.common_listview_show)
	ListView mListView;

	@ViewById(R.id.common_pull_refresh_view_show)
	PullToRefreshView mPullToRefreshView;

	private AuctionBeginAdapter mAdapter;

	private PageInditor<ProductItem> mPageInditor =
			new PageInditor<ProductItem>();

	private AuctionType mAuctionType;

	@AfterViews
	void initUI()
	{
		mPullToRefreshView.setOnHeaderRefreshListener(this);
		mPullToRefreshView.setOnFooterRefreshListener(this);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);

		getDataEmptyView().setBackgroundResource(R.drawable.transparent);
		getDataEmptyView().showViewWaiting();
		refreshData(true);
	}

	private void refreshData(boolean isPullRefresh)
	{
		mPageInditor.setPullRefresh(true);

		if (null != mAuctionType)
		{
			if (mAuctionType == AuctionType.START)
			{
				ProductBusiness.queryAuctionProducts(getHttpDataLoader(), 0, 0,
						(int) Endpoint.serverDate().getTime() / 1000, 0, 0,
						mPageInditor.getPageNum());
			}
			else if (mAuctionType == AuctionType.END)
			{
				ProductBusiness.queryAuctionProducts(getHttpDataLoader(), 0, 0,
						0, (int) Endpoint.serverDate().getTime() / 1000, 0,
						mPageInditor.getPageNum());
			}
			else if (mAuctionType == AuctionType.ZERO)
			{
				ProductBusiness.queryAuctionProducts(getHttpDataLoader(), 0, 0,
						0, 0, 0, mPageInditor.getPageNum());
			}
		}
	}

	public void setAuctionType(AuctionType auctionType)
	{
		this.mAuctionType = auctionType;
	}

	@Override
	public void onRecvMsg(MessageData msg) throws Exception
	{
		if (msg.valiateReq(ProductService.PaipinProductsRequest.class)){
			mPullToRefreshView.onFooterRefreshComplete();
			mPullToRefreshView.onHeaderRefreshComplete();
			mPageInditor.clear();

			Products responseProduct = (Products) msg.getRspObject();
			if (ProductBusiness.validateQueryProducts(responseProduct))
			{
				mPageInditor.add(responseProduct.Data.Results);
				if (null == mAdapter)
				{
					mAdapter =
							new AuctionBeginAdapter(getActivity(),
									mPageInditor.getAll());
					mPageInditor.bindAdapter(mListView, mAdapter);
				}
				else
				{
					mAdapter.notifyDataSetChanged();
				}

				if (mPageInditor.getAll().size() == responseProduct.Data.Total)
				{
					mPullToRefreshView.setFooterRefreshComplete();
				}
				else
				{
					mPullToRefreshView.setFooterVisible();
				}
				getDataEmptyView().removeAllViews();
			}
			else
			{
				if (!ValidateUtil.isListEmpty(mPageInditor.getAll()))
				{
					getDataEmptyView().setVisibility(View.GONE);
				}
				else
				{
					String message =
							ProductBusiness.validateQueryState(responseProduct,
									getString(R.string.common_data_empty));
					getDataEmptyView().showViewDataEmpty(false, false, msg,
							message);
				}
			}
		}
	}

	@Override
	public void onFooterRefresh(PullToRefreshView view)
	{
		refreshData(false);
	}

	@Override
	public void onHeaderRefresh(PullToRefreshView view)
	{
		refreshData(true);
	}

}