package com.android.juzbao.utils;

import android.content.Context;
import android.widget.ImageView;

import com.android.juzbao.application.BaseApplication;
import com.android.quna.activity.R;
import com.android.zcomponent.http.api.host.Endpoint;
import com.bumptech.glide.Glide;

/**
 * Created by admin on 2017/3/17.
 */

public class GlideUtil {

    public static void glide(String imgPath, ImageView imageView) {
        Glide.with(BaseApplication.app)
                .load(Endpoint.IMAGE + imgPath)
                .error(R.drawable.ease_default_image)
                .into(imageView);
    }
}
