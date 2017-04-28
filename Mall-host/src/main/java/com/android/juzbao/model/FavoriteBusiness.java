
package com.android.juzbao.model;

import android.content.Context;
import android.widget.TextView;

import com.android.juzbao.activity.ProductDetailActivity;
import com.android.juzbao.activity.ShopDetailActivity;
import com.server.api.model.CommonReturn;
import com.server.api.model.IsFavorited;
import com.android.juzbao.application.BaseApplication;
import com.android.juzbao.constant.ResultActivity;
import com.android.juzbao.dao.FavoriteDao;
import com.android.zcomponent.http.HttpDataLoader;
import com.android.zcomponent.http.api.model.MessageData;
import com.android.zcomponent.http.constant.ErrorCode;
import com.android.zcomponent.json.JsonSerializerFactory;
import com.android.zcomponent.util.CommonUtil;
import com.android.zcomponent.util.ShowMsg;
import com.server.api.service.FavoriteService;

public class FavoriteBusiness {

    public static void addProductFavorite(HttpDataLoader httpDataLoader,
                                          int productId) {
        FavoriteDao.sendCmdQueryAddProductFavorite(httpDataLoader, productId);
    }

    public static void delProductFavorite(HttpDataLoader httpDataLoader,
                                          int favoriteId) {
        FavoriteDao.sendCmdQueryDelProductFavorite(httpDataLoader, favoriteId);
    }

    public static void isProductFavorite(HttpDataLoader httpDataLoader,
                                         int productId) {
        FavoriteDao.sendCmdQueryIsProductFavorite(httpDataLoader, productId);
    }

    public static void queryProductFavorite(HttpDataLoader httpDataLoader,
                                            int page) {
        FavoriteDao.sendCmdQueryProductFavorites(httpDataLoader, page);
    }

    public static void addShopFavorite(HttpDataLoader httpDataLoader,
                                       int shopId) {
        FavoriteDao.sendCmdQueryAddShopFavorite(httpDataLoader, shopId);
    }

    public static void delShopFavorite(HttpDataLoader httpDataLoader,
                                       int favoriteId) {
        FavoriteDao.sendCmdQueryDelShopFavorite(httpDataLoader, favoriteId);
    }

    public static void isShopFavorite(HttpDataLoader httpDataLoader,
                                      int shopId) {
        FavoriteDao.sendCmdQueryIsShopFavorite(httpDataLoader, shopId);
    }

    public static void queryShopFavorite(HttpDataLoader httpDataLoader,
                                         int page) {
        FavoriteDao.sendCmdQueryShopFavorites(httpDataLoader, page);
    }

    public static void delProductBrowse(HttpDataLoader httpDataLoader,
                                        int favoriteId) {
        FavoriteDao.sendCmdQueryDelProductBrowse(httpDataLoader, favoriteId);
    }

    public static void queryProductBrowse(HttpDataLoader httpDataLoader,
                                          int page) {
        FavoriteDao.sendCmdQueryProductBrowse(httpDataLoader, page);
    }

    public static class FavoriteHelper {

        private Context mContext;

        private HttpDataLoader mDataLoader;

        private TextView tvewFavorite;

        private int resIdUnFavorite;

        private int resIdFavorited;

        private int productId;

        private int shopId;

        private boolean isQueryFavoriteState = true;

        public FavoriteHelper(Context context, HttpDataLoader dataLoader) {
            mContext = context;
            mDataLoader = dataLoader;
        }

        public void favoriteStateView(TextView tvewFavorite,
                                      int resIdUnFavorite, int resIdFavorited) {
            this.tvewFavorite = tvewFavorite;
            this.resIdUnFavorite = resIdUnFavorite;
            this.resIdFavorited = resIdFavorited;
        }

        public void queryIsProductFavorited(boolean isQueryFavoriteState, int productId) {
            if (BaseApplication.isLogin()) {
                this.isQueryFavoriteState = isQueryFavoriteState;
                FavoriteBusiness.isProductFavorite(mDataLoader, productId);
            }
        }

        public void queryIsShopFavorited(boolean isQueryFavoriteState, int shopId) {
            if (BaseApplication.isLogin()) {
                this.isQueryFavoriteState = isQueryFavoriteState;
                FavoriteBusiness.isShopFavorite(mDataLoader, shopId);
            }
        }

        public void setProductId(int productId) {
            this.productId = productId;
        }

        public void setShopId(int shopId) {
            this.shopId = shopId;
        }

        public void onRecvMsg(MessageData msg) {
            if (msg.valiateReq(FavoriteService.IsProductFavoritedRequest.class)) {
                CommonReturn response =
                        JsonSerializerFactory.Create().decode(
                                new String(msg.getContext().data()),
                                CommonReturn.class);

                if (null != response) {
                    if (response.code == ErrorCode.INT_QUERY_DATA_SUCCESS) {
                        if (isQueryFavoriteState) {
                            if (null != tvewFavorite) {
                                CommonUtil.setDrawableTop(mContext,
                                        tvewFavorite, resIdFavorited);
                            }
                        } else {

                            IsFavorited data =
                                    JsonSerializerFactory
                                            .Create()
                                            .decode(new String(msg.getContext()
                                                    .data()), IsFavorited.class);
                            delProductFavorite(mDataLoader,
                                    ProductDetailActivity.mProductId
                                    /*Integer.parseInt(data.data)*/);
                        }
                    } else {
                        if (isQueryFavoriteState) {
                            if (null != tvewFavorite) {
                                CommonUtil.setDrawableTop(mContext,
                                        tvewFavorite, resIdUnFavorite);
                            }
                        } else {
                            if (productId > 0) {
                                addProductFavorite(mDataLoader, productId);
                            }
                        }
                    }
                } else {
                    ShowMsg.showToast(mContext.getApplicationContext(), msg, "");
                }
            } else if (msg.valiateReq(FavoriteService.AddProductFavoriteRequest.class)) {
                CommonReturn response = (CommonReturn) msg.getRspObject();
                if (null != response) {
                    if (response.code == ErrorCode.INT_QUERY_DATA_SUCCESS) {
                        ShowMsg.showToast(mContext.getApplicationContext(),
                                "添加收藏成功");
                        CommonUtil.setDrawableTop(mContext, tvewFavorite,
                                resIdFavorited);
                        BaseApplication.getInstance().setActivityResult(
                                ResultActivity.CODE_EDIT_FAVORITE, null);
                    } else {
                        ShowMsg.showToast(mContext.getApplicationContext(),
                                response.message);
                    }
                } else {
                    ShowMsg.showToast(mContext.getApplicationContext(),
                            "添加收藏失败");
                }
            } else if (msg.valiateReq(FavoriteService.DelProductFavoriteRequest.class)) {
                CommonReturn response = msg.getRspObject();
                if (null != response) {
                    if (response.code == ErrorCode.INT_QUERY_DATA_SUCCESS) {
                        ShowMsg.showToast(mContext.getApplicationContext(),
                                "删除收藏成功");
                        CommonUtil.setDrawableTop(mContext, tvewFavorite,
                                resIdUnFavorite);
                        BaseApplication.getInstance().setActivityResult(
                                ResultActivity.CODE_EDIT_FAVORITE, null);
                    } else {
                        ShowMsg.showToast(mContext.getApplicationContext(),
                                response.message);
                    }
                } else {
                    ShowMsg.showToast(mContext.getApplicationContext(),
                            "删除收藏失败");
                }
            } else if (msg.valiateReq(FavoriteService.AddShopFavoriteRequest.class)) {
                CommonReturn response = msg.getRspObject();
                if (null != response) {
                    if (response.code == ErrorCode.INT_QUERY_DATA_SUCCESS) {
                        ShowMsg.showToast(mContext.getApplicationContext(),
                                "添加收藏成功");
                        tvewFavorite.setText("已收藏");
                        BaseApplication.getInstance().setActivityResult(
                                ResultActivity.CODE_EDIT_FAVORITE, null);
                    } else {
                        ShowMsg.showToast(mContext.getApplicationContext(),
                                response.message);
                    }
                } else {
                    ShowMsg.showToast(mContext.getApplicationContext(),
                            "添加收藏失败");
                }
            } else if (msg.valiateReq(FavoriteService.DelShopFavoriteRequest.class)) {
                CommonReturn response = (CommonReturn) msg.getRspObject();
                if (null != response) {
                    if (response.code == ErrorCode.INT_QUERY_DATA_SUCCESS) {
                        ShowMsg.showToast(mContext.getApplicationContext(),
                                "删除收藏成功");
                        tvewFavorite.setText("未收藏");
                        BaseApplication.getInstance().setActivityResult(
                                ResultActivity.CODE_EDIT_FAVORITE, null);
                    } else {
                        ShowMsg.showToast(mContext.getApplicationContext(),
                                response.message);
                    }
                } else {
                    ShowMsg.showToast(mContext.getApplicationContext(),
                            "删除收藏失败");
                }
            } else if (msg.valiateReq(FavoriteService.IsShopFavoriteRequest.class)) {
                CommonReturn response =
                        JsonSerializerFactory.Create().decode(
                                new String(msg.getContext().data()),
                                CommonReturn.class);

                if (null != response) {
                    if (response.code == ErrorCode.INT_QUERY_DATA_SUCCESS) {
                        if (isQueryFavoriteState) {
                            if (null != tvewFavorite) {
                                tvewFavorite.setText("已收藏");
                            }
                        } else {
                            IsFavorited data =
                                    JsonSerializerFactory
                                            .Create()
                                            .decode(new String(msg.getContext()
                                                    .data()), IsFavorited.class);
                            delShopFavorite(mDataLoader,
                                    ShopDetailActivity.mShopId
                                    /*Integer.parseInt(data.data)*/);
                        }
                    } else {
                        if (isQueryFavoriteState) {
                            if (null != tvewFavorite) {
                                tvewFavorite.setText("未收藏");
                            }
                        } else {
                            if (shopId > 0) {
                                addShopFavorite(mDataLoader, shopId);
                            }
                        }
                    }
                } else {
                    ShowMsg.showToast(mContext.getApplicationContext(), msg, "");
                }
            }
        }
    }
}
