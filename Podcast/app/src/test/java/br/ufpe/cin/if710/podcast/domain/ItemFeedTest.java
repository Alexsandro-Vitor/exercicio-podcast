package br.ufpe.cin.if710.podcast.domain;

import android.content.ClipData;
import android.content.ContentValues;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Alexsandro on 13/12/2017.
 */
public class ItemFeedTest {
    private ItemFeed itemFeed;
    private ItemFeed itemFeed2;
    private final String titulo = "Título 1";
    private final String link = "Link 1";
    private final String pubDate = "Pubdate 1";
    private final String description = "Desc 1";
    private final String downloadLink = "DownloadLink 1";
    private final String uri = "Uri 1";

    @Before
    public void setUp() throws Exception {
        itemFeed = new ItemFeed(titulo, link, pubDate, description, downloadLink, uri);
        itemFeed2 = new ItemFeed(null, null, null, null, null, null);
    }

    @Test
    public void getTitle() throws Exception {
        assertEquals(titulo, itemFeed.getTitle());
    }

    @Test
    public void getLink() throws Exception {
        assertEquals(link, itemFeed.getLink());
    }

    @Test
    public void getPubDate() throws Exception {
        assertEquals(pubDate, itemFeed.getPubDate());
    }

    @Test
    public void getDescription() throws Exception {
        assertEquals(description, itemFeed.getDescription());
    }

    @Test
    public void getDownloadLink() throws Exception {
        assertEquals(downloadLink, itemFeed.getDownloadLink());
    }

    @Test
    public void getUri() throws Exception {
        assertEquals(uri, itemFeed.getUri());
    }

    @Test
    public void getEstado() throws Exception {
        assertEquals(ItemFeed.BAIXOU, itemFeed.getEstado());
        assertEquals(ItemFeed.NAO_BAIXOU, itemFeed2.getEstado());
    }

    @Test
    public void setEstado() throws Exception {
        itemFeed.setEstado(ItemFeed.BAIXANDO);
        assertEquals(ItemFeed.BAIXANDO, itemFeed.getEstado());
    }
}