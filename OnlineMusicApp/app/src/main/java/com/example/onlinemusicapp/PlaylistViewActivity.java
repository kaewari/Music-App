package com.example.onlinemusicapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class PlaylistViewActivity extends AppCompatActivity {

    //View value in this activity
    ListView songsListView;
    TextView playlistTitle;
    Button backToPrevious;
    Button playlistOptions;

    @SuppressLint({"RestrictedApi", "NonConstantResourceId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_view);

        playlistTitle = findViewById(R.id.playlistName);
        songsListView = findViewById(R.id.listViewSongsInPlaylist);
        backToPrevious = findViewById(R.id.backToPrevious);
        playlistOptions = findViewById(R.id.playlistMenuView);

        //Back button
        backToPrevious.setOnClickListener(v -> finish());

        //load the songs of playlist
        int playlistId = getIntent().getIntExtra("playlistId", -1);
        Log.d("ID ", String.valueOf(playlistId));
        if (playlistId > -1) {
            //success get playlist information
            com.example.onlinemusicapp.PlaylistUtils.getPlaylist(MusicAppGlobal.getInstance().getAccessToken()
                    , playlistId, new com.example.onlinemusicapp.PlaylistUtils() {
                @Override
                public void doSomething(PlaylistAnswerResponse response) {

                    playlistTitle.setText(response.get_playlistName());

                    ArrayList<SongAnswerResponse> listSongs = new ArrayList<>(response.get_songsList());
                    PlaylistSongAdapter adapter = new PlaylistSongAdapter(PlaylistViewActivity.this,
                            response.get_id(), listSongs);
                    songsListView.setAdapter(adapter);
                }
            });
        }
        else {
            Toast.makeText(getApplicationContext(), "Không tìm thấy playlist này", Toast.LENGTH_SHORT).show();
        }

        //Menu playlist button
        playlistOptions.setOnClickListener(v -> {
            PopupMenu playlistOptionsMenu = new PopupMenu(PlaylistViewActivity.this, v);
            playlistOptionsMenu.getMenuInflater().inflate(R.menu.playlist_option_menu, playlistOptionsMenu.getMenu());
            playlistOptionsMenu.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.editPlaylist:
                        PlaylistUtils.getPlaylist(MusicAppGlobal.getInstance().getAccessToken()
                                , playlistId, new PlaylistUtils() {
                            @Override
                            public void doSomething(PlaylistAnswerResponse response) {
                                Intent editIntent = new Intent(PlaylistViewActivity.this, PlaylistAddEditActivity.class);
                                editIntent.putExtra("AddEditLabel", 0);
                                editIntent.putExtra("playlistName", response.get_playlistName());
                                editIntent.putExtra("playlistId", response.get_id());
                                startActivity(editIntent);
                            }
                        });

                        return true;
                    case R.id.deletePlaylist:
                        PlaylistUtils.deletePlaylist(MusicAppGlobal.getInstance().getAccessToken()
                                , playlistId, new PlaylistUtils() {
                            @Override
                            public void doSomething() {
                                finish();
                            }
                        });
                        return true;
                }
                return false;
            });
            playlistOptionsMenu.show();
        });

    }

    @Override
    protected void onRestart() {
        this.recreate();
        super.onRestart();
    }

    public void restartAndToastNotify(String message) {
        this.onRestart();
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public class PlaylistSongAdapter extends BaseAdapter {

        Context context;
        int playlistId;
        ArrayList<SongAnswerResponse> listSongs;
        LayoutInflater layoutInflater;

        public PlaylistSongAdapter(Context context, int playlistId, ArrayList<SongAnswerResponse> listSongs) {
            this.context = context;
            this.playlistId = playlistId;
            this.listSongs = listSongs;
            this.layoutInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() { return this.listSongs.size(); }

        @Override
        public Object getItem(int position) { return this.listSongs.get(position); }

        @Override
        public long getItemId(int position) {
            return ((SongAnswerResponse) getItem(position)).get_id();
        }

        @SuppressLint({"ViewHolder", "InflateParams", "NonConstantResourceId"})
        @Override
        public View getView(int position, View view, ViewGroup parent) {
            view = layoutInflater.inflate(R.layout.playlist_song_item,null);

            TextView playlistSong_name = view.findViewById(R.id.playlistSong_name);
            TextView playlistSong_singer = view.findViewById(R.id.playlistSong_singer);
            Button playlistSongOptions = view.findViewById(R.id.playlistSong_option);

            playlistSong_name.setText(listSongs.get(position).get_songName());
            playlistSong_singer.setText(listSongs.get(position).get_singer().get_name());
            playlistSongOptions.setOnClickListener(v -> {
                PopupMenu playlistSongOptionsMenu = new PopupMenu(PlaylistViewActivity.this, v);
                playlistSongOptionsMenu.getMenuInflater().inflate(R.menu.song_in_playlist_option_menu,
                        playlistSongOptionsMenu.getMenu());
                playlistSongOptionsMenu.setOnMenuItemClickListener(item -> {
                    switch (item.getItemId()) {
                        case R.id.playlistSong_play:
                            Intent intent = new Intent(getApplicationContext(), PlayerPlayList.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("cakhuc", listSongs.get(position));
                            bundle.putParcelableArrayList("danhsachnhac", listSongs);
                            intent.putExtras(bundle);
                            startActivity(intent);

                            return true;
                        case R.id.playlistSong_remove:
                            if (listSongs.size() <= 1) {
                                PlaylistUtils.deletePlaylist(MusicAppGlobal.getInstance().getAccessToken()
                                        , playlistId, new PlaylistUtils() {
                                    @Override
                                    public void doSomething() {
                                        finish();
                                    }
                                });
                            }
                            else {
                                PlaylistUtils.removeSongFromPlaylist(MusicAppGlobal.getInstance().getAccessToken(),
                                        playlistId, listSongs.get(position).get_id(), new PlaylistUtils() {
                                            @Override
                                            public void doSomething() {
                                                //do something when response is returned
                                                PlaylistViewActivity.this.restartAndToastNotify(
                                                        "Thao tác xóa thành công");
                                            }
                                        });
                            }
                            return true;
                        default:
                            return false;
                    }
                });
                playlistSongOptionsMenu.show();
            });

            return view;
        }
    }
}