
package com.android.juzbao.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.mall.resource.R;
import com.android.zcomponent.activity.ImageViewPagerActivity;
import com.android.zcomponent.adapter.CommonAdapter;
import com.android.zcomponent.http.api.host.Endpoint;
import com.android.zcomponent.util.ImageLoaderUtil;
import com.android.zcomponent.views.CircleImageView;
import com.server.api.model.ReviewItem;

import java.util.List;

/**
 * <p>
 * Description: 评价列表项
 * </p>
 *
 * @ClassName:ReviewAdapter
 * @author: wei
 * @date: 2015-11-10
 */
public class ReviewAdapter extends CommonAdapter {

    public ReviewAdapter(Context context, List<?> list) {
        super(context, list);

        options =
                ImageLoaderUtil.getDisplayImageOptions(R.drawable.img_empty_logo_small);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            convertView = layoutInflater.inflate(R.layout.adapter_review, null);
        }

        CircleImageView imgvewHeadPicture =
                findViewById(convertView, R.id.imgvew_head_picture_show);
        ImageView imgvewPhoto1 =
                findViewById(convertView, R.id.imgvew_photo1_show);
        ImageView imgvewPhoto2 =
                findViewById(convertView, R.id.imgvew_photo2_show);
        ImageView imgvewPhoto3 =
                findViewById(convertView, R.id.imgvew_photo3_show);
        ImageView imgvewPhoto4 =
                findViewById(convertView, R.id.imgvew_photo4_show);

        TextView tvewReviewName =
                findViewById(convertView, R.id.tvew_review_name_show);
        TextView tvewReviewDesc =
                findViewById(convertView, R.id.tvew_review_desc_show);
        TextView tvewReviewTime =
                findViewById(convertView, R.id.tvew_review_time_show);

        ReviewItem review = (ReviewItem) mList.get(position);

        tvewReviewName.setText(review.username);
        tvewReviewDesc.setText(review.content);
        tvewReviewTime.setText(review.format_time);
        mImageLoader.displayImage(Endpoint.HOST + review.avatar,
                imgvewHeadPicture, options);

        imgvewPhoto1.setVisibility(View.GONE);
        imgvewPhoto2.setVisibility(View.GONE);
        imgvewPhoto3.setVisibility(View.GONE);
        imgvewPhoto4.setVisibility(View.GONE);
        if (null != review.images && review.images.length > 0) {
            for (int i = 0; i < review.images.length; i++) {
                if (i == 0 && !TextUtils.isEmpty(review.images[i])) {
                    mImageLoader.displayImage(Endpoint.HOST + review.images[i],
                            imgvewPhoto1, options);
                    imgvewPhoto1.setVisibility(View.VISIBLE);
                    imgvewPhoto1.setOnClickListener(new ImageClickListener(i,
                            review.images));
                } else if (i == 1 && !TextUtils.isEmpty(review.images[i])) {
                    mImageLoader.displayImage(Endpoint.HOST + review.images[i],
                            imgvewPhoto2, options);
                    imgvewPhoto2.setVisibility(View.VISIBLE);
                    imgvewPhoto2.setOnClickListener(new ImageClickListener(i,
                            review.images));
                } else if (i == 2 && !TextUtils.isEmpty(review.images[i])) {
                    mImageLoader.displayImage(Endpoint.HOST + review.images[i],
                            imgvewPhoto3, options);
                    imgvewPhoto3.setVisibility(View.VISIBLE);
                    imgvewPhoto3.setOnClickListener(new ImageClickListener(i,
                            review.images));
                } else if (i == 3 && !TextUtils.isEmpty(review.images[i])) {
                    mImageLoader.displayImage(Endpoint.HOST + review.images[i],
                            imgvewPhoto4, options);
                    imgvewPhoto4.setVisibility(View.VISIBLE);
                    imgvewPhoto4.setOnClickListener(new ImageClickListener(i,
                            review.images));
                }
            }
        }

        return convertView;
    }

    public class ImageClickListener implements OnClickListener {

        private int position = 0;

        private String[] urls;

        public ImageClickListener(int position, String[] urls) {
            this.position = position;
            this.urls = urls;
        }

        @Override
        public void onClick(View v) {
            String[] temUrls = new String[urls.length];
            for (int i = 0; i < urls.length; i++) {
                temUrls[i] = Endpoint.HOST + urls[i];
            }
            ImageViewPagerActivity.open(mContext, temUrls, position);
        }

    }
}