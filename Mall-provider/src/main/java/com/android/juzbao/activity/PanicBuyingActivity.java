
package com.android.juzbao.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.juzbao.constant.ProviderResultActivity;
import com.android.juzbao.enumerate.SpecialType;
import com.android.juzbao.model.ProviderProductBusiness;
import com.android.juzbao.provider.R;
import com.android.zcomponent.adapter.CommonAdapter;
import com.android.zcomponent.common.uiframe.FramewrokApplication;
import com.android.zcomponent.http.api.host.Endpoint;
import com.android.zcomponent.http.api.model.MessageData;
import com.android.zcomponent.http.constant.ErrorCode;
import com.android.zcomponent.util.ClientInfo;
import com.android.zcomponent.util.CommonUtil;
import com.android.zcomponent.util.ListUtil;
import com.android.zcomponent.util.ShowMsg;
import com.android.zcomponent.util.StringUtil;
import com.android.zcomponent.util.TimeUtil;
import com.android.zcomponent.util.ValidateUtil;
import com.android.zcomponent.views.WheelViewPopupWindow;
import com.android.zcomponent.views.WheelViewPopupWindow.OnPopSureClickListener;
import com.android.zcomponent.views.swipebacklayout.lib.app.SwipeBackActivity;
import com.server.api.model.CommonReturn;
import com.server.api.model.GiftCategory;
import com.server.api.model.GiftCategory.GiftCategoryItem;
import com.server.api.model.PaincTimes;
import com.server.api.service.ProductService;
import com.server.api.service.ProductService.AddProductGiftRequest;
import com.server.api.service.ProductService.AddProductPanicRequest;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Description: 发布到抢购会
 * </p>
 *
 * @ClassName:PanicBuyingActivity
 * @author: wei
 * @date: 2015-11-11
 */
@EActivity(resName = "activity_panic_buying")
public class PanicBuyingActivity extends SwipeBackActivity {
  @ViewById(resName = "edittxt_panic_buying_money_show")
  EditText mEdittxtPanicBuyingMoney;

  @ViewById(resName = "edittxt_panic_buying_discount_one_show")
  TextView mEdittxtPanicBuyingDiscountOne;

  @ViewById(resName = "edittxt_panic_buying_discount_two_show")
  EditText mEdittxtPanicBuyingDiscountTwo;

  @ViewById(resName = "llayout_panic_buying_choose_show_click")
  LinearLayout mllayoutProduct;

  @ViewById(resName = "tvew_panic_time_show")
  TextView mTvewPaincTime;

  @ViewById(resName = "progressbar_categoryz_show")
  ProgressBar mProgressBar;

  @ViewById(resName = "llayout_panic_buying")
  LinearLayout mLayoutPainicBuying;

  @ViewById(resName = "gvew_gift_category_show")
  GridView mGvewCategory;

  private long mProductId;

  private String[] mArrayDiscount = {"不打折", "9折", "8折", "7折", "6折", "5折",
      "4折", "3折", "2折", "1折"};

  private List<GiftCategoryItem> mlistCategory;

  private List<Long> mlistGiftCategoryId = new ArrayList<Long>();

  private List<PaincTimes.Data> mlistPaincTimes;

  private String[] mArrayPaincTimes;

  private WheelViewPopupWindow mPopupWindow;

  private boolean isSelectDiscount;

  private int mDiscoundId;

  private long mPaincTimeId;

  private SpecialType mSpecialType;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @AfterViews
  void initUI() {
    mEdittxtPanicBuyingMoney
        .addTextChangedListener(new StringUtil.DecimalTextWatcher(
            mEdittxtPanicBuyingMoney, 2));
    Intent intent = getIntent();
    mProductId = intent.getLongExtra("id", 0);
    String type = intent.getStringExtra("type");

    if (SpecialType.GIFT.getValue().equals(type)) {
      mSpecialType = SpecialType.GIFT;
      mLayoutPainicBuying.setVisibility(View.GONE);
      mGvewCategory.setVisibility(View.VISIBLE);
      getTitleBar().setTitleText("发布到选礼物");
      ProviderProductBusiness.queryGiftCategory(getHttpDataLoader());
    } else if (SpecialType.PANIC.getValue().equals(type)) {
      mSpecialType = SpecialType.PANIC;
      mLayoutPainicBuying.setVisibility(View.VISIBLE);
      mGvewCategory.setVisibility(View.GONE);
      getTitleBar().setTitleText("发布到抢购会");
    }

    if (mProductId > 0) {
      mllayoutProduct.setVisibility(View.GONE);
    } else {
      mllayoutProduct.setVisibility(View.VISIBLE);
    }

  }

