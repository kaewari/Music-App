package com.example.onlinemusicapp;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface apiService {

    //playlist here---------------------------------------------------------------------------------

    @Headers("Content-Type: application/json")
    @GET("playlist") //get all playlist on the account
    Call<List<PlaylistAnswerResponse>> getAnswer(@Header("Authorization") String token);

    @Headers("Content-Type: application/json")
    @GET("playlist/{playlistId}") //get playlist with id
    Call<PlaylistAnswerResponse> getAnswer(@Header("Authorization") String token, @Path("playlistId") int id);

    @Headers("Content-Type: application/json")
    @POST("playlist") //add new playlist
    Call<ResponseBody> addPlaylist(@Header("Authorization") String token, @Body CustomBodyX newPlaylist);

    @Headers("Content-Type: application/json")
    @POST("playlist/{playlistID}/{songID}") //add song to exist Playlist
    Call<ResponseBody> addSongToExistPlaylist(@Header("Authorization") String token,
                                          @Path("playlistID") int playlistID,
                                          @Path("songID") int songID);

    @Headers("Content-Type: application/json")
    @PUT("playlist/{playlistId}") //edit exist playlist
    Call<ResponseBody> editPlaylist(@Header("Authorization") String token, @Path("playlistId") int id,
                                    @Body PlaylistAnswerResponse playlist);

    @DELETE("playlist/{playlistID}/{songID}") //delete song from a playlist
    Call<ResponseBody> deleteSongToExistPlaylist(@Header("Authorization") String token,
                                                 @Path("playlistID") int playlistID,
                                                 @Path("songID") int songID);

    @DELETE("playlist/{playlistId}") //delete the playlist
    Call<ResponseBody> deletePlaylist(@Header("Authorization") String token, @Path("playlistId") int id);

    @GET("song/{songId}")
    Call<SongAnswerResponse> getSongFromId(@Path("songId") int id);

    @GET("song")
    Call<List<SongAnswerResponse>> getAllSongs();

    //song here-------------------------------------------------------------------------------------

    @Multipart
    @POST("song")
    Call<SOSong> sendSong(@Header("Authorization") String token, @Part("name") RequestBody name, @Part("singerId") RequestBody singerId, @Part MultipartBody.Part path);

    @FormUrlEncoded
    @PUT("song/{id}")
    Call<SOSong> changeSong(@Header("Authorization") String token, @Path("id") int id, @Field("name") String name, @Field("singerId") int singerId);

    @GET("song")
    Call<List<SOSong>> getSong();

    @GET("song/{id}")
    Call<SOSong> getSongId(@Path("id") int id);

    @DELETE("song/{id}")
    Call<SOSong> deleteSong(@Header("Authentication") String token, @Path("id") int id);

    @FormUrlEncoded
    @POST("singer")
    Call<Singer> sendSinger(@Header("Authorization") String token, @Field("name") String name, @Field("description") String description);

    @DELETE("singer/{id}")
    Call<Singer> deleteSinger(@Header("Authorization") String token, @Path("id") int id);

    @FormUrlEncoded
    @PUT("singer/{id}")
    Call<Singer> changeSinger(@Header("Authorization") String token, @Path("id") int id, @Field("name") String name, @ Field("description") String description);

    @GET("singer")
    Call<List<Singer>> getSingers();

    @GET("singer/{id}")
    Call<List<Singer>> getSingerId(@Path("id") int id);

    //pendingSong

    @Headers("Content-Type: application/json")
    @GET("song/pending")
    Call<List<SongAnswerResponse>> getPendingSong(@Header("Authorization") String token);

    //user here-------------------------------------------------------------------------------------

    @POST("register")
    Call<ResponseUser> sendUser(@Body Users user);

    @POST("login")
    Call<ResponseUser> login(@Body Users loginUser);

    @Headers("Content-Type: application/json")
    @POST("logout")
    Call<ResponseBody> logOutUser(@Header("Authorization") String token);


    //singer here-----------------------------------------------------------------------------------

}
