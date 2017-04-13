package eventos.com.br.eventos.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import eventos.com.br.eventos.R;
import eventos.com.br.eventos.adapter.CidadesAdapter;
import eventos.com.br.eventos.adapter.EstadosAdapter;
import eventos.com.br.eventos.adapter.FaculdadesAdapter;
import eventos.com.br.eventos.dao.FiltroDAO;
import eventos.com.br.eventos.fragments.FilterEventosDialog;
import eventos.com.br.eventos.model.Cidade;
import eventos.com.br.eventos.model.Estado;
import eventos.com.br.eventos.model.Faculdade;
import eventos.com.br.eventos.model.Filtro;
import eventos.com.br.eventos.model.FiltroTipo;
import eventos.com.br.eventos.rest.EnderecoRest;
import eventos.com.br.eventos.rest.FaculdadeRest;
import eventos.com.br.eventos.util.ValidationUtil;
import livroandroid.lib.utils.AndroidUtils;

public class FiltroActivity extends BaseActivity {
    private Spinner spFaculdades, spEstados, spCidades;
    private Faculdade faculdadeSelecionada;
    private Estado estadoSelecionado;
    private Cidade cidadeSelecionada;
    private ProgressBar progress;
    private Filtro filtro;
    private EditText tDataInicio, tDataFinal;
    private boolean updateCampos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtro);
        setUpToolbar();
        setUpNavigation();

        findViewById(R.id.btnFiltrar).setOnClickListener(onClickFiltrar());
        findViewById(R.id.btnLimpar).setOnClickListener(onClickLimpar());

        spFaculdades = (Spinner) findViewById(R.id.spFaculdades);
        spEstados = (Spinner) findViewById(R.id.spEstados);
        spCidades = (Spinner) findViewById(R.id.spCidades);
        progress = (ProgressBar) findViewById(R.id.progress);
        tDataInicio = (EditText) findViewById(R.id.tDataInicio);
        tDataFinal = (EditText) findViewById(R.id.tDataFinal);

        try {
            FiltroDAO dao = new FiltroDAO();
            filtro = dao.getFiltro();

            if (filtro == null) {
                filtro = new Filtro();
            }
            if (filtro != null) {
                updateCampos = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            filtro = new Filtro();
        }

        setDateTimeField();
        buscar();
    }

    private View.OnClickListener onClickLimpar() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tDataInicio.setText("");
                tDataFinal.setText("");
                spFaculdades.setSelection(0);
                spEstados.setSelection(0);
                spCidades.setSelection(0);

                filtro.setDataFinal(null);
                filtro.setDataInicial(null);
                filtro.setIdFaculdade(Long.MAX_VALUE);
                filtro.setIdEstado(Long.MAX_VALUE);
                filtro.setIdCidade(Long.MAX_VALUE);
            }
        };
    }

    public void onFilter(Filtro filtro) {

        try {
            FiltroDAO dao = new FiltroDAO();
            dao.save(filtro);

            setResult(MainActivity.CODIGO_FILTRO);
            finish();
        } catch (SQLException e) {

        }
    }

    private View.OnClickListener onClickFiltrar() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onFilter(filtro);

                /*
                if (ValidationUtil.validaSpinnerEstadoFiltro(spEstados)) {
                    filtro.setFiltroTipo(FiltroTipo.TODOS);
                    onFilter(filtro);
                    createToast("Buscando todos os eventos");
                    return;
                }

                if (ValidationUtil.validaSpinnerEstado(spEstados) && !ValidationUtil.validaSpinnerCidade(spCidades)) {
                    createToast("Selecione uma cidade");
                    return;
                }

                if (ValidationUtil.validaSpinnerFaculdadeFiltro(spFaculdades)) {
                    filtro.setFiltroTipo(FiltroTipo.FACULDADE);
                    onFilter(filtro);
                    createToast("Buscando todos os eventos da faculdade " + faculdadeSelecionada.getNome());
                    return;
                }

                if (ValidationUtil.validaSpinnerEstado(spEstados) && ValidationUtil.validaSpinnerCidade(spCidades)) {
                    filtro.setFiltroTipo(FiltroTipo.CIDADE);
                    onFilter(filtro);
                    createToast("Buscando todos os eventos da cidade " + cidadeSelecionada.getNome());
                    return;
                }

                createToast("Selecione algum critério de filtro");
                */
            }
        };
    }

    // Spinners
    private void buscar() {
        if (AndroidUtils.isNetworkAvailable(getContext())) {
            new FiltroActivity.EstadosTask().execute();
        } else {
            alert("Alerta", "Você não esta conectado na internet");
        }
    }

    private void setDateTimeField() {
        Calendar dataInicial = Calendar.getInstance();
        Calendar dataFinal = Calendar.getInstance();

        DatePickerDialog dataInicioDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

                filtro.setDataInicial(new GregorianCalendar(year, monthOfYear, dayOfMonth, 0, 0, 0));
                tDataInicio.setText(dateFormatter.format(filtro.getDataInicial().getTime()));
            }

        }, dataInicial.get(Calendar.YEAR), dataInicial.get(Calendar.MONTH), dataInicial.get(Calendar.DAY_OF_MONTH));

        DatePickerDialog dataFimDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

                filtro.setDataFinal(new GregorianCalendar(year, monthOfYear, dayOfMonth, 23, 59, 59));
                tDataFinal.setText(dateFormatter.format(filtro.getDataFinal().getTime()));
            }

        }, dataFinal.get(Calendar.YEAR), dataFinal.get(Calendar.MONTH), dataFinal.get(Calendar.DAY_OF_MONTH));

        dataInicioDialog.getDatePicker().setMinDate(Calendar.getInstance().getTime().getTime());
        dataFimDialog.getDatePicker().setMinDate(Calendar.getInstance().getTime().getTime());

        tDataInicio.setOnFocusChangeListener(focusGeneric(dataInicioDialog));
        tDataInicio.setOnClickListener(clickGeneric(dataInicioDialog));

        tDataFinal.setOnFocusChangeListener(focusGeneric(dataFimDialog));
        tDataFinal.setOnClickListener(clickGeneric(dataFimDialog));

        if (updateCampos) {
            SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

            if (filtro.getDataInicial() != null) {
                tDataInicio.setText(dateFormatter.format(filtro.getDataInicial().getTime()));
            }

            if (filtro.getDataFinal() != null) {
                tDataFinal.setText(dateFormatter.format(filtro.getDataFinal().getTime()));
            }
        }
    }

    private View.OnClickListener clickGeneric(final AlertDialog dialog) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        };
    }

    private View.OnFocusChangeListener focusGeneric(final AlertDialog dialog) {
        return new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    dialog.show();
                }
            }
        };
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
            alert("Alerta", "Aconteceu algum erro ao tentar buscar as faculdades");
            faculdades = new ArrayList<>();
        }

        // Label Faculdades
        Faculdade f = new Faculdade();
        f.setNome("Todas as faculdades");
        f.setId(Long.MAX_VALUE);
        faculdades.add(0, f);

        BaseAdapter adapter = new FaculdadesAdapter(getContext(), faculdades);
        spFaculdades.setAdapter(adapter);
        spFaculdades.setSelection(0);

        final List<Faculdade> faculdadesAux = faculdades;

        spFaculdades.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                faculdadeSelecionada = faculdadesAux.get(i);

                Log.e("BUG", "Faculdades: "+faculdadeSelecionada.getId());

               /* if (faculdadeSelecionada.getId().equals(Long.MAX_VALUE)) {
                    return;
                }*/
                filtro.setIdFaculdade(faculdadeSelecionada.getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        if (updateCampos) {
            selecionaItemSpinnerFaculdade(FiltroActivity.this.spFaculdades, filtro.getIdFaculdade());
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
            alert("Alerta", "Aconteceu algum erro ao tentar buscar os estados");
            estados = new ArrayList<>();
        }

        // Label Estado
        final Estado estado = new Estado();
        estado.setNome("Todos os estados");
        estado.setId(Long.MAX_VALUE);
        estados.add(0, estado);

        BaseAdapter adapter = new EstadosAdapter(getContext(), estados);
        spEstados.setAdapter(adapter);
        spEstados.setSelection(0);

        final List<Estado> estadosAux = estados;

        spEstados.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                estadoSelecionado = estadosAux.get(i);
                filtro.setIdEstado(estadoSelecionado.getId());

                Log.e("BUG", "Estados: "+estadoSelecionado.getId());

                if (estadoSelecionado.getId().equals(Long.MAX_VALUE)) {
                    limparSpinnerCidades();
                    limparSpinnerFaculdades();
                    filtro.setIdFaculdade(Long.MAX_VALUE);
                    filtro.setIdCidade(Long.MAX_VALUE);
                    return;
                }
                new FiltroActivity.CidadesTask().execute(estadoSelecionado.getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // Spinner cidades
        limparSpinnerCidades();

        // Spinner Faculdades
        limparSpinnerFaculdades();

        if (!updateCampos) {
            filtro.setIdFaculdade(Long.MAX_VALUE);
            filtro.setIdCidade(Long.MAX_VALUE);
        }

        if (updateCampos) {
            selecionaItemSpinnerEstados(FiltroActivity.this.spEstados, filtro.getIdEstado());
        }
    }

    private void limparSpinnerFaculdades() {
        List<Faculdade> faculdades = new ArrayList<>();
        Faculdade f = new Faculdade();
        f.setNome("Todas as faculdades");
        f.setId(Long.MAX_VALUE);
        faculdades.add(0, f);

        BaseAdapter adapterFaculdades = new FaculdadesAdapter(getContext(), faculdades);
        spFaculdades.setAdapter(adapterFaculdades);
        spFaculdades.setSelection(0);
    }

    private void limparSpinnerCidades() {
        List<Cidade> cidades = new ArrayList<>();
        Cidade cidade = new Cidade();
        cidade.setNome("Todas as cidades");
        cidade.setId(Long.MAX_VALUE);
        cidades.add(0, cidade);

        BaseAdapter adapterCidades = new CidadesAdapter(getContext(), cidades);
        spCidades.setAdapter(adapterCidades);
        spCidades.setSelection(0);
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
            alert("Alerta", "Aconteceu algum erro ao tentar buscar as cidades");
            cidades = new ArrayList<>();
        }

        // Label Cidade
        Cidade cidade = new Cidade();
        cidade.setNome("Todas as cidades");
        cidade.setId(Long.MAX_VALUE);
        cidades.add(0, cidade);

        CidadesAdapter adapter = (CidadesAdapter) FiltroActivity.this.spCidades.getAdapter();
        adapter.updateSpinner(cidades);
        FiltroActivity.this.spCidades.setSelection(0);

        final List<Cidade> cidadesAux = cidades;

        spCidades.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                cidadeSelecionada = cidadesAux.get(i);
                filtro.setIdCidade(cidadeSelecionada.getId());

                Log.e("BUG", "Cidade: "+cidadeSelecionada.getId());

                if (cidadeSelecionada.getId().equals(Long.MAX_VALUE)) {
                    limparSpinnerFaculdades();
                    filtro.setIdFaculdade(Long.MAX_VALUE);
                    return;
                }
                new FiltroActivity.FaculdadesTask().execute(cidadeSelecionada.getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        if (updateCampos) {
            selecionaItemSpinnerCidades(FiltroActivity.this.spCidades, filtro.getIdCidade());
        }
    }

    public void createToast(String texto) {
        Toast.makeText(getContext(), texto, Toast.LENGTH_SHORT).show();
    }
    public static void selecionaItemSpinnerFaculdade(Spinner spnr, Long id) {
        BaseAdapter adapter = (BaseAdapter) spnr.getAdapter();

        for (int position = 0; position < adapter.getCount(); position++) {
            Faculdade faculdade = (Faculdade) adapter.getItem(position);

            if (faculdade.getId().equals(id)) {
                spnr.setSelection(position);
                return;
            }
        }
    }

    public static void selecionaItemSpinnerEstados(Spinner spnr, Long id) {
        BaseAdapter adapter = (BaseAdapter) spnr.getAdapter();

        for (int position = 0; position < adapter.getCount(); position++) {
            Estado estado = (Estado) adapter.getItem(position);

            if (estado.getId().equals(id)) {
                spnr.setSelection(position);
                return;
            }
        }
    }

    public static void selecionaItemSpinnerCidades(Spinner spnr, Long id) {
        BaseAdapter adapter = (BaseAdapter) spnr.getAdapter();

        for (int position = 0; position < adapter.getCount(); position++) {
            Cidade cidade = (Cidade) adapter.getItem(position);

            if (cidade.getId().equals(id)) {
                spnr.setSelection(position);
                return;
            }
        }
    }
}
