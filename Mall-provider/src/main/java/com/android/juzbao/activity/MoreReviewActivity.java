package com.android.juzbao.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.juzbao.model.ProviderFileBusiness;
import com.android.juzbao.provider.R;
import com.android.juzbao.util.CommonValidate;
import com.android.zcomponent.http.api.host.Endpoint;
import com.android.zcomponent.http.api.model.MessageData;
import com.android.zcomponent.json.JsonSerializerFactory;
import com.android.zcomponent.util.ImageLoaderUtil;
import com.android.zcomponent.util.LogEx;
import com.android.zcomponent.util.ShowMsg;
import com.android.zcomponent.util.StringUtil;
import com.android.zcomponent.views.swipebacklayout.lib.app.SwipeBackActivity;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.server.api.model.CommonReturn;
import com.server.api.model.Order;
import com.server.api.model.OrderItem;
import com.server.api.service.ReviewService;

public class MoreReviewActivity extends SwipeBackActivity {

    private LinearLayout layout_images;
    private ImageView imageView1, imageView2, imageView3, imageView4;
    private TextView mShopNameTv;
    private LinearLayout mOrder_item;
    private EditText mDesc_showEt;
    private TextView mMoreReviewTv;

    private Order mOrder;

    private int miSelectPosition = -1;

