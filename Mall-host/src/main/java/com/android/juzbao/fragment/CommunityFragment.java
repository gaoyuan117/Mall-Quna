
package com.android.juzbao.fragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;

import android.text.TextUtils;

import com.android.mall.resource.R;
import com.android.juzbao.activity.TabHostActivity;
import com.android.zcomponent.common.uiframe.fragment.BaseFragment;
import com.android.zcomponent.fragment.WebViewFragment;

/**
 * <p>
 * Description: 社区
 * </p>
 *
 * @ClassName:CommunityFragment
 * @author: wei
 * @date: 2015-11-10
 */
@EFragment(R.layout.fragment_community)
public class CommunityFragment extends BaseFragment {

    private WebViewFragment mWebViewFragment;

    private String mstrUrl;

    @AfterViews
    void initUI() {
        mWebViewFragment = new WebViewFragment() {

            @Override
            public String getUrl() {
                if (TextUtils.isEmpty(mstrUrl)) {
                    if (getActivity() instanceof TabHostActivity) {
                        ((TabHostActivity) getActivity()).queryAppInfo();
                    }
                }
                return mstrUrl;
            }

            @Override
            public String getTitle() {
                return "社区";
            }

            @Override
            public String getHtml() {
                return null;
            }
        };
        addFragment(R.id.flayout_webview, mWebViewFragment);
    }

    public void setUrl(String url) {
        mstrUrl = url;
        if (null != mWebViewFragment && mWebViewFragment.isCreated()) {
            mWebViewFragment.loadUrl();
        }
    }

}