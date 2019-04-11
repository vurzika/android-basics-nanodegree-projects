package com.viktorija.android.tourguideapp.activities.main;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.viktorija.android.tourguideapp.R;
import com.viktorija.android.tourguideapp.models.Sight;

import java.util.ArrayList;

/**
 * Adapter responsible for returning individual views in lists
 */
public class SightListAdapter extends ArrayAdapter<Sight> {

    /**
     * This is our own custom constructor (it doesn't mirror a superclass constructor).
     * The context is used to inflate the layout file, and the list is the data we want
     * to populate into the lists.
     *
     * @param context The current context. Used to inflate the layout file.
     * @param sights  A List of sight objects to display in a list.
     */
    public SightListAdapter(Activity context, ArrayList<Sight> sights) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for two TextViews and an ImageView, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context, 0, sights);
    }

    /**
     * Provides a view for an AdapterView (ListView, GridView, etc.)
     *
     * @param position    The position in the list of data that should be displayed in the
     *                    list item view.
     * @param convertView The recycled view to populate.
     * @param parent      The parent ViewGroup that is used for inflation.
     * @return The View for the position in the AdapterView.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Check if the existing view is being reused, otherwise inflate the view (we get the
        // list item vie that we can use
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        // Get current sight to display
        Sight currentSight = getItem(position);

        // Set name of point of interest
        TextView pointOfInterestNameTextView = listItemView.findViewById(R.id.point_of_interest_name);
        pointOfInterestNameTextView.setText(currentSight.getName());

        // Set background image
        ImageView signImageView = listItemView.findViewById(R.id.sight_image);
        signImageView.setBackgroundResource(currentSight.getPhotoId());

        // Return created view
        return listItemView;
    }
}
