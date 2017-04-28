
package com.android.juzbao.activity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.juzbao.enumerate.CategoryType;
import com.android.juzbao.provider.R;
import com.server.api.model.AddProduct;
import com.server.api.model.CommonReturn;
import com.server.api.model.ProductItem;
import com.server.api.service.ProductService;
import com.server.api.service.ProductService.ProductAddAuctionRequest;
import com.server.api.service.ProductService.ProductEditAuctionRequest;
import com.android.juzbao.model.ProviderProductBusiness;
import com.android.juzbao.constant.ProviderResultActivity;
import com.android.juzbao.enumerate.ProductType;
import com.android.juzbao.util.CommonValidate;
import com.android.zcomponent.common.uiframe.FramewrokApplication;
import com.android.zcomponent.http.api.model.MessageData;
import com.android.zcomponent.util.ShowMsg;
import com.android.zcomponent.util.StringUtil;
import com.android.zcomponent.util.TimeUtil;
import com.android.zcomponent.views.TimePopupWindow;
import com.android.zcomponent.views.WheelViewPopupWindow;
import com.android.zcomponent.views.WheelViewPopupWindow.OnPopSureClickListener;

/**
 * <p>
 * Description: 发布拍品
 * </p>
 *
 * @ClassName:ReleaseAuctionGoodsActivity
 * @author: wei
 * @date: 2015-11-11
 */
@EActivity(resName = "activity_release_auction_goods")
public class ReleaseAuctionGoodsActivity extends AddProductActivity {

    @ViewById(resName = "edittxt_auction_goods_startprice_show")
    EditText mEdittxtAuctionGoodsStartprice;

    @ViewById(resName = "edittxt_auction_goods_transactionprice_show")
    EditText mEdittxtAuctionGoodsTransactionprice;

    @ViewById(resName = "edittxt_auction_goods_stock_show")
    EditText mEdittxtAuctionGoodsStock;

    @ViewById(resName = "edittxt_auction_goods_indentify_show")
    EditText mEdittxtAuctionGoodsIndentify;

    @ViewById(resName = "tvew_auction_goods_category_show")
    TextView mTvewAuctionGoodsCategory;

    @ViewById(resName = "tvew_auction_goods_description_show")
    TextView mTvewAuctionGoodsDescription;

    @ViewById(resName = "tvew_auction_goods_start_date_show")
    TextView mTvewStartDate;

    @ViewById(resName = "tvew_auction_goods_start_time_show")
    TextView mTvewStartTime;

    @ViewById(resName = "tvew_auction_goods_end_date_show")
    TextView mTvewEndDate;

    @ViewById(resName = "tvew_auction_goods_end_time_show")
    TextView mTvewEndTime;

    @ViewById(resName = "tvew_auction_now_release_click")
    TextView mTvewSubmit;

    @ViewById(resName = "progressbar_categoryz_show")
    ProgressBar mProgressBar;

    private WheelViewPopupWindow mPopupWindow;

    private TimePopupWindow mTimePopupWindow;

    private ProductAddAuctionRequest mAddRequest;

    private ProductEditAuctionRequest mEditRequest;

    private String[] mArrayHours = {"00:00", "01:00", "02:00", "03:00",
            "04:00", "05:00", "06:00", "07:00", "08:00", "09:00", "10:00",
            "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00",
            "18:00", "19:00", "20:00", "21:00", "22:00", "23:00"};

    private int miStartTimeId = -1;

    private int miEndTimeId = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    protected void initUI() {
        super.initUI();
        if (mProductId > 0) {
            mTvewWarehouse.setVisibility(View.GONE);
            getTitleBar().setTitleText("编辑拍品");
            mTvewSubmit.setText("保存");
        } else {
            getTitleBar().setTitleText("发布拍品");
        }
    }

    @Override
    public void onRecvMsg(MessageData msg) throws Exception {
        super.onRecvMsg(msg);

        if (msg.valiateReq(ProductService.ProductAddAuctionRequest.class)) {
            AddProduct response = (AddProduct) msg.getRspObject();

            if (CommonValidate.validateQueryState(this, msg, response, "发布失败")) {
                ShowMsg.showToast(this, "发布成功");
                finish();
            }
        } else if (msg.valiateReq(ProductService.ProductEditAuctionRequest.class)) {
            final CommonReturn response = (CommonReturn) msg.getRspObject();
            if (CommonValidate.validateQueryState(this, msg, response, "编辑失败")) {
                ShowMsg.showToast(this, "编辑成功");
                FramewrokApplication.getInstance().setActivityResult(
                        ProviderResultActivity.CODE_EDIT_PRODUCT, null);
                finish();
            }
        }
    }

