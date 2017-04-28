
package com.android.juzbao.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.juzbao.application.BaseApplication;
import com.android.juzbao.constant.ResultActivity;
import com.android.juzbao.model.OrderBusiness;
import com.android.juzbao.model.ProviderFileBusiness;
import com.android.juzbao.model.ReviewBusiness;
import com.android.juzbao.util.CommonValidate;
import com.android.mall.resource.R;
import com.android.zcomponent.http.api.model.MessageData;
import com.android.zcomponent.json.JsonSerializerFactory;
import com.android.zcomponent.util.ImageLoaderUtil;
import com.android.zcomponent.util.ShowMsg;
import com.android.zcomponent.views.swipebacklayout.lib.app.SwipeBackActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.server.api.model.CommonReturn;
import com.server.api.model.Order;
import com.server.api.service.ReviewService;
import com.server.api.service.ReviewService.AddReviewRequest;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * <p>
 * Description: 添加评价
 * </p>
 *
 * @ClassName:AddReviewActivity
 * @author: wei
 * @date: 2015-11-10
 */
@EActivity(R.layout.activity_add_review)
public class AddReviewActivity extends SwipeBackActivity {

    private static final int LEVEL_BEST = 1;

    private static final int LEVEL_BETTER = 2;

    private static final int LEVEL_BAD = 3;

    @ViewById(R.id.imgvew_review_best_show)
    ImageView mImgvewReviewBest;

    @ViewById(R.id.imgvew_review_better_show)
    ImageView mImgvewReviewBetter;

    @ViewById(R.id.imgvew_review_bad_show)
    ImageView mImgvewReviewBad;

    @ViewById(R.id.imgvew_1)
    ImageView mImgvew1;

    @ViewById(R.id.imgvew_2)
    ImageView mImgvew2;

    @ViewById(R.id.imgvew_3)
    ImageView mImgvew3;

    @ViewById(R.id.imgvew_4)
    ImageView mImgvew4;

    @ViewById(R.id.rating_real_show)
    RatingBar mRatingReal;

    @ViewById(R.id.rating_server_show)
    RatingBar mRatingServer;

    @ViewById(R.id.rating_speed_show)
    RatingBar mRatingSpeed;

    @ViewById(R.id.rating_deliver_server_show)
    RatingBar mRatingDeliverServer;

    @ViewById(R.id.tvew_order_name_show)
    TextView mTvewOrderName;

    @ViewById(R.id.editvew_desc_show)
    EditText mEdtvewReviewDesc;

    @ViewById(R.id.llayout_order_item)
    LinearLayout mLlayoutOrderItem;

    private Order mOrder;

    private AddReviewRequest mAddReviewRequest;

    private int level = LEVEL_BEST;

    private int miSelectPosition = -1;

    private ProviderFileBusiness mFileBusiness;

    /**
     * 追加评价
     */
    private boolean isMoreContent = false;

    private boolean isSaler = false;

    /**
     * llayout_ratingbar
     */
    @ViewById(R.id.llayout_ratingbar)
    LinearLayout llayout_ratingbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    void initUI() {
        getTitleBar().setTitleText("添加评价 ");

        mFileBusiness = new ProviderFileBusiness(this, getHttpDataLoader());
        mFileBusiness.setOutParams(1, 1, 450, 450);

        Intent intent = getIntent();
        String order = intent.getStringExtra("order");
        //是否是追加评论
        isMoreContent = intent.getBooleanExtra("isMoreContent", false);
        //是否是卖家
        isSaler = intent.getBooleanExtra("isSaler", false);
        if (isMoreContent) {
            llayout_ratingbar.setVisibility(View.GONE);
        }

        mOrder = JsonSerializerFactory.Create().decode(order, Order.class);
        mAddReviewRequest = new AddReviewRequest();
        showOrderInfo(mOrder);
    }

