package com.android.juzbao.model.circle;

/**
 * Created by admin on 2017/3/23.
 */

public class ZanBean {

    /**
     * code : 1
     * message : 操作成功
     * data : {"thumbs_cnt":"1","is_thumbs_up":1}
     */

    private int code;
    private String message;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * thumbs_cnt : 1
         * is_thumbs_up : 1
         */

        private String thumbs_cnt;
        private int is_thumbs_up;

        public String getThumbs_cnt() {
            return thumbs_cnt;
        }

        public void setThumbs_cnt(String thumbs_cnt) {
            this.thumbs_cnt = thumbs_cnt;
        }

        public int getIs_thumbs_up() {
            return is_thumbs_up;
        }

        public void setIs_thumbs_up(int is_thumbs_up) {
            this.is_thumbs_up = is_thumbs_up;
        }
    }
}