    private void showTimePopupWindow() {
        if (null == mTimePopupWindow) {
            mTimePopupWindow = new TimePopupWindow(this);
            mTimePopupWindow.setYearsLength(TimeUtil.getLocalTime().getYear(),
                    TimeUtil.getLocalTime().getYear() + 50);
            mTimePopupWindow
                    .setOnPopSureClickListener(new OnPopSureClickListener() {

                        @Override
                        public void onPopSureClick(Object firstObj,
                                                   Object secondObj, Object thirdObj) {
                            String date =
                                    firstObj + "-" + secondObj + "-" + thirdObj;
                            if (isSelectStateDate) {
                                mTvewStartDate.setText(date);
                            } else {
                                mTvewEndDate.setText(date);
                            }
                        }
                    });
        }

        mTimePopupWindow.showWindowBottom(mEdittxtAuctionGoodsStock);
    }

    private void showTimeCategory() {
        if (null == mPopupWindow) {
            mPopupWindow = new WheelViewPopupWindow(this);
            mPopupWindow.dismissWheelViewSecond();
            mPopupWindow.dismissWheelViewThird();

            mPopupWindow.setOnPopSureClickListener(new OnPopSureClickListener() {

                @Override
                public void onPopSureClick(Object firstObj, Object secondObj,
                                           Object thirdObj) {
                    int position = mPopupWindow.getFirstViewCurItem();
                    if (isSelectStartTime) {
                        miStartTimeId = position;
                        mTvewStartTime.setText((String) firstObj);
                    } else {
                        miEndTimeId = position;
                        mTvewEndTime.setText((String) firstObj);
                    }
                }
            });
        }

        mPopupWindow.setFirstViewAdapter(mArrayHours);
        mPopupWindow.showWindowBottom(mEdittxtAuctionGoodsStock);
    }

    private boolean isSelectStateDate;

    private boolean isSelectStartTime;

    @Click(resName = "llayout_start_date_click")
    void onClickLlayoutStateDate() {
        isSelectStateDate = true;
        showTimePopupWindow();
    }

    @Click(resName = "llayout_start_time_click")
    void onClickLlayoutStateTime() {
        isSelectStartTime = true;
        showTimeCategory();
    }

    @Click(resName = "llayout_end_date_click")
    void onClickLlayoutEndDate() {
        isSelectStateDate = false;
        showTimePopupWindow();
    }

    @Click(resName = "llayout_end_time_click")
    void onClickLlayoutEndTime() {
        isSelectStartTime = false;
        showTimeCategory();
    }

    @Click(resName = "tvew_deposit_warehouse_click")
    void onClickTvewDepositWarehouse() {
    }

    @Click(resName = "tvew_auction_now_release_click")
    void onClickTvewAuctionNowRelease() {
        if (mProductId > 0) {
            editProduct();
        } else {
            addProduct();
        }
    }

