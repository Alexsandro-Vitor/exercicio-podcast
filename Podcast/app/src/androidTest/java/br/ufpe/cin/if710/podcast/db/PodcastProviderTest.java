package br.ufpe.cin.if710.podcast.db;

import android.content.ContentValues;
import android.database.Cursor;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Alexsandro on 10/12/2017.
 */
public class PodcastProviderTest {
    private PodcastProvider provider = new PodcastProvider();
    private ContentValues cv;

    @Before
    public void setUp() throws Exception {
        provider.onCreate();
        cv = new ContentValues();
        cv.put(PodcastProviderContract.TITLE, "Titulo 1");
        cv.put(PodcastProviderContract.LINK, "Link 1");
        cv.put(PodcastProviderContract.DATE, "Data 1");
        cv.put(PodcastProviderContract.DESC, "Descricao 1");
        cv.put(PodcastProviderContract.DOWNLOAD_LINK, "Link 1");
        cv.put(PodcastProviderContract.FILE_URI, "");
        provider.insert(PodcastProviderContract.EPISODE_LIST_URI, cv);
    }

    @After
    public void tearDown() throws Exception {
        provider.delete(PodcastProviderContract.EPISODE_LIST_URI, null, null);
    }

    @Test
    public void delete() throws Exception {
        String[] selectionArgs = {(String) cv.get(PodcastProviderContract.TITLE)};
        assertEquals(1, provider.delete(PodcastProviderContract.EPISODE_LIST_URI,
                PodcastProviderContract.TITLE + " =?",
                selectionArgs));
    }

    @Test
    public void insert() throws Exception {
        ContentValues cv = new ContentValues();
        cv.put(PodcastProviderContract.TITLE, "Titulo 2");
        cv.put(PodcastProviderContract.LINK, "Link 2");
        cv.put(PodcastProviderContract.DATE, "Data 2");
        cv.put(PodcastProviderContract.DESC, "Descricao 2");
        cv.put(PodcastProviderContract.DOWNLOAD_LINK, "Link 2");
        cv.put(PodcastProviderContract.FILE_URI, "");
        assertNotNull(provider.insert(PodcastProviderContract.EPISODE_LIST_URI, cv));
    }

    @Test
    public void onCreate() throws Exception {
        assertTrue(provider.onCreate());
    }

    @Test
    public void query() throws Exception {
        String[] selectionArgs = {(String) cv.get(PodcastProviderContract.TITLE)};
        Cursor cursor = provider.query(PodcastProviderContract.EPISODE_LIST_URI, PodcastProviderContract.ALL_COLUMNS,
                PodcastProviderContract.TITLE + " =?", selectionArgs, null);
        assertNotNull(cursor);
        assertTrue(cursor.getCount() > 0);
    }

    @Test
    public void update() throws Exception {
        String[] selectionArgs = {(String) cv.get(PodcastProviderContract.TITLE)};
        cv.put(PodcastProviderContract.DESC, "Descricao 2");
        assertEquals(1, provider.update(PodcastProviderContract.EPISODE_LIST_URI, cv,
                PodcastProviderContract.TITLE + " = ?", selectionArgs));
    }

}