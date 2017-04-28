package com.server.api.model;

import java.math.BigDecimal;


public class CartItem {

    public String id;

    public String cart_id;

    public String shop_id;

    public String shop_title;

    public boolean isEdit;

    public boolean isSelect;

    public Data[] product;

    public static class Data {

        public String cart_id;

        public String product_id;

        public String title;

        public String option_ids;

        public String product_attr;

        public String quantity;

        public BigDecimal price;

        public BigDecimal identify_price;

        public BigDecimal total_price;

        public String image_path;

        public Image images;

        public boolean isSelect;
    }
}
