package com.persist.persistqiushibaike;

import java.util.ArrayList;
import java.util.List;

import com.appkefu.lib.interfaces.KFIMInterfaces;
import com.persist.adapter.DetailListAdapter;
import com.persist.adapter.MyListAdapter;
import com.persist.info.AshamedInfo;
import com.persist.info.CommentsInfo;
import com.persist.info.UserInfo;
import com.persist.model.Model;
import com.persist.myview.MyDetailsListView;
import com.persist.net.ThreadPoolUtils;
import com.persist.thread.HttpGetThread;
import com.persist.utils.LoadImg;
import com.persist.utils.MyJson;
import com.persist.utils.LoadImg.ImageDownloadCallBack;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class UserInfoActivity extends Activity {
	private UserInfo info = null;
	private ImageView mUserRevise, mUserMore, mUserCamera;
	private LinearLayout mBrief, mQiushi;
	private LinearLayout mUserBrief, mUserQiushi;
	private RelativeLayout mBack;
	private Boolean myflag = true;
	private TextView SendMessage, UserName, UserAge, UserHobbies, UserPlace,
			UserExplain, UserTime, UserBrand, userinfo;
	private LoadImg loadImgHeadImg;
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
	private MyDetailsListView Detail_List;
	private LinearLayout Detail__progressBar;
	private RelativeLayout Detail_CommentsNum;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_userinfo);
		Intent intent = getIntent();
		Bundle bund = intent.getBundleExtra("value");
		info = (UserInfo) bund.getSerializable("UserInfo");
		initView();
		createUserInfo();
		mAdapter = new MyListAdapter(UserInfoActivity.this, list);
		ListBottem = new Button(UserInfoActivity.this);
		ListBottem.setText("点击加载更多");
		ListBottem.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (flag && listBottemFlag) {
					url = Model.UASHAMED + "uid=" + info.getUserid()
							+ "&start=" + mStart + "&end=" + mEnd;
					ThreadPoolUtils.execute(new HttpGetThread(hand, url));
					listBottemFlag = false;
				} else if (!listBottemFlag)
					Toast.makeText(UserInfoActivity.this, "正在加载中...", 1).show();
			}
		});
		Detail_List.addFooterView(ListBottem, null, false);
		ListBottem.setVisibility(View.GONE);
		Detail_List.setAdapter(mAdapter);
		String endParames = Model.UASHAMED + "uid=" + info.getUserid()
				+ "&start=" + mStart + "&end=" + mEnd;
		ThreadPoolUtils.execute(new HttpGetThread(hand, endParames));
		// 设置个人资料"NICKNAME"
		KFIMInterfaces.setVCardField(UserInfoActivity.this, "NICKNAME",
				info.getUname());
	}

	private void initView() {
		// TODO Auto-generated method stub
		mBrief = (LinearLayout) findViewById(R.id.Brief);
		mQiushi = (LinearLayout) findViewById(R.id.Qiushi);
		mUserBrief = (LinearLayout) findViewById(R.id.UserBrief);
		mUserQiushi = (LinearLayout) findViewById(R.id.UserQiushi);
		mBack = (RelativeLayout) findViewById(R.id.UserBack);
		mUserRevise = (ImageView) findViewById(R.id.UserRevise);
		mUserMore = (ImageView) findViewById(R.id.img_UserMore);
		mUserCamera = (ImageView) findViewById(R.id.UserCamera);
		SendMessage = (TextView) findViewById(R.id.SendMessage);
		UserName = (TextView) findViewById(R.id.UserName);
		UserAge = (TextView) findViewById(R.id.UserAge);
		UserHobbies = (TextView) findViewById(R.id.UserHobbies);
		UserPlace = (TextView) findViewById(R.id.UserPlace);
		UserExplain = (TextView) findViewById(R.id.UserExplain);
		UserTime = (TextView) findViewById(R.id.UserTime);
		UserBrand = (TextView) findViewById(R.id.UserBrand);
		userinfo = (TextView) findViewById(R.id.userinfo);
		Detail_List = (MyDetailsListView) findViewById(R.id.Detail_List);
		Detail__progressBar = (LinearLayout) findViewById(R.id.Detail__progressBar);
		Detail_CommentsNum = (RelativeLayout) findViewById(R.id.usernoashamed);
		MyOnClick my = new MyOnClick();
		Detail_List.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(UserInfoActivity.this,
						AshamedDetailActivity.class);
				Bundle bund = new Bundle();
				bund.putSerializable("AshamedInfo", list.get(arg2));
				intent.putExtra("value", bund);
				startActivity(intent);
			}
		});
		mBrief.setOnClickListener(my);
		mQiushi.setOnClickListener(my);
		mUserRevise.setOnClickListener(my);
		mUserMore.setOnClickListener(my);
		mBack.setOnClickListener(my);
		mUserCamera.setOnClickListener(my);
		SendMessage.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				KFIMInterfaces.startChatWithUser(UserInfoActivity.this,// 上下文Context
						Model.APPKEY + info.getUname(),// 对方用户名
						info.getUname());// 自定义会话窗口标题
			}
		});
	}

	class MyOnClick implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			int mId = v.getId();
			switch (mId) {
			case R.id.Brief:
				myflag = true;
				initCont(myflag);
				break;
			case R.id.Qiushi:
				myflag = false;
				initCont(myflag);
				break;
			case R.id.UserBack:
				finish();
				break;
			case R.id.UserRevise:
				// Intent intent = new Intent(UserInfoActivity.this,.class);
				// startActivity(intent);
				break;
			case R.id.UserCamera:
				// Intent intent = new Intent(UserInfoActivity.this,.class);
				// startActivity(intent);
				break;
			case R.id.img_UserMore:
				logout();
				break;
			}

		}

	}

	// 退出登录
	private void logout() {
		if (Model.MYUSERINFO != null) {
			if (info.getUname().equals(Model.MYUSERINFO.getUname())) {
				new AlertDialog.Builder(this)
						.setMessage("确认退出登录?")
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										Model.MYUSERINFO = null;
										SharedPreferences sp = getSharedPreferences(
												"UserInfo", MODE_PRIVATE);
										Editor editor = sp.edit();
										editor.clear();
										editor.commit();
										// 退出登录
										KFIMInterfaces
												.Logout(UserInfoActivity.this);
										Intent intent = new Intent(
												UserInfoActivity.this,
												LoginActivity.class);
										startActivity(intent);
										finish();
									}
								}).setNegativeButton("取消", null).create()
						.show();
			}
		}
	}

	@SuppressLint("HandlerLeak")
	Handler hand = new Handler() {

		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			if (msg.what == 404) {
				Toast.makeText(UserInfoActivity.this, "请求失败，服务器故障", 1).show();
				listBottemFlag = true;
			} else if (msg.what == 100) {
				Toast.makeText(UserInfoActivity.this, "服务器无响应", 1).show();
				listBottemFlag = true;
			} else if (msg.what == 200) {
				String result = (String) msg.obj;
				if (result != null) {
					List<AshamedInfo> newList = myJson.getAshamedList(result);
					if (newList != null) {
						if (newList.size() == 5) {
							Detail_List.setVisibility(View.VISIBLE);
							ListBottem.setVisibility(View.VISIBLE);
							mStart += 5;
							mEnd += 5;
						} else if (newList.size() == 0) {
							if (list.size() == 0)
								Detail_CommentsNum.setVisibility(View.VISIBLE);
							ListBottem.setVisibility(View.GONE);
							Detail_List.setVisibility(View.GONE);
						} else {
							Detail_List.setVisibility(View.VISIBLE);
							ListBottem.setVisibility(View.GONE);
						}
						for (AshamedInfo info : newList) {
							list.add(info);
						}
						listBottemFlag = true;
					} else {
						Detail_List.setVisibility(View.GONE);
						Detail_CommentsNum.setVisibility(View.VISIBLE);
					}
				}
				Detail__progressBar.setVisibility(View.GONE);
				mAdapter.notifyDataSetChanged();
			}
			mAdapter.notifyDataSetChanged();
		};

	};

	private void initCont(Boolean myflag) {
		if (myflag) {
			mBrief.setBackgroundResource(R.drawable.cab_background_top_light);
			mQiushi.setBackgroundResource(R.drawable.ab_stacked_solid_light);
			mUserBrief.setVisibility(View.VISIBLE);
			mUserQiushi.setVisibility(View.GONE);
		} else {
			mBrief.setBackgroundResource(R.drawable.ab_stacked_solid_light);
			mQiushi.setBackgroundResource(R.drawable.cab_background_top_light);
			mUserBrief.setVisibility(View.GONE);
			mUserQiushi.setVisibility(View.VISIBLE);
		}
	}

	private void createUserInfo() {
		if (Model.MYUSERINFO != null) {
			if (info.getUname().equals(Model.MYUSERINFO.getUname()))
				SendMessage.setVisibility(View.GONE);
		} else {
			Intent intent = new Intent(UserInfoActivity.this,
					LoginActivity.class);
			startActivity(intent);
		}
		UserName.setText(info.getUname());
		if (!info.getUage().equals("null")) {
			UserAge.setText(info.getUage());
			if (info.getUsex().equals("0")) {
				UserAge.setBackgroundResource(R.drawable.nearby_gender_female);
			} else if (info.getUsex().equals("1")) {
				UserAge.setBackgroundResource(R.drawable.nearby_gender_male);
			}
		}
		if (!info.getUhobbles().equals("null")) {
			UserHobbies.setText("兴趣爱好　" + info.getUhobbles());
		}
		if (!info.getUplace().equals("null")) {
			UserPlace.setText("常出没地　" + info.getUplace());
		}
		if (!info.getUexplain().equals("null")) {
			UserExplain.setText("个人说明　" + info.getUexplain());
			userinfo.setText(info.getUexplain());
		}
		if (!info.getUbrand().equals("null")) {
			UserBrand.setText("爪机品牌　" + info.getUbrand());
		}
		UserTime.setText("注册时间　" + info.getUtime());
		if (!info.getUhead().equals("null")) {
			loadImgHeadImg = new LoadImg(UserInfoActivity.this);
			Bitmap bit = loadImgHeadImg.loadImage(mUserCamera,
					Model.USERHEADURL + info.getUhead(),
					new ImageDownloadCallBack() {
						public void onImageDownload(ImageView imageView,
								Bitmap bitmap) {
							mUserCamera.setImageBitmap(bitmap);
						}
					});
			if (bit != null) {
				mUserCamera.setImageBitmap(bit);
			}
		}
	}

}
