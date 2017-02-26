package com.persist.persistqiushibaike;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class Setting_yijianActivity extends Activity {
	private Button tijiao;
	private ImageView fanhui;
	private EditText yijianfankui_txt1, yijianfankui_txt2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.setting_yijianfankui);
		initView();
	}

	private void initView() {
		yijianfankui_txt1 = (EditText) findViewById(R.id.yijianfankui_Edittext1);
		yijianfankui_txt2 = (EditText) findViewById(R.id.yijianfankui_Edittext2);
		fanhui = (ImageView) findViewById(R.id.setting_yijianfankui_back);
		tijiao = (Button) findViewById(R.id.yijianfankui_button);
		MyOnclickListener mOnclickListener = new MyOnclickListener();
		yijianfankui_txt1.setOnClickListener(mOnclickListener);
		yijianfankui_txt2.setOnClickListener(mOnclickListener);
		fanhui.setOnClickListener(mOnclickListener);
		tijiao.setOnClickListener(mOnclickListener);
	}

	private class MyOnclickListener implements View.OnClickListener {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			int mID = arg0.getId();
			if (mID == R.id.setting_yijianfankui_back) {
				Setting_yijianActivity.this.finish();
			}
			if (mID == R.id.yijianfankui_button) {
				Setting_yijianActivity.this.finish();
				Toast.makeText(Setting_yijianActivity.this, "意见已经发送成功", 1)
						.show();
			}
		}

	}
}
