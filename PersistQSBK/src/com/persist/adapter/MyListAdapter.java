package com.persist.adapter;

import java.util.List;

import com.appkefu.lib.interfaces.KFIMInterfaces;
import com.persist.info.AshamedInfo;
import com.persist.model.Model;
import com.persist.persistqiushibaike.R;
import com.persist.utils.LoadImg;
import com.persist.utils.LoadImg.ImageDownloadCallBack;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MyListAdapter extends BaseAdapter {

	private List<AshamedInfo> list;
	private Context ctx;
	private LoadImg loadImgHeadImg;
	private LoadImg loadImgMainImg;
	private boolean upFlag = false;
	private boolean downFlag = false;

	public MyListAdapter(Context ctx, List<AshamedInfo> list) {
		this.list = list;
		this.ctx = ctx;
		// 加载图片对象
		loadImgHeadImg = new LoadImg(ctx);
		loadImgMainImg = new LoadImg(ctx);
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(final int arg0, View arg1, ViewGroup arg2) {
		final Holder hold;
		if (arg1 == null) {
			hold = new Holder();
			arg1 = View.inflate(ctx, R.layout.mylistview_item, null);
			hold.UserHead = (ImageView) arg1.findViewById(R.id.Item_UserHead);
			hold.UserName = (TextView) arg1.findViewById(R.id.Item_UserName);
			hold.MainText = (TextView) arg1.findViewById(R.id.Item_MainText);
			hold.MainImg = (ImageView) arg1.findViewById(R.id.Item_MainImg);
			hold.Up = (LinearLayout) arg1.findViewById(R.id.Item_Up);
			hold.Up_Img = (ImageView) arg1.findViewById(R.id.Item_Up_img);
			hold.Up_text = (TextView) arg1.findViewById(R.id.Item_Up_text);
			hold.Down = (LinearLayout) arg1.findViewById(R.id.Item_Down);
			hold.Down_Img = (ImageView) arg1.findViewById(R.id.Item_Down_img);
			hold.Down_text = (TextView) arg1.findViewById(R.id.Item_Down_text);
			hold.ShareNum = (TextView) arg1.findViewById(R.id.Item_ShareNum);
			hold.Share = (LinearLayout) arg1.findViewById(R.id.Item_Share);
			hold.Share_Img = (ImageView) arg1.findViewById(R.id.Item_Share_img);
			arg1.setTag(hold);
		} else {
			hold = (Holder) arg1.getTag();
		}

		hold.UserName.setText(list.get(arg0).getUname());
		hold.MainText.setText(list.get(arg0).getQvalue());
		hold.Up_text.setText(list.get(arg0).getQlike());
		hold.Down_text.setText("-" + list.get(arg0).getQunlike());
		hold.ShareNum.setText(list.get(arg0).getQshare());

		// 设置监听
		hold.Up.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String num;
				if (upFlag || downFlag) {// 判断是否提交过
					if (!upFlag) {// 判断提交的是不是顶 如果不是则操作
						hold.Down
								.setBackgroundResource(R.drawable.button_vote_enable);
						hold.Down_Img
								.setImageResource(R.drawable.icon_for_enable);
						hold.Down_text.setTextColor(Color.parseColor("#815F3D"));
						num = String.valueOf(Integer.parseInt(list.get(arg0)
								.getQunlike()) - 1);
						hold.Down_text.setText("-" + num);
						list.get(arg0).setQunlike(num);
						num = String.valueOf(Integer.parseInt(list.get(arg0)
								.getQlike()) + 1);
						hold.Up_text.setText(num);
						list.get(arg0).setQlike(num);
					}
				} else {
					num = String.valueOf(Integer.parseInt(list.get(arg0)
							.getQlike()) + 1);
					hold.Up_text.setText(num);
					list.get(arg0).setQlike(num);
				}
				upFlag = true;
				downFlag = false;
				hold.Up.setBackgroundResource(R.drawable.button_vote_active);
				hold.Up_Img.setImageResource(R.drawable.icon_for_active);
				hold.Up_text.setTextColor(Color.RED);
				hold.Up.setTag("0");
			}
		});
		hold.Down.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String num;
				if (upFlag || downFlag) {
					if (!downFlag) {
						hold.Up.setBackgroundResource(R.drawable.button_vote_enable);
						hold.Up_Img
								.setImageResource(R.drawable.icon_against_enable);
						hold.Up_text.setTextColor(Color.parseColor("#815F3D"));
						num = String.valueOf(Integer.parseInt(list.get(arg0)
								.getQlike()) - 1);
						hold.Up_text.setText(num);
						list.get(arg0).setQlike(num);
						num = String.valueOf(Integer.parseInt(list.get(arg0)
								.getQunlike()) + 1);
						hold.Down_text.setText("-" + num);
						list.get(arg0).setQunlike(num);
					}
				} else {
					num = String.valueOf(Integer.parseInt(list.get(arg0)
							.getQunlike()) + 1);
					hold.Down_text.setText("-" + num);
					list.get(arg0).setQlike(num);
				}
				upFlag = false;
				downFlag = true;
				hold.Down.setBackgroundResource(R.drawable.button_vote_active);
				hold.Down_Img.setImageResource(R.drawable.icon_for_active);
				hold.Down_text.setTextColor(Color.RED);
			}
		});
		hold.Share.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Toast.makeText(ctx, "分享被点击", 1).show();
			}
		});
		hold.UserHead.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Toast.makeText(ctx, "点击发送小纸条", 1).show();
				if (Model.MYUSERINFO != null) {
					KFIMInterfaces.startChatWithUser(ctx,// 上下文Context
							Model.APPKEY + list.get(arg0).getUname(),// 对方用户名
							list.get(arg0).getUname());// 自定义会话窗口标题
				} else {
					Toast.makeText(ctx, "请先登录才能发送小纸条哦", 1).show();
				}
			}
		});
		hold.MainImg.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Toast.makeText(ctx, "查看大图被点击", 1).show();
			}
		});

		hold.Up.setTag(list.get(arg0).getQid());
		// Log.e("liuxiaowei", hold.Up.getTag().toString());
		hold.Up.setBackgroundResource(R.drawable.button_vote_enable);
		hold.Up_Img.setImageResource(R.drawable.icon_against_enable);
		hold.Up_text.setTextColor(Color.parseColor("#815F3D"));
		if (hold.Up.getTag().equals("0")) {
			hold.Up.setBackgroundResource(R.drawable.button_vote_active);
			hold.Up_Img.setImageResource(R.drawable.icon_for_active);
			hold.Up_text.setTextColor(Color.RED);
		}
		hold.UserHead.setImageResource(R.drawable.default_users_avatar);
		if (list.get(arg0).getUhead().equalsIgnoreCase("")) {
			hold.UserHead.setImageResource(R.drawable.default_users_avatar);
		} else {
			hold.UserHead.setTag(Model.USERHEADURL + list.get(arg0).getUhead());
			Bitmap bitHead = loadImgHeadImg.loadImage(hold.UserHead,
					Model.USERHEADURL + list.get(arg0).getUhead(),
					new ImageDownloadCallBack() {
						@Override
						public void onImageDownload(ImageView imageView,
								Bitmap bitmap) {
							if (arg0 >= list.size()) {
								if (hold.UserHead
										.getTag()
										.equals(Model.USERHEADURL
												+ list.get(arg0 - 1).getUhead())) {
									hold.UserHead.setImageBitmap(bitmap);
								}
							} else {
								if (hold.UserHead.getTag().equals(
										Model.USERHEADURL
												+ list.get(arg0).getUhead())) {
									hold.UserHead.setImageBitmap(bitmap);
								}
							}

						}
					});
			if (bitHead != null) {
				hold.UserHead.setImageBitmap(bitHead);
			}
		}
		hold.MainImg.setImageResource(R.drawable.default_content_pic);
		if (list.get(arg0).getQimg().equalsIgnoreCase("")) {
			hold.MainImg.setVisibility(View.GONE);
		} else {
			hold.MainImg.setVisibility(View.VISIBLE);
			hold.MainImg.setTag(Model.USERHEADURL + list.get(arg0).getQimg());
			Bitmap bitMain = loadImgMainImg.loadImage(hold.MainImg,
					Model.QIMGURL + list.get(arg0).getQimg(),
					new ImageDownloadCallBack() {
						@Override
						public void onImageDownload(ImageView imageView,
								Bitmap bitmap) {
							if (arg0 >= list.size()) {
								if (hold.MainImg.getTag().equals(
										Model.QIMGURL
												+ list.get(arg0 - 1).getQimg())) {
									hold.MainImg.setImageBitmap(bitmap);
								}
							} else {
								if (hold.MainImg.getTag().equals(
										Model.QIMGURL
												+ list.get(arg0).getQimg())) {
									hold.MainImg.setImageBitmap(bitmap);
								}
							}

						}
					});
			if (bitMain != null) {
				hold.MainImg.setImageBitmap(bitMain);
			}
		}

		return arg1;
	}

	static class Holder {
		ImageView UserHead;
		TextView UserName;
		TextView MainText;
		ImageView MainImg;
		LinearLayout Up;
		ImageView Up_Img;
		TextView Up_text;
		LinearLayout Down;
		ImageView Down_Img;
		TextView Down_text;
		TextView ShareNum;
		LinearLayout Share;
		ImageView Share_Img;
	}
}
