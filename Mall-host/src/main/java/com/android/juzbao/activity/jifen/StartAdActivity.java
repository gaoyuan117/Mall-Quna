package com.android.juzbao.activity.jifen;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.juzbao.activity.TabHostActivity;
import com.android.quna.activity.R;
import com.android.zcomponent.common.uiframe.BaseActivity;
import com.android.zcomponent.http.api.host.Endpoint;
import com.android.zcomponent.json.JsonSerializerFactory;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.server.api.model.jifenmodel.StartAd;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_start_ad)
public class StartAdActivity extends BaseActivity {

    @ViewById(R.id.img_ad)
    ImageView imageView;

    @ViewById(R.id.tvew_ad_time_update)
    TextView textView;

    private Bitmap logo;

    private StartAd.Data startAd;

    private boolean isClickSkip = false;

    private int skipTime = 5;

    private static final int CODE_MSG_UPDATA = 0X111;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private Handler handler = new Handler() {
        @Override public void handleMessage(Message msg) {
            if (isClickSkip) {
                return;
            }
            if (msg.what > 0) {
                textView.setText("跳过  " + msg.what);
                handler.sendEmptyMessageDelayed(--skipTime, 1000);
            } else {
                intentToTabHoast();
            }
        }
    };

    @AfterViews
    void initUI() {
        String ad = getIntent().getStringExtra("ad");
        startAd = JsonSerializerFactory.Create().decode(ad, StartAd.Data.class);
        logo = getIntent().getParcelableExtra("logo");
        if (startAd != null) {
            showInfo();
        } else {

        }
    }

    private void showInfo() {
        if (logo != null){
            imageView.setImageBitmap(logo);
        }else {
            DisplayImageOptions options = new DisplayImageOptions.Builder()
                    .cacheOnDisk(true).build();
            ImageLoader.getInstance().displayImage(Endpoint.HOST + startAd.logo, imageView,options);
        }
        handler.sendEmptyMessage(skipTime);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                isClickSkip = true;
                removeMsg();
                intentToTabHoast();
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                isClickSkip = true;
                removeMsg();
                intentToDetailActivity();
            }
        });
    }

    private void intentToTabHoast() {
        startActivity(new Intent(this, TabHostActivity.class));
        finish();
    }

    private void intentToDetailActivity() {
        Bundle bundle = new Bundle();
        String ad = JsonSerializerFactory.Create().encode(startAd);
        bundle.putString("ad", ad);
        Intent intent = new Intent(StartAdActivity.this, StartAdActivityDetail_.class);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

    private void removeMsg() {
        handler.removeMessages(skipTime);
    }

    @Override public boolean onKeyBack(int iKeyCode, KeyEvent event) {
        return true;
    }
}
