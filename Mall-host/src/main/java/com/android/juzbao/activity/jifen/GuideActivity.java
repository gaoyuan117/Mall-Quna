package com.android.juzbao.activity.jifen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.juzbao.activity.TabHostActivity;
import com.android.quna.activity.R;
import com.android.zcomponent.common.uiframe.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class GuideActivity extends BaseActivity {

  private ViewPager mViewPager;
  private List<ImageView> mImageResList;
  private Button mStartBtn;

  private LinearLayout mPointLayout;
  private View mRedPoint;
  private int mPointAway;
  private final int[] mResIds = {R.drawable.splash, R.drawable.guide_01, R.drawable.guide_02,
      R.drawable.guide_03, R.drawable.guide_04};

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    this.initView();
    this.initData();
    this.addPoint();
  }

  private void initView() {
    setContentView(R.layout.activity_guide);
    mViewPager = (ViewPager) findViewById(R.id.view_pager);
    mPointLayout = (LinearLayout) findViewById(R.id.ll_point);
    mRedPoint = findViewById(R.id.red_point);
    mStartBtn = (Button) findViewById(R.id.btn_start);
    mStartBtn.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {
        startActivity(new Intent(GuideActivity.this, TabHostActivity.class));
//                SharedPreferencesUtil.putBoolean(Config.IS_SHOW_GUIDE, true);
        finish();
      }
    });
  }

  private void initData() {
    mImageResList = new ArrayList<>();
    for (int mResId : mResIds) {
      ImageView mGuideImage = new ImageView(this);
      mGuideImage.setScaleType(ImageView.ScaleType.FIT_XY);
      mGuideImage.setBackgroundResource(mResId);
      mImageResList.add(mGuideImage);
    }
    MyPagerAdapter adapter = new MyPagerAdapter();
    mViewPager.setAdapter(adapter);
    mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

      @Override public void onPageScrolled(
          int position,
          float positionOffset,
          int positionOffsetPixels) {
        int len = (int) (positionOffset * mPointAway + position * mPointAway);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mRedPoint.getLayoutParams();
        params.leftMargin = len;
        mRedPoint.setLayoutParams(params);
      }

      @Override public void onPageSelected(int position) {
        super.onPageSelected(position);
        if (mViewPager.getCurrentItem() == mImageResList.size() - 1) {
          mStartBtn.setVisibility(View.VISIBLE);
        } else {
          mStartBtn.setVisibility(View.INVISIBLE);
        }
      }
    });
  }

  private void addPoint() {
    for (int i = 0; i < mImageResList.size(); i++) {
      View point = new View(this);
      point.setBackgroundResource(R.drawable.shape_point_gray);
      int pxSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 6, getResources().getDisplayMetrics());
      LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(pxSize, pxSize);
      if (i != 0) {
        params.leftMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
      }
      point.setLayoutParams(params);
      mPointLayout.addView(point);
    }

    mPointLayout.getViewTreeObserver().addOnGlobalLayoutListener(
        new ViewTreeObserver.OnGlobalLayoutListener() {
          @Override public void onGlobalLayout() {
            mPointLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);

            mPointAway = mPointLayout.getChildAt(1).getLeft() - mPointLayout.getChildAt(0).getLeft();
          }
        });
  }

  private class MyPagerAdapter extends PagerAdapter {

    @Override
    public int getCount() {
      return mImageResList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
      return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
      container.addView(mImageResList.get(position));
      return mImageResList.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
      container.removeView((View) object);
    }
  }
}
