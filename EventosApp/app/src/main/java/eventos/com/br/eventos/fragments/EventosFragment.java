package eventos.com.br.eventos.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.io.File;
import java.util.List;

import eventos.com.br.eventos.R;
import eventos.com.br.eventos.activity.EventoActivity;
import eventos.com.br.eventos.adapter.EventoAdapter;
import eventos.com.br.eventos.model.Evento;
import eventos.com.br.eventos.rest.EventoRest;
import eventos.com.br.eventos.tasks.DownloadImagemTask;
import eventos.com.br.eventos.util.AndroidUtils;
import eventos.com.br.eventos.util.TipoBusca;

public class EventosFragment extends BaseFragment {
    protected RecyclerView recyclerView;
    private SwipeRefreshLayout swipeLayout;
    private List<Evento> eventos;
    private TipoBusca tipoDeBusca;
    private ProgressBar progress;
    private File imagemShare;
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
                    taskEventos();
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
            taskEventos();
        }

        super.onResume();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        taskEventos();
    }

    private void taskEventos() {
        // Busca os eventos: Dispara a Task
        //startTask("eventos", new GetEventosTask(pullToRefresh, getContext()), pullToRefresh ? R.id.swipeToRefresh : R.id.progress);
        new GetEventosTask().execute();
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
                recyclerView.setAdapter(new EventoAdapter(getContext(), eventos, onClickEvento(), onClickEditar(), onClickFavoritar()));
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

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
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
            progress.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

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
                new DownloadImagemTask(getAppCompatActivity(), onCallbackDownloadImagem()).execute(e.getEnderecoImagem());
            }
        };
    }

    public DownloadImagemTask.CallbackDownloadImagem onCallbackDownloadImagem() {
        return new DownloadImagemTask.CallbackDownloadImagem() {

            @Override
            public void onCallbackDownloadImagem(File imagem) {
                imagemShare = imagem;

                Uri uri = Uri.fromFile(imagem);
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                shareIntent.setType("image/*");
                getAppCompatActivity().startActivity(Intent.createChooser(shareIntent, "Compartilhar Evento"));
            }
        };
    }

    @Override
    public void onDetach() {
        if (imagemShare != null && imagemShare.exists()) {
            imagemShare.delete();
        }
        super.onDetach();
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
