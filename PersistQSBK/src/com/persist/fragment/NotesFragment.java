package com.persist.fragment;

import java.util.ArrayList;
import java.util.List;

import com.persist.persistqiushibaike.R;
import com.appkefu.lib.db.KFConversationHelper;
import com.appkefu.lib.ui.entity.KFConversationEntity;
import com.appkefu.lib.utils.KFSLog;
import com.persist.adapter.ConversationAdapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemLongClickListener;

public class NotesFragment extends Fragment implements OnClickListener {

	private NotesFragmentCallBack mNotesFragmentCallBack;
	private View view;
	private Context ctx;
	private ImageView Menu;
	private ListView mListView;
	private List<KFConversationEntity> mConversationList = new ArrayList<KFConversationEntity>();
	private ConversationAdapter mConversationListAdapter;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.activity_xiaozhitiao, null);
		ctx = view.getContext();
		initView();
		invalidateConversation();
		return view;
	}

	private void initView() {
		Menu = (ImageView) view.findViewById(R.id.Menu);
		mListView = (ListView) view.findViewById(R.id.Notes_List);
		Menu.setOnClickListener(this);
		mListView.setOnItemLongClickListener(new OnItemLongClickListener() {
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int position, long id) {
				// TODO Auto-generated method stub

				final int location = position;
				new AlertDialog.Builder(ctx)
						.setMessage("确定要删除此会话？")
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										// 是否应该一起将消息记录删除，而不仅仅是删除会话conversation
										KFConversationEntity entity = (KFConversationEntity) mConversationListAdapter
												.getItem(location);
										KFSLog.d("name:" + entity.getName());
										KFConversationHelper
												.getConversationHelper(ctx)
												.deleteConversation(
														entity.getName());

										mConversationList.remove(location);
										mConversationListAdapter
												.notifyDataSetChanged();
									}
								}).setNegativeButton("取消", null).create()
						.show();
				return false;
			}
		});
	}

	@SuppressWarnings("unchecked")
	public void invalidateConversation() {
		mConversationList = KFConversationHelper.getConversationHelper(ctx)// 第一次运行会出错！！！！！！！！！！
				.getAllConversation();
		mConversationListAdapter = new ConversationAdapter(ctx,
				mConversationList);
		// mListView = (ListView) view.findViewById(R.id.Notes_List);
		mListView.setAdapter(mConversationListAdapter);// 报错的根源！！！！！！！！！！！！！！！
		mConversationListAdapter.notifyDataSetChanged();
	}

	@Override
	public void onClick(View arg0) {
		int mID = arg0.getId();
		switch (mID) {
		case R.id.Menu:
			mNotesFragmentCallBack.callback(R.id.Menu);
			break;
		default:
			break;
		}
	}

	public void setCallBack(NotesFragmentCallBack mNotesFragmentCallBack) {
		this.mNotesFragmentCallBack = mNotesFragmentCallBack;
	}

	public interface NotesFragmentCallBack {
		public void callback(int flag);
	}

}
