
package com.server.api.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.math.BigDecimal;

@SuppressWarnings("serial")
public class AdProduct extends BaseEntity implements Serializable {
    @SerializedName("data")
    public AdItem[] Data;

    public static class AdItem {

        public int id;

        public String position;

        public String product_id;

        public String cover_id;

        public String sort;

        public String create_time;

        public String update_time;

        public String begin_time;

        public String end_time;

        public String image;

        public String title;

        public String link;

        public AdProductItem product;

        @Override public String toString() {
            return "AdItem{" +
                    "id=" + id +
                    ", position='" + position + '\'' +
                    ", product_id='" + product_id + '\'' +
                    ", cover_id='" + cover_id + '\'' +
                    ", sort='" + sort + '\'' +
                    ", create_time='" + create_time + '\'' +
                    ", update_time='" + update_time + '\'' +
                    ", begin_time='" + begin_time + '\'' +
                    ", end_time='" + end_time + '\'' +
                    ", image='" + image + '\'' +
                    ", title='" + title + '\'' +
                    ", link='" + link + '\'' +
                    ", product=" + product +
                    '}';
        }
    }

    public static class AdProductItem {
        public String title;
        public BigDecimal price;
        public String view;

        @Override public String toString() {
            return "AdProductItem{" +
                    "title='" + title + '\'' +
                    ", price='" + price + '\'' +
                    ", view='" + view + '\'' +
                    '}';
        }
    }
}
