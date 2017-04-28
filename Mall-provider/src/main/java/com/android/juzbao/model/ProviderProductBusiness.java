
package com.android.juzbao.model;

import android.content.Context;
import android.text.TextUtils;

import com.android.juzbao.dao.ProviderProduct;
import com.android.juzbao.util.CommonValidate;
import com.android.zcomponent.http.HttpDataLoader;
import com.android.zcomponent.http.api.host.Endpoint;
import com.android.zcomponent.http.api.model.MessageData;
import com.android.zcomponent.http.constant.ErrorCode;
import com.android.zcomponent.json.JsonSerializerFactory;
import com.android.zcomponent.util.ShowMsg;
import com.android.zcomponent.util.TimeUtil;
import com.android.zcomponent.util.ValidateUtil;
import com.android.zcomponent.views.DataEmptyView;
import com.server.api.model.PaincTimes;
import com.server.api.model.PaincTimes.Data;
import com.server.api.model.UploadFile;
import com.server.api.service.ProductService;
import com.server.api.service.ProductService.AddProductGiftRequest;
import com.server.api.service.ProductService.AddProductPanicRequest;
import com.server.api.service.ProductService.ProductAddAuctionRequest;
import com.server.api.service.ProductService.ProductAddDingzhiRequest;
import com.server.api.service.ProductService.ProductAddPutongRequest;
import com.server.api.service.ProductService.ProductEditAuctionRequest;
import com.server.api.service.ProductService.ProductEditPutongRequest;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class ProviderProductBusiness {

  public static void queryCategory(HttpDataLoader httpDataLoader, String gid) {
    ProviderProduct.sendCmdQueryCategory(httpDataLoader, gid);
  }

  public static void queryThirdCategory(HttpDataLoader httpDataLoader, String gid) {
    ProviderProduct.sendCmdQueryThirdCategory(httpDataLoader, gid);
  }

  public static void queryGroup(HttpDataLoader httpDataLoader, String group) {
    ProductService.GroupRequest request =
        new ProductService.GroupRequest();
    request.Group = group;
    httpDataLoader.doPostProcess(request,
        com.server.api.model.ProductCategory.class);
  }

  public static void queryGiftCategory(HttpDataLoader httpDataLoader) {
    ProviderProduct.sendCmdQueryGiftCategory(httpDataLoader);
  }

  public static void queryGiftProduct(HttpDataLoader httpDataLoader,
                                      long productId) {
    ProviderProduct.sendCmdQueryProductGift(httpDataLoader, productId);
  }

  public static void queryProductDetail(HttpDataLoader httpDataLoader, long id) {
    ProviderProduct.sendCmdQueryProduct(httpDataLoader, id);
  }

  public static void queryMyProductDetail(HttpDataLoader httpDataLoader,
                                          long id) {
    ProviderProduct.sendCmdQueryMyProduct(httpDataLoader, id);
  }

  public static void queryMyProducts(HttpDataLoader httpDataLoader,
                                     String type, int page, int status) {
    ProviderProduct
        .sendCmdQueryMyProduct(httpDataLoader, type, page, status);
  }

  public static void queryEditOption(HttpDataLoader httpDataLoader,
                                     String id, String productId, String text) {
    ProductService.EditOptionRequest request = new ProductService.EditOptionRequest();
    request.Id = id;
    request.PId = "";
    request.ProductId = productId;
    request.Text = text;
    httpDataLoader.doPostProcess(request,
        com.server.api.model.CommonReturn.class);
  }

  public static void queryEditChildOption(HttpDataLoader httpDataLoader,
                                          String id, String pid, String productId, String text) {
    ProductService.EditChildOptionRequest request = new ProductService.EditChildOptionRequest();
    request.Id = id;
    request.PId = pid;
    request.ProductId = productId;
    request.Text = text;
    httpDataLoader.doPostProcess(request,
        com.server.api.model.CommonReturn.class);
  }

  public static void queryDelChildOption(HttpDataLoader httpDataLoader,
                                         String id) {
    ProductService.DelOptionRequest request = new ProductService.DelOptionRequest();
    request.Id = id;
    httpDataLoader.doPostProcess(request,
        com.server.api.model.CommonReturn.class);
  }

  public static void queryAddOptionPrice(HttpDataLoader httpDataLoader,
                                         String ids, String productId, BigDecimal price) {
    ProductService.AddOptionPriceRequest request = new ProductService.AddOptionPriceRequest();
    request.Ids = ids;
    request.ProductId = productId;
    request.Price = price;
    httpDataLoader.doPostProcess(request,
        com.server.api.model.CommonReturn.class);
  }

  public static void queryDelOptionPrice(HttpDataLoader httpDataLoader,
                                         String id) {
    ProductService.DelOptionPriceRequest request = new ProductService.DelOptionPriceRequest();
    request.Id = id;
    httpDataLoader.doPostProcess(request,
        com.server.api.model.CommonReturn.class);
  }

  public static boolean addProductGift(Context context,
                                       HttpDataLoader httpDataLoader, AddProductGiftRequest
                                           request) {
    if (null == request.SpecialGiftId || request.SpecialGiftId.length <= 0) {
      ShowMsg.showToast(context, "请选择选礼物分类");
      return false;
    }

    ProviderProduct.sendCmdQueryAddProductGift(httpDataLoader, request);
    return true;
  }

  public static void queryPaincTime(HttpDataLoader httpDataLoader) {
    ProviderProduct.sendCmdQueryPaincTime(httpDataLoader);
  }

  public static void queryPaincAfterTime(HttpDataLoader httpDataLoader) {
    ProviderProduct.sendCmdQueryPaincAfterTime(httpDataLoader);
  }

  public static void queryPutawayProduct(HttpDataLoader httpDataLoader,
                                         String id) {
    ProviderProduct.sendCmdPutawayProduct(httpDataLoader, id);
  }

  public static void queryHaltProduct(HttpDataLoader httpDataLoader, String id) {
    ProviderProduct.sendCmdHaltProduct(httpDataLoader, id);
  }

  public static void queryDelProduct(HttpDataLoader httpDataLoader, String id) {
    ProviderProduct.sendCmdDelProduct(httpDataLoader, id);
  }

  public static boolean addProductPainc(Context context,
                                        HttpDataLoader httpDataLoader, AddProductPanicRequest
                                            request) {
    if (request.ConditionPrice.doubleValue() <= 0) {
      ShowMsg.showToast(context, "请输入满足价格");
      return false;
    }

    if (request.Discount < 0) {
      ShowMsg.showToast(context, "请选择折扣");
      return false;
    }

    if (request.SpecialPanicId <= 0) {
      ShowMsg.showToast(context, "请选择抢购时间");
      return false;
    }

    ProviderProduct.sendCmdQueryAddProductPanic(httpDataLoader, request);
    return true;
  }

  public static boolean addPutongProduct(Context context,
                                         HttpDataLoader httpDataLoader, ProductAddPutongRequest
                                             request) {
    if (null == request) {
      return false;
    }

    if (TextUtils.isEmpty(request.Title)) {
      ShowMsg.showToast(context, "请输入商品标题");
      return false;
    }

    if (request.CategoryId <= 0) {
      ShowMsg.showToast(context, "请选择商品分类");
      return false;
    }

    if (request.Price <= 0) {
      ShowMsg.showToast(context, "请输入价格");
      return false;
    }

    if (request.Quantity <= 0) {
      ShowMsg.showToast(context, "请输入库存数");
      return false;
    }

    if (request.Score < 0) {
      ShowMsg.showToast(context, "赠送积分不能为负");
      return false;
    }

    if (request.Score < 1) {
      ShowMsg.showToast(context, "发布商品必须赠送1个积分");
      return false;
    }

    if (request.Pics == null && request.Movie == 0) {
      ShowMsg.showToast(context, "必须上传图片或者视频");
      return false;
    }

    if (TextUtils.isEmpty(request.Description)) {
      ShowMsg.showToast(context, "请输入商品描述");
      return false;
    }

    if (TextUtils.isEmpty(request.ProvinceId)) {
      ShowMsg.showToast(context, "请选择发货地址");
      return false;
    }

    if (TextUtils.isEmpty(request.Address)) {
      ShowMsg.showToast(context, "请输入详细地址");
      return false;
    }

    if (request.SecurityDelivery <= 0) {
      ShowMsg.showToast(context, "请选择发货时间");
      return false;
    }

    ProviderProduct.sendCmdQueryAddPutongProduct(httpDataLoader, request);
    return true;
  }

  public static void queryProductOptions(HttpDataLoader httpDataLoader, String productId) {
    ProductService.ProductOptionRequest request = new ProductService.ProductOptionRequest();
    request.ProductId = productId;
    httpDataLoader.doPostProcess(request, com.server.api.model.ProductOptions.class);
  }

  public static boolean addPaipingProduct(Context context,
                                          HttpDataLoader httpDataLoader, ProductAddAuctionRequest
                                              request) {
    if (null == request) {
      return false;
    }

    if (TextUtils.isEmpty(request.Title)) {
      ShowMsg.showToast(context, "请输入商品标题");
      return false;
    }

    if (request.CategoryId <= 0) {
      ShowMsg.showToast(context, "请选择商品分类");
      return false;
    }

    if (request.Price <= 0) {
      ShowMsg.showToast(context, "请输入起拍价格");
      return false;
    }

    if (request.Maxprice <= 0) {
      ShowMsg.showToast(context, "请输入立即成交价格");
      return false;
    }

    if (request.Quantity <= 0) {
      ShowMsg.showToast(context, "请输入库存数");
      return false;
    }

    if (TextUtils.isEmpty(request.Description)) {
      ShowMsg.showToast(context, "请输入商品描述");
      return false;
    }

    if (request.ProvinceId <= 0) {
      ShowMsg.showToast(context, "请发货地区");
      return false;
    }

    if (TextUtils.isEmpty(request.Address)) {
      ShowMsg.showToast(context, "请输入详细地址");
      return false;
    }

    if (TextUtils.isEmpty(request.StartDate)) {
      ShowMsg.showToast(context, "请选择开始日期");
      return false;
    }

    if (request.StartTime < 0) {
      ShowMsg.showToast(context, "请选择开始时间");
      return false;
    }

    if (TextUtils.isEmpty(request.EndDate)) {
      ShowMsg.showToast(context, "请选择结束日期");
      return false;
    }

    if (request.EndTime < 0) {
      ShowMsg.showToast(context, "请选择结束时间");
      return false;
    }

    if (request.SecurityDelivery <= 0) {
      ShowMsg.showToast(context, "请选择发货时间");
      return false;
    }

    ProviderProduct.sendCmdQueryAddPaipingProduct(httpDataLoader, request);
    return true;
  }

  public static boolean addDingzhiProduct(Context context,
                                          HttpDataLoader httpDataLoader, ProductAddDingzhiRequest
                                              request) {
    if (null == request) {
      return false;
    }

    if (TextUtils.isEmpty(request.Verify)) {
      ShowMsg.showToast(context, "请输入短信邀请码");
      return false;
    }

    if (TextUtils.isEmpty(request.Title)) {
      ShowMsg.showToast(context, "请输入商品标题");
      return false;
    }

    if (request.CategoryId <= 0) {
      ShowMsg.showToast(context, "请选择商品分类");
      return false;
    }

    if (TextUtils.isEmpty(request.Description)) {
      ShowMsg.showToast(context, "请输入材质需求");
      return false;
    }

    if (request.Price <= 0) {
      ShowMsg.showToast(context, "请输入价格");
      return false;
    }

    if (request.ProvinceId <= 0) {
      ShowMsg.showToast(context, "请发货地区");
      return false;
    }

    if (TextUtils.isEmpty(request.Address)) {
      ShowMsg.showToast(context, "请输入详细地址");
      return false;
    }

    if (TextUtils.isEmpty(request.EndTime)) {
      ShowMsg.showToast(context, "请选择交货期限");
      return false;
    }

    if (request.OverDay <= 0) {
      ShowMsg.showToast(context, "请输入延迟周期");
      return false;
    }

    if (request.OverPersent <= 0) {
      ShowMsg.showToast(context, "请输入补偿百分比");
      return false;
    }

    if (request.SecurityDelivery <= 0) {
      ShowMsg.showToast(context, "请选择发货时间");
      return false;
    }

    ProviderProduct.sendCmdQueryAddDingzhiProduct(httpDataLoader, request);
    return true;
  }

  public static boolean editPutongProduct(Context context,
                                          HttpDataLoader httpDataLoader, ProductEditPutongRequest
                                              request) {
    if (null == request) {
      return false;
    }



//        if (request.Pics == null || request.Movie == 0){
//            ShowMsg.showToast(context,"必须上传图片或者视频");
//        }

    if (TextUtils.isEmpty(request.Title)) {
      ShowMsg.showToast(context, "请输入商品标题");
      return false;
    }

    if (request.CategoryId <= 0) {
      ShowMsg.showToast(context, "请选择商品分类");
      return false;
    }

    if (request.Price <= 0) {
      ShowMsg.showToast(context, "请输入价格");
      return false;
    }

    if (request.Score < 0) {
      ShowMsg.showToast(context, "赠送积分不能为负");
      return false;
    }
    if (request.Score < 1) {
      ShowMsg.showToast(context, "发布商品必须赠送1个积分");
      return false;
    }

    if (request.Quantity <= 0) {
      ShowMsg.showToast(context, "请输入库存数");
      return false;
    }

    if (TextUtils.isEmpty(request.Description)) {
      ShowMsg.showToast(context, "请输入商品描述");
      return false;
    }

    if (TextUtils.isEmpty(request.ProvinceId)) {
      ShowMsg.showToast(context, "请选择发货地址");
      return false;
    }

    if (TextUtils.isEmpty(request.Address)) {
      ShowMsg.showToast(context, "请输入详细地址");
      return false;
    }

    if (request.SecurityDelivery <= 0) {
      ShowMsg.showToast(context, "请选择发货时间");
      return false;
    }

    ProviderProduct.sendCmdQueryEditPutongProduct(httpDataLoader, request);
    return true;
  }

  public static boolean editPaipingProduct(Context context,
                                           HttpDataLoader httpDataLoader,
                                           ProductEditAuctionRequest request) {
    if (null == request) {
      return false;
    }

    if (TextUtils.isEmpty(request.Title)) {
      ShowMsg.showToast(context, "请输入商品标题");
      return false;
    }

    if (request.CategoryId <= 0) {
      ShowMsg.showToast(context, "请选择商品分类");
      return false;
    }

    if (request.Price <= 0) {
      ShowMsg.showToast(context, "请输入起拍价格");
      return false;
    }

    if (request.Maxprice <= 0) {
      ShowMsg.showToast(context, "请输入立即成交价格");
      return false;
    }

    if (request.Quantity <= 0) {
      ShowMsg.showToast(context, "请输入库存数");
      return false;
    }

    if (TextUtils.isEmpty(request.Description)) {
      ShowMsg.showToast(context, "请输入商品描述");
      return false;
    }

    if (request.ProvinceId <= 0) {
      ShowMsg.showToast(context, "请发货地区");
      return false;
    }

    if (TextUtils.isEmpty(request.Address)) {
      ShowMsg.showToast(context, "请输入详细地址");
      return false;
    }

    if (TextUtils.isEmpty(request.StartDate)) {
      ShowMsg.showToast(context, "请选择开始日期");
      return false;
    }

    if (request.StartTime < 0) {
      ShowMsg.showToast(context, "请选择开始时间");
      return false;
    }

    if (TextUtils.isEmpty(request.EndDate)) {
      ShowMsg.showToast(context, "请选择结束日期");
      return false;
    }

    if (request.EndTime < 0) {
      ShowMsg.showToast(context, "请选择结束时间");
      return false;
    }

    if (request.SecurityDelivery <= 0) {
      ShowMsg.showToast(context, "请选择发货时间");
      return false;
    }

    ProviderProduct.sendCmdQueryEditPaipingProduct(httpDataLoader, request);
    return true;
  }

  public static String getHtmlData(String bodyHTML) {
    String head =
        "<head>"
            + "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, " +
            "user-scalable=no\"> "
            + "<style>img{max-width: 100%; width:auto; height:auto;}</style>"
            + "</head>";
    return "<html>" + head + "<body>" + bodyHTML + "</body></html>";
  }

  public static boolean validateImageUrl(String imageUrl) {
    if (!TextUtils.isEmpty(imageUrl) && !"false".equals(imageUrl)) {
      return true;
    }

    return false;
  }

  public static boolean validateQueryProducts(
      com.server.api.model.Products responseProduct) {
    if (null != responseProduct) {
      if (null != responseProduct.Data
          && null != responseProduct.Data.Results
          && responseProduct.Data.Results.length > 0) {
        return true;
      } else {
        return false;
      }
    } else {
      return false;
    }
  }

  public static String validateQueryState(
      com.server.api.model.Products responseProduct,
      String faileMessage) {
    if (null != responseProduct) {
      if (responseProduct.code != ErrorCode.INT_QUERY_DATA_SUCCESS) {
        return responseProduct.message;
      } else {
        return faileMessage;
      }
    } else {
      return faileMessage;
    }
  }

  public static boolean validateQueryTimes(PaincTimes response,
                                           DataEmptyView dataEmptyView, MessageData msg, String
                                               emptyMessage) {
    if (null != response) {
      if (response.code == ErrorCode.INT_QUERY_DATA_SUCCESS) {
        if (!ValidateUtil.isArrayEmpty(response.Data)) {
          return true;
        } else {
          dataEmptyView.showViewDataEmpty(false, false, msg,
              emptyMessage);
          return false;
        }
      } else {
        dataEmptyView.showViewDataEmpty(false, false, msg,
            response.message);
        return false;
      }
    } else {
      dataEmptyView.showViewDataEmpty(false, false, msg, emptyMessage);
      return false;
    }
  }

  public static void formatPaincingTime(List<PaincTimes.Data> listPaincTimes)
      throws Exception {
    if (null == listPaincTimes) {
      return;
    }
    String currentDate =
        TimeUtil.transformTimeFormat(Endpoint.serverDate(),
            TimeUtil.STR_FORMAT_DATE);

    for (int i = 0; i < listPaincTimes.size(); i++) {
      PaincTimes.Data paincTime = listPaincTimes.get(i);
      long startTime = Long.parseLong(paincTime.start_time) * 1000;
      long endTime = Long.parseLong(paincTime.end_time) * 1000;

      String strStartTime =
          TimeUtil.transformLongTimeFormat(startTime,
              TimeUtil.STR_FORMAT_HOUR_MINUTE);
      String strEndTime =
          TimeUtil.transformLongTimeFormat(endTime,
              TimeUtil.STR_FORMAT_HOUR_MINUTE);

      Date newStartDate =
          TimeUtil.getDateByStrDate(currentDate + " " + strStartTime,
              TimeUtil.STR_FORMAT_DATE_TIME2);
      Date newEndDate =
          TimeUtil.getDateByStrDate(currentDate + " " + strEndTime,
              TimeUtil.STR_FORMAT_DATE_TIME2);

      paincTime.start_time = "" + newStartDate.getTime() / 1000;
      paincTime.end_time = "" + newEndDate.getTime() / 1000;

      listPaincTimes.set(i, paincTime);
    }

    Collections.sort(listPaincTimes, new PaincTimeComparator());
  }

  private static class PaincTimeComparator implements
      Comparator<PaincTimes.Data> {

    @Override
    public int compare(Data lhs, Data rhs) {
      Long time1 = Long.parseLong(lhs.start_time);
      Long time2 = Long.parseLong(rhs.start_time);

      return time1.compareTo(time2);
    }
  }

  public static UploadFile parseUploadFile(Context context, MessageData msg) {
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

    if (CommonValidate.validateQueryState(context, msg, uploadFile, "文件上传失败")) {
      ShowMsg.showToast(context, "文件上传成功");
    }
    return uploadFile;
  }
}
