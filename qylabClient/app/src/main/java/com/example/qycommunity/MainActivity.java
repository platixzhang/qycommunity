package com.example.qycommunity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;
import android.view.View;
import android.content.Intent;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

public class MainActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;

    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        usernameEditText = findViewById(R.id.et1);
        passwordEditText = findViewById(R.id.et2);
    }

    public void onClickSignIn(View view)
    {
        username = usernameEditText.getText().toString();
        password = passwordEditText.getText().toString();
        if(username.isEmpty()||password.isEmpty()||password.length()<6){
            Toast.makeText(MainActivity.this,"Login failed: invalid info",Toast.LENGTH_SHORT).show();
            return;
        }
        SignInRequest(username,password);
    }

    public void onClickSignUp(View view){
        Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    public void onClickForget(View view){
        Intent intent = new Intent(MainActivity.this, ForgetActivity.class);
        startActivity(intent);
    }

    public void SignInRequest(final String username,final String password)
    {
        /*
        state represents a special value that implies your login result.
        ----------------------------------------------------------------
        ID      State
        ================================================================
        0       Can't link to database
        1       Success
        2       login failed
        ----------------------------------------------------------------
         */
        int state=0;
        //main function is to call a servlet to submit the info.
        //String url = "http://10.0.2.2:8080/qyServer0/loginServlet?username="+username+"&password="+password;
        String url = "http://10.0.2.2:8080/qyServer0/loginServlet";
        String tag = "login";
        //取得请求队列
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        //防止重复请求，所以先取消tag标识的请求队列
        requestQueue.cancelAll(tag);
        //post your info
        final StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //System.out.println("===============\noh shit!"+response);
                            JSONObject jsonObject = (JSONObject) new JSONObject(response).get("result");
                            String state=jsonObject.getString("rtnvalue");
                            //System.out.println(state);
                            if (state.equals("-1")) {
                                Toast.makeText(MainActivity.this, "Login failed!", Toast.LENGTH_SHORT).show();
                            }else if(state.equals("1")){
                                Toast.makeText(MainActivity.this, "Welcome!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(MainActivity.this, MainUserActivity.class);
                                intent.putExtra("username",username);                  //可以进行其他动作的界面
                                startActivity(intent);
                            }else {//SignUpSucceed
                                Toast.makeText(MainActivity.this, "Check your password!", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            //做自己的请求异常操作，如Toast提示（“无网络连接”等）
                            Log.e("TAG", e.getMessage(), e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("password", password);
                return params;
            }
        };
        //设置Tag标签
        request.setTag(tag);
        //将请求添加到队列中
        requestQueue.add(request);
    }
}
