package com.android.juzbao.model.circle;

import java.util.List;

/**
 * Created by admin on 2017/3/24.
 */

public class MyDynamicBean {

    /**
     * code : 1
     * message : 操作成功
     * data : {"list":[{"id":"10","content":"好看","cover_ids":"11","video_id":"11","uid":"80","up_time":"昨天","is_img":1,"is_mov":1,"image_path":["/Uploads/Picture/2015-11-16/56497f0d8158f.png"],"thumb_image_path":"","movie_path":"/Uploads/Movie/"},{"id":"2","content":"好看啦啦","cover_ids":"739,738","video_id":"0","uid":"80","up_time":"昨天","is_img":1,"image_path":["/Uploads/Picture/2017-03-04/58ba5ec931989.jpg","/Uploads/Picture/2017-03-04/58ba5eaca2bd3.jpg"],"thumb_image_path":"","movie_path":"/Uploads/Movie/"},{"id":"3","content":"好看1","cover_ids":"739","video_id":"12","uid":"80","up_time":"2017-03-20","is_img":1,"is_mov":1,"image_path":["/Uploads/Picture/2017-03-04/58ba5ec931989.jpg"],"thumb_image_path":"/Uploads/Picture/2015-11-16/5649495497709.png","movie_path":"/Uploads/Movie/2015-11-22/5651355155a0d.flv"},{"id":"7","content":"好看6","cover_ids":"739","video_id":"0","uid":"80","up_time":"2017-03-19","is_img":1,"image_path":["/Uploads/Picture/2017-03-04/58ba5ec931989.jpg"],"thumb_image_path":"","movie_path":"/Uploads/Movie/"}],"user":{"nickname":"15318188253","avatar":"737","avatat_img":"/Uploads/Picture/2017-03-02/58b77fb572c9c.jpg"}}
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
         * list : [{"id":"10","content":"好看","cover_ids":"11","video_id":"11","uid":"80","up_time":"昨天","is_img":1,"is_mov":1,"image_path":["/Uploads/Picture/2015-11-16/56497f0d8158f.png"],"thumb_image_path":"","movie_path":"/Uploads/Movie/"},{"id":"2","content":"好看啦啦","cover_ids":"739,738","video_id":"0","uid":"80","up_time":"昨天","is_img":1,"image_path":["/Uploads/Picture/2017-03-04/58ba5ec931989.jpg","/Uploads/Picture/2017-03-04/58ba5eaca2bd3.jpg"],"thumb_image_path":"","movie_path":"/Uploads/Movie/"},{"id":"3","content":"好看1","cover_ids":"739","video_id":"12","uid":"80","up_time":"2017-03-20","is_img":1,"is_mov":1,"image_path":["/Uploads/Picture/2017-03-04/58ba5ec931989.jpg"],"thumb_image_path":"/Uploads/Picture/2015-11-16/5649495497709.png","movie_path":"/Uploads/Movie/2015-11-22/5651355155a0d.flv"},{"id":"7","content":"好看6","cover_ids":"739","video_id":"0","uid":"80","up_time":"2017-03-19","is_img":1,"image_path":["/Uploads/Picture/2017-03-04/58ba5ec931989.jpg"],"thumb_image_path":"","movie_path":"/Uploads/Movie/"}]
         * user : {"nickname":"15318188253","avatar":"737","avatat_img":"/Uploads/Picture/2017-03-02/58b77fb572c9c.jpg"}
         */

        private UserBean user;
        private List<ListBean> list;
        private int is_follow;
        private int is_me;

        public int getIs_me() {
            return is_me;
        }

        public void setIs_me(int is_me) {
            this.is_me = is_me;
        }

        public int getIs_follow() {
            return is_follow;
        }

        public void setIs_follow(int is_follow) {
            this.is_follow = is_follow;
        }

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class UserBean {
            /**
             * nickname : 15318188253
             * avatar : 737
             * avatat_img : /Uploads/Picture/2017-03-02/58b77fb572c9c.jpg
             */

            private String nickname;
            private String avatar;
            private String avatar_img;

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public String getAvatat_img() {
                return avatar_img;
            }

            public void setAvatat_img(String avatat_img) {
                this.avatar_img = avatat_img;
            }
        }

        public static class ListBean {
            /**
             * id : 10
             * content : 好看
             * cover_ids : 11
             * video_id : 11
             * uid : 80
             * up_time : 昨天
             * is_img : 1
             * is_mov : 1
             * image_path : ["/Uploads/Picture/2015-11-16/56497f0d8158f.png"]
             * thumb_image_path :
             * movie_path : /Uploads/Movie/
             */

            private String id;
            private String content;
            private String cover_ids;
            private String video_id;
            private String uid;
            private String up_time;
            private int is_img;
            private int is_mov;
            private String thumb_image_path;
            private String movie_path;
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

            public int getIs_mov() {
                return is_mov;
            }

            public void setIs_mov(int is_mov) {
                this.is_mov = is_mov;
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

            public List<String> getImage_path() {
                return image_path;
            }

            public void setImage_path(List<String> image_path) {
                this.image_path = image_path;
            }
        }
    }
}
