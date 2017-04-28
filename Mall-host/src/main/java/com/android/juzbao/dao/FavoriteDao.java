
package com.android.juzbao.dao;

import com.server.api.model.CommonReturn;
import com.server.api.model.IsFavorited;
import com.server.api.model.ProductFavoritePageResult;
import com.server.api.model.ShopFavoritePageResult;
import com.server.api.service.FavoriteService;
import com.android.juzbao.constant.GlobalConst;
import com.android.zcomponent.http.HttpDataLoader;

public class FavoriteDao {

    public static void sendCmdQueryAddProductFavorite(
            HttpDataLoader httpDataLoader, int productId) {
        FavoriteService.AddProductFavoriteRequest request =
                new FavoriteService.AddProductFavoriteRequest();
        request.ProductId = productId;

        httpDataLoader.doPostProcess(request,
                CommonReturn.class);
    }

    public static void sendCmdQueryDelProductFavorite(
            HttpDataLoader httpDataLoader, int favoriteId) {
        FavoriteService.DelProductFavoriteRequest request =
                new FavoriteService.DelProductFavoriteRequest();
        request.Id = favoriteId;
        httpDataLoader.doPostProcess(request,
                CommonReturn.class);
    }

    public static void sendCmdQueryIsProductFavorite(
            HttpDataLoader httpDataLoader, int productId) {
        FavoriteService.IsProductFavoritedRequest request =
                new FavoriteService.IsProductFavoritedRequest();
        request.ProductId = productId;

        httpDataLoader.doPostProcess(request, IsFavorited.class, false);
    }

    public static void sendCmdQueryProductFavorites(
            HttpDataLoader httpDataLoader, int page) {
        FavoriteService.FavoriteProductsRequest request =
                new FavoriteService.FavoriteProductsRequest();
        request.Page = page;
        request.Pagesize = GlobalConst.INT_NUM_PAGE;
        httpDataLoader.doPostProcess(request, ProductFavoritePageResult.class);
    }

    public static void sendCmdQueryAddShopFavorite(
            HttpDataLoader httpDataLoader, int shopId) {
        FavoriteService.AddShopFavoriteRequest request =
                new FavoriteService.AddShopFavoriteRequest();
        request.ShopId = shopId;

        httpDataLoader.doPostProcess(request, CommonReturn.class);
    }

    public static void sendCmdQueryDelShopFavorite(
            HttpDataLoader httpDataLoader, int favoriteId) {
        FavoriteService.DelShopFavoriteRequest request =
                new FavoriteService.DelShopFavoriteRequest();
        request.Id = favoriteId;
        httpDataLoader.doPostProcess(request,
                CommonReturn.class);
    }

    public static void sendCmdQueryIsShopFavorite(
            HttpDataLoader httpDataLoader, int shopId) {
        FavoriteService.IsShopFavoriteRequest request =
                new FavoriteService.IsShopFavoriteRequest();
        request.ShopId = shopId;

        httpDataLoader.doPostProcess(request, IsFavorited.class, false);
    }

    public static void sendCmdQueryShopFavorites(
            HttpDataLoader httpDataLoader, int page) {
        FavoriteService.FavoriteShopsRequest request =
                new FavoriteService.FavoriteShopsRequest();
        request.Page = page;
        request.Pagesize = GlobalConst.INT_NUM_PAGE;
        httpDataLoader.doPostProcess(request, ShopFavoritePageResult.class);
    }

    public static void sendCmdQueryDelProductBrowse(
            HttpDataLoader httpDataLoader, int favoriteId) {
        FavoriteService.DelProductBrowseRequest request =
                new FavoriteService.DelProductBrowseRequest();
        request.Id = favoriteId;
        httpDataLoader.doPostProcess(request, CommonReturn.class);
    }

    public static void sendCmdQueryProductBrowse(
            HttpDataLoader httpDataLoader, int page) {
        FavoriteService.BrowseProductsRequest request =
                new FavoriteService.BrowseProductsRequest();
        request.Page = page;
        request.Pagesize = GlobalConst.INT_NUM_PAGE;
        httpDataLoader.doPostProcess(request, ProductFavoritePageResult.class);
    }
}
