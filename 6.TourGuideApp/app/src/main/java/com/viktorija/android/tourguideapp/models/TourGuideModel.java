package com.viktorija.android.tourguideapp.models;

import android.content.Context;

import com.viktorija.android.tourguideapp.R;

import java.util.ArrayList;

/**
 * Class containing application state and data
 */
public class TourGuideModel {

    public static final String CATEGORY_NAME_NATURE_AND_PARKS = "Nature and Parks";
    public static final String CATEGORY_NAME_SIGHTS_AND_LANDMARKS = "Sights and Landmarks";
    public static final String CATEGORY_NAME_MUSEUMS = "Museums";
    public static final String CATEGORY_NAME_SHOPPING = "Shopping";

    // Selected Sight
    private static Sight selectedSight;

    // Selected Category Tab Number
    private static int selectedCategoryTabNumber;

    public static Sight getSelectedSight() {
        return selectedSight;
    }

    public static void setSelectedSight(Sight selectedSight) {
        TourGuideModel.selectedSight = selectedSight;
    }

    public static int getSelectedCategoryTabNumber() {
        return selectedCategoryTabNumber;
    }

    public static void setSelectedCategoryTabNumber(int selectedCategoryTabNumber) {
        TourGuideModel.selectedCategoryTabNumber = selectedCategoryTabNumber;
    }

    // Getting sights based on the Sight Category name
    public static ArrayList<Sight> getSights(Context context, String categoryName) {
        if (CATEGORY_NAME_NATURE_AND_PARKS.equals(categoryName)) {
            return getNatureAndParks(context);
        } else if (CATEGORY_NAME_SIGHTS_AND_LANDMARKS.equals(categoryName)) {
            return getSightsAndLandmarks(context);
        } else if (CATEGORY_NAME_MUSEUMS.equals(categoryName)) {
            return getMuseums(context);
        } else {
            return getShopping(context);
        }
    }

    public static ArrayList<Sight> getNatureAndParks(Context context) {

        //Create an ArrayList of Nature and Parks
        final ArrayList<Sight> sights = new ArrayList<>();

        sights.add(new Sight(context.getString(R.string.golden_gate_park), CATEGORY_NAME_NATURE_AND_PARKS, R.drawable.golden_gate_park, context.getString
                (R.string.description_golden_gate_park), context.getString
                (R.string.website_golden_gate_park), context.getString(R.string.address_golden_gate_park),
                context.getString(R.string.phone_golden_gate_park)));
        sights.add(new Sight(context.getString(R.string.muir_woods), CATEGORY_NAME_NATURE_AND_PARKS,R.drawable.muir_woods, context.getString
                (R.string.description_muir_woods), context.getString
                (R.string.website_muir_woods), context.getString(R.string.address_muir_woods),
                context.getString(R.string.phone_muir_woods)));
        sights.add(new Sight(context.getString(R.string.yosemite_national_park), CATEGORY_NAME_NATURE_AND_PARKS,R.drawable.yosemite, context.getString
                (R.string.description_yosemite), context.getString
                (R.string.website_yosemite), context.getString(R.string.address_yosemite),
                context.getString(R.string.phone_yosemite)));
        sights.add(new Sight(context.getString(R.string.monterey), CATEGORY_NAME_NATURE_AND_PARKS, R.drawable.monterey, context.getString
                (R.string.description_monterey), context.getString
                (R.string.website_monterey), context.getString(R.string.address_monterey),
                context.getString(R.string.phone_monterey)));
        return sights;
    }

