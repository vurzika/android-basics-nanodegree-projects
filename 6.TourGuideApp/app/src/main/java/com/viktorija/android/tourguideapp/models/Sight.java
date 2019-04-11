package com.viktorija.android.tourguideapp.models;

/**
 * {@link Sight} represents a point of interest the user wants to learn about.
 * It contains a name of the selected point of interest, it's picture, description,
 * web site, address and phone number.
 */
public class Sight {

    /**
     * Name of the point of interest
     */
    private String name;

    /**
     * Category of sight
     */
    public String category;
    /**
     * Picture resource id of the point of interest
     */
    private int photoId;
    /**
     * Description of the point of interest
     */
    private String description;

    /**
     * Web site of the point of interest
     */
    private String webSite;
    /**
     * Address of the point of interest
     */
    private String address;
    /**
     * Phone of the point of interest
     */
    private String phone;

    public Sight(String name, String category, int photoId, String description, String webSite, String address, String phone) {
        this.name = name;
        this.category = category;
        this.photoId = photoId;
        this.description = description;
        this.webSite = webSite;
        this.address = address;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public int getPhotoId() {
        return photoId;
    }

    public String getDescription() {
        return description;
    }

    public String getWebSite() {
        return webSite;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }
}
