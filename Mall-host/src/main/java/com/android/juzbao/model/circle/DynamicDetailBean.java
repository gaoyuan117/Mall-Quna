package com.android.juzbao.model.circle;

import java.util.List;

/**
 * Created by admin on 2017/3/23.
 */

public class DynamicDetailBean {


    /**
     * code : 1
     * message : 操作成功
     * data : {"post_content":{"id":"2","content":"好看啦啦","cover_ids":"739,738","video_id":"0","uid":"80","up_time":"1490078002","nickname":"15318188253","avatar_img":"/Uploads/Picture/2016-02-03/56b0d52b8e940.jpg","is_follow":0,"is_thumbs_up":1,"thumbs_up_cnt":"2","comment_cnt":"2"},"comment":[{"id":"2","content":"很好看2","uid":"81","post_id":"2","up_time":"56分钟前","nickname":"18629581242","avatar_img":"/Uploads/Picture/2016-02-03/56b0d53b2a7b0.jpg","is_thumbs_up":1,"thumbs_up_cnt":"1","reply_cnt":"2","reply":[{"id":"3","content":"teienwa","com_id":"2","uid":"83","up_time":"7分钟前","nickname":"18786284971","avatar_img":"/Uploads/Picture/2016-02-17/56c3f01175d04.jpg"},{"id":"4","content":"12321","com_id":"2","uid":"83","up_time":"6分钟前","nickname":"18786284971","avatar_img":"/Uploads/Picture/2016-02-17/56c3f01175d04.jpg"}]},{"id":"1","content":"很好看","uid":"80","post_id":"2","up_time":"57分钟前","nickname":"15318188253","avatar_img":"/Uploads/Picture/2016-02-03/56b0d52b8e940.jpg","is_thumbs_up":0,"thumbs_up_cnt":"0","reply_cnt":"0","reply":null}]}
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
         * post_content : {"id":"2","content":"好看啦啦","cover_ids":"739,738","video_id":"0","uid":"80","up_time":"1490078002","nickname":"15318188253","avatar_img":"/Uploads/Picture/2016-02-03/56b0d52b8e940.jpg","is_follow":0,"is_thumbs_up":1,"thumbs_up_cnt":"2","comment_cnt":"2"}
         * comment : [{"id":"2","content":"很好看2","uid":"81","post_id":"2","up_time":"56分钟前","nickname":"18629581242","avatar_img":"/Uploads/Picture/2016-02-03/56b0d53b2a7b0.jpg","is_thumbs_up":1,"thumbs_up_cnt":"1","reply_cnt":"2","reply":[{"id":"3","content":"teienwa","com_id":"2","uid":"83","up_time":"7分钟前","nickname":"18786284971","avatar_img":"/Uploads/Picture/2016-02-17/56c3f01175d04.jpg"},{"id":"4","content":"12321","com_id":"2","uid":"83","up_time":"6分钟前","nickname":"18786284971","avatar_img":"/Uploads/Picture/2016-02-17/56c3f01175d04.jpg"}]},{"id":"1","content":"很好看","uid":"80","post_id":"2","up_time":"57分钟前","nickname":"15318188253","avatar_img":"/Uploads/Picture/2016-02-03/56b0d52b8e940.jpg","is_thumbs_up":0,"thumbs_up_cnt":"0","reply_cnt":"0","reply":null}]
         */

        private PostContentBean post_content;
        private List<CommentBean> comment;

        public PostContentBean getPost_content() {
            return post_content;
        }

        public void setPost_content(PostContentBean post_content) {
            this.post_content = post_content;
        }

        public List<CommentBean> getComment() {
            return comment;
        }

        public void setComment(List<CommentBean> comment) {
            this.comment = comment;
        }

        public static class PostContentBean {
            public List<String> getImage_path() {
                return image_path;
            }

            public void setImage_path(List<String> image_path) {
                this.image_path = image_path;
            }

