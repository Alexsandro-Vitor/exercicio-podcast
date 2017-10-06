package br.ufpe.cin.if710.podcast.db;

import android.content.ContentResolver;
import android.net.Uri;

/**
 * Created by leopoldomt on 9/19/17.
 */

public class PodcastProviderContract {

    public final static String _ID = "_id";
    public final static String TITLE = "title";
    public final static String DATE = "pubDate";
    public final static String LINK = "link";
    public final static String DESC = "description";
    public final static String DOWNLOAD_LINK = "downloadLink";
    public final static String FILE_URI = "downloadUri";
    public static final String EPISODE_TABLE = "episodes";

    public final static String[] ALL_COLUMNS = {
            _ID, TITLE, DATE, LINK, DESC, DOWNLOAD_LINK, FILE_URI};

    private static final Uri BASE_LIST_URI = Uri.parse("content://br.ufpe.cin.if710.podcast.feed/");
    //URI para tabela
    public static final Uri EPISODE_LIST_URI = Uri.withAppendedPath(BASE_LIST_URI, EPISODE_TABLE);

    // Mime type para colecao de itens
    public static final String CONTENT_DIR_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/PodcastProvider.data.text";

    // Mime type para um item especifico
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/PodcastProvider.data.text";

}