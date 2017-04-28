package com.android.juzbao.model.circle;

import com.google.gson.annotations.SerializedName;

/**
 * Created by admin on 2017/3/27.
 */

public class JpushBean {

    /**
     * content_type : text
     * inv_to_id : 92
     * 0 : jiguang
     */

    private String content_type;
    private String inv_to_id;
    @SerializedName("0")
    private String _$0;

    public String getContent_type() {
        return content_type;
    }

    public void setContent_type(String content_type) {
        this.content_type = content_type;
    }

    public String getInv_to_id() {
        return inv_to_id;
    }

    public void setInv_to_id(String inv_to_id) {
        this.inv_to_id = inv_to_id;
    }

    public String get_$0() {
        return _$0;
    }

    public void set_$0(String _$0) {
        this._$0 = _$0;
    }
}