    private void editProduct() {
        mEditRequest = new ProductEditAuctionRequest();

        mEditRequest.Id = mProductId;
        mEditRequest.Title = mEdittxtTitle.getText().toString();
        mEditRequest.CategoryId = mCategoryId;
        mEditRequest.Description = mstrDesc;
        mEditRequest.Address = mstrAddress;
        mEditRequest.Type = ProductType.PAIPIN.getValue();
        mEditRequest.Pics = getUploadFile();
        mEditRequest.Movie = getVideoUploadFile();
        mEditRequest.MovieThumbId = getVideoThumbnailFile();

        mEditRequest.InSpecialPanic = getInSpecialPainc();
        mEditRequest.InSpecialGift = getInSpecialGift();
        mEditRequest.SecurityDelivery = getSecurityDelivery();
        mEditRequest.Security7days = getSecurity7days();
        mEditRequest.StartDate = mTvewStartDate.getText().toString();
        mEditRequest.EndDate = mTvewEndDate.getText().toString();
        mEditRequest.StartTime = miStartTimeId;
        mEditRequest.EndTime = miEndTimeId;

        try {
            if (!TextUtils.isEmpty(mEdittxtAuctionGoodsStartprice.getText()
                    .toString())) {
                mEditRequest.Price =
                        Double.parseDouble(mEdittxtAuctionGoodsStartprice
                                .getText().toString());
            }

            if (!TextUtils.isEmpty(mEdittxtAuctionGoodsTransactionprice
                    .getText().toString())) {
                mEditRequest.Maxprice =
                        Double.parseDouble(mEdittxtAuctionGoodsTransactionprice
                                .getText().toString());
            }

            if (!TextUtils.isEmpty(mEdittxtAuctionGoodsStock.getText()
                    .toString())) {
                mEditRequest.Quantity =
                        Integer.parseInt(mEdittxtAuctionGoodsStock.getText()
                                .toString());
            }

            if (null != mAddress) {
                mEditRequest.ProvinceId = Integer.parseInt(mAddress.province_id);
                mEditRequest.CityId = Integer.parseInt(mAddress.city_id);
                mEditRequest.AreaId = Integer.parseInt(mAddress.area_id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        boolean isSend =
                ProviderProductBusiness.editPaipingProduct(this,
                        getHttpDataLoader(), mEditRequest);
        if (isSend) {
            showWaitDialog(1, false, R.string.common_submit_data);
        }
    }

    private void addProduct() {
        mAddRequest = new ProductAddAuctionRequest();

        mAddRequest.Title = mEdittxtTitle.getText().toString();
        mAddRequest.CategoryId = mCategoryId;
        mAddRequest.Description = mstrDesc;
        mAddRequest.Address = mstrAddress;
        mAddRequest.Type = ProductType.PAIPIN.getValue();
        mAddRequest.Pics = getUploadFile();
        mAddRequest.Movie = getVideoUploadFile();
        mAddRequest.MovieThumbId = getVideoThumbnailFile();
        mAddRequest.SecurityDelivery = getSecurityDelivery();
        mAddRequest.Security7days = getSecurity7days();
        mAddRequest.StartDate = mTvewStartDate.getText().toString();
        mAddRequest.EndDate = mTvewEndDate.getText().toString();
        mAddRequest.StartTime = miStartTimeId;
        mAddRequest.EndTime = miEndTimeId;

        try {
            if (!TextUtils.isEmpty(mEdittxtAuctionGoodsStartprice.getText()
                    .toString())) {
                mAddRequest.Price =
                        Double.parseDouble(mEdittxtAuctionGoodsStartprice
                                .getText().toString());
            }

            if (!TextUtils.isEmpty(mEdittxtAuctionGoodsTransactionprice
                    .getText().toString())) {
                mAddRequest.Maxprice =
                        Double.parseDouble(mEdittxtAuctionGoodsTransactionprice
                                .getText().toString());
            }

            if (!TextUtils.isEmpty(mEdittxtAuctionGoodsStock.getText()
                    .toString())) {
                mAddRequest.Quantity =
                        Integer.parseInt(mEdittxtAuctionGoodsStock.getText()
                                .toString());
            }

            if (null != mAddress) {
                mAddRequest.ProvinceId = Integer.parseInt(mAddress.province_id);
                mAddRequest.CityId = Integer.parseInt(mAddress.city_id);
                mAddRequest.AreaId = Integer.parseInt(mAddress.area_id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        boolean isSend =
                ProviderProductBusiness.addPaipingProduct(this,
                        getHttpDataLoader(), mAddRequest);
        if (isSend) {
            showWaitDialog(1, false, R.string.common_submit_data);
        }
    }

    @Override
    protected void showProductInfo(ProductItem productItem) {
        super.showProductInfo(productItem);

        mEdittxtAuctionGoodsStock.setText(productItem.quantity);
        mEdittxtAuctionGoodsTransactionprice.setText(""
                + StringUtil.formatProgress(productItem.maxprice));
        mEdittxtAuctionGoodsStartprice.setText(""
                + StringUtil.formatProgress(productItem.price));

        String startDate =
                TimeUtil.transformLongTimeFormat(
                        Long.parseLong(productItem.start_time) * 1000,
                        TimeUtil.STR_FORMAT_DATE);
        String startTime =
                TimeUtil.transformLongTimeFormat(
                        Long.parseLong(productItem.start_time) * 1000,
                        TimeUtil.STR_FORMAT_HOUR_MINUTE);

        String endDate =
                TimeUtil.transformLongTimeFormat(
                        Long.parseLong(productItem.end_time) * 1000,
                        TimeUtil.STR_FORMAT_DATE);
        String endTime =
                TimeUtil.transformLongTimeFormat(
                        Long.parseLong(productItem.end_time) * 1000,
                        TimeUtil.STR_FORMAT_HOUR_MINUTE);

        mTvewStartDate.setText(startDate);
        mTvewEndDate.setText(endDate);

        mTvewStartTime.setText(startTime);
        mTvewEndTime.setText(endTime);

        for (int i = 0; i < mArrayHours.length; i++) {
            if (mArrayHours[i].equals(startTime)) {
                miStartTimeId = i;
            }

            if (mArrayHours[i].equals(endTime)) {
                miEndTimeId = i;
            }
        }
    }

    @Click(resName = "llayout_description_click")
    protected void onClickEditDescription() {
        super.onClickEditDescription();
    }

    @Click(resName = "llayout_category_click")
    protected void onClickRlayoutProductCategory() {
        super.onClickRlayoutProductCategory();
    }

    @Override
    public TextView getCategoryEditvew() {
        return mTvewAuctionGoodsCategory;
    }

    @Override
    public ProgressBar getCategoryProgressBar() {
        return mProgressBar;
    }

    @Override
    public TextView getDescriptionTextView() {
        return mTvewAuctionGoodsDescription;
    }

    @Override
    public String getCategoryType() {
        return CategoryType.PRODCUT.getValue();
    }

}