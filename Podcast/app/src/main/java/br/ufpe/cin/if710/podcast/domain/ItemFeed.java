package br.ufpe.cin.if710.podcast.domain;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;

import br.ufpe.cin.if710.podcast.db.PodcastDBHelper;
import br.ufpe.cin.if710.podcast.db.PodcastProviderContract;

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

    public ItemFeed(Cursor cursor) {
        title = getColValue(cursor, PodcastProviderContract.TITLE);
        link = getColValue(cursor, PodcastProviderContract.LINK);
        pubDate = getColValue(cursor, PodcastProviderContract.DATE);
        description = getColValue(cursor, PodcastProviderContract.DESC);
        downloadLink = getColValue(cursor, PodcastProviderContract.DOWNLOAD_LINK);
    }

    private String getColValue(Cursor cursor, String col) {
        return cursor.getString(cursor.getColumnIndex(col));
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
        cv.put(PodcastProviderContract.TITLE, valid(title));
        cv.put(PodcastProviderContract.LINK, valid(link));
        cv.put(PodcastProviderContract.DATE, valid(pubDate));
        cv.put(PodcastProviderContract.DESC, valid(description));
        cv.put(PodcastProviderContract.DOWNLOAD_LINK, valid(downloadLink));
        cv.put(PodcastProviderContract.FILE_URI, "");
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