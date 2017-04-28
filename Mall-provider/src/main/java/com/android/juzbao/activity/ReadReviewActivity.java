package com.android.juzbao.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.juzbao.model.ProviderOrderBusiness;
import com.android.juzbao.provider.R;
import com.android.zcomponent.http.api.host.Endpoint;
import com.android.zcomponent.http.api.model.MessageData;
import com.android.zcomponent.http.constant.ErrorCode;
import com.android.zcomponent.json.JsonSerializerFactory;
import com.android.zcomponent.util.ImageLoaderUtil;
import com.android.zcomponent.util.LogEx;
import com.android.zcomponent.views.swipebacklayout.lib.app.SwipeBackActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.server.api.model.Order;
import com.server.api.model.ReviewOrderItem;
import com.server.api.model.ReviewOrderResult;
import com.server.api.service.ReviewService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;


/**
 * <p>
 * Description:  查看评价
 */
@EActivity(resName = "activity_read_review")
public class ReadReviewActivity extends SwipeBackActivity {

    @ViewById(resName = "imgvew_1")
    ImageView mImgvew1;

    @ViewById(resName = "imgvew_2")
    ImageView mImgvew2;

    @ViewById(resName = "imgvew_3")
    ImageView mImgvew3;

    @ViewById(resName = "imgvew_4")
    ImageView mImgvew4;

    //订单item
    @ViewById(resName = "llayout_order_item")
    LinearLayout mLlayoutOrderItem;

    //综合评价
    @ViewById(resName = "tvew_review_better")
    TextView mTvewReviewBetter;

    //描述相符
    @ViewById(resName = "tvew_real_show")
    TextView mTvewReal;

    //服务态度
    @ViewById(resName = "tvew_server_show")
    TextView mTvewServer;

    //发货速度
    @ViewById(resName = "tvew_speed_show")
    TextView mTvewSpeed;

    //物流服务
    @ViewById(resName = "tvew_deliver_server_show")
    TextView mTvewDeliverServer;

    //店铺名称
    @ViewById(resName = "tvew_order_name_show")
    TextView mTvewOrderName;

    //评价列表
    @ViewById(resName = "llayout_review_message")
    LinearLayout mllayout_review_message;

    //评价内容
    @ViewById(resName = "tvew_review_message")
    TextView mtvew_review_message;

    @ViewById(resName = "btn_review_click")
    Button mbtn_review_click;

    private Order mOrder;
    private boolean isSaler = false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    void initUI() {
        getTitleBar().setTitleText("查看评价");
        Intent intent = getIntent();
        if (null != intent) {
            String order = intent.getStringExtra("order");
            isSaler = intent.getBooleanExtra("isSaler", false);
            mOrder = JsonSerializerFactory.Create().decode(order, Order.class);
            this.showButtonStatus();
            this.showOrderInfo();
            getDataEmptyView().setBackgroundResource(R.drawable.transparent);
            this.getReView(mOrder.order_id);
        }

    }

    /**
     * 显示订单基本信息
     */
    private void showOrderInfo() {

        mTvewOrderName.setText(mOrder.products[0].shop_title);
        DisplayImageOptions options = ImageLoaderUtil.getDisplayImageOptions(R.drawable.img_empty_logo_small);
        ProviderOrderBusiness.showOrderItem(this, mLlayoutOrderItem, mOrder.products, ImageLoader.getInstance(), options);
    }

    /**
     * 设置按钮状态
     */
    private void showButtonStatus() {
        if (isSaler) {
//            mbtn_review_click.setText("回复买家");
            mbtn_review_click.setVisibility(View.GONE);
        } else {
            mbtn_review_click.setText("追加评论");
        }
    }

    /**
     * 发起获取商品评价的请求
     *
     * @param id order_id
     */
    private void getReView(String id) {
        ReviewService.ReviewRequest request = new ReviewService.ReviewRequest();
        request.Id = id;
        LogEx.d("订单id", request.Id);
        LogEx.d("订单id", request.Token + "==");
        getHttpDataLoader().doPostProcess(request, ReviewOrderResult.class);
    }

    @Override public void onRecvMsg(MessageData msg) throws Exception {
        if (msg.valiateReq(ReviewService.ReviewRequest.class)) {
            ReviewOrderResult response = (ReviewOrderResult) msg.getRspObject();
            if (null != response) {
                if (response.code == ErrorCode.INT_QUERY_DATA_SUCCESS) {
                    this.showReviewInfo(response.Data[0]);
                    findViewById(R.id.llayout_content).setVisibility(View.VISIBLE);
                }
            }
        }
    }

