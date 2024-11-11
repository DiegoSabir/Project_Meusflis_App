package com.sabir.meusflis.Activities;

import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.sabir.meusflis.R;

public class PlayerActivity extends AppCompatActivity {

    private PlayerView playerView;
    private ExoPlayer simpleExoPlayer;
    private String VIDEO_ID, VIDEO_TITLE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        VIDEO_ID = getIntent().getStringExtra("vid");
        VIDEO_TITLE = getIntent().getStringExtra("title");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(VIDEO_TITLE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        setupPlayer();
    }

    private void setupPlayer() {
        playerView = findViewById(R.id.video_player);

        // Construimos la URL de Google Drive
        String driveUrl = "https://drive.google.com/uc?export=download&id=" + VIDEO_ID;

        // Crear un DefaultTrackSelector y un ExoPlayer instance
        DefaultTrackSelector trackSelector = new DefaultTrackSelector(this);
        simpleExoPlayer = new ExoPlayer.Builder(this).setTrackSelector(trackSelector).build();

        // Configurar el DataSource.Factory
        DefaultHttpDataSource.Factory dataSourceFactory = new DefaultHttpDataSource.Factory();

        // Crear el MediaItem y asignarlo al ExoPlayer
        MediaItem mediaItem = MediaItem.fromUri(Uri.parse(driveUrl));
        simpleExoPlayer.setMediaItem(mediaItem);

        // Asociar el playerView al ExoPlayer y preparar
        playerView.setPlayer(simpleExoPlayer);
        simpleExoPlayer.prepare();
        simpleExoPlayer.setPlayWhenReady(true);
        playerView.setKeepScreenOn(true);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (simpleExoPlayer != null) {
            simpleExoPlayer.release();
            simpleExoPlayer = null;
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
