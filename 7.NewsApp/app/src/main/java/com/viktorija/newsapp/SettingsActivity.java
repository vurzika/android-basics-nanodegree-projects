package com.viktorija.newsapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

/**
 * Responsible for settings screen
 */
public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
    }

    public static class NewsArticlePreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings_main);

            // setup initial preference values

            // get preferences
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.getActivity());

            // setup news topic preference
            Preference newsTopicPreference = findPreference(getString(R.string.settings_news_topic_key));
            newsTopicPreference.setOnPreferenceChangeListener(this);
            // update summary to display current value from preferences
            String newsTopicValue = preferences.getString(
                    newsTopicPreference.getKey(),
                    getString(R.string.settings_news_topic_default)
            );
            onPreferenceChange(newsTopicPreference, newsTopicValue);

            // setup show thumbnails preference
            Preference showThumbnailsPreference = findPreference(getString(R.string.settings_show_thumbnails_key));
            showThumbnailsPreference.setOnPreferenceChangeListener(this);
            // update summary to display current value from preferences
            boolean showThumbnailsValue = preferences.getBoolean(
                    showThumbnailsPreference.getKey(),
                    getResources().getBoolean(R.bool.settings_show_thumbnails_default)
            );
            onPreferenceChange(showThumbnailsPreference, showThumbnailsValue);
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            // The code in this method takes care of updating the displayed preference summary after it has been changed
            if (preference instanceof CheckBoxPreference) {
                CheckBoxPreference checkBoxPreference = (CheckBoxPreference) preference;
                if (checkBoxPreference.isChecked()) {
                    preference.setSummary(getString(R.string.settings_show_thumbnails_summary_checked));
                } else {
                    preference.setSummary(getString(R.string.settings_show_thumbnails_summary_unchecked));
                }
            } else {
                preference.setSummary(value.toString());
            }
            return true;
        }
    }
}
