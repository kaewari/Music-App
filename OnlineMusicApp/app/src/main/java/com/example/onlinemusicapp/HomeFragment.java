package com.example.onlinemusicapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    Home home;
    String token;
    apiService mService;
    RecyclerView recyclerView;
    AdapterSongs adapter;
    private ArrayList<SOSong> songList;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Chỉnh layout cho fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        home = (Home) getActivity();

        //Lấy token từ HomeActivity

        recyclerView = v.findViewById(R.id.rv_1);
        mService = RetrofitClient.getRetrofitInitializedService();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        songList = new ArrayList<>();
        adapter = new AdapterSongs(songList, new AdapterSongs.PostItemListener() {
            @Override
            public void onPostClick(SOSong song) {
                onClickGoToPlayer(song);
                Toast.makeText(getActivity(), "Ban dang nghe bai hat" + song.getName()
                        + " cua ca si " + song.getSinger().getName(), Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setAdapter(adapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        getSong();
        return v;
    }

    //  Tải danh sách bài hát
    private void getSong() {
        mService.getSong().enqueue(new Callback<List<SOSong>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<List<SOSong>> call, Response<List<SOSong>> response) {
                if (response.isSuccessful()) {
                    songList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                    Toast.makeText(getActivity(), "Loading...", Toast.LENGTH_LONG).show();
                    Log.d("HomeActivity", "Load list song successful!" + " " + token);
                } else {
                    Toast.makeText(getActivity(), "Load list song unsuccessful!", Toast.LENGTH_SHORT).show();
                    Log.d("HomeActivity", "Load list song unsuccessful! " + response.code());
                }
            }
            @Override
            public void onFailure(Call<List<SOSong>> call, Throwable t) {
                Log.d("HomeActivity", "Error " + t.getMessage());
            }
        });
    }

    //  Nhấn nghe nhạc
    private void onClickGoToPlayer(SOSong song){
        Intent intent = new Intent(getContext(), Player.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("cakhuc", song);
        bundle.putParcelableArrayList("danhsachnhac", songList);
        intent.putExtras(bundle);
        startActivity(intent);
    }

}