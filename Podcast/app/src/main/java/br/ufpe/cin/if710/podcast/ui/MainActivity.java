package br.ufpe.cin.if710.podcast.ui;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import br.ufpe.cin.if710.podcast.R;
import br.ufpe.cin.if710.podcast.db.PodcastProviderContract;
import br.ufpe.cin.if710.podcast.domain.ItemFeed;
import br.ufpe.cin.if710.podcast.domain.XmlFeedParser;
import br.ufpe.cin.if710.podcast.services.DownloadService;
import br.ufpe.cin.if710.podcast.ui.adapter.RecyclerXmlFeedAdapter;

public class MainActivity extends Activity {

    //ao fazer envio da resolucao, use este link no seu codigo!
    private final String RSS_FEED = "http://leopoldomt.com/if710/fronteirasdaciencia.xml";
    //TODO teste com outros links de podcast

    //private ListView items;
    private RecyclerView items;

    //private XmlFeedAdapter adapter;
    private RecyclerXmlFeedAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        items = findViewById(R.id.items);
        items.setHasFixedSize(true);
        items.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(this,SettingsActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (connected()) new DownloadXmlTask().execute(RSS_FEED);
        new RecoverXmlTask().execute();
    }

    //Checa se há conexão com a internet
    private boolean connected() {
        ConnectivityManager manager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        return (info != null) && info.isConnected();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //XmlFeedAdapter adapter = (XmlFeedAdapter) items.getAdapter();
        //adapter.clear();
    }

    //Baixa o Xml e insere informações no DB
    private class DownloadXmlTask extends AsyncTask<String, Void, List<ItemFeed>> {

        @Override
        protected List<ItemFeed> doInBackground(String... params) {
            List<ItemFeed> itemList = new ArrayList<>();
            try {
                if (mudou(params[0])) itemList = XmlFeedParser.parse(getRssFeed(params[0]));
                //getContentResolver().delete(PodcastProviderContract.EPISODE_LIST_URI, null, null);
            } catch (IOException e) {
                Log.e("DownloadXmlTask", e.getMessage());
            } catch (XmlPullParserException e) {
                Log.e("DownloadXmlTask", e.getMessage());
            }
            return itemList;
        }

        @Override
        protected void onPostExecute(List<ItemFeed> feed) {
            /*if (trocouLink) {
                getContentResolver().delete(PodcastProviderContract.EPISODE_LIST_URI, null, null);
                trocouLink = true;
            }*/

            int contagem = 0;
            for (ItemFeed item : feed) {
                if (aindaNaoBaixou(item)) {
                    ContentResolver resolver = getContentResolver();
                    ContentValues cv = item.toCV();
                    resolver.insert(PodcastProviderContract.EPISODE_LIST_URI, cv);
                    contagem++;
                }
            }
            Toast.makeText(getApplicationContext(), "Novos podcasts: "+ contagem, Toast.LENGTH_SHORT).show();
            Toast.makeText(getApplicationContext(), "Total: "+ feed.size(), Toast.LENGTH_SHORT).show();
        }

        //Checa se um podcast ainda não está no DB
        private boolean aindaNaoBaixou(ItemFeed item) {
            ContentResolver resolver = getContentResolver();
            String selection = PodcastProviderContract.DATE + " =?";
            String[] selectionArgs = {item.getPubDate()};
            Cursor cursor = resolver.query(PodcastProviderContract.EPISODE_LIST_URI,
                    PodcastProviderContract.ALL_COLUMNS, selection, selectionArgs, null);
            int qtd = (cursor == null) ? 0 : cursor.getCount();
            if (cursor != null) cursor.close();
            return qtd == 0;
        }
    }

    //Recupera a informação do banco de dados e cria as views
    private class RecoverXmlTask extends AsyncTask<String, Void, List<ItemFeed>> {
        @Override
        protected List<ItemFeed> doInBackground(String... params) {
            ContentResolver resolver = getContentResolver();
            Cursor cursor = resolver.query(PodcastProviderContract.EPISODE_LIST_URI, null, null, null, null);
            List<ItemFeed> itemList = new ArrayList<>();
            while (cursor.moveToNext()) {
                itemList.add(new ItemFeed(cursor));
            }
            cursor.close();
            return itemList;
        }

        @Override
        protected void onPostExecute(List<ItemFeed> feed) {
            //Adapter Personalizado
            //adapter = new XmlFeedAdapter(getApplicationContext(), R.layout.itemlista, feed);
            adapter = RecyclerXmlFeedAdapter.getInstance(getApplicationContext(), feed);
            //new RecyclerXmlFeedAdapter(getApplicationContext(), feed);

            //atualizar o list view
            items.setAdapter(adapter);
            //items.setTextFilterEnabled(true);

            Toast.makeText(getApplicationContext(), "Views montadas: " + feed.size(), Toast.LENGTH_SHORT).show();
        }
    }

    //TODO Opcional - pesquise outros meios de obter arquivos da internet
    private String getRssFeed(String feed) throws IOException {
        InputStream in = null;
        String rssFeed = "";
        try {
            URL url = new URL(feed);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            in = conn.getInputStream();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            for (int count; (count = in.read(buffer)) != -1; ) {
                out.write(buffer, 0, count);
            }
            byte[] response = out.toByteArray();
            rssFeed = new String(response, "UTF-8");
        } finally {
            if (in != null) {
                in.close();
            }
        }
        return rssFeed;
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(DownloadService.DOWNLOAD_COMPLETE);
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(onDownloadComplete, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(onDownloadComplete);
    }

    private BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int position = intent.getIntExtra("position", 0);
            RecyclerXmlFeedAdapter.ViewHolder holder = (RecyclerXmlFeedAdapter.ViewHolder)items.findViewHolderForAdapterPosition(position);
            Button button = holder.button;

            String pubDate = (String)holder.item_date.getText();
            ContentResolver resolver = getContentResolver();
            ContentValues cv = new ContentValues();
            String uri = intent.getStringExtra("uri");
            cv.put(PodcastProviderContract.FILE_URI, uri);

            String selection = PodcastProviderContract.DATE + " =?";
            String[] selectionArgs = {pubDate};
            int s = resolver.update(PodcastProviderContract.EPISODE_LIST_URI, cv, selection, selectionArgs);
            adapter.baixou(position);    //Atualizando informação no adapter
            //button.setEnabled(true);
            //button.setText(R.string.action_listen);
            adapter.notifyItemChanged(position);
        }
    };

    private boolean mudou(String feed) throws IOException {
        if (adapter == null) return true;
        else {
            URL url = new URL(feed);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("If-Modified-Since", adapter.ultimoUpdate);
            if (conn.getResponseCode() == 304) return false;
            else {
                RecyclerXmlFeedAdapter.getInstance(getApplicationContext(), null)
                        .ultimoUpdate = conn.getHeaderField("Last-Modified");
                return true;
            }
        }
    }
}