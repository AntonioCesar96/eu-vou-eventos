package eventos.com.br.eventos.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;

import eventos.com.br.eventos.R;
import eventos.com.br.eventos.model.Evento;
import eventos.com.br.eventos.services.EventoService;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventoFragment extends BaseFragment {

    private Evento evento;
    private ProgressBar progress;
    private TextView txtDesc;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_evento, container, false);

        evento = (Evento) getArguments().getSerializable("evento");
        setHasOptionsMenu(true);

        initFields(view);

        return view;
    }

    private void initFields(View view) {
        txtDesc = (TextView) view.findViewById(R.id.txtDesc);
        progress = (ProgressBar) view.findViewById(R.id.progress);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        buscaEvento();
    }

    private void buscaEvento() {
        new EventoTask().execute(evento.getId());
    }

    private class EventoTask extends AsyncTask<Long, Void, Evento> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress.setVisibility(View.VISIBLE);
        }

        @Override
        protected Evento doInBackground(Long... longs) {
            Long idEvento = longs[0];

            EventoService service = new EventoService(getContext());

            try {
                return service.getEvento(idEvento);
            } catch (IOException e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(Evento evento) {
            progress.setVisibility(View.GONE);
            if (evento != null) {
                preencherEvento(evento);
            }
        }
    }

    private void preencherEvento(Evento evento) {
        txtDesc.setText(evento.getDescricao());
    }
}

