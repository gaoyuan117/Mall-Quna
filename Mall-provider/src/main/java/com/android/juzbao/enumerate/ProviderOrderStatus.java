
package com.android.juzbao.enumerate;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName:OrderByEnum
 * @Description: 订单状态
 * @author:
 * @date: 2013-6-26
 * <p>
 * 商家订单状态
 */
public enum ProviderOrderStatus {
    ALL("全部", "all"),

    PAY("待支付", "0"),

    DELIVERY("待发货", "1"),

    RECEIPT1("待收货", "2"),

    RECEIPT2("待评价", "3"),

    RECEIPT3("交易完成", "4"),

    ENSURE("已关闭", "5"),

    REFUND("退款中", "6"),

    COMPLETE("申请退款", "7"),

    CLOSE("已退款", "8");

    ProviderOrderStatus(String name, String value) {
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

    public static List<ProviderOrderStatus> toList() {
        ProviderOrderStatus[] distances = ProviderOrderStatus.values();
        List<ProviderOrderStatus> listDistances = new ArrayList<ProviderOrderStatus>();
        for (int i = 0; i < distances.length; i++) {
            listDistances.add(distances[i]);
        }

        return listDistances;
    }

    private String mstrName;

    private String mstrValue;
}
