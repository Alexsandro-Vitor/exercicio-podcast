package br.ufpe.cin.if710.podcast.domain;

import android.content.ContentValues;

import org.junit.Before;
import org.junit.Test;

import br.ufpe.cin.if710.podcast.db.PodcastProviderContract;

import static org.junit.Assert.*;

/**
 * Created by Alexsandro on 13/12/2017.
 */
public class ItemFeedCVTest {
    private ItemFeed itemFeed2;

    @Before
    public void setUp() throws Exception {
        itemFeed2 = new ItemFeed(null, null, null, null, null, null);
    }

    @Test
    public void toCV() throws Exception {
        ContentValues cv = itemFeed2.toCV();
        assertNotNull(cv.get(PodcastProviderContract.TITLE));
        assertNotNull(cv.get(PodcastProviderContract.LINK));
        assertNotNull(cv.get(PodcastProviderContract.DATE));
        assertNotNull(cv.get(PodcastProviderContract.DESC));
        assertNotNull(cv.get(PodcastProviderContract.DOWNLOAD_LINK));
        assertNotNull(cv.get(PodcastProviderContract.FILE_URI));
    }

}