  private void showCategory() {
    if (null == mPopupWindow) {
      mPopupWindow = new WheelViewPopupWindow(this);
      mPopupWindow.dismissWheelViewSecond();
      mPopupWindow.dismissWheelViewThird();

      mPopupWindow.setOnPopSureClickListener(new OnPopSureClickListener() {

        @Override
        public void onPopSureClick(Object firstObj, Object secondObj,
                                   Object thirdObj) {
          int position = mPopupWindow.getFirstViewCurItem();

          if (isSelectDiscount) {
            if (mArrayDiscount.length > position) {
              mDiscoundId = position;
              mEdittxtPanicBuyingDiscountOne
                  .setText(mArrayDiscount[position].replace(
                      "折", ""));
            }
          } else {
            if (mArrayPaincTimes.length > position) {
              mPaincTimeId =
                  Long.parseLong(mlistPaincTimes
                      .get(position).id);
              mTvewPaincTime.setText(mArrayPaincTimes[position]);
            }
          }
        }
      });
    }

    if (isSelectDiscount) {
      mPopupWindow.setFirstViewAdapter(mArrayDiscount);
    } else {
      mPopupWindow.setFirstViewAdapter(mArrayPaincTimes);
    }
    mPopupWindow.showWindowBottom(mEdittxtPanicBuyingMoney);
  }

  @Override
  public void onRecvMsg(MessageData msg) throws Exception {
    if (msg.valiateReq(ProductService.GiftCagetoryRequest.class)) {
      mProgressBar.setVisibility(View.GONE);
      GiftCategory response = (GiftCategory) msg.getRspObject();
      if (null != response) {
        if (response.code == ErrorCode.INT_QUERY_DATA_SUCCESS) {
          if (!ValidateUtil.isArrayEmpty(response.Data)) {
            mlistCategory = ListUtil.arrayToList(response.Data);
            mGvewCategory.setAdapter(new GiftCategoryAdapter(this,
                mlistCategory));
            CommonUtil.setGridViewHeightBasedOnChildren(
                mGvewCategory, 5);
          } else {
            ShowMsg.showToast(getApplicationContext(), "未查询到选礼物分类");
          }
        } else {
          ShowMsg.showToast(getApplicationContext(), response.message);
        }
      } else {
        ShowMsg.showToast(getApplicationContext(), msg, "查询选礼物分类失败");
      }
    } else if (msg.valiateReq(ProductService.AddProductPanicRequest.class)) {
      CommonReturn response = (CommonReturn) msg.getRspObject();
      if (null != response) {
        if (response.code == ErrorCode.INT_QUERY_DATA_SUCCESS) {
          ShowMsg.showToast(getApplicationContext(), "加入抢购会成功");
          FramewrokApplication.getInstance().setActivityResult(
              ProviderResultActivity.CODE_EDIT_SPECIAL_PANIC,
              null);
          FramewrokApplication.getInstance().closeAcitity(
              ToSpecialActivity_.class);
          finish();
        } else {
          ShowMsg.showToast(getApplicationContext(), response.message);
        }
      } else {
        ShowMsg.showToast(getApplicationContext(), msg, "加入抢购会失败");
      }
    } else if (msg.valiateReq(ProductService.AddProductGiftRequest.class)) {
      CommonReturn response = (CommonReturn) msg.getRspObject();
      if (null != response) {
        if (response.code == ErrorCode.INT_QUERY_DATA_SUCCESS) {
          ShowMsg.showToast(getApplicationContext(), "加入选礼物成功");

          FramewrokApplication
              .getInstance()
              .setActivityResult(
                  ProviderResultActivity.CODE_EDIT_SPECIAL_GIFT,
                  null);
          FramewrokApplication.getInstance().closeAcitity(
              ToSpecialActivity_.class);
          finish();
        } else {
          ShowMsg.showToast(getApplicationContext(), response.message);
        }
      } else {
        ShowMsg.showToast(getApplicationContext(), msg, "加入选礼物失败");
      }
    } else if (msg.valiateReq(ProductService.PanicTimesRequest.class)) {
      PaincTimes response = (PaincTimes) msg.getRspObject();
      mProgressBar.setVisibility(View.GONE);
      if (null != response) {
        if (response.code == ErrorCode.INT_QUERY_DATA_SUCCESS) {
          mlistPaincTimes = ListUtil.arrayToList(response.Data);
          ProviderProductBusiness.formatPaincingTime(mlistPaincTimes);
          mArrayPaincTimes = new String[mlistPaincTimes.size()];

          for (int i = 0; i < mlistPaincTimes.size(); i++) {
            String startTime =
                TimeUtil.transformLongTimeFormat(
                    Long.parseLong(mlistPaincTimes.get(i).start_time) * 1000,
                    TimeUtil.STR_FORMAT_HOUR_MINUTE);
            String endTime =
                TimeUtil.transformLongTimeFormat(
                    Long.parseLong(mlistPaincTimes.get(i).end_time) * 1000,
                    TimeUtil.STR_FORMAT_HOUR_MINUTE);
            mArrayPaincTimes[i] = startTime + " - " + endTime;
          }
          isSelectDiscount = false;
          showCategory();
        } else {

        }
      } else {
        ShowMsg.showToast(getApplicationContext(), msg, "抢购时间查询失败");
      }
    }
  }

