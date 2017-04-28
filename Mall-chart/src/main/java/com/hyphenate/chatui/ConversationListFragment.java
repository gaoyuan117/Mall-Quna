package com.hyphenate.chatui;

import java.util.List;

import android.content.Intent;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.juzbao.chart.R;
import com.android.zcomponent.util.CommonUtil;
import com.easemob.easeui.simple.Constant;
import com.easemob.easeui.simple.InviteMessgeDao;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMConversation.EMConversationType;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.ui.EaseConversationListFragment;
import com.hyphenate.util.NetUtils;

public class ConversationListFragment extends EaseConversationListFragment{

    private TextView errorText;
    
    private RelativeLayout mRlayoutSearch;

    @Override
    protected void initView() {
        super.initView();
        View errorView = (LinearLayout) View.inflate(getActivity(),R.layout.em_chat_neterror_item, null);
        errorItemContainer.addView(errorView);
        errorText = (TextView) errorView.findViewById(R.id.tv_connect_errormsg);
        mRlayoutSearch = (RelativeLayout) getView().findViewById(R.id.rlayout_searchbar);
        EMClient.getInstance().chatManager().addConversationListener(convListener);
        EMClient.getInstance().chatManager().addMessageListener(messageListener);
        hideTitleBar();
        hideSearchBar();
    }
    
    @Override
    public void onDestroy()
    {
    	super.onDestroy();
    	
    	EMClient.getInstance().chatManager().removeConversationListener(convListener);
    	EMClient.getInstance().chatManager().removeMessageListener(messageListener);
    }
    
	private EMMessageListener messageListener = new EMMessageListener()
	{

		@Override
		public void onMessageReceived(List<EMMessage> messages)
		{
			refresh();
		}

		@Override
		public void onCmdMessageReceived(List<EMMessage> messages)
		{
			
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
	
    /**
     * 显示搜索栏
     */
    public void showSearchBar(){
        if(mRlayoutSearch != null){
        	mRlayoutSearch.setVisibility(View.VISIBLE);
        }
    }
    
    /**
     * 隐藏搜索栏
     */
    public void hideSearchBar(){
        if(mRlayoutSearch != null){
        	mRlayoutSearch.setVisibility(View.GONE);
        }
    }
    
    @Override
    protected void setUpView() {
        super.setUpView();
        // 注册上下文菜单
        registerForContextMenu(conversationListView);
        conversationListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EMConversation conversation = conversationListView.getItem(position);
                String username = conversation.getUserName();
                if (username.equals(EMClient.getInstance().getCurrentUser()))
                    Toast.makeText(getActivity(), R.string.Cant_chat_with_yourself, 0).show();
                else {
                    // 进入聊天页面
                    Intent intent = new Intent(getActivity(), ChatActivity.class);
                    if(conversation.isGroup()){
                        if(conversation.getType() == EMConversationType.ChatRoom){
                            // it's group chat
                            intent.putExtra(Constant.EXTRA_CHAT_TYPE, Constant.CHATTYPE_CHATROOM);
                        }else{
                            intent.putExtra(Constant.EXTRA_CHAT_TYPE, Constant.CHATTYPE_GROUP);
                        }
                    }
                    // it's single chat
                    intent.putExtra(Constant.EXTRA_USER_ID, username);
                    startActivity(intent);
                }
            }
        });
        
        CommonUtil.setListViewHeightBasedOnChildren(conversationListView);
    }

    @Override
    protected void onConnectionDisconnected() {
        super.onConnectionDisconnected();
        if (NetUtils.hasNetwork(getActivity())){
         errorText.setText(R.string.can_not_connect_chat_server_connection);
        } else {
          errorText.setText(R.string.the_current_network);
        }
    }
    
    private Handler mHandler = new Handler(new Callback()
	{
		
		@Override
		public boolean handleMessage(Message msg)
		{
			CommonUtil.setListViewHeightBasedOnChildren(conversationListView);
			return false;
		}
	});
    
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        getActivity().getMenuInflater().inflate(R.menu.em_delete_message, menu); 
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        boolean deleteMessage = false;
        if (item.getItemId() == R.id.delete_message) {
            deleteMessage = true;
        } else if (item.getItemId() == R.id.delete_conversation) {
            deleteMessage = false;
        }
    	EMConversation tobeDeleteCons = conversationListView.getItem(((AdapterContextMenuInfo) item.getMenuInfo()).position);
    	if (tobeDeleteCons == null) {
    	    return true;
    	}
        // 删除此会话
        EMClient.getInstance().chatManager().deleteConversation(tobeDeleteCons.getUserName(), deleteMessage);
        InviteMessgeDao inviteMessgeDao = new InviteMessgeDao(getActivity());
        inviteMessgeDao.deleteMessage(tobeDeleteCons.getUserName());
        refresh();
        mHandler.sendEmptyMessageDelayed(0, 200);
        return true;
    }

}
