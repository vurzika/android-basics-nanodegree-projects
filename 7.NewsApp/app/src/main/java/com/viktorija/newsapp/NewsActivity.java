package com.viktorija.newsapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Main activity that displays list of news
 */
public class NewsActivity extends AppCompatActivity implements LoaderCallbacks<List<NewsArticle>> {

    @BindView(R.id.empty_view)
    TextView emptyStateTextView;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    // Swipe Refresh Layout for "pull-to-refresh"
    @BindView(R.id.news_swipe_refresh_layout)
    SwipeRefreshLayout newsSwipeRefreshLayout;

    /**
     * URL for news dataset
     */
    private static final String NEWS_REQUEST_URL =
            "https://content.guardianapis.com/search";

    /**
     * Constant value for the movies loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int NEWS_LOADER_ID = 1;

    /**
     * List of news that is displayed in the app (initially empty)
     */
    private List<NewsArticle> newsList = new ArrayList<>();

    /**
     * List adapter
     */
    private RecyclerView.Adapter newsListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        ButterKnife.bind(this);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Create a new adapter that takes an empty list of news as input
        newsListAdapter = new NewsArticlesListAdapter(newsList);
        recyclerView.setAdapter(newsListAdapter);

        // set listener to handle pull-to-refresh
        newsSwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        refreshData();
                    }
                }
        );

        updateUI(newsList);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // refresh data every time we open this activity or get back to
        // this activity by returning from settings screen
        refreshData();
    }

    /**
     * Refreshes data on the screen
     */
    private void refreshData() {
        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        // Hide list before loading data
        recyclerView.setVisibility(View.GONE);

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            // display loading indicator
            newsSwipeRefreshLayout.setRefreshing(true);

            // hide other elements
            emptyStateTextView.setVisibility(View.GONE);

            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getSupportLoaderManager();

            // Initialize or restart the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.restartLoader(NEWS_LOADER_ID, null, this);
        } else {
            // Otherwise, display error
            // First, hide loading indicator and list so error message will be visible
            newsSwipeRefreshLayout.setRefreshing(false);

            // Update empty state with no connection error message
            emptyStateTextView.setVisibility(View.VISIBLE);
            emptyStateTextView.setText(R.string.no_internet_connection);
        }
    }

    /**
     * Updates UI contents to display provided data
     *
     * @param data data to display
     */
    private void updateUI(List<NewsArticle> data) {
        // replace data with new data set
        newsList.clear();

        if (data != null) {
            newsList.addAll(data);
        }

        // notify recycle view that data changed
        newsListAdapter.notifyDataSetChanged();

        // if we have data then display list
        if (newsList.size() > 0) {
            recyclerView.setVisibility(View.VISIBLE);
            emptyStateTextView.setVisibility(View.GONE);
        } else {
            // otherwise display error message
            recyclerView.setVisibility(View.GONE);
            emptyStateTextView.setVisibility(View.VISIBLE);
            emptyStateTextView.setText(R.string.no_news);
        }
    }

    @Override
    @NonNull
    // onCreateLoader instantiates and returns a new Loader for the given ID
    public Loader<List<NewsArticle>> onCreateLoader(int i, @Nullable Bundle bundle) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        // get news topic value from settings (if not available use default topic)
        String newsTopic = sharedPreferences.getString(
                getString(R.string.settings_news_topic_key),
                getString(R.string.settings_news_topic_default)
        );

        // check if it is needed to display thumbnails from settings (if not available use default)
        boolean showThumbnails = sharedPreferences.getBoolean(
                getString(R.string.settings_show_thumbnails_key),
                getResources().getBoolean(R.bool.settings_show_thumbnails_default)
        );

        // parse breaks apart the URI string that's passed into its parameter
        Uri baseUri = Uri.parse(NEWS_REQUEST_URL);

        // buildUpon prepares the baseUri that we just parsed so we can add query parameters to it
        Uri.Builder uriBuilder = baseUri.buildUpon();

        // Append query parameter and its value.
        uriBuilder.appendQueryParameter("api-key", "935e33f2-84b4-4bf6-b60a-e0408925b8f4");
        uriBuilder.appendQueryParameter("q", newsTopic);
        uriBuilder.appendQueryParameter("show-tags", "contributor");
        if (showThumbnails) {
            uriBuilder.appendQueryParameter("show-fields", "thumbnail");
        }

        // Return the completed uri
        return new NewsLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<NewsArticle>> loader, List<NewsArticle> newsArticles) {
        // Hide loading indicator because the data has been loaded
        newsSwipeRefreshLayout.setRefreshing(false);

        updateUI(newsArticles);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<NewsArticle>> loader) {
        // Loader reset, so we can clear out our existing data.
        newsList.clear();
        newsListAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
