
package com.android.juzbao.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.mall.resource.R;
import com.android.zcomponent.adapter.CommonAdapter;
import com.android.zcomponent.http.api.host.Endpoint;
import com.android.zcomponent.util.MyLayoutAdapter;
import com.server.api.model.ProductCategory;

import java.util.List;

/**
 * <p>
 * Description: 二级列表项
 * </p>
 * 
 * @ClassName:CommunityCategoryAdapter
 * @author: wei
 * @date: 2015-11-10
 * 
 */
public class Category3Adapter extends CommonAdapter
{
	private int miSelectPosition = -1;

	public Category3Adapter(Context context, List<?> list)
	{
		super(context, list);
	}

	public void setSelectPosition(int position)
	{
		miSelectPosition = position;
		notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		if (null == convertView)
		{
			convertView =
					layoutInflater.inflate(R.layout.adapter_category3, null);
		}

		TextView tvewReviewTitle =
				findViewById(convertView, R.id.tvew_title_show);
		final ProductCategory.CategoryItem category =
				(ProductCategory.CategoryItem) mList.get(position);
		tvewReviewTitle.setText(category.title);
		if (miSelectPosition == position)
		{
			tvewReviewTitle.setTextColor(getColor(R.color.red));
		}
		else
		{
			tvewReviewTitle.setTextColor(getColor(R.color.black));
		}
		return convertView;
	}
}