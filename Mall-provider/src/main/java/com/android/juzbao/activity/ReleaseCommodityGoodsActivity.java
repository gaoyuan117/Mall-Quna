
package com.android.juzbao.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.juzbao.constant.ProviderResultActivity;
import com.android.juzbao.enumerate.CategoryType;
import com.android.juzbao.enumerate.ProductType;
import com.android.juzbao.enumerate.SpecialType;
import com.android.juzbao.model.ProviderProductBusiness;
import com.android.juzbao.provider.R;
import com.android.juzbao.util.CommonValidate;
import com.android.zcomponent.common.uiframe.FramewrokApplication;
import com.android.zcomponent.http.api.model.MessageData;
import com.android.zcomponent.util.CommonUtil;
import com.android.zcomponent.util.ShowMsg;
import com.android.zcomponent.util.ShowMsg.IConfirmDialogKeyBack;
import com.android.zcomponent.util.StringUtil;
import com.server.api.model.AddProduct;
import com.server.api.model.CommonReturn;
import com.server.api.model.ProductItem;
import com.server.api.model.jifenmodel.JifenCommonReturn;
import com.server.api.service.ProductService;
import com.server.api.service.ProductService.ProductAddPutongRequest;
import com.server.api.service.ProductService.ProductEditPutongRequest;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * <p>
 * Description: 发布商品
 * </p>
 *
 * @ClassName:ReleaseCommodityGoodsActivity
 * @author: wei
 * @date: 2015-11-11
 */
@EActivity(resName = "activity_release_commodity_goods")
public class ReleaseCommodityGoodsActivity extends AddProductActivity {

  @ViewById(resName = "edittxt_commodity_goods_price_show")
  EditText mEdittxtCommodityGoodsPrice;

  @ViewById(resName = "edittxt_commodity_goods_jifen_show")
  EditText mEdittxtCommodityGoodsJifen;

  @ViewById(resName = "edittxt_commodity_goods_stock_show")
  EditText mEdittxtCommodityGoodsStock;

  @ViewById(resName = "edittxt_commodity_goods_indentify_show")
  EditText mEdittxtCommodityGoodsIndentify;

  @ViewById(resName = "tvew_commodity_goods_category_show")
  TextView mTvewCommodityGoodsCategory;

  @ViewById(resName = "tvew_commodity_goods_description_show")
  TextView mTvewCommodityGoodsDescription;

  @ViewById(resName = "tvew_commodity_now_release_click")
  TextView mTvewCommodityGoodsRelease;

  @ViewById(resName = "progressbar_categoryz_show")
  ProgressBar mProgressBar;

  @ViewById(resName = "ll_add_panicbuy")
  LinearLayout mLladdPanicBuy;

  private ProductAddPutongRequest mRequest;

  private ProductEditPutongRequest mPutongRequest;

  private boolean isPutHalt = false;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @AfterViews
  protected void initUI() {
    super.initUI();

    if (mProductId > 0) {
      mTvewWarehouse.setText("加入抢购会");
      mTvewCommodityGoodsRelease.setText("保存");
      getTitleBar().setTitleText("编辑商品");
      mLladdPanicBuy.setVisibility(View.VISIBLE);
    } else {
      getTitleBar().setTitleText("发布商品");
      mLladdPanicBuy.setVisibility(View.GONE);
    }

    mEdittxtCommodityGoodsPrice.addTextChangedListener(
        new StringUtil.DecimalTextWatcher(mEdittxtCommodityGoodsPrice, 2));
    mEdittxtCommodityGoodsJifen.addTextChangedListener(
        new StringUtil.DecimalTextWatcher(mEdittxtCommodityGoodsJifen, 0));
  }

