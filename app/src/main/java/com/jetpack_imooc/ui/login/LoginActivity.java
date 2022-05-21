package com.jetpack_imooc.ui.login;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.jetpack.libcommon.utils.EasyLog;
import com.jetpack.libcommon.utils.ToastUtils;
import com.jetpack.libnetwork.net.ApiResponse;
import com.jetpack.libnetwork.net.ApiService;
import com.jetpack.libnetwork.net.JsonCallback;
import com.jetpack_imooc.R;
import com.jetpack_imooc.model.User;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 登录
 * @author dhl
 * @date 2022 0428
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {


    private static final String TAG = "LoginActivity";

    private View actionClose;
    private View actionLogin;

    private Tencent tencent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        actionClose = findViewById(R.id.action_close);
        actionLogin = findViewById(R.id.action_login);
        actionClose.setOnClickListener(this);
        actionLogin.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.action_close){
            finish();
        }else if(v.getId() == R.id.action_login){
            login();
        }
    }

    private void login() {
        if (tencent == null) {
            tencent = Tencent.createInstance("101794421", getApplicationContext());
        }
        tencent.login(this, "all", loginListener);
    }
    IUiListener loginListener = new IUiListener() {
        @Override
        public void onComplete(Object o) {
            JSONObject response = (JSONObject) o;
            EasyLog.e(TAG,"rsp"+response);
            try {
                String openid = response.getString("openid");
                String access_token = response.getString("access_token");
                String expires_in = response.getString("expires_in");
                long expires_time = response.getLong("expires_time");

                tencent.setOpenId(openid);
                tencent.setAccessToken(access_token, expires_in);
                QQToken qqToken = tencent.getQQToken();
                getUserInfo(qqToken, expires_time, openid);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(UiError uiError) {
            Toast.makeText(getApplicationContext(), "登录失败:reason" + uiError.toString(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel() {
            Toast.makeText(getApplicationContext(), "登录取消", Toast.LENGTH_SHORT).show();
        }
    };
    private void getUserInfo(QQToken qqToken, long expires_time, String openid) {
        UserInfo userInfo = new UserInfo(getApplicationContext(), qqToken);
        userInfo.getUserInfo(new IUiListener() {
            @Override
            public void onComplete(Object o) {
                JSONObject response = (JSONObject) o;

                EasyLog.e(TAG,"rsp"+response);
                try {
                    String nickname = response.getString("nickname");
                    String figureurl_2 = response.getString("figureurl_2");

                    save(nickname, figureurl_2, openid, expires_time);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(UiError uiError) {
                Toast.makeText(getApplicationContext(), "登录失败:reason" + uiError.toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), "登录取消", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void save(String nickname, String avatar, String openid, long expires_time) {
        ApiService.get("/user/insert")
                .addParam("name",nickname)
                .addParam("avatar",avatar)
                .addParam("qqOpenId",openid)
                .addParam("expires_time",expires_time)
                .execute(new JsonCallback<User>() {
                    @Override
                    public void onSuccess(ApiResponse<User> response) {
                        if(response.body != null){
                            UserManager.get().save(response.body);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    finish();
                                }
                            });
                        }else{
                            ToastUtils.INSTANCE.show("登录失败");
                        }

                    }

                    @Override
                    public void onError(ApiResponse response) {
                        ToastUtils.INSTANCE.show("登录失败");
                    }
                });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_LOGIN) {
            Tencent.onActivityResultData(requestCode, resultCode, data, loginListener);
        }
    }




}