package com.persist.persistqiushibaike;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;


/**
 * 拍照界面
 * */

public class CameralActivity extends Activity{

	private Bitmap bitmap;
	String imgPath = "/sdcard/img.jpg";
	Uri uri =null;
	String sdStatus=null;
	boolean isstate=true;
	private static IMGCallBack mIMGCallBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sdStatus = Environment.getExternalStorageState();
		if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
			Log.d("CameralAct","if (!sdStatus.equals(Environment.MEDIA_MOUNTED)");
			isstate=false;
			Intent it = new Intent("android.media.action.IMAGE_CAPTURE");
			startActivityForResult(it, Activity.DEFAULT_KEYS_DIALER);

		}else{
			Log.d("CameralAct","if (sdStatus.equals(Environment.MEDIA_MOUNTED)");
			isstate=true;
			File vFile = new File(imgPath);
			if(!vFile.exists())
			{
				File vDirPath = vFile.getParentFile(); //new File(vFile.getParent());
				vDirPath.mkdirs();
			}
			Uri uri = Uri.fromFile(vFile);
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);//
			startActivityForResult(intent, 0);
		}
	}

	/******************解决图片旋转问题****************************/
	public static Bitmap rotaingImageView(int angle , Bitmap bitmap) {  
		//旋转图片 动作  
		Matrix matrix = new Matrix();;  
		matrix.postRotate(angle);  
		// 创建新的图片  
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,  
				bitmap.getWidth(), bitmap.getHeight(), matrix, true);  
		return resizedBitmap;  
	}
	/** 
	 * 读取图片属性：旋转的角度 
	 * @param path 图片绝对路径 
	 * @return degree旋转的角度 
	 */  
	public static int readPictureDegree(String path) {  
		int degree  = 0;  
		try {  
			//读取图片属性的
			ExifInterface exifInterface = new ExifInterface(path);  
			int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);  
			switch (orientation) {  
			case ExifInterface.ORIENTATION_ROTATE_90:  
				degree = 90;  
				break;  
			case ExifInterface.ORIENTATION_ROTATE_180:  
				degree = 180;  
				break;  
			case ExifInterface.ORIENTATION_ROTATE_270:  
				degree = 270;  
				break;  
			}  
		} catch (IOException e) {  
			e.printStackTrace();  
		}  
		return degree;  
	} 

	/**********************************************/ 
	File fial=null;
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if ( isstate && resultCode == Activity.RESULT_OK) {

			fial=new File(imgPath);
			uri=Uri.fromFile(fial);
			//设置图片对应属性(压缩图片)
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 2;
			options.inPreferredConfig = Bitmap.Config.RGB_565;
			options.inPurgeable = true;
			options.inInputShareable = true;
			int degree = readPictureDegree(fial.getAbsolutePath()); 

			if (null != bitmap && bitmap.isRecycled() == false) {
				bitmap.recycle();
				bitmap=null;
			}

			if (uri != null) {
				bitmap = BitmapFactory.decodeFile(uri.getPath(),
						options);

				bitmap=rotaingImageView(degree,BitmapFactory.decodeFile(uri.getPath(),
						options));
			}
			// 保存图片到本地
			if (bitmap != null) {
				new MyCamaralThread().start();
			}  else{
				Toast.makeText(CameralActivity.this, "照片获取失败", 1).show();
				CameralActivity.this.finish();
			}
			return;
		}else if(!isstate){
			new MyCamaralThread().start();
			if (data != null) {
				Uri uri = data.getData();
				if (null != uri || uri.getPath() != null) {
					try {
						BitmapFactory.Options options = new BitmapFactory.Options();
						options.inSampleSize = 2;
						options.inPreferredConfig = Bitmap.Config.RGB_565;
						options.inPurgeable = true;
						options.inInputShareable = true;

						if (null != bitmap && bitmap.isRecycled() == false) {
							bitmap.recycle();
						}
						if (uri != null) {
							bitmap = BitmapFactory.decodeFile(uri.getPath(),
									options);
						}

					} catch (Exception e) {
						e.printStackTrace();
					}
					if (bitmap == null) {
						Bundle bundle = data.getExtras();
						if (bundle != null) {
							bitmap = (Bitmap) bundle.get("data");
						}
					}
					// 保存图片到本地
					if (bitmap != null) {
						new MyCamaralThread().start();
					}else{
						Toast.makeText(CameralActivity.this, "照片获取失败", 1).show();
						CameralActivity.this.finish();
					} 
				}
			} 
			return;
		}
		CameralActivity.this.finish();
	}

	class MyCamaralThread extends Thread{

		public void run(){
			//压缩图片
			float mWeight = 480f;
			float mHight = 854f;
			float scaleWidth;   
			float scaleHeight;  
			scaleWidth = ((float)mWeight)/bitmap.getWidth();
			scaleHeight = ((float)mHight)/bitmap.getHeight();	
			Matrix matrix = new Matrix(); 
			matrix.postScale(scaleWidth, scaleHeight);  
			Bitmap mbit = null;
			mbit = Bitmap.createBitmap(bitmap, 0, 0,   
					bitmap.getWidth(), bitmap.getHeight(), matrix, true); 
			ByteArrayOutputStream bStream = new ByteArrayOutputStream();
			mbit.compress(CompressFormat.PNG, 100, bStream);
			byte[] bytes = bStream.toByteArray();
			String data = Base64.encodeToString(bytes, Base64.DEFAULT);
			mIMGCallBack.callback(data);
			CameralActivity.this.finish();
		}
	}

	public static void setIMGcallback(IMGCallBack myIMGCallBack){
		mIMGCallBack = myIMGCallBack;
	}

	public interface IMGCallBack{
		public void callback(String data);
	}

}
