package com.quintype.musicstreaming.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Track {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("kind")
    @Expose
    private String kind;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("last_modified")
    @Expose
    private String lastModified;
    @SerializedName("permalink")
    @Expose
    private String permalink;
    @SerializedName("permalink_url")
    @Expose
    private String permalinkUrl;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("duration")
    @Expose
    private int duration;
    @SerializedName("sharing")
    @Expose
    private String sharing;
    @SerializedName("waveform_url")
    @Expose
    private String waveformUrl;
    @SerializedName("stream_url")
    @Expose
    private String streamUrl;
    @SerializedName("uri")
    @Expose
    private String uri;
    @SerializedName("user_id")
    @Expose
    private int userId;
    @SerializedName("artwork_url")
    @Expose
    private String artworkUrl;
    @SerializedName("comment_count")
    @Expose
    private int commentCount;
    @SerializedName("commentable")
    @Expose
    private boolean commentable;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("download_count")
    @Expose
    private int downloadCount;
    @SerializedName("downloadable")
    @Expose
    private boolean downloadable;
    @SerializedName("embeddable_by")
    @Expose
    private String embeddableBy;
    @SerializedName("favoritings_count")
    @Expose
    private int favoritingsCount;
    @SerializedName("genre")
    @Expose
    private String genre;
    @SerializedName("isrc")
    @Expose
    private Object isrc;
    @SerializedName("label_id")
    @Expose
    private Object labelId;
    @SerializedName("label_name")
    @Expose
    private Object labelName;
    @SerializedName("license")
    @Expose
    private String license;
    @SerializedName("original_content_size")
    @Expose
    private int originalContentSize;
    @SerializedName("original_format")
    @Expose
    private String originalFormat;
    @SerializedName("playback_count")
    @Expose
    private int playbackCount;
    @SerializedName("purchase_title")
    @Expose
    private Object purchaseTitle;
    @SerializedName("purchase_url")
    @Expose
    private Object purchaseUrl;
    @SerializedName("release")
    @Expose
    private String release;
    @SerializedName("release_day")
    @Expose
    private Object releaseDay;
    @SerializedName("release_month")
    @Expose
    private Object releaseMonth;
    @SerializedName("release_year")
    @Expose
    private Object releaseYear;
    @SerializedName("reposts_count")
    @Expose
    private int repostsCount;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("streamable")
    @Expose
    private boolean streamable;
    @SerializedName("tag_list")
    @Expose
    private String tagList;
    @SerializedName("track_type")
    @Expose
    private Object trackType;
    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("likes_count")
    @Expose
    private int likesCount;
    @SerializedName("attachments_uri")
    @Expose
    private String attachmentsUri;
    @SerializedName("bpm")
    @Expose
    private Object bpm;
    @SerializedName("key_signature")
    @Expose
    private String keySignature;
    @SerializedName("user_favorite")
    @Expose
    private boolean userFavorite;
    @SerializedName("user_playback_count")
    @Expose
    private Object userPlaybackCount;
    @SerializedName("video_url")
    @Expose
    private Object videoUrl;
    @SerializedName("download_url")
    @Expose
    private Object downloadUrl;

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

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getLastModified() {
        return lastModified;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    public String getPermalink() {
        return permalink;
    }

    public void setPermalink(String permalink) {
        this.permalink = permalink;
    }

    public String getPermalinkUrl() {
        return permalinkUrl;
    }

    public void setPermalinkUrl(String permalinkUrl) {
        this.permalinkUrl = permalinkUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getSharing() {
        return sharing;
    }

    public void setSharing(String sharing) {
        this.sharing = sharing;
    }

    public String getWaveformUrl() {
        return waveformUrl;
    }

    public void setWaveformUrl(String waveformUrl) {
        this.waveformUrl = waveformUrl;
    }

    public String getStreamUrl() {
        return streamUrl;
    }

    public void setStreamUrl(String streamUrl) {
        this.streamUrl = streamUrl;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getArtworkUrl() {
        return artworkUrl;
    }

    public void setArtworkUrl(String artworkUrl) {
        this.artworkUrl = artworkUrl;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public boolean isCommentable() {
        return commentable;
    }

    public void setCommentable(boolean commentable) {
        this.commentable = commentable;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(int downloadCount) {
        this.downloadCount = downloadCount;
    }

    public boolean isDownloadable() {
        return downloadable;
    }

    public void setDownloadable(boolean downloadable) {
        this.downloadable = downloadable;
    }

    public String getEmbeddableBy() {
        return embeddableBy;
    }

    public void setEmbeddableBy(String embeddableBy) {
        this.embeddableBy = embeddableBy;
    }

    public int getFavoritingsCount() {
        return favoritingsCount;
    }

    public void setFavoritingsCount(int favoritingsCount) {
        this.favoritingsCount = favoritingsCount;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Object getIsrc() {
        return isrc;
    }

    public void setIsrc(Object isrc) {
        this.isrc = isrc;
    }

    public Object getLabelId() {
        return labelId;
    }

    public void setLabelId(Object labelId) {
        this.labelId = labelId;
    }

    public Object getLabelName() {
        return labelName;
    }

    public void setLabelName(Object labelName) {
        this.labelName = labelName;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public int getOriginalContentSize() {
        return originalContentSize;
    }

    public void setOriginalContentSize(int originalContentSize) {
        this.originalContentSize = originalContentSize;
    }

    public String getOriginalFormat() {
        return originalFormat;
    }

    public void setOriginalFormat(String originalFormat) {
        this.originalFormat = originalFormat;
    }

    public int getPlaybackCount() {
        return playbackCount;
    }

    public void setPlaybackCount(int playbackCount) {
        this.playbackCount = playbackCount;
    }

    public Object getPurchaseTitle() {
        return purchaseTitle;
    }

    public void setPurchaseTitle(Object purchaseTitle) {
        this.purchaseTitle = purchaseTitle;
    }

    public Object getPurchaseUrl() {
        return purchaseUrl;
    }

    public void setPurchaseUrl(Object purchaseUrl) {
        this.purchaseUrl = purchaseUrl;
    }

    public String getRelease() {
        return release;
    }

    public void setRelease(String release) {
        this.release = release;
    }

    public Object getReleaseDay() {
        return releaseDay;
    }

    public void setReleaseDay(Object releaseDay) {
        this.releaseDay = releaseDay;
    }

    public Object getReleaseMonth() {
        return releaseMonth;
    }

    public void setReleaseMonth(Object releaseMonth) {
        this.releaseMonth = releaseMonth;
    }

    public Object getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(Object releaseYear) {
        this.releaseYear = releaseYear;
    }

    public int getRepostsCount() {
        return repostsCount;
    }

    public void setRepostsCount(int repostsCount) {
        this.repostsCount = repostsCount;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public boolean isStreamable() {
        return streamable;
    }

    public void setStreamable(boolean streamable) {
        this.streamable = streamable;
    }

    public String getTagList() {
        return tagList;
    }

    public void setTagList(String tagList) {
        this.tagList = tagList;
    }

    public Object getTrackType() {
        return trackType;
    }

    public void setTrackType(Object trackType) {
        this.trackType = trackType;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    public String getAttachmentsUri() {
        return attachmentsUri;
    }

    public void setAttachmentsUri(String attachmentsUri) {
        this.attachmentsUri = attachmentsUri;
    }

    public Object getBpm() {
        return bpm;
    }

    public void setBpm(Object bpm) {
        this.bpm = bpm;
    }

    public String getKeySignature() {
        return keySignature;
    }

    public void setKeySignature(String keySignature) {
        this.keySignature = keySignature;
    }

    public boolean isUserFavorite() {
        return userFavorite;
    }

    public void setUserFavorite(boolean userFavorite) {
        this.userFavorite = userFavorite;
    }

    public Object getUserPlaybackCount() {
        return userPlaybackCount;
    }

    public void setUserPlaybackCount(Object userPlaybackCount) {
        this.userPlaybackCount = userPlaybackCount;
    }

    public Object getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(Object videoUrl) {
        this.videoUrl = videoUrl;
    }

    public Object getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(Object downloadUrl) {
        this.downloadUrl = downloadUrl;
    }
}