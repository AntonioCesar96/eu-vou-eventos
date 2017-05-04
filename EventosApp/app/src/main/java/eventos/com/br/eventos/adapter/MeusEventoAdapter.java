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
public class MeusEventoAdapter extends RecyclerView.Adapter<MeusEventoAdapter.EventoViewHolder> {

    private final List<Evento> eventos;
    private final Context context;
    private EventoOnClickListener eventoOnClickListener;
    private final EditarOnClickListener editarOnClickListener;

    public MeusEventoAdapter(Context context, List<Evento> eventos, EventoOnClickListener
            eventoOnClickListener, EditarOnClickListener editarOnClickListener) {
        this.context = context;
        this.eventos = eventos;
        this.eventoOnClickListener = eventoOnClickListener;
        this.editarOnClickListener = editarOnClickListener;
    }

    @Override
    public EventoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_meus_eventos, parent, false);
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

        if (eventoOnClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    eventoOnClickListener.onClickEvento(holder, position);
                }
            });
        }

        if (editarOnClickListener != null) {
            holder.icEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    editarOnClickListener.onClickEditar(holder.icEdit, position);
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

    public interface EditarOnClickListener {
        void onClickEditar(View view, int idx);
    }

    // ViewHolder com as views
    public static class EventoViewHolder extends RecyclerView.ViewHolder {
        TextView txtData;
        TextView txtNome;
        TextView txtLocal;
        ImageButton icEdit;
        LinearLayout popupMenuWrapper;

        EventoViewHolder(View view) {
            super(view);
            // Cria as views para salvar no ViewHolder
            txtNome = (TextView) view.findViewById(R.id.txtNome);
            txtData = (TextView) view.findViewById(R.id.txtData);
            txtLocal = (TextView) view.findViewById(R.id.txtLocal);
            icEdit = (ImageButton) view.findViewById(R.id.icEdit);
            popupMenuWrapper = (LinearLayout) view.findViewById(R.id.popupMenuWrapper);
        }
    }
}

