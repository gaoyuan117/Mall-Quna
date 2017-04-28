
package com.android.juzbao.model;

import android.content.Context;
import android.text.TextUtils;

import com.android.juzbao.dao.AddressDao;
import com.android.zcomponent.http.HttpDataLoader;
import com.android.zcomponent.util.ShowMsg;

public class AddressBusiness {

    public static void queryJZBAddress(HttpDataLoader httpDataLoader) {
        AddressDao.sendCmdQueryJZBAddress(httpDataLoader);
    }

    public static void queryMyAddress(HttpDataLoader httpDataLoader) {
        AddressDao.sendCmdQueryMyAddress(httpDataLoader);
    }

    public static void queryDelAddress(HttpDataLoader httpDataLoader, String id) {
        AddressDao.sendCmdDelAddress(httpDataLoader, id);
    }

    public static boolean addMyAddress(Context context, HttpDataLoader httpDataLoader,
                                       String mobile, String realName, String provinceId, String cityId,
                                       String areaId, String address, String qsh, boolean isDefault) {
        if (TextUtils.isEmpty(provinceId)) {
            ShowMsg.showToast(context, "请选择地区");
            return false;
        }

        if (TextUtils.isEmpty(address)) {
            ShowMsg.showToast(context, "请输入详细地址");
            return false;
        }
        if (TextUtils.isEmpty(realName)) {
            ShowMsg.showToast(context, "请输入收货人姓名");
            return false;
        }
        if (TextUtils.isEmpty(mobile)) {
            ShowMsg.showToast(context, "请输入收货人联系电话");
            return false;
        }
        if (TextUtils.isEmpty(qsh)) {
            ShowMsg.showToast(context, "请输入公寓号和寝室号");
            return false;
        }

        AddressDao.sendCmdQueryAddAddress(httpDataLoader, mobile, realName,
                provinceId, cityId, areaId, address + qsh, isDefault);

        return true;
    }

    public static boolean modifyMyAddress(Context context, HttpDataLoader httpDataLoader,
                                          int id, String mobile, String realName, String provinceId, String cityId,
                                          String areaId, String address, String qsh, boolean isDefault) {
        if (TextUtils.isEmpty(provinceId)) {
            ShowMsg.showToast(context, "请选择地区");
            return false;
        }

        if (TextUtils.isEmpty(address)) {
            ShowMsg.showToast(context, "请输入详细地址");
            return false;
        }
        if (TextUtils.isEmpty(realName)) {
            ShowMsg.showToast(context, "请输入收货人姓名");
            return false;
        }
        if (TextUtils.isEmpty(mobile)) {
            ShowMsg.showToast(context, "请输入收货人联系电话");
            return false;
        }
        if (TextUtils.isEmpty(qsh)) {
            ShowMsg.showToast(context, "请输入公寓号和寝室号");
            return false;
        }

        AddressDao.sendCmdQueryModifyAddress(httpDataLoader, id, mobile, realName,
                provinceId, cityId, areaId, address + qsh, isDefault);

        return true;
    }
}
