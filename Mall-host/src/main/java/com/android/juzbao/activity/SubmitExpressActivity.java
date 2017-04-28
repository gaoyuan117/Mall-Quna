
package com.android.juzbao.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.juzbao.application.BaseApplication;
import com.android.juzbao.constant.ProviderResultActivity;
import com.android.juzbao.model.DistinguishBusiness;
import com.android.juzbao.model.ProviderFileBusiness;
import com.android.juzbao.model.ShippingBusiness;
import com.android.juzbao.util.CommonValidate;
import com.android.mall.resource.R;
import com.android.zcomponent.activity.EditActivity;
import com.android.zcomponent.http.api.model.MessageData;
import com.android.zcomponent.util.ShowMsg;
import com.android.zcomponent.views.WheelViewPopupWindow;
import com.android.zcomponent.views.WheelViewPopupWindow.OnPopSureClickListener;
import com.android.zcomponent.views.swipebacklayout.lib.app.SwipeBackActivity;
import com.server.api.model.CommonReturn;
import com.server.api.model.Shipping;
import com.server.api.service.DistinguishService;
import com.server.api.service.ShippingService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * Description: 提交物流凭证(发货)
 * </p>
 * 
 * @ClassName:SubmitExpressActivity
 * @author: wei
 * @date: 2015-11-10
 * 
 */
@EActivity(R.layout.activity_submit_express)
public class SubmitExpressActivity extends SwipeBackActivity
{

	@ViewById(R.id.checkbox_schedule_show)
	CheckBox mCheckboxSchedule;

	@ViewById(R.id.tvew_express_name_show)
	TextView mTvewExpressName;

	@ViewById(R.id.tvew_express_num_show)
	TextView mTvewExpressNum;

	@ViewById(R.id.imgvew_photo_show)
	ImageView mImgvewPhoto;

	@ViewById(R.id.progressbar_categoryz_show)
	ProgressBar mProgressBar;

	private List<Shipping.Data> mlistShipping;

	private String[] mShippingNames;

	private String mDistinguishId;

	private String mShippingCode;

	private String mDeliveryCode;

	private WheelViewPopupWindow mPopupWindow;

	protected ProviderFileBusiness mFileBusiness;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	@AfterViews
	void initUI()
	{
		getTitleBar().setTitleText("提交物流凭证");
		mDistinguishId = getIntent().getStringExtra("id");
		mFileBusiness = new ProviderFileBusiness(this, getHttpDataLoader());
		mFileBusiness.setOutParams(5, 2, 1280, 512);
	}

	@Override
	public void onRecvMsg(MessageData msg) throws Exception
	{
		mFileBusiness.onRecvMsg(msg);

		if (msg.valiateReq(DistinguishService.DistinguishShipmentRequest.class)){
			CommonReturn response = msg.getRspObject();
			if (CommonValidate.validateQueryState(this, msg, response,
					"提交物流凭证失败"))
			{
				ShowMsg.showToast(getApplicationContext(), "提交物流凭证成功");
				BaseApplication.getInstance().setActivityResult(
						ProviderResultActivity.CODE_DISTINGUISH_STATUS, null);
				finish();
			}
		}else if (msg.valiateReq(ShippingService.ShippingListRequest.class)){
			mProgressBar.setVisibility(View.GONE);
			Shipping response =  msg.getRspObject();
			if (CommonValidate.validateQueryState(this, msg, response))
			{
				mlistShipping =
						new ArrayList<Shipping.Data>(
								Arrays.asList(response.Data));
				mShippingNames = new String[mlistShipping.size()];
				for (int i = 0; i < mShippingNames.length; i++)
				{
					mShippingNames[i] = mlistShipping.get(i).title;
				}
				showShipping();
			}
			else
			{
				ShowMsg.showToast(this, msg, "获取物流公司信息失败");
			}
		}
	}

	private void showShipping()
	{
		if (null == mPopupWindow)
		{
			mPopupWindow = new WheelViewPopupWindow(this);
			mPopupWindow.dismissWheelViewSecond();
			mPopupWindow.dismissWheelViewThird();

			mPopupWindow.setOnPopSureClickListener(new OnPopSureClickListener()
			{

				@Override
				public void onPopSureClick(Object firstObj, Object secondObj,
						Object thirdObj)
				{
					int position = mPopupWindow.getFirstViewCurItem();
					if (mlistShipping.size() > position)
					{
						try
						{
							mShippingCode = mlistShipping.get(position).code;
							mTvewExpressName.setText(mlistShipping
									.get(position).title);
						}
						catch (Exception e)
						{
							e.printStackTrace();
						}
					}
				}
			});
		}

		mPopupWindow.setFirstViewAdapter(mShippingNames);
		mPopupWindow.showWindowBottom(mTvewExpressName);
	}

	@Click(R.id.btn_submit_click)
	void onClickBtnSubmit()
	{
		boolean isSend =
				DistinguishBusiness.queryDistinguishShippment(this,
						getHttpDataLoader(), mDistinguishId, mShippingCode,
						mDeliveryCode, mFileBusiness.getImageFileIds());
		if (isSend)
		{
			showWaitDialog(1, false, R.string.common_submit_data);
		}
	}

	@Click(R.id.llayout_express_photo_click)
	void onClickLlayoutExpressPhoto()
	{
		mFileBusiness.selectPicture();
	}

	@Click(R.id.rlayout_express_name_click)
	void onClickRlayoutExpressName()
	{
		if (null != mlistShipping && mlistShipping.size() > 0)
		{
			showShipping();
		}
		else
		{
			mProgressBar.setVisibility(View.VISIBLE);
			ShippingBusiness.queryShippingList(getHttpDataLoader());
		}
	}

	@Click(R.id.rlayout_product_num_click)
	void onClickRlayoutProductNum()
	{
		Bundle bundle = new Bundle();
		bundle.putString(EditActivity.CONTENT, mDeliveryCode);
		bundle.putString(EditActivity.TITLE, "编辑物流单号");
		bundle.putString(EditActivity.HINT, "请输入物流单号");
		bundle.putInt(EditActivity.MIN_LINE, 1);
		getIntentHandle().intentToActivity(bundle, EditActivity.class);
	}

	@Click(R.id.tvew_select_photo_click)
	void onClickTvewSelectPhoto()
	{

	}

	@Override
	public void onActivityResultCallBack(int resultCode, Intent intent)
	{
		if (resultCode == EditActivity.CODE_EDIT_COMPLETE)
		{
			if (!TextUtils.isEmpty(intent.getStringExtra("content")))
			{
				mDeliveryCode = intent.getStringExtra("content");
				mTvewExpressNum.setText(mDeliveryCode);
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);

		mFileBusiness.clear();
		Bitmap bitmap =
				mFileBusiness.onActivityResult(requestCode, resultCode, data);
		mImgvewPhoto.setImageBitmap(bitmap);
	}
}