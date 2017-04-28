
package com.android.juzbao.model;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.android.juzbao.util.CommonValidate;
import com.android.zcomponent.common.uiframe.BaseActivity;
import com.android.zcomponent.http.HttpDataLoader;
import com.android.zcomponent.http.api.model.MessageData;
import com.android.zcomponent.json.JsonSerializerFactory;
import com.android.zcomponent.util.PictureSelectUtil;
import com.android.zcomponent.util.ShowMsg;
import com.android.zcomponent.util.StringUtil;
import com.server.api.model.UploadFile;
import com.server.api.model.UploadFile.FileInfo;
import com.server.api.service.FileService.PostFileRequest;

import java.util.ArrayList;
import java.util.List;

public class ProviderFileBusiness {

  private Context mContext;

  private PictureSelectUtil mPictureSelectUtil;

  private List<FileInfo> mImageFiles = new ArrayList<>();

  private String mstrFilePath;

  private HttpDataLoader mHttpDataLoader;

  public ProviderFileBusiness(Context context, HttpDataLoader httpDataLoader) {
    mContext = context;
    mHttpDataLoader = httpDataLoader;
    mPictureSelectUtil = new PictureSelectUtil(context);
    dismissRecordBtn();
  }

  public void clear() {
    mImageFiles.clear();
    mstrFilePath = null;
  }

  public void dismissRecordBtn() {
    mPictureSelectUtil.dismissRecordBtn();
  }

  public void showRecordBtn() {
    mPictureSelectUtil.showRecordBtn();
  }

  public void setOutParams(int aspectX, int aspectY, int outputX, int outputY) {
    mPictureSelectUtil.setOutParams(aspectX, aspectY, outputX, outputY);
  }

  public void selectPicture() {
    mPictureSelectUtil.selectPicture();
  }

  private OnUploadSuccessListener mOnUploadSuccessListener;

  public void setOnUploadSuccessListener(
      OnUploadSuccessListener successListener) {
    this.mOnUploadSuccessListener = successListener;
  }

  public interface OnUploadSuccessListener {

    public void onUploadSuccess(String id);
  }

  public void onRecvMsg(MessageData msg) {
    if (msg.valiateReq(PostFileRequest.class)) {
      String reaponse = null;
      if (null != msg.getContext().data()) {
        reaponse = new String(msg.getContext().data());
      }

      UploadFile uploadFile = null;
      if (!TextUtils.isEmpty(reaponse)) {
        uploadFile =
            JsonSerializerFactory.Create().decode(reaponse,
                UploadFile.class);
      }
      if (CommonValidate.validateQueryState(mContext, msg, uploadFile,
          "文件上传失败")) {
        ShowMsg.showToast(mContext, "文件上传成功");
        mImageFiles.add(uploadFile.Data.file);

        if (null != mOnUploadSuccessListener) {
          mOnUploadSuccessListener
              .onUploadSuccess(uploadFile.Data.file.id);
        }
      }
      mPictureSelectUtil.deleteTempFile();
    }
  }

  public List<FileInfo> getImageFiles() {
    return mImageFiles;
  }

  public String[] getImageFileIds() {
    String[] fileIds = null;
    if (null != mImageFiles && mImageFiles.size() > 0) {
      fileIds = new String[mImageFiles.size()];
      for (int i = 0; i < fileIds.length; i++) {
        fileIds[i] = mImageFiles.get(i).id;
      }
    }
    return fileIds;
  }

  public void sendPostRequest(Context context, HttpDataLoader httpDataLoader,
                              String filePath) {
    ((BaseActivity) context).showWaitDialog(1, false, "正在上传文件");
    PostFileRequest request = new PostFileRequest();
    httpDataLoader.doPostFileProcess(request, filePath, String.class);
  }

  public Bitmap onActivityResult(int requestCode, int resultCode, Intent data) {
    if (resultCode == Activity.RESULT_OK) {
      if (null == mPictureSelectUtil) {
        return null;
      }

      mstrFilePath =
          mPictureSelectUtil
              .getPicture(requestCode, resultCode, data);
      if (!StringUtil.isEmptyString(mstrFilePath)) {
        Bitmap bitmap = BitmapFactory.decodeFile(mstrFilePath);
        sendPostRequest(mContext, mHttpDataLoader, mstrFilePath);

        return bitmap;
      }
    } else if (resultCode == Activity.RESULT_CANCELED) {
      if (null != mPictureSelectUtil) {
        mPictureSelectUtil.deleteTempFile();
      }
    }

    return null;
  }
}
