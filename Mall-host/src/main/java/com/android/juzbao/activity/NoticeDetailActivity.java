
package com.android.juzbao.activity;

import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.android.juzbao.dao.jifendao.JiFenDao;
import com.android.juzbao.model.ProductBusiness;
import com.android.juzbao.util.CommonValidate;
import com.android.mall.resource.R;
import com.android.zcomponent.http.api.model.MessageData;
import com.android.zcomponent.views.swipebacklayout.lib.app.SwipeBackActivity;
import com.server.api.model.HomeNoticeDetail;
import com.server.api.service.ProductService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * <p>
 * Description: 公告详情
 * </p>
 *
 * @ClassName:NoticeDetailActivity
 * @author: wei
 * @date: 2015-11-10
 */
@EActivity(R.layout.activity_notice_detail)
public class NoticeDetailActivity extends SwipeBackActivity {

    @ViewById(R.id.tvew_title_show)
    TextView mTvewTitle;

    @ViewById(R.id.tvew_desc_show)
    WebView mWebView;

    private String mstrNewsId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    void initUI() {
        getTitleBar().setTitleText("公告详情");
        getDataEmptyView().showViewWaiting();
        mstrNewsId = getIntent().getStringExtra("id");
        ProductBusiness.queryNoticeDetail(getHttpDataLoader(), mstrNewsId);

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
    }

    @Override
    public void onDataEmptyClickRefresh() {
        ProductBusiness.queryNoticeDetail(getHttpDataLoader(), mstrNewsId);
    }

    @Override
    public void onRecvMsg(MessageData msg) throws Exception {
        if (msg.valiateReq(ProductService.NoticeDetailRequest.class)){
            HomeNoticeDetail response = msg.getRspObject();
            if (CommonValidate.validateQueryState(this, msg, response)) {
                mTvewTitle.setText(response.Data.title);
                String html = ProductBusiness.getHtmlData(response.Data.content);
                html = JiFenDao.getHTML(html);
                mWebView.loadData(html, "text/html; charset=UTF-8", null);
                getDataEmptyView().dismiss();
            } else {
                getDataEmptyView().showViewDataEmpty(true, false, msg, "");
            }
        }
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

    /**
     * 如果页面中链接,如果希望点击链接继续在当前browser中响应,
     * 而不是新开Android的系统browser中响应该链接,必须覆盖 WebView的WebViewClient对象.
     */
    private class MyWebViewClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url != null) {
                view.loadUrl(url);
            }
            return true;
        }
    }
}