  @Click(resName = "edittxt_panic_buying_discount_one_show")
  void onClickTvewDiscount() {
    ClientInfo.closeSoftInput(mEdittxtPanicBuyingMoney, this);
    isSelectDiscount = true;
    showCategory();
  }

  @Click(resName = "btn_panic_buying_join_click")
  void onClickBtnPanicBuyingJoin() {
    if (SpecialType.GIFT == mSpecialType) {
      addSelectGift();
    } else if (SpecialType.PANIC == mSpecialType) {
      addPanicBuying();
    }
  }

  private void addPanicBuying() {
    AddProductPanicRequest request = new AddProductPanicRequest();
    if (!TextUtils.isEmpty(mEdittxtPanicBuyingMoney.getText().toString())) {
      request.ConditionPrice =
          new BigDecimal(mEdittxtPanicBuyingMoney.getText()
              .toString());
    } else {
      request.ConditionPrice = new BigDecimal(0);
    }
    request.Discount = 10 - mDiscoundId;
    request.ProductId = mProductId;
    request.SpecialPanicId = mPaincTimeId;

    if (request.ConditionPrice.doubleValue() > 0) {
      if (TextUtils.isEmpty(mEdittxtPanicBuyingDiscountOne.getText()
          .toString())) {
        ShowMsg.showToast(getApplicationContext(), "请选择折扣");
        return;
      }
    }

    boolean isSend = ProviderProductBusiness.addProductPainc(this,
        getHttpDataLoader(), request);

    if (isSend) {
      showWaitDialog(1, false, R.string.common_submit_data);
    }
  }

  private void addSelectGift() {
    AddProductGiftRequest request = new AddProductGiftRequest();
    request.ProductId = mProductId;
    request.SpecialGiftId =
        mlistGiftCategoryId
            .toArray(new Long[mlistGiftCategoryId.size()]);
    boolean isSend =
        ProviderProductBusiness.addProductGift(this,
            getHttpDataLoader(), request);
    if (isSend) {
      showWaitDialog(1, false, R.string.common_submit_data);
    }
  }

  @Click(resName = "llayout_panic_buying_choose_show_click")
  void onClickLlayoutPanicBuyingChoose() {

  }

  @Click(resName = "llayout_panic_buying_start_time_click")
  void onClickLlayoutPanicBuyingStartTime() {
    ClientInfo.closeSoftInput(mEdittxtPanicBuyingMoney, this);
    if (ValidateUtil.isListEmpty(mlistPaincTimes)) {
      mProgressBar.setVisibility(View.VISIBLE);
      ProviderProductBusiness.queryPaincAfterTime(getHttpDataLoader());
    } else {
      isSelectDiscount = false;
      showCategory();
    }
  }

  @Click(resName = "llayout_panic_buying_end_time_click")
  void onClickLlayoutPanicBuyingEndTime() {

  }

  public class GiftCategoryAdapter extends CommonAdapter {

    public GiftCategoryAdapter(Context context, List<?> list) {
      super(context, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      if (null == convertView) {
        convertView =
            layoutInflater.inflate(
                R.layout.adapter_select_gift_images, null);
      }

      ImageView imgvewPhoto =
          findViewById(convertView, R.id.imgvew_category);

      RelativeLayout imgvewPhotoMask =
          findViewById(convertView, R.id.llayout_category_mask);

      final GiftCategoryItem category =
          (GiftCategoryItem) mList.get(position);
      mImageLoader.displayImage(Endpoint.HOST + category.image,
          imgvewPhoto, options);

      if (mlistGiftCategoryId.contains(Long.parseLong(category.id))) {
        imgvewPhotoMask.setVisibility(View.VISIBLE);
      } else {
        imgvewPhotoMask.setVisibility(View.GONE);
      }

      convertView.setOnClickListener(new OnCategoryItemClickListener(
          imgvewPhotoMask, true, position));

      imgvewPhotoMask.setOnClickListener(new OnCategoryItemClickListener(
          imgvewPhotoMask, false, position));

      return convertView;
    }

    public class OnCategoryItemClickListener implements OnClickListener {

      private boolean isAdd;

      private int position;

      private RelativeLayout imgvewPhotoMask;

      public OnCategoryItemClickListener(RelativeLayout imgvewPhotoMask,
                                         boolean isAdd, int position) {
        this.isAdd = isAdd;
        this.position = position;
        this.imgvewPhotoMask = imgvewPhotoMask;
      }

      @Override
      public void onClick(View v) {
        if (isAdd) {
          imgvewPhotoMask.setVisibility(View.VISIBLE);
          mlistGiftCategoryId.add(Long.parseLong(mlistCategory
              .get(position).id));
        } else {
          imgvewPhotoMask.setVisibility(View.GONE);
          mlistGiftCategoryId.remove(Long.parseLong(mlistCategory
              .get(position).id));
        }
        notifyDataSetChanged();
      }
    }
  }
}