
package com.android.juzbao.activity;

import java.util.ArrayList;
import java.util.Arrays;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.mall.resource.R;
import com.android.juzbao.activity.order.MyDistinguishOrderActivity_;
import com.android.zcomponent.http.api.host.Endpoint;
import com.server.api.model.ShippingAdminDetail;
import com.server.api.model.ShippingInfoItem;
import com.server.api.model.ShippingItem;
import com.server.api.model.ShippingSellerDetail;
import com.android.juzbao.adapter.ExpressStatusAdapter;
import com.android.juzbao.model.ShippingBusiness;
import com.android.juzbao.util.CommonValidate;
import com.android.zcomponent.http.api.model.MessageData;
import com.android.zcomponent.util.ImageLoaderUtil;
import com.android.zcomponent.util.ShowMsg;
import com.android.zcomponent.views.swipebacklayout.lib.app.SwipeBackActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.server.api.service.ShippingService;

/**
 * <p>
 * Description: 物流详情
 * </p>
 * 
 * @ClassName:ExpressDetailActivity
 * @author: wei
 * @date: 2015-11-10
 * 
 */
@EActivity(R.layout.activity_express_detail)
public class ExpressDetailActivity extends SwipeBackActivity
{

	@ViewById(R.id.imgvew_product_show)
	ImageView mImgvewProduct;

	@ViewById(R.id.imgvew_express_show)
	ImageView mImgvewExpress;

	@ViewById(R.id.lvew_express_status_show)
	LinearLayout mLvewExpressStatus;

	@ViewById(R.id.tvew_express_status_show)
	TextView mTvewExpressStatus;

	@ViewById(R.id.tvew_express_show)
	TextView mTvewExpress;

	@ViewById(R.id.tvew_no_shipping_show)
	TextView mTvewNoExpress;

	@ViewById(R.id.tvew_express_id_show)
	TextView mTvewExpressId;

	@ViewById(R.id.tvew_express_phone_show_click)
	TextView mTvewExpressPhone;

	private String mId;

	private String mImage;

	private ShippingItem mShippingInfo;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	@AfterViews
	void initUI()
	{
		getTitleBar().setTitleText("物流详情");

		mId = getIntent().getStringExtra("id");
		mImage = getIntent().getStringExtra("image");

		queryShippingDetail();
	}

	private void queryShippingDetail()
	{
		getDataEmptyView().showViewWaiting();
		if (getIntentHandle().isIntentFrom(DistinguishEnsureActivity_.class)
				|| getIntentHandle().isIntentFrom(
						MyDistinguishOrderActivity_.class))
		{
			ShippingBusiness.queryDistinguishShippingDetail(
					getHttpDataLoader(), mId);
		}
		else
		{
			ShippingBusiness.queryOrderShippingDetail(getHttpDataLoader(), mId);
		}
	}

	@Override
	public void onDataEmptyClickRefresh()
	{
		queryShippingDetail();
	}

	@Override
	public void onRecvMsg(MessageData msg) throws Exception
	{
		if (msg.valiateReq(ShippingService.DistinguishShippingDetailRequest.class)){
			ShippingSellerDetail response =
					(ShippingSellerDetail) msg.getRspObject();
			if (CommonValidate.validateQueryState(this, msg, response))
			{
				showShippingInfo(response.Data.seller_shipping);
				getDataEmptyView().dismiss();
			}
			else
			{
				getDataEmptyView().showViewDataEmpty(true, false, msg,
						"物流信息查询失败");
			}
		}else if (msg.valiateReq(ShippingService.OrderShippingDetailRequest.class)){
			ShippingAdminDetail response = msg.getRspObject();
			if (CommonValidate.validateQueryState(this, msg, response))
			{
				showShippingInfo(response.Data.admin_shipping);
				getDataEmptyView().dismiss();
			}
			else
			{
				getDataEmptyView().showViewDataEmpty(true, false, msg,
						"物流信息查询失败");
			}
		}
	}

	private void showShippingInfo(ShippingItem shippingInfo)
	{
		mShippingInfo = shippingInfo;
		mTvewExpress.setText(shippingInfo.shipping_title);
		mTvewExpressPhone.setText(shippingInfo.shipping_mobile);
		mTvewExpressId.setText(shippingInfo.code);

		if (null != shippingInfo.shipping)
		{
			mTvewExpressStatus.setText(shippingInfo.shipping.status);

			ExpressStatusAdapter adapter =
					new ExpressStatusAdapter(this,
							new ArrayList<ShippingInfoItem>(
									Arrays.asList(shippingInfo.shipping.data)));

			for (int i = 0; i < shippingInfo.shipping.data.length; i++)
			{
				View view = adapter.getView(i, null, null);
				mLvewExpressStatus.addView(view);
			}
			mTvewNoExpress.setVisibility(View.GONE);
		}
		else
		{
			mTvewNoExpress.setVisibility(View.VISIBLE);
		}

		DisplayImageOptions options =
				ImageLoaderUtil
						.getDisplayImageOptions(R.drawable.img_empty_logo_small);

		if (null != shippingInfo.images && shippingInfo.images.length > 0)
		{
			ImageLoader.getInstance().displayImage(
					Endpoint.HOST + shippingInfo.images[0], mImgvewExpress,
					options);
			mImgvewExpress.setVisibility(View.VISIBLE);
		}

		if (!TextUtils.isEmpty(mImage))
		{
			ImageLoader.getInstance().displayImage(Endpoint.HOST + mImage,
					mImgvewProduct, options);
		}
	}

	@Click(R.id.tvew_express_phone_show_click)
	void onClickTvewExpressPhone()
	{
		ShowMsg.showCallDialog(this, mShippingInfo.shipping_mobile);
	}

}