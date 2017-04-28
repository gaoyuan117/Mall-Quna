package com.server.api.model.jifenmodel;

import com.google.gson.annotations.SerializedName;
import com.server.api.model.BaseEntity;

import java.io.Serializable;

/**
 * Created by Koterwong on 2016/7/29.
 * 便民信息详情
 */

public class EasyPeopleDetail  extends BaseEntity implements Serializable {

    @SerializedName("data")
    public Data data;

    public static class Data{
        public String id;
        public String title;
        public String thumb;
        public String content;
        public String add_time;
        public String update_time;
    }
}
