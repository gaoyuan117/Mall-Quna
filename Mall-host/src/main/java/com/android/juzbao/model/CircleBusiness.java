package com.android.juzbao.model;

import com.android.juzbao.activity.circle.DynamicDetailActivity;
import com.android.juzbao.model.circle.DynamicBean;
import com.android.juzbao.model.circle.DynamicDetailBean;
import com.android.juzbao.model.circle.ZanBean;
import com.android.zcomponent.http.HttpDataLoader;
import com.server.api.model.CommonReturn;
import com.server.api.service.CircleService;

/**
 * Created by admin on 2017/3/22.
 */

public class CircleBusiness {

    public static void commitDynamic(HttpDataLoader httpDataLoader) {
        CircleService.commitDynamic upPicture = new CircleService.commitDynamic();
        httpDataLoader.doPostProcess(upPicture,CommonReturn.class);
    }

    public static void getAllDynamic(HttpDataLoader httpDataLoader) {
        CircleService.getAllDynamic upPicture = new CircleService.getAllDynamic();
        httpDataLoader.doPostProcess(upPicture,DynamicBean.class);
    }

    public static void loadDynamicDetail(HttpDataLoader httpDataLoader) {
        CircleService.getAllDynamic upPicture = new CircleService.getAllDynamic();
        httpDataLoader.doPostProcess(upPicture, ZanBean.class);
    }

    public static void comment(HttpDataLoader httpDataLoader) {
        CircleService.getAllDynamic upPicture = new CircleService.getAllDynamic();
        httpDataLoader.doPostProcess(upPicture, CommonReturn.class);
    }

}