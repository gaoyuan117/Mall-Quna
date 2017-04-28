package com.server.api.service;

import com.android.zcomponent.communication.http.Context;
import com.android.zcomponent.communication.http.annotation.HttpPortal;
import com.android.zcomponent.http.api.host.Endpoint;
import com.android.zcomponent.json.WebFormEncoder;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by admin on 2017/3/22.
 */

public class CircleService extends Endpoint {

    /**
     * 发布动态
     */
    @HttpPortal(path = "http://xiaoyuan.jzbwlkj.com/api/Circle/postThreads", method = Context.Method.Post, encoder = WebFormEncoder.class)
    public static class commitDynamic extends Endpoint {
        @SerializedName("content")
        public String content;
        @SerializedName("cover_ids")
        public String cover_ids;
        @SerializedName("video_id")
        public String video_id;
    }

    /**
     * 获取全部动态
     */
    @HttpPortal(path = "http://xiaoyuan.jzbwlkj.com/api/Circle/allDynamic", method = Context.Method.Post, encoder = WebFormEncoder.class)
    public static class getAllDynamic extends Endpoint {

    }

    /**
     * 动态详情
     */
    @HttpPortal(path = "http://xiaoyuan.jzbwlkj.com/api/Circle/postDetails", method = Context.Method.Post, encoder = WebFormEncoder.class)
    public static class loadDynamicDetail extends Endpoint {
        @SerializedName("id")
        public String id;
    }

    /**
     * 点赞
     */
    @HttpPortal(path = "http://xiaoyuan.jzbwlkj.com/api/Circle/thumbs_up", method = Context.Method.Post, encoder = WebFormEncoder.class)
    public static class zan extends Endpoint {
        @SerializedName("id")
        public String id;
        @SerializedName("type")
        public String type;
    }


    /**
     * 评论帖子
     */
    @HttpPortal(path = "http://xiaoyuan.jzbwlkj.com/api/Circle/addcomment", method = Context.Method.Post, encoder = WebFormEncoder.class)
    public static class comment extends Endpoint {
        @SerializedName("content")
        public String content;
        @SerializedName("post_id")
        public String post_id;
    }

    /**
     * 回复评论
     */
    @HttpPortal(path = "http://xiaoyuan.jzbwlkj.com/api/Circle/addreply", method = Context.Method.Post, encoder = WebFormEncoder.class)
    public static class reply extends Endpoint {
        @SerializedName("content")
        public String content;
        @SerializedName("com_id")
        public String com_id;
    }


    /**
     * 关注
     */
    @HttpPortal(path = "http://xiaoyuan.jzbwlkj.com/api/Circle/addfollow", method = Context.Method.Post, encoder = WebFormEncoder.class)
    public static class concern extends Endpoint {
        @SerializedName(" to_uid")
        public String to_uid;
    }

    /**
     * 关注列表
     */
    @HttpPortal(path = "http://xiaoyuan.jzbwlkj.com/api/Circle/followList", method = Context.Method.Post, encoder = WebFormEncoder.class)
    public static class getConcern extends Endpoint {

    }

    /**
     * 个人动态
     */
    @HttpPortal(path = "http://xiaoyuan.jzbwlkj.com/api/Circle/ownDynamic", method = Context.Method.Post, encoder = WebFormEncoder.class)
    public static class myDynamic extends Endpoint {
        @SerializedName("uid")
        public String uid;
    }

    /**
     * 发起需求
     * demand 需求
     * number 需求人数
     * eff_time 有效期限
     * description 详情描述
     * place       邀约地点
     * money       酬金
     */
    @HttpPortal(path = "http://xiaoyuan.jzbwlkj.com/api/Circle/invite", method = Context.Method.Post, encoder = WebFormEncoder.class)
    public static class sendDemand extends Endpoint {
        @SerializedName("demand")
        public String demand;

        @SerializedName("number")
        public String number;

        @SerializedName("eff_time")
        public String eff_time;

        @SerializedName("description")
        public String description;

        @SerializedName("place")
        public String place;

        @SerializedName("money")
        public String money;
    }

    /**
     * 支付酬金
     */
    @HttpPortal(path = "http://xiaoyuan.jzbwlkj.com/api/Circle/rechargeOrder", method = Context.Method.Post, encoder = WebFormEncoder.class)
    public static class payAliMoney extends Endpoint {
        @SerializedName("invite_id")
        public String invite_id;
    }

    /**
     * 获取全部邀约
     */
    @HttpPortal(path = "http://xiaoyuan.jzbwlkj.com/api/Circle/inviteList", method = Context.Method.Post, encoder = WebFormEncoder.class)
    public static class getAllDemand extends Endpoint {
    }

    /**
     * 邀约详情
     */
    @HttpPortal(path = "http://xiaoyuan.jzbwlkj.com/api/Circle/inviteDetails", method = Context.Method.Post, encoder = WebFormEncoder.class)
    public static class demandDetail extends Endpoint {
        @SerializedName("inv_id")
        public String inv_id;
    }

    /**
     * 应邀
     */
    @HttpPortal(path = "http://xiaoyuan.jzbwlkj.com/api/Circle/addinvite", method = Context.Method.Post, encoder = WebFormEncoder.class)
    public static class addInvite extends Endpoint {
        @SerializedName("inv_id")
        public String inv_id;
        @SerializedName("content")
        public String content;
        @SerializedName("type")
        public String type;
    }

    /**
     * 接受应邀
     */
    @HttpPortal(path = "http://xiaoyuan.jzbwlkj.com/api/Circle/choiceInvite", method = Context.Method.Post, encoder = WebFormEncoder.class)
    public static class agreeInvite extends Endpoint {
        @SerializedName("id")
        public String id;
    }


    /**
     * 同意邀约
     */
    @HttpPortal(path = "http://xiaoyuan.jzbwlkj.com/api/Circle/whetherAgree", method = Context.Method.Post, encoder = WebFormEncoder.class)
    public static class agreeOrRefuse extends Endpoint {
        @SerializedName("inv_to_id")
        public String inv_to_id;
        @SerializedName("type")
        public String type;
    }
}
