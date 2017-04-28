package com.server.api.model.jifenmodel;


import com.server.api.model.BaseEntity;

/**
 * Created by Administrator on 2016/8/25.
 */
public class PlatformOrderReturn extends BaseEntity {

    public Data data;

    public static class Data {
        public String id;
        public String order_no;
    }
}
