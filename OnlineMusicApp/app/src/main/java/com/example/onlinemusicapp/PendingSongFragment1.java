package com.example.onlinemusicapp;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.List;

import retrofit2.Response;

public class PendingSongFragment1 extends Fragment {

    ListView songsListView;

    public PendingSongFragment1() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_pending_songs, container);
        songsListView = (ListView) v.findViewById(R.id.listView_peding_songs);
        PlaylistUtils.getPendingSong(MusicAppGlobal.getInstance().getAccessToken(), new PlaylistUtils() {
            @Override
            public void doSomething(Response<List<SongAnswerResponse>> response) {
                PendingSongAdapter adapter = new PendingSongAdapter(v.getContext(), response.body());
                songsListView.setAdapter(adapter);
            }
        });


        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public class PendingSongAdapter extends BaseAdapter {

        Context context;
        List<SongAnswerResponse> listSongs;
        LayoutInflater layoutInflater;

        public PendingSongAdapter(Context context, List<SongAnswerResponse> listSongs) {
            this.context = context;
            this.listSongs = listSongs;
            this.layoutInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return this.listSongs.size();
        }

        @Override
        public Object getItem(int position) {
            return this.listSongs.get(position);
        }

        @Override
        public long getItemId(int position) {
            return ((SongAnswerResponse) getItem(position)).get_id();
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            view = layoutInflater.inflate(R.layout.playlist_song_item, null);

            TextView playlistSong_songName = (TextView) view.findViewById(R.id.playlistSong_name);
            TextView playlistSong_singer = (TextView) view.findViewById(R.id.playlistSong_singer);
            Button playlistSongOptions = (Button) view.findViewById(R.id.playlistSong_option);

            playlistSong_songName.setText(listSongs.get(position).get_songName());
            playlistSong_singer.setText(listSongs.get(position).get_singer().get_name());
            playlistSongOptions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu playlistSongOptionsMenu = new PopupMenu(getContext(), v);
                    playlistSongOptionsMenu.getMenuInflater().inflate(R.menu.song_in_pendings_menu,
                            playlistSongOptionsMenu.getMenu());
                    playlistSongOptionsMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.pendingSong_OK:
                                    //verified here
                                    return true;
                                case R.id.pendingSong_remove:
                                    //delete song here
                                    return true;
                                default:
                                    return false;
                            }
                        }
                    });
                    playlistSongOptionsMenu.show();
                }
            });

            return view;
        }
    }
}
