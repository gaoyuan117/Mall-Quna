package com.android.juzbao.activity.jifen;

import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.android.juzbao.activity.TabHostActivity;
import com.android.juzbao.dao.jifendao.JiFenDao;
import com.android.juzbao.model.ProductBusiness;
import com.android.quna.activity.R;
import com.android.zcomponent.common.uiframe.BaseActivity;
import com.android.zcomponent.json.JsonSerializerFactory;
import com.server.api.model.jifenmodel.StartAd;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_start_ad_detail)
public class StartAdActivityDetail extends BaseActivity {

    @ViewById(R.id.webView)
    WebView mWebView;

    private StartAd.Data startAd;

    @AfterViews
    void initUI() {
        getTitleBar().setTitleText("官方活动");
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setAllowFileAccess(true);
        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        mWebView.getSettings().setDefaultTextEncodingName("utf-8");
        mWebView.getSettings().setAppCacheEnabled(true);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        mWebView.addJavascriptInterface(this, "java2js");

        String info = getIntent().getStringExtra("ad");
        startAd = JsonSerializerFactory.Create().decode(info, StartAd.Data.class);
        if (startAd != null) {
            showInfo();
        } else {

        }
    }

    private void showInfo() {
        getTitleBar().getBackTextView().setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                intentToTabHoat();
            }
        });
        String html = ProductBusiness.getHtmlData(startAd.content);
        String content = JiFenDao.getHTML(html);
        mWebView.loadData(content, "text/html; charset=UTF-8", null);
    }

    @Override public boolean onKeyBack(int iKeyCode, KeyEvent event) {
        return true;
    }

    private void intentToTabHoat() {
        getIntentHandle().intentToActivity(TabHostActivity.class);
        finish();
    }
}