    @Override
    public void onRecvMsg(MessageData msg) throws Exception {
        mFileBusiness.onRecvMsg(msg);

        if (msg.valiateReq(AddReviewRequest.class)) {
            CommonReturn response = (CommonReturn) msg.getRspObject();
            if (CommonValidate.validateQueryState(this, msg, response, "评价失败")) {
                ShowMsg.showToast(getApplicationContext(), "评价成功");
                mOrder.products[miSelectPosition].is_comment = "1";
                showOrderInfo(mOrder);
                reset();
                Intent intent = new Intent();
                intent.putExtra("order", JsonSerializerFactory.Create().encode(mOrder));
                BaseApplication.getInstance().setActivityResult(ResultActivity.CODE_ADD_SHOP_REVIEW, intent);
                if (OrderBusiness.isAllOrderItemCommented(mOrder.products)) {
                    finish();
                }
            }
        } else if (msg.valiateReq(ReviewService.MoreCommentRequest.class)) {
            CommonReturn response = (CommonReturn) msg.getRspObject();
            if (CommonValidate.validateQueryState(this, msg, response, "评价失败")) {
                ShowMsg.showToast(getApplicationContext(), "评价成功");
                mOrder.products[miSelectPosition].is_comment = "1";
                showOrderInfo(mOrder);
                reset();
                Intent intent = new Intent();
                intent.putExtra("order", JsonSerializerFactory.Create().encode(mOrder));
                BaseApplication.getInstance().setActivityResult(ResultActivity.CODE_ADD_SHOP_REVIEW, intent);
                if (OrderBusiness.isAllOrderItemCommented(mOrder.products)) {
                    finish();
                }
            }
        }
    }

    private void reset() {
        setReviewLevel(0);
        mEdtvewReviewDesc.setText("");
        mRatingReal.setProgress(5);
        mRatingServer.setProgress(5);
        mRatingSpeed.setProgress(5);
        mImgvew1.setImageBitmap(null);
        mImgvew2.setImageBitmap(null);
        mImgvew3.setImageBitmap(null);
    }

    private void showOrderInfo(Order order) {
        if (null == order || null == order.products
                || order.products.length == 0) {
            return;
        }

        mTvewOrderName.setText(order.products[0].shop_title);

        DisplayImageOptions options =
                ImageLoaderUtil
                        .getDisplayImageOptions(R.drawable.img_empty_logo_small);
        OrderBusiness.showOrderItem(this, mLlayoutOrderItem, order.products,
                ImageLoader.getInstance(), options);
        for (int i = 0; i < mLlayoutOrderItem.getChildCount(); i++) {
            View view = mLlayoutOrderItem.getChildAt(i);
            TextView selectMark =
                    (TextView) view
                            .findViewById(R.id.tvew_product_commented_show);
            if ("1".equals(order.products[i].is_comment)) {
                selectMark.setVisibility(View.VISIBLE);
            } else {
                selectMark.setVisibility(View.GONE);
                view.setOnClickListener(new OrderItemClickListener(i));
            }
        }
    }

    public class OrderItemClickListener implements OnClickListener {

        private int selectPosition;

        public OrderItemClickListener(int position) {
            selectPosition = position;
        }

        @Override
        public void onClick(View v) {
            miSelectPosition = selectPosition;

            for (int i = 0; i < mLlayoutOrderItem.getChildCount(); i++) {
                View view = mLlayoutOrderItem.getChildAt(i);
                ImageView selectMark =
                        (ImageView) view.findViewById(R.id.imgvew_select);
                if (selectPosition == i) {
                    selectMark.setVisibility(View.VISIBLE);
                } else {
                    selectMark.setVisibility(View.GONE);
                }
            }
        }

    }

    @Click(R.id.tvew_order_name_show)
    void onClickTvewShopName() {
        // ProductBusiness.intentToProductDetailActivity(this, null,
        // Integer.parseInt(mOrder.products[0].product_id));
    }

    @Click(R.id.llayout_review_best_click)
    void onClickLlayoutReviewBest() {
        setReviewLevel(LEVEL_BEST);
    }

    @Click(R.id.llayout_review_better_click)
    void onClickLlayoutReviewBetter() {
        setReviewLevel(LEVEL_BETTER);
    }

