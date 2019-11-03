package com.viktorija.newsapp;

import java.util.Date;

/**
 * News article containing summary information about a news
 */
public class NewsArticle {

    private String webTitle;

    private String sectionName;

    private Date webPublicationDate;

    private String webUrl;

    private String authorName;

    private String thumbnailUrl;

    /**
     * Constructs a new {@link NewsArticle} object.
     *
     * @param webTitle           article title
     * @param sectionName        article section name
     * @param webPublicationDate date when article was published
     * @param webUrl             url of an article
     * @param authorName         name of the author
     */
    public NewsArticle(String webTitle, String sectionName, Date webPublicationDate, String webUrl, String authorName, String thumbnailUrl) {
        this.webTitle = webTitle;
        this.sectionName = sectionName;
        this.webPublicationDate = webPublicationDate;
        this.webUrl = webUrl;
        this.authorName = authorName;
        this.thumbnailUrl = thumbnailUrl;
    }

    /**
     * Returns news article title
     */
    public String getWebTitle() {
        return webTitle;
    }

    /**
     * Returns news article section name
     */
    public String getSectionName() {
        return sectionName;
    }

    /**
     * Returns news article publication date
     */
    public Date getWebPublicationDate() {
        return webPublicationDate;
    }

    /**
     * Returns news article web page url
     */
    public String getWebUrl() {
        return webUrl;
    }

    /**
     * Returns the name of the author
     */
    public String getAuthorName() {
        return authorName;
    }

    /**
     * Returns url of the thumbnail
     */
    public String getThumbnailUrl() {
        return thumbnailUrl;
    }
}