  @Override
  public void onRecvMsg(MessageData msg) throws Exception {
    super.onRecvMsg(msg);

    if (msg.valiateReq(ProductService.ProductAddPutongRequest.class)) {
      final AddProduct response = (AddProduct) msg.getRspObject();

      if (CommonValidate.validateQueryState(this, msg, response, "发布失败")) {
        mProductId = response.data.id;
        if (isPutHalt) {
          ProviderProductBusiness.queryHaltProduct(getHttpDataLoader(), String.valueOf(mProductId));
        } else {
          ProviderProductBusiness.queryPutawayProduct(getHttpDataLoader(), String.valueOf
              (mProductId));
        }
        ShowMsg.showToast(this, "发布成功");
        ShowMsg.showConfirmDialog(this, new IConfirmDialogKeyBack() {

          @Override
          public void onConfirm(boolean confirmValue,
                                boolean onKeyBack) {
            if (onKeyBack) {
              finish();
              return;
            }

            if (!confirmValue) {
              Bundle bundle = new Bundle();
              bundle.putString("id", String.valueOf(response.data.id));
              getIntentHandle().intentToActivity(bundle,
                  EditOptionActivity_.class);
            }
            finish();
          }
        }, "否", "是", "是否需要添加商品选项？");
      }
    } else if (msg.valiateReq(ProductService.ProductEditPutongRequest.class)) {
      final CommonReturn response = (CommonReturn) msg.getRspObject();

      if (CommonValidate.validateQueryState(this, msg, response, "编辑失败")) {
        ShowMsg.showToast(this, "编辑成功");
        FramewrokApplication.getInstance().setActivityResult(ProviderResultActivity
            .CODE_EDIT_PRODUCT, null);
        finish();
      }
    } else if (msg.valiateReq(ProductService.PutawayProductRequest.class)) {
      JifenCommonReturn response = msg.getRspObject();

      if (response != null && response.code == -3) {
        if (!TextUtils.isEmpty(response.message)) {
          ShowMsg.showToast(this, response.message);
        }
        ShowMsg.showToast(this, response.data);
      }
    }
  }

  @Override
  protected void showProductInfo(ProductItem productItem) {
    super.showProductInfo(productItem);

    mEdittxtCommodityGoodsStock.setText(productItem.quantity);
    mEdittxtCommodityGoodsPrice.setText("" + StringUtil.formatProgress(productItem.price));
    if (null != productItem.score) {
      mEdittxtCommodityGoodsJifen.setText(productItem.score);
      CommonUtil.setEditViewSelectionEnd(mEdittxtCommodityGoodsJifen);
    }
  }

  @Click(resName = "tvew_deposit_warehouse_click")
  void onClickTvewDepositWarehouse() {

    if (mProductId > 0) {
//            Bundle bundle = new Bundle();
//            bundle.putLong("id", mProductId);
//            getIntentHandle()
//                    .intentToActivity(bundle, ToSpecialActivity_.class);

      if (!mCheckBoxAddPaincBuy.isChecked()) {
        showToast("请勾选申请加入抢购会");
        return;
      }
      intentToPanicBuyingActivity();
    } else {
      isPutHalt = true;
      addProduct();
    }
  }

  @Click(resName = "llayout_description_click")
  protected void onClickEditDescription() {
    super.onClickEditDescription();
  }

  @Click(resName = "layout_category_click")
  protected void onClickRlayoutProductCategory() {
    super.onClickRlayoutProductCategory();
  }

  @Click(resName = "tvew_commodity_now_release_click")
  void onClickTvewCommodityNowRelease() {
    if (mProductId > 0) {
      editProduct();
    } else {
      isPutHalt = false;
      addProduct();
    }
  }

  private void intentToPanicBuyingActivity() {
    Bundle bundle = new Bundle();
    bundle.putLong("id", mProductId);
    bundle.putString("type", SpecialType.PANIC.getValue());
    getIntentHandle().intentToActivity(bundle, PanicBuyingActivity_.class);
  }

