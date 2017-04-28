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
 * Description:  社区分类列表项 
 * </p>
 * 
 * @ClassName:CommunityCategoryAdapter
 * @author: wei
 * @date: 2015-11-10
 * 
 */
public class CommunityCategoryAdapter extends CommonAdapter
{
    public CommunityCategoryAdapter(Context context, List<?> list)
    {
        super(context, list);
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (null == convertView)
        {
            convertView = layoutInflater.inflate(
                R.layout.adapter_community_category, null);
        }
        
        ImageView imgvewPhoto = 
            findViewById(convertView, R.id.imgvew_photo_show);
        TextView tvewReviewTitle = 
            findViewById(convertView, R.id.tvew_review_title_show);

        

        return convertView;
    }
}