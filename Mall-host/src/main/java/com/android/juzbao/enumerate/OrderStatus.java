
package com.android.juzbao.enumerate;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName:OrderByEnum
 * @Description: 订单状态
 * @author:
 * @date: 2013-6-26
 */
public enum OrderStatus {

    ALL("全部", "All"),

    SUBMIT("待支付", "submit"),

    SUBMITED("已支付", "submited"),

    REFUND("退款", "refund"),

    /**
     * 待付款
     */
    PAY("未提交", "0"),

    /**
     * 待发货
     */
    DELIVERY("已提交", "1"),

    /**
     * 待收货
     */
    RECEIPT1("已发货", "2"),

    ENSURE("待评价 ", "3"),

    COMPLETE("交易成功 ", "4"),

    CLOSE("交易关闭 ", "5");

    OrderStatus(String name, String value) {
        mstrName = name;
        mstrValue = value;
    }

    public String getName() {
        return mstrName;
    }

    public void setName(String strName) {
        this.mstrName = strName;
    }

    public String getValue() {
        return mstrValue;
    }

    public void setValue(String strValue) {
        this.mstrValue = strValue;
    }

    public static List<OrderStatus> toList() {
        OrderStatus[] distances = OrderStatus.values();
        List<OrderStatus> listDistances = new ArrayList<OrderStatus>();
        for (int i = 0; i < distances.length; i++) {
            listDistances.add(distances[i]);
        }

        return listDistances;
    }

    public static String getNameById(String status) {
        List<OrderStatus> list = toList();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getValue().equals(status)) {
                return list.get(i).getName();
            }
        }
        return "";
    }

    private String mstrName;

    private String mstrValue;
}
