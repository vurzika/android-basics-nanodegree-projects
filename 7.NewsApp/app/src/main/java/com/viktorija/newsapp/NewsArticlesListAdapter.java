package com.viktorija.newsapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsArticlesListAdapter extends RecyclerView.Adapter<NewsArticlesListAdapter.ViewHolder> {

    private List<NewsArticle> newsArticlesList;

    /**
     * Constructor for adapter
     *
     * @param newsArticlesList list of news to use
     */
    public NewsArticlesListAdapter(List<NewsArticle> newsArticlesList) {
        this.newsArticlesList = newsArticlesList;
    }

    // Creates new views (invoked by the layout manager)
    @NonNull
    @Override
    public NewsArticlesListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                                 int viewType) {
        // create a new view
        View listItem = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_list_item, parent, false);

        // create view holder
        final ViewHolder viewHolder = new ViewHolder(listItem);

        // set item click listener to open news article on list item click
        listItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View clickedView) {
                // Find the current news article that was clicked on
                NewsArticle currentNewsArticle = newsArticlesList.get(viewHolder.getAdapterPosition());

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri newsUri = Uri.parse(currentNewsArticle.getWebUrl());

                // Create a new intent to view the news article URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsUri);

                // Send the intent to launch a new activity
                clickedView.getContext().startActivity(websiteIntent);
            }
        });

        return viewHolder;
    }

    // Sets the contents of a list item view based on position (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // get news article based on position
        NewsArticle newsArticle = newsArticlesList.get(position);

        // updates view element
        holder.newsTitleTextView.setText(newsArticle.getWebTitle());
        holder.newsCategoryTextView.setText(newsArticle.getSectionName());
        holder.newsAuthorNameTextView.setText(newsArticle.getAuthorName());

        Date newsArticleDateTime = newsArticle.getWebPublicationDate();

        // if date is available set date, otherwise clean date values
        if (newsArticleDateTime != null) {
            holder.newsDateTextView.setText(formatDate(newsArticleDateTime));
            holder.newsTimeTextView.setText(formatTime(newsArticleDateTime));
        } else {
            holder.newsDateTextView.setText("");
            holder.newsTimeTextView.setText("");
        }

        if (newsArticle.getThumbnailUrl() != null) {
            // hide thumbnail until image is loaded
            holder.newsThumbnailImageView.setVisibility(View.INVISIBLE);

            // download image using url provided
            holder.downloadThumbnailImage(newsArticle.getThumbnailUrl());
        } else {
            // hide image and give space for content
            holder.newsThumbnailImageView.setVisibility(View.GONE);
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return newsArticlesList.size();
    }

    /**
     * Return the formatted date string (i.e. "Mar 3, 1984") from a Date object.
     */
    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(dateObject);
    }

    /**
     * Return the formatted date string (i.e. "4:30 PM") from a Date object.
     */
    private String formatTime(Date dateObject) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateObject);
    }

    /**
     * View Holder for views used on news list item
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.news_title)
        TextView newsTitleTextView;

        @BindView(R.id.news_category)
        TextView newsCategoryTextView;

        @BindView(R.id.date)
        TextView newsDateTextView;

        @BindView(R.id.time)
        TextView newsTimeTextView;

        @BindView(R.id.author_name)
        TextView newsAuthorNameTextView;

        @BindView(R.id.news_thumbnail)
        ImageView newsThumbnailImageView;

        // Task for downloading image for a view holder
        // * See: https://medium.com/@crossphd/android-image-loading-from-a-string-url-6c8290b82c5e
        // * And: https://stackoverflow.com/a/32485013
        private AsyncTask<String, Void, Bitmap> downloadImageTask;

        public ViewHolder(View listItem) {
            super(listItem);

            ButterKnife.bind(this, listItem);
        }

        /**
         * Download image by provided URL and sets it in view holder
         *
         * @param url image url
         */
        public void downloadThumbnailImage(String url) {
            // cancel existing task that downloads the image before downloading new one
            // as existing task might be still running
            if (downloadImageTask != null) {
                downloadImageTask.cancel(true);
            }

            downloadImageTask = new DownloadImageTask().execute(url);
        }

        /**
         * Task for downloading the image based on string
         */
        private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

            protected Bitmap doInBackground(String... urls) {
                // get url
                String url = urls[0];

                // download image
                Bitmap imageBitmap = null;
                try {
                    InputStream in = new java.net.URL(url).openStream();
                    imageBitmap = BitmapFactory.decodeStream(in);
                } catch (Exception e) {
                    Log.e("Error", e.getMessage());
                    e.printStackTrace();
                }
                return imageBitmap;
            }

            protected void onPostExecute(Bitmap result) {
                // if we got result
                if (result != null) {
                    // set image
                    newsThumbnailImageView.setImageBitmap(result);

                    // display image as it was hidden during loading
                    newsThumbnailImageView.setVisibility(View.VISIBLE);
                }
            }
        }
    }
}