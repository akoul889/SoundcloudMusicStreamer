package com.quintype.musicstreaming.models;

        import com.google.gson.annotations.Expose;
        import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("avatar_url")
    @Expose
    private String avatarUrl;
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("kind")
    @Expose
    private String kind;
    @SerializedName("permalink_url")
    @Expose
    private String permalinkUrl;
    @SerializedName("uri")
    @Expose
    private String uri;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("permalink")
    @Expose
    private String permalink;
    @SerializedName("last_modified")
    @Expose
    private String lastModified;

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getPermalinkUrl() {
        return permalinkUrl;
    }

    public void setPermalinkUrl(String permalinkUrl) {
        this.permalinkUrl = permalinkUrl;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPermalink() {
        return permalink;
    }

    public void setPermalink(String permalink) {
        this.permalink = permalink;
    }

    public String getLastModified() {
        return lastModified;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

}