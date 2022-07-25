package com.example.onlinemusicapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterSongs extends RecyclerView.Adapter<AdapterSongs.ViewHolder> {
    private final List<SOSong> songList;
    private final PostItemListener itemListener;

    public AdapterSongs(List<SOSong> songList, PostItemListener postItemListener) {
        itemListener = postItemListener;
        this.songList = songList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.song, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SOSong song = songList.get(position);
        if (song == null)
            return;
        holder.txtBaiHat.setText(song.getName());
        holder.txtCaSi.setText(song.getSinger().getName());
        holder.relativeLayout.setOnClickListener(v -> itemListener.onPostClick(song));
    }

    @Override
    public int getItemCount() {
        if (songList != null)
            return songList.size();
        return 0;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView txtBaiHat;
        private final TextView txtCaSi;
        private final RelativeLayout relativeLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtBaiHat = itemView.findViewById(R.id.tv_Name);
            txtCaSi = itemView.findViewById(R.id.tv_SingerName);
            relativeLayout = itemView.findViewById(R.id.ll_itemSong);
        }
    }

    public interface PostItemListener {
        void onPostClick(SOSong soSong);
    }
}