            /**
             * id : 2
             * content : 好看啦啦
             * cover_ids : 739,738
             * video_id : 0
             * uid : 80
             * up_time : 1490078002
             * nickname : 15318188253
             * avatar_img : /Uploads/Picture/2016-02-03/56b0d52b8e940.jpg
             * is_follow : 0
             * is_thumbs_up : 1
             * thumbs_up_cnt : 2
             * comment_cnt : 2
             */
            private List<String> image_path;
            private String id;
            private String content;
            private String cover_ids;
            private String video_id;
            private String uid;
            private String up_time;
            private String nickname;
            private String avatar_img;
            private int is_follow;
            private int is_thumbs_up;
            private String thumbs_up_cnt;
            private String comment_cnt;
            private String movie_path;
            private int is_img;

            public String getMovie_path() {
                return movie_path;
            }

            public void setMovie_path(String movie_path) {
                this.movie_path = movie_path;
            }

            public int getIs_img() {
                return is_img;
            }

            public void setIs_img(int is_img) {
                this.is_img = is_img;
            }

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

            public int getIs_follow() {
                return is_follow;
            }

            public void setIs_follow(int is_follow) {
                this.is_follow = is_follow;
            }

            public int getIs_thumbs_up() {
                return is_thumbs_up;
            }

            public void setIs_thumbs_up(int is_thumbs_up) {
                this.is_thumbs_up = is_thumbs_up;
            }

            public String getThumbs_up_cnt() {
                return thumbs_up_cnt;
            }

            public void setThumbs_up_cnt(String thumbs_up_cnt) {
                this.thumbs_up_cnt = thumbs_up_cnt;
            }

            public String getComment_cnt() {
                return comment_cnt;
            }

            public void setComment_cnt(String comment_cnt) {
                this.comment_cnt = comment_cnt;
            }
        }

        public static class CommentBean {
            /**
             * id : 2
             * content : 很好看2
             * uid : 81
             * post_id : 2
             * up_time : 56分钟前
             * nickname : 18629581242
             * avatar_img : /Uploads/Picture/2016-02-03/56b0d53b2a7b0.jpg
             * is_thumbs_up : 1
             * thumbs_up_cnt : 1
             * reply_cnt : 2
             * reply : [{"id":"3","content":"teienwa","com_id":"2","uid":"83","up_time":"7分钟前","nickname":"18786284971","avatar_img":"/Uploads/Picture/2016-02-17/56c3f01175d04.jpg"},{"id":"4","content":"12321","com_id":"2","uid":"83","up_time":"6分钟前","nickname":"18786284971","avatar_img":"/Uploads/Picture/2016-02-17/56c3f01175d04.jpg"}]
             */

            private String id;
            private String content;
            private String uid;
            private String post_id;
            private String up_time;
            private String nickname;
            private String avatar_img;
            private int is_thumbs_up;
            private String thumbs_up_cnt;
            private String reply_cnt;
            private List<ReplyBean> reply;

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

            public String getUid() {
                return uid;
            }

            public void setUid(String uid) {
                this.uid = uid;
            }

            public String getPost_id() {
                return post_id;
            }

            public void setPost_id(String post_id) {
                this.post_id = post_id;
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

            public int getIs_thumbs_up() {
                return is_thumbs_up;
            }

            public void setIs_thumbs_up(int is_thumbs_up) {
                this.is_thumbs_up = is_thumbs_up;
            }

            public String getThumbs_up_cnt() {
                return thumbs_up_cnt;
            }

            public void setThumbs_up_cnt(String thumbs_up_cnt) {
                this.thumbs_up_cnt = thumbs_up_cnt;
            }

            public String getReply_cnt() {
                return reply_cnt;
            }

            public void setReply_cnt(String reply_cnt) {
                this.reply_cnt = reply_cnt;
            }

            public List<ReplyBean> getReply() {
                return reply;
            }

            public void setReply(List<ReplyBean> reply) {
                this.reply = reply;
            }

            public static class ReplyBean {
                /**
                 * id : 3
                 * content : teienwa
                 * com_id : 2
                 * uid : 83
                 * up_time : 7分钟前
                 * nickname : 18786284971
                 * avatar_img : /Uploads/Picture/2016-02-17/56c3f01175d04.jpg
                 */

                private String id;
                private String content;
                private String com_id;
                private String uid;
                private String up_time;
                private String nickname;
                private String avatar_img;

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

                public String getCom_id() {
                    return com_id;
                }

                public void setCom_id(String com_id) {
                    this.com_id = com_id;
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
            }
        }
    }
}
