package eventos.com.br.eventos.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TimePicker;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import eventos.com.br.eventos.R;
import eventos.com.br.eventos.adapter.CidadesAdapter;
import eventos.com.br.eventos.adapter.EstadosAdapter;
import eventos.com.br.eventos.adapter.FaculdadesAdapter;
import eventos.com.br.eventos.config.EventosApplication;
import eventos.com.br.eventos.model.Cidade;
import eventos.com.br.eventos.model.Estado;
import eventos.com.br.eventos.model.Evento;
import eventos.com.br.eventos.model.Faculdade;
import eventos.com.br.eventos.model.Local;
import eventos.com.br.eventos.model.Response;
import eventos.com.br.eventos.model.ResponseWithURL;
import eventos.com.br.eventos.model.Usuario;
import eventos.com.br.eventos.rest.EnderecoRest;
import eventos.com.br.eventos.rest.EventoRest;
import eventos.com.br.eventos.rest.FaculdadeRest;
import eventos.com.br.eventos.util.CameraUtil;
import eventos.com.br.eventos.util.ValidationUtil;
import livroandroid.lib.utils.AlertUtils;
import livroandroid.lib.utils.AndroidUtils;

public class EventoPUActivity extends BaseActivity {
    private EditText txtNome, txtDesc, txtDataInicio, txtHoraInicio, txtLocalNome, txtLocalCep, txtLocalRua, txtLocalBairro, txtLocalNumero, tNomeAtletica;
    private List<EditText> editTexts;
    private ImageView imgView;
    private Evento evento;
    private Button btnSalvar;
    private Spinner spFaculdades, spEstados, spCidades;
    private Faculdade faculdadeSelecionada;
    private Estado estadoSelecionado;
    private Cidade cidadeSelecionada;
    private File fileImage;
    private CameraUtil cameraUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento_pu);
        setUpToolbar();
        setUpNavigation();

        cameraUtil = new CameraUtil(getActivity());
        evento = new Evento();

        initFields();

        setDateTimeField();

        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                this.evento = (Evento) bundle.getSerializable("Evento");
                if (evento != null) {
                    txtNome.setText(evento.getNome());
                }
            }
        }

        // Botão salvar
        btnSalvar.setOnClickListener(onClickSalvar());

        // Botão abrir a galeria
        ImageButton btAbrirGaleria = (ImageButton) findViewById(R.id.btnGaleria);
        btAbrirGaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraUtil.abrirGaleria(getActivity());
            }
        });

        focusEditText();

        buscar();
    }

    private void buscar() {
        if (AndroidUtils.isNetworkAvailable(getContext())) {
            new EstadosTask().execute();
        } else {
            alert("Alerta", "Você não esta conectado na internet");
        }
    }

    public void focusEditText() {

        final View viewNome = findViewById(R.id.viewNome);

        txtNome.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    viewNome.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.primary));
                } else {
                    viewNome.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.cinzaBBB));
                }
            }
        });
    }

    private void initFields() {
        txtNome = (EditText) findViewById(R.id.tNome);
        txtDesc = (EditText) findViewById(R.id.tDesc);
        txtDataInicio = (EditText) findViewById(R.id.tDataInicio);
        txtHoraInicio = (EditText) findViewById(R.id.tHoraInicio);
        txtLocalNome = (EditText) findViewById(R.id.localNome);
        txtLocalCep = (EditText) findViewById(R.id.localCep);
        txtLocalRua = (EditText) findViewById(R.id.localRua);
        txtLocalBairro = (EditText) findViewById(R.id.localBairro);
        txtLocalNumero = (EditText) findViewById(R.id.localNumero);
        tNomeAtletica = (EditText) findViewById(R.id.tNomeAtletica);
        imgView = (ImageView) findViewById(R.id.imgGaleria);
        spFaculdades = (Spinner) findViewById(R.id.spFaculdades);
        spEstados = (Spinner) findViewById(R.id.spLocalEstado);
        spCidades = (Spinner) findViewById(R.id.spLocalCidade);
        btnSalvar = (Button) findViewById(R.id.btSalvar);
        editTexts = Arrays.asList(txtNome, txtDesc, txtDataInicio, txtHoraInicio, txtLocalNome, txtLocalCep, txtLocalRua, txtLocalBairro, txtLocalNumero, tNomeAtletica);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        this.fileImage = cameraUtil.pegarImagem(requestCode, resultCode, data, imgView);
    }

    /**
     * Listener chamado quando usuário clicar no botão salvar, faz as devidas validacoes
     *
     * @return
     */
    private View.OnClickListener onClickSalvar() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean validaOk = validaCampos();

                Usuario usuario = EventosApplication.getInstance().getUsuario();
                if (usuario == null){
                    validaOk = false;
                }

                if (validaOk) {
                    // Validação de campos preenchidos
                    Local local = new Local();
                    local.setNome(txtLocalNome.getText().toString());
                    local.setCep(txtLocalCep.getText().toString());
                    local.setCidade(cidadeSelecionada);
                    local.setRua(txtLocalRua.getText().toString());
                    local.setBairro(txtLocalBairro.getText().toString());
                    local.setNumero(txtLocalNumero.getText().toString());

                    evento.setNome(txtNome.getText().toString());
                    evento.setDescricao(txtDesc.getText().toString());
                    evento.setNomeAtletica(tNomeAtletica.getText().toString());
                    evento.setLocal(local);
                    evento.setFaculdade(faculdadeSelecionada);
                    evento.setUsuario(usuario);

                    salvarTask(evento);
                }
            }
        };
    }

    private boolean validaCampos() {
        List<EditText> editTexts = Arrays.asList(txtNome, txtDesc, txtDataInicio, txtHoraInicio, txtLocalNome, txtLocalCep, txtLocalRua, txtLocalBairro, txtLocalNumero);
        boolean validaOk = ValidationUtil.validateNotNull(editTexts);
        if (validaOk) {
            validaOk = ValidationUtil.validateName(txtNome);
        }
        if (validaOk) {
            validaOk = ValidationUtil.validateDate(txtDataInicio);
        }
        if (validaOk) {
            validaOk = ValidationUtil.validateTime(txtHoraInicio);
        }
        if (validaOk) {
            validaOk = ValidationUtil.validaSpinnerFaculdade(spFaculdades);
        }
        return validaOk;
    }

    /**
     * Executa AsyncTask que salva/atualiza o evento
     *
     * @param evento
     */
    private void salvarTask(Evento evento) {
        new EventoTask().execute(evento);
    }

    /**
     * Classe que tem a função de salvar ou atualizar o evento no web service.
     */
    private class EventoTask extends AsyncTask<Evento, Void, Response> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(getContext(), "Cadastrando", "Realizando cadastro do evento aguarde...", false, false);
        }

        @Override
        protected Response doInBackground(Evento... eventos) {
            Evento evento = eventos[0];
            EventoRest service = new EventoRest(getApplicationContext());

            Response response;

            try {
                // Faz upload da foto
                if (fileImage != null && fileImage.exists()) {
                    ResponseWithURL responseWithURL = service.postFotoBase64(fileImage);
                    if (responseWithURL != null && responseWithURL.isOk()) {
                        // Atualiza a foto do carro
                        evento.setEnderecoImagem(responseWithURL.getUrl());
                    }
                }
                // Salva o evento
                response = service.save(evento);

            } catch (Exception e) {
                response = null;
            }
            return response;
        }

        @Override
        protected void onPostExecute(Response response) {
            progressDialog.dismiss();

            if (response != null && "OK".equals(response.getStatus())) {
                // Fecha a tela
                setResult(MainActivity.RECRIAR_ACTIVITY);
                finish();
            } else {
                AlertUtils.alert(getAppCompatActivity(), "Alerta", "Erro ao tentar salvar evento " + evento.getNome());
            }
        }
    }

    private void setDateTimeField() {

        Calendar newCalendar = Calendar.getInstance();
        evento.setDataHora(newCalendar);

        DatePickerDialog dataInicioDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

                evento.getDataHora().set(year, monthOfYear, dayOfMonth);
                txtDataInicio.setText(dateFormatter.format(evento.getDataHora().getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        TimePickerDialog horaInicioDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm", Locale.getDefault());

                evento.getDataHora().set(Calendar.HOUR_OF_DAY, selectedHour);
                evento.getDataHora().set(Calendar.MINUTE, selectedMinute);

                txtHoraInicio.setText(timeFormatter.format(evento.getDataHora().getTime()));
            }
        }, newCalendar.get(Calendar.HOUR_OF_DAY), newCalendar.get(Calendar.MINUTE), true);

        dataInicioDialog.getDatePicker().setMinDate(Calendar.getInstance().getTime().getTime());

        txtDataInicio.setOnFocusChangeListener(focusGeneric(dataInicioDialog));
        txtDataInicio.setOnClickListener(clickGeneric(dataInicioDialog));

        txtHoraInicio.setOnFocusChangeListener(focusGeneric(horaInicioDialog));
        txtHoraInicio.setOnClickListener(clickGeneric(horaInicioDialog));
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
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(getContext(), "Buscando faculdades", "Aguarde...", false, false);
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
            progressDialog.dismiss();
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
        f.setNome("Selecione uma faculdade");
        f.setId(Long.MAX_VALUE);
        faculdades.add(0, f);

        BaseAdapter adapter = new FaculdadesAdapter(getContext(), faculdades);
        EventoPUActivity.this.spFaculdades.setAdapter(adapter);
        EventoPUActivity.this.spFaculdades.setSelection(0);

        final List<Faculdade> faculdadesAux = faculdades;

        EventoPUActivity.this.spFaculdades.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                faculdadeSelecionada = faculdadesAux.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    private class EstadosTask extends AsyncTask<Void, Void, List<Estado>> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(getContext(), "Buscando estados", "Aguarde...", false, false);
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

            progressDialog.dismiss();

            configSpinnerEstados(estados);
        }
    }

    private void configSpinnerEstados(List<Estado> estados) {
        if (estados == null) {
            alert("Alerta", "Aconteceu algum erro ao tentar buscar os estados");
            estados = new ArrayList<>();
        }

        // Label Estado
        Estado estado = new Estado();
        estado.setNome("Estado");
        estado.setId(Long.MAX_VALUE);
        estados.add(0, estado);

        BaseAdapter adapter = new EstadosAdapter(getContext(), estados);
        EventoPUActivity.this.spEstados.setAdapter(adapter);
        EventoPUActivity.this.spEstados.setSelection(0);

        final List<Estado> estadosAux = estados;

        EventoPUActivity.this.spEstados.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                estadoSelecionado = estadosAux.get(i);

                if (estadoSelecionado.getId().equals(Long.MAX_VALUE)) {
                    return;
                }
                new CidadesTask().execute(estadoSelecionado.getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // Spinner cidades
        List<Cidade> cidades = new ArrayList<>();
        Cidade cidade = new Cidade();
        cidade.setNome("Cidade");
        cidade.setId(Long.MAX_VALUE);
        cidades.add(0, cidade);

        BaseAdapter adapterCidades = new CidadesAdapter(getContext(), cidades);
        EventoPUActivity.this.spCidades.setAdapter(adapterCidades);
        EventoPUActivity.this.spCidades.setSelection(0);

        // Spinner Faculdades
        List<Faculdade> faculdades = new ArrayList<>();
        Faculdade f = new Faculdade();
        f.setNome("Selecione uma faculdade");
        f.setId(Long.MAX_VALUE);
        faculdades.add(0, f);

        BaseAdapter adapterFaculdades = new FaculdadesAdapter(getContext(), faculdades);
        EventoPUActivity.this.spFaculdades.setAdapter(adapterFaculdades);
        EventoPUActivity.this.spFaculdades.setSelection(0);
    }


    private class CidadesTask extends AsyncTask<Long, Void, List<Cidade>> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(getContext(), "Buscando cidades", "Aguarde...", false, false);
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

            progressDialog.dismiss();
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
        cidade.setNome("Cidade");
        cidade.setId(Long.MAX_VALUE);
        cidades.add(0, cidade);

        CidadesAdapter adapter = (CidadesAdapter) EventoPUActivity.this.spCidades.getAdapter();
        adapter.updateSpinner(cidades);
        EventoPUActivity.this.spCidades.setSelection(0);

        final List<Cidade> cidadesAux = cidades;

        EventoPUActivity.this.spCidades.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                cidadeSelecionada = cidadesAux.get(i);

                if (cidadeSelecionada.getId().equals(Long.MAX_VALUE)) {
                    return;
                }

                new FaculdadesTask().execute(cidadeSelecionada.getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}
