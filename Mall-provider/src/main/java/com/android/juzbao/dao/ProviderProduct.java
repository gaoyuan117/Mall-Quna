
package com.android.juzbao.dao;

import com.server.api.model.ProductCategory;
import com.server.api.service.ProductService;
import com.android.juzbao.constant.ProviderGlobalConst;
import com.android.zcomponent.http.HttpDataLoader;

public class ProviderProduct {

    public static void sendCmdQueryCategory(HttpDataLoader httpDataLoader, String gid) {
        ProductService.CategoryTreeRequest request =
                new ProductService.CategoryTreeRequest();
        request.Gid = gid;
        httpDataLoader.doPostProcess(request, ProductCategory.class);
    }

    public static void sendCmdQueryThirdCategory(HttpDataLoader httpDataLoader, String gid) {
        ProductService.ThirdCategoryTreeRequest request =
                new ProductService.ThirdCategoryTreeRequest();
        request.Gid = gid;
        httpDataLoader.doPostProcess(request, ProductCategory.class);
    }

    public static void sendCmdQueryGiftCategory(HttpDataLoader httpDataLoader) {
        ProductService.GiftCagetoryRequest request =
                new ProductService.GiftCagetoryRequest();
        httpDataLoader.doPostProcess(request,
                com.server.api.model.GiftCategory.class);
    }

    public static void sendCmdQueryProduct(HttpDataLoader httpDataLoader, long id) {
        ProductService.ProductRequest request =
                new ProductService.ProductRequest();
        request.Id = id;
        httpDataLoader.doPostProcess(request,
                com.server.api.model.Product.class);
    }

    public static void sendCmdQueryMyProduct(HttpDataLoader httpDataLoader, long id) {
        ProductService.MyProductRequest request =
                new ProductService.MyProductRequest();
        request.Id = id;
        httpDataLoader.doPostProcess(request,
                com.server.api.model.Product.class);
    }

    public static void sendCmdQueryMyProduct(HttpDataLoader httpDataLoader, String type, int
            page, int status) {
        ProductService.MyProductsRequest request =
                new ProductService.MyProductsRequest();
        request.Page = page;
        request.Pagesize = ProviderGlobalConst.INT_PAGE_NUMBER;
        request.Type = type;
        request.Status = status;
        httpDataLoader.doPostProcess(request,
                com.server.api.model.Products.class);
    }

    public static void sendCmdQueryProductGift(HttpDataLoader httpDataLoader, long productId) {
        ProductService.ProductGiftRequest request =
                new ProductService.ProductGiftRequest();
        request.ProductId = productId;
        httpDataLoader.doPostProcess(request,
                com.server.api.model.GiftProduct.class);
    }

    public static void sendCmdQueryPaincTime(HttpDataLoader httpDataLoader) {
        ProductService.PanicTimesRequest request =
                new ProductService.PanicTimesRequest();
        request.Type = "currday";
        httpDataLoader.doPostProcess(request,
                com.server.api.model.PaincTimes.class);
    }

    public static void sendCmdQueryPaincAfterTime(HttpDataLoader httpDataLoader) {
        ProductService.PanicTimesRequest request =
                new ProductService.PanicTimesRequest();
        request.Type = "all";
        httpDataLoader.doPostProcess(request,
                com.server.api.model.PaincTimes.class);
    }

    public static void sendCmdHaltProduct(HttpDataLoader httpDataLoader,
                                          String id) {
        ProductService.HaltProductRequest request =
                new ProductService.HaltProductRequest();
        request.Id = id;
        httpDataLoader.doPostProcess(request,
                com.server.api.model.CommonReturn.class);
    }

    public static void sendCmdPutawayProduct(HttpDataLoader httpDataLoader,
                                             String id) {
        ProductService.PutawayProductRequest request =
                new ProductService.PutawayProductRequest();
        request.Id = id;
        httpDataLoader.doPostProcess(request,
                com.server.api.model.CommonReturn.class);
    }

    public static void sendCmdDelProduct(HttpDataLoader httpDataLoader,
                                         String id) {
        ProductService.DelProductRequest request =
                new ProductService.DelProductRequest();
        request.Id = id;
        httpDataLoader.doPostProcess(request,
                com.server.api.model.jifenmodel.JifenCommonReturn.class);
    }

    public static void sendCmdQueryAddProductGift(
            HttpDataLoader httpDataLoader,
            ProductService.AddProductGiftRequest request) {
        httpDataLoader.doPostProcess(request,
                com.server.api.model.CommonReturn.class);
    }

    public static void sendCmdQueryAddProductPanic(
            HttpDataLoader httpDataLoader,
            ProductService.AddProductPanicRequest request) {
        httpDataLoader.doPostProcess(request,
                com.server.api.model.CommonReturn.class);
    }

    public static void sendCmdQueryAddPutongProduct(
            HttpDataLoader httpDataLoader,
            ProductService.ProductAddPutongRequest request) {
        httpDataLoader.doPostProcess(request,
                com.server.api.model.AddProduct.class);
    }

    public static void sendCmdQueryAddPaipingProduct(
            HttpDataLoader httpDataLoader,
            ProductService.ProductAddAuctionRequest request) {
        httpDataLoader.doPostProcess(request, com.server.api.model.AddProduct.class);
    }

    public static void sendCmdQueryAddDingzhiProduct(
            HttpDataLoader httpDataLoader,
            ProductService.ProductAddDingzhiRequest request) {
        httpDataLoader.doPostProcess(request,
                com.server.api.model.AddProduct.class);
    }

    public static void sendCmdQueryEditPutongProduct(
            HttpDataLoader httpDataLoader,
            ProductService.ProductEditPutongRequest request) {
        httpDataLoader.doPostProcess(request,
                com.server.api.model.CommonReturn.class);
    }

    public static void sendCmdQueryEditPaipingProduct(
            HttpDataLoader httpDataLoader,
            ProductService.ProductEditAuctionRequest request) {
        httpDataLoader.doPostProcess(request,
                com.server.api.model.CommonReturn.class);
    }
}
