
package com.android.juzbao.application;

import android.app.Activity;
import android.content.Intent;
import android.support.multidex.MultiDex;
import android.text.TextUtils;
import android.view.View;

import com.android.juzbao.activity.TabHostActivity;
import com.android.juzbao.activity.account.LoginActivity_;
import com.android.juzbao.activity.me.MyMessageActivity_;
import com.android.juzbao.constant.Config;
import com.android.mall.resource.R;
import com.android.zcomponent.common.uiframe.ConfigMgr;
import com.android.zcomponent.common.uiframe.FramewrokApplication;
import com.android.zcomponent.communication.http.Communicator;
import com.android.zcomponent.communication.http.Communicator.ResponseEventArgs;
import com.android.zcomponent.communication.http.Context;
import com.android.zcomponent.constant.ComponentR;
import com.android.zcomponent.delegate.Event.EventHandler;
import com.android.zcomponent.http.api.host.Endpoint;
import com.android.zcomponent.jpush.JPushUtil;
import com.android.zcomponent.json.JsonSerializerFactory;
import com.android.zcomponent.util.GpsLocationUtil;
import com.android.zcomponent.util.LogEx;
import com.android.zcomponent.util.LogEx.LogLevelType;
import com.android.zcomponent.util.SharedPreferencesUtil;
import com.android.zcomponent.util.ShowMsg;
import com.android.zcomponent.util.ShowMsg.IConfirmDialog;
import com.android.zcomponent.views.SystemManitance;
import com.easemob.easeui.simple.EaseHelper;
import com.easemob.easeui.simple.EaseMessageNotify;
import com.server.api.model.BaseEntity;


