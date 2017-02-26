package com.persist.persistqiushibaike;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appkefu.lib.interfaces.KFIMInterfaces;
import com.persist.adapter.DetailListAdapter;
import com.persist.info.AshamedInfo;
import com.persist.info.CommentsInfo;
import com.persist.model.AccessTokenKeeper;
import com.persist.model.Model;
import com.persist.myview.MyDetailsListView;
import com.persist.net.ThreadPoolUtils;
import com.persist.thread.HttpGetThread;
import com.persist.utils.LoadImg;
import com.persist.utils.MyJson;
import com.persist.utils.LoadImg.ImageDownloadCallBack;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuth;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.StatusesAPI;
import com.sina.weibo.sdk.openapi.models.ErrorInfo;
import com.sina.weibo.sdk.openapi.models.Status;
import com.sina.weibo.sdk.openapi.models.StatusList;
import com.sina.weibo.sdk.utils.LogUtil;

public class AshamedDetailActivity extends Activity {

	private AshamedInfo info = null;
	private LoadImg loadImg;
	Bitmap bit = null;
	private MyJson myJson = new MyJson();
	private ImageView Detail_Back, Detail_SenComment;
	private TextView Detail_AshameID;
	private ImageView Detail_UserHead;
	private TextView Detail_UserName;
	private TextView Detail_MainText;
	private ImageView Detail_MainImg;
	private LinearLayout Detail_Up;
	private ImageView Detail_Up_Img;
	private TextView Detail_Up_text;
	private LinearLayout Detail_Down;
	private ImageView Detail_Down_Img;
	private TextView Detail_Down_text;
	private TextView Detail_ShareNum;
	private LinearLayout Detail_Share;
	private ImageView Detail_Share_Img;
	private MyDetailsListView Detail_List;
	private LinearLayout Detail__progressBar;
	private TextView Detail_CommentsNum;
	private int commentsFlag = 0;
	private boolean upFlag = false;
	private boolean downFlag = false;
	private List<CommentsInfo> list = new ArrayList<CommentsInfo>();
	private DetailListAdapter mAdapter = null;
	private Button ListBottem = null;
	private int mStart = 0;
	private int mEnd = 5;
	private String url = null;
	private boolean flag = true;
	private boolean listBottemFlag = true;
	/** 微博 Web 授权类，提供登陆等功能 */
	private WeiboAuth mWeiboAuth;
	/** 封装了 "access_token"，"expires_in"，"refresh_token"，并提供了他们的管理功能 */
	private Oauth2AccessToken mAccessToken;
	/** 用于获取微博信息流等操作的API */
	private StatusesAPI mStatusesAPI;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_ashamed_detail);
		Intent intent = getIntent();
		Bundle bund = intent.getBundleExtra("value");
		info = (AshamedInfo) bund.getSerializable("AshamedInfo");
		loadImg = new LoadImg(AshamedDetailActivity.this);
		initView();
		addInformation();
		mWeiboAuth = new WeiboAuth(this, Model.APP_KEY, Model.REDIRECT_URL,
				Model.SCOPE);
		addImg();

		mAdapter = new DetailListAdapter(AshamedDetailActivity.this, list);
		ListBottem = new Button(AshamedDetailActivity.this);
		ListBottem.setText("点击加载更多");
		ListBottem.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (flag && listBottemFlag) {
					url = Model.COMMENTS + "qid=" + info.getQid() + "&start="
							+ mStart + "&end=" + mEnd;
					ThreadPoolUtils.execute(new HttpGetThread(hand, url));
					listBottemFlag = false;
				} else if (!listBottemFlag)
					Toast.makeText(AshamedDetailActivity.this, "正在加载中...", 1)
							.show();
			}
		});
		Detail_List.addFooterView(ListBottem, null, false);
		ListBottem.setVisibility(View.GONE);
		Detail_List.setAdapter(mAdapter);
		String endParames = Model.COMMENTS + "qid=" + info.getQid() + "&start="
				+ mStart + "&end=" + mEnd;
		ThreadPoolUtils.execute(new HttpGetThread(hand, endParames));
		// 从 SharedPreferences 中读取上次已保存好 AccessToken 等信息，
		// 第一次启动本应用，AccessToken 不可用
		mAccessToken = AccessTokenKeeper.readAccessToken(this);
		if (mAccessToken.isSessionValid()) {
			updateTokenView(true);
		}
	}

	private void initView() {
		MyOnClickListner myOnclick = new MyOnClickListner();
		Detail_Back = (ImageView) findViewById(R.id.Detail_Back);
		Detail_SenComment = (ImageView) findViewById(R.id.Detail_SenComment);
		Detail_AshameID = (TextView) findViewById(R.id.Detail_AshameID);
		Detail_UserHead = (ImageView) findViewById(R.id.Detail_UserHead);
		Detail_UserName = (TextView) findViewById(R.id.Detail_UserName);
		Detail_MainText = (TextView) findViewById(R.id.Detail_MainText);
		Detail_MainImg = (ImageView) findViewById(R.id.Detail_MainImg);
		Detail_Up = (LinearLayout) findViewById(R.id.Detail_Up);
		Detail_Up_Img = (ImageView) findViewById(R.id.Detail_Up_Img);
		Detail_Up_text = (TextView) findViewById(R.id.Detail_Up_text);
		Detail_Down = (LinearLayout) findViewById(R.id.Detail_Down);
		Detail_Down_Img = (ImageView) findViewById(R.id.Detail_Down_Img);
		Detail_Down_text = (TextView) findViewById(R.id.Detail_Down_text);
		Detail_ShareNum = (TextView) findViewById(R.id.Detail_ShareNum);
		Detail_Share = (LinearLayout) findViewById(R.id.Detail_Share);
		Detail_Share_Img = (ImageView) findViewById(R.id.Detail_Share_Img);
		Detail_List = (MyDetailsListView) findViewById(R.id.Detail_List);
		Detail__progressBar = (LinearLayout) findViewById(R.id.Detail__progressBar);
		Detail_CommentsNum = (TextView) findViewById(R.id.Detail_CommentsNum);
		Detail_Back.setOnClickListener(myOnclick);
		Detail_SenComment.setOnClickListener(myOnclick);
		Detail_UserHead.setOnClickListener(myOnclick);
		Detail_MainImg.setOnClickListener(myOnclick);
		Detail_Up.setOnClickListener(myOnclick);
		Detail_Down.setOnClickListener(myOnclick);
		Detail_Share.setOnClickListener(myOnclick);
	}

	private class MyOnClickListner implements View.OnClickListener {
		public void onClick(View arg0) {
			int mID = arg0.getId();
			switch (mID) {
			case R.id.Detail_Back:
				AshamedDetailActivity.this.finish();
				break;
			case R.id.Detail_SenComment:
				if (Model.MYUSERINFO != null) {
					Intent intent = new Intent(AshamedDetailActivity.this,
							SendCommentActivity.class);
					Bundle bund = new Bundle();
					bund.putSerializable("AshamedInfo", info);
					intent.putExtra("value", bund);
					startActivity(intent);
				} else {
					Intent intent = new Intent(AshamedDetailActivity.this,
							LoginActivity.class);
					startActivity(intent);
				}

				break;
			case R.id.Detail_UserHead:
				if (Model.MYUSERINFO != null) {
					KFIMInterfaces.startChatWithUser(
							AshamedDetailActivity.this,// 上下文Context
							Model.APPKEY + info.getUname(),// 对方用户名
							info.getUname());// 自定义会话窗口标题
				} else {
					Intent intent = new Intent(AshamedDetailActivity.this,
							LoginActivity.class);
					startActivity(intent);
				}
				break;
			case R.id.Detail_MainImg:
				Toast.makeText(AshamedDetailActivity.this, "点击查看大图", 1).show();
				break;
			case R.id.Detail_Up:
				downFlag = false;
				upFlag = true;
				upOrDown();
				break;
			case R.id.Detail_Down:
				upFlag = false;
				downFlag = true;
				upOrDown();
				break;
			case R.id.Detail_Share:
				info.setQshare(String.valueOf(Integer.parseInt(info.getQshare()) + 1));
				Detail_ShareNum.setText(String.valueOf(info.getQshare()));
				String ShareValues = info.getQvalue();
				// Log.e("liuxiaowei", "val" + ShareValues);
				// Log.e("liuxiaowei", "img" + info.getQimg());

				if (mAccessToken == null && !mAccessToken.isSessionValid()) {
					mWeiboAuth.anthorize(new AuthListener());
				}
				mStatusesAPI = new StatusesAPI(mAccessToken);
				Log.e("liuxiaowei", "img" + !info.getQimg().equals(""));
				if (!info.getQimg().equals("")) {

					// Drawable drawable = getResources().getDrawable(
					// R.drawable.ic_launcher);
					// Bitmap bitmap = ((BitmapDrawable)
					// drawable).getBitmap();
					Drawable drawable = Detail_MainImg.getDrawable();
					Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
					mStatusesAPI.upload(ShareValues, bitmap, null, null,
							mListener);
				} else {
					mStatusesAPI.update(ShareValues, null, null, mListener);
				}
				break;
			default:
				break;
			}
		}

		private void upOrDown() {
			String upNum = info.getQlike();
			String downNum = info.getQunlike();
			int num1 = Integer.parseInt(upNum);
			int num2 = Integer.parseInt(downNum);
			Detail_Up.setBackgroundResource(R.drawable.button_vote_enable);
			Detail_Up_Img.setImageResource(R.drawable.icon_for_enable);
			Detail_Up_text.setTextColor(Color.parseColor("#815F3D"));
			Detail_Down.setBackgroundResource(R.drawable.button_vote_enable);
			Detail_Down_Img.setImageResource(R.drawable.icon_against_enable);
			Detail_Down_text.setTextColor(Color.parseColor("#815F3D"));

			if (upFlag) {
				Detail_Up.setBackgroundResource(R.drawable.button_vote_active);
				Detail_Up_Img.setImageResource(R.drawable.icon_for_active);
				Detail_Up_text.setTextColor(Color.RED);
				if (commentsFlag == 0) {
					upNum = String.valueOf(num1 + 1);
					commentsFlag = 1;
				} else if (commentsFlag == 2) {
					upNum = String.valueOf(num1 + 1);
					downNum = String.valueOf(num2 - 1);
					commentsFlag = 1;
				}
			} else if (downFlag) {
				Detail_Down
						.setBackgroundResource(R.drawable.button_vote_active);
				Detail_Down_Img
						.setImageResource(R.drawable.icon_against_active);
				Detail_Down_text.setTextColor(Color.RED);
				if (commentsFlag == 0) {
					downNum = String.valueOf(num2 + 1);
					commentsFlag = 2;
				} else if (commentsFlag == 1) {
					downNum = String.valueOf(num2 + 1);
					upNum = String.valueOf(num1 - 1);
					commentsFlag = 2;
				}
			}
			info.setQlike(upNum);
			info.setQunlike(downNum);
			Detail_Up_text.setText(upNum);
			if (!downNum.endsWith("0")) {
				Detail_Down_text.setText("-" + downNum);
			} else {
				Detail_Down_text.setText(downNum);
			}
		}
	}

	@SuppressLint("HandlerLeak")
	Handler hand = new Handler() {

		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			if (msg.what == 404) {
				Toast.makeText(AshamedDetailActivity.this, "请求失败，服务器故障", 1)
						.show();
				listBottemFlag = true;
			} else if (msg.what == 100) {
				Toast.makeText(AshamedDetailActivity.this, "服务器无响应", 1).show();
				listBottemFlag = true;
			} else if (msg.what == 200) {
				String result = (String) msg.obj;
				if (result != null) {
					List<CommentsInfo> newList = myJson
							.getAhamedCommentsList(result);
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
							Toast.makeText(AshamedDetailActivity.this,
									"已经没有了...", 1).show();
						} else {
							Detail_List.setVisibility(View.VISIBLE);
							ListBottem.setVisibility(View.GONE);
						}
						for (CommentsInfo info : newList) {
							list.add(info);
						}
						listBottemFlag = true;
					} else {
						Detail_CommentsNum.setVisibility(View.VISIBLE);
					}
				}
				Detail__progressBar.setVisibility(View.GONE);
				mAdapter.notifyDataSetChanged();
			}
		};

	};

	private void addImg() {
		if (!info.getUhead().equals("")) {
			Detail_UserHead.setTag(Model.USERHEADURL + info.getUhead());
			bit = loadImg.loadImage(Detail_UserHead,
					Model.USERHEADURL + info.getUhead(),
					new ImageDownloadCallBack() {
						public void onImageDownload(ImageView imageView,
								Bitmap bitmap) {
							Detail_UserHead.setImageBitmap(bitmap);
						}
					});
			if (bit != null) {
				Detail_UserHead.setImageBitmap(bit);
			}
		} else {
			Detail_UserHead.setImageResource(R.drawable.default_users_avatar);
		}
		if (!info.getQimg().equals("")) {
			Detail_MainImg.setTag(Model.QIMGURL + info.getQimg());
			bit = loadImg.loadImage(Detail_MainImg,
					Model.QIMGURL + info.getQimg(),
					new ImageDownloadCallBack() {
						public void onImageDownload(ImageView imageView,
								Bitmap bitmap) {
							Detail_MainImg.setImageBitmap(bitmap);
						}
					});
			if (bit != null) {
				Detail_MainImg.setImageBitmap(bit);
			}
		} else {
			Detail_MainImg.setVisibility(View.GONE);
		}

	}

	private void addInformation() {
		Detail_AshameID.setText("糗事ID" + info.getQid());
		Detail_UserName.setText(info.getUname());
		Detail_MainText.setText(info.getQvalue());
		Detail_Up_text.setText(info.getQlike());
		Detail_Down_text.setText("-" + info.getQunlike());
		Detail_ShareNum.setText(info.getQshare());
	}

	/**
	 * 微博 OpenAPI 回调接口。
	 */
	private RequestListener mListener = new RequestListener() {
		@Override
		public void onComplete(String response) {
			if (!TextUtils.isEmpty(response)) {
				if (response.startsWith("{\"statuses\"")) {
					// 调用 StatusList#parse 解析字符串成微博列表对象
					StatusList statuses = StatusList.parse(response);
					if (statuses != null && statuses.total_number > 0) {
						Toast.makeText(AshamedDetailActivity.this,
								"获取微博信息流成功, 条数: " + statuses.statusList.size(),
								Toast.LENGTH_LONG).show();
					}
				} else if (response.startsWith("{\"created_at\"")) {
					// 调用 Status#parse 解析字符串成微博对象
					Status status = Status.parse(response);
					Toast.makeText(AshamedDetailActivity.this,
							"发送一送微博成功, id = " + status.id, Toast.LENGTH_LONG)
							.show();
				} else {
					Toast.makeText(AshamedDetailActivity.this, response,
							Toast.LENGTH_LONG).show();
				}
			}
		}

		@Override
		public void onWeiboException(WeiboException e) {
			ErrorInfo info = ErrorInfo.parse(e.getMessage());
			Toast.makeText(AshamedDetailActivity.this, info.toString(),
					Toast.LENGTH_LONG).show();
		}
	};

	class AuthListener implements WeiboAuthListener {

		@Override
		public void onComplete(Bundle values) {
			// 从 Bundle 中解析 Token
			mAccessToken = Oauth2AccessToken.parseAccessToken(values);
			if (mAccessToken.isSessionValid()) {
				// 显示 Token
				// updateTokenView(false);

				// 保存 Token 到 SharedPreferences
				AccessTokenKeeper.writeAccessToken(AshamedDetailActivity.this,
						mAccessToken);
				Toast.makeText(AshamedDetailActivity.this, "授权成功",
						Toast.LENGTH_SHORT).show();
				// 对statusAPI实例化
				mStatusesAPI = new StatusesAPI(mAccessToken);
			} else {
				// 以下几种情况，您会收到 Code：
				// 1. 当您未在平台上注册的应用程序的包名与签名时；
				// 2. 当您注册的应用程序包名与签名不正确时；
				// 3. 当您在平台上注册的包名和签名与您当前测试的应用的包名和签名不匹配时。
				String code = values.getString("code");
				String message = "授权失败";
				if (!TextUtils.isEmpty(code)) {
					message = message + "\nObtained the code: " + code;
				}
				Toast.makeText(AshamedDetailActivity.this, message,
						Toast.LENGTH_LONG).show();
			}
		}

		public void onCancel() {
			Toast.makeText(AshamedDetailActivity.this, "取消授权",
					Toast.LENGTH_LONG).show();
		}

		@Override
		public void onWeiboException(WeiboException e) {
			Toast.makeText(AshamedDetailActivity.this,
					"Auth exception : " + e.getMessage(), Toast.LENGTH_LONG)
					.show();
		}
	}

	/**
	 * 显示当前 Token 信息。
	 * 
	 * @param hasExisted
	 *            配置文件中是否已存在 token 信息并且合法
	 */
	private void updateTokenView(boolean hasExisted) {
		String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
				.format(new java.util.Date(mAccessToken.getExpiresTime()));
		String format = "Token：%1$s \n有效期：%2$s";
		// Toast.makeText(AshamedDetailActivity.this,
		// String.format(format, mAccessToken.getToken(), date), 1).show();
		String message = String.format(format, mAccessToken.getToken(), date);
		if (hasExisted) {
			message = "Token 仍在有效期内，无需再次登录。" + "\n" + message;
		}
		// Toast.makeText(AshamedDetailActivity.this, message, 3).show();
	}

	@Override
	protected void onRestart() {
		String endParames = Model.COMMENTS + "qid=" + info.getQid() + "&start="
				+ mStart + "&end=" + mEnd;
		ThreadPoolUtils.execute(new HttpGetThread(hand, endParames));
		super.onRestart();
	}

}
