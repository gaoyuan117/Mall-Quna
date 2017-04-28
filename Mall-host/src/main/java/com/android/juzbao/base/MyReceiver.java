package com.android.juzbao.base;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.android.juzbao.activity.TabHostActivity;
import com.android.juzbao.activity.circle.NotifyActivity;
import com.android.quna.activity.R;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by admin on 2017/3/25.
 */

public class MyReceiver extends BroadcastReceiver {
    private NotificationManager manager;
    private String content;
    private String extra;

    @Override
    public void onReceive(Context context, Intent intent) {
        // 自定义消息不会展示在通知栏，完全要开发者写代码去处理
        Bundle bundle = intent.getExtras();
        content = bundle.getString(JPushInterface.EXTRA_MESSAGE);
        extra = bundle.getString(JPushInterface.EXTRA_EXTRA);
        Log.e("gy", "extra :" + extra);
        if (!TextUtils.isEmpty(extra)) {
            creatNotify(context, content);
        }
    }


    private void creatNotify(Context context, String message) {
        manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        //跳转意图
        Intent intent = new Intent(context, NotifyActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("json", message);
        bundle.putString("extra", extra);
        intent.putExtras(bundle);

        PendingIntent pending = PendingIntent.getActivity(context, 912, intent, PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder build = new NotificationCompat.Builder(context);
        build.setContentTitle("邀约通知");//设置标题
        build.setContentText("您有一条邀约通知,赶快点击查看吧~");//设置内容
        build.setTicker("邀约通知");
        build.setSmallIcon(R.drawable.logo);//设置小图标,必须有
        build.setAutoCancel(true);//设置点击后自动取消
        build.setContentIntent(pending);
        build.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
        manager.notify(1, build.build());//发送通知，就是将通知显示到手机的通知栏里
    }
}
