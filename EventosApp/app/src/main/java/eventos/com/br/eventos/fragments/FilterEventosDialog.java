package eventos.com.br.eventos.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import eventos.com.br.eventos.R;
import eventos.com.br.eventos.activity.BaseActivity;
import eventos.com.br.eventos.adapter.CidadesAdapter;
import eventos.com.br.eventos.adapter.EstadosAdapter;
import eventos.com.br.eventos.adapter.FaculdadesAdapter;
import eventos.com.br.eventos.dao.FiltroDAO;
import eventos.com.br.eventos.model.Cidade;
import eventos.com.br.eventos.model.Estado;
import eventos.com.br.eventos.model.Faculdade;
import eventos.com.br.eventos.model.Filtro;
import eventos.com.br.eventos.model.FiltroTipo;
import eventos.com.br.eventos.rest.EnderecoRest;
import eventos.com.br.eventos.rest.FaculdadeRest;
import eventos.com.br.eventos.util.ValidationUtil;
import livroandroid.lib.utils.AndroidUtils;

public class FilterEventosDialog extends DialogFragment {
    private Spinner spFaculdades, spEstados, spCidades;
    private Faculdade faculdadeSelecionada;
    private Estado estadoSelecionado;
    private Cidade cidadeSelecionada;
    private BaseActivity activity;
    private Callback callback;
    private ProgressBar progress;
    private Filtro filtro;

    // Método utilitário para criar o dialog
    public static void show(FragmentManager fm, AppCompatActivity activity, Callback callback) {
        FragmentTransaction ft = fm.beginTransaction();
        Fragment prev = fm.findFragmentByTag("filter_eventos");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        FilterEventosDialog frag = new FilterEventosDialog();
        frag.callback = callback;
        frag.activity = (BaseActivity) activity;

        try {
            FiltroDAO dao = new FiltroDAO();
            frag.filtro = dao.getFiltro();

            if (frag.filtro == null) {
                frag.filtro = new Filtro();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            frag.filtro = new Filtro();
        }

        frag.show(ft, "filter_eventos");
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() == null) {
            return;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_filter, container, false);

        view.findViewById(R.id.btnFiltrar).setOnClickListener(onClickFiltrar());

        spFaculdades = (Spinner) view.findViewById(R.id.spFaculdades);
        spEstados = (Spinner) view.findViewById(R.id.spEstados);
        spCidades = (Spinner) view.findViewById(R.id.spCidades);
        progress = (ProgressBar) view.findViewById(R.id.progress);

        buscar();

        return view;
    }

    private View.OnClickListener onClickFiltrar() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (callback != null) {

                    if (!ValidationUtil.validaSpinnerEstado(spEstados)) {
                        filtro.setFiltroTipo(FiltroTipo.TODOS);
                        callback.onFilter(filtro);
                        createToast("Buscando todos os eventos");
                        dismiss();
                        return;
                    }

                    if (ValidationUtil.validaSpinnerEstado(spEstados) && !ValidationUtil.validaSpinnerCidade(spCidades)) {
                        createToast("Selecione uma cidade");
                        return;
                    }

                    if (ValidationUtil.validaSpinnerFaculdade(spFaculdades)) {
                        filtro.setFiltroTipo(FiltroTipo.FACULDADE);
                        callback.onFilter(filtro);
                        createToast("Buscando todos os eventos da faculdade " + faculdadeSelecionada.getNome());
                        dismiss();
                        return;
                    }

                    if (ValidationUtil.validaSpinnerEstado(spEstados) && ValidationUtil.validaSpinnerCidade(spCidades)) {
                        filtro.setFiltroTipo(FiltroTipo.CIDADE);
                        callback.onFilter(filtro);
                        createToast("Buscando todos os eventos da cidade " + cidadeSelecionada.getNome());
                        dismiss();
                        return;
                    }
                }

