package com.persist.myview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.LinearLayout;


public class FrameLinear extends LinearLayout{

	private MyScroll my;
	private boolean GesturFlag = false;//���ر�־��
	private GestureDetector mGestureDetector;//���Ƽ���
	private boolean flag = false;
	private float start,end;
	public FrameLinear(Context context, AttributeSet attrs) {
		super(context, attrs);
		mGestureDetector = new GestureDetector(new MyGestur());
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		GesturFlag = mGestureDetector.onTouchEvent(ev);
		if(ev.getAction() == MotionEvent.ACTION_DOWN){
			start = ev.getX();
		}
		if(ev.getAction() == MotionEvent.ACTION_UP){
			end = ev.getX();
			if((end-start)<0 && flag){
				my.getScrollFlag(true);
			}else{
				my.getScrollFlag(false);
			}

		}
		return super.dispatchTouchEvent(ev);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return GesturFlag;
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return super.onTouchEvent(event);
	}
	private class MyGestur extends GestureDetector.SimpleOnGestureListener{

		@Override
		public boolean onDown(MotionEvent e) {
			flag = false;
			return super.onDown(e);
		}
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			if(Math.abs(distanceY) > Math.abs(distanceX)){
				return false;
			}else{
				if(Math.abs(distanceX) <= (2*Math.abs(distanceY))){
					return false;
				}else {
					flag = true;
					return true;
				}
			}
		}



	}
	public void setOnMyScroll(MyScroll my){
		this.my = my;//��activity���е�ʵ���MyScroll�ӿڵ���Ķ��󴫵ݽ���
	}
	public interface MyScroll{
		abstract void getScrollFlag(boolean flag);
	}
}
