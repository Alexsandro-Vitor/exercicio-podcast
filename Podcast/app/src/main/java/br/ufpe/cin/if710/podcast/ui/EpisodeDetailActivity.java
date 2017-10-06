package br.ufpe.cin.if710.podcast.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import br.ufpe.cin.if710.podcast.R;

public class EpisodeDetailActivity extends Activity {

    public static final String TITLE = "title";
    public static final String DATE = "date";
    public static final String DESC = "desc";
    public static final String DOWNLOAD_LINK = "download";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_episode_detail);

        Bundle extras = this.getIntent().getExtras();
        ((TextView) findViewById(R.id.title)).setText(extras.getString(TITLE));
        ((TextView) findViewById(R.id.date)).setText(extras.getString(DATE));
        ((TextView) findViewById(R.id.desc)).setText(extras.getString(DESC));
        TextView downloadView = findViewById(R.id.download);
        downloadView.setText(String.format(getString(R.string.link_download), extras.getString(DOWNLOAD_LINK)));
    }
}