public class
BaseApplication extends FramewrokApplication {

    private boolean bIsReloginDialogShow = false;
    public static android.content.Context app;

    @Override
    public void onCreate() {
        //初始化资源文件的Id
        new ComponentR(this);
        super.onCreate();

        app = this;

        EaseHelper.getInstance().init(this);
        EaseMessageNotify.getInstance().addMessageListener();
        // 设置默认日志级别为错误级别
        ConfigMgr.setLogLevel(LogLevelType.TYPE_LOG_LEVEL_DEBUG);
        // 初始化百度地图
        // SDKInitializer.initialize(this);
        // 初始化定位组件
        GpsLocationUtil.init(getApplicationContext());
        // 初始化图片加载组件
        initImageLoader(getApplicationContext());
        setTitleBarMoreShow(true);
        Endpoint.setEncrypt(false);
        Endpoint.HOST = "http://xiaoyuan.jzbwlkj.com";
//    Endpoint.HOST = "http://112.74.14.50:8081";
//    Endpoint.HOST = "http://118.178.139.52";
//    Endpoint.Token = Setting  sBase.getInstance().readStringByKey(Config.TOKEN);
        if (!TextUtils.isEmpty(SharedPreferencesUtil.get(Config.TOKEN)))
            Endpoint.Token = SharedPreferencesUtil.get(Config.TOKEN);
        LogEx.d(TAG, "Token ：" + SharedPreferencesUtil.get(Config.TOKEN));
        addRequestEvent();
        addResponseEvent();
        String s = SharedPreferencesUtil.get(Config.USER_MOBILE);
        if(!TextUtils.isEmpty(s)){
            JPushUtil.startPushServices(this,s,s);
        }
//        JPushUtil.init(this);

    }

    @Override
    public void onSystemMaintance(String message) {
        if (isManitanceShow) {
            return;
        }
        final Activity activity = getCurActivity();
        mSystemManitance = new SystemManitance(activity);
        mSystemManitance.showWebView("file:///android_asset/www/sys_stop.html");
    }

    @Override
    public void onUnauthorized() {
        final Activity activity = getCurActivity();
        if (null != activity && !isLoginAcitityActive()) {
            setLogin(false);
//      JPushUtil.stopPushServices(getApplicationContext());
            if (!bIsReloginDialogShow) {
                bIsReloginDialogShow = true;
                ShowMsg.showConfirmDialog(activity, new IConfirmDialog() {

                    @Override
                    public void onConfirm(boolean confirmValue) {
                        bIsReloginDialogShow = false;
                        if (confirmValue) {
                            intentToLoginActivity(activity);
                        }
                    }
                }, "登录", "取消", R.string.common_net_unauthorized);
            }
        }
    }

    @Override
    public void onTitleBarCreate(View view, View morePop, boolean isShowMore) {
        super.onTitleBarCreate(view, morePop, isShowMore);

        if (isShowMore) {
            if (null != view) {
                View tittleDot = view.findViewById(R.id.common_title_more_dot);
                EaseMessageNotify.getInstance().addView(tittleDot);
            }

            if (null != morePop) {
                View messageDot = morePop.findViewById(R.id.message_more_dot);
                EaseMessageNotify.getInstance().addView(messageDot);
            }
        }
    }

    @Override
    public void onTitleBarResume() {
        super.onTitleBarResume();
        EaseMessageNotify.getInstance().onResume();
    }

    @Override
    public void onTitleBarDestory(View view, View morePop, boolean isShowMore) {
        super.onTitleBarDestory(view, morePop, isShowMore);

        if (isShowMore) {
            if (null != view) {
                View tittleDot = view.findViewById(R.id.common_title_more_dot);
                EaseMessageNotify.getInstance().removeView(tittleDot);
            }

            if (null != morePop) {
                View messageDot = morePop.findViewById(R.id.message_more_dot);
                EaseMessageNotify.getInstance().removeView(messageDot);
            }
        }
    }

    @Override
    public void onTitleBarMoreItemClick(View view, int position) {
        super.onTitleBarMoreItemClick(view, position);

        if (position == 0) {
            if (isLogin()) {
                Intent intent = new Intent(getCurActivity(), MyMessageActivity_.class);
                getCurActivity().startActivity(intent);
            } else {
                intentToLoginActivity(getCurActivity());
            }
        } else if (position == 1) {
            for (int i = 0; i < getActivitys().size(); i++) {
                if (getActivitys().get(i) instanceof TabHostActivity) {
                    ((TabHostActivity) getActivitys().get(i)).setSelectedTab(0);
                } else {
                    getActivitys().get(i).finish();
                }
            }
        }
    }

    public void addRequestEvent() {
        Endpoint.communicator()
                .getRequestEvent()
                .addEventHandler(new EventHandler<Communicator.RequestEventArgs>() {
                    @Override
                    public void handle(Object sender, Communicator.RequestEventArgs args) {
                        String uri = args.context().uri().replace("jzb.jxlnxx.com", "xiaoyuan.jzbwlkj.com");
                        args.context().set(Context.Attribute.Uri, uri);
                    }
                });
    }

    public void addResponseEvent() {
        Endpoint.communicator()
                .getResponseEvent()
                .addEventHandler(new EventHandler<ResponseEventArgs>() {
                    @Override
                    public void handle(Object sender, final ResponseEventArgs args) {
                        getCurActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (null != args.context().data()) {
                                    String data = new String(args.context().data());

                                    BaseEntity baseEntity =
                                            JsonSerializerFactory.Create().decode(data,
                                                    BaseEntity.class);
                                    if (null != baseEntity && baseEntity.code == -94) {
                                        onUnauthorized();
                                    }
                                    if (null != baseEntity && baseEntity.code == -96) {
                                        onUnauthorized();
                                    }
                                    if (null != baseEntity && baseEntity.code == -95) {
                                        onUnauthorized();
                                    }
                                }
                            }
                        });
                    }
                });
    }

    public static void intentToLoginActivity(Activity activity) {
        Intent intent = new Intent(activity, LoginActivity_.class);
        activity.startActivity(intent);
    }

    public boolean isLoginAcitityActive() {
        for (int i = 0; i < getActivitys().size(); i++) {
            if (getActivitys().get(i) instanceof LoginActivity_) {
                return true;
            }
        }
        return false;
    }

	@Override protected void attachBaseContext(android.content.Context base) {
		super.attachBaseContext(base);
		MultiDex.install(this);
	}
}
