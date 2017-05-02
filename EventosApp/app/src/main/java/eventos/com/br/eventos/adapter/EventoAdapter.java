package eventos.com.br.eventos.adapter;

/**
 * Created by rlech on 9/26/2015.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import eventos.com.br.eventos.R;
import eventos.com.br.eventos.model.Evento;
import eventos.com.br.eventos.util.ImageUtils;

/**
 * Adapter para recycler view
 */
public class EventoAdapter extends RecyclerView.Adapter<EventoAdapter.EventoViewHolder> {

    private final List<Evento> eventos;
    private final Context context;
    private EventoOnClickListener eventoOnClickListener;
    private final CompartilharOnClickListener compartilharOnClickListener;

    public EventoAdapter(Context context, List<Evento> eventos, EventoOnClickListener
            eventoOnClickListener, CompartilharOnClickListener compartilharOnClickListener) {
        this.context = context;
        this.eventos = eventos;
        this.eventoOnClickListener = eventoOnClickListener;
        this.compartilharOnClickListener = compartilharOnClickListener;
    }

    @Override
    public EventoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_eventos, parent, false);
        EventoViewHolder viewHolder = new EventoViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final EventoViewHolder holder, final int position) {
        Evento evento = this.eventos.get(position);

        SimpleDateFormat formatData = new SimpleDateFormat("EEE',' dd 'de' MMMM 'às' HH:mm", Locale.getDefault());
        String data = formatData.format(evento.getDataHora().getTime()).toUpperCase();

        holder.txtData.setText(data);
        holder.txtNome.setText(evento.getNome());
        holder.txtLocal.setText("Local: " + evento.getLocal().getNome());

        ImageUtils.setImageFeed(context, evento.getEnderecoImagem(), holder.imgEvento, holder.imgEventoWrapper, holder.progress);

        if (eventoOnClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    eventoOnClickListener.onClickEvento(holder, position);
                }
            });
        }

        if (compartilharOnClickListener != null) {
            holder.icCompartilhar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    compartilharOnClickListener.onClickCompartilhar(holder, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return this.eventos != null ? this.eventos.size() : 0;
    }

    public interface EventoOnClickListener {
        void onClickEvento(EventoViewHolder holder, int idx);
    }

    public interface CompartilharOnClickListener {
        void onClickCompartilhar(EventoViewHolder holder, int idx);
    }

    public interface FavoritarOnClickListener {
        void onClickFavoritar(EventoViewHolder holder, int idx);
    }

    // ViewHolder com as views
    public static class EventoViewHolder extends RecyclerView.ViewHolder {
        TextView txtData;
        TextView txtNome;
        TextView txtLocal;
        ImageButton icCompartilhar;
        ImageView imgEvento;
        LinearLayout imgEventoWrapper;
        ProgressBar progress;

        EventoViewHolder(View view) {
            super(view);
            // Cria as views para salvar no ViewHolder
            txtNome = (TextView) view.findViewById(R.id.txtNome);
            txtData = (TextView) view.findViewById(R.id.txtData);
            txtLocal = (TextView) view.findViewById(R.id.txtLocal);
            icCompartilhar = (ImageButton) view.findViewById(R.id.icCompartilhar);
            imgEvento = (ImageView) view.findViewById(R.id.imgEvento);
            imgEventoWrapper = (LinearLayout) view.findViewById(R.id.imgEventoWrapper);
            progress = (ProgressBar) view.findViewById(R.id.progressImg);
        }
    }
}

