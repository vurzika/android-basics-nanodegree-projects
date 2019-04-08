package com.viktorija.android.musicplayer.activities.main;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.viktorija.android.musicplayer.R;
import com.viktorija.android.musicplayer.activities.albums.AlbumsActivity;
import com.viktorija.android.musicplayer.models.Artist;
import com.viktorija.android.musicplayer.models.MusicPlayerModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ArtistsRecyclerViewAdapter extends RecyclerView.Adapter<ArtistsRecyclerViewAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Artist> artists;

    /**
     * Adapter to display artists
     *
     * @param context application context to resolve resources
     * @param artists list of artists to display
     */
    public ArtistsRecyclerViewAdapter(Context context, ArrayList<Artist> artists) {
        this.context = context;
        this.artists = artists;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.artist_list_item, viewGroup, false);

        // Return a new holder instance
        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        // Get the {@link Artist} object located at this position in the list
        Artist currentArtist = artists.get(position);

        // Update UI elements
        viewHolder.artistNameTextView.setText(currentArtist.getName());

        int albumCount = currentArtist.getAlbumsCount();
        String albumCountText = context.getResources().getQuantityString(R.plurals.number_of_albums, albumCount, albumCount);

        viewHolder.artistAlbumsCountTextView.setText(albumCountText);

        viewHolder.imageView.setImageResource(currentArtist.getThumbnailId());
    }

    @Override
    public int getItemCount() {
        return artists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.artist_name_text_view)
        TextView artistNameTextView;

        @BindView(R.id.artist_albums_count_text_view)
        TextView artistAlbumsCountTextView;

        @BindView(R.id.artist_icon)
        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(view -> {
                // get item at current position
                Artist currentArtist = artists.get(getAdapterPosition());

                //Save selected Artist to a model
                MusicPlayerModel.setSelectedArtist(currentArtist);

                //Create a new intent to open the {@link AlbumsActivity}
                Intent albumsIntent = new Intent(context, AlbumsActivity.class);

                //start the new activity
                context.startActivity(albumsIntent);
            });
        }
    }
}
