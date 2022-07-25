package com.example.onlinemusicapp;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaylistUtils{

    public void doSomething(List<PlaylistAnswerResponse> response) {}
    public void doSomething(PlaylistAnswerResponse response) {}
    public void doSomething(SongAnswerResponse response) {}
    public void doSomething(Response<List<SongAnswerResponse>> response) {}
    public void doSomething() {}

    //get all playlist the signed user have
    public static void getAllPlaylists(String signedToken, PlaylistUtils playlistUtils) {
        apiService apiService = RetrofitClient.getRetrofitInitializedService();
        apiService.getAnswer(signedToken).enqueue(new Callback<List<PlaylistAnswerResponse>>() {

            @Override
            public void onResponse(Call<List<PlaylistAnswerResponse>> call,
                                   Response<List<PlaylistAnswerResponse>> response) {
                if (response.isSuccessful()) {
                    //do something with response json here
                    if (response.body() == null || response.body().size() == 0) {
                        playlistUtils.doSomething();
                    }
                    else {
                        playlistUtils.doSomething(response.body());
                    }
                }
                //else {
                    //do something with response json here with fail
                //}
            }

            @Override
            public void onFailure(Call<List<PlaylistAnswerResponse>> call, Throwable t) {
                Log.e("(App Announcement) Error get value from API ", "At PlaylistUtils.getAllPlaylists(token)");
            }
        });
    }

    //get playlist with id
    public static void getPlaylist(String signedToken, int id, PlaylistUtils playlistUtils) {
        apiService apiService = RetrofitClient.getRetrofitInitializedService();
        apiService.getAnswer(signedToken, id).enqueue(new Callback<PlaylistAnswerResponse>() {

            @Override
            public void onResponse(Call<PlaylistAnswerResponse> call,
                                   Response<PlaylistAnswerResponse> response) {
                if (response.isSuccessful()) {
                    //do something with response json here
                    playlistUtils.doSomething(response.body());
                }
            }

            @Override
            public void onFailure(Call<PlaylistAnswerResponse> call, Throwable t) {
                Log.e("(App Announcement) Error get value from API ", "At PlaylistUtils.getPlaylist(token, id)");
            }
        });
    }

    //add new playlist to the user
    public static void addNewPlaylist(String signedToken, String playlistName
            , ArrayList<Integer> songs, PlaylistUtils playlistUtils) {
        apiService apiService = RetrofitClient.getRetrofitInitializedService();
        //create playlist
        CustomBodyX postPlaylist = new CustomBodyX();
        postPlaylist.set_name(playlistName);
        postPlaylist.set_songId(songs.get(0));
        apiService.addPlaylist(signedToken, postPlaylist).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                apiService.getAnswer(signedToken).enqueue(new Callback<List<PlaylistAnswerResponse>>() {
                    @Override
                    public void onResponse(Call<List<PlaylistAnswerResponse>> call, Response<List<PlaylistAnswerResponse>> response) {
                        int playlistId = response.body().get(response.body().size() - 1).get_id();
                        for (int count = 1; count < songs.size(); count++) {
                            int finalCount = count;
                            Thread anotherThread = new Thread() {
                                @Override
                                public void run() {
                                    PlaylistUtils.addSongToPlaylist(signedToken, playlistId, songs.get(finalCount), new PlaylistUtils());
                                }
                            };
                            anotherThread.start();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<PlaylistAnswerResponse>> call, Throwable t) {

                    }
                });
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("(App Announcement) Error post value from API "
                        , "At PlaylistUtils.addNewPlaylist(token, playlistAnswerResponse)");
            }
        });

    }

    //add song to existed playlist
    public static void addSongToPlaylist(String signedToken, int playlistId, int songId
                                            , PlaylistUtils playlistUtils) {
        apiService apiService = RetrofitClient.getRetrofitInitializedService();
        apiService.addSongToExistPlaylist(signedToken, playlistId, songId).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    //can do something you want in here
                    playlistUtils.doSomething();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("(App Announcement) Error post value from API "
                        , "At PlaylistUtils.addSongToPlaylist(token, playlistId, songId)");
            }
        });
        try {
            Thread.sleep(15000);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    //edit name of playlist
    public static void editPlaylist(String signedToken, int playlistId,
                                       String playlistName, PlaylistUtils playlistUtils) {
        apiService apiService = RetrofitClient.getRetrofitInitializedService();
        PlaylistAnswerResponse postPlaylist = new PlaylistAnswerResponse();
        postPlaylist.set_id(playlistId);
        postPlaylist.set_playlistName(playlistName);
        postPlaylist.set_songsList(null);
        postPlaylist.set_userID(0);
        postPlaylist.set_createdDate("sample date");
        apiService.editPlaylist(signedToken, playlistId, postPlaylist).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    //can do something you want in here
                    playlistUtils.doSomething();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("(App Announcement) Error put value into API "
                        , "At PlaylistUtils.editPlaylist(token, playlistId, playlistAnswerResponse)");
            }
        });
    }

    //delete song from existed playlist
    public static void removeSongFromPlaylist(String signedToken, int playlistId, int songId
            , PlaylistUtils playlistUtils) {
        apiService apiService = RetrofitClient.getRetrofitInitializedService();
        apiService.deleteSongToExistPlaylist(signedToken, playlistId, songId).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    //can do something you want in here
                    playlistUtils.doSomething();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("(App Announcement) Error post value from API "
                        , "At PlaylistUtils.removeSongFromPlaylist(token, playlistId, songId)");
            }
        });
    }

    //delete playlist
    public static void deletePlaylist(String signedToken, int playlistId, PlaylistUtils playlistUtils) {
        apiService apiService = RetrofitClient.getRetrofitInitializedService();
        apiService.deletePlaylist(signedToken, playlistId).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    //can do something you want in here
                    playlistUtils.doSomething();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("(App Announcement) Error post value from API "
                        , "At PlaylistUtils.deletePlaylist(token, playlistId)");
            }
        });
    }

    public static void getSongFromId(String signedToken, int songId, PlaylistUtils playlistUtils) {
        apiService apiService = RetrofitClient.getRetrofitInitializedService();
        apiService.getSongFromId(songId).enqueue(new Callback<SongAnswerResponse>() {
            @Override
            public void onResponse(Call<SongAnswerResponse> call, Response<SongAnswerResponse> response) {
                if (response.isSuccessful()) {
                    //can do something you want in here
                    playlistUtils.doSomething(response.body());
                }
            }

            @Override
            public void onFailure(Call<SongAnswerResponse> call, Throwable t) {
                Log.e("(App Announcement) Error post value from API "
                        , "At PlaylistUtils.getSongFromId(songId)");
            }
        });
    }

    public static void getAllSongs(PlaylistUtils playlistUtils) {
        apiService apiService = RetrofitClient.getRetrofitInitializedService();
        apiService.getAllSongs().enqueue(new Callback<List<SongAnswerResponse>>() {
            @Override
            public void onResponse(Call<List<SongAnswerResponse>> call, Response<List<SongAnswerResponse>> response) {
                //can do something you want in here
                playlistUtils.doSomething(response);
            }

            @Override
            public void onFailure(Call<List<SongAnswerResponse>> call, Throwable t) {
                Log.e("(App Announcement) Error post value from API "
                        , "At PlaylistUtils.getAllSongs()");
            }
        });
    }

    public static void getPendingSong(String signedToken, PlaylistUtils playlistUtils) {
        apiService apiService = RetrofitClient.getRetrofitInitializedService();
        apiService.getPendingSong(signedToken).enqueue(new Callback<List<SongAnswerResponse>>() {
            @Override
            public void onResponse(Call<List<SongAnswerResponse>> call, Response<List<SongAnswerResponse>> response) {
                playlistUtils.doSomething(response);
            }

            @Override
            public void onFailure(Call<List<SongAnswerResponse>> call, Throwable t) {
                Log.e("(App Announcement) Error post value from API "
                        , "At PlaylistUtils.getPendingSong(token)");
            }
        });
    }

}
