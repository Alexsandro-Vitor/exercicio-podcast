package br.ufpe.cin.if710.podcast.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import br.ufpe.cin.if710.podcast.notifications.DownloadCompleteNotification;

/**
 * Service que baixa o podcast e o armazena na memória externa
 */

public class DownloadService extends IntentService {
    private DownloadCompleteNotification receiver;
    public static final String DOWNLOAD_COMPLETE = "br.ufpe.cin.if710.services.action.DOWNLOAD_COMPLETE";

    public DownloadService() {
        super("DownloadService");
        Log.d("DownloadPodcast", "Construiu o service");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        receiver = new DownloadCompleteNotification();
        /*IntentFilter filter = new IntentFilter();
        filter.addAction(DOWNLOAD_COMPLETE);
        this.registerReceiver(receiver, filter);*/

        Log.d("DownloadService", "Broadcast registrado");
        IntentFilter filter = new IntentFilter(DownloadService.DOWNLOAD_COMPLETE);
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(receiver, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //this.unregisterReceiver(receiver);
        Log.d("DownloadService", "O Broadcast já fez seu trabalho");
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(receiver);
    }

    @Override
    public void onHandleIntent(Intent i) {
        Log.d("DownloadPodcast", "Começou o service");
        try {
            File root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            root.mkdirs();
            Uri uri = i.getData();
            File output = new File(root, uri.getLastPathSegment());
            Log.d("DownloadPodcast", output.getPath());
            Log.d("DownloadPodcast", uri.toString());
            if (output.exists()) output.delete();
            HttpURLConnection c = getHttpURLConnection(i);
            FileOutputStream fos = new FileOutputStream(output.getPath());
            BufferedOutputStream out = new BufferedOutputStream(fos);
            try {
                InputStream in = c.getInputStream();
                byte[] buffer = new byte[8192];
                int len;
                while ((len = in.read(buffer)) >= 0) {
                    out.write(buffer, 0, len);
                }
                out.flush();
            } finally {
                fos.getFD().sync();
                out.close();
                c.disconnect();
            }
            Intent intent = new Intent(DOWNLOAD_COMPLETE);
            intent.putExtra("position", i.getIntExtra("position", 0));
            intent.putExtra("uri", uri.toString());
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        } catch (Exception e) {
            Log.e("DownloadPodcast", e.getMessage());
        }
    }

    private HttpURLConnection getHttpURLConnection(Intent i) throws IOException {
        URL url = new URL(i.getData().toString());
        return (HttpURLConnection) url.openConnection();
    }
}