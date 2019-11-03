package com.viktorija.newsapp;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.util.List;

/**
 * Loads a list of news by using an AsyncTask to perform the
 * network request to the given URL.
 */
public class NewsLoader extends AsyncTaskLoader<List<NewsArticle>> {

    /**
     * Query URL
     */
    private String url;

    /**
     * Constructs a new {@link NewsLoader} object.
     *
     * @param context application context
     * @param url     url for loading the data
     */
    public NewsLoader(Context context, String url) {
        super(context);

        this.url = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /**
     * This is on a background thread.
     */
    @Override
    public List<NewsArticle> loadInBackground() {
        if (url == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list of news.
        return QueryUtils.fetchNewsData(url);
    }
}
