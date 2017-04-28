package com.android.juzbao.model.circle;

import java.util.List;

/**
 * Created by admin on 2017/3/17.
 */

public class DynamicBean {

    /**
     * code : 1
     * message : 操作成功
     * data : [{"id":"2","content":"好看啦啦","cover_ids":"739,738","video_id":"0","uid":"80","up_time":"前天14:33","is_img":1,"image_path":["/Uploads/Picture/2017-03-04/58ba5ec931989.jpg","/Uploads/Picture/2017-03-04/58ba5eaca2bd3.jpg"],"thumb_image_path":"","movie_path":"/Uploads/Movie/","avatat_img":"/Uploads/Picture/2017-03-02/58b77fb572c9c.jpg","nickname":"15318188253"},{"id":"3","content":"好看1","cover_ids":"739","video_id":"12","uid":"80","up_time":"03月20日 12:23","is_img":1,"is_mov":1,"image_path":["/Uploads/Picture/2017-03-04/58ba5ec931989.jpg"],"thumb_image_path":"/Uploads/Picture/2015-11-16/5649495497709.png","movie_path":"/Uploads/Movie/2015-11-22/5651355155a0d.flv","avatat_img":"/Uploads/Picture/2017-03-02/58b77fb572c9c.jpg","nickname":"15318188253"},{"id":"4","content":"好看2","cover_ids":"0","video_id":"0","uid":"81","up_time":"03月20日 12:23","image_path":[""],"thumb_image_path":"","movie_path":"/Uploads/Movie/","avatat_img":"","nickname":"18629581242"},{"id":"5","content":"好看4","cover_ids":"739","video_id":"0","uid":"81","up_time":"2016年03月21日","is_img":1,"image_path":["/Uploads/Picture/2017-03-04/58ba5ec931989.jpg"],"thumb_image_path":"","movie_path":"/Uploads/Movie/","avatat_img":"","nickname":"18629581242"}]
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
         * id : 2
         * content : 好看啦啦
         * cover_ids : 739,738
         * video_id : 0
         * uid : 80
         * up_time : 前天14:33
         * is_img : 1
         * image_path : ["/Uploads/Picture/2017-03-04/58ba5ec931989.jpg","/Uploads/Picture/2017-03-04/58ba5eaca2bd3.jpg"]
         * thumb_image_path :
         * movie_path : /Uploads/Movie/
         * avatat_img : /Uploads/Picture/2017-03-02/58b77fb572c9c.jpg
         * nickname : 15318188253
         * is_mov : 1
         */

        private String id;
        private String content;
        private String cover_ids;
        private String video_id;
        private String uid;
        private String up_time;
        private int is_img;
        private String thumb_image_path;
        private String movie_path;
        private String avatar_img;
        private String nickname;
        private int is_mov;
        private List<String> image_path;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getCover_ids() {
            return cover_ids;
        }

        public void setCover_ids(String cover_ids) {
            this.cover_ids = cover_ids;
        }

        public String getVideo_id() {
            return video_id;
        }

        public void setVideo_id(String video_id) {
            this.video_id = video_id;
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

        public int getIs_img() {
            return is_img;
        }

        public void setIs_img(int is_img) {
            this.is_img = is_img;
        }

        public String getThumb_image_path() {
            return thumb_image_path;
        }

        public void setThumb_image_path(String thumb_image_path) {
            this.thumb_image_path = thumb_image_path;
        }

        public String getMovie_path() {
            return movie_path;
        }

        public void setMovie_path(String movie_path) {
            this.movie_path = movie_path;
        }

        public String getAvatat_img() {
            return avatar_img;
        }

        public void setAvatat_img(String avatat_img) {
            this.avatar_img = avatat_img;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public int getIs_mov() {
            return is_mov;
        }

        public void setIs_mov(int is_mov) {
            this.is_mov = is_mov;
        }

        public List<String> getImage_path() {
            return image_path;
        }

        public void setImage_path(List<String> image_path) {
            this.image_path = image_path;
        }
    }
}