                createToast("Selecione algum critério de filtro");
            }
        };
    }

    // Interface para retornar o resultado
    public interface Callback {
        void onFilter(Filtro filtro);
    }

    // Spinners
    private void buscar() {
        if (AndroidUtils.isNetworkAvailable(getContext())) {
            new FilterEventosDialog.EstadosTask().execute();
        } else {
            activity.alert("Alerta", "Você não esta conectado na internet");
        }
    }

    private class FaculdadesTask extends AsyncTask<Long, Void, List<Faculdade>> {

        @Override
        protected void onPreExecute() {
            progress.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<Faculdade> doInBackground(Long... longs) {
            Long idCidade = longs[0];

            FaculdadeRest service = new FaculdadeRest(getContext());

            try {
                return service.getFaculdades(idCidade);
            } catch (Exception e) {
                Log.e("ERRO", e.getMessage(), e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Faculdade> faculdades) {
            progress.setVisibility(View.GONE);
            configSpinnerFaculdades(faculdades);
        }
    }

    private void configSpinnerFaculdades(List<Faculdade> faculdades) {
        if (faculdades == null) {
            activity.alert("Alerta", "Aconteceu algum erro ao tentar buscar as faculdades");
            faculdades = new ArrayList<>();
        }

        // Label Faculdades
        Faculdade f = new Faculdade();
        f.setNome("Todas as faculdades");
        f.setId(Long.MAX_VALUE);
        faculdades.add(0, f);

        BaseAdapter adapter = new FaculdadesAdapter(getContext(), faculdades);
        FilterEventosDialog.this.spFaculdades.setAdapter(adapter);
        FilterEventosDialog.this.spFaculdades.setSelection(0);

        final List<Faculdade> faculdadesAux = faculdades;

        FilterEventosDialog.this.spFaculdades.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                faculdadeSelecionada = faculdadesAux.get(i);

                if (faculdadeSelecionada.getId().equals(Long.MAX_VALUE)) {
                    return;
                }
                filtro.setIdFaculdade(faculdadeSelecionada.getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //selecionaItemSpinner(FilterEventosDialog.this.spFaculdades, filtro.getIdFaculdade());
    }

    public static void selecionaItemSpinner(Spinner spnr, Long id) {
        BaseAdapter adapter = (BaseAdapter) spnr.getAdapter();

        for (int position = 0; position < adapter.getCount(); position++) {
            Faculdade faculdade = (Faculdade) adapter.getItem(position);

            if (faculdade.getId().equals(id)) {
                spnr.setSelection(position);
                return;
            }
        }
    }

    private class EstadosTask extends AsyncTask<Void, Void, List<Estado>> {

        @Override
        protected void onPreExecute() {
            progress.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<Estado> doInBackground(Void... voids) {
            EnderecoRest service = new EnderecoRest(getContext());

            try {
                return service.getEstados();
            } catch (Exception e) {
                Log.e("ERRO", e.getMessage(), e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Estado> estados) {
            progress.setVisibility(View.GONE);
            configSpinnerEstados(estados);
        }
    }

    private void configSpinnerEstados(List<Estado> estados) {
        if (estados == null) {
            activity.alert("Alerta", "Aconteceu algum erro ao tentar buscar os estados");
            estados = new ArrayList<>();
        }

        // Label Estado
        Estado estado = new Estado();
        estado.setNome("Todos os estado");
        estado.setId(Long.MAX_VALUE);
        estados.add(0, estado);

        BaseAdapter adapter = new EstadosAdapter(getContext(), estados);
        FilterEventosDialog.this.spEstados.setAdapter(adapter);
        FilterEventosDialog.this.spEstados.setSelection(0);

        final List<Estado> estadosAux = estados;

        FilterEventosDialog.this.spEstados.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                estadoSelecionado = estadosAux.get(i);

                if (estadoSelecionado.getId().equals(Long.MAX_VALUE)) {
                    return;
                }
                new FilterEventosDialog.CidadesTask().execute(estadoSelecionado.getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // Spinner cidades
        List<Cidade> cidades = new ArrayList<>();
        Cidade cidade = new Cidade();
        cidade.setNome("Todas as cidades");
        cidade.setId(Long.MAX_VALUE);
        cidades.add(0, cidade);
        filtro.setIdCidade(cidade.getId());

        BaseAdapter adapterCidades = new CidadesAdapter(getContext(), cidades);
        FilterEventosDialog.this.spCidades.setAdapter(adapterCidades);
        FilterEventosDialog.this.spCidades.setSelection(0);

        // Spinner Faculdades
        List<Faculdade> faculdades = new ArrayList<>();
        Faculdade f = new Faculdade();
        f.setNome("Todas as faculdades");
        f.setId(Long.MAX_VALUE);
        faculdades.add(0, f);
        filtro.setIdFaculdade(f.getId());

        BaseAdapter adapterFaculdades = new FaculdadesAdapter(getContext(), faculdades);
        FilterEventosDialog.this.spFaculdades.setAdapter(adapterFaculdades);
        FilterEventosDialog.this.spFaculdades.setSelection(0);
    }


    private class CidadesTask extends AsyncTask<Long, Void, List<Cidade>> {

        @Override
        protected void onPreExecute() {
            progress.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<Cidade> doInBackground(Long... ids) {
            Long idEstado = ids[0];
            EnderecoRest service = new EnderecoRest(getContext());

            try {
                return service.getCidades(idEstado);
            } catch (Exception e) {
                Log.e("ERRO", e.getMessage(), e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Cidade> cidades) {
            progress.setVisibility(View.GONE);
            configSpinnerCidades(cidades);
        }
    }

    private void configSpinnerCidades(List<Cidade> cidades) {
        if (cidades == null) {
            activity.alert("Alerta", "Aconteceu algum erro ao tentar buscar as cidades");
            cidades = new ArrayList<>();
        }

        // Label Cidade
        Cidade cidade = new Cidade();
        cidade.setNome("Todas as cidades");
        cidade.setId(Long.MAX_VALUE);
        cidades.add(0, cidade);

        CidadesAdapter adapter = (CidadesAdapter) FilterEventosDialog.this.spCidades.getAdapter();
        adapter.updateSpinner(cidades);
        FilterEventosDialog.this.spCidades.setSelection(0);

        final List<Cidade> cidadesAux = cidades;

        FilterEventosDialog.this.spCidades.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                cidadeSelecionada = cidadesAux.get(i);

                if (cidadeSelecionada.getId().equals(Long.MAX_VALUE)) {
                    return;
                }
                filtro.setIdCidade(cidadeSelecionada.getId());
                new FilterEventosDialog.FaculdadesTask().execute(cidadeSelecionada.getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void createToast(String texto) {
        Toast.makeText(getContext(), texto, Toast.LENGTH_SHORT).show();
    }
}
