package com.server.api.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Koterwong on 2016/7/21.
 */

public class ReviewOrderResult extends BaseEntity {

    @SerializedName("data")
    public ReviewOrderItem []Data;

}
