package com.android.juzbao.model.circle;

import java.util.List;

/**
 * Created by admin on 2017/3/25.
 */

public class DemandDetailBean {

    /**
     * code : 1
     * message : 操作成功
     * data : {"detail":{"id":"8","demand":"看电影","number":"5","description":"4D电影","place":"百丰大厦","money":"100.00","uid":"80","eff_time":"3","up_time":"1490321841","pay_status":"1","end_date":"2017-03-27","nickname":"15318188253","avatar_img":"/Uploads/Picture/2017-03-02/58b77fb572c9c.jpg","ying_cnt":"2","is_invite":1},"ying_list":[{"id":"1","inv_id":"8","content":"技能\n","uid":"81","up_time":"1490324534","status":"0","anonymity":"1","nickname":"18629581242","avatar_img":"/Uploads/Picture/2017-02-24/58afd84b97c18.jpg","is_name":1},{"id":"2","inv_id":"8","content":"技能\n","uid":"80","up_time":"1490324634","status":"0","anonymity":"2","nickname":"某某","avatar_img":"","is_name":2}]}
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
         * detail : {"id":"8","demand":"看电影","number":"5","description":"4D电影","place":"百丰大厦","money":"100.00","uid":"80","eff_time":"3","up_time":"1490321841","pay_status":"1","end_date":"2017-03-27","nickname":"15318188253","avatar_img":"/Uploads/Picture/2017-03-02/58b77fb572c9c.jpg","ying_cnt":"2","is_invite":1}
         * ying_list : [{"id":"1","inv_id":"8","content":"技能\n","uid":"81","up_time":"1490324534","status":"0","anonymity":"1","nickname":"18629581242","avatar_img":"/Uploads/Picture/2017-02-24/58afd84b97c18.jpg","is_name":1},{"id":"2","inv_id":"8","content":"技能\n","uid":"80","up_time":"1490324634","status":"0","anonymity":"2","nickname":"某某","avatar_img":"","is_name":2}]
         */

        private DetailBean detail;
        private List<YingListBean> ying_list;

        public DetailBean getDetail() {
            return detail;
        }

        public void setDetail(DetailBean detail) {
            this.detail = detail;
        }

        public List<YingListBean> getYing_list() {
            return ying_list;
        }

        public void setYing_list(List<YingListBean> ying_list) {
            this.ying_list = ying_list;
        }

        public static class DetailBean {
            /**
             * id : 8
             * demand : 看电影
             * number : 5
             * description : 4D电影
             * place : 百丰大厦
             * money : 100.00
             * uid : 80
             * eff_time : 3
             * up_time : 1490321841
             * pay_status : 1
             * end_date : 2017-03-27
             * nickname : 15318188253
             * avatar_img : /Uploads/Picture/2017-03-02/58b77fb572c9c.jpg
             * ying_cnt : 2
             * is_invite : 1
             */

            private String id;
            private String demand;
            private String number;
            private String description;
            private String place;
            private String money;
            private String uid;
            private String eff_time;
            private String up_time;
            private String pay_status;
            private String end_date;
            private String nickname;
            private String avatar_img;
            private String ying_cnt;
            private int is_invite;
            private int is_own;

            public int getIs_own() {
                return is_own;
            }

            public void setIs_own(int is_own) {
                this.is_own = is_own;
            }

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

            public String getUid() {
                return uid;
            }

            public void setUid(String uid) {
                this.uid = uid;
            }

            public String getEff_time() {
                return eff_time;
            }

            public void setEff_time(String eff_time) {
                this.eff_time = eff_time;
            }

            public String getUp_time() {
                return up_time;
            }

            public void setUp_time(String up_time) {
                this.up_time = up_time;
            }

            public String getPay_status() {
                return pay_status;
            }

            public void setPay_status(String pay_status) {
                this.pay_status = pay_status;
            }

            public String getEnd_date() {
                return end_date;
            }

            public void setEnd_date(String end_date) {
                this.end_date = end_date;
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

            public int getIs_invite() {
                return is_invite;
            }

            public void setIs_invite(int is_invite) {
                this.is_invite = is_invite;
            }
        }

        public static class YingListBean {
            /**
             * id : 1
             * inv_id : 8
             * content : 技能

             * uid : 81
             * up_time : 1490324534
             * status : 0
             * anonymity : 1
             * nickname : 18629581242
             * avatar_img : /Uploads/Picture/2017-02-24/58afd84b97c18.jpg
             * is_name : 1
             */

            private String id;
            private String inv_id;
            private String content;
            private String uid;
            private String up_time;
            private String status;
            private String anonymity;
            private String nickname;
            private String avatar_img;
            private int is_name;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getInv_id() {
                return inv_id;
            }

            public void setInv_id(String inv_id) {
                this.inv_id = inv_id;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getUid() {
                return uid;
            }

            public void setUid(String uid) {
                this.uid = uid;
            }

            public String getUp_time() {
                return up_time;
            }

            public void setUp_time(String up_time) {
                this.up_time = up_time;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getAnonymity() {
                return anonymity;
            }

            public void setAnonymity(String anonymity) {
                this.anonymity = anonymity;
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

            public int getIs_name() {
                return is_name;
            }

            public void setIs_name(int is_name) {
                this.is_name = is_name;
            }
        }
    }
}
