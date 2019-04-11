package com.viktorija.android.tourguideapp.activities.main;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.viktorija.android.tourguideapp.R;
import com.viktorija.android.tourguideapp.activities.sight.SightActivity;
import com.viktorija.android.tourguideapp.models.Sight;
import com.viktorija.android.tourguideapp.models.TourGuideModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class SightTabFragment extends Fragment {

    private final static String PARAM_CATEGORY_NAME = "PARAM_CATEGORY_NAME";
    private final static String PARAM_CATEGORY_COLOR = "PARAM_CATEGORY_COLOR";

    private int categoryColorId;
    private String categoryName;

    @BindView(R.id.list)
    ListView listView;

    public SightTabFragment() {
        // Required empty public constructor
    }

    public static SightTabFragment newInstance(String categoryName, int categoryColorId) {
        SightTabFragment myFragment = new SightTabFragment();

        // passing a bundle to the setArguments method so data is available after a Fragment is recreated
        Bundle args = new Bundle();
        args.putString(PARAM_CATEGORY_NAME, categoryName);
        args.putInt(PARAM_CATEGORY_COLOR, categoryColorId);
        myFragment.setArguments(args);

        return myFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();

        if (arguments != null) {
            categoryName = arguments.getString(PARAM_CATEGORY_NAME);
            categoryColorId = arguments.getInt(PARAM_CATEGORY_COLOR);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.sight_list, container, false);

        ButterKnife.bind(this, rootView);

        final ArrayList<Sight> sights = TourGuideModel.getSights(getContext(), categoryName);

        SightListAdapter itemsAdapter =
                new SightListAdapter(getActivity(), sights);

        // Make the {@link ListView} use the {@link ArrayAdapter} we created above, so that the
        // {@link ListView} will display list items for each sight in the list of sights.
        listView.setAdapter(itemsAdapter);

        listView.setBackgroundColor(getResources().getColor(categoryColorId));


        // Set a click listener to show details of the selected sight
        listView.setOnItemClickListener((parent, view, position, id) -> {

            // Get the {@link Sight} object at the given position the user clicked on
            Sight sight = sights.get(position);

            //Save selected Sight to a model
            TourGuideModel.setSelectedSight(sight);

            //Create a new intent to open the {@link SightActivity}
            Intent sightIntent = new Intent(getActivity(), SightActivity.class);

            // Save to intent color we want to use for details activity
            sightIntent.putExtra(SightActivity.EXTRA_BACKGROUND_COLOR_ID, categoryColorId);

            startActivity(sightIntent);
        });

        return rootView;
    }
}