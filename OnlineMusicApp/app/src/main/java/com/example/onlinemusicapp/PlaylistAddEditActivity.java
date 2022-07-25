package com.example.onlinemusicapp;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Response;

public class PlaylistAddEditActivity extends AppCompatActivity {

    private static final int MAX_SHOWED_TO_ADD = 25;

    Button backToPrevious;
    TextView textLabelFunction;
    RelativeLayout addSongLayout;
    Button submit;
    EditText editPlaylistName;
    ArrayList<Integer> chosenSongForAdding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_add_edit);

        submit = (Button) findViewById(R.id.submitButton);
        addSongLayout = (RelativeLayout) findViewById(R.id.addSongLayout);
        editPlaylistName = (EditText) findViewById(R.id.playlistName_createEdit);

        backToPrevious = (Button) findViewById(R.id.backToPrevious);
        backToPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        textLabelFunction = (TextView) findViewById(R.id.functionLabel);
        ListView listViewSong;
        if (getIntent().getIntExtra("AddEditLabel", 1) == 1) { //add new
            textLabelFunction.setText("Thêm danh sách mới");
            chosenSongForAdding = new ArrayList<>();
            ((TextView) findViewById(R.id.addSongLayoutTextView)).setVisibility(View.VISIBLE);
            addSongLayout.setVisibility(View.VISIBLE);

            SearchView searchSong = (SearchView) findViewById(R.id.searchSongToAddToPlaylist);
             listViewSong = (ListView) findViewById(R.id.listSongToAdd);

            PlaylistUtils.getAllSongs(new PlaylistUtils() {
                @Override
                public void doSomething(Response<List<SongAnswerResponse>> response) {
                    List<SongAnswerResponse> listSongs = response.body();


                    PlaylistAddSongAdapter adapter = new PlaylistAddSongAdapter(getApplicationContext(),
                            listSongs);
                    listViewSong.setAdapter(adapter);

                    searchSong.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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


                }
            });


            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ArrayList<Integer> chosenSong = ((PlaylistAddSongAdapter)listViewSong.getAdapter()).getChosenSong();
                    PlaylistUtils.addNewPlaylist(MusicAppGlobal.getInstance().getAccessToken(),
                            editPlaylistName.getText().toString(), chosenSong, new PlaylistUtils());
                    finish();
                }
            });

        }
        else { //change name exist item
            textLabelFunction.setText("Sửa thông tin");
            submit.setText("Hoàn tất");
            getIntent().putExtra("needToLoad", true);

           editPlaylistName.setText(
                    getIntent().getStringExtra("playlistName")
            );


           submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PlaylistUtils.editPlaylist(MusicAppGlobal.getInstance().getAccessToken(),
                            getIntent().getIntExtra("playlistId", -1),
                            editPlaylistName.getText().toString(), new PlaylistUtils() {
                                @Override
                                public void doSomething() {
                                    Toast.makeText(PlaylistAddEditActivity.this, "Đổi tên thành công", Toast.LENGTH_SHORT).show();
                                }
                            });
                    finish();
                }
           });

        }


    }

    public class PlaylistAddSongAdapter extends BaseAdapter implements Filterable {

        Context context;
        List<SongAnswerResponse> listSongs;
        List<SongAnswerResponse> listSongsFilter;
        ArrayList<Integer> chosenSong;
        List<SongAnswerResponse> songWithNullSearch;
        LayoutInflater layoutInflater;

        public PlaylistAddSongAdapter(Context context, List<SongAnswerResponse> listSongs) {
            this.context = context;
            this.listSongs = listSongs;
            this.listSongsFilter = listSongs;
            this.chosenSong = new ArrayList<>();
            this.layoutInflater = LayoutInflater.from(context);

            this.songWithNullSearch = new ArrayList<>();
            if (listSongsFilter != null && listSongsFilter.size() > 0) {

                while (true)
                {
                    int randomizedSongIndex = new Random().nextInt(listSongsFilter.size());
                    if (this.songWithNullSearch.contains(listSongsFilter.get(randomizedSongIndex))) {
                        continue;
                    }
                    this.songWithNullSearch.add(listSongsFilter.get(randomizedSongIndex));
                    if (this.songWithNullSearch.size() >= MAX_SHOWED_TO_ADD || this.songWithNullSearch.size() >= listSongsFilter.size()) { //max size of default list song
                        break;
                    }
                }
                this.listSongs = this.songWithNullSearch;
            }
        }

        public ArrayList<Integer> getChosenSong() { return this.chosenSong; }

        @Override
        public int getCount() {
            return listSongs.size();
        }

        @Override
        public Object getItem(int position) {
            return listSongs.get(position);
        }

        @Override
        public long getItemId(int position) {
            return listSongs.get(position).get_id();
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            view = layoutInflater.inflate(R.layout.playlist_add_song, null);

            TextView songName = (TextView) view.findViewById(R.id.playlistSong_name);
            TextView songSinger = (TextView) view.findViewById(R.id.playlistSong_singer);
            Button btnAdd = (Button) view.findViewById(R.id.btn_add);

            songName.setText(listSongs.get(position).get_songName());
            songSinger.setText(listSongs.get(position).get_singer().get_name());
            if (!isChosen(listSongs.get(position))) { //not yet
                btnAdd.setBackground(getResources().getDrawable(R.drawable.ic_baseline_add_circle_outline_24));
            }
            else { //have been added -> remove
                btnAdd.setBackground(getResources().getDrawable(R.drawable.ic_baseline_remove_circle_outline_24));
            }
            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isChosen(listSongs.get(position))) { //not yet
                        chosenSong.add((Integer)listSongs.get(position).get_id());
                        btnAdd.setBackground(getResources().getDrawable(R.drawable.ic_baseline_remove_circle_outline_24));
                    }
                    else { //have been added -> remove
                        chosenSong.remove((Integer)listSongs.get(position).get_id());
                        btnAdd.setBackground(getResources().getDrawable(R.drawable.ic_baseline_add_circle_outline_24));
                    }
                }
            });
            return view;
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint == null || constraint.length() == 0) {
                        Log.d("Offf", String.valueOf(songWithNullSearch.size()));
                        filterResults.count = songWithNullSearch.size();
                        filterResults.values = songWithNullSearch;
                    }
                    else {
                        String charSequence = constraint.toString().toLowerCase();
                        List<SongAnswerResponse> shownSongList = new ArrayList<>();
                        if (listSongsFilter != null && listSongsFilter.size() > 0) {
                            for (SongAnswerResponse song : listSongsFilter)
                            {
                                if (song.get_songName().toLowerCase().contains(charSequence)) {
                                    if (shownSongList.contains(song)) {
                                        continue;
                                    }
                                    shownSongList.add(song);
                                    if (shownSongList.size() >= MAX_SHOWED_TO_ADD) { //max size of default list song
                                        break;
                                    }
                                }

                            }
                        }
                        filterResults.count = shownSongList.size();
                        filterResults.values = shownSongList;
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    listSongs = (List<SongAnswerResponse>) results.values;
                    notifyDataSetChanged();
                }
            };
            return filter;
        }

        private boolean isChosen(SongAnswerResponse song) {
            if (chosenSong == null || chosenSong.size() == 0) { return false; }
            for (Integer songId : chosenSong) {
                if (song.get_id() == songId) { return true; }
            }
            return false;
        }
    }
}