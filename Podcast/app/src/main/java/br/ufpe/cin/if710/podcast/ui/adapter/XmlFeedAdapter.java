package br.ufpe.cin.if710.podcast.ui.adapter;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import android.content.AsyncQueryHandler;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import br.ufpe.cin.if710.podcast.R;
import br.ufpe.cin.if710.podcast.db.PodcastProvider;
import br.ufpe.cin.if710.podcast.db.PodcastProviderContract;
import br.ufpe.cin.if710.podcast.domain.ItemFeed;
import br.ufpe.cin.if710.podcast.ui.EpisodeDetailActivity;
import br.ufpe.cin.if710.podcast.ui.MainActivity;

public class XmlFeedAdapter extends ArrayAdapter<ItemFeed> {

    int linkResource;

    public XmlFeedAdapter(Context context, int resource, List<ItemFeed> objects) {
        super(context, resource, objects);
        linkResource = resource;
    }

    /**
     * public abstract View getView (int position, View convertView, ViewGroup parent)
     * <p>
     * Added in API level 1
     * Get a View that displays the data at the specified position in the data set. You can either create a View manually or inflate it from an XML layout file. When the View is inflated, the parent View (GridView, ListView...) will apply default layout parameters unless you use inflate(int, android.view.ViewGroup, boolean) to specify a root view and to prevent attachment to the root.
     * <p>
     * Parameters
     * position	The position of the item within the adapter's data set of the item whose view we want.
     * convertView	The old view to reuse, if possible. Note: You should check that this view is non-null and of an appropriate type before using. If it is not possible to convert this view to display the correct data, this method can create a new view. Heterogeneous lists can specify their number of view types, so that this View is always of the right type (see getViewTypeCount() and getItemViewType(int)).
     * parent	The parent that this view will eventually be attached to
     * Returns
     * A View corresponding to the data at the specified position.
     */


    static class ViewHolder {
        TextView item_title;
        TextView item_date;
        Button button;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(getContext(), linkResource, null);
            holder = new ViewHolder();
            holder.item_title = (TextView) convertView.findViewById(R.id.item_title);
            holder.item_date = (TextView) convertView.findViewById(R.id.item_date);
            holder.button = (Button) convertView.findViewById(R.id.item_action);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.item_title.setText(getItem(position).getTitle());
        addListener(getItem(position), holder.item_title);
        holder.item_date.setText(getItem(position).getPubDate());
        addDownload(getItem(position), holder.button);
        return convertView;
    }

    //Tarefa 5 concluída
    private void addListener(final ItemFeed item, TextView view) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), EpisodeDetailActivity.class);
                intent.putExtra(PodcastProviderContract.TITLE, item.getTitle());
                intent.putExtra(PodcastProviderContract.DATE, item.getPubDate());
                intent.putExtra(PodcastProviderContract.DESC, item.getDescription());
                intent.putExtra(PodcastProviderContract.DOWNLOAD_LINK, item.getDownloadLink());
                getContext().startActivity(intent);
            }
        });
    }

    private void addDownload(final ItemFeed item, final Button button) {
        button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (button.getText().equals(getContext().getResources().getString(R.string.action_listen))) {
                    Log.d("XmlFeedAdapter", "Ainda não toca");
                    String[] coluna = {PodcastProviderContract.FILE_URI};
                    //Esse cursor está com erro
                    Cursor cursor = getContext().getContentResolver().query(PodcastProviderContract.EPISODE_LIST_URI,
                            coluna, PodcastProviderContract.TITLE + " = \"" + item.getTitle() + "\"", null, null);
                    for (String col : cursor.getColumnNames()) {
                        Log.d("XmlFeedAdapter", cursor.getString(cursor.getColumnIndex(col)));
                    }

                } else {
                    new DownloadPodcast(getContext(), button).execute(item);
                }
            }
        });
    }

    private class DownloadPodcast extends AsyncTask<ItemFeed, Void, Boolean> {
        private Context context;
        private Button button;

        public DownloadPodcast(Context context, Button button) {
            this.context = context;
            this.button = button;
        }

        @Override
        protected void onPreExecute() {
            button.setText(R.string.action_downloading);
        }

        @Override
        protected Boolean doInBackground(ItemFeed... params) {
            ItemFeed item = params[0];
            try {
                URL url = new URL(item.getDownloadLink());
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                Log.d("XmlFeedAdapter", "Conectou");
                InputStream in = conn.getInputStream();
                FileOutputStream out = context.openFileOutput(item.getTitle()+".mp3", Context.MODE_PRIVATE);
                //Le e escreve no arquivo enquanto tiver bytes nele
                byte[] buffer = new byte[1024];
                for (int count; (count = in.read(buffer)) != -1; ) {
                    out.write(buffer, 0, count);
                }
                in.close();
                out.close();

                //Colocando URI no DB
                ContentValues cv = new ContentValues();
                cv.put(PodcastProviderContract.FILE_URI, item.getTitle()+".mp3");
                context.getContentResolver().update(PodcastProviderContract.EPISODE_LIST_URI, cv,
                        PodcastProviderContract.TITLE + " = \"" + item.getTitle() + "\"", null);
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean boo) {
            if (boo) button.setText(R.string.action_listen);
            else {
                button.setText(R.string.action_download);
                Toast.makeText(context, "Download falhou", Toast.LENGTH_SHORT).show();
            }
        }
    }
}