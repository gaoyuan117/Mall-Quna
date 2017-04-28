
package com.android.juzbao.activity;

import com.android.juzbao.fragment.CartFragment_;
import com.android.mall.resource.R;
import com.android.zcomponent.views.swipebacklayout.lib.app.SwipeBackActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

/**
 * <p>
 * Description: 购物车
 * </p>
 * 
 * @ClassName:CagetoryActivity
 * @author: wei
 * @date: 2015-11-10
 * 
 */
@EActivity(R.layout.activity_cart)
public class CartActivity extends SwipeBackActivity
{

	
	private CartFragment_ mCartFragment;

	@AfterViews
	void initUI()
	{
		getTitleBar().setTitleText("购物车");
		mCartFragment = new CartFragment_();
		addFragment(R.id.flayout_cart, mCartFragment);
	}
}