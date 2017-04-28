package com.server.api.model.jifenmodel;

import com.server.api.model.BaseEntity;

public class OrderMergeReturn extends BaseEntity{

    public Data data;
    public static class Data{
        public String order_no;
        public String price;
        public String name;
        public String order_ids;
        public String time;
        public String pay_status;
    }
}
