
package com.android.juzbao.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.zcomponent.http.api.host.Endpoint;
import com.server.api.model.ProductFavorite;
import com.android.mall.resource.R;
import com.android.zcomponent.adapter.CommonAdapter;
import com.android.zcomponent.util.CommonUtil;
import com.android.zcomponent.util.StringUtil;

/**
 * <p>
 * Description: 收藏商品列表项
 * </p>
 * 
 * @ClassName:FavoriteProductAdapter
 * @author: wei
 * @date: 2015-11-10
 * 
 */
public class FavoriteProductAdapter extends CommonAdapter
{

	private boolean isEdit = false;

	public FavoriteProductAdapter(Context context, List<?> list)
	{
		super(context, list);
	}

	public void setEdit()
	{
		this.isEdit = !isEdit;
		this.notifyDataSetChanged();
	}
	
	public void setEdit(boolean isEdit)
	{
		this.isEdit = isEdit;
		this.notifyDataSetChanged();
	}

	public boolean isEdit()
	{
		return isEdit;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent)
	{
		if (null == convertView)
		{
			convertView =
					layoutInflater.inflate(R.layout.adapter_favorite_product,
							null);
		}

		ImageView imgvewPhoto =
				findViewById(convertView, R.id.imgvew_photo_show);

		Button imgvewDel = findViewById(convertView, R.id.favorite_del);

		TextView tvewProductName =
				findViewById(convertView, R.id.tvew_product_name_show);
		TextView tvewProductNowPrice =
				findViewById(convertView, R.id.tvew_product_now_price_show);

		if (isEdit)
		{
			imgvewDel.setVisibility(View.VISIBLE);
		}
		else
		{
			imgvewDel.setVisibility(View.GONE);
		}

		final ProductFavorite favorite = (ProductFavorite) mList.get(position);
		tvewProductName.setText(favorite.product_title);
		tvewProductNowPrice.setText("¥"
				+ StringUtil.formatProgress(favorite.product_price));
		mImageLoader.displayImage(Endpoint.HOST + favorite.image, imgvewPhoto,
				options);

		imgvewDel.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				if (!CommonUtil.isLeastSingleClick())
				{
					return;
				}

				if (null != mDeleteClickListener)
				{
					mDeleteClickListener.onClickDelete(position);
				}
			}
		});

		convertView.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				if (!CommonUtil.isLeastSingleClick())
				{
					return;
				}

				if (null != mDeleteClickListener)
				{
					mDeleteClickListener.onClickSelect(position);
				}
			}
		});
		return convertView;
	}

	private OnItemActionClickListener mDeleteClickListener;

	public void setOnItemActionClickListener(
			OnItemActionClickListener deleteClickListener)
	{
		mDeleteClickListener = deleteClickListener;
	}

	public interface OnItemActionClickListener
	{

		public void onClickDelete(int position);

		public void onClickSelect(int position);
	}
}