
package com.easemob.easeui.simple;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;
import android.view.View;

import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;

public class EaseMessageNotify
{

	private List<View> mStateView = new ArrayList<View>();
	
	private static EaseMessageNotify mInstance;
	
	private EaseMessageNotify()
	{
		
	}
	
	public static EaseMessageNotify getInstance()
	{
		if (null == mInstance)
		{
			mInstance = new EaseMessageNotify();
		}
		
		return mInstance;
	}

	public void refresh()
	{
		Log.e("zjw", "message refresh");
		
		if (null == mStateView)
		{
			return ;
		}
		
		for (int i = 0; i < mStateView.size(); i++)
		{
			mStateView.get(i).setVisibility(View.GONE);
		}
		
		Map<String, EMConversation> conversations =
				EMClient.getInstance().chatManager().getAllConversations();
		for (EMConversation conversation : conversations.values()) {
        	if (conversation.getUnreadMsgCount() > 0){
        		for (int i = 0; i < mStateView.size(); i++)
        		{
        			mStateView.get(i).setVisibility(View.VISIBLE);
        		}
        	}
        }
	}
	
	public void addView(View view)
	{
		if (null != view)
		{
			mStateView.add(view);
			refresh();
		}
	}
	
	public void removeView(View view)
	{
		if (null != view)
		{
			mStateView.remove(view);	
		}
	}
	
	public void onResume()
	{
		refresh();
	}
	
	public Handler handler = new Handler(new Callback()
	{
		
		@Override
		public boolean handleMessage(Message msg)
		{
			if (null != mStateView)
			{
				for (int i = 0; i < mStateView.size(); i++)
        		{
        			mStateView.get(i).setVisibility(View.VISIBLE);
        		}
			}
			return false;
		}
	});

	public void addMessageListener()
	{
		EMClient.getInstance().chatManager()
				.addMessageListener(messageListener);
		refresh();
	}

	public void removeMessageListener()
	{
		EMClient.getInstance().chatManager()
				.removeMessageListener(messageListener);
		refresh();
	}

	private EMMessageListener messageListener = new EMMessageListener()
	{

		@Override
		public void onMessageReceived(List<EMMessage> messages)
		{
			handler.sendEmptyMessage(0);
		}

		@Override
		public void onCmdMessageReceived(List<EMMessage> messages)
		{
			handler.sendEmptyMessage(0);
		}

		@Override
		public void onMessageReadAckReceived(List<EMMessage> messages)
		{
		}

		@Override
		public void onMessageDeliveryAckReceived(List<EMMessage> message)
		{
		}

		@Override
		public void onMessageChanged(EMMessage message, Object change)
		{
		}
	};

}
