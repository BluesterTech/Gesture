package cn.bluester.recordtask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import org.xutils.common.Callback;
import org.xutils.common.util.MD5;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.Set;

import cn.bluester.recordtask.util.CacheUtils;
import cn.bluester.recordtask.util.MyConstants;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

public class LoginActivity extends Activity {

    private EditText userNameEdit, passwordEdit;
    private Button loginBtn;
    private Context mContext;
    //用户名
    private String username;
    //密码
    private String pwd;
    //登录过期时间
    private long loginTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        //初始化XUtils3
        x.Ext.init(this.getApplication());

        mContext = this;
        userNameEdit = (EditText) findViewById(R.id.et_username);
        passwordEdit = (EditText) findViewById(R.id.et_password);
        loginBtn = (Button) findViewById(R.id.btn_login);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(getMacAddress());
//                Toast.makeText(mContext,getMacAddress(),Toast.LENGTH_SHORT).show();
                requestLogin();
                loginBtn.setClickable(false);
            }
        });
        //判断token是否过期
        if (System.currentTimeMillis() < CacheUtils.getLong(mContext, MyConstants.LOGINTIME, 0)) {
            Intent intent = new Intent(mContext, MainActivity.class);
            startActivity(intent);
        }

        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        JPushInterface.setAlias(this, CacheUtils.getString(this, MyConstants.MACADRESS,getMacAddress()), new TagAliasCallback() {

            @Override
            public void gotResult(int arg0, String arg1, Set<String> arg2) {
                // TODO Auto-generated method stub
                Log.i("TAG", "alias="+arg1);
            }
        });
    }

    //获取mac地址
    private String getMacAddress() {
        String macAddress = CacheUtils.getString(mContext, MyConstants.MACADRESS, null);
        if (macAddress == null) {
            WifiManager wifi = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = wifi.getConnectionInfo();
            macAddress = info.getMacAddress();
            if (null == macAddress) {
                return "";
            }
            macAddress = macAddress.replace(":", "");
            CacheUtils.putValue(mContext, MyConstants.MACADRESS, macAddress);
        }
        return macAddress;
    }

    //请求登录
    private void requestLogin() {
        this.username = userNameEdit.getText().toString().trim();
        this.pwd = passwordEdit.getText().toString().trim();
        String macAddress = getMacAddress();

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(pwd)) {
            Toast.makeText(this, "用户名或密码不能为空", Toast.LENGTH_SHORT).show();
            loginBtn.setClickable(true);
            return;
        } else {
            pwd = MD5.md5(pwd);
        }

        String loginUrl = MyConstants.LOGINURL + "?keyAPI=" + macAddress + "@" + username + "&passWd=" + pwd;
        RequestParams params = new RequestParams(loginUrl);
        params.setAsJsonContent(true);//需放在设置参数之前
        params.addHeader("Content-Type", "application/json");
//        params.addBodyParameter("keyAPI", macAddress + "@" + username);
//        params.addBodyParameter("passWd", pwd);
        requestByPost(params);
//        Log.i(mContext.getClass().getName(), "isJsonContent:" + params.isAsJsonContent() + "--headers:" + params.getHeaders() + "--content:" + params.getBodyContent() + "--param:" + params.getBodyParams());
    }

    //post请求
    private void requestByPost(final RequestParams params) {
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.i(mContext.getClass().getName(), result);
                JSONObject jsonObject = JSON.parseObject(result);
                String suc = (String) jsonObject.get("suc");
                if ("0".equals(suc)) {
                    String error = (String) jsonObject.get("error");
                    Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show();
                } else {
                    String token = (String) jsonObject.get("token");
                    String uid = (String) jsonObject.get("uid");

                    //保存相关数据
                    CacheUtils.putValue(mContext, MyConstants.TOKEN, token);
                    CacheUtils.putValue(mContext, MyConstants.USERNAME, username);
                    CacheUtils.putValue(mContext, MyConstants.PASSWORD, pwd);
                    CacheUtils.putValue(mContext, MyConstants.UID, uid);

                    //获取系统时间，30天登录有效期
                    long currentTime = System.currentTimeMillis();
                    loginTime = currentTime + 30 * 24 * 3600 * 1000L;
                    CacheUtils.putValue(mContext, MyConstants.LOGINTIME, loginTime);

                    Intent intent = new Intent(mContext, MainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(mContext, "网络请求出错", Toast.LENGTH_SHORT).show();
                loginBtn.setClickable(true);
            }

            @Override
            public void onCancelled(CancelledException cex) {
                loginBtn.setClickable(true);
            }

            @Override
            public void onFinished() {
                loginBtn.setClickable(true);
            }
        });
    }
    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        JPushInterface.onPause(this);
    }
    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        JPushInterface.onResume(this);
    }

}
