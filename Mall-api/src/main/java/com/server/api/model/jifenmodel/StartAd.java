package com.server.api.model.jifenmodel;

import com.server.api.model.BaseEntity;

/**
 * Created by Koterwong on 2016/7/28.
 * 程序启动广告
 */
public class StartAd extends BaseEntity {

    public Data data;

    public static class Data {
        public String title;
        public String content;
        public String logo;
        public int status;
    }
}
