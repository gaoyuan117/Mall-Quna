package com.server.api.model;

/**
 * Created by Koterwong on 2016/7/21.
 */

public class ReviewOrderMsgItem {
    public String id;
    public String comment_id;
    public String uid;
    public String is_saler;
    public String content;
    public String create_time;

    @Override public String toString() {
        return "ReviewOrderMsgItem{" +
                "id='" + id + '\'' +
                ", comment_id='" + comment_id + '\'' +
                ", uid='" + uid + '\'' +
                ", is_saler='" + is_saler + '\'' +
                ", content='" + content + '\'' +
                ", create_time='" + create_time + '\'' +
                '}';
    }
}
