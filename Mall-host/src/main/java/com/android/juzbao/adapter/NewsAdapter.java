
package com.android.juzbao.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.juzbao.activity.HelpDetailActivity_;
import com.android.juzbao.activity.NewsDetailActivity;
import com.android.juzbao.activity.NewsDetailActivity_;
import com.android.mall.resource.R;
import com.android.zcomponent.adapter.CommonAdapter;
import com.server.api.model.ArticleInfo;
import com.server.api.model.ArticleItem;
import com.server.api.model.NewsItem;

import java.util.List;

/**
 * <p>
 * Description: 二级列表项
 * </p>
 *
 * @ClassName:NewsAdapter
 * @author: wei
 * @date: 2015-11-10
 */
public class NewsAdapter extends CommonAdapter {

    public NewsAdapter(Context context, List<?> list) {
        super(context, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            convertView =
                    layoutInflater.inflate(R.layout.adapter_news, null);
        }
        NewsItem newsItem = (NewsItem)mList.get(position);
        TextView tvewReviewTitle =
                findViewById(convertView, R.id.tvew_title_show);
        tvewReviewTitle.setText(newsItem.title);
        convertView.setOnClickListener(new ContentClickListener(position));
        return convertView;
    }

    private class ContentClickListener implements OnClickListener {
        private int groupPosition;

        public ContentClickListener(int groupPosition) {
            this.groupPosition = groupPosition;
        }

        @Override
        public void onClick(View v) {
            final NewsItem category =
                    (NewsItem) mList.get(groupPosition);

            Bundle bundle = new Bundle();
            bundle.putString("id", category.id);
            intentToActivity(bundle, NewsDetailActivity_.class);
        }
    }
}