package com.persist.persistqiushibaike;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

public class NearbyActivity extends Activity{
	private  LinearLayout mBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_nearby);
		initview();



	}
	private void initview() {
		// TODO Auto-generated method stub
		mBack = (LinearLayout) findViewById(R.id.linearLayoutback);
		MyOnClick my = new MyOnClick();
		mBack.setOnClickListener(my);
	}
	class MyOnClick implements android.view.View.OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			int mId = arg0.getId();
			switch (mId) {
			case R.id.linearLayoutback:
				finish();
				
				break;

			default:
				break;
			}
		}
		
	}



}
