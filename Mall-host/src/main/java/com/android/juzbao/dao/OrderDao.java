
package com.android.juzbao.dao;

import com.android.juzbao.constant.GlobalConst;
import com.android.zcomponent.http.HttpDataLoader;
import com.server.api.model.AddOrderResult;
import com.server.api.model.CommonReturn;
import com.server.api.model.OrderDetail;
import com.server.api.model.OrderPageResult;
import com.server.api.service.OrderService;

import java.math.BigDecimal;

public class OrderDao {

  public static void sendCmdQueryAddOrder(HttpDataLoader httpDataLoader,
                                          OrderService.SubmitOrderRequest request) {
    httpDataLoader.doPostProcess(request, AddOrderResult.class);
  }

  public static void sendCmdQueryQueryOrder(HttpDataLoader httpDataLoader,
                                            int page, String status) {
    OrderService.QueryOrderRequest request =
        new OrderService.QueryOrderRequest();
    request.status = status;
    request.page = page;
    request.page_size = GlobalConst.INT_NUM_PAGE;
    httpDataLoader.doPostProcess(request, OrderPageResult.class);
  }

  public static void sendCmdQueryQueryAllOrder(HttpDataLoader httpDataLoader,
                                               int page) {
    OrderService.QueryAllOrderRequest request = new OrderService.QueryAllOrderRequest();
    request.page = page;
    request.page_size = GlobalConst.INT_NUM_PAGE;
    httpDataLoader.doPostProcess(request, OrderPageResult.class);
  }


  public static void sendCmdQueryOrderDetail(HttpDataLoader httpDataLoader,
                                             String orderId) {
    OrderService.OrderDetailRequest request = new OrderService.OrderDetailRequest();
    request.id = orderId;
    httpDataLoader.doPostProcess(request, OrderDetail.class);
  }

  public static void sendCmdOrderComplete(HttpDataLoader httpDataLoader,
                                          String orderId) {
    OrderService.ReceiveOrderRequest request =
        new OrderService.ReceiveOrderRequest();
    request.OrderId = orderId;
    httpDataLoader.doPostProcess(request,
        CommonReturn.class);
  }

  public static void sendCmdOrderCancel(HttpDataLoader httpDataLoader,
                                        String orderId) {
    OrderService.CancelOrderRequest request = new OrderService.CancelOrderRequest();
    request.id = orderId;
    httpDataLoader.doPostProcess(request, CommonReturn.class);
  }

  public static void sendCmdOrderRefund(HttpDataLoader httpDataLoader,
                                        String type, String orderId, String productId, BigDecimal
                                            money,
                                        String reason, String explain, String[] coverIds) {
    OrderService.ReturnOrderRequest request =
        new OrderService.ReturnOrderRequest();
    request.OrderId = orderId;
    request.ProductId = productId;
    request.Money = money;
    request.Explain = explain;
    request.CoverIds = coverIds;
    request.Type = type;
    request.Reason = reason;
    httpDataLoader.doPostProcess(request, CommonReturn.class);
  }

  public static void sendCmdConfirmOrder(HttpDataLoader httpDataLoader, String orderId) {
    OrderService.ConfirmOrderRequest request = new OrderService.ConfirmOrderRequest();
    request.OrderId = orderId;
    httpDataLoader.doPostProcess(request, CommonReturn.class);
  }

  public static void sendCmdOrderDel(HttpDataLoader httpDataLoader, String order_id) {
    OrderService.DelOrderRequest request = new OrderService.DelOrderRequest();
    request.id = order_id;
    httpDataLoader.doPostProcess(request, CommonReturn.class);
  }
}
