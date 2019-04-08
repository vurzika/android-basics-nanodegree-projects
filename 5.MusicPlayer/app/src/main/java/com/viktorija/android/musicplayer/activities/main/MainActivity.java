package com.viktorija.android.musicplayer.activities.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;

import com.viktorija.android.musicplayer.R;
import com.viktorija.android.musicplayer.models.Artist;
import com.viktorija.android.musicplayer.models.MusicPlayerModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.artist_recycler_view)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        // Get sample data from model

        final ArrayList<Artist> artists = MusicPlayerModel.getArtists();

        // Setup recycler view to display the data

        ArtistsRecyclerViewAdapter itemsAdapter =
                new ArtistsRecyclerViewAdapter(this, artists);
        recyclerView.setAdapter(itemsAdapter);

        // add divider between items
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
    }
}
