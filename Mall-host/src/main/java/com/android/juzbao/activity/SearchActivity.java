
package com.android.juzbao.activity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.widget.EditText;
import com.android.mall.resource.R;
import com.android.juzbao.fragment.SearchByNameFragment;
import com.android.zcomponent.views.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * <p>
 * Description: 搜索
 * </p>
 * 
 * @ClassName:SearchActivity
 * @author: wei
 * @date: 2015-11-10
 * 
 */
@EActivity(R.layout.activity_search)
public class SearchActivity extends SwipeBackActivity
{

	@ViewById(R.id.editvew_search_show)
	EditText mEditvewSearch;
	
	private SearchByNameFragment mCagtegoryFragment;

	@AfterViews
	void initUI()
	{
		getTitleBar().setTitleText("分类");
		
		mCagtegoryFragment = new SearchByNameFragment();
		addFragment(R.id.flayout_category, mCagtegoryFragment);
	}

}