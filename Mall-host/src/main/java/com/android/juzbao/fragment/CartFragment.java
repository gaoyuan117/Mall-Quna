
package com.android.juzbao.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.TextView;

import com.android.juzbao.activity.CartActivity_;
import com.android.juzbao.activity.order.OrderEnsureActivity_;
import com.android.juzbao.adapter.CartAdapter;
import com.android.juzbao.adapter.CartAdapter.EditProductClickListener;
import com.android.juzbao.application.BaseApplication;
import com.android.juzbao.constant.ResultActivity;
import com.android.juzbao.model.CartBusiness;
import com.android.juzbao.util.CommonValidate;
import com.android.mall.resource.R;
import com.android.zcomponent.common.uiframe.fragment.BaseFragment;
import com.android.zcomponent.http.api.model.MessageData;
import com.android.zcomponent.http.constant.ErrorCode;
import com.android.zcomponent.json.JsonSerializerFactory;
import com.android.zcomponent.util.CommonUtil;
import com.android.zcomponent.util.LogEx;
import com.android.zcomponent.util.ShowMsg;
import com.android.zcomponent.util.StringUtil;
import com.server.api.model.CartItem;
import com.server.api.model.CartPageResult;
import com.server.api.model.CartTradeResult;
import com.server.api.model.CommonReturn;
import com.server.api.service.CartService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * Description: 购物车
 * </p>
 */
