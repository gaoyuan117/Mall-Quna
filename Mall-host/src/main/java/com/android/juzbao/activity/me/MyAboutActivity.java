
package com.android.juzbao.activity.me;

import android.os.Bundle;
import android.widget.TextView;

import com.android.juzbao.activity.HelpActivity_;
import com.android.juzbao.model.AccountBusiness;
import com.android.juzbao.util.CommonValidate;
import com.android.mall.resource.R;
import com.android.zcomponent.http.api.model.MessageData;
import com.android.zcomponent.util.ClientInfo;
import com.android.zcomponent.util.update.AppUpdate;
import com.android.zcomponent.views.swipebacklayout.lib.app.SwipeBackActivity;
import com.server.api.model.Version;
import com.server.api.service.AccountService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * <p>
 * Description: 关于
 * </p>
 *
 * @ClassName:MyAboutActivity
 * @author: wei
 * @date: 2015-11-10
 */
@EActivity(R.layout.activity_my_about)
public class MyAboutActivity extends SwipeBackActivity {

    private boolean isCheckVersion = false;

    @ViewById(R.id.tvew_version_show)
    TextView mTvewVersion;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    void initUI() {
        getTitleBar().setTitleText(" 关于聚真宝网络");

        AccountBusiness.queryVersion(getHttpDataLoader());
    }

    @Override
    public void onRecvMsg(MessageData msg) throws Exception {
        if (msg.valiateReq(AccountService.VersionRequest.class)) {
            Version response = (Version) msg.getRspObject();
            if (CommonValidate.validateQueryState(this, msg, response)) {
                if (isCheckVersion) {
                    AccountBusiness.checkAppUpdate(response.data[0]);
                } else {
                    if (AppUpdate.isNewVersion(response.data[0].number)) {
                        mTvewVersion.setText("有新版本" + response.data[0].title);
                    } else {
                        mTvewVersion.setText("已是最新版本"
                                + ClientInfo.getVersionName(this));
                    }
                }
            } else {
                mTvewVersion.setText("已是最新版本"
                        + ClientInfo.getVersionName(this));
            }

            isCheckVersion = false;
        }
    }

    @Click(R.id.rlayout_my_update_click)
    void onClickRlayoutMyUpdate() {
        isCheckVersion = true;
        AccountBusiness.queryVersion(getHttpDataLoader());
    }

    @Click(R.id.rlayout_my_help_click)
    void onClickRlayoutMyHelp() {
        getIntentHandle().intentToActivity(HelpActivity_.class);
    }

}