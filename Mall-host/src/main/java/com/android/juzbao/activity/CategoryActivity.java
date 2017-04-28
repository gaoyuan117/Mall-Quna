
package com.android.juzbao.activity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.text.TextUtils;
import android.widget.EditText;
import com.android.mall.resource.R;
import com.android.juzbao.fragment.CategroyFragment_;
import com.android.zcomponent.views.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * <p>
 * Description: 分类
 * </p>
 * 
 * @ClassName:CagetoryActivity
 * @author: wei
 * @date: 2015-11-10
 * 
 */
@EActivity(R.layout.activity_category)
public class CategoryActivity extends SwipeBackActivity
{

	@ViewById(R.id.editvew_search_show)
	EditText mEditvewSearch;
	
	private CategroyFragment_ mCagtegoryFragment;

	@AfterViews
	void initUI()
	{
		String title = getIntentHandle().getString("title");
		if (!TextUtils.isEmpty(title)){
			getTitleBar().setTitleText(title);
		}else {
			getTitleBar().setTitleText("分类");
		}
		mCagtegoryFragment = new CategroyFragment_();
		addFragment(R.id.flayout_category, mCagtegoryFragment);
	}

	@Click(R.id.imgvew_clear_icon_click)
	void onClickImgvewClearIcon()
	{
		mEditvewSearch.setText("");
	}

	@Click(R.id.tvew_scan_click)
	void onClickTvewScan()
	{
        
	}
}