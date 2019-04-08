package com.viktorija.android.musicplayer.activities.albums;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.viktorija.android.musicplayer.R;
import com.viktorija.android.musicplayer.activities.songs.SongsActivity;
import com.viktorija.android.musicplayer.models.Album;
import com.viktorija.android.musicplayer.models.MusicPlayerModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AlbumsRecyclerViewAdapter extends RecyclerView.Adapter<AlbumsRecyclerViewAdapter.ViewHolder> {

    private Context context;
    private List<Album> albums;

    /**
     * Adapter to display albums
     *
     * @param context The current context. Used to inflate the layout file.
     * @param albums  A List of album objects to display in a list.
     */
    public AlbumsRecyclerViewAdapter(Activity context, ArrayList<Album> albums) {
        this.context = context;
        this.albums = albums;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.album_grid_item, viewGroup, false);

        // Return a new holder instance
        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Album currentAlbum = albums.get(position);

        viewHolder.albumNameTextView.setText(currentAlbum.getName());

        // Get the version number from the current Album object and
        // set this text on the TextView
        int songCount = currentAlbum.getSongCount();
        Resources resources = context.getResources();
        String songCountText = resources.getQuantityString(R.plurals.number_of_songs, songCount, songCount);

        String albumDescription = resources.getString(R.string.album_description, songCountText, currentAlbum.getYear());
        viewHolder.albumDescriptionTextView.setText(albumDescription);

        viewHolder.albumThumbnailImageView.setImageResource(currentAlbum.getThumbnailId());
    }

    @Override
    public int getItemCount() {
        return albums.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.album_name_text_view)
        TextView albumNameTextView;

        @BindView(R.id.album_description_text_view)
        TextView albumDescriptionTextView;

        @BindView(R.id.album_thumbnail_image_view)
        ImageView albumThumbnailImageView;

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(view -> {
                //Save selected Album to a model
                Album selectedAlbum = albums.get(getAdapterPosition());
                MusicPlayerModel.setSelectedAlbum(selectedAlbum);

                //Create a new intent to open the {@link SongsActivity}
                Intent songsIntent = new Intent(context, SongsActivity.class);

                //start the new activity
                context.startActivity(songsIntent);
            });
        }
    }
}
