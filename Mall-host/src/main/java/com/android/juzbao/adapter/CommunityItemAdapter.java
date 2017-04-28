package com.android.juzbao.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.mall.resource.R;
import com.android.zcomponent.adapter.CommonAdapter;


/**
 * <p>
 * Description:  社区列表项 
 * </p>
 * 
 * @ClassName:CommunityItemAdapter
 * @author: wei
 * @date: 2015-11-10
 * 
 */
public class CommunityItemAdapter extends CommonAdapter
{
    public CommunityItemAdapter(Context context, List<?> list)
    {
        super(context, list);
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (null == convertView)
        {
            convertView = layoutInflater.inflate(
                R.layout.adapter_community_item, null);
        }
        
        ImageView imgvewHeadPicture = 
            findViewById(convertView, R.id.imgvew_head_picture_show);
        ImageView imgvewPhoto1 = 
            findViewById(convertView, R.id.imgvew_photo1_show);
        ImageView imgvewPhoto2 = 
            findViewById(convertView, R.id.imgvew_photo2_show);
        ImageView imgvewPhoto3 = 
            findViewById(convertView, R.id.imgvew_photo3_show);
        TextView tvewReviewName = 
            findViewById(convertView, R.id.tvew_review_name_show);
        TextView tvewReviewTime = 
            findViewById(convertView, R.id.tvew_review_time_show);
        TextView tvewReviewDesc = 
            findViewById(convertView, R.id.tvew_review_desc_show);

        

        return convertView;
    }
}