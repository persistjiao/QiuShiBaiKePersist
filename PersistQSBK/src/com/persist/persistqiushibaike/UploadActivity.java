package com.persist.persistqiushibaike;

/**
 * 发表糗事
 * */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.persist.model.Model;
import com.persist.net.ThreadPoolUtils;
import com.persist.persistqiushibaike.CameralActivity.IMGCallBack;
import com.persist.persistqiushibaike.PhotoAct.IMGCallBack1;
import com.persist.thread.HttpPostThread;

public class UploadActivity extends Activity {

	private ImageView mClose, mUpLoadEdit, mCamera, mAlbum;
	private EditText mNeirongEdit;
	private String data = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_upload);
		initView();
	}

	private void initView() {
		// 获取关闭按钮id
		mClose = (ImageView) findViewById(R.id.close);
		MyOnclickListener mOnclickListener = new MyOnclickListener();
		// 发表按钮
		mUpLoadEdit = (ImageView) findViewById(R.id.UpLoadEdit);
		// 相机按钮
		mCamera = (ImageView) findViewById(R.id.camera);
		// 图片按钮
		mAlbum = (ImageView) findViewById(R.id.album);
		mNeirongEdit = (EditText) findViewById(R.id.neirongEdit);
		mClose.setOnClickListener(mOnclickListener);
		mUpLoadEdit.setOnClickListener(mOnclickListener);
		mCamera.setOnClickListener(mOnclickListener);
		mAlbum.setOnClickListener(mOnclickListener);
		CameralActivity.setIMGcallback(new IMGCallBack() {

			@Override
			public void callback(String data) {
				UploadActivity.this.data = data;
			}
		});
		PhotoAct.setIMGcallback(new IMGCallBack1() {

			@Override
			public void callback(String data) {
				UploadActivity.this.data = data;
			}
		});
	}

	private class MyOnclickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			int ID = v.getId();
			switch (ID) {
			case R.id.close:
				UploadActivity.this.finish();
				break;
			case R.id.UpLoadEdit:
				if (Model.MYUSERINFO != null) {
					sendMeth();
				} else {
					Intent intent = new Intent(UploadActivity.this,
							LoginActivity.class);
					startActivity(intent);
				}
				break;
			case R.id.camera:
				Intent intent = new Intent(UploadActivity.this,
						CameralActivity.class);
				startActivity(intent);
				break;
			case R.id.album:
				Intent intent3 = new Intent(UploadActivity.this, PhotoAct.class);
				startActivity(intent3);
				break;
			}
		}
	}

	private void sendMeth() {
		if (mNeirongEdit.getText().toString().equals("")) {
			Toast.makeText(UploadActivity.this, "请先填写糗事文字再提交", 1).show();
			return;
		}
		String uid = Model.MYUSERINFO.getUserid();// 用户ID
		String tid = "1";// 糗事类型ID
		String qvalue = mNeirongEdit.getText().toString();// 糗事内容
		String qimg = "";// 糗事图片
		if (!data.equalsIgnoreCase("")) {
			qimg = System.currentTimeMillis() + ".png";// 糗事图片
		}
		String Json = "{\"uid\":\"" + uid + "\"," + "\"tid\":\"" + tid + "\","
				+ "\"qimg\":\"" + qimg + "\"," + "\"qvalue\":\"" + qvalue
				+ "\"," + "\"qlike\":\"0\"," + "\"qunlike\":\"0\"}";
		ThreadPoolUtils.execute(new HttpPostThread(hand, Model.ADDVALUE, Json,
				data));
		UploadActivity.this.finish();
	}

	Handler hand = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			if (msg.what == 200) {
				String result = (String) msg.obj;
				if (result != null && result.equals("ok")) {
					Toast.makeText(UploadActivity.this, "糗事发送成功", 1).show();
					UploadActivity.this.finish();
				}
			}
		};
	};

}
