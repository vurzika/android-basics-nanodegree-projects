package com.viktorija.android.musicplayer.activities.nowplaying;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.viktorija.android.musicplayer.R;
import com.viktorija.android.musicplayer.models.MusicPlayerModel;
import com.viktorija.android.musicplayer.models.Song;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Reusable fragment that represents now playing widget on UI
 */
public class NowPlayingFragment extends Fragment {

    @BindView(R.id.now_playing_song_duration_text_view)
    TextView songDurationTextView;

    @BindView(R.id.now_playing_song_name_text_view)
    TextView songNameTextView;

    @BindView(R.id.now_playing_button_stop)
    ImageView stopButtonImageView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_now_playing, container, false);

        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        stopButtonImageView.setOnClickListener(buttonImageView -> {
            MusicPlayerModel.setSelectedSong(null);
            // hide containing view from activity
            view.setVisibility(View.GONE);
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        // display latest information every time widget appears on UI
        refreshUI();
    }

    private void refreshUI() {
        View fragmentView = getView();

        if (fragmentView != null) {
            // get song that is playing
            Song selectedSong = MusicPlayerModel.getSelectedSong();

            // if there is dong playing, display refresh UI and display fragment view
            if (selectedSong != null) {
                songDurationTextView.setText(selectedSong.getDuration());
                songNameTextView.setText(selectedSong.getName());
                fragmentView.setVisibility(View.VISIBLE);
            } else {
                // otherwise hide view
                fragmentView.setVisibility(View.GONE);
            }
        }
    }
}
