
package com.android.juzbao.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.provider.MediaStore.Video.Thumbnails;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.juzbao.constant.ProviderResultActivity;
import com.android.juzbao.enumerate.Security7days;
import com.android.juzbao.enumerate.SecurityDelivery;
import com.android.juzbao.model.ProviderProductBusiness;
import com.android.juzbao.provider.R;
import com.android.juzbao.util.CommonValidate;
import com.android.video.activity.VideoActivity;
import com.android.video.surfaceview.VideoPlayView;
import com.android.video.surfaceview.VideoPlayView.ThumbnailCallBack;
import com.android.zcomponent.activity.EditActivity;
import com.android.zcomponent.http.api.host.Endpoint;
import com.android.zcomponent.http.api.model.MessageData;
import com.android.zcomponent.http.constant.ErrorCode;
import com.android.zcomponent.json.JsonSerializerFactory;
import com.android.zcomponent.util.BitmapUtil;
import com.android.zcomponent.util.ClientInfo;
import com.android.zcomponent.util.CommonUtil;
import com.android.zcomponent.util.FileUtil;
import com.android.zcomponent.util.PictureSelectUtil;
import com.android.zcomponent.util.ShowMsg;
import com.android.zcomponent.util.StringUtil;
import com.android.zcomponent.util.TimeUtil;
import com.android.zcomponent.views.TimePopupWindow;
import com.android.zcomponent.views.TimePopupWindow.OnTimeEnsureListener;
import com.android.zcomponent.views.WheelViewPopupWindow;
import com.android.zcomponent.views.WheelViewPopupWindow.OnItemChangeListener;
import com.android.zcomponent.views.WheelViewPopupWindow.OnPopSureClickListener;
import com.android.zcomponent.views.swipebacklayout.lib.app.SwipeBackActivity;
import com.server.api.model.Address;
import com.server.api.model.Product;
import com.server.api.model.ProductCategory;
import com.server.api.model.ProductItem;
import com.server.api.model.ProductOptions;
import com.server.api.model.UploadFile;
import com.server.api.model.UploadFile.FileInfo;
import com.server.api.service.FileService.PostFileRequest;
import com.server.api.service.FileService.PostFileVideoRequest;
import com.server.api.service.ProductService;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Description:
 * </p>
 *
 * @ClassName:AddProductActivity
 * @author: wei
 * @date: 2015-11-10
 */
public abstract class AddProductActivity extends SwipeBackActivity {

  CheckBox mCheckboxSupportRefund;
  CheckBox mCheckBoxAddPaincBuy;

  RadioButton mCheckboxOneDay;
  RadioButton mCheckboxTwoDays;
  RadioButton mCheckboxThreeDays;
  ImageView mImgvewPhoto1;
  ImageView mImgvewPhoto2;
  ImageView mImgvewPhoto3;
  ImageView mImgvewVideo;
  RelativeLayout mRlayoutVideo;
  LinearLayout mLlayoutPhoto;
  EditText mEdittxtTitle;
  TextView mTvewShipAddress;
  TextView mTvewAddress;

  TextView mTvewOption;

  TextView mTvewAddPhoto;

  TextView mTvewWarehouse;

  private WheelViewPopupWindow mPopupWindow;

  private List<ProductCategory.CategoryItem> mlistCategory;

  private String[] mlistCategoryName;

  private String[] mlistCategoryName1;

  private String[] mlistCategoryName2;

  protected int mCategoryId;

  protected String mstrDesc;

  protected String mstrAddress;

  protected Address.Data mAddress;

  protected boolean isEditAddress = false;

  protected TimePopupWindow mTimePopupWindow;

  protected PictureSelectUtil mPictureSelectUtil;

  protected String mstrFilePath;

  protected String mstrThumbnailPath;

  protected List<FileInfo> mImageFile = new ArrayList<UploadFile.FileInfo>();

  protected FileInfo mVideoFile;

  protected FileInfo mThumbnailFile;

  protected long mProductId;

  protected ProductItem mProduct;

  protected String videoUrl;

  protected String thumbnail;

  protected boolean isUplpadThumbnail;

  protected boolean isEditImage = false;

