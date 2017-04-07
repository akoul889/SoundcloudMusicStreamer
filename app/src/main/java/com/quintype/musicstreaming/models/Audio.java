package com.quintype.musicstreaming.models;

import java.io.Serializable;

public class Audio implements Serializable {

    private String data;
    private String title;
    private String genre;
    private String artist;
    private String artwork;
    private String streamUrl;

    public Audio(String data, String title, String genre, String artist, String artwork, String
            streamUrl) {
        this.data = data;
        this.title = title;
        this.genre = genre;
        this.artist = artist;
        this.artwork = artwork;
        this.streamUrl = streamUrl;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getArtwork() {
        return artwork;
    }

    public void setArtwork(String artwork) {
        this.artwork = artwork;
    }

    public String getStreamUrl() {
        return streamUrl;
    }

    public void setStreamUrl(String streamUrl) {
        this.streamUrl = streamUrl;
    }
}