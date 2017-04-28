
package com.android.juzbao.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.zcomponent.http.api.host.Endpoint;
import com.server.api.model.CourseItem;
import com.android.juzbao.provider.R;
import com.android.video.activity.VideoActivity;
import com.android.zcomponent.adapter.CommonAdapter;
import com.android.zcomponent.util.MyLayoutAdapter;

/**
 * <p>
 * Description:
 * </p>
 * 
 * @ClassName:CourseDocAdapter
 * @author: wei
 * @date: 2015-11-10
 * 
 */
public class CourseVideoAdapter extends CommonAdapter
{

	private int itemWidth;

	public CourseVideoAdapter(Context context, List<?> list)
	{
		super(context, list);

		itemWidth =
				(int) (MyLayoutAdapter.getInstance().getScreenWidth() - 20 * MyLayoutAdapter
						.getInstance().getDensityRatio()) / 2;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		if (null == convertView)
		{
			convertView =
					layoutInflater.inflate(R.layout.adapter_course_video, null);
			convertView.setLayoutParams(new AbsListView.LayoutParams(itemWidth, itemWidth));
		}

		TextView tvewTitle = findViewById(convertView, R.id.tvew_title_show);
		ImageView imgvewPhoto =
				findViewById(convertView, R.id.imgvew_photo_show);

		final CourseItem courseItem = (CourseItem) mList.get(position);

		tvewTitle.setText(courseItem.title);
		loadImage(Endpoint.HOST + courseItem.thumb_cover_path, imgvewPhoto);
		convertView.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				String videoUrl = Endpoint.HOST + courseItem.movie_path;
				String thumbnail = Endpoint.HOST + courseItem.thumb_cover_path;
				VideoActivity.open(mContext, videoUrl, thumbnail);
			}
		});
		return convertView;
	}
}