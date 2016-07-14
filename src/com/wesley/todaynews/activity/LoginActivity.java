package com.wesley.todaynews.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wesley.todaynews.R;
import com.wesley.todaynews.db.TodayNewsDB;
import com.wesley.todaynews.utils.PrefUtils;

public class LoginActivity extends Activity implements OnClickListener {
	private TodayNewsDB mNewsDB;

	private TextView tvLogin;// 登录
	private TextView tvSafe;// 忘记密码
	private TextView tvRegister;// 注册
	private EditText etUsername;// 用户名
	private EditText etPasswd;// 密码
	private CheckBox cbSavePass;// 记住密码

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);

		mNewsDB = TodayNewsDB.getInstance(this);
		initView();
		initData();

		boolean isRemember = PrefUtils.getBoolean(this, etUsername.getText()
				.toString(), true);
		System.out.println("保存的值是多少：：：" + isRemember);
		if (isRemember) {
			cbSavePass.setChecked(true);
			etUsername.setText(mNewsDB.getName());
			etPasswd.setText(mNewsDB.getPass());
		} else {
			cbSavePass.setChecked(false);
			etUsername.setText(mNewsDB.getName());
			etPasswd.setText("");
		}
	}

	private void initView() {
		tvLogin = (TextView) findViewById(R.id.login_btn_login);
		tvSafe = (TextView) findViewById(R.id.login_cb_visible);
		tvRegister = (TextView) findViewById(R.id.login_cb_openvibra);
		etUsername = (EditText) findViewById(R.id.login_edit_account);
		etPasswd = (EditText) findViewById(R.id.login_edit_pwd);
		cbSavePass = (CheckBox) findViewById(R.id.login_cb_savepwd);
	}

	private void initData() {
		tvLogin.setOnClickListener(this);
		tvSafe.setOnClickListener(this);
		tvRegister.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		// 登录
		case R.id.login_btn_login:
			String pass = mNewsDB.getPass();
			if (etPasswd.getText().toString().equals(pass)) {
				// 如果记住密码被勾选
				if (cbSavePass.isChecked()) {
					PrefUtils.setBoolean(this, etUsername.getText().toString(),
							true);

				} else {// 没有记住密码
					PrefUtils.setBoolean(this, etUsername.getText().toString(),
							false);
					System.out.println("不记住密码，执行了没？？？");
				}

				Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();

				finish();
			} else {
				Toast.makeText(this, "账号或密码错误，请重新输入", Toast.LENGTH_SHORT)
						.show();
			}
			break;
		// 忘记密码
		case R.id.login_cb_visible:
			break;
		// 注册
		case R.id.login_cb_openvibra:
			startActivity(new Intent(this, RegisterActivity.class));
			break;

		default:
			break;
		}
	}

}