  private void editProduct() {
    mPutongRequest = new ProductEditPutongRequest();

    mPutongRequest.Id = mProductId;
    mPutongRequest.Type = ProductType.PUTONG.getValue();
    mPutongRequest.Title = mEdittxtTitle.getText().toString();
    mPutongRequest.CategoryId = mCategoryId;
    mPutongRequest.Description = mstrDesc;
    mPutongRequest.Address = mstrAddress;
    mPutongRequest.Pics = getUploadFile();
    mPutongRequest.Movie = getVideoUploadFile();
    mPutongRequest.MovieThumbId = getVideoThumbnailFile();
    mPutongRequest.Security7days = getSecurity7days();
    mPutongRequest.SecurityDelivery = getSecurityDelivery();
    mPutongRequest.InSpecialPanic = getAddPanicBuy();
//    mPutongRequest.InSpecialPanic = getInSpecialPainc();
    mPutongRequest.InSpecialGift = getInSpecialGift();
    if (null != mAddress) {
      mPutongRequest.ProvinceId = mAddress.province_id;
      mPutongRequest.CityId = mAddress.city_id;
      mPutongRequest.AreaId = mAddress.area_id;
    }

    try {
      if (!TextUtils.isEmpty(mEdittxtCommodityGoodsStock.getText().toString())) {
        mPutongRequest.Quantity = Integer.parseInt(mEdittxtCommodityGoodsStock.getText().toString
            ());
      }

      if (!TextUtils.isEmpty(mEdittxtCommodityGoodsPrice.getText().toString())) {
        mPutongRequest.Price = Double.parseDouble(mEdittxtCommodityGoodsPrice.getText().toString());
      }

      if (!TextUtils.isEmpty(mEdittxtCommodityGoodsJifen.getText().toString())) {
        mPutongRequest.Score = Integer.parseInt(mEdittxtCommodityGoodsJifen.getText().toString());
      }

    } catch (Exception e) {
      e.printStackTrace();
    }

    boolean isSend = ProviderProductBusiness.editPutongProduct(this, getHttpDataLoader(),
        mPutongRequest);
    if (isSend) {
      showWaitDialog(1, false, R.string.common_submit_data);
    }
  }

  private void addProduct() {
    mRequest = new ProductAddPutongRequest();

    mRequest.Type = ProductType.PUTONG.getValue();
    mRequest.Title = mEdittxtTitle.getText().toString();
    mRequest.CategoryId = mCategoryId;
    mRequest.Description = mstrDesc;
    mRequest.Address = mstrAddress;
    mRequest.Pics = getUploadFile();
    mRequest.Movie = getVideoUploadFile();
    mRequest.MovieThumbId = getVideoThumbnailFile();
    mRequest.Security7days = getSecurity7days();
    mRequest.SecurityDelivery = getSecurityDelivery();
    if (null != mAddress) {
      mRequest.ProvinceId = mAddress.province_id;
      mRequest.CityId = mAddress.city_id;
      mRequest.AreaId = mAddress.area_id;
    }

    try {
      if (!TextUtils.isEmpty(mEdittxtCommodityGoodsStock.getText().toString())) {
        mRequest.Quantity = Integer.parseInt(mEdittxtCommodityGoodsStock.getText().toString());
      }

      if (!TextUtils.isEmpty(mEdittxtCommodityGoodsPrice.getText().toString())) {
        mRequest.Price = Double.parseDouble(mEdittxtCommodityGoodsPrice.getText().toString());
      }

      if (!TextUtils.isEmpty(mEdittxtCommodityGoodsJifen.getText().toString())) {
        mRequest.Score = Integer.parseInt(mEdittxtCommodityGoodsJifen.getText().toString());
      }

    } catch (Exception e) {
      e.printStackTrace();
    }

    boolean isSend = ProviderProductBusiness.addPutongProduct(this, getHttpDataLoader(), mRequest);
    if (isSend) {
      showWaitDialog(1, false, R.string.common_submit_data);
    }
  }

  @Override
  public TextView getCategoryEditvew() {
    return mTvewCommodityGoodsCategory;
  }

  @Override
  public String getCategoryType() {
    return CategoryType.PRODCUT.getValue();
  }

  @Override
  public ProgressBar getCategoryProgressBar() {
    return mProgressBar;
  }

  @Override
  public TextView getDescriptionTextView() {
    return mTvewCommodityGoodsDescription;
  }

}