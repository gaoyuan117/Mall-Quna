
package com.android.juzbao.model;

import com.android.juzbao.dao.CartDao;
import com.android.zcomponent.http.HttpDataLoader;
import com.android.zcomponent.util.ListUtil;
import com.server.api.model.CartItem;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CartBusiness {

    public static void queryCarts(HttpDataLoader httpDataLoader, int page) {
        CartDao.sendCmdQueryCarts(httpDataLoader, page);
    }

    public static void addToCart(HttpDataLoader httpDataLoader,
                                 BigDecimal price, int productId, int quantity, String options) {
        CartDao.sendCmdQueryAddToCart(httpDataLoader, price, productId, quantity, options);
    }

    public static void delToCart(HttpDataLoader httpDataLoader, int cartId) {
        CartDao.sendCmdDelCartById(httpDataLoader, cartId);
    }

    public static void editCart(HttpDataLoader httpDataLoader,
                                BigDecimal price, int cartId, int quantity, String options) {
        CartDao.sendCmdQueryEditCart(httpDataLoader, price, cartId, quantity,
                options);
    }

    public static void toTrade(HttpDataLoader httpDataLoader, Integer[] cartIds) {
        CartDao.sendCmdToTrade(httpDataLoader, cartIds);
    }

    public static CartItem.Data[] removeProduct(CartItem.Data[] products,
                                                int childPosition) {
        if (null == products || products.length == 0) {
            return null;
        }

        List<CartItem.Data> tempListProducts = ListUtil.arrayToList(products);
        tempListProducts.remove(childPosition);
        if (tempListProducts.size() > 0) {
            products = new CartItem.Data[tempListProducts.size()];
            for (int i = 0; i < products.length; i++) {
                products[i] = tempListProducts.get(i);
            }

            return products;
        }
        return null;
    }

    public static int editQuantity(CartItem.Data product, boolean isAdd,
                                   boolean isRemove) {
        if (null == product) {
            return 0;
        }

        String strQuantity = product.quantity;
        if (isRemove) {
            strQuantity = "0";
        } else {
            if (isAdd) {
                strQuantity = "" + (Integer.parseInt(strQuantity) + 1);
            } else {
                strQuantity = "" + (Integer.parseInt(strQuantity) - 1);
            }
        }

        return Integer.parseInt(strQuantity);
    }

    public static void selectCartProduct(List<CartItem> listCartItems,
                                         int groupPosition, int childPosition, boolean isSelectAll) {
        if (null == listCartItems) {
            return;
        }
        if (isSelectAll) {
            listCartItems.get(groupPosition).isSelect =
                    !listCartItems.get(groupPosition).isSelect;
            for (int i = 0; i < listCartItems.get(groupPosition).product.length; i++) {
                listCartItems.get(groupPosition).product[i].isSelect =
                        listCartItems.get(groupPosition).isSelect;
            }
        } else {
            listCartItems.get(groupPosition).product[childPosition].isSelect =
                    !listCartItems.get(groupPosition).product[childPosition].isSelect;

            boolean isAllSelect = true;
            for (int i = 0; i < listCartItems.get(groupPosition).product.length; i++) {
                if (!listCartItems.get(groupPosition).product[i].isSelect) {
                    isAllSelect = false;
                }
            }
            listCartItems.get(groupPosition).isSelect = isAllSelect;
        }

    }

    public static boolean selectAllCartProduct(List<CartItem> listCartItems) {
        if (null == listCartItems || listCartItems.size() == 0) {
            return false;
        }

        for (int i = 0; i < listCartItems.size(); i++) {
            listCartItems.get(i).isSelect = !listCartItems.get(i).isSelect;

            for (int j = 0; j < listCartItems.get(i).product.length; j++) {
                listCartItems.get(i).product[j].isSelect =
                        !listCartItems.get(i).product[j].isSelect;
            }
        }

        if (listCartItems.get(0).isSelect) {
            return true;
        }

        return false;
    }

    public static boolean isAllCartProductSelected(List<CartItem> listCartItems) {
        if (null == listCartItems || listCartItems.size() == 0) {
            return false;
        }

        for (int i = 0; i < listCartItems.size(); i++) {
            if (!listCartItems.get(i).isSelect) {
                return false;
            }
        }

        return true;
    }

    public static BigDecimal getTotalMoney(List<CartItem> listCartItems) {
        BigDecimal totalPrice = new BigDecimal(0);
        if (null == listCartItems) {
            return totalPrice;
        }
        for (int i = 0; i < listCartItems.size(); i++) {
            for (int j = 0; j < listCartItems.get(i).product.length; j++) {
                CartItem.Data product = listCartItems.get(i).product[j];
                if (null != product && product.isSelect) {
                    totalPrice = totalPrice.add(product.total_price);
                }
            }
        }
        return totalPrice;
    }

    public static Integer[] getAllSelectCartId(List<CartItem> listCartItems) {
        if (null == listCartItems) {
            return null;
        }
        List<Integer> listCartIds = new ArrayList<Integer>();

        for (int i = 0; i < listCartItems.size(); i++) {
            for (int j = 0; j < listCartItems.get(i).product.length; j++) {
                CartItem.Data product = listCartItems.get(i).product[j];
                if (null != product && product.isSelect) {
                    listCartIds.add(Integer.parseInt(product.cart_id));
                }
            }
        }

        if (listCartIds.size() > 0) {
            Integer[] arrayCartIds = new Integer[listCartIds.size()];
            for (int i = 0; i < listCartIds.size(); i++) {
                arrayCartIds[i] = listCartIds.get(i);
            }
            return arrayCartIds;
        }

        return null;
    }
}