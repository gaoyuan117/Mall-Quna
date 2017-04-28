
package com.android.juzbao.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.juzbao.activity.HelpDetailActivity_;
import com.android.mall.resource.R;
import com.android.zcomponent.adapter.CommonAdapter;
import com.server.api.model.ArticleInfo;
import com.server.api.model.ArticleItem;

import java.util.List;

/**
 * <p>
 * Description: 二级列表项
 * </p>
 *
 * @ClassName:ArticlesAdapter
 * @author: wei
 * @date: 2015-11-10
 */
public class ArticlesAdapter extends CommonAdapter {

  public ArticlesAdapter(Context context, List<?> list) {
    super(context, list);
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    if (null == convertView) {
      convertView =
          layoutInflater.inflate(R.layout.adapter_articles, null);
    }

    TextView tvewReviewTitle =
        findViewById(convertView, R.id.tvew_title_show);

    LinearLayout llayoutContent =
        findViewById(convertView, R.id.llayout_content);

    final ArticleItem category =
        (ArticleItem) mList.get(position);
    if (category._child != null)
      getContentView(position, llayoutContent, category._child);
    tvewReviewTitle.setText(category.title);

    return convertView;
  }

  private void getContentView(int position, LinearLayout linearLayouts, ArticleInfo[]
      articleInfos) {
    linearLayouts.removeAllViews();

    for (int i = 0; i < articleInfos.length; i++) {
      View view =
          layoutInflater.inflate(R.layout.adapter_articles_child, null);
      TextView tvewReviewTitle =
          (TextView) view.findViewById(R.id.tvew_title_show);
      tvewReviewTitle.setText(articleInfos[i].title);
      view.setOnClickListener(new ContentClickListener(position, i));
      linearLayouts.addView(view);
    }
  }

  private class ContentClickListener implements OnClickListener {
    private int groupPosition;
    private int childPosition;

    public ContentClickListener(int groupPosition, int childPosition) {
      this.groupPosition = groupPosition;
      this.childPosition = childPosition;
    }

    @Override
    public void onClick(View v) {
      final ArticleItem category =
          (ArticleItem) mList.get(groupPosition);

      Bundle bundle = new Bundle();
      bundle.putString("id", category._child[childPosition].id);
      intentToActivity(bundle, HelpDetailActivity_.class);
    }
  }
}