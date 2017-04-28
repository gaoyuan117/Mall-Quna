
package com.android.juzbao.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.zcomponent.http.api.host.Endpoint;
import com.server.api.model.GiftCategory.GiftCategoryItem;
import com.android.mall.resource.R;
import com.android.zcomponent.adapter.CommonAdapter;
import com.android.zcomponent.util.CommonUtil;

/**
 * <p>
 * Description:
 * </p>
 * 
 * @ClassName:GiftCategoryAdapter
 * @author: wei
 * @date: 2015-11-10
 * 
 */
public class GiftCategoryAdapter extends CommonAdapter
{

	public GiftCategoryAdapter(Context context, List<?> list)
	{
		super(context, list);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent)
	{
		if (null == convertView)
		{
			convertView =
					layoutInflater.inflate(R.layout.adapter_select_gift_images,
							null);
		}

		ImageView imgvewPhoto = findViewById(convertView, R.id.imgvew_category);

		RelativeLayout imgvewPhotoMask =
				findViewById(convertView, R.id.llayout_category_mask);

		if (position == getSelectPosition())
		{
			imgvewPhotoMask.setVisibility(View.VISIBLE);
		}
		else
		{
			imgvewPhotoMask.setVisibility(View.GONE);
		}
		
		final GiftCategoryItem category =
				(GiftCategoryItem) mList.get(position);
		mImageLoader.displayImage(Endpoint.HOST + category.image, imgvewPhoto,
				options);
		
		imgvewPhoto.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				if (!CommonUtil.isLeastSingleClick())
				{
					return;
				}
				if (null != face)
				{
					face.onClickSelect(position);
				}
			}
		});
		
		imgvewPhotoMask.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				if (!CommonUtil.isLeastSingleClick())
				{
					return;
				}
				if (null != face)
				{
					face.onClickSelect(position);
				}
			}
		});
		
		return convertView;
	}
	
	CallBackInteface face;

	public void setCallBackInteface(CallBackInteface cbif)
	{
		this.face = cbif;
	}

	public interface CallBackInteface
	{

		public void onClickSelect(int position);
	}
	
}