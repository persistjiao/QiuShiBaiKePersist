package com.persist.persistqiushibaike;

import com.persist.info.AshamedInfo;
import com.persist.model.Model;
import com.persist.net.ThreadPoolUtils;
import com.persist.thread.HttpPostThread;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * 发糗事模块
 * 
 * @author 534429149
 * 
 */

public class SendCommentActivity extends Activity {

	private AshamedInfo info = null;
	private ImageView Comment_Back, Comment_Send;
	private EditText Comment_Edit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_send_comment);
		initView();
		Intent intent = getIntent();
		Bundle bund = intent.getBundleExtra("value");
		info = (AshamedInfo) bund.getSerializable("AshamedInfo");
	}

	private void initView() {
		Comment_Back = (ImageView) findViewById(R.id.Comment_Back);
		Comment_Send = (ImageView) findViewById(R.id.Comment_Send);
		Comment_Edit = (EditText) findViewById(R.id.Comment_Edit);
		Comment_Back.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				SendCommentActivity.this.finish();
			}
		});
		Comment_Send.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (Model.MYUSERINFO != null) {
					sendMeth();
				} else {
					Intent intent = new Intent(SendCommentActivity.this,
							LoginActivity.class);
					startActivity(intent);
				}
			}
		});
	}

	private void sendMeth() {
		if (Comment_Edit.getText().toString().equals("")) {
			Toast.makeText(SendCommentActivity.this, "请先填写糗事文字再提交", 1).show();
			return;
		}
		String uid = Model.MYUSERINFO.getUserid();
		String qid = info.getQid();
		String cvalue = Comment_Edit.getText().toString();
		String Json = "{\"uid\":\"" + uid + "\"," + "\"qid\":\"" + qid + "\","
				+ "\"cvalue\":\"" + cvalue + "\"}";
		ThreadPoolUtils
				.execute(new HttpPostThread(hand, Model.ADDCOMMENT, Json));
		SendCommentActivity.this.finish();
	}

	Handler hand = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			if (msg.what == 200) {
				String result = (String) msg.obj;
				if (result != null && result.equals("ok")) {
					Toast.makeText(SendCommentActivity.this, "评论发送成功", 1)
							.show();
					SendCommentActivity.this.finish();
				}
			}
		};
	};
}