  protected int miImagePosition = 1;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    mPictureSelectUtil = new PictureSelectUtil(this);
    mPictureSelectUtil.setOutParams(4, 3, 800, 600);
  }

  protected void initUI() {
    bindWidget();

    Intent intent = getIntent();

    mProductId = intent.getLongExtra("id", -1);
    if (mProductId > 0) {
      ProviderProductBusiness.queryMyProductDetail(getHttpDataLoader(), mProductId);
      ProviderProductBusiness.queryProductOptions(getHttpDataLoader(), String.valueOf(mProductId));
      findViewById(R.id.llayout_option_click).setVisibility(View.VISIBLE);
      showWaitDialog(1, false, R.string.common_loading_title);
    } else {
      findViewById(R.id.llayout_option_click).setVisibility(View.GONE);
    }
  }

  private void bindWidget() {
    mCheckboxSupportRefund =
        (CheckBox) findViewById(R.id.checkbox_support_refund_show);
    mCheckBoxAddPaincBuy = (CheckBox) findViewById(R.id.checkbox_add_panicbug_show);

    mCheckboxOneDay =
        (RadioButton) findViewById(R.id.checkbox_one_day_show);
    mCheckboxTwoDays =
        (RadioButton) findViewById(R.id.checkbox_two_days_show);
    mCheckboxThreeDays =
        (RadioButton) findViewById(R.id.checkbox_three_days_show);

    mLlayoutPhoto = (LinearLayout) findViewById(R.id.llayout_photo_show);
    mImgvewPhoto1 = (ImageView) findViewById(R.id.imgvew_auction_pic1_show);
    mImgvewPhoto2 = (ImageView) findViewById(R.id.imgvew_auction_pic2_show);
    mImgvewPhoto3 = (ImageView) findViewById(R.id.imgvew_auction_pic3_show);
    mImgvewVideo = (ImageView) findViewById(R.id.imgvew_auction_video_show);
    mRlayoutVideo =
        (RelativeLayout) findViewById(R.id.rlayout_auction_video_show);
    mTvewAddPhoto =
        (TextView) findViewById(R.id.tvew_auction_goods_uploding_click);
    mEdittxtTitle = (EditText) findViewById(R.id.edittxt_goods_title_show);
    mTvewShipAddress = (TextView) findViewById(R.id.tvew_ship_address_show);
    mTvewAddress = (TextView) findViewById(R.id.tvew_address_show);
    mTvewOption = (TextView) findViewById(R.id.tvew_option_show);
    mTvewWarehouse =
        (TextView) findViewById(R.id.tvew_deposit_warehouse_click);

    setClickListener();
  }

  public void setClickListener() {
    View shipAddress = findViewById(R.id.llayout_ship_address_click);
    if (null != shipAddress) {
      shipAddress.setOnClickListener(new OnClickListener() {

        @Override
        public void onClick(View v) {
          getIntentHandle().intentToActivity(
              CitySelectActivity_.class);
        }
      });
    }

    View address = findViewById(R.id.llayout_address_click);
    if (null != address) {
      address.setOnClickListener(new OnClickListener() {

        @Override
        public void onClick(View v) {
          onClickEditAddress();
        }
      });
    }

    View option = findViewById(R.id.llayout_option_click);
    if (null != option) {
      option.setOnClickListener(new OnClickListener() {

        @Override
        public void onClick(View v) {
          onClickEditOption();
        }
      });
    }

    if (null != mTvewAddPhoto) {
      mTvewAddPhoto.setOnClickListener(new OnClickListener() {

        @Override
        public void onClick(View v) {
          if (null != mImageFile && mImageFile.size() > 2) {
            ShowMsg.showToast(getApplicationContext(), "最多提交3张图片");
            return;
          }

          isEditImage = false;
          miImagePosition = mImageFile.size();
          onClickTvewSelectPhoto();
        }
      });
    }

    if (null != mImgvewPhoto1) {
      mImgvewPhoto1.setOnClickListener(new OnClickListener() {

        @Override
        public void onClick(View v) {
          miImagePosition = 0;
          isEditImage = true;
          onClickTvewSelectPhoto();
        }
      });
    }

    if (null != mImgvewPhoto2) {
      mImgvewPhoto2.setOnClickListener(new OnClickListener() {

        @Override
        public void onClick(View v) {
          miImagePosition = 1;
          isEditImage = true;
          onClickTvewSelectPhoto();
        }
      });
    }

    if (null != mImgvewPhoto3) {
      mImgvewPhoto3.setOnClickListener(new OnClickListener() {

        @Override
        public void onClick(View v) {
          miImagePosition = 2;
          isEditImage = true;
          onClickTvewSelectPhoto();
        }
      });
    }

    if (null != mRlayoutVideo) {
      mRlayoutVideo.setOnClickListener(new OnClickListener() {

        @Override
        public void onClick(View v) {
          if (!TextUtils.isEmpty(videoUrl)) {
            VideoActivity.open(AddProductActivity.this, videoUrl,
                thumbnail);
          }
        }
      });
    }
  }

  protected void showProductInfo(ProductItem productItem) {
    if (null != productItem) {
      mProduct = productItem;

      mEdittxtTitle.setText(productItem.title);
      CommonUtil.setEditViewSelectionEnd(mEdittxtTitle);

      TextView tvewCategory = getCategoryEditvew();
      tvewCategory.setText(productItem.category_title);
      try {
        mCategoryId = Integer.parseInt(productItem.category_id);
      } catch (Exception e) {
        e.getStackTrace();
      }

      if (String.valueOf(Security7days.SUPPORT.getValue()).equals(
          productItem.security_7days)) {
        mCheckboxSupportRefund.setChecked(true);
      }

      if ("1".equals(productItem.in_special_panic)) {
        mCheckBoxAddPaincBuy.setChecked(true);
      }

      if (String.valueOf(SecurityDelivery.HOUR24.getValue()).equals(
          productItem.security_delivery)) {
        mCheckboxOneDay.setChecked(true);
      } else if (String.valueOf(SecurityDelivery.HOUR48.getValue()).equals(
          productItem.security_delivery)) {
        mCheckboxTwoDays.setChecked(true);
      } else if (String.valueOf(SecurityDelivery.HOUR72.getValue()).equals(
          productItem.security_delivery)) {
        mCheckboxThreeDays.setChecked(true);
      }

      if (!TextUtils.isEmpty(productItem.description)) {
        mstrDesc = productItem.description;
        setContentEditStatus(getDescriptionTextView(), mstrDesc);
      }

      if (!TextUtils.isEmpty(productItem.address) && null != mTvewAddress) {
        mstrAddress = productItem.address;
        setContentEditStatus(mTvewAddress, mstrAddress);
      }

      if (!TextUtils.isEmpty(productItem.address_full)
          && null != mTvewShipAddress) {
        mTvewShipAddress.setText(productItem.address_full.replace(
            productItem.address, ""));

        mAddress = new Address.Data();
        mAddress.province_id = productItem.province_id;
        mAddress.city_id = productItem.city_id;
        mAddress.area_id = productItem.area_id;
      }

      if (null != productItem.movie) {
        videoUrl = Endpoint.HOST + productItem.movie.path;

        mVideoFile = new FileInfo();
        mVideoFile.id = productItem.movie.id;
        mVideoFile.path = productItem.movie.path;

        if (!TextUtils.isEmpty(productItem.movie.thumb_image_path)) {
          thumbnail =
              Endpoint.HOST + productItem.movie.thumb_image_path;
          loadImage(mImgvewVideo, thumbnail,
              R.drawable.img_empty_logo_small);
          mRlayoutVideo.setVisibility(View.VISIBLE);
        } else {
          new VideoPlayView(this).getVideoThumbnail(videoUrl,
              new ThumbnailCallBack() {

                @Override
                public void onThumbnail(Bitmap bitmap) {
                  mImgvewVideo.setImageBitmap(BitmapUtil
                      .rotateImage(bitmap, 90));
                  mRlayoutVideo.setVisibility(View.VISIBLE);
                }
              });
        }
      }

      if (null != productItem.images && productItem.images.length > 0) {
        mImageFile.clear();
        mLlayoutPhoto.setVisibility(View.VISIBLE);
        for (int i = 0; i < productItem.images.length; i++) {
          FileInfo fileInfo = new FileInfo();
          fileInfo.id = productItem.images[i].cover_id;
          fileInfo.path = productItem.images[i].path;
          mImageFile.add(fileInfo);

          String imageUrl = Endpoint.HOST + fileInfo.path;
          if (i == 0) {
            loadImage(mImgvewPhoto1, imageUrl,
                R.drawable.img_empty_logo_middle);
          } else if (i == 1) {
            loadImage(mImgvewPhoto2, imageUrl,
                R.drawable.img_empty_logo_middle);
          } else if (i == 2) {
            loadImage(mImgvewPhoto3, imageUrl,
                R.drawable.img_empty_logo_middle);
          }
        }
      }
    }
  }

  @Override
  public void onRecvMsg(MessageData msg) throws Exception {
    if (msg.valiateReq(ProductService.MyProductRequest.class)) {
      Product response = msg.getRspObject();
      if (null != response) {
        if (response.code == ErrorCode.INT_QUERY_DATA_SUCCESS) {
          showProductInfo(response.Data);
        }
      }
    } else if (msg.valiateReq(ProductService.ProductOptionRequest.class)) {
      ProductOptions response = msg.getRspObject();
      if (CommonValidate.validateQueryState(this, msg, response)) {
        setContentEditStatus(mTvewOption, "已经编辑");
      } else {
      }
    } else if (msg.valiateReq(PostFileRequest.class)) {
      UploadFile uploadFile = ProviderProductBusiness.parseUploadFile(this, msg);
      if (null != uploadFile && null != uploadFile.Data) {
        if (isUplpadThumbnail) {
          mThumbnailFile = uploadFile.Data.file;
          FileUtil.delFile(mstrThumbnailPath);
        } else {
          if (isEditImage) {
            mImageFile.set(miImagePosition, uploadFile.Data.file);
          } else {
            mImageFile.add(uploadFile.Data.file);
          }
          mLlayoutPhoto.setVisibility(View.VISIBLE);
        }
      }
      isUplpadThumbnail = false;
    } else if (msg.valiateReq(PostFileVideoRequest.class)) {
      UploadFile uploadFile = ProviderProductBusiness.parseUploadFile(this, msg);
      if (null != uploadFile && null != uploadFile.Data) {
        mVideoFile = uploadFile.Data.file;
        mImgvewVideo.setVisibility(View.VISIBLE);

        isUplpadThumbnail = true;
        sendPostRequest(mstrThumbnailPath);
      } else {
        dismissWaitDialog();
      }
    } else if (msg.valiateReq(ProductService.GroupRequest.class)) {
      ProductCategory reaponse = msg.getRspObject();
      getCategoryProgressBar().setVisibility(View.GONE);
      if (CommonValidate.validateQueryState(this, msg, reaponse,
          "查询商品分类失败")) {
        mlistCategory =
            new ArrayList<ProductCategory.CategoryItem>(reaponse.Data);
        mlistCategoryName = new String[mlistCategory.size()];
        for (int i = 0; i < mlistCategory.size(); i++) {
          mlistCategoryName[i] = mlistCategory.get(i).name;
        }
        showCategory();
        setChildCategory(0);
      } else {
        ShowMsg.showToast(this, "未查询到商品分类");
      }
    } else if (msg.valiateReq(ProductService.CategoryTreeRequest.class)) {
      ProductCategory reaponse = msg.getRspObject();
      mPopupWindow.setSecondViewAdapter(null);
      if (CommonValidate.validateQueryState(this, msg, reaponse)) {
        if (null != reaponse.Data) {
          for (int i = 0; i < mlistCategory.size(); i++) {
            if (reaponse.Data.get(0).gid.equals(mlistCategory.get(i).id)) {
              mlistCategory.get(i)._child = reaponse.Data;
              setSecondCategory(i, 0);
              break;
            }
          }
        }
      }
    } else if (msg.valiateReq(ProductService.ThirdCategoryTreeRequest.class)) {
      ProductCategory reaponse = msg.getRspObject();
      mPopupWindow.setThirdViewAdapter(null);
      if (CommonValidate.validateQueryState(this, msg, reaponse)) {
        if (null != reaponse.Data) {
          for (int j = 0; j < mlistCategory.size(); j++) {
            List<ProductCategory.CategoryItem> secondItems = mlistCategory.get(j)._child;
            if (null != secondItems) {
              for (int i = 0; i < secondItems.size(); i++) {
                if (reaponse.Data.get(0).gid.equals(secondItems.get(i).id)) {
                  mlistCategory.get(j)._child.get(i)._child = reaponse.Data;
                  setThirdCategory(j, i);
                  break;
                }
              }
            }
          }
        }
      }
    }
  }

  private int ifirstPosition = -1;
  private int isecondPosition = -1;
  private int ithirdPosition = -1;


  private void showCategory() {
    if (null == mPopupWindow) {
      mPopupWindow = new WheelViewPopupWindow(this);
      mPopupWindow.setOnItemChangeListener(new OnItemChangeListener() {

        @Override
        public void onPopItemChange(int firstPosition,
                                    int secondPosition, int thirdPosition) {
          if (ifirstPosition != firstPosition) {
            setChildCategory(firstPosition);
            ifirstPosition = firstPosition;
          }
          if (isecondPosition != secondPosition) {
            setThirdCategory(firstPosition, secondPosition);
            isecondPosition = secondPosition;
          }
        }
      });
      mPopupWindow.setOnPopSureClickListener(new OnPopSureClickListener() {

        @Override
        public void onPopSureClick(Object firstObj, Object secondObj,
                                   Object thirdObj) {
          int parentPosition = mPopupWindow.getFirstViewCurItem();
          int childPosition = mPopupWindow.getSecondViewCurItem();
          int thirdPosition = mPopupWindow.getThirdViewCurItem();

          if (mlistCategory.size() > parentPosition) {
            try {

              ProductCategory.CategoryItem firstItem = mlistCategory.get(parentPosition);
              String parentTitle = firstItem.name;
              String content = "";
              ProductCategory.CategoryItem secondItem = null;
              String childTitle = "";
              if (null != firstItem._child && firstItem._child.size() > childPosition) {
                secondItem = firstItem._child.get(childPosition);
                childTitle = secondItem.title;
                mCategoryId =
                    Integer.parseInt(secondItem.id);
                content = parentTitle + " - " + childTitle;
              }

              String thirdTitle = "";
              if (null != secondItem._child && secondItem._child.size() > thirdPosition) {
                ProductCategory.CategoryItem thirdItem = secondItem._child.get(thirdPosition);
                thirdTitle = thirdItem.title;
                mCategoryId =
                    Integer.parseInt(thirdItem.id);
                content = content + " - " + thirdTitle;
              }
              getCategoryEditvew().setText(content);
            } catch (Exception e) {
              e.printStackTrace();
            }
          }
        }
      });
    }

    mPopupWindow.setFirstViewAdapter(mlistCategoryName);
    mPopupWindow.showWindowBottom(getCategoryEditvew());
  }

  private void setChildCategory(int firstPosition) {

    List<ProductCategory.CategoryItem> firstCategory = mlistCategory.get(firstPosition)._child;
    if (null == firstCategory) {
      String firstId = mlistCategory.get(firstPosition).id;
      ProviderProductBusiness.queryCategory(getHttpDataLoader(), firstId);
      mPopupWindow.setSecondViewAdapter(null);
    } else {

      if (firstCategory.size() > 0) {
        mlistCategoryName1 = new String[firstCategory.size()];
        for (int i = 0; i < firstCategory.size(); i++) {
          mlistCategoryName1[i] = firstCategory.get(i).title;
        }
        mPopupWindow.setSecondViewAdapter(mlistCategoryName1);
      }
    }
  }

  private void setSecondCategory(int firstPosition, int secondPosition) {

    List<ProductCategory.CategoryItem> secondCategory = mlistCategory.get(firstPosition)._child;
    if (null != secondCategory && secondCategory.size() > 0) {
      mlistCategoryName1 = new String[secondCategory.size()];
      for (int i = 0; i < secondCategory.size(); i++) {
        mlistCategoryName1[i] = secondCategory.get(i).title;
      }
      mPopupWindow.setSecondViewAdapter(mlistCategoryName1);

      setThirdCategory(firstPosition, secondPosition);
    }
  }

  private void setThirdCategory(int firstPosition, int secondPosition) {

    List<ProductCategory.CategoryItem> secondCategory = mlistCategory.get(firstPosition)._child;
    if (null != secondCategory && secondCategory.size() > 0) {
      List<ProductCategory.CategoryItem> thirdCategory = secondCategory.get(secondPosition)._child;
      if (null != thirdCategory && thirdCategory.size() > 0) {
        mlistCategoryName2 = new String[thirdCategory.size()];
        for (int i = 0; i < thirdCategory.size(); i++) {
          mlistCategoryName2[i] = thirdCategory.get(i).title;
        }
        mPopupWindow.setThirdViewAdapter(mlistCategoryName2);
      } else {
        String secondId = secondCategory.get(secondPosition).id;
        ProviderProductBusiness.queryThirdCategory(getHttpDataLoader(), secondId);
      }
    }
  }

  protected void onClickEditDescription() {
    isEditAddress = false;
    Bundle bundle = new Bundle();
    bundle.putString(EditActivity.CONTENT, mstrDesc);
    bundle.putString(EditActivity.TITLE, "编辑商品描述");
    bundle.putString(EditActivity.HINT, "请输入商品描述");
    bundle.putInt(EditActivity.MIN_LINE, 5);
    getIntentHandle().intentToActivity(bundle, EditDescActivity.class);
  }

  protected void onClickEditAddress() {
    isEditAddress = true;
    Bundle bundle = new Bundle();
    bundle.putString(EditActivity.CONTENT, mstrAddress);
    bundle.putString(EditActivity.TITLE, "编辑详细地址");
    bundle.putString(EditActivity.HINT, "请输入详细地址");
    bundle.putInt(EditActivity.MIN_LINE, 5);
    getIntentHandle().intentToActivity(bundle, EditActivity.class);
  }

  protected void onClickEditOption() {
    Bundle bundle = new Bundle();
    bundle.putString("id", String.valueOf(mProductId));
    getIntentHandle().intentToActivity(bundle, EditOptionActivity_.class);
  }

  public Integer[] getUploadFile() {
    if (null != mImageFile && mImageFile.size() > 0) {
      try {
        Integer[] pics = new Integer[mImageFile.size()];
        for (int i = 0; i < mImageFile.size(); i++) {
          pics[i] = Integer.parseInt(mImageFile.get(i).id);
        }
        return pics;
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return null;
  }

  public int getVideoUploadFile() {
    if (null != mVideoFile) {
      return Integer.parseInt(mVideoFile.id);
    }
    return 0;
  }

  public int getVideoThumbnailFile() {
    if (null != mThumbnailFile) {
      return Integer.parseInt(mThumbnailFile.id);
    }
    return 0;
  }

  public int getSecurity7days() {
    if (mCheckboxSupportRefund.isChecked()) {
      return Security7days.SUPPORT.getValue();
    } else {
      return Security7days.NOT_SUPPORT.getValue();
    }
  }

  public String getAddPanicBuy() {
    return mCheckBoxAddPaincBuy.isChecked() ? "1" : "0";
  }

  public int getSecurityDelivery() {
    if (mCheckboxOneDay.isChecked()) {
      return SecurityDelivery.HOUR24.getValue();
    } else if (mCheckboxTwoDays.isChecked()) {
      return SecurityDelivery.HOUR48.getValue();
    } else if (mCheckboxThreeDays.isChecked()) {
      return SecurityDelivery.HOUR72.getValue();
    }
    return SecurityDelivery.HOUR72.getValue();
  }

  public String getInSpecialGift() {
    if (null != mProduct) {
      return mProduct.in_special_gift;
    }
    return "0";
  }

  public String getInSpecialPainc() {
    if (null != mProduct) {
      return mProduct.in_special_panic;
    }
    return "0";
  }

  protected void onClickRlayoutProductCategory() {
    if (null != mlistCategoryName && mlistCategoryName.length > 0) {
      showCategory();
    } else {
      getCategoryProgressBar().setVisibility(View.VISIBLE);
      ProviderProductBusiness.queryGroup(getHttpDataLoader(), getCategoryType());
    }
  }

  /**
   * 点击上传图片或视频
   */
  protected void onClickTvewSelectPhoto() {
    mPictureSelectUtil.selectPicture();
  }

  public void sendPostRequest(String filePath) {
    showWaitDialog(1, false, "正在上传文件");

    PostFileRequest request = new PostFileRequest();
    getHttpDataLoader().doPostFileProcess(request, filePath, String.class);
  }

  public void sendPostVideoRequest(String filePath) {
    showWaitDialog(2, false, "正在上传文件");

    PostFileVideoRequest request = new PostFileVideoRequest();
    getHttpDataLoader().doPostFileProcess(request, filePath, String.class);
  }

  protected void setContentEditStatus(TextView textView, String content) {
    if (null == textView) {
      return;
    }

    if (!TextUtils.isEmpty(content)) {
      textView.setText("已编辑");
      textView.setTextColor(getResources().getColor(R.color.black));
    } else {
      textView.setText("未编辑");
      textView.setTextColor(getResources().getColor(R.color.gray));
    }
  }

  @Override
  public void onActivityResultCallBack(int resultCode, Intent intent) {
    if (resultCode == EditActivity.CODE_EDIT_COMPLETE) {
      if (!TextUtils.isEmpty(intent.getStringExtra("content"))) {
        if (isEditAddress) {
          mstrAddress = intent.getStringExtra("content");
          setContentEditStatus(mTvewAddress, mstrAddress);
        } else {
          mstrDesc = intent.getStringExtra("content");
          setContentEditStatus(getDescriptionTextView(), mstrDesc);
        }
      }
    } else if (resultCode == ProviderResultActivity.CODE_CITY_SELECT) {
      mAddress =
          JsonSerializerFactory.Create().decode(
              intent.getStringExtra("address"),
              Address.Data.class);
      if (null != mAddress) {
        String strAddress =
            mAddress.province + mAddress.city + mAddress.area;
        mTvewShipAddress.setText(strAddress);
      }
    } else if (resultCode == ProviderResultActivity.CODE_EDIT_SPECIAL_PANIC) {
      if (null != mProduct) {
        mProduct.in_special_panic = "1";
      }
    } else if (resultCode == ProviderResultActivity.CODE_EDIT_SPECIAL_GIFT) {
      if (null != mProduct) {
        mProduct.in_special_gift = "1";
      }
    }
  }

  protected void showTimePopupWindow(final TextView textView) {
    mTimePopupWindow = new TimePopupWindow(this);
    mTimePopupWindow.setAnimationStyle(R.style.Theme_Bill_Popupwindow);
    mTimePopupWindow.setYearsLength(TimeUtil.getLocalTime().getYear(),
        TimeUtil.getLocalTime().getYear() + 50);
    mTimePopupWindow.setOnTimeEnsureListener(new OnTimeEnsureListener() {

      @Override
      public void onTimePopDismiss(String date) {
        textView.setText(date);
      }
    });

    ClientInfo.closeSoftInput(textView, this);
    mTimePopupWindow.setCurrentDate(textView.getText().toString());
    mTimePopupWindow.showWindowBottom(textView);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == Activity.RESULT_OK) {
      if (null == mPictureSelectUtil) {
        return;
      }

      if (requestCode == PictureSelectUtil.REQ_CODE_RECORD) {
        mstrFilePath =
            mPictureSelectUtil.getVideo(requestCode, resultCode,
                data);
        if (!StringUtil.isEmptyString(mstrFilePath)) {
          Bitmap bitmap =
              ThumbnailUtils.createVideoThumbnail(mstrFilePath,
                  Thumbnails.MICRO_KIND);
          bitmap = BitmapUtil.rotateImage(bitmap, 90);
          mImgvewVideo.setImageBitmap(bitmap);
          mRlayoutVideo.setVisibility(View.VISIBLE);

          long token = System.currentTimeMillis();

          if (null != bitmap) {
            mstrThumbnailPath =
                BitmapUtil.saveBitmap(this, bitmap, token + ".jpg",
                    false);
            sendPostVideoRequest(mstrFilePath);
          }
        }
      } else {
        mstrFilePath =
            mPictureSelectUtil.getPicture(requestCode, resultCode,
                data);
        if (!StringUtil.isEmptyString(mstrFilePath)) {
          Bitmap bitmap = BitmapFactory.decodeFile(mstrFilePath);
          if (null != bitmap) {
            sendPostRequest(mstrFilePath);
          }
          if (miImagePosition == 0) {
            mImgvewPhoto1.setImageBitmap(bitmap);
          } else if (miImagePosition == 1) {
            mImgvewPhoto2.setImageBitmap(bitmap);
          } else if (miImagePosition == 2) {
            mImgvewPhoto3.setImageBitmap(bitmap);
          }
        }
      }
    } else if (resultCode == Activity.RESULT_CANCELED) {
      if (null != mPictureSelectUtil) {
        mPictureSelectUtil.deleteTempFile();
      }
    }
  }

  public abstract TextView getDescriptionTextView();

  public abstract TextView getCategoryEditvew();

  public abstract String getCategoryType();

  public abstract ProgressBar getCategoryProgressBar();
}