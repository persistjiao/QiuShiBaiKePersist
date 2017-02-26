package com.persist.persistqiushibaike;

import com.appkefu.lib.interfaces.KFIMInterfaces;
import com.persist.model.Model;
import com.persist.net.ThreadPoolUtils;
import com.persist.thread.HttpPostThread;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class RegistetActivity extends Activity {
	private ImageView mClose;
	private EditText mName, mPassword;
	private RelativeLayout mNext;
	private TextView mStartlogin;
	private String url = null;
	private String value = null;
	private String username = null;
	private String password = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_register);
		initView();
	}

	private void initView() {
		mClose = (ImageView) findViewById(R.id.registClose);
		mName = (EditText) findViewById(R.id.Redit_name);
		mPassword = (EditText) findViewById(R.id.Redit_password);
		mNext = (RelativeLayout) findViewById(R.id.next);
		mStartlogin = (TextView) findViewById(R.id.startlogin);
		MyOnClickListener my = new MyOnClickListener();
		mClose.setOnClickListener(my);
		mNext.setOnClickListener(my);
		mStartlogin.setOnClickListener(my);

	}

	private void reginstet() {
		url = Model.REGISTET;
		value = "{\"uname\":\"" + username + "\",\"upassword\":\"" + password
				+ "\"}";
		Log.e("qianpengyu", value);
		ThreadPoolUtils.execute(new HttpPostThread(hand, url, value));
	}

	class MyOnClickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			int mId = v.getId();
			switch (mId) {
			case R.id.registClose:
				finish();
				break;
			case R.id.next:
				username = mName.getText().toString();
				password = mPassword.getText().toString();
				if (!username.equalsIgnoreCase(null)
						&& !password.equalsIgnoreCase(null)
						&& !username.equals("") && !password.equals("")) {
					if (username.length() >= 6 && password.length() >= 6) {
						// reginstet();// 请求注册功能
						registerThread();// 注册app客服
					} else {
						Toast.makeText(RegistetActivity.this, "用户名或密码最少为6位", 1)
								.show();
					}
				} else {
					Toast.makeText(RegistetActivity.this, "请先填写用户名或密码", 1)
							.show();
				}
				break;
			case R.id.startlogin:
				Intent intent = new Intent(RegistetActivity.this,
						LoginActivity.class);
				startActivity(intent);
				break;
			}

		}
	}

	// INSERT INTO `quser`( `uname`, `upassword`) VALUES ('111111','111111');
	Handler hand = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			if (msg.what == 404) {
				Toast.makeText(RegistetActivity.this, "请求失败，服务器故障", 1).show();
			} else if (msg.what == 100) {
				Toast.makeText(RegistetActivity.this, "服务器无响应", 1).show();
			} else if (msg.what == 200) {
				String result = (String) msg.obj;
				Log.e("qiangpengyu", "result:" + result);
				if (result.equals("ok")) {
					Toast.makeText(RegistetActivity.this, "注册成功,请登陆", 1).show();
					Intent intent = new Intent();
					intent.putExtra("NameValue", username);
					intent.putExtra("PasswordValue", password);
					setResult(2, intent);
					finish();
				} else if (result.trim().equals("no")) {
					mName.setText("");
					mPassword.setText("");
					// Toast.makeText(RegistetActivity.this, "用户名以存在,请重新注册", 1)
					// .show();
					return;
				} else {
					mName.setText("");
					mPassword.setText("");
					Toast.makeText(RegistetActivity.this, "注册失败", 1).show();
					return;
				}

			}
		};
	};

	public void registerThread() {

		final Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				Integer result = (Integer) msg.obj;

				if (result == 1) {
					Log.d("注册成功", "注册成功");
					reginstet();// 请求注册功能
					// Toast.makeText(RegistetActivity.this, "注册成功",
					// Toast.LENGTH_LONG).show();
				} else if (result == -400001) {
					Log.d("注册失败", "用户名长度最少为6");
					mName.setText("");
					mPassword.setText("");
					Toast.makeText(RegistetActivity.this,
							"注册失败:用户名长度最少为6(错误码:-400001)", Toast.LENGTH_LONG)
							.show();
				} else if (result == -400002) {
					Log.d("注册失败", "密码长度最少为6");
					mName.setText("");
					mPassword.setText("");
					Toast.makeText(RegistetActivity.this,
							"注册失败:密码长度最少为6(错误码:-400002)", Toast.LENGTH_LONG)
							.show();
				} else if (result == -400003) {
					Log.d("注册失败", "此用户名已经被注册");
					mName.setText("");
					mPassword.setText("");
					Toast.makeText(RegistetActivity.this, "注册失败:此用户名已经被注册",
							Toast.LENGTH_LONG).show();
				} else if (result == -400004) {
					Log.d("注册失败", "用户名含有非法字符");
					mName.setText("");
					mPassword.setText("");
					Toast.makeText(RegistetActivity.this, "注册失败:用户名含有非法字符",
							Toast.LENGTH_LONG).show();
				} else if (result == 0) {
					Log.d("注册失败",
							"其他原因：有可能是短时间内重复注册，为防止恶意注册，服务器对同一个IP注册做了时间间隔限制，即10分钟内同一个IP只能注册一个账号");
					mName.setText("");
					mPassword.setText("");
					Toast.makeText(RegistetActivity.this, "注册失败:别太累了，等下再注册",
							Toast.LENGTH_LONG).show();
				}
			}
		};

		new Thread() {
			public void run() {
				Message msg = new Message();
				// 目前用户名为整个微客服唯一，建议开发者在程序内部将appkey做为用户名的后缀，
				// 这样可以保证用户名的唯一性
				// 注册接口，返回结果为int
				msg.obj = KFIMInterfaces.register(Model.APPKEY + username,
						password);
				handler.sendMessage(msg);
			}
		}.start();

	}

}
