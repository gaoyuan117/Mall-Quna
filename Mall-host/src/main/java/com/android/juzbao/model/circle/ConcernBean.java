package com.android.juzbao.model.circle;

import java.util.List;

/**
 * Created by admin on 2017/3/18.
 */

public class ConcernBean {


    /**
     * code : 1
     * message : 操作成功
     * data : [{"id":"7","uid":"80","to_uid":"81","up_time":"1490169356","nickname":"18629581242","avatar_img":"/Uploads/Picture/2016-02-03/56b0d53b2a7b0.jpg","content":"小学班里有个小胖子，..."}]
     */

    private int code;
    private String message;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 7
         * uid : 80
         * to_uid : 81
         * up_time : 1490169356
         * nickname : 18629581242
         * avatar_img : /Uploads/Picture/2016-02-03/56b0d53b2a7b0.jpg
         * content : 小学班里有个小胖子，...
         */

        private String id;
        private String uid;
        private String to_uid;
        private String up_time;
        private String nickname;
        private String avatar_img;
        private String content;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getTo_uid() {
            return to_uid;
        }

        public void setTo_uid(String to_uid) {
            this.to_uid = to_uid;
        }

        public String getUp_time() {
            return up_time;
        }

        public void setUp_time(String up_time) {
            this.up_time = up_time;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getAvatar_img() {
            return avatar_img;
        }

        public void setAvatar_img(String avatar_img) {
            this.avatar_img = avatar_img;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
