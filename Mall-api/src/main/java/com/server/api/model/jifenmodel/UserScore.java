package com.server.api.model.jifenmodel;

import com.server.api.model.BaseEntity;

public class UserScore extends BaseEntity {

    public Data data;

    public static class Data {
        public String score;
    }

}
