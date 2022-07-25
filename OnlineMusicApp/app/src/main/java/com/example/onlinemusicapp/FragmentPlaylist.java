package com.example.onlinemusicapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FragmentPlaylist extends Fragment {

    RecyclerView recyclerView;
    SearchView searchView;
    GridCellAdapter adapter;
    Button addPlaylistButton;

    private List<PlaylistAnswerResponse> listPlaylists  = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_playlist, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = (RecyclerView) view.findViewById(R.id.playGridView);
        GridLayoutManager manager = new GridLayoutManager(getContext(), 2
                , GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        adapter = new GridCellAdapter(listPlaylists, new GridCellAdapter.PostItemListener() {
            @Override
            public void onPostClick(SongAnswerResponse song) {
                //do something
            }
        });

        recyclerView.setAdapter(adapter);

        searchView = (SearchView) view.findViewById(R.id.playlist_search);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return true;
            }
        });

        addPlaylistButton = (Button) view.findViewById(R.id.addPlaylistButton);
        addPlaylistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addPlaylistIntent = new Intent(getContext(), PlaylistAddEditActivity.class);
                addPlaylistIntent.removeExtra("AddEditLabel");
                addPlaylistIntent.putExtra("AddEditLabel", 1);
                startActivity(addPlaylistIntent);
            }
        });

    }

    @Override
    public void onResume() {
        if (listPlaylists.size() > 0) {
            listPlaylists.clear();
        }
        this.refresh();
        Log.d("Playlist Status ", "Updated");
        super.onResume();
    }



    public void refresh() {
        PlaylistUtils.getAllPlaylists(MusicAppGlobal.getInstance().getAccessToken(), new PlaylistUtils() {
            @Override
            public void doSomething(List<PlaylistAnswerResponse> response) {
                listPlaylists.addAll(response);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void doSomething() {
                //null list
                adapter.notifyDataSetChanged();
            }
        });
    }

    public static class GridCellAdapter extends RecyclerView.Adapter<GridCellAdapter.ViewHolder> implements Filterable {

        private List<PlaylistAnswerResponse> listPlaylist;
        private List<PlaylistAnswerResponse> showListPlaylist;
        private PostItemListener itemListener;

        public GridCellAdapter(List<PlaylistAnswerResponse> playlists, PostItemListener postItemListener) {
            listPlaylist = playlists;
            showListPlaylist = playlists;
            itemListener = postItemListener;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            PlaylistAnswerResponse playlist = listPlaylist.get(position);
            holder.label.setText(playlist.get_playlistName());
            holder.linearLayoutPlaylist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent viewPlaylist = new Intent(holder.context, PlaylistViewActivity.class);
                    viewPlaylist.removeExtra("playlistId");
                    viewPlaylist.putExtra("playlistId", playlist.get_id());
                    holder.context.startActivity(viewPlaylist);
                }
            });

        }

        @Override
        public int getItemCount() {
            if (showListPlaylist != null) {
                return showListPlaylist.size();
            }
            return 0;
        }

        public class ViewHolder extends RecyclerView.ViewHolder{
            LinearLayout linearLayoutPlaylist;
            ImageView imageView;
            TextView label;
            Context context;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                linearLayoutPlaylist = itemView.findViewById(R.id.linearLayoutPlaylist);
                imageView = itemView.findViewById(R.id.imagePlaylist);
                label = itemView.findViewById(R.id.playlistName);
                context = itemView.getContext();
            }
        }

        public interface PostItemListener {
            void onPostClick(SongAnswerResponse song);
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {

                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults results = new FilterResults();
                    if (constraint == null || constraint.length() == 0) {
                        results.count = listPlaylist.size();
                        results.values = listPlaylist;
                    }
                    else {
                        Log.d("C ", constraint.toString().toLowerCase());
                        List<PlaylistAnswerResponse> itemResult = new ArrayList<>();
                        for (PlaylistAnswerResponse item : listPlaylist) {
                            Log.d("Item ", item.get_playlistName());
                            if (item.get_playlistName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                                itemResult.add(item);
                                Log.d("aaaa", "OK");
                            }
                        }
                        results.count = itemResult.size();
                        results.values = itemResult;
                    }
                    return results;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    showListPlaylist = (List<PlaylistAnswerResponse>) results.values;
                    notifyDataSetChanged();
                }
            };
            return filter;
        }
    }
}