    private ProviderFileBusiness mFileBusiness;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.initView();
        this.initData();
        this.initEvent();
    }

    private void initView() {
        setContentView(R.layout.activity_more_review);
        getTitleBar().setTitleText("追加评价");
        mShopNameTv = (TextView) findViewById(R.id.tvew_order_name_show);
        imageView1 = (ImageView) findViewById(R.id.imgvew_1);
        imageView2 = (ImageView) findViewById(R.id.imgvew_2);
        imageView3 = (ImageView) findViewById(R.id.imgvew_3);
        imageView4 = (ImageView) findViewById(R.id.imgvew_4);
        mOrder_item = (LinearLayout) findViewById(R.id.llayout_order_item);
        mDesc_showEt = (EditText) findViewById(R.id.editvew_desc_show);
        mMoreReviewTv = (TextView) findViewById(R.id.tvew_more_review_click);
        layout_images = (LinearLayout) findViewById(R.id.layout_images);
        layout_images.setVisibility(View.GONE);
    }

    private void initData() {
        Intent intent = getIntent();
        String order = intent.getStringExtra("order");
        mOrder = JsonSerializerFactory.Create().decode(order, Order.class);
        mFileBusiness = new ProviderFileBusiness(this, getHttpDataLoader());
        mFileBusiness.setOutParams(1, 1, 450, 450);
        showOrderInfo(mOrder);
    }

    private void initEvent() {
        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mFileBusiness.selectPicture();
            }
        });
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mFileBusiness.selectPicture();
            }
        });
        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mFileBusiness.selectPicture();
            }
        });
        imageView4.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mFileBusiness.selectPicture();
            }
        });
        /**
         * 追加评价
         */
        mMoreReviewTv.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                ReviewService.MoreCommentRequest request = new ReviewService.MoreCommentRequest();
                request.CoverIds = mFileBusiness.getImageFileIds();
                request.OrderId = mOrder.order_id;
                if (miSelectPosition != -1) {
                    request.ProductId = mOrder.products[miSelectPosition].product_id;
                } else {
                    ShowMsg.showToast(getApplicationContext(), "请选择要评价的商品");
                    return;
                }
                request.Content = mDesc_showEt.getText().toString();
                if (TextUtils.isEmpty(request.Content)) {
                    ShowMsg.showToast(MoreReviewActivity.this, "请输入评价内容");
                    return;
                }
                getHttpDataLoader().doPostProcess(request, CommonReturn.class);
            }
        });
    }

    @Override public void onRecvMsg(MessageData msg) throws Exception {
        if (msg.valiateReq(ReviewService.MoreCommentRequest.class)) {
            CommonReturn response = (CommonReturn) msg.getRspObject();
            if (CommonValidate.validateQueryState(this, msg, response, "评价失败")) {
                LogEx.d("评价状态", response.message.toString());
                ShowMsg.showToast(getApplicationContext(), "评价成功");
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        finish();
                    }
                }, 200);
            }
        }
    }

    private void showOrderInfo(Order order) {
        if (null == order || null == order.products || order.products.length == 0) {
            return;
        }
        mShopNameTv.setText(order.products[0].shop_title);
        mOrder_item.removeAllViews();

        for (int i = 0; i < order.products.length; i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.layout_shop_order_item, null);
            OrderItem orderItem = order.products[i];
            ImageView imgvewPhoto = (ImageView) view.findViewById(R.id.imgvew_photo_show);
            ImageLoader.getInstance().displayImage(
                    Endpoint.HOST + orderItem.image_path, imgvewPhoto,
                    ImageLoaderUtil.getDisplayImageOptions(R.drawable.img_empty_logo_small));
            TextView tvewProductName = (TextView) view.findViewById(R.id.tvew_product_name_show);
            TextView tvewProductAttr = (TextView) view.findViewById(R.id.tvew_product_attr_show);
            TextView tvewProductNowPrice = (TextView) view.findViewById(R.id.tvew_product_now_price_show);
//            TextView tvewProductPrice = (TextView) view.findViewById(R.id.tvew_product_price_show);
            TextView tvewProductNum = (TextView) view.findViewById(R.id.tvew_product_num_show);
            TextView tvewLine = (TextView) view.findViewById(R.id.tvew_line);
            if (i == order.products.length - 1) {
                tvewLine.setVisibility(View.GONE);
            }
            tvewProductAttr.setText(orderItem.product_attr);
            tvewProductName.setText(orderItem.product_title);
            if (StringUtil.formatProgress(orderItem.price).doubleValue() > 0) {
                tvewProductNowPrice.setText("¥" + StringUtil.formatProgress(orderItem.price));
            } else {
                tvewProductNowPrice.setText("¥" + StringUtil.formatProgress(orderItem.unit_price));
            }
            tvewProductNum.setText("x" + orderItem.quantity);
            mOrder_item.addView(view);
        }
        for (int i = 0; i < mOrder_item.getChildCount(); i++) {
            View view = mOrder_item.getChildAt(i);
            TextView selectMark = (TextView) view.findViewById(R.id.tvew_product_commented_show);
            if ("1".equals(order.products[i].is_comment)) {
                selectMark.setVisibility(View.VISIBLE);
                view.setOnClickListener(new OrderItemClickListener(i));
            } else {
                selectMark.setVisibility(View.GONE);
                view.setOnClickListener(new OrderItemClickListener(i));
            }
        }
    }

    public class OrderItemClickListener implements View.OnClickListener {

        private int selectPosition;

        public OrderItemClickListener(int position) {
            selectPosition = position;
        }

        @Override
        public void onClick(View v) {
            miSelectPosition = selectPosition;

            for (int i = 0; i < mOrder_item.getChildCount(); i++) {
                View view = mOrder_item.getChildAt(i);
                ImageView selectMark = (ImageView) view.findViewById(R.id.imgvew_select);
                if (selectPosition == i) {
                    selectMark.setVisibility(View.VISIBLE);
                } else {
                    selectMark.setVisibility(View.GONE);
                }
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bitmap bitmap =
                mFileBusiness.onActivityResult(requestCode, resultCode, data);
        if (mFileBusiness.getImageFiles().size() == 0) {
            imageView1.setImageBitmap(bitmap);
        } else if (mFileBusiness.getImageFiles().size() == 1) {
            imageView2.setImageBitmap(bitmap);
        } else if (mFileBusiness.getImageFiles().size() == 2) {
            imageView3.setImageBitmap(bitmap);
        } else if (mFileBusiness.getImageFiles().size() == 3) {
            imageView4.setImageBitmap(bitmap);
        }
    }
}