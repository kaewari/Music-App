//package com.example.onlinemusicapp;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.GridView;
//import android.widget.TextView;
//
//import androidx.appcompat.widget.SearchView;
//import androidx.fragment.app.Fragment;
//
///**
// * A simple {@link Fragment} subclass.
// * Use the {@link PlaylistFragment1#newInstance} factory method to
// * create an instance of this fragment.
// */
//public class PlaylistFragment1 extends Fragment implements View.OnClickListener{
//
//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
//
//    //User-defined
//    GridView allPlaylist;
//    SearchView searchBar;
//    Button addPlaylist;
//    TextView noPlaylist;
//    Button addPlaylistBelow;
//
//
//    public PlaylistFragment1() {
//        // Required empty public constructor
//    }
//
//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment PlaylistFragment.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static PlaylistFragment1 newInstance(String param1, String param2) {
//        PlaylistFragment1 fragment = new PlaylistFragment1();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//
//        View view = inflater.inflate(R.layout.fragment_playlist)
//
//
////        //Search function
////        View view = inflater.inflate(R.layout.fragment_playlist, container);
////        searchBar = (SearchView) view.findViewById(R.id.playlist_search);
////
////        //Add function
////        addPlaylist = (Button) view.findViewById(R.id.addPlaylistButton);
////        noPlaylist = view.findViewById(R.id.nonePlaylistForShowing);;
////        addPlaylistBelow = (Button) view.findViewById(R.id.addPlaylistButtonBelow);
////        addPlaylist.setOnClickListener(PlaylistFragment.this);
////        allPlaylist = view.findViewById(R.id.playGridView);
////
////
////        //Load Playlist
////        PlaylistUtils.getAllPlaylists(MusicAppGlobal.getInstance().getAccessToken(), new PlaylistUtils() {
////            @Override
////            public void doSomething(List<PlaylistAnswerResponse> response) { //when true have response not null
////
////                Log.d("onCreate log ", String.valueOf(allPlaylist));
////                GridCellAdapter adapter = new GridCellAdapter(PlaylistFragment.this.getContext(), response);
////                Log.d("FFFFFFFFFFFFF", String.valueOf(response));
////                allPlaylist.setAdapter(adapter);
////
////                //search bar
////                searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
////                    @Override
////                    public boolean onQueryTextSubmit(String query) {
////                        return true;
////                    }
////
////                    @Override
////                    public boolean onQueryTextChange(String newText) {
////                        adapter.getFilter().filter(newText);
////                        return true;
////                    }
////                });
////
////                allPlaylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
////                    @Override
////                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
////                        //playlistID as id
////                        //load into inner Playlist activity
////                        Intent viewPlaylist = new Intent(view.getContext(), PlaylistViewActivity.class);
////                        viewPlaylist.removeExtra("playlistId");
////                        viewPlaylist.putExtra("playlistId", id);
////                        startActivity(viewPlaylist);
////                    }
////                });
////            }
////
////            @Override
////            public void doSomething() {
////                noPlaylist.setText("Bạn chưa có playlist\nThêm ngay để tận hưởng nào!");
////                addPlaylistBelow.setVisibility(View.VISIBLE);
////                addPlaylistBelow.setOnClickListener(PlaylistFragment.this);
////            }
////        });
//
//        return super.onCreateView(inflater, container, savedInstanceState);
//    }
//
//    @Override
//    public void onClick(View v) {
//        //start activity for adding new playlist
//        Intent addPlaylistItent = new Intent(v.getContext(), PlaylistAddEditActivity.class);
//        addPlaylistItent.removeExtra("AddEditLabel");
//        addPlaylistItent.putExtra("AddEditLabel", 1);
//        startActivity(addPlaylistItent);
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//    }
//
////    public static class GridCellAdapter extends BaseAdapter implements Filterable {
////
////        Context context;
////        List<PlaylistAnswerResponse> playlists;
////        List<PlaylistAnswerResponse> filteredPlaylist;
////        LayoutInflater inflater;
////
////
////        public GridCellAdapter(Context context, List<PlaylistAnswerResponse> playlists) {
////            this.context = context;
////            this.playlists = playlists;
////            this.filteredPlaylist = playlists;
////            inflater = LayoutInflater.from(context);
////        }
////
////        @Override
////        public int getCount() {
////            return this.playlists.size();
////        }
////
////        @Override
////        public Object getItem(int position) {
////            return playlists.get(position);
////        }
////
////        @Override
////        public long getItemId(int position) {
////            return ((PlaylistAnswerResponse) getItem(position)).get_id();
////        }
////
////        @Override
////        public View getView(int position, View view, ViewGroup parent) {
////            view = inflater.inflate(R.layout.grid_item, null);
////            ImageView image = (ImageView) view.findViewById(R.id.imagePlaylist);
////            TextView title = (TextView) view.findViewById(R.id.playlistName);
////
////            image.setImageResource(R.drawable.playlist_icon);
////            title.setText(this.playlists.get(position).get_playlistName());
////
////            return view;
////        }
////
////
//        @Override
//        public Filter getFilter() {
//            Filter filter = new Filter() {
//
//                @Override
//                protected FilterResults performFiltering(CharSequence constraint) {
//                    FilterResults results = new FilterResults();
//                    if (constraint == null || constraint.length() == 0) {
//                        results.count = filteredPlaylist.size();
//                        results.values = filteredPlaylist;
//                    }
//                    else {
//                        String searchSequence = constraint.toString().toLowerCase();
//                        List<PlaylistAnswerResponse> itemResult = new ArrayList<>();
//                        for (PlaylistAnswerResponse item : filteredPlaylist) {
//                            if (item.get_playlistName().toLowerCase().contains(searchSequence)) {
//                                itemResult.add(item);
//                            }
//                        }
//                        results.count = itemResult.size();
//                        results.values = itemResult;
//                    }
//                    return results;
//                }
//
//                @Override
//                protected void publishResults(CharSequence constraint, FilterResults results) {
//                    playlists = (List<PlaylistAnswerResponse>) results.values;
//                    notifyDataSetChanged();
//                }
//            };
//            return filter;
//        }
////
////        public void updateItems(List<PlaylistAnswerResponse> listItems) {
////            this.playlists = this.filteredPlaylist = listItems;
////            notifyDataSetChanged();
////        }
//
////    }
////
////    public void callNotifyDataSetChange() {
////        GridView gridView = PlaylistFragment.this.allPlaylist;
////
////        PlaylistUtils.getAllPlaylists(MusicAppGlobal.getInstance().getAccessToken(), new PlaylistUtils() {
////            @Override
////            public void doSomething(List<PlaylistAnswerResponse> response) { //when true have response not null
////
////                PlaylistFragment.this.noPlaylist.setText(null);
////                PlaylistFragment.this.addPlaylistBelow.setVisibility(View.INVISIBLE);
////                if (gridView.getAdapter() != null && response != null) {
////                    ((GridCellAdapter) PlaylistFragment.this.allPlaylist.getAdapter()).updateItems(response);
////                }
////                else {
////                    updateGridPlaylistView(response);
////                }
////            }
////
////            @Override
////            public void doSomething() {
////                PlaylistFragment.this.noPlaylist.setText("Bạn chưa có playlist\nThêm ngay để tận hưởng nào!");
////                PlaylistFragment.this.addPlaylistBelow.setVisibility(View.VISIBLE);
////                PlaylistFragment.this.addPlaylistBelow.setOnClickListener(PlaylistFragment.this);
////                updateGridPlaylistView(null);
////            }
////
////            private void updateGridPlaylistView(List<PlaylistAnswerResponse> response) {
////                if (response == null) {
////                    PlaylistFragment.this.allPlaylist.setAdapter(null);
////
////                }
////                else {
////                    GridCellAdapter adapter = new GridCellAdapter(PlaylistFragment.this.getContext(), response);
////                    PlaylistFragment.this.allPlaylist.setAdapter(adapter);
////
////                    //search bar
////                    PlaylistFragment.this.searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
////                        @Override
////                        public boolean onQueryTextSubmit(String query) {
////                            return true;
////                        }
////
////                        @Override
////                        public boolean onQueryTextChange(String newText) {
////                            adapter.getFilter().filter(newText);
////                            return true;
////                        }
////                    });
////                    PlaylistFragment.this.allPlaylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
////                        @Override
////                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
////                            //playlistID as id
////                            //load into inner Playlist activity
////                            Intent viewPlaylist = new Intent(view.getContext(), PlaylistViewActivity.class);
////                            viewPlaylist.removeExtra("playlistId");
////                            viewPlaylist.putExtra("playlistId", id);
////                            startActivity(viewPlaylist);
////                        }
////                    });
////                }
////            }
////        });
////
////
////    }
////
////    @Override
////    public void onStart() {
////        callNotifyDataSetChange();
////        Log.d("Fragment Update State", "Done");
////        super.onStart();
////    }
//}