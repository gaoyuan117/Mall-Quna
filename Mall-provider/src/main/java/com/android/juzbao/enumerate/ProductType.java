
package com.android.juzbao.enumerate;

public enum ProductType {
    PAIZHAO("paizhao"),
    PUTONG("putong"),
    PAIPIN("paipin"),
    DINGZHI("dingzhi"),
    SCORE("score"),
    XIANZHI("xianzhi");

    private String value;

    ProductType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
