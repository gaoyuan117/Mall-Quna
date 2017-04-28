package com.android.juzbao.model.circle;

import java.util.List;

/**
 * Created by admin on 2017/3/24.
 */

public class AllDemandBean {

    /**
     * code : 1
     * message : 操作成功
     * data : [{"id":"8","demand":"看电影","number":"5","description":"4D电影","place":"百丰大厦","money":"100.00","nickname":"15318188253","avatar_img":"/Uploads/Picture/2017-03-02/58b77fb572c9c.jpg","ying_cnt":"2","end_date":"2017-03-27"}]
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
         * id : 8
         * demand : 看电影
         * number : 5
         * description : 4D电影
         * place : 百丰大厦
         * money : 100.00
         * nickname : 15318188253
         * avatar_img : /Uploads/Picture/2017-03-02/58b77fb572c9c.jpg
         * ying_cnt : 2
         * end_date : 2017-03-27
         */

        private String id;
        private String demand;
        private String number;
        private String description;
        private String place;
        private String money;
        private String nickname;
        private String avatar_img;
        private String ying_cnt;
        private String end_date;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getDemand() {
            return demand;
        }

        public void setDemand(String demand) {
            this.demand = demand;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getPlace() {
            return place;
        }

        public void setPlace(String place) {
            this.place = place;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
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

        public String getYing_cnt() {
            return ying_cnt;
        }

        public void setYing_cnt(String ying_cnt) {
            this.ying_cnt = ying_cnt;
        }

        public String getEnd_date() {
            return end_date;
        }

        public void setEnd_date(String end_date) {
            this.end_date = end_date;
        }
    }
}