    /**
     * 显示评价信息
     */
    private void showReviewInfo(ReviewOrderItem reviewOrderItem) {

        if ("1".equals(reviewOrderItem.rate)) {
            Drawable drawable = getResources().getDrawable(R.drawable.pingjia01);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            mTvewReviewBetter.setCompoundDrawables(drawable, null, null, null);
            mTvewReviewBetter.setText("好评");
        } else if ("2".equals(reviewOrderItem.rate)) {
            Drawable drawable = getResources().getDrawable(R.drawable.pingjia02);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            mTvewReviewBetter.setCompoundDrawables(drawable, null, null, null);
            mTvewReviewBetter.setText("中评");
        } else if ("3".equals(reviewOrderItem.rate)) {
            Drawable drawable = getResources().getDrawable(R.drawable.pingjia03);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            mTvewReviewBetter.setCompoundDrawables(drawable, null, null, null);
            mTvewReviewBetter.setText("差评");
        }
        mTvewReal.setText(reviewOrderItem.rating_desc + "分");
        mTvewServer.setText(reviewOrderItem.rating_attitude + "分");
        mTvewSpeed.setText(reviewOrderItem.rating_speed + "分");
        mTvewDeliverServer.setText(reviewOrderItem.rating_shipping + "分");

        ImageView[] imageViews = {mImgvew1, mImgvew2, mImgvew3, mImgvew4};
        DisplayImageOptions options = ImageLoaderUtil.getDisplayImageOptions(R.drawable.img_empty_logo_small);
        if (reviewOrderItem.images != null) {
            for (int i = 0; i < reviewOrderItem.images.length; i++) {
                //不知道这里为什么需要从1开始！
                if (TextUtils.isEmpty(reviewOrderItem.images[i])) {
                    continue;
                }
                ImageLoader.getInstance().displayImage(Endpoint.HOST + reviewOrderItem.images[i], imageViews[i], options);
                imageViews[i].setVisibility(View.VISIBLE);
                imageViews[i].setOnClickListener(new View.OnClickListener() {
                    @Override public void onClick(View v) {
                        // TODO: 2016/7/21 设置ImageView的点击事件
                    }
                });
            }
        }

        //显示聊天内容
        mtvew_review_message.setText("评论内容：" + reviewOrderItem.content);

        mllayout_review_message.removeAllViews();

        for (int i = 1; i < reviewOrderItem.messages.length; i++) {

            TextView textView = new TextView(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            layoutParams.leftMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10f, getResources().getDisplayMetrics());
            layoutParams.rightMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10f, getResources().getDisplayMetrics());
            layoutParams.topMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 5f, getResources().getDisplayMetrics());
            layoutParams.bottomMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 5f, getResources().getDisplayMetrics());
            textView.setTextSize(14);
            textView.setLineSpacing(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3f, getResources().getDisplayMetrics()), 1);
            textView.setTextColor(0xbb333333);
            textView.setLayoutParams(layoutParams);
            if ("1".equals(reviewOrderItem.messages[i].is_saler)) {
//                textView.setText("卖家回复： " + reviewOrderItem.messages[i].content);
            } else {
                textView.setText("追加评论：" + reviewOrderItem.messages[i].content);
                mllayout_review_message.addView(textView);
            }
        }
    }

    /**
     * 点击提交评论
     */
    @Click(resName = "btn_review_click")
    public void clickReview(View view) {
        if (isSaler) {
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putString("order", JsonSerializerFactory.Create().encode(mOrder));
//        bundle.putBoolean("isMoreContent", true);
//        ComponentName cn = new ComponentName
//                ("com.android.juzbao.activity", "com.android.juzbao.activity.AddReviewActivity_");
//        Intent intent = new Intent();
//        intent.putExtras(bundle);
//        intent.setComponent(cn);
//        this.startActivity(intent);
        Intent intent = new Intent();
        intent.setClass(this, MoreReviewActivity.class);
        intent.putExtras(bundle);
        this.startActivity(intent);
    }

    @Override protected void onRestart() {
        super.onRestart();
        if (mOrder != null) {
            getReView(mOrder.order_id);
        }
    }
}
