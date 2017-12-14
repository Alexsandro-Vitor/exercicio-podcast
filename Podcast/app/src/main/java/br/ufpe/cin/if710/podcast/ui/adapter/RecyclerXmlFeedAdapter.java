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

    private Context context;
    private List<ItemFeed> lista;
    public String ultimoUpdate;

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

    private static RecyclerXmlFeedAdapter instance;

    private RecyclerXmlFeedAdapter(Context context, List<ItemFeed> objects) {
        this.context = context;
        lista = objects;
    }

    public static RecyclerXmlFeedAdapter getInstance(Context context, List<ItemFeed> objects) {
        if (instance == null) instance = new RecyclerXmlFeedAdapter(context, objects);
        else {
            instance.context = context;
            instance.lista = objects;
        }
        return instance;
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