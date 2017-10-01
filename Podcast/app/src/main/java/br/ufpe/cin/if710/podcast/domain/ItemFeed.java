package br.ufpe.cin.if710.podcast.domain;

import android.content.ContentValues;

import br.ufpe.cin.if710.podcast.db.PodcastDBHelper;

public class ItemFeed {
    private final String title;
    private final String link;
    private final String pubDate;
    private final String description;
    private final String downloadLink;


    public ItemFeed(String title, String link, String pubDate, String description, String downloadLink) {
        this.title = title;
        this.link = link;
        this.pubDate = pubDate;
        this.description = description;
        this.downloadLink = downloadLink;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public String getPubDate() {
        return pubDate;
    }

    public String getDescription() {
        return description;
    }

    public String getDownloadLink() {
        return downloadLink;
    }

    public ContentValues toCV() {
        ContentValues cv = new ContentValues();
        cv.put(PodcastDBHelper.EPISODE_TITLE, valid(title));
        cv.put(PodcastDBHelper.EPISODE_LINK, valid(link));
        cv.put(PodcastDBHelper.EPISODE_DATE, valid(pubDate));
        cv.put(PodcastDBHelper.EPISODE_DESC, valid(description));
        cv.put(PodcastDBHelper.EPISODE_DOWNLOAD_LINK, valid(downloadLink));
        cv.put(PodcastDBHelper.EPISODE_FILE_URI, "");
        return cv;
    }

    private String valid(String s) {
        return (s != null) ? s : "";
    }

    @Override
    public String toString() {
        return title;
    }
}