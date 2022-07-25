package com.example.onlinemusicapp;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterUserActivity extends AppCompatActivity {

    TextView haveAcc;
    Button btnRegist;
    EditText edtName, edtPass, edtConPass, edtEmail;
    String name, email, password, conPassword;

    private apiService mService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        btnRegist = findViewById(R.id.btn_register);
        edtName = findViewById(R.id.edt_nameUser);
        edtEmail = findViewById(R.id.edt_mailUser);
        edtPass = findViewById(R.id.edt_passUser);
        edtConPass = findViewById(R.id.edt_rePass);

        mService = RetrofitClient.getRetrofitInitializedService();

        haveAcc = findViewById(R.id.tv_alreadyHaveAcc);
        haveAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnRegist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = edtName.getText().toString();
                email = edtEmail.getText().toString();
                password = edtPass.getText().toString();
                conPassword = edtConPass.getText().toString();
                registerUser();
                finish();
            }
        });
    }
        private void registerUser () {
            Users user = new Users();
            user.setName(name);
            user.setEmail(email);
            user.setPassword(password);
            user.setPasswordConfirmation(conPassword);

            if (user.getPasswordConfirmation().equals(user.getPassword())) {
                mService.sendUser(user).enqueue(new Callback<ResponseUser>() {
                    @Override
                    public void onResponse(Call<ResponseUser> call, Response<ResponseUser> response) {
                        if (response.isSuccessful()) {
                            ResponseUser infoUser = response.body();
                            String json = new Gson().toJson(infoUser);
                            Log.d("RegisterActivity", "Dang ky thanh cong: " + json);
                            Toast.makeText(RegisterUserActivity.this, "Đăng ký tài khoản thành công", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e("RegisterActivity", "Dang ky khong thanh cong, loi: " + response.code());
                            Toast.makeText(RegisterUserActivity.this, "Đăng ký không thành công. Vui lòng kiểm tra lại thông tin", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseUser> call, Throwable t) {
                        Log.e("RegisterActivity", "Loi " + t.getMessage());
                    }
                });
            } else {
                Log.d("MainActivity", "Kiem tra lai xac nhan mat khau");
            }
        }




    }