    @Click(R.id.llayout_review_bad_click)
    void onClickLlayoutReviewBad() {
        setReviewLevel(LEVEL_BAD);
    }

    @Click(R.id.imgvew_1)
    void onClickImageView1() {
        mFileBusiness.selectPicture();
    }

    @Click(R.id.imgvew_2)
    void onClickImageView2() {
        mFileBusiness.selectPicture();
    }

    @Click(R.id.imgvew_3)
    void onClickImageView3() {
        mFileBusiness.selectPicture();
    }

    @Click(R.id.imgvew_4)
    void onClickImageView4() {
        mFileBusiness.selectPicture();
    }

    /**
     * 添加评价
     */
    @Click(R.id.tvew_submit_click)
    void onClickTvewSubmit() {
        if (isMoreContent) {
            ReviewService.MoreCommentRequest request = new ReviewService.MoreCommentRequest();
            request.CoverIds = mFileBusiness.getImageFileIds();
            request.OrderId = mOrder.order_id;
            if (miSelectPosition != -1) {
                request.ProductId = mOrder.products[miSelectPosition].product_id;
            } else {
                ShowMsg.showToast(getApplicationContext(), "请选择要评价的商品");
                return;
            }
            request.Content = mEdtvewReviewDesc.getText().toString();
            if (TextUtils.isEmpty(request.Content)) {
                ShowMsg.showToast(this, "请输入评价内容");
                return;
            }
            getHttpDataLoader().doPostProcess(request, CommonReturn.class);
        } else {
            mAddReviewRequest.RatingDesc = mRatingReal.getProgress();
            mAddReviewRequest.RatingAttitude = mRatingServer.getProgress();
            mAddReviewRequest.RatingSpeed = mRatingSpeed.getProgress();
            mAddReviewRequest.RatingShipping = mRatingDeliverServer.getProgress();
            mAddReviewRequest.Content = mEdtvewReviewDesc.getText().toString();
            mAddReviewRequest.Rate = level;
            mAddReviewRequest.CoverIds = mFileBusiness.getImageFileIds();
            mAddReviewRequest.OrderId = mOrder.order_id;
            if (miSelectPosition != -1) {
                mAddReviewRequest.ProductId =
                        mOrder.products[miSelectPosition].product_id;
            } else {
                ShowMsg.showToast(getApplicationContext(), "请选择要评价的商品");
                return;
            }
            boolean isSend =
                    ReviewBusiness.queryAddReview(this, getHttpDataLoader(),
                            mAddReviewRequest);
            if (isSend) {
                showWaitDialog(1, false, R.string.common_submit_data);
            }
        }
    }

    private void setReviewLevel(int level) {
        this.level = level;
        mImgvewReviewBest.setBackgroundResource(R.drawable.cart_option);
        mImgvewReviewBetter.setBackgroundResource(R.drawable.cart_option);
        mImgvewReviewBad.setBackgroundResource(R.drawable.cart_option);

        if (level == LEVEL_BEST) {
            mImgvewReviewBest.setBackgroundResource(R.drawable.cart_option_on);
        } else if (level == LEVEL_BETTER) {
            mImgvewReviewBetter
                    .setBackgroundResource(R.drawable.cart_option_on);
        } else if (level == LEVEL_BAD) {
            mImgvewReviewBad.setBackgroundResource(R.drawable.cart_option_on);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bitmap bitmap =
                mFileBusiness.onActivityResult(requestCode, resultCode, data);
        if (mFileBusiness.getImageFiles().size() == 0) {
            mImgvew1.setImageBitmap(bitmap);
        } else if (mFileBusiness.getImageFiles().size() == 1) {
            mImgvew2.setImageBitmap(bitmap);
        } else if (mFileBusiness.getImageFiles().size() == 2) {
            mImgvew3.setImageBitmap(bitmap);
        } else if (mFileBusiness.getImageFiles().size() == 3) {
            mImgvew4.setImageBitmap(bitmap);
        }
    }
}