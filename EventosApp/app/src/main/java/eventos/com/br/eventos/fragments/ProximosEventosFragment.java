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
import java.util.ArrayList;
import java.util.List;

import eventos.com.br.eventos.R;
import eventos.com.br.eventos.activity.EventoActivity;
import eventos.com.br.eventos.adapter.EventoAdapter;
import eventos.com.br.eventos.model.Evento;
import eventos.com.br.eventos.rest.EventoRest;
import eventos.com.br.eventos.tasks.DownloadImagemTask;
import eventos.com.br.eventos.util.AndroidUtils;

public class ProximosEventosFragment extends BaseFragment {
    protected RecyclerView recyclerView;
    private SwipeRefreshLayout swipeLayout;
    private List<Evento> eventos;
    private ProgressBar progress, progressPag;
    private int pagina = 1;
    private int maxResultados = 5;
    private boolean carregando = false;
    private boolean carregarMais = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_eventos, container, false);

        eventos = new ArrayList<>();

        progress = (ProgressBar) view.findViewById(R.id.progress);
        progressPag = (ProgressBar) view.findViewById(R.id.progressPag);
        progressPag.setVisibility(View.GONE);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);

        // Swipe to Refresh
        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeToRefresh);
        swipeLayout.setOnRefreshListener(OnRefreshListener());
        swipeLayout.setColorSchemeResources(
                R.color.refresh_progress_1,
                R.color.refresh_progress_2,
                R.color.refresh_progress_3);

        recyclerView.addOnScrollListener(onScrollChangeListener(mLayoutManager));

        return view;
    }

    private RecyclerView.OnScrollListener onScrollChangeListener(final LinearLayoutManager mLayoutManager) {
        return new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0 && carregarMais) //check for scroll down
                {
                    int totalItemCount = mLayoutManager.getItemCount();
                    int lastVisiblesItems = mLayoutManager.findLastVisibleItemPosition();

                    if (totalItemCount > 0) {
                        totalItemCount -= 1;
                    }

                    if (!carregando && lastVisiblesItems == totalItemCount) {
                        pagina++;
                        taskEventos();
                    }
                }
            }
        };
    }


    private SwipeRefreshLayout.OnRefreshListener OnRefreshListener() {
        return new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Valida se existe conexão ao fazer o gesto Pull to Refresh
                if (AndroidUtils.isNetworkAvailable(getContext())) {
                    // Atualiza ao fazer o gesto Pull to Refresh
                    pagina = 1;
                    carregarMais = true;
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        taskEventos();
    }

    private void taskEventos() {
        new GetEventosTask().execute();
    }

    private class GetEventosTask extends AsyncTask<Void, Void, List<Evento>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            carregando = true;

            if (pagina == 1) {
                progress.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.INVISIBLE);
                return;
            }

            progressPag.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<Evento> doInBackground(Void... longs) {
            EventoRest service = new EventoRest(getContext());

            try {
                return service.getEventosProximos(pagina, maxResultados);
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Evento> eventos) {
            carregando = false;

            if (eventos != null) {
                if (pagina == 1) {
                    progress.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);

                    // Salva a lista de eventos no atributo da classe
                    ProximosEventosFragment.this.eventos = eventos;
                    // Atualiza a view na UI Thread
                    recyclerView.setAdapter(new EventoAdapter(getContext(), ProximosEventosFragment.this.eventos, onClickEvento(), onClickCompartilhar()));
                    return;
                }

                progressPag.setVisibility(View.GONE);

                if (eventos.size() != maxResultados) {
                    carregarMais = false;
                }

                EventoAdapter adapter = (EventoAdapter) recyclerView.getAdapter();
                adapter.updateList(eventos);

                return;
            }

            progress.setVisibility(View.GONE);
            progressPag.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
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

                Uri uri = Uri.fromFile(imagem);
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                shareIntent.setType("image/*");
                getAppCompatActivity().startActivity(Intent.createChooser(shareIntent, "Compartilhar Evento"));
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

                startActivity(intent);
            }
        };
    }
}
