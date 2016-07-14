package com.wesley.todaynews.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.wesley.todaynews.R;
import com.wesley.todaynews.db.TodayNewsDB;

public class RegisterActivity extends Activity {

	private TodayNewsDB mNewsDB;

	private EditText etUsername;
	private EditText etPass;
	private EditText etPassSure;
	private Button btnRegister;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_register);

		mNewsDB = TodayNewsDB.getInstance(this);
		initView();
		initData();
	}

	private void initView() {
		etUsername = (EditText) findViewById(R.id.login_edit_account);
		etPass = (EditText) findViewById(R.id.login_edit_pwd);
		etPassSure = (EditText) findViewById(R.id.et_sure);
		btnRegister = (Button) findViewById(R.id.btn_register);
	}

	private void initData() {

		btnRegister.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				if (!TextUtils.isEmpty(etUsername.getText().toString())) {

					if (TextUtils.isEmpty(etPass.getText().toString())
							|| TextUtils.isEmpty(etPassSure.getText()
									.toString())) {
						Toast.makeText(RegisterActivity.this, "密码不能为空",
								Toast.LENGTH_SHORT).show();
					} else {

						if (etPass.getText().toString()
								.equals(etPassSure.getText().toString())) {

							if (mNewsDB.isOnlyOne(etUsername.getText()
									.toString())) {
								Toast.makeText(RegisterActivity.this,
										"已经注册过账号了", Toast.LENGTH_SHORT).show();
								return;
							}

							if (!mNewsDB.isNameExists(etUsername.getText()
									.toString())) {
								// 保存用户名和密码到数据库
								mNewsDB.saveUser(etUsername.getText()
										.toString(), etPass.getText()
										.toString());
								Toast.makeText(RegisterActivity.this,
										"恭喜你，注册成功", Toast.LENGTH_SHORT).show();
								finish();

							} else {
								Toast.makeText(RegisterActivity.this, "账号已存在",
										Toast.LENGTH_SHORT).show();
							}

						} else {
							Toast.makeText(RegisterActivity.this,
									"两次密码不一致，请重新输入", Toast.LENGTH_SHORT).show();
						}
					}

				} else {
					Toast.makeText(RegisterActivity.this, "账号不能为空",
							Toast.LENGTH_SHORT).show();
				}
			}
		});

	}

}
