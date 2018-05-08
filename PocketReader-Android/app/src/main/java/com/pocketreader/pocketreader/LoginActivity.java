package com.pocketreader.pocketreader;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.pocketreader.pocketreader.account.AccountUtils;
import com.pocketreader.pocketreader.account.User;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.exception.BmobException;

/**
 * Created by tony on 5/8/18.
 */

public class LoginActivity extends BaseActivity {
    @BindView(R.id.edt_account)
    EditText edtAccount;
    @BindView(R.id.edt_pwd)
    EditText edtPwd;
    @BindView(R.id.btn_register)
    Button btnRegister;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.tv_result)
    TextView tvResult;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_register, R.id.btn_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_register:
                User user = new User();
                user.setUsername(edtAccount.getText().toString());
                user.setPassword(edtPwd.getText().toString());
                AccountUtils.register(user, new AccountUtils.OnUserListener() {
                    @Override
                    public void onSuccess(User user) {
                        tvResult.setText(user.getSessionToken());
                        gotoMainActivity();
                    }

                    @Override
                    public void onFail(BmobException e) {
                        tvResult.setText(e.toString());
                    }
                });
                break;
            case R.id.btn_login:
                AccountUtils.login(edtAccount.getText().toString(), edtPwd.getText().toString(), new AccountUtils.OnUserListener() {
                    @Override
                    public void onSuccess(User user) {
                        tvResult.setText(user.getSessionToken());
                        gotoMainActivity();
                    }

                    @Override
                    public void onFail(BmobException e) {
                        tvResult.setText(e.toString());
                    }
                });
                break;
        }
    }

    private void gotoMainActivity(){
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }
}
