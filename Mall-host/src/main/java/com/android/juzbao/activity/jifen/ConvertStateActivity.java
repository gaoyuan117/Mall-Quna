package com.android.juzbao.activity.jifen;

import android.widget.ImageView;
import android.widget.TextView;

import com.android.quna.activity.R;
import com.android.zcomponent.views.swipebacklayout.lib.app.SwipeBackActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by Koterwong on 2016/7/29.
 * 积分兑换成功或失败
 */
@EActivity(R.layout.activity_convert_state)
public class ConvertStateActivity extends SwipeBackActivity {

    @ViewById(R.id.imgvew_convert_state)
    ImageView mConvertIv;

    @ViewById(R.id.tvew_convert_state)
    TextView mConvertTv;

    @AfterViews
    void initUI() {
        String classname = getIntent().getStringExtra("classname");
        if (null != classname && classname.equals(MyJifenSendPayActivity_.class.getName())) {
            getTitleBar().setTitleText("支付成功");
            mConvertTv.setText("恭喜您支付成功");
        } else if (null != classname && classname.equals(JifenConvertActivity_.class.getName())) {
            getTitleBar().setTitleText("兑换成功");
            mConvertTv.setText("恭喜您兑换成功");
        }
    }
}
