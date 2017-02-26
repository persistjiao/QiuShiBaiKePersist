package com.persist.persistqiushibaike;

import com.persist.model.Model;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SettingActivity extends Activity {
	private CheckBox yejianmoshi, tongzhi, baocunjindu;
	private RelativeLayout tupianjiazai, zitidaxiao, qingchuhuancun,
			wodezhanghao, yijianfankui, guoduandianzan, banbenjiance,
			guanyuqiubai;
	private ImageView fanhui;
	private TextView tupiantext, zititext, huancuntext;
	private Builder builder;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.setting);
		initView();
	}

	private void initView() {
		tupiantext = (TextView) findViewById(R.id.tupianjiazai_text);
		zititext = (TextView) findViewById(R.id.zitidaxiao_text);
		huancuntext = (TextView) findViewById(R.id.qingchuhuancun_text);
		fanhui = (ImageView) findViewById(R.id.setting_back);
		yejianmoshi = (CheckBox) findViewById(R.id.setting_yejianmoshi_checkbox);
		tongzhi = (CheckBox) findViewById(R.id.setting_tongzhi_checkbox);
		baocunjindu = (CheckBox) findViewById(R.id.setting_baocunjindu_checkbox);
		tupianjiazai = (RelativeLayout) findViewById(R.id.setting_tupianjiazai);
		zitidaxiao = (RelativeLayout) findViewById(R.id.setting_zitidaxiao);
		qingchuhuancun = (RelativeLayout) findViewById(R.id.setting_qingchuhuancun);
		wodezhanghao = (RelativeLayout) findViewById(R.id.setting_wodezhanghao);
		yijianfankui = (RelativeLayout) findViewById(R.id.setting_yijianfankui);
		guoduandianzan = (RelativeLayout) findViewById(R.id.setting_guoduandianzan);
		banbenjiance = (RelativeLayout) findViewById(R.id.setting_banbenjiance);
		guanyuqiubai = (RelativeLayout) findViewById(R.id.setting_guanyuqiubai);
		MyOnclickListener mOnclickListener = new MyOnclickListener();
		fanhui.setOnClickListener(mOnclickListener);
		yejianmoshi.setOnClickListener(mOnclickListener);
		tongzhi.setOnClickListener(mOnclickListener);
		baocunjindu.setOnClickListener(mOnclickListener);
		tupianjiazai.setOnClickListener(mOnclickListener);
		zitidaxiao.setOnClickListener(mOnclickListener);
		qingchuhuancun.setOnClickListener(mOnclickListener);
		wodezhanghao.setOnClickListener(mOnclickListener);
		yijianfankui.setOnClickListener(mOnclickListener);
		guoduandianzan.setOnClickListener(mOnclickListener);
		banbenjiance.setOnClickListener(mOnclickListener);
		guanyuqiubai.setOnClickListener(mOnclickListener);
	}

	private class MyOnclickListener implements View.OnClickListener {
		public void onClick(View v) {
			int mID = v.getId();
			if (mID == R.id.setting_back) {
				SettingActivity.this.finish();
			}
			if (mID == R.id.setting_yejianmoshi_checkbox) {
				Toast.makeText(SettingActivity.this, "切换到夜间模式", 1).show();
			}
			if (mID == R.id.setting_tongzhi_checkbox) {
				Toast.makeText(SettingActivity.this, "通知被点击", 1).show();
			}
			if (mID == R.id.setting_baocunjindu_checkbox) {
				Toast.makeText(SettingActivity.this, "保存进度被点击", 1).show();
			}
			if (mID == R.id.setting_tupianjiazai) {
				Builder builder = new Builder(SettingActivity.this);
				builder.setTitle("请选择图片加载方式");
				final String[] items = new String[] { "总是自动加载", "仅在WIFI环境中加载" };
				DialogInterface.OnClickListener dialog = new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface arg1, int arg0) {
						// TODO Auto-generated method stub
						if (arg0 == DialogInterface.BUTTON_POSITIVE) {
							arg1.cancel();
						}
						switch (arg0) {
						case 0:
							tupiantext.setText("wifi");
							break;
						case 1:
							tupiantext.setText("自动");
							break;
						}
					}
				};
				builder.setItems(items, dialog);
				builder.setPositiveButton("取消", dialog);
				AlertDialog alertDialog = builder.create();
				alertDialog.show();
			}
			if (mID == R.id.setting_zitidaxiao) {
				Builder builder = new Builder(SettingActivity.this);
				builder.setTitle("请选择字体大小");
				final String[] items = new String[] { "正常", "加大" };
				DialogInterface.OnClickListener dialog = new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface arg1, int arg0) {
						// TODO Auto-generated method stub
						if (arg0 == DialogInterface.BUTTON_POSITIVE) {
							arg1.cancel();
						}
						switch (arg0) {
						case 0:
							zititext.setText("正常");
							break;
						case 1:
							zititext.setText("加大");
							break;
						}
					}
				};
				builder.setItems(items, dialog);
				builder.setPositiveButton("取消", dialog);
				AlertDialog alertDialog = builder.create();
				alertDialog.show();

			}
			if (mID == R.id.setting_qingchuhuancun) {
				Toast.makeText(SettingActivity.this, "点击清除缓存", 1).show();
			}
			if (mID == R.id.setting_wodezhanghao) {
				if (Model.MYUSERINFO != null) {
					Intent intent = new Intent(SettingActivity.this,
							UserInfoActivity.class);
					Bundle bund = new Bundle();
					bund.putSerializable("UserInfo", Model.MYUSERINFO);
					intent.putExtra("value", bund);
					startActivity(intent);
				} else {
					Intent intent = new Intent(SettingActivity.this,
							LoginActivity.class);
					startActivity(intent);
				}
			}
			if (mID == R.id.setting_yijianfankui) {
				Intent intent2 = new Intent(SettingActivity.this,
						Setting_yijianActivity.class);
				startActivity(intent2);
			}
			if (mID == R.id.setting_guoduandianzan) {
				Toast.makeText(SettingActivity.this, "果断点赞被点击", 1).show();
			}
			if (mID == R.id.setting_banbenjiance) {
				Toast.makeText(SettingActivity.this, "亲，已经是最新版本了", 1).show();
			}
			if (mID == R.id.setting_guanyuqiubai) {
				Intent intent = new Intent(SettingActivity.this,
						AboutActivity.class);
				startActivity(intent);
			}
		}

	}
}
