package br.ufpe.cin.if710.podcast.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import br.ufpe.cin.if710.podcast.R;
import br.ufpe.cin.if710.podcast.db.PodcastProviderContract;

public class EpisodeDetailActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_episode_detail);

        Bundle extras = this.getIntent().getExtras();
        ((TextView) findViewById(R.id.title)).setText(extras.getString(PodcastProviderContract.TITLE));
        ((TextView) findViewById(R.id.date)).setText(extras.getString(PodcastProviderContract.DATE));
        ((TextView) findViewById(R.id.desc)).setText(extras.getString(PodcastProviderContract.DESC));
        TextView downloadView = findViewById(R.id.download);
        downloadView.setText(String.format(getString(R.string.link_download), extras.getString(PodcastProviderContract.DOWNLOAD_LINK)));
        ((TextView) findViewById(R.id.uri)).setText(extras.getString(PodcastProviderContract.FILE_URI));
    }
}
