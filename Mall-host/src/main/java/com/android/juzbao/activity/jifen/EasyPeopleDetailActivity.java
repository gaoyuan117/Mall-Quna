package com.android.juzbao.activity.jifen;

import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.juzbao.dao.jifendao.JiFenDao;
import com.android.juzbao.model.ProductBusiness;
import com.android.quna.activity.R;
import com.android.zcomponent.http.api.model.MessageData;
import com.android.zcomponent.http.constant.ErrorCode;
import com.android.zcomponent.json.JsonSerializerFactory;
import com.android.zcomponent.util.TimeUtil;
import com.android.zcomponent.views.swipebacklayout.lib.app.SwipeBackActivity;
import com.server.api.model.jifenmodel.EasyPeople;
import com.server.api.model.jifenmodel.IndexBannerDetail;
import com.server.api.service.JiFenService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_easy_people_detail)
public class EasyPeopleDetailActivity extends SwipeBackActivity {

    @ViewById(R.id.tvew_info_title)
    TextView mInfoTitle;
    @ViewById(R.id.tvew_info_time)
    TextView mInfoTime;
    @ViewById(R.id.tvew_info_view)
    TextView mInfoView;

    @ViewById(R.id.easy_infomation_llayout)
    LinearLayout mEasyLlayout;

//    @ViewById(R.id.iv_info_img)
//    ImageView mInfoImg;

    @ViewById(R.id.webView)
    WebView mWebView;

    private EasyPeople.Data detailInfo;

    @AfterViews void initUI() {

        getTitleBar().setTitleText("查看信息");
        getDataEmptyView().showViewWaiting();

        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setAllowFileAccess(true);
        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        mWebView.getSettings().setDefaultTextEncodingName("utf-8");
        mWebView.getSettings().setAppCacheEnabled(true);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);

        mWebView.addJavascriptInterface(this, "java2js");

        mWebView.setWebChromeClient(new ChromeClient());
        mWebView.setWebViewClient(new MyWebViewClient());



        if (getIntent().getBooleanExtra("isFromBanner", false)) {
            String id = getIntent().getStringExtra("id");
            if (!TextUtils.isEmpty(id)) {
                JiFenDao.sendQuertIndexBannerDetail(getHttpDataLoader(), id);
            } else {
                getDataEmptyView().showViewDataEmpty(true, false, 2, "未查询到数据");
            }
        } else {
            String info = getIntent().getStringExtra("info");
            detailInfo = JsonSerializerFactory.Create().decode(info, EasyPeople.Data.class);
            if (detailInfo != null) {
                showInfo();
            } else {

                getDataEmptyView().showViewDataEmpty(true, false, 2, "未查询到数据");
            }
        }
    }

    @Override public void onRecvMsg(MessageData msg) throws Exception {
        if (msg.valiateReq(JiFenService.JifenIndexbannerDetailRequest.class)) {
            IndexBannerDetail response = msg.getRspObject();
            if (response != null && response.code == ErrorCode.INT_QUERY_DATA_SUCCESS) {
                mInfoTitle.setText(response.data[0].title);
                String html = ProductBusiness.getHtmlData(response.data[0].content);
                mEasyLlayout.setVisibility(View.GONE);
                String content = JiFenDao.getHTML(html);
                mWebView.loadData(content, "text/html; charset=UTF-8", null);
            } else {
                getDataEmptyView().showViewDataEmpty(true, false, 2, "未查询到数据");
            }
        }
    }

    private void showInfo() {
        mInfoTitle.setText(detailInfo.title);
        mInfoTime.setText(TimeUtil.transformLongTimeFormat(detailInfo.update_time, TimeUtil.STR_FORMAT_DATE));
        if (detailInfo.hits != null)
            mInfoView.setText("浏览：" + detailInfo.hits);
        JiFenDao.sendQueryEasyPeopleDetail(getHttpDataLoader(), detailInfo.id);
        String html = ProductBusiness.getHtmlData(detailInfo.content);
        String content = JiFenDao.getHTML(html);
        mWebView.loadData(content, "text/html; charset=UTF-8", null);
    }

    @Override public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
            if (mWebView.canGoBack()) {
                mWebView.goBack();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private class ChromeClient extends WebChromeClient {

        @Override public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            if (newProgress == 100) {
                getDataEmptyView().dismiss();
            }
        }

        @Override public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            setTitle(title);
        }
    }

    private class MyWebViewClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url != null) {
                view.loadUrl(url);
            }
            return true;
        }
    }
}
