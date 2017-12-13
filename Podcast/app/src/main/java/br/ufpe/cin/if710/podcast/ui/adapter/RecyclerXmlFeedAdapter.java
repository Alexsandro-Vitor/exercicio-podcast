package br.ufpe.cin.if710.podcast.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import br.ufpe.cin.if710.podcast.R;
import br.ufpe.cin.if710.podcast.db.PodcastProviderContract;
import br.ufpe.cin.if710.podcast.domain.ItemFeed;
import br.ufpe.cin.if710.podcast.services.DownloadService;
import br.ufpe.cin.if710.podcast.services.MediaService;
import br.ufpe.cin.if710.podcast.ui.EpisodeDetailActivity;

public class RecyclerXmlFeedAdapter extends RecyclerView.Adapter<RecyclerXmlFeedAdapter.ViewHolder> {

    private int linkResource;
    private Context context;
    private List<ItemFeed> lista;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView item_title;
        public TextView item_date;
        public Button button;

        public ViewHolder(final View view) {
            super(view);
            this.item_title = view.findViewById(R.id.item_title);
            this.item_date = view.findViewById(R.id.item_date);
            this.button = view.findViewById(R.id.item_action);
            view.setTag(this);
        }
    }

    public RecyclerXmlFeedAdapter(Context context, int resource, List<ItemFeed> objects) {
        this.context = context;
        linkResource = resource;
        lista = objects;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //View view = View.inflate(context, linkResource, null);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemlista, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ItemFeed item = lista.get(position);
        holder.item_title.setText(item.getTitle());
        addListener(item, holder.item_title);
        holder.item_date.setText(item.getPubDate());
        //Se o podcast já foi baixado, ele já pode ser ouvido
        switch (item.getEstado()) {
            case ItemFeed.NAO_BAIXOU:
                holder.button.setText(R.string.action_download);
                holder.button.setEnabled(true);
                addDownload(position, holder.button);
                break;
            case ItemFeed.BAIXANDO:
                holder.button.setText(R.string.action_downloading);
                holder.button.setEnabled(false);
                break;
            case ItemFeed.BAIXOU:
                holder.button.setText(R.string.action_listen);
                holder.button.setEnabled(true);
                addEscutar(item, holder.button);
        }
    }

    @Override
    public int getItemCount() {
        return (lista == null) ? 0 : lista.size();
    }

    public void baixou(int position) {
        lista.get(position).setEstado(ItemFeed.BAIXOU);
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


    /*static class ViewHolder {
        TextView item_title;
        TextView item_date;
        Button button;
    }*/

    /*public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(getContext(), linkResource, null);
            holder = new ViewHolder();
            holder.item_title = convertView.findViewById(R.id.item_title);
            holder.item_date = convertView.findViewById(R.id.item_date);
            holder.button = convertView.findViewById(R.id.item_action);
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
    }*/

    //Adiciona o listener para ir a tela de detalhes do podcast
    private void addListener(final ItemFeed item, TextView view) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EpisodeDetailActivity.class);
                intent.putExtra(PodcastProviderContract.TITLE, item.getTitle());
                intent.putExtra(PodcastProviderContract.DATE, item.getPubDate());
                intent.putExtra(PodcastProviderContract.DESC, item.getDescription());
                intent.putExtra(PodcastProviderContract.DOWNLOAD_LINK, item.getDownloadLink());
                intent.putExtra(PodcastProviderContract.FILE_URI, item.getUri());
                context.startActivity(intent);
            }
        });
    }

    //Adiciona um listener ao Button para baixar podcasts
    private void addDownload(final int position, final Button button) {
        button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                ItemFeed item = lista.get(position);
                Toast.makeText(context, "Baixando " + item.getTitle(), Toast.LENGTH_SHORT).show();
                item.setEstado(ItemFeed.BAIXANDO);
                notifyItemChanged(position);

                Intent intent = new Intent(context.getApplicationContext(), DownloadService.class);
                intent.setData(Uri.parse(item.getDownloadLink()));
                Log.d("addDownload", item.getDownloadLink());
                intent.putExtra("position", position);
                context.startService(intent);
            }
        });
    }

    //Adiciona um listener ao Button para tocar podcasts
    private void addEscutar(final ItemFeed item, final Button button) {
        button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Tocando " + item.getTitle(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context.getApplicationContext(), MediaService.class);
                intent.setData(Uri.parse(item.getUri()));
                context.startService(intent);
            }
        });
    }
}