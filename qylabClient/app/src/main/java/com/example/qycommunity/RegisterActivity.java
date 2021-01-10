package com.example.qycommunity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
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

public class RegisterActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText confirmEditText;
    private TextView errorText;

    private String username;
    private String password;
    private String confirmpassword;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        usernameEditText = findViewById(R.id.usernameReg);
        passwordEditText = findViewById(R.id.password1);
        confirmEditText = findViewById(R.id.password2);
        emailEditText = findViewById(R.id.email);
        errorText = findViewById(R.id.error);
    }

    public void onClickRegister(View view)
    {
        username = usernameEditText.getText().toString();
        password = passwordEditText.getText().toString();
        confirmpassword = confirmEditText.getText().toString();
        email = emailEditText.getText().toString();
        if(email.isEmpty())
        {
            Toast.makeText(RegisterActivity.this,"You have not entered email!",Toast.LENGTH_SHORT).show();
            errorText.setText(R.string.error_empty_email);
            return;
        }
        else if(username.isEmpty())
        {
            Toast.makeText(RegisterActivity.this,"You have not entered the username!",Toast.LENGTH_SHORT).show();
            errorText.setText(R.string.error_empty_username);
            return;
        }
        else if(!password.equals(confirmpassword))
        {
            //Toast.makeText(RegisterActivity.this,password+" "+confirmpassword,Toast.LENGTH_SHORT).show();
            Toast.makeText(RegisterActivity.this,"Your passwords are not the same,please confirm!",Toast.LENGTH_SHORT).show();
            errorText.setText(R.string.error_diff_password);
            return;
        }
        int state=SignUpRequest(username,password,email);
        /*switch(state)
        {
            case 0:
                Toast.makeText(RegisterActivity.this, "Sign up failed: unable to get the database", Toast.LENGTH_SHORT).show();
                errorText.setText(R.string.error_no_net);
                break;
            default:
                Toast.makeText(RegisterActivity.this, "Sign up failed: unspecified error!", Toast.LENGTH_SHORT).show();
                errorText.setText(R.string.error_default);
                break;
        }*/
        return;
    }

    public void onClickBack(View view){
        Intent intent = new Intent( RegisterActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public int SignUpRequest(final String username,final String password,final String email)
    {
        /*
        state represents a special value that implies your register result.
        ----------------------------------------------------------------
        ID      State
        ================================================================
        0       Can't link to database
        1       Success
        2       Email registered
        3       Username registered
        ----------------------------------------------------------------
         */
        int state=0;
        //To be continued..
        String url = "http://10.0.2.2:8080/qyServer0/addUserServlet";
        String tag = "register";
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
                            System.out.println(state);
                            if (state.equals("-1")) {
                                Toast.makeText(RegisterActivity.this, "Register failed!", Toast.LENGTH_SHORT).show();
                                errorText.setText(R.string.error_no_net);
                            }else if(state.equals("1")){
                                Toast.makeText(RegisterActivity.this, "Register successfully!", Toast.LENGTH_SHORT).show();
                            }else if(state.equals("2")){//SignUpSucceed
                                Toast.makeText(RegisterActivity.this, "E-mail used!", Toast.LENGTH_SHORT).show();
                                errorText.setText(R.string.error_used_email);
                            }else if(state.equals("3")){
                                Toast.makeText(RegisterActivity.this, "Username used!", Toast.LENGTH_SHORT).show();
                                errorText.setText(R.string.error_used_username);
                            }else
                            {
                                Toast.makeText(RegisterActivity.this, "I don't know this problem and please call the developers!", Toast.LENGTH_SHORT).show();
                                errorText.setText(R.string.error_default);
                            }
                        } catch (JSONException e) {
                            //做自己的请求异常操作，如Toast提示（“无网络连接”等）
                            Log.e("TAG", e.getMessage(), e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //做自己的响应错误操作，如Toast提示（“请稍后重试”等）
                Log.e("TAG", error.getMessage(), error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("email",email);
                params.put("password", password);
                return params;
            }
        };
        //设置Tag标签
        request.setTag(tag);
        //将请求添加到队列中
        requestQueue.add(request);
        return state;
    }

}
