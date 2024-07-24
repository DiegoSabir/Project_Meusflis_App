package com.example.meusflis.Activities;

import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.meusflis.R;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;

public class VideoPlayerOnlineActivity extends AppCompatActivity {

    PlayerView playerView;
    ProgressBar progressBar;
    ImageView btnFullScreen;

    SimpleExoPlayer simpleExoPlayer;

    boolean flag = false;
    String videoUrlExtract = "";
    String DriveApiKey = "AIzaSyANqk55i6VvX64EhZR4QHItX4adN7ephro";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player_online);

        playerView = findViewById(R.id.player_view);
        progressBar = findViewById(R.id.progress_bar);
        btnFullScreen = playerView.findViewById(R.id.bt_fullscreen);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        try {
            videoUrlExtract = getIntent().getStringExtra("videoUrl");

            Uri videourl = Uri.parse("https://www.googleapis.com/drive/v3/files/" + videoUrlExtract + "?alt=media&key=" + DriveApiKey);

            LoadControl loadControl = new DefaultLoadControl();

            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();

            TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));

            simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(VideoPlayerOnlineActivity.this, trackSelector, loadControl);

            DefaultHttpDataSourceFactory factory = new DefaultHttpDataSourceFactory("exoplayer_video");

            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

            MediaSource mediaSource = new ExtractorMediaSource(videourl, factory, null, null);

            playerView.setPlayer(simpleExoPlayer);

            playerView.setKeepScreenOn(true);

            simpleExoPlayer.prepare(mediaSource);

            simpleExoPlayer.setPlayWhenReady(true);

            simpleExoPlayer.addListener(new Player.EventListener(){

                @Override
                public void onTimelineChanged(Timeline timeline, Object manifest, int reason){

                }

                @Override
                public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections){

                }

                @Override
                public void onLoadingChanged(boolean isLoading){

                }

                @Override
                public void onPlayerStateChanged(boolean playWhenReady, int playbackState){

                    if (playbackState == Player.STATE_BUFFERING){
                        progressBar.setVisibility(View.VISIBLE);
                    }
                    else if (playbackState == Player.STATE_READY){
                        progressBar.setVisibility(View.GONE);
                    }

                }

                @Override
                public void onRepeatModeChanged(int repeatMode){

                }

                @Override
                public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled){

                }
            });
        }
        catch (Exception e){
            Toast.makeText(this, "Error:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        btnFullScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag){
                    btnFullScreen.setImageDrawable(getResources().getDrawable(R.drawable.baseline_fullscreen_24));
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    flag = false;
                }
                else{
                    btnFullScreen.setImageDrawable(getResources().getDrawable(R.drawable.baseline_fullscreen_exit_24));
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    flag = true;
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

        try {
            simpleExoPlayer.setPlayWhenReady(false);
            simpleExoPlayer.getPlaybackState();
        }
        catch (Exception e){
            Toast.makeText(this, "Error:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        try {
            simpleExoPlayer.setPlayWhenReady(true);
            simpleExoPlayer.getPlaybackState();
        }
        catch (Exception e){
            Toast.makeText(this, "Error:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}