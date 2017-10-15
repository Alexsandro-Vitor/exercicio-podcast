package br.ufpe.cin.if710.podcast.ui.adapter;

import java.io.IOException;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import br.ufpe.cin.if710.podcast.R;
import br.ufpe.cin.if710.podcast.db.PodcastProviderContract;
import br.ufpe.cin.if710.podcast.domain.ItemFeed;
import br.ufpe.cin.if710.podcast.services.DownloadService;
import br.ufpe.cin.if710.podcast.services.MediaService;
import br.ufpe.cin.if710.podcast.ui.EpisodeDetailActivity;

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
        ItemFeed item = getItem(position);
        holder.item_title.setText(item.getTitle());
        addListener(getItem(position), holder.item_title);
        holder.item_date.setText(item.getPubDate());
        addDownload(position, holder.button);
        //Se o podcast já foi baixado, ele já pode ser ouvido
        if (!item.getUri().isEmpty()) holder.button.setText(R.string.action_listen);
        return convertView;
    }

    //Adiciona o listener para ir a tela de detalhes do podcast
    private void addListener(final ItemFeed item, TextView view) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), EpisodeDetailActivity.class);
                intent.putExtra(PodcastProviderContract.TITLE, item.getTitle());
                intent.putExtra(PodcastProviderContract.DATE, item.getPubDate());
                intent.putExtra(PodcastProviderContract.DESC, item.getDescription());
                intent.putExtra(PodcastProviderContract.DOWNLOAD_LINK, item.getDownloadLink());
                intent.putExtra(PodcastProviderContract.FILE_URI, item.getUri());
                getContext().startActivity(intent);
            }
        });
    }

    //Adiciona um listener ao Button. Se o texto for "Baixar", ele baixa o podcast, se for "Escutar", ele toca
    private void addDownload(final int position, final Button button) {
        button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                ItemFeed item = getItem(position);
                if (button.getText().equals(getContext().getString(R.string.action_download))) {
                    Toast.makeText(getContext(), "Baixando " + item.getTitle(), Toast.LENGTH_SHORT).show();
                    button.setEnabled(false);
                    Intent intent = new Intent(getContext().getApplicationContext(), DownloadService.class);
                    intent.setData(Uri.parse(item.getDownloadLink()));
                    Log.d("addDownload", item.getDownloadLink());
                    intent.putExtra("position", position);
                    getContext().startService(intent);
                } else {
                    Intent intent = new Intent(getContext().getApplicationContext(), MediaService.class);
                    intent.setData(Uri.parse(item.getUri()));
                    getContext().startService(intent);
                }
            }
        });
    }
}