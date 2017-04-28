
package com.android.juzbao.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.mall.resource.R;
import com.android.zcomponent.adapter.CommonAdapter;
import com.android.zcomponent.http.api.host.Endpoint;
import com.server.api.model.ProductCategory;

import java.util.List;

/**
 * <p>
 * Description:
 * </p>
 * 
 * @ClassName:HomeGroupAdapter
 * @author: wei
 * @date: 2015-11-10
 * 
 */
public class HomeGroupAdapter extends CommonAdapter
{

	private int miSelectPosition;

	public HomeGroupAdapter(Context context, List<?> list)
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
					layoutInflater.inflate(R.layout.adapter_home_group, null);
		}

		//分类图片
		ImageView imgvewMark =
				findViewById(convertView, R.id.imgvew_group_image_show);
		TextView tvewReviewTitle =
				findViewById(convertView, R.id.tvew_group_name_show);

		final ProductCategory.CategoryItem category =
				(ProductCategory.CategoryItem) mList.get(position);

		if (category.imageId > 0){
			imgvewMark.setImageResource(category.imageId);
		}else{
			loadImage(Endpoint.HOST + category.image, imgvewMark);
		}
		tvewReviewTitle.setText(category.name);
		return convertView;
	}
}