@EFragment(R.layout.fragment_cart)
public class CartFragment extends BaseFragment implements
        EditProductClickListener {

    @ViewById(R.id.imgvew_select_show)
    TextView mImgvewSelect;

    @ViewById(R.id.tvew_settle_show)
    TextView mTvewSettle;

    @ViewById(R.id.tvew_total_money_show)
    TextView mTvewTotalMoney;

    @ViewById(R.id.tvew_select_show_click)
    TextView mtvewSelectAll;

    @ViewById(R.id.common_listview_show)
    ExpandableListView mListView;

    private List<CartItem> mlistCartItems = null;

    private CartAdapter mCartAdapter;

    private int groupPosition;

    private int childPosition;

    private boolean isAdd;

    private boolean isRemove;

    @AfterViews
    void initUI() {
        mListView.setGroupIndicator(null);
        mListView.setOnGroupClickListener(new OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                boolean b = mListView.expandGroup(groupPosition);
                LogEx.d("cart", "" + b);
                return false;
            }
        });

        getDataEmptyView().setBackgroundResource(R.drawable.transparent);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (BaseApplication.isLogin()) {
            CartBusiness.queryCarts(getHttpDataLoader(), 1);
            getDataEmptyView().showViewWaiting();
        } else {
            getDataEmptyView().showViewDataEmpty(false, false, true, 1, "还未登录", "去登录");
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            if (BaseApplication.isLogin()) {
                CartBusiness.queryCarts(getHttpDataLoader(), 1);
            }
        }
    }

    @Override
    public void onDataEmptyClickChange() {
        BaseApplication.intentToLoginActivity(getActivity());
    }

    @Override
    public void onLoginSuccess() {
        CartBusiness.queryCarts(getHttpDataLoader(), 1);
        getDataEmptyView().showViewWaiting();
    }

    @Override
    public void onActivityResultCallBack(int resultCode, Intent intent) {
        if (resultCode == ResultActivity.CODE_ADD_ORDER) {
            CartBusiness.queryCarts(getHttpDataLoader(), 1);
            getDataEmptyView().showViewWaiting();
        }
    }

    @Override
    public void onRecvMsg(MessageData msg) throws Exception {
        if (msg.valiateReq(CartService.QueryCartsRequest.class)) {
            CartPageResult response = msg.getRspObject();
            if (CommonValidate.validateQueryState(getActivity(), msg, response)) {
                if (null != response.Data && response.Data.Result.length > 0) {
                    mlistCartItems = new ArrayList<CartItem>(Arrays.asList(response.Data.Result));
                    getDataEmptyView().setVisibility(View.GONE);
                } else {
                    if (null != mlistCartItems) {
                        mlistCartItems.clear();
                    }
                    getDataEmptyView().showViewDataEmpty(false, false, msg, "购物车空空如也");
                }

                if (null == mCartAdapter) {
                    mCartAdapter = new CartAdapter(getActivity(), mlistCartItems);
                    mCartAdapter.setEditProductClickListener(this);
                    mListView.setAdapter(mCartAdapter);
                    int groupCount = mListView.getCount();
                    for (int i = 0; i < groupCount; i++) {
                        mListView.expandGroup(i);
                    }
                } else {
                    mCartAdapter.refreshAdapterData(mlistCartItems);
//                    mCartAdapter.notifyDataSetChanged();
                    for (int i = 0; i < mCartAdapter.getGroupCount(); i++) {
                        mListView.expandGroup(i);
                    }
                }

                showTotalMoney();
            } else {
                getDataEmptyView().showViewDataEmpty(false, false, msg,
                        "购物车空空如也");
            }
        } else if (msg.valiateReq(CartService.DelCartByIdRequest.class)) {
            CommonReturn response = (CommonReturn) msg.getRspObject();
            if (null != response) {
                if (response.code == ErrorCode.INT_QUERY_DATA_SUCCESS) {
                    CartItem.Data[] products =
                            CartBusiness.removeProduct(
                                    mlistCartItems.get(groupPosition).product,
                                    childPosition);
                    if (null != products) {
                        mlistCartItems.get(groupPosition).product = products;
                    } else {
                        mlistCartItems.remove(groupPosition);
                    }

                    if (mlistCartItems.size() == 0) {
                        getDataEmptyView().showViewDataEmpty(false, false, msg,
                                "购物车空空如也");
                    }

                    mCartAdapter.notifyDataSetChanged();

                    showTotalMoney();
                } else {
                    ShowMsg.showToast(getActivity(), msg, response.message);
                }
            } else {
                ShowMsg.showToast(getActivity(), msg, "删除失败");
            }
        } else if (msg.valiateReq(CartService.EditCartRequest.class)) {
            CommonReturn response = msg.getRspObject();
            if (null != response) {
                if (response.code == ErrorCode.INT_QUERY_DATA_SUCCESS) {
                    CartItem.Data product =
                            mlistCartItems.get(groupPosition).product[childPosition];
                    product.quantity =
                            CartBusiness.editQuantity(product, isAdd, isRemove)
                                    + "";
                    product.total_price =
                            product.price.multiply(new BigDecimal(
                                    product.quantity));
                    CartItem cartItem = mlistCartItems.get(groupPosition);
                    cartItem.product[childPosition] = product;
                    mlistCartItems.set(groupPosition, cartItem);
                    mCartAdapter.refreshAdapterData(mlistCartItems);
                    mCartAdapter.notifyDataSetChanged();

                    showTotalMoney();
                } else {
                    ShowMsg.showToast(getActivity(), msg, response.message);
                }
            } else {
                ShowMsg.showToast(getActivity(), msg, "编辑失败");
            }
        } else if (msg.valiateReq(CartService.ToTradeRequest.class)) {
            CartTradeResult response = (CartTradeResult) msg.getRspObject();
            if (CommonValidate.validateQueryState(getActivity(), msg, response)) {
                Bundle bundle = new Bundle();
                bundle.putString("product", JsonSerializerFactory.Create().encode(response.Data));
                getIntentHandle().intentToActivity(bundle, OrderEnsureActivity_.class);

                if (getActivity() instanceof CartActivity_) {
                    getActivity().finish();
                }
            } else {
                ShowMsg.showToast(getActivity(), msg, "结算失败");
            }
        }
    }

    private void showTotalMoney() {
        BigDecimal totalPrice = CartBusiness.getTotalMoney(mlistCartItems);
        mTvewTotalMoney.setText("合计：¥" + StringUtil.formatProgress(totalPrice));
        if (null != mCartAdapter) {
            mCartAdapter.refreshAdapterData(mlistCartItems);
        }
        if (null == mlistCartItems || mlistCartItems.size() == 0) {
            showAllSelectBtnState(false);
        }
    }

    private void showAllSelectBtnState(boolean isSelectAll) {
        if (isSelectAll) {
            CommonUtil.setDrawableLeft(getActivity(), mtvewSelectAll,
                    R.drawable.cart_option_on);
        } else {
            CommonUtil.setDrawableLeft(getActivity(), mtvewSelectAll,
                    R.drawable.cart_option);
        }
    }

    @Click(R.id.tvew_select_show_click)
    void onClickSelectAll() {
        boolean isSelectAll = CartBusiness.selectAllCartProduct(mlistCartItems);
        showAllSelectBtnState(isSelectAll);
        showTotalMoney();
    }

    @Click(R.id.tvew_settle_show_click)
    void onClickTvewSettel() {
        Integer[] cartIds = CartBusiness.getAllSelectCartId(mlistCartItems);
        if (null != cartIds) {
            CartBusiness.toTrade(getHttpDataLoader(), cartIds);
            showWaitDialog(1, false, R.string.common_submit_data);
        } else {
            ShowMsg.showToast(getActivity(), "请选择需购买的商品");
        }
    }

    @Override
    public void onClickEditProduct(int groupPosition, int childPosition,
                                   boolean isAdd, boolean isRemove) {
        this.groupPosition = groupPosition;
        this.childPosition = childPosition;
        this.isAdd = isAdd;
        this.isRemove = isRemove;

        CartItem.Data product =
                mlistCartItems.get(groupPosition).product[childPosition];
        if (isRemove) {
            int cartId = Integer.parseInt(product.cart_id);
            CartBusiness.delToCart(getHttpDataLoader(), cartId);
            showWaitDialog(1, false, R.string.common_submit_data);
        } else {
            int quantity = CartBusiness.editQuantity(product, isAdd, isRemove);

            if (quantity > 0) {
                CartBusiness.editCart(getHttpDataLoader(), product.price,
                        Integer.parseInt(product.cart_id), quantity,
                        product.option_ids);
                showWaitDialog(1, false, R.string.common_submit_data);
            } else {
                ShowMsg.showToast(getActivity(), "再减就没了哦");
            }
        }
    }

    @Override
    public void onClickSelectProduct(int groupPosition, int childPosition,
                                     boolean isSelectAll) {
        CartBusiness.selectCartProduct(mlistCartItems, groupPosition,
                childPosition, isSelectAll);
        showAllSelectBtnState(CartBusiness
                .isAllCartProductSelected(mlistCartItems));
        showTotalMoney();

    }
}