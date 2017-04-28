
package com.android.juzbao.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.juzbao.activity.ProductDetailActivity;
import com.android.juzbao.activity.ProductDetailActivity_;
import com.android.juzbao.activity.ShopDetailActivity_;
import com.android.mall.resource.R;
import com.android.zcomponent.adapter.CommonExpandabelAdapter;
import com.android.zcomponent.http.api.host.Endpoint;
import com.android.zcomponent.util.StringUtil;
import com.android.zcomponent.views.IntentHandle;
import com.server.api.model.CartItem;

import java.util.List;

/**
 * <p>
 * Description: 选礼物列表项
 * </p>
 *
 * @ClassName:CartAdapter
 * @author: wei
 * @date: 2015-11-10
 */
public class CartAdapter extends CommonExpandabelAdapter {

    private boolean isShowOrderProduct;
    private Activity activity;

    public CartAdapter(Context context, List<?> list) {
        super(context, list);
        activity = (Activity) context;
    }

    @Override
    public int getGroupCount() {
        if (null == mList) {
            return 0;
        }
        return mList.size();
    }

    public void setShowOrderProduct(boolean isShowOrderProduct) {
        this.isShowOrderProduct = isShowOrderProduct;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (null == ((CartItem) mList.get(groupPosition)).product) {
            return 0;
        }

        return ((CartItem) mList.get(groupPosition)).product.length;
    }

    @Override
    public Object getGroup(int groupPosition) {
        if (null == mList.get(groupPosition)) {
            return null;
        }
        return mList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        if (null == mList.get(groupPosition)) {
            return null;
        }
        return ((CartItem) mList.get(groupPosition)).product;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        if (null == convertView) {
            convertView =
                    layoutInflater.inflate(R.layout.adapter_cart_group, null);
        }
        ImageView imgvewSelect =
                findViewById(convertView, R.id.imgvew_select_show);

        TextView tvewEdit = findViewById(convertView, R.id.tvew_edit_show);
        TextView tvewEditLine =
                findViewById(convertView, R.id.tvew_edit_line_show);
        TextView tvewShopName =
                findViewById(convertView, R.id.tvew_shop_name_show);

        if (isShowOrderProduct) {
            imgvewSelect.setVisibility(View.INVISIBLE);
            imgvewSelect.getLayoutParams().width = 20;
            tvewEdit.setVisibility(View.GONE);
            tvewEditLine.setVisibility(View.GONE);
        }

        final CartItem cartItem = (CartItem) mList.get(groupPosition);
        tvewEdit.setOnClickListener(new ChangeEditStateClickListener(
                groupPosition));
        tvewShopName.setText(cartItem.shop_title);
        if (cartItem.isEdit) {
            tvewEdit.setText("完成");
        } else {
            tvewEdit.setText("编辑");
        }

        tvewShopName.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putInt("id", Integer.parseInt(cartItem.shop_id));
                bundle.putString("title", cartItem.shop_title);
//                bundle.putString("im", cartItem.im_account);
                Intent intent = new Intent();

                if (null != bundle) {
                    intent.putExtras(bundle);
                }

                intent.putExtra("classname", activity.getComponentName()
                        .getClassName());
                intent.setClass(mContext, ShopDetailActivity_.class);
                mContext.startActivity(intent);

            }
        });

        if (cartItem.isSelect) {
            imgvewSelect.setImageResource(R.drawable.cart_option_on);
        } else {
            imgvewSelect.setImageResource(R.drawable.cart_option);
        }

        imgvewSelect.setOnClickListener(new SelectClickListener(groupPosition,
                -1, true));
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        if (null == convertView) {
            convertView =
                    layoutInflater.inflate(R.layout.adapter_cart_child, null);
        }

        final ImageView imgvewSelect1 =
                findViewById(convertView, R.id.imgvew_select1_show);
        RelativeLayout layout = findViewById(convertView, R.id.rl_cart);
        ImageView imgvewPhoto =
                findViewById(convertView, R.id.imgvew_photo_show);
        RelativeLayout rlayoutProductInfos =
                findViewById(convertView, R.id.rlayout_product_infos);
        RelativeLayout rlayoutAction =
                findViewById(convertView, R.id.rlayout_action);
        TextView tvewProductName =
                findViewById(convertView, R.id.tvew_product_name_show);
        TextView tvewProductPrice =
                findViewById(convertView, R.id.tvew_product_price_show);
        TextView tvewProductAttr =
                findViewById(convertView, R.id.tvew_product_attr_show);
        TextView tvewProductNum =
                findViewById(convertView, R.id.tvew_product_num_show);
        TextView tvewSelectNum =
                findViewById(convertView, R.id.tvew_select_num_show);

        TextView tvewSubProduct =
                findViewById(convertView, R.id.tvew_sub_product_show);
        TextView tvewAddProduct =
                findViewById(convertView, R.id.tvew_add_product_show);
        TextView tvewDelProduct =
                findViewById(convertView, R.id.tvew_del_product_show);

        if (isShowOrderProduct) {
            imgvewSelect1.setVisibility(View.GONE);
        }

        final CartItem.Data[] product = ((CartItem) mList.get(groupPosition)).product;

        tvewProductName.setText(product[childPosition].title);
        tvewProductPrice.setText("¥"
                + StringUtil.formatProgress(product[childPosition].price));
        tvewProductNum.setText("x" + product[childPosition].quantity);
        tvewSelectNum.setText(product[childPosition].quantity);
        tvewProductAttr.setText(product[childPosition].product_attr);
        String imagePath = null;
        if (!TextUtils.isEmpty(product[childPosition].image_path)) {
            imagePath = product[childPosition].image_path;
        } else {
            if (null != product[childPosition].images
                    && !TextUtils.isEmpty(product[childPosition].images.path)) {
                imagePath = product[childPosition].images.path;
            }
        }

        if (!TextUtils.isEmpty(imagePath)) {
            mImageLoader.displayImage(Endpoint.HOST + imagePath, imgvewPhoto,
                    options);
        } else {
            imgvewPhoto.setImageResource(R.drawable.img_empty_logo_small);
        }

        if (((CartItem) mList.get(groupPosition)).isEdit) {
            rlayoutProductInfos.setVisibility(View.GONE);
            rlayoutAction.setVisibility(View.VISIBLE);
        } else {
            rlayoutProductInfos.setVisibility(View.VISIBLE);
            rlayoutAction.setVisibility(View.GONE);
        }

        if (product[childPosition].isSelect) {
            imgvewSelect1.setImageResource(R.drawable.cart_option_on);
        } else {
            imgvewSelect1.setImageResource(R.drawable.cart_option);
        }

//        layout.setOnClickListener(new SelectClickListener(groupPosition,
//                childPosition, false));

        layout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ProductDetailActivity_.class);
                intent.putExtra("id", Integer.valueOf(product[childPosition].product_id));
                mContext.startActivity(intent);
            }
        });

        imgvewSelect1.setOnClickListener(new SelectClickListener(groupPosition,
                childPosition, false));

        tvewAddProduct.setOnClickListener(new EditClickListener(groupPosition,
                childPosition, true, false));
        tvewSubProduct.setOnClickListener(new EditClickListener(groupPosition,
                childPosition, false, false));
        tvewDelProduct.setOnClickListener(new EditClickListener(groupPosition,
                childPosition, false, true));
        return convertView;
    }

    private EditProductClickListener mEditProductClickListener;

    public interface EditProductClickListener {

        public void onClickEditProduct(int groupPosition, int childPosition,
                                       boolean isAdd, boolean isRemove);

        public void onClickSelectProduct(int groupPosition, int childPosition,
                                         boolean isSelectAll);
    }

    public void setEditProductClickListener(EditProductClickListener listener) {
        mEditProductClickListener = listener;
    }

    private class SelectClickListener implements OnClickListener {

        private int groupPosition;

        private int childPosition;

        private boolean isSelectAll;

        public SelectClickListener(int groupPosition, int childPosition,
                                   boolean isSelectAll) {
            this.groupPosition = groupPosition;
            this.childPosition = childPosition;
            this.isSelectAll = isSelectAll;
        }

        @Override
        public void onClick(View view) {
            if (null != mEditProductClickListener) {
                mEditProductClickListener.onClickSelectProduct(groupPosition,
                        childPosition, isSelectAll);
            }
        }
    }

    private class EditClickListener implements OnClickListener {

        private int groupPosition;

        private int childPosition;

        private boolean isAdd;

        private boolean isRemove;

        public EditClickListener(int groupPosition, int childPosition,
                                 boolean isAdd, boolean isRemove) {
            this.groupPosition = groupPosition;
            this.childPosition = childPosition;
            this.isAdd = isAdd;
            this.isRemove = isRemove;
        }

        @Override
        public void onClick(View view) {
            if (null != mEditProductClickListener) {
                mEditProductClickListener.onClickEditProduct(groupPosition,
                        childPosition, isAdd, isRemove);
            }
        }
    }

    private class ChangeEditStateClickListener implements OnClickListener {

        private int groupPosition;

        public ChangeEditStateClickListener(int groupPosition) {
            this.groupPosition = groupPosition;
        }

        @Override
        public void onClick(View view) {
            ((CartItem) mList.get(groupPosition)).isEdit =
                    !((CartItem) mList.get(groupPosition)).isEdit;
            notifyDataSetChanged();
        }
    }

}