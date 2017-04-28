
package com.android.juzbao.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.mall.resource.R;
import com.android.zcomponent.adapter.CommonExpandabelAdapter;
import com.server.api.model.ProductCategory;

import java.util.List;

/**
 * <p>
 * Description: 分类一级列表项
 * </p>
 * 
 * @ClassName:Category1Adapter
 * @author: wei
 * @date: 2015-11-10
 * 
 */
public class Category1Adapter extends CommonExpandabelAdapter
{

	private int miSelectPosition;

	private int miChildSelectPosition = -1;

	public Category1Adapter(Context context, List<?> list)
	{
		super(context, list);
	}

	public void setSelectPosition(int position)
	{
		miSelectPosition = position;
		notifyDataSetChanged();
	}

	public void setChildSelectPosition(int position)
	{
		miChildSelectPosition = position;
		notifyDataSetChanged();
	}

	public int getChildSelectPosition()
	{
		return miChildSelectPosition;
	}

	@Override
	public int getChildrenCount(int groupPosition)
	{
		if (null == ((ProductCategory.CategoryItem) mList.get(groupPosition))._child)
		{
			return 0;
		}
		return ((ProductCategory.CategoryItem) mList.get(groupPosition))._child.size();
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
							 View convertView, ViewGroup parent)
	{
		if (null == convertView)
		{
			convertView = layoutInflater.inflate(R.layout.adapter_category1, null);
		}

		LinearLayout llayoutBg =
				findViewById(convertView, R.id.llayout_category_bg_show);
		TextView tvewReviewTitle =
				findViewById(convertView, R.id.tvew_review_title_show);

		ProductCategory.CategoryItem category =
				(ProductCategory.CategoryItem) mList.get(groupPosition);
		tvewReviewTitle.setText(category.name);
		llayoutBg.setBackgroundColor(getColor(R.color.gray_white));
		if (miSelectPosition == groupPosition)
		{
			tvewReviewTitle.setTextColor(getColor(R.color.red));
		}
		else
		{
			tvewReviewTitle.setTextColor(getColor(R.color.black));
		}
		return convertView;
	}

	@Override
	public View getChildView(final int groupPosition, final int childPosition,
							 boolean isLastChild, View convertView, ViewGroup parent)
	{
		if (null == convertView)
		{
			convertView =
					layoutInflater.inflate(R.layout.adapter_category1, null);
		}

		LinearLayout llayoutBg =
				(LinearLayout)findViewById(convertView, R.id.llayout_category_bg_show);
		TextView tvewReviewTitle =
				(TextView) findViewById(convertView, R.id.tvew_review_title_show);

//		LogEx.d("MMM",((ProductCategory.CategoryItem)mList.get(groupPosition)).toString());


		ProductCategory.CategoryItem category =
				(ProductCategory.CategoryItem) mList.get(groupPosition);
		if (null != category._child && category._child.size() > childPosition){
			ProductCategory.CategoryItem child = category._child.get(childPosition);
			tvewReviewTitle.setText(child.title);
		}else{
			tvewReviewTitle.setText("");
		}

		if (childPosition == miChildSelectPosition){
			llayoutBg.setBackgroundColor(getColor(R.color.gray_white_tra));
		}else{
			llayoutBg.setBackgroundColor(getColor(R.color.white));
		}

		convertView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mListener.onChildClick(null, null, groupPosition, childPosition, 0);
			}
		});
		return convertView;
	}

	private ExpandableListView.OnChildClickListener mListener;

	public void setOnChildClickListener(ExpandableListView.OnChildClickListener listener){
		mListener = listener;
	}
}