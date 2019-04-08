package com.viktorija.android.musicplayer.activities.songs;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;

import com.viktorija.android.musicplayer.R;
import com.viktorija.android.musicplayer.models.Album;
import com.viktorija.android.musicplayer.models.MusicPlayerModel;
import com.viktorija.android.musicplayer.models.Song;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SongsActivity extends AppCompatActivity {

    @BindView(R.id.songs_recycler_view)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_songs);

        ButterKnife.bind(this);

        // Get currently selected album
        Album selectedAlbum = MusicPlayerModel.getSelectedAlbum();

        // Set activity title
        setTitle(selectedAlbum.getName());

        // Get songs of the currently selected album
        final ArrayList<Song> songs = selectedAlbum.getSongs();

        // Setup recycler view to display the data
        SongsRecyclerViewAdapter itemsAdapter =
                new SongsRecyclerViewAdapter(this, songs);
        recyclerView.setAdapter(itemsAdapter);

        // Add divider between items
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
    }
}
