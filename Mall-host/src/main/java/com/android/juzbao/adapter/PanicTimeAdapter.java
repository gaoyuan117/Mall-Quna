
package com.android.juzbao.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.mall.resource.R;
import com.android.zcomponent.adapter.CommonAdapter;
import com.android.zcomponent.http.api.host.Endpoint;
import com.android.zcomponent.util.MyLayoutAdapter;
import com.android.zcomponent.util.TimeUtil;
import com.server.api.model.PaincTimes;
import com.server.api.model.PaincTimes.Data;

import java.util.List;

/**
 * <p>
 * Description: 抢购时间
 * </p>
 *
 * @ClassName:PanicTimeAdapter
 * @author: wei
 * @date: 2015-11-10
 */
public class PanicTimeAdapter extends CommonAdapter {

  private int itemWidth;

  public PanicTimeAdapter(Context context, List<?> list) {
    super(context, list);

    itemWidth = (int) (MyLayoutAdapter.getInstance().getScreenWidth() / 5);
  }

  public void setSelectPosition(int position) {
    miSelectPosition = position;
    notifyDataSetChanged();
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    if (null == convertView) {
      convertView = layoutInflater.inflate(R.layout.adapter_painc_time, null);
    }

    TextView tvewReviewTitle = findViewById(convertView, R.id.tvew_painc_buy_time_click);
    tvewReviewTitle.getLayoutParams().width = itemWidth;
    PaincTimes.Data paincTime = (Data) mList.get(position);

    try {
      long serverTime = Endpoint.serverDate().getTime();
      long startTime = Long.parseLong(paincTime.start_time) * 1000;
      long endTime = Long.parseLong(paincTime.end_time) * 1000;
      String time = TimeUtil.transformLongTimeFormat(startTime, TimeUtil.STR_FORMAT_HOUR_MINUTE);
      if (serverTime >= startTime && serverTime < endTime) {
        // 正在进行
        tvewReviewTitle.setText(time + "\n抢购进行中");
      } else if (serverTime < startTime) {
        // 即将开始
        tvewReviewTitle.setText(time + "\n即将开场");
      } else if (serverTime > endTime) {
        //已结束
        tvewReviewTitle.setText(time + "\n已结束");
      } else {
        // 已开始
        tvewReviewTitle.setText(time + "\n已开抢");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return convertView;
  }
}