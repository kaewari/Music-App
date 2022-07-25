package com.example.onlinemusicapp;

import android.annotation.SuppressLint;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

public class PendingSongFragment extends Fragment {

//    ListView songsListView;

    RecyclerView recyclerView;
    PendingSongsAdapter adapter;
    private ArrayList<SongAnswerResponse> listSong = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_pending_songs, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = (RecyclerView) view.findViewById(R.id.listView_peding_songs);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new PendingSongsAdapter(listSong, new AdapterSongs.PostItemListener() {
            @Override
            public void onPostClick(SOSong soSong) {
                //do something here
            }
        });

        recyclerView.setAdapter(adapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        refresh();
    }

    private void refresh() {
        PlaylistUtils.getPendingSong(MusicAppGlobal.getInstance().getAccessToken(), new PlaylistUtils() {
            @Override
            public void doSomething(Response<List<SongAnswerResponse>> response) {
                listSong.addAll(response.body());
                adapter.notifyDataSetChanged();
            }
        });
    }

    public static class PendingSongsAdapter extends RecyclerView.Adapter<PendingSongsAdapter.ViewHolder> {

        private List<SongAnswerResponse> listPendingSongs;
        private com.example.onlinemusicapp.AdapterSongs.PostItemListener itemListener;
        public PendingSongsAdapter(List<SongAnswerResponse> songs, com.example.onlinemusicapp.AdapterSongs.PostItemListener postItemListener) {
            listPendingSongs = songs;
            itemListener = postItemListener;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.playlist_song_item, parent, false);
            PendingSongsAdapter.ViewHolder vHolder = new ViewHolder(view);
            return vHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            SongAnswerResponse song = listPendingSongs.get(position);

            holder.tvSongName.setText(listPendingSongs.get(position).get_songName());
            holder.tvSingerName.setText(listPendingSongs.get(position).get_singer().get_name());
            holder.btnOptions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu playlistSongOptionsMenu = new PopupMenu(holder.itemView.getContext(), v);
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
        }

        @Override
        public int getItemCount() {
            return listPendingSongs.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvSongName, tvSingerName;
            Button btnOptions;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                tvSongName = itemView.findViewById(R.id.playlistSong_name);
                tvSingerName = itemView.findViewById(R.id.playlistSong_singer);
                btnOptions = itemView.findViewById(R.id.playlistSong_option);
            }
        }


    public interface PostItemListener {
            void onPostClick(SongAnswerResponse song);
        }
    }
}
