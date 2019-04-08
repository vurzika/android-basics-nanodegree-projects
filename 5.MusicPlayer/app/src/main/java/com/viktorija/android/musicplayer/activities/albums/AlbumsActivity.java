package com.viktorija.android.musicplayer.activities.albums;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import com.viktorija.android.musicplayer.R;
import com.viktorija.android.musicplayer.models.Album;
import com.viktorija.android.musicplayer.models.Artist;
import com.viktorija.android.musicplayer.models.MusicPlayerModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AlbumsActivity extends AppCompatActivity {

    @BindView(R.id.albums_recycler_view)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_albums);

        ButterKnife.bind(this);

        // Get currently selected artist
        Artist selectedArtist = MusicPlayerModel.getSelectedArtist();

        // Set activity title
        setTitle(selectedArtist.getName());

        // Get albums of the currently selected artist
        final ArrayList<Album> albums = selectedArtist.getAlbums();

        // Setup recycler view to display the data
        AlbumsRecyclerViewAdapter recyclerViewAdapter =
                new AlbumsRecyclerViewAdapter(this, albums);

        recyclerView.setAdapter(recyclerViewAdapter);
    }
}
