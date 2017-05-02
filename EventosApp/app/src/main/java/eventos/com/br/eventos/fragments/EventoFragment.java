package eventos.com.br.eventos.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import eventos.com.br.eventos.R;
import eventos.com.br.eventos.config.EventosApplication;
import eventos.com.br.eventos.dao.DataBaseHelper;
import eventos.com.br.eventos.dao.EventoDAO;
import eventos.com.br.eventos.dao.NotificacaoDAO;
import eventos.com.br.eventos.model.Evento;
import eventos.com.br.eventos.model.Notificacao;
import eventos.com.br.eventos.rest.EventoRest;
import eventos.com.br.eventos.tasks.CompartilharTask;
import eventos.com.br.eventos.util.AlarmEventoUtil;

public class EventoFragment extends BaseFragment {

    private Evento evento;
    private ProgressBar progress;
    private TextView txtDesc;
    private TextView txtLocal;
    private TextView txtRuaMostra;
    private TextView txtNumeroMostra;
    private TextView txtLocalBairro;
    private TextView txtDataEvento;
    private FloatingActionButton fabFavorito;
    public boolean flagClickFab;
    private DataBaseHelper dataBaseHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_evento, container, false);

        dataBaseHelper = EventosApplication.getInstance().getDataBaseHelper();
        evento = (Evento) getArguments().getSerializable("evento");
        setHasOptionsMenu(true);

        initFields(view);
        configEventoFavoritado(evento);

        return view;
    }

    private void configEventoFavoritado(Evento evento) {
        try {
            EventoDAO eventoDAO = new EventoDAO(dataBaseHelper.getConnectionSource());
            Evento eventoBanco = eventoDAO.getById(evento.getId());

            if (eventoBanco != null) {
                flagClickFab = true;
                fabFavorito.setImageResource(R.drawable.ic_turned_in_not_padding);
            }
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }

    private void initFields(View view) {
        txtDesc = (TextView) view.findViewById(R.id.txtDesc);
        progress = (ProgressBar) view.findViewById(R.id.progress);
        txtLocal = (TextView) view.findViewById(R.id.txtLocal);
        txtDataEvento = (TextView) view.findViewById(R.id.txtDataEvento);
        txtRuaMostra = (TextView) view.findViewById(R.id.txtRuaMostra);
        txtNumeroMostra = (TextView) view.findViewById(R.id.txtNumeroMostra);
        txtLocalBairro = (TextView) view.findViewById(R.id.txtLocalBairro);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        buscaEvento();
    }

    private void buscaEvento() {
        new EventoTask().execute(evento.getId());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_evento, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.ic_share) {
            compartilharEvento();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void compartilharEvento() {
        new CompartilharTask(getAppCompatActivity()).execute(evento.getEnderecoImagem());
    }

    public void setFabButton(FloatingActionButton fabFavorito) {
        this.fabFavorito = fabFavorito;
        this.fabFavorito.setOnClickListener(clickFabFavoritoSemBuscarEventoDoWebService());
    }

    private View.OnClickListener clickFabFavoritoSemBuscarEventoDoWebService() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Não é possível favoritar enquanto o evento não for totalmente carregado",
                        Toast.LENGTH_SHORT).show();
            }
        };
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
            EventoRest service = new EventoRest(getContext());

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
        this.evento = evento;

        SimpleDateFormat formatData = new SimpleDateFormat("EEE',' dd 'de' MMMM 'às' HH:mm", Locale.getDefault());
        String data = formatData.format(evento.getDataHora().getTime()).toUpperCase();

        txtDataEvento.setText(data);
        txtDesc.setText(evento.getDescricao());
        txtLocal.setText(evento.getLocal().getNome());
        txtRuaMostra.setText(evento.getLocal().getRua());
        txtNumeroMostra.setText(evento.getLocal().getNumero());
        txtLocalBairro.setText("Bairro: " + evento.getLocal().getBairro());

        fabFavorito.setOnClickListener(clickFabFavorito());
    }

    private View.OnClickListener clickFabFavorito() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (flagClickFab) {
                    fabFavorito.setImageResource(R.drawable.ic_turned_in_not_not_padding);
                    flagClickFab = false;
                    desFavoritar();
                    return;
                }

                fabFavorito.setImageResource(R.drawable.ic_turned_in_not_padding);
                flagClickFab = true;
                favoritar();
            }
        };
    }

    private void favoritar() {
        try {
            EventoDAO eventoDAO = new EventoDAO(dataBaseHelper.getConnectionSource());
            eventoDAO.save(evento);

            agendarAlarm(evento.getId(), evento.getDataHora());

            Toast.makeText(getContext(), evento.getNome() + " adicionado aos favoritos", Toast.LENGTH_SHORT).show();
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }

    private void desFavoritar() {
        try {
            EventoDAO eventoDAO = new EventoDAO(dataBaseHelper.getConnectionSource());
            eventoDAO.remove(evento);

            cancelarAlarm(evento.getId());

            Toast.makeText(getContext(), evento.getNome() + " removido dos favoritos", Toast.LENGTH_SHORT).show();
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }

    public void agendarAlarm(Long idEvento, Calendar data) {

        long diaNotificacao = pegarMilisegundosDiaDaNotificacao(data);

        try {
            Notificacao n = criarNotificacao(idEvento, diaNotificacao);
            new NotificacaoDAO(dataBaseHelper.getConnectionSource()).save(n);

            AlarmEventoUtil alarmEventoUtil = new AlarmEventoUtil(getContext(), dataBaseHelper);
            alarmEventoUtil.agendarAlarm(idEvento, diaNotificacao);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void cancelarAlarm(Long idEvento) {
        AlarmEventoUtil alarmEventoUtil = new AlarmEventoUtil(getContext(), dataBaseHelper);
        alarmEventoUtil.cancelarAlarm(idEvento);
    }

    private Notificacao criarNotificacao(Long idEvento, long dia) {
        Notificacao n = new Notificacao();
        n.setDataEvento(dia);
        n.setIdEvento(idEvento);
        n.setPrimeiraNotificacao(true);
        return n;
    }

    private long pegarMilisegundosDiaDaNotificacao(Calendar data) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        c.add(Calendar.SECOND, 10);
        long time = c.getTimeInMillis();

        return time;
    }
}

