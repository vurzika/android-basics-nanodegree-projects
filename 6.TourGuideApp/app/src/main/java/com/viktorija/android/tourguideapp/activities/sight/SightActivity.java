package com.viktorija.android.tourguideapp.activities.sight;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.viktorija.android.tourguideapp.R;
import com.viktorija.android.tourguideapp.models.Sight;
import com.viktorija.android.tourguideapp.models.TourGuideModel;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.text.Layout.JUSTIFICATION_MODE_INTER_WORD;

public class SightActivity extends AppCompatActivity {

    public static final String EXTRA_BACKGROUND_COLOR_ID = "EXTRA_BACKGROUND_COLOR_ID";

    @BindView(R.id.sight_activity_scroll_view)
    ScrollView scrollView;

    @BindView(R.id.sight_details_image)
    ImageView sightImageView;

    @BindView(R.id.sight_details_overview)
    TextView overviewTextView;

    @BindView(R.id.sight_details_address)
    TextView addressTextView;

    @BindView(R.id.sight_details_phone_number)
    TextView phoneTextView;

    @BindView(R.id.sight_details_web_site)
    TextView websiteTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sight);

        ButterKnife.bind(this);

        // check if we have custom background color
        Intent intent = getIntent();
        int backgroundColorId = intent.getIntExtra(EXTRA_BACKGROUND_COLOR_ID, R.color.category_nature_and_parks);

        // get color by color id from resources
        int backgroundColor = ContextCompat.getColor(this, backgroundColorId);

        // set custom background background color
        scrollView.setBackgroundColor(backgroundColor);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(backgroundColor));

        // get selected data element
        Sight selectedSight = TourGuideModel.getSelectedSight();

        // Set activity title
        setTitle(selectedSight.getName());

        // And content
        sightImageView.setImageResource(selectedSight.getPhotoId());
        overviewTextView.setText(selectedSight.getDescription());

        // set text of an overviewTextView to be Justified (On Android 26+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            overviewTextView.setJustificationMode(JUSTIFICATION_MODE_INTER_WORD);
        }

        addressTextView.setText(selectedSight.getAddress());
        phoneTextView.setText(selectedSight.getPhone());
        websiteTextView.setText(selectedSight.getWebSite());
    }
}
