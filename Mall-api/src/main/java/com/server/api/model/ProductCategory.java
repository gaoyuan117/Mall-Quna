
package com.server.api.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductCategory extends BaseEntity {

    /**
     * 所有节点
     */
    @SerializedName("data")
    public List<CategoryItem> Data;

    public static class CategoryItem {

        /**
         * 当前节点id
         */
        public String id;

        /**
         * 分类模块分组
         */
        public String group;

        /**
         * 三级分类模块记录父id
         */
        public String pid;

        /**
         * 分组ID，等于分类模块的id
         */
        public String gid;

        /**
         * 商品分类名称
         */
        public String title;

        /**
         * 分类模块名称
         */
        public String name;

        public String image;

        /**
         * 需求和其他是写死的
         */
        public int imageId;

        public String sort;

        /**
         * 记录自己的孩子
         */
        public List<CategoryItem> _child;

        @Override public String toString() {
            return "CategoryItem{" +
                    "id='" + id + '\'' +
                    ", pid='" + pid + '\'' +
                    ", gid='" + gid + '\'' +
                    ", title='" + title + '\'' +
                    ", name='" + name + '\'' +
                    ", image='" + image + '\'' +
                    ", imageId=" + imageId +
                    ", sort='" + sort + '\'' +
                    ", _child=" + _child +
                    '}';
        }
    }

    @Override public String toString() {
        return "ProductCategory{" +
                "Data=" + Data +
                '}';
    }
}
