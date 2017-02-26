package com.persist.myview;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.persist.persistqiushibaike.R;
import com.persist.info.AshamedInfo;
import com.persist.model.Model;
import com.persist.net.ThreadPoolUtils;
import com.persist.thread.HttpGetThread;
import com.persist.utils.LoadImg;
import com.persist.utils.MyJson;
import com.persist.utils.LoadImg.ImageDownloadCallBack;

public class AuditView {

	private Context ctx;
	private View mView = null;
	private TextView mText;
	private ImageView mImageView;
	private List<AshamedInfo> list = new ArrayList<AshamedInfo>();
	private int start = 0;
	private int end = 10;
	private MyJson myJson = new MyJson();
	private int ListFlag = 0;
	private boolean animFlag = true;
	private CallBack mCallBack = null;
	private LoadImg loadImgHeadImg;

	public AuditView(Context ctx) {
		this.ctx = ctx;
		loadImgHeadImg = new LoadImg(ctx);
	}

	public View createView() {
		mView = (View) View.inflate(ctx, R.layout.activity_auditcenter, null);
		mText = (TextView) mView.findViewById(R.id.AuditCenterText);
		mImageView = (ImageView) mView.findViewById(R.id.AuditCenterImg);
		// getNET();
		return mView;
	}

	// 请求服务器方法
	private void getNET() {
		String endurl = Model.AUDIT + "start=" + start + "&end=" + end;
		ThreadPoolUtils.execute(new HttpGetThread(hand, endurl));
	}

	public void NextView() {

		if (list == null) {
			mText.setText("查询中请等待");
			animFlag = false;
			mCallBack.callback(animFlag);
			getNET();
			return;
		}
		if (ListFlag >= list.size()) {
			mText.setText("查询中请等待");
			ListFlag = list.size();
			getNET();
			animFlag = false;
			mCallBack.callback(animFlag);
			return;
		} else {
			animFlag = true;
			mText.setText(list.get(ListFlag).getQvalue());
			mImageView.setImageBitmap(null);
			if (!list.get(ListFlag).getQimg().equals(null)
					&& !list.get(ListFlag).getQimg().equals("")) {
				Model.IMGFLAG = true;
				mImageView
						.setTag((Model.QIMGURL + list.get(ListFlag).getQimg()));
				Bitmap bitHead = loadImgHeadImg.loadImage(mImageView,
						Model.QIMGURL + list.get(ListFlag).getQimg(),
						new ImageDownloadCallBack() {
							@Override
							public void onImageDownload(ImageView imageView,
									Bitmap bitmap) {
								Model.IMGFLAG = false;
								if (((String) mImageView.getTag())
										.equalsIgnoreCase((Model.QIMGURL + list
												.get(ListFlag - 1).getQimg()))) {
									Log.e("", "设置图片:");
									imageView.setImageBitmap(bitmap);
								}
							}
						});
				if (bitHead != null) {
					Log.e("", "if (bitHead != null)");
					Model.IMGFLAG = false;
					mImageView.setImageBitmap(bitHead);
				}
			}
			ListFlag++;
			mCallBack.callback(animFlag);
		}

	}

	Handler hand = new Handler() {
		@SuppressWarnings("unused")
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			if (msg.what == 200) {
				String result = (String) msg.obj;
				Log.e("liuxiaowei", result);
				if (result == null)
					return;
				if (result.equals("praise")) {
					return;
				}
				if (result.equals("unpraise")) {
					return;
				}

				Log.e("liuxiaowei", result);
				List<AshamedInfo> newList = myJson.getAshamedList(result);
				Log.e("liuxiaowei", "newList.size():" + newList.size());
				if (newList != null) {
					start += 10;
					end += 10;
					list.addAll(newList);
					Log.e("liuxiaowei", "list.size():" + list.size());
					NextView();
					return;
				} else if (newList == null) {
					Toast.makeText(ctx, "最后一条", 1).show();
					return;
				}
			}
		};
	};

	private void Praise() {

	}

	private void unPraise() {

	}

	public void setCallBack(CallBack call) {
		this.mCallBack = call;
	}

	public interface CallBack {
		public void callback(boolean animFlag);
	}
}
