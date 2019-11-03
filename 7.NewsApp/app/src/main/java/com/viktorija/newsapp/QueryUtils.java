/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.viktorija.newsapp;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Helper methods related to requesting and receiving news article data.
 */
public final class QueryUtils {

    /**
     * Tag for the log messages
     */
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /**
     * Date format used for parsing news publication date
     **/
    private static final String PUBLICATION_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Query the Guardian dataset and return a list of {@link NewsArticle} objects.
     */
    public static List<NewsArticle> fetchNewsData(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link News Article}s
        List<NewsArticle> newsArticleList = extractFeatureFromJson(jsonResponse);

        // Return the list of {@link News Article}s
        return newsArticleList;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the news article's JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return a list of {@link NewsArticle} objects that has been built up from
     * parsing the given JSON response.
     */
    private static List<NewsArticle> extractFeatureFromJson(String newsJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(newsJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding news to
        List<NewsArticle> newsArticles = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(newsJSON);

            JSONObject responseJson = baseJsonResponse.getJSONObject("response");

            // Extract the JSONArray associated with the key called "results",
            // which represents a list of features (or news).
            JSONArray newsArrayJson = responseJson.getJSONArray("results");

            // For each news in the newsArray, create an {@link NewsArticle} object
            for (int i = 0; i < newsArrayJson.length(); i++) {

                // Get a single news at position i within the list of news
                JSONObject currentNewsArticleJson = newsArrayJson.getJSONObject(i);

                String webTitle = currentNewsArticleJson.getString("webTitle");
                String sectionName = currentNewsArticleJson.getString("sectionName");
                String webUrl = currentNewsArticleJson.getString("webUrl");

                String webPublicationDateString = currentNewsArticleJson.getString("webPublicationDate");
                Date webPublicationDate = parsePublicationDate(webPublicationDateString);

                String authorName = null;

                // Find a tag with type "contributor" to get article author if available
                JSONArray tagsJson = currentNewsArticleJson.getJSONArray("tags");
                for (int j = 0; j < tagsJson.length(); j++) {
                    JSONObject tagsItemJson = tagsJson.getJSONObject(j);
                    if ("contributor".equals(tagsItemJson.getString("type"))) {
                        authorName = tagsItemJson.getString("webTitle");
                        break;
                    }
                }

                String thumbnailUrl = null;

                // check if we have object fields (will be absent if thumbnails are not requested)
                if (currentNewsArticleJson.has("fields")) {
                    JSONObject fieldsJson = currentNewsArticleJson.getJSONObject("fields");
                    thumbnailUrl = fieldsJson.getString("thumbnail");
                }

                // Create a new {@link News Article} object with the magnitude, location, time,
                // and url from the JSON response.
                NewsArticle newsArticle = new NewsArticle(webTitle, sectionName, webPublicationDate, webUrl, authorName, thumbnailUrl);

                // Add the new {@link News Article} to the list of news Articles.
                newsArticles.add(newsArticle);
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the news article's JSON results", e);
        }

        // Return the list of newsArticles
        return newsArticles;
    }

    /**
     * Parses String to Date
     *
     * @param dateString date represented as string
     * @return date
     */
    private static Date parsePublicationDate(String dateString) {
        try {
            return new SimpleDateFormat(PUBLICATION_DATE_FORMAT).parse(dateString);
        } catch (ParseException e) {
            Log.e(LOG_TAG, "Unable to parse publication date ", e);
        }

        return null;
    }
}