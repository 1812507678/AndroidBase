package com.haijun.shop.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.haijun.shop.R;
import com.haijun.shop.bean.User;
import com.haijun.shop.util.DialogUtil;
import com.haijun.shop.util.ToastUtil;
import com.haijun.shop.util.UserUtil;
import com.haijun.shop.view.CircleImageView;

import org.xutils.x;

import java.io.File;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

public class AccountnfoActivity extends BaseActivity {

	private static final String TAG = "AccountnfoActivity";
	private TextView tv_personcenter_nickname;
	private CircleImageView iv_personcenter_iocn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_accountnfo);

	}

	protected void initView() {
		setCenterText("个人信息");
		setLeftImage(R.drawable.back_normal);

		RelativeLayout rl_personcenter_persondata = (RelativeLayout) findViewById(R.id.rl_personcenter_persondata);
		RelativeLayout rl_personcenter_address = (RelativeLayout) findViewById(R.id.rl_personcenter_address);
		tv_personcenter_nickname = (TextView) findViewById(R.id.tv_personcenter_nickname);
		iv_personcenter_iocn = (CircleImageView) findViewById(R.id.iv_personcenter_iocn);
		TextView tv_personcenter_phone = (TextView) findViewById(R.id.tv_personcenter_phone);

		RelativeLayout rl_personcenter_nickname = (RelativeLayout) findViewById(R.id.rl_personcenter_nickname);

		MyOnClicklListener myOnClicklListener =new MyOnClicklListener();
		rl_personcenter_persondata.setOnClickListener(myOnClicklListener);
		rl_personcenter_nickname.setOnClickListener(myOnClicklListener);
		rl_personcenter_address.setOnClickListener(myOnClicklListener);

		User userInfo = UserUtil.getUserInfo();
		if (userInfo!=null){
			if (!TextUtils.isEmpty(userInfo.getNickname())){
				tv_personcenter_nickname.setText(userInfo.getNickname());
			}
			if (!TextUtils.isEmpty(userInfo.getPhone())){
				tv_personcenter_phone.setText(userInfo.getPhone());
			}
			if (!TextUtils.isEmpty(userInfo.getIconUrl())){
				x.image().bind(iv_personcenter_iocn,userInfo.getIconUrl());
			}
		}

	}

	@Override
	protected void initData() {

	}

	class MyOnClicklListener implements View.OnClickListener{

		@Override
		public void onClick(View view) {
			Intent intent = new Intent(AccountnfoActivity.this, ModifyAccountItemActivity.class);
			switch (view.getId()){
				//选择头像
				case R.id.rl_personcenter_persondata:
					Intent pickiIntent = new Intent();
					//匹配其过滤器
					pickiIntent.setAction("android.intent.action.PICK");
					pickiIntent.setType("image/*");
					startActivityForResult(pickiIntent,113);
					break;
				case R.id.rl_personcenter_nickname:
					intent.putExtra("type",1);
					startActivityForResult(intent,110);
					break;

				case R.id.rl_personcenter_address:
					intent = new Intent(AccountnfoActivity.this, DeliveryAddressActivity.class);
					startActivity(intent);
					break;
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.i(TAG, "requestCode:" + requestCode);
		if (resultCode==RESULT_OK){
			String result = data.getStringExtra("result");

			if (requestCode==110){
				tv_personcenter_nickname.setText(result);
				setResult(RESULT_OK);
			}

			else if (requestCode==113){
				Uri uri = data.getData();
				Cursor cursor = getContentResolver().query(uri, null, null, null,null);
				if (cursor != null && cursor.moveToFirst()) {
					String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)); //storage/emulated/0/360Browser/download/20151006063040806.jpg
					Log.i(TAG, "path:" + path);
					iv_personcenter_iocn.setImageBitmap(BitmapFactory.decodeFile(path));

					final BmobFile icon = new BmobFile(new File(path));
					icon.upload(new UploadFileListener() {
						@Override
						public void done(BmobException e) {
							Log.i(TAG,"上传:   "+e);
							if (e==null){
								final User user = UserUtil.getUserInfo();
								if (user!=null){
									user.setIconUrl(icon.getUrl());
									user.update(new UpdateListener() {
										@Override
										public void done(BmobException e) {
											if (e==null){
												Toast.makeText(AccountnfoActivity.this,"头像上传成功",Toast.LENGTH_SHORT).show();
												UserUtil.saveUserToLocal(user);
												setResult(RESULT_OK);
											}else {
												Toast.makeText(AccountnfoActivity.this,"头像上传失败"+e,Toast.LENGTH_SHORT).show();
											}
										}
									});

								}

							}else {
								ToastUtil.showToask("上传失败，请检查网络是否正常");
								Log.i(TAG,"上传失败error:   "+e);
							}
						}
					});

					cursor.close();

				}
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	public void back(View view){
		finish();
	}


}

		