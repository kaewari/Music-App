package com.example.onlinemusicapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Home extends AppCompatActivity {

    TextView tv1;
    EditText edtName, edtPath, edtSingerId;
    Button btnThemBaiHat, btnThemCaSi, btnXemBaiHat;
    String string, Name, songPath, receiveToken;
    int songId, singerId, songSingerId;
    FrameLayout frameLayout;
    BottomNavigationView bottomNavigationView;
    final String TAG_FRAGMENT = "fragment_music_app";

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        if (MusicAppGlobal.getInstance().getUserMode() == MusicAppGlobal.UserMode.User) {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_home);

            bottomNavigationView = findViewById(R.id.MenuNavigation);
            //Mặc định HomeFragment
            getSupportFragmentManager().beginTransaction().replace(R.id.fl_Container, new com.example.onlinemusicapp.HomeFragment()).commit();


            bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment fragment = new HomeFragment();
                    switch (item.getItemId()) {
                        case R.id.it_MuiscList:
                            fragment = new HomeFragment();
                            break;
                        case R.id.it_Playlist:
                            fragment = new FragmentPlaylist();
                            break;
                        case R.id.it_Player:
                            //Gửi token sang PlayerActivity
                            Intent player = new Intent(getApplicationContext(), Player.class).setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                            startActivity(player);
                            break;
                        case R.id.it_Search:
                            fragment = new SearchFragment();
                            break;
                        case R.id.it_InfoUser:
                            fragment = new InfoUserFragment();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fl_Container, fragment)
                            .commit();
                    return true;
                }
            });
        }
        else {
            Log.d("Admin la ", "day");
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_home_admin);

            bottomNavigationView = findViewById(R.id.MenuNavigation);

            getSupportFragmentManager().beginTransaction().replace(R.id.fl_Container, new PendingSongFragment()).commit();

            bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment fragment = new HomeFragment();
                    switch (item.getItemId()) {
                        case R.id.it_MuiscList:
                            fragment = new PendingSongFragment();
                            break;
                        case R.id.it_Search:
                            fragment = new SearchFragment();
                            break;
                        case R.id.it_InfoUser:
                            fragment = new InfoUserFragment();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fl_Container, fragment)
                            .commit();
                    return true;
                }
            });
        }
    }
}

