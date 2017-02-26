package com.persist.persistqiushibaike;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;

import com.persist.anim3d.Rotate3dAnimation;
import com.persist.myview.AuditView;
import com.persist.myview.FrameLinear;
import com.persist.myview.AuditView.CallBack;
import com.persist.myview.FrameLinear.MyScroll;

public class AuditActivity extends Activity{

	private LinearLayout mBottemGroup;
	private FrameLinear mCenterGroup;
	private Button mLeft,mRight;
	private View mBottemView;
	private boolean ViewFlag = true;
	private viewMyScroll mviewMyScroll = new viewMyScroll();
	private AuditView mLast;
	private View mLastScroll = null;
	private boolean animFlag = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_audit);
		initView();
	}

	private void initView(){
		mLast = new AuditView(AuditActivity.this);
		mCenterGroup = (FrameLinear) findViewById(R.id.datu);
		mBottemGroup = (LinearLayout) findViewById(R.id.AuditBottemGroup);
		mBottemView = View.inflate(AuditActivity.this,R.layout.activity_auditbottem,null);
		mLeft = (Button) mBottemView.findViewById(R.id.AuditLeftBtn);
		mRight = (Button) mBottemView.findViewById(R.id.AuditRightBtn);
		mCenterGroup.setOnMyScroll(mviewMyScroll);
		mLastScroll = (View) mLast.createView();
		mLast.setCallBack(new CallBack() {

			@Override
			public void callback(boolean animFlag) {
				AuditActivity.this.animFlag = animFlag;
			}
		});
		mCenterGroup.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				return true;
			}
		});
		mLeft.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if(!animFlag)
					return;
				Animation upanim = (Animation) AnimationUtils.
						loadAnimation(AuditActivity.this,R.anim.upanim);
				mLastScroll.startAnimation(upanim);
				mLast.NextView();
				upanim.setAnimationListener(new DisplayNextView(mLastScroll));
			}
		});
		mRight.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if(!animFlag)
					return;
				Animation downanim = (Animation) AnimationUtils.
						loadAnimation(AuditActivity.this,R.anim.down);
				mLastScroll.startAnimation(downanim);
				mLast.NextView();
				downanim.setAnimationListener(new DisplayNextView(mLastScroll));

			}
		});
	}

	private class viewMyScroll implements MyScroll{

		@Override
		public void getScrollFlag(boolean flag) {
			Log.e("zhangxinyi", "flag:"+flag);
			updateView(flag);
		}
	}

	private void updateView(boolean flag){

		if(flag){
			if(ViewFlag){
				ViewFlag = false;
				mCenterGroup.setBackgroundColor(Color.WHITE);
				mBottemGroup.removeAllViews();
				mBottemGroup.addView(mBottemView);
				mCenterGroup.addView(mLastScroll);
				mLast.NextView();
				return;
			}else{
				if(!animFlag)
					return;
				applyRotation(0,45,mLastScroll);
				mLast.NextView();
			}
			return;
		}
	}

	//右侧进入动画
	private void rightAnimation(View mScroll){
		Animation rightaAnimation = AnimationUtils.loadAnimation(
				AuditActivity.this,R.anim.right_in);
		mScroll.setAnimation(rightaAnimation);
		rightaAnimation.startNow();
	}

	private void applyRotation(float start, float end,View mScroll) {
		// 计算中心点
		final float centerX = mCenterGroup.getWidth() / 2.0f;
		final float centerY = mCenterGroup.getHeight() / 2.0f;
		final Rotate3dAnimation rotation = new Rotate3dAnimation(start, end,
				centerX, centerY, 310.0f, true);
		rotation.setDuration(200);
		rotation.setFillAfter(true);
		rotation.setInterpolator(new AccelerateInterpolator());
		// 设置监听
		rotation.setAnimationListener(new DisplayNextView(mScroll));
		mScroll.startAnimation(rotation);
	}

	private final class DisplayNextView implements Animation.AnimationListener {

		View mScroll;

		public DisplayNextView(View mScroll){
			this.mScroll = mScroll;
		}

		public void onAnimationStart(Animation animation) {
		}
		// 动画结束
		public void onAnimationEnd(Animation animation) {
			//			mScroll.post(new SwapViews());
			//添加进入新的view
			rightAnimation(mScroll);
		}
		public void onAnimationRepeat(Animation animation) {
		}
	}

}
