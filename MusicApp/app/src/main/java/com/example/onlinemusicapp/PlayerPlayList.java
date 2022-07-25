package com.example.onlinemusicapp;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class PlayerPlayList extends AppCompatActivity {
    SeekBar seekBar;
    TextView txtRunTime, txtTimeTotal, txtTitle, txtAuthorName, tvDownload;
    ImageButton btnPrev, btnPlay, btnNext, btnSuffle, btnReapeat;
    ImageView bgFull;
    MediaPlayer mediaPlayer;
    ArrayList<SongAnswerResponse> songArrayList;
    ArrayList<SongAnswerResponse> temp;
    Random random;
    DiscMusic discMusic;
    ViewPager viewPager;
    int position = 0;
    boolean isSuffleClick = false;
    int isRepeatClick = 1;
    SongAnswerResponse song;
    String url_api = "http://tiennm0000-001-site1.ctempurl.com", pathDownload;
    boolean clickNextPre = false;
    Toolbar toolbar;
    public static ViewPaperPlaylistNhac adapternhac;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        init();
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            txtTitle.setText("Song name");
            txtAuthorName.setText("Singer name");
        } else {
            song = (SongAnswerResponse) bundle.get("cakhuc");
            songArrayList = bundle.getParcelableArrayList("danhsachnhac");
            txtTitle.setText(song.get_songName());
//            txtAuthorName.setText(song.get_singer().get_name());
            String url = url_api + song.get_path();
            playMusic(url);
            for(int i = 0; i < songArrayList.size(); i++){
                if(Objects.equals(songArrayList.get(i).get_id(), song.get_id())){
                    position = i;
                }
            }
        }
        temp = (ArrayList<SongAnswerResponse>) songArrayList.clone();

        btnSuffle.setOnClickListener(view -> btnSuffleAction());

        btnReapeat.setOnClickListener(view -> btnReapeatAction());

        btnPlay.setOnClickListener(view -> btnPlayAction());

        btnNext.setOnClickListener(view -> {
            clickNextPre = true;
            try {
                btnNextAction();
                btnNext.setClickable(false);
                Thread.sleep(1000);
                btnNext.setClickable(true);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        btnPrev.setOnClickListener(view -> {
            clickNextPre = true;
            try {
                btnPrevAction();
                btnPrev.setClickable(false);
                Thread.sleep(1000);
                btnPrev.setClickable(true);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });

        tvDownload.setOnClickListener(view -> starDownload());
    }
    private void btnSuffleAction() {
        if (!isSuffleClick) {
            btnSuffle.setImageResource(R.drawable.iconsuffle);
            isSuffleClick = true;
        } else {
            btnSuffle.setImageResource(R.drawable.iconshuffled);
            isSuffleClick = false;
        }
    }
    private void btnReapeatAction() {
        if (isRepeatClick == 1) {
            btnReapeat.setImageResource(R.drawable.iconrepeat);
            isRepeatClick = 2;
        } else {
            if (isRepeatClick == 2) {
                btnReapeat.setImageResource(R.drawable.repeat_one);
                isRepeatClick = 3;
            } else {
                if (isRepeatClick == 3) {
                    btnReapeat.setImageResource(R.drawable.iconsyned);
                    isRepeatClick = 1;
                }
            }
        }
    }
    private void btnPlayAction(){
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            discMusic.pauseDisc();
            btnPlay.setImageResource(R.drawable.nutpause);
        } else {
            mediaPlayer.start();
            discMusic.startDisc();
            btnPlay.setImageResource(R.drawable.nutplay);
        }
    }
    private void btnPrevAction() throws InterruptedException {
        position--;
        if (position < 0) {
            position = songArrayList.size() - 1;
        }
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
        txtTitle.setText(songArrayList.get(position).get_songName());
//        txtAuthorName.setText(songArrayList.get(position).get_singer().get_name());
        String url = url_api + songArrayList.get(position).get_path();
        btnPlay.setImageResource(R.drawable.nutplay);
        playMusic(url);
    }
    private void btnNextAction() throws InterruptedException {
        position++;
        if (position > songArrayList.size() - 1) {
            position = 0;
        }
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
        txtTitle.setText(songArrayList.get(position).get_songName());
//        txtAuthorName.setText(songArrayList.get(position).get_singer().get_name());
        String url = url_api + songArrayList.get(position).get_path();
        btnPlay.setImageResource(R.drawable.nutplay);
        playMusic(url);
    }

    private void playMusic(String url) {
        try {
            initMediaPlayer();
            mediaPlayer = new MediaPlayer();
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                mediaPlayer.release();
            }
            mediaPlayer.setDataSource(url);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.prepare();
            btnPlay.setImageResource(R.drawable.nutplay);
            mediaPlayer.start();
            runTime();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void initFirstSong(String url){
        try {
            mediaPlayer = new MediaPlayer();
            initMediaPlayer();
            mediaPlayer.setDataSource(url);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.prepare();
            mediaPlayer.start();
            runTime();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @SuppressLint("SetTextI18n")
    private void TimeSong() {
        //Format time
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat formatTime = new SimpleDateFormat("mm:ss");
        //Set time
        if (mediaPlayer.getCurrentPosition() >= mediaPlayer.getDuration())
            txtRunTime.setText(formatTime.format(mediaPlayer.getDuration()));
        else
            txtRunTime.setText(formatTime.format(mediaPlayer.getCurrentPosition()));
        if (mediaPlayer.getDuration() - mediaPlayer.getCurrentPosition() <= 0)
            txtTimeTotal.setText("00:00");
        else
            txtTimeTotal.setText(formatTime.format(mediaPlayer.getDuration() - mediaPlayer.getCurrentPosition()));
        //Set thông số seekbar
        seekBar.setMax(mediaPlayer.getDuration());
        seekBar.setProgress(mediaPlayer.getCurrentPosition());
    }

    private void runTime() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                TimeSong();
                if (!clickNextPre) {
                    mediaPlayer.setOnCompletionListener(mp -> {
                        discMusic.startDisc();
                        if (!isSuffleClick) {
                            if (isRepeatClick == 2) {
                                try {
                                    btnNextAction();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                if (isRepeatClick == 3) {
                                    btnPlay.setImageResource(R.drawable.nutplay);
                                    txtTitle.setText(songArrayList.get(position).get_songName());
//                                    txtAuthorName.setText(songArrayList.get(position).get_singer().get_name());
                                    playMusic(url_api + songArrayList.get(position).get_path());
                                } else {
                                    if (isRepeatClick == 1) {
                                        position++;
                                        if (position > songArrayList.size() - 1) {
                                            position = 0;
                                            btnPlay.setImageResource(R.drawable.nutpause);
                                            if (mediaPlayer.isPlaying()) {
                                                mediaPlayer.stop();
                                                mediaPlayer.release();
                                            }
                                            txtTitle.setText(songArrayList.get(position).get_songName());
//                                            txtAuthorName.setText(songArrayList.get(position).get_singer().get_name());
                                            initFirstSong(url_api + songArrayList.get(position).get_path());
                                            mediaPlayer.pause();
                                        } else {
                                            btnPlay.setImageResource(R.drawable.nutplay);
                                            if (mediaPlayer.isPlaying()) {
                                                mediaPlayer.stop();
                                                mediaPlayer.release();
                                            }
                                            txtTitle.setText(songArrayList.get(position).get_songName());
//                                            txtAuthorName.setText(songArrayList.get(position).get_singer().get_name());
                                            playMusic(url_api + songArrayList.get(position).get_path());
                                        }
                                    }
                                }
                            }
                        } else {
                            if (isRepeatClick == 1) {
                                temp.remove(position);
                                if (!mediaPlayer.isPlaying()) {
                                    if (temp.size() > 0) {
                                        position = random.nextInt(temp.size());
                                        btnPlay.setImageResource(R.drawable.nutplay);
                                        txtTitle.setText(songArrayList.get(position).get_songName());
//                                        txtAuthorName.setText(songArrayList.get(position).get_singer().get_name());
                                        initFirstSong(url_api + temp.get(position).get_path());
                                        playMusic(url_api + temp.get(position).get_path());
                                    } else {
                                        temp = (ArrayList<SongAnswerResponse>) songArrayList.clone();
                                        position = random.nextInt(temp.size());
                                        btnPlay.setImageResource(R.drawable.nutpause);
                                        if (mediaPlayer.isPlaying()) {
                                            mediaPlayer.stop();
                                            mediaPlayer.release();
                                        }
                                        txtTitle.setText(songArrayList.get(position).get_songName());
//                                        txtAuthorName.setText(songArrayList.get(position).get_singer().get_name());
                                        initFirstSong(url_api + temp.get(position).get_path());
                                        mediaPlayer.pause();
                                    }
                                }
                            } else {
                                if (isRepeatClick == 2) {
                                    temp.remove(position);
                                    if (!mediaPlayer.isPlaying()) {
                                        if (temp.size() <= 0) {
                                            temp = (ArrayList<SongAnswerResponse>) songArrayList.clone();
                                        }
                                        position = random.nextInt(temp.size());
                                        btnPlay.setImageResource(R.drawable.nutplay);
                                        txtTitle.setText(songArrayList.get(position).get_songName());
//                                        txtAuthorName.setText(songArrayList.get(position).get_singer().get_name());
                                        playMusic(url_api + temp.get(position).get_path());
                                    }
                                } else {
                                    if (isRepeatClick == 3) {
                                        btnPlay.setImageResource(R.drawable.nutplay);
                                        txtTitle.setText(songArrayList.get(position).get_songName());
//                                        txtAuthorName.setText(songArrayList.get(position).get_singer().get_name());
                                        playMusic(url_api + temp.get(position).get_path());
                                    }
                                }
                            }
                        }
                    });
                }
                handler.postDelayed(this, 100);
            }
        }, 100);
        clickNextPre = false;
    }

    private void init() {
        random = new Random();
        txtRunTime = findViewById(R.id.tv_RunTime);
        txtTimeTotal = findViewById(R.id.tv_TimeTotal);
        txtTitle = findViewById(R.id.tv_SongName);
        tvDownload = findViewById(R.id.tv_Download);
        seekBar = findViewById(R.id.seekBar_time);
        btnNext = findViewById(R.id.imgBtn_Next);
        btnPrev = findViewById(R.id.imgBtn_Prev);
        btnPlay = findViewById(R.id.imgBtn_Play);
        btnSuffle = findViewById(R.id.imgBtn_Suffle);
        btnReapeat = findViewById(R.id.imgBtn_Repeat);
        txtAuthorName = findViewById(R.id.tv_AuthorName);
        viewPager = findViewById(R.id.vp_PagerMusic);
        bgFull = findViewById(R.id.bg_Full);
        discMusic = new DiscMusic();
        adapternhac = new ViewPaperPlaylistNhac(getSupportFragmentManager());
        adapternhac.addFragment(discMusic);
        viewPager.setAdapter(adapternhac);
        mediaPlayer = new MediaPlayer();
        toolbar = findViewById(R.id.toolbar_play_nhac);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(view -> {
            finish();
            mediaPlayer.stop();
            temp.clear();
            songArrayList.clear();
        });
    }

    private void initMediaPlayer() {

//        Format time
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat formatTime = new SimpleDateFormat("mm:ss");
//        Set time
        txtRunTime.setText(formatTime.format(mediaPlayer.getCurrentPosition()));
        txtTimeTotal.setText(formatTime.format(mediaPlayer.getDuration()));
    }

    //    Tải xuống file nhac
    private void starDownload() {
        pathDownload = url_api + song.get_path();
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(pathDownload));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        request.setTitle(song.get_songName());
        request.setDescription("Downloading " + song.get_songName());
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_ONLY_COMPLETION);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, String.valueOf(System.currentTimeMillis()));

        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        if (downloadManager != null)
            downloadManager.enqueue(request);
    }
}
