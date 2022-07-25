package com.example.onlinemusicapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private static final String INTERNAL_PATH= Environment.getDataDirectory().getPath() + "/data/com.example.user/";
    private static final String ACCOUNT_FILE = "acc.txt";
    TextView btnRes;
    EditText edtUser, edtPassword;
    Button btnLogin, btnFogotPass;
    String email, password;

    private apiService mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtUser = findViewById(R.id.edt_loginUser);
        edtPassword = findViewById(R.id.edt_loginPass);
        btnLogin = findViewById(R.id.bth_login);

        mService = RetrofitClient.getRetrofitInitializedService();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = edtUser.getText().toString();
                password = edtPassword.getText().toString();
                login();
            }
        });

        //Vào activity Register
        btnRes = findViewById(R.id.tv_register);
        btnRes.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, RegisterUserActivity.class);
            startActivity(intent);
        });

    }
    private void login () {
        Users loginUser = new Users();
        loginUser.setEmail(email);
        loginUser.setPassword(password);

        mService.login(loginUser).enqueue(new Callback<ResponseUser>() {
            @Override
            public void onResponse(Call<ResponseUser> call, Response<ResponseUser> response) {
                if (response.isSuccessful()) {
                    String info = response.body().getToken();
                    String info1 = response.body().setToken(info);
                    Log.d("login", "Dang nhap thanh cong");
                    Log.d("login", "Thong tin user: " + info);
                    //save token
                    SharedPreferences preferences = getSharedPreferences("MyMusicAppGlobal", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("user-access-token","Bearer " + "token");
                    MusicAppGlobal.getInstance().setAccessToken(response.body().getToken());
                    MusicAppGlobal.getInstance().setAccessMode(
                            response.body().getRole()
                    );
                    Log.d("mode 2", String.valueOf(MusicAppGlobal.getInstance().getUserMode()));
                    //get into homepage
                    Intent home = new Intent(LoginActivity.this, Home.class);
                    home.putExtra("user_Name", email);
                    startActivity(home);
                    finish();
                } else {
                    Log.d("login", "Dang nhap khong thanh cong: " + response.code());
                }}



            @Override
            public void onFailure(Call<ResponseUser> call, Throwable t) {
                Log.d("RegisterActivity", "Loi: " + t.getMessage());
            }
        });



    }

}
