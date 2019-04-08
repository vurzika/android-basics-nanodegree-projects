package com.viktorija.android.musicplayer.activities.nowplaying;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.viktorija.android.musicplayer.R;
import com.viktorija.android.musicplayer.models.Artist;
import com.viktorija.android.musicplayer.models.MusicPlayerModel;
import com.viktorija.android.musicplayer.models.Song;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NowPlayingActivity extends AppCompatActivity {

    @BindView(R.id.song_duration_text_view)
    TextView songDurationTextView;

    @BindView(R.id.song_name_text_view)
    TextView songNameTextView;

    @BindView(R.id.author_name_text_view)
    TextView artistNameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_now_playing);

        ButterKnife.bind(this);

        // get state from model

        Song selectedSong = MusicPlayerModel.getSelectedSong();
        Artist selectedArtist = MusicPlayerModel.getSelectedArtist();

        // update UI

        songDurationTextView.setText(selectedSong.getDuration());
        songNameTextView.setText(selectedSong.getName());
        artistNameTextView.setText(selectedArtist.getName());
    }
}
