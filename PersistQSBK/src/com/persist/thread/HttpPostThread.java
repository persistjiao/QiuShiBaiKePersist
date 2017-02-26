package com.persist.thread;

import android.os.Handler;
import android.os.Message;

import com.persist.net.MyPost;

/**
 * 网络Post请求的线程
 * */

public class HttpPostThread implements Runnable {

	private Handler hand;
	private String url;
	private String value;
	private String img = "";
	private MyPost myGet = new MyPost();

	public HttpPostThread(Handler hand, String endParamerse, String value,
			String img) {
		this.hand = hand;
		// 拼接访问服务器完整的地址
		url = endParamerse;
		this.value = value;
		this.img = img;
	}

	public HttpPostThread(Handler hand, String endParamerse, String value) {
		this.hand = hand;
		// 拼接访问服务器完整的地址
		url = endParamerse;
		this.value = value;
	}

	@Override
	public void run() {
		// 获取我们回调主ui的message
		Message msg = hand.obtainMessage();
		String result = null;
		if (img.equalsIgnoreCase("")) {
			result = myGet.doPost(url, value);
		} else {
			result = myGet.doPost(url, img, value);
		}
		msg.what = 200;
		msg.obj = result;
		// 给主ui发送消息传递数据
		hand.sendMessage(msg);

	}

}
