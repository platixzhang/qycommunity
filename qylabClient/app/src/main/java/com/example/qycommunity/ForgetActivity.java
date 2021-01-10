package com.example.qycommunity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ForgetActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_forget);
        usernameEditText = findViewById(R.id.usernameForget);
        passwordEditText = findViewById(R.id.password1);
        confirmEditText = findViewById(R.id.password2);
        emailEditText = findViewById(R.id.email);
        errorText = findViewById(R.id.error);
    }

    public void onClickConfrim(View view)
    {
        username = usernameEditText.getText().toString();
        password = passwordEditText.getText().toString();
        confirmpassword = confirmEditText.getText().toString();
        email = emailEditText.getText().toString();
        if(email.isEmpty())
        {
            Toast.makeText(ForgetActivity.this,"You have not entered email!",Toast.LENGTH_SHORT).show();
            errorText.setText(R.string.error_empty_email);
            return;
        }
        else if(username.isEmpty())
        {
            Toast.makeText(ForgetActivity.this,"You have not entered the username!",Toast.LENGTH_SHORT).show();
            errorText.setText(R.string.error_empty_username);
            return;
        }
        else if(!password.equals(confirmpassword))
        {
            //Toast.makeText(RegisterActivity.this,password+" "+confirmpassword,Toast.LENGTH_SHORT).show();
            Toast.makeText(ForgetActivity.this,"Your passwords are not the same,please confirm!",Toast.LENGTH_SHORT).show();
            errorText.setText(R.string.error_diff_password);
            return;
        }
        ForgetRequest(username,password,email);
    }

    public int ForgetRequest(final String username,final String password,final String email) {
        /*
        state represents a special value that implies your register result.
        ----------------------------------------------------------------
        ID      State
        ================================================================
        0       Can't link to database
        1       Success
        other   Failed
        ----------------------------------------------------------------
         */
        int state = 0;
        String url = "http://10.0.2.2:8080/qyServer0/forgetServlet";
        String tag = "forget";
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
                                Toast.makeText(ForgetActivity.this, "Password reset failed!", Toast.LENGTH_SHORT).show();
                                errorText.setText(R.string.error_no_net);
                                errorText.setTextColor(android.graphics.Color.RED);
                            }else if(state.equals("1")){
                                Toast.makeText(ForgetActivity.this, "Password reset successfully!", Toast.LENGTH_SHORT).show();
                                errorText.setText(R.string.reset_ok);
                                errorText.setTextColor(android.graphics.Color.GREEN);
                            }else
                            {
                                Toast.makeText(ForgetActivity.this, "I don't know this problem and please call the developers!", Toast.LENGTH_SHORT).show();
                                errorText.setText(R.string.error_default);
                                errorText.setTextColor(android.graphics.Color.RED);
                            }
                        } catch (JSONException e) {
                            //做自己的请求异常操作
                            Log.e("TAG", e.getMessage(), e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //error
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
        //To be continue..
        return state;
    }
}
