package com.server.api.model.jifenmodel;

import com.server.api.model.BaseEntity;

/**
 * Created by Administrator on 2016/8/26.
 */
public class ShopScore extends BaseEntity {

    public Data data;

    public static class Data {
        public String give_score;
        public String recover_score;
        public String uid;
        public String id;
        public String score;
    }
}
