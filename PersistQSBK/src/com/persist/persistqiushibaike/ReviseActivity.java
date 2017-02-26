package com.persist.persistqiushibaike;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;

public class ReviseActivity extends Activity {
	private ImageView mBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_reviseinfo);
		initview();
	}

	private void initview() {
		// TODO Auto-generated method stub
		mBack = (ImageView) findViewById(R.id.imageView_back);
		MyOnClick my = new MyOnClick();
		mBack.setOnClickListener(my);

	}

	class MyOnClick implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			int mId = v.getId();
			switch (mId) {
			case R.id.imageView_back:
				finish();
				break;

			default:
				break;
			}
		}

	}
}
