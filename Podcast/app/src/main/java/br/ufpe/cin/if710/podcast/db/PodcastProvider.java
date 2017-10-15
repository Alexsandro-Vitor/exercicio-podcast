package br.ufpe.cin.if710.podcast.db;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

public class PodcastProvider extends ContentProvider {

    PodcastDBHelper db;

    public PodcastProvider() {
    }

    private boolean isEpisodeUri(Uri uri) {
        return uri.getLastPathSegment().equals(PodcastProviderContract.EPISODE_TABLE);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        if (isEpisodeUri(uri)) {
            return db.getWritableDatabase().delete(PodcastDBHelper.DATABASE_TABLE, selection, selectionArgs);
        } return 0;
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        if (isEpisodeUri(uri)) {
            SQLiteDatabase data = db.getWritableDatabase();
            long id = data.insert(PodcastDBHelper.DATABASE_TABLE, null, values);
            Log.d("PodcastProvider", "Inseriu "+id);
            return Uri.withAppendedPath(PodcastProviderContract.EPISODE_LIST_URI, Long.toString(id));
        } return null;
    }

    @Override
    public boolean onCreate() {
        db = PodcastDBHelper.getInstance(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        if (isEpisodeUri(uri)) {
            return db.getReadableDatabase().query(PodcastDBHelper.DATABASE_TABLE,
                    projection, selection, selectionArgs, null, null, sortOrder);
        } return null;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        if (isEpisodeUri(uri)) {
            return db.getWritableDatabase().update(PodcastDBHelper.DATABASE_TABLE, values, selection, selectionArgs);
        } return 0;
    }
}
