package com.viktorija.android.tourguideapp.activities.main;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.viktorija.android.tourguideapp.R;
import com.viktorija.android.tourguideapp.models.TourGuideModel;

public class CategoryTabsAdapter extends FragmentPagerAdapter {

    /**
     * Context of the app
     */
    private Context context;

    /**
     * Create a new {@link CategoryTabsAdapter} object.
     *
     * @param context is the context of the app used to read resources
     * @param fm      is the fragment manager that will keep each fragment's state in the adapter
     *                across swipes.
     */
    public CategoryTabsAdapter(Context context, FragmentManager fm) {
        super(fm);

        this.context = context;
    }

    /**
     * Returns the {@link Fragment} that should be displayed for the given page number.
     */
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            // (R.color.category_museums, TourGuideModel.getMuseums(context))
            case 0:
                return SightTabFragment.newInstance(TourGuideModel.CATEGORY_NAME_NATURE_AND_PARKS, R.color.category_nature_and_parks);
            case 1:
                return SightTabFragment.newInstance(TourGuideModel.CATEGORY_NAME_SIGHTS_AND_LANDMARKS, R.color.category_sights_and_landmarks);
            case 2:
                return SightTabFragment.newInstance(TourGuideModel.CATEGORY_NAME_MUSEUMS, R.color.category_museums);
            default:
                return SightTabFragment.newInstance(TourGuideModel.CATEGORY_NAME_SHOPPING, R.color.category_shopping);
        }
    }

    /**
     * Returns the total number of pages.
     */
    @Override
    public int getCount() {
        return 4;
    }

    /**
     * Returns tab title
     *
     * @param position tab position
     * @return title to display for position
     */
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return context.getString(R.string.category_nature_and_parks);
            case 1:
                return context.getString(R.string.category_sights_and_landmarks);
            case 2:
                return context.getString(R.string.category_museums);
            default:
                return context.getString(R.string.category_shopping);
        }
    }
}