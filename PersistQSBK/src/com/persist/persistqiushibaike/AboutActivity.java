package com.persist.persistqiushibaike;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;

public class AboutActivity extends Activity {
	private ImageView fanhui;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.setting_guanyu);
		initView();
	}

	private void initView() {
		fanhui = (ImageView) findViewById(R.id.guanyuqibai_back);
		WebView web = (WebView) findViewById(R.id.webView1);
		web.loadUrl("file:///android_asset/about.html");
		MyOnclickListener mOnclickListener = new MyOnclickListener();
		fanhui.setOnClickListener(mOnclickListener);
	}

	private class MyOnclickListener implements View.OnClickListener {

		@Override
		public void onClick(View arg0) {
			int mID = arg0.getId();
			if (mID == R.id.guanyuqibai_back) {
				AboutActivity.this.finish();
			}
		}

	}

}
