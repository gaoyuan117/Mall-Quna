/*
 * IndexBanner     2016/9/7-09-07
 * Copyright (c) 2016 KOTERWONG All right reserved
 */
package com.server.api.model.jifenmodel;

import com.server.api.model.BaseEntity;

/**
 * Created by Koterwong on 2016/9/7.
 * Desc:
 */
public class IndexBannerDetail extends BaseEntity {

    public Data[] data;

    public static class Data {
        public String id;
        public String title;
        public String content;
        public String create_time;
        public String image;
    }
}
