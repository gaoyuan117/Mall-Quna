package com.android.juzbao.model.circle;

/**
 * Created by admin on 2017/3/24.
 */

public class DemandBean {

    /**
     * code : 1
     * message : 申请应邀成功
     * data : {"info":5,"money":"0.01"}
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
         * info : 5
         * money : 0.01
         */

        private int invite_id;
        private String money;

        public int getInfo() {
            return invite_id;
        }

        public void setInfo(int info) {
            this.invite_id = info;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }
    }
}
