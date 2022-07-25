package com.example.onlinemusicapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    EditText edtUser, edtPassword;
    TextView tvPassForgot, tvRegister;
    Button btnLogin, btnRegister;
    String email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //MusicAppGlobal.init();
//        SharedPreferences sharedPreferences = getSharedPreferences("MyMusicAppGlobal", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString("user-access-token", "token");
//        editor.apply();
        if (MusicAppGlobal.getInstance().hasLoggedIn()) {
            Intent intent = new Intent(MainActivity.this, Home.class);
            startActivity(intent);
        }
        else {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }
        finish();
    }
}