package com.viktorija.android.musicplayer.activities.songs;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.viktorija.android.musicplayer.R;
import com.viktorija.android.musicplayer.activities.nowplaying.NowPlayingActivity;
import com.viktorija.android.musicplayer.models.MusicPlayerModel;
import com.viktorija.android.musicplayer.models.Song;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SongsRecyclerViewAdapter extends RecyclerView.Adapter<SongsRecyclerViewAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Song> songs;

    public SongsRecyclerViewAdapter(Context context, ArrayList<Song> songs) {
        this.context = context;
        this.songs = songs;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.song_list_item, viewGroup, false);

        // Return a new holder instance
        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Song currentSong = songs.get(position);

        viewHolder.songNameTextView.setText(currentSong.getName());
        viewHolder.songDurationTextView.setText(currentSong.getDuration());
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.song_name_text_view)
        TextView songNameTextView;

        @BindView(R.id.song_duration_text_view)
        TextView songDurationTextView;

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(view -> {

                //Save selected Song to a model
                Song selectedSong = songs.get(getAdapterPosition());
                MusicPlayerModel.setSelectedSong(selectedSong);

                //Create a new intent to open the {@link NowPlayingActivity}
                Intent songsIntent = new Intent(context, NowPlayingActivity.class);

                //start the new activity
                context.startActivity(songsIntent);
            });
        }
    }
}
