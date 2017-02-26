package com.persist.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.persist.persistqiushibaike.R;
import com.persist.adapter.MyListAdapter;
import com.persist.info.AshamedInfo;
import com.persist.model.Model;
import com.persist.myview.MyListView;
import com.persist.myview.MyListView.OnRefreshListener;
import com.persist.net.ThreadPoolUtils;
import com.persist.persistqiushibaike.AshamedDetailActivity;
import com.persist.thread.HttpGetThread;
import com.persist.utils.MyJson;

/**
 * 热门的fragment
 * */
public class HotFragment extends Fragment implements OnClickListener {

	private String hotUrl = Model.GANHUO;
	private int topMeunFlag = 1;
	private View view;
	private ImageView mTopImg;
	private ImageView mSendAshamed;
	private TextView mTopMenuOne, mTopMenuTwo, mTopMenuThree;
	private MyListView myListView;
	private LinearLayout mLinearLayout, load_progressBar;
	private TextView HomeNoValue;
	private HotFragmentCallBack mHotFragmentCallBack;
	private MyJson myJson = new MyJson();
	private List<AshamedInfo> list = new ArrayList<AshamedInfo>();
	private MyListAdapter mAdapter = null;
	private Button ListBottem = null;
	private int mStart = 0;
	private int mEnd = 5;
	private String url = null;
	private boolean flag = true;
	private boolean loadflag = false;
	private boolean listBottemFlag = true;
	private Context ctx;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.frame_home, null);
		ctx = view.getContext();
		myListView = new MyListView(ctx);
		initView();
		return view;
	}

	private void initView() {
		load_progressBar = (LinearLayout) view
				.findViewById(R.id.load_progressBar);
		mLinearLayout = (LinearLayout) view.findViewById(R.id.HomeGroup);
		myListView.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT));
		myListView.setDivider(null);
		mLinearLayout.addView(myListView);
		mTopImg = (ImageView) view.findViewById(R.id.Menu);
		mSendAshamed = (ImageView) view.findViewById(R.id.SendAshamed);
		mTopMenuOne = (TextView) view.findViewById(R.id.TopMenuOne);
		mTopMenuTwo = (TextView) view.findViewById(R.id.TopMenuTwo);
		mTopMenuThree = (TextView) view.findViewById(R.id.TopMenuThree);
		HomeNoValue = (TextView) view.findViewById(R.id.HomeNoValue);
		mTopImg.setOnClickListener(this);
		mSendAshamed.setOnClickListener(this);
		mTopMenuOne.setOnClickListener(this);
		mTopMenuTwo.setOnClickListener(this);
		mTopMenuThree.setOnClickListener(this);
		createTextColor();
		switch (topMeunFlag) {
		case 1:
			mTopMenuOne.setTextColor(Color.WHITE);
			mTopMenuOne.setBackgroundResource(R.drawable.top_tab_active);
			break;
		case 2:
			mTopMenuTwo.setTextColor(Color.WHITE);
			mTopMenuTwo.setBackgroundResource(R.drawable.top_tab_active);
			break;
		case 3:
			mTopMenuThree.setTextColor(Color.WHITE);
			mTopMenuThree.setBackgroundResource(R.drawable.top_tab_active);
			break;
		}
		mAdapter = new MyListAdapter(ctx, list);
		ListBottem = new Button(ctx);
		ListBottem.setText("点击加载更多");
		ListBottem.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (flag && listBottemFlag) {
					url = hotUrl + "start=" + mStart + "&end=" + mEnd;
					ThreadPoolUtils.execute(new HttpGetThread(hand, url));
					listBottemFlag = false;
				} else if (!listBottemFlag)
					Toast.makeText(ctx, "正在加载中...", 1).show();
			}
		});
		myListView.addFooterView(ListBottem, null, false);
		ListBottem.setVisibility(View.GONE);
		myListView.setAdapter(mAdapter);
		myListView.setOnItemClickListener(new MainListOnItemClickListener());
		url = Model.GANHUO + "start=" + mStart + "&end=" + mEnd;
		ThreadPoolUtils.execute(new HttpGetThread(hand, url));
		myListView.setonRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {

				if (loadflag == true) {
					mStart = 0;
					mEnd = 5;
					url = hotUrl + "start=" + mStart + "&end=" + mEnd;
					ListBottem.setVisibility(View.GONE);
					ThreadPoolUtils.execute(new HttpGetThread(hand, url));
					loadflag = false;
				} else {
					Toast.makeText(ctx, "正在加载中，请勿重复刷新", 1).show();
				}

			}
		});
	}

	@Override
	public void onClick(View arg0) {
		int mID = arg0.getId();
		switch (mID) {
		case R.id.Menu:
			mHotFragmentCallBack.callback(R.id.Menu);
			break;
		case R.id.SendAshamed:
			mHotFragmentCallBack.callback(R.id.SendAshamed);
			break;
		case R.id.TopMenuOne:
			createTextColor();
			mTopMenuOne.setTextColor(Color.WHITE);
			mTopMenuOne.setBackgroundResource(R.drawable.top_tab_active);
			if (topMeunFlag != 1) {
				hotUrl = Model.GANHUO;
				topMeunFlag = 1;
				createListModel();
			}
			break;
		case R.id.TopMenuTwo:
			createTextColor();
			mTopMenuTwo.setTextColor(Color.WHITE);
			mTopMenuTwo.setBackgroundResource(R.drawable.top_tab_active);
			if (topMeunFlag != 2) {
				hotUrl = Model.NENCAO;
				topMeunFlag = 2;
				createListModel();
			}
			break;
		case R.id.TopMenuThree:
			createTextColor();
			mTopMenuThree.setTextColor(Color.WHITE);
			mTopMenuThree.setBackgroundResource(R.drawable.top_tab_active);
			if (topMeunFlag != 3) {
				hotUrl = Model.WENZI;
				topMeunFlag = 3;
				createListModel();
			}
			break;
		default:
			break;
		}
	}

	private void createListModel() {
		ListBottem.setVisibility(View.GONE);
		mLinearLayout.setVisibility(View.GONE);
		load_progressBar.setVisibility(View.VISIBLE);
		loadflag = false;
		mStart = 0;
		mEnd = 5;
		url = hotUrl + "start=" + mStart + "&end=" + mEnd;
		ThreadPoolUtils.execute(new HttpGetThread(hand, url));
	}

	private class MainListOnItemClickListener implements OnItemClickListener {
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			Intent intent = new Intent(ctx, AshamedDetailActivity.class);
			Bundle bund = new Bundle();
			bund.putSerializable("AshamedInfo", list.get(arg2 - 1));
			intent.putExtra("value", bund);
			startActivity(intent);
		}
	}

	@SuppressWarnings("deprecation")
	private void createTextColor() {
		Drawable background = new BitmapDrawable();
		mTopMenuOne.setTextColor(Color.parseColor("#815F3D"));
		mTopMenuTwo.setTextColor(Color.parseColor("#815F3D"));
		mTopMenuThree.setTextColor(Color.parseColor("#815F3D"));
		mTopMenuOne.setBackgroundDrawable(background);
		mTopMenuTwo.setBackgroundDrawable(background);
		mTopMenuThree.setBackgroundDrawable(background);
		HomeNoValue.setVisibility(View.GONE);
	}

	public void setCallBack(HotFragmentCallBack mHotFragmentCallBack) {
		this.mHotFragmentCallBack = mHotFragmentCallBack;
	}

	public interface HotFragmentCallBack {
		public void callback(int flag);
	}

	Handler hand = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			if (msg.what == 404) {
				Toast.makeText(ctx, "找不到地址", 1).show();
				listBottemFlag = true;
			} else if (msg.what == 100) {
				Toast.makeText(ctx, "传输失败", 1).show();
				listBottemFlag = true;
			} else if (msg.what == 200) {
				String result = (String) msg.obj;
				if (result != null) {
					List<AshamedInfo> newList = myJson.getAshamedList(result);
					if (newList != null) {
						if (newList.size() == 5) {
							ListBottem.setVisibility(View.VISIBLE);
							mStart += 5;
							mEnd += 5;
						} else if (newList.size() == 0) {
							if (list.size() == 0)
								HomeNoValue.setVisibility(View.VISIBLE);
							ListBottem.setVisibility(View.GONE);
							Toast.makeText(ctx, "已经没有了...", 1).show();
						} else {
							ListBottem.setVisibility(View.GONE);
						}
						if (!loadflag) {
							list.removeAll(list);
						}
						for (AshamedInfo info : newList) {
							list.add(info);
						}
						listBottemFlag = true;
					} else {
						if (list.size() == 0)
							HomeNoValue.setVisibility(View.VISIBLE);
					}
				}
				mLinearLayout.setVisibility(View.VISIBLE);
				load_progressBar.setVisibility(View.GONE);
				myListView.onRefreshComplete();
				mAdapter.notifyDataSetChanged();
				loadflag = true;
			}
			mAdapter.notifyDataSetChanged();
		};
	};

}
