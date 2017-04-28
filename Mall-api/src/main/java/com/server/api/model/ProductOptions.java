package com.server.api.model;

import java.math.BigDecimal;


public class ProductOptions extends BaseEntity {

    public Data data;

    public static class Data {
        public ProductOption[] option;

        public ProductPrices[] option_price;
    }

    public static class ProductOption {
        public String id;

        public String title;

        public ProductOptionItem[] _child;
    }

    public static class ProductOptionItem {
        public String id;

        public String title;

        public boolean isEnable;
    }

    public static class ProductPrices {
        public String id;

        public BigDecimal price;

        public String product_option_id;

        public String product_option_text;
    }
}