    public static ArrayList<Sight> getSightsAndLandmarks(Context context) {

        //Create an ArrayList of Sights and Landmarks
        final ArrayList<Sight> sights = new ArrayList<>();

        sights.add(new Sight(context.getString(R.string.alcatraz_island), CATEGORY_NAME_SIGHTS_AND_LANDMARKS, R.drawable.alcatraz, context.getString
                (R.string.description_alcatraz_island), context.getString
                (R.string.website_alcatraz_island), context.getString(R.string.address_alcatraz_island),
                context.getString(R.string.phone_alcatraz_island)));
        sights.add(new Sight(context.getString(R.string.golden_gate_bridge), CATEGORY_NAME_SIGHTS_AND_LANDMARKS, R.drawable.golden_gate_bridge, context.getString
                (R.string.description_golden_gate_bridge), context.getString
                (R.string.website_golden_gate_bridge), context.getString(R.string.address_golden_gate_bridge),
                context.getString(R.string.phone_golden_gate_bridge)));
        sights.add(new Sight(context.getString(R.string.pier_39), CATEGORY_NAME_SIGHTS_AND_LANDMARKS, R.drawable.pier_39, context.getString
                (R.string.description_pier_39), context.getString
                (R.string.website_pier_39), context.getString(R.string.address_pier_39),
                context.getString(R.string.phone_pier_39)));
        sights.add(new Sight(context.getString(R.string.lombard_street), CATEGORY_NAME_SIGHTS_AND_LANDMARKS, R.drawable.lombard_street, context.getString
                (R.string.description_lombard_street), context.getString
                (R.string.website_lombard_street), context.getString(R.string.address_lombard_street),
                context.getString(R.string.phone_lombard_street)));
        sights.add(new Sight(context.getString(R.string.twin_peaks), CATEGORY_NAME_SIGHTS_AND_LANDMARKS, R.drawable.twin_peaks, context.getString
                (R.string.description_twin_peaks), context.getString
                (R.string.website_twin_peaks), context.getString(R.string.address_twin_peaks),
                context.getString(R.string.phone_twin_peaks)));
        return sights;
    }

    public static ArrayList<Sight> getMuseums(Context context) {

        //Create an ArrayList of Museums
        final ArrayList<Sight> sights = new ArrayList<>();

        sights.add(new Sight(context.getString(R.string.california_academy_of_sciences), CATEGORY_NAME_MUSEUMS, R.drawable.academy_of_sciences, context.getString
                (R.string.description_california_academy_of_sciences), context.getString
                (R.string.website_california_academy_of_sciences), context.getString(R.string.address_california_academy_of_sciences),
                context.getString(R.string.phone_california_academy_of_sciences)));
        sights.add(new Sight(context.getString(R.string.de_young_museum), CATEGORY_NAME_MUSEUMS, R.drawable.de_young_museum, context.getString
                (R.string.description_de_young_museum), context.getString
                (R.string.website_de_young_museum), context.getString(R.string.address_de_young_museum),
                context.getString(R.string.phone_de_young_museum)));
        sights.add(new Sight(context.getString(R.string.computer_history_museum), CATEGORY_NAME_MUSEUMS, R.drawable.computer_history_museum, context.getString
                (R.string.description_computer_history_museum), context.getString
                (R.string.website_computer_history_museum), context.getString(R.string.address_computer_history_museum),
                context.getString(R.string.phone_computer_history_museum)));
        return sights;
    }

    public static ArrayList<Sight> getShopping(Context context) {

        //Create an ArrayList of Shopping places
        final ArrayList<Sight> sights = new ArrayList<>();

        sights.add(new Sight(context.getString(R.string.westfield_san_francisco_centre), CATEGORY_NAME_SHOPPING, R.drawable.westfield_san_francisco, context.getString
                (R.string.description_westfield_san_francisco_centre), context.getString
                (R.string.website_westfield_san_francisco_centre), context.getString(R.string.address_westfield_san_francisco_centre),
                context.getString(R.string.phone_westfield_san_francisco_centre)));
        sights.add(new Sight(context.getString(R.string.stanford_shopping_center), CATEGORY_NAME_SHOPPING, R.drawable.stanford_shopping_center, context.getString
                (R.string.description_stanford_shopping_center), context.getString
                (R.string.website_stanford_shopping_centere), context.getString(R.string.address_stanford_shopping_center),
                context.getString(R.string.phone_stanford_shopping_center)));
        sights.add(new Sight(context.getString(R.string.san_francisco_premium_outlets), CATEGORY_NAME_SHOPPING, R.drawable.livermore_premium_outlets, context.getString
                (R.string.description_san_francisco_premium_outlets), context.getString
                (R.string.website_san_francisco_premium_outlets), context.getString(R.string.address_san_francisco_premium_outlets),
                context.getString(R.string.phone_san_francisco_premium_outlets)));
        return sights;
    }

}
