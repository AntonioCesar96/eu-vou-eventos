package eventos.com.br.eventos.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import eventos.com.br.eventos.R;
import eventos.com.br.eventos.activity.EventoActivity;
import eventos.com.br.eventos.adapter.EventoAdapter;
import eventos.com.br.eventos.model.Evento;
import eventos.com.br.eventos.rest.EventoRest;
import eventos.com.br.eventos.tasks.CompartilharTask;
import eventos.com.br.eventos.util.TipoBusca;
import livroandroid.lib.utils.AndroidUtils;

public class EventosFragment extends BaseFragment {
    protected RecyclerView recyclerView;
    private SwipeRefreshLayout swipeLayout;
    private List<Evento> eventos;
    private TipoBusca tipoDeBusca;
    private ProgressBar progress;
    public static final int ATUALIZAR_FAVORITOS = 9;

    // Método para instanciar esse fragment pelo tipo.
    public static EventosFragment newInstance(TipoBusca tipo) {

        Bundle bundle = new Bundle();
        bundle.putSerializable("tipoDeBusca", tipo);

        EventosFragment f = new EventosFragment();
        f.setArguments(bundle);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // Lê o tipo dos argumentos.
            this.tipoDeBusca = (TipoBusca) getArguments().get("tipoDeBusca");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_eventos, container, false);

        progress = (ProgressBar) view.findViewById(R.id.progress);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);

        // Swipe to Refresh
        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeToRefresh);
        swipeLayout.setOnRefreshListener(OnRefreshListener());
        swipeLayout.setColorSchemeResources(
                R.color.refresh_progress_1,
                R.color.refresh_progress_2,
                R.color.refresh_progress_3);

        return view;
    }

    private SwipeRefreshLayout.OnRefreshListener OnRefreshListener() {
        return new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Valida se existe conexão ao fazer o gesto Pull to Refresh
                if (AndroidUtils.isNetworkAvailable(getContext())) {
                    // Atualiza ao fazer o gesto Pull to Refresh
                    taskEventos(true);
                    swipeLayout.setRefreshing(false);
                } else {
                    swipeLayout.setRefreshing(false);
                    snack(recyclerView, R.string.msg_error_conexao_indisponivel);
                }
            }
        };
    }

    @Override
    public void onResume() {
        if (TipoBusca.FAVORITOS.equals(tipoDeBusca)) {
            taskEventos(false);
        }

        super.onResume();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        taskEventos(false);
    }

    private void taskEventos(boolean pullToRefresh) {
        // Busca os eventos: Dispara a Task
        //startTask("eventos", new GetEventosTask(pullToRefresh, getContext()), pullToRefresh ? R.id.swipeToRefresh : R.id.progress);
        new GetEventosTask(pullToRefresh).execute();
    }

    /*
    // Task para buscar os eventos
    private class GetEventosTask implements TaskListener<List<Evento>> {
        private boolean refresh;
        private Context context;

        public GetEventosTask(boolean refresh, Context context) {
            this.refresh = refresh;
            this.context = context;
        }

        @Override
        public List<Evento> execute() throws Exception {
            EventoRest service = new EventoRest(this.context);
            return service.getEventos(tipoDeBusca);
        }

        @Override
        public void updateView(List<Evento> eventos) {
            if (eventos != null) {
                // Salva a lista de eventos no atributo da classe
                EventosFragment.this.eventos = eventos;
                // Atualiza a view na UI Thread
                recyclerView.setAdapter(new EventoAdapter(getContext(), eventos, onClickEvento(), onClickCompartilhar(), onClickFavoritar()));
            }
        }

        @Override
        public void onError(Exception e) {
            // Qualquer exceção lançada no método execute vai cair aqui.
            snack(recyclerView ,"Ocorreu algum erro ao buscar os dados.");
        }

        @Override
        public void onCancelled(String s) {
        }
    }
*/

    private class GetEventosTask extends AsyncTask<Void, Void, List<Evento>> {

        private boolean pullToRefresh;

        public GetEventosTask(boolean pullToRefresh) {
            this.pullToRefresh = pullToRefresh;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (!pullToRefresh) {
                progress.setVisibility(View.VISIBLE);
            }
        }

        @Override
        protected List<Evento> doInBackground(Void... longs) {
            EventoRest service = new EventoRest(getContext());

            try {
                return service.getEventos(tipoDeBusca);
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Evento> eventos) {
            if (!pullToRefresh) {
                progress.setVisibility(View.GONE);
            }

            if (eventos != null) {
                // Salva a lista de eventos no atributo da classe
                EventosFragment.this.eventos = eventos;
                // Atualiza a view na UI Thread
                recyclerView.setAdapter(new EventoAdapter(getContext(), eventos, onClickEvento(), onClickCompartilhar()));
                return;
            }

            snack(recyclerView, "Ocorreu algum erro ao buscar os dados.");
        }
    }

    private EventoAdapter.CompartilharOnClickListener onClickCompartilhar() {
        return new EventoAdapter.CompartilharOnClickListener() {
            @Override
            public void onClickCompartilhar(EventoAdapter.EventoViewHolder holder, int idx) {

                Evento e = eventos.get(idx);
                new CompartilharTask(getAppCompatActivity()).execute(e.getEnderecoImagem());
            }
        };
    }

    private EventoAdapter.EventoOnClickListener onClickEvento() {
        return new EventoAdapter.EventoOnClickListener() {
            @Override
            public void onClickEvento(EventoAdapter.EventoViewHolder holder, int idx) {
                Evento e = eventos.get(idx);

                Intent intent = new Intent(getContext(), EventoActivity.class);
                intent.putExtra("evento", e);

                startActivityForResult(intent, ATUALIZAR_FAVORITOS);
                //startActivity(intent);
            }
        };
    }
}
