package eventos.com.br.eventos.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import eventos.com.br.eventos.R;
import eventos.com.br.eventos.adapter.FaculdadesAdapter;
import eventos.com.br.eventos.config.EventosApplication;
import eventos.com.br.eventos.dao.DataBaseHelper;
import eventos.com.br.eventos.model.Faculdade;
import eventos.com.br.eventos.model.ResponseWithURL;
import eventos.com.br.eventos.model.Usuario;
import eventos.com.br.eventos.dao.UsuarioDAO;
import eventos.com.br.eventos.rest.EventoRest;
import eventos.com.br.eventos.rest.FaculdadeRest;
import eventos.com.br.eventos.rest.UsuarioRest;
import eventos.com.br.eventos.util.CameraUtil;
import eventos.com.br.eventos.util.ImageUtils;
import eventos.com.br.eventos.util.ValidationUtil;
import livroandroid.lib.utils.AndroidUtils;

public class AlterarDadosUsuarioActivity extends BaseActivity {

    private Button btnCadastrar;
    private EditText txtNome, txtEmail, txtSenha;
    private ImageView imgView;
    private Spinner spFaculdades;
    private CheckBox cbOrganizador;
    private ProgressDialog progressDialog;
    private Faculdade faculdadeSelecionada;
    private CameraUtil cameraUtil;
    private File fileImage;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_dados_usuario);
        setUpToolbar();
        setUpNavigation();

        cameraUtil = new CameraUtil(getActivity());

        try {
            DataBaseHelper dataBaseHelper = EventosApplication.getInstance().getDataBaseHelper();
            UsuarioDAO dao = new UsuarioDAO(dataBaseHelper.getConnectionSource());
            usuario = dao.getUsuarioDonoDoCelular();
        } catch (SQLException e) {
            Log.i("", e.getMessage());
        }

        initFields();

        buscarFaculdades();

        updateFields();

        focusEditText();
    }

    private void updateFields() {
        txtNome.setText(usuario.getNome());
        txtEmail.setText(usuario.getEmail());
        txtSenha.setText(usuario.getSenha());
        cbOrganizador.setChecked(usuario.isQueroSerAdmin());
        ImageUtils.setImageUsuario(getContext(), usuario.getFotoPerfil(), imgView, (ProgressBar) findViewById(R.id.progressImg));
    }

    private void initFields() {
        btnCadastrar = (Button) findViewById(R.id.btnCadastrar);
        txtNome = (EditText) findViewById(R.id.txtNome);
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtSenha = (EditText) findViewById(R.id.txtSenha);
        spFaculdades = (Spinner) findViewById(R.id.spFaculdades);
        cbOrganizador = (CheckBox) findViewById(R.id.cbOrganizador);
        imgView = (ImageView) findViewById(R.id.img);

        // Botão abrir a galeria
        findViewById(R.id.btnGaleria).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraUtil.abrirGaleria(getActivity());
            }
        });

        // Botão abrir a galeria
        findViewById(R.id.btnCamera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraUtil.abrirCamera(getActivity());
            }
        });

        btnCadastrar.setOnClickListener(onClickCadastrar());
    }

    public void focusEditText() {

        final View viewTxtNome = findViewById(R.id.viewTxtNome);
        final View viewTxtEmail = findViewById(R.id.viewTxtEmail);
        final View viewTxtSenha = findViewById(R.id.viewTxtSenha);

        txtEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    viewTxtEmail.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.primary));
                } else {
                    viewTxtEmail.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.cinzaBBB));
                }
            }
        });

        txtSenha.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    viewTxtSenha.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.primary));
                } else {
                    viewTxtSenha.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.cinzaBBB));
                }
            }
        });

        txtNome.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    viewTxtNome.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.primary));
                } else {
                    viewTxtNome.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.cinzaBBB));
                }
            }
        });
    }

    private View.OnClickListener onClickCadastrar() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean validaOk = validaCampos();

                if (validaOk) {
                    usuario.setNome(txtNome.getText().toString());
                    usuario.setEmail(txtEmail.getText().toString());
                    usuario.setSenha(txtSenha.getText().toString());
                    usuario.setQueroSerAdmin(cbOrganizador.isChecked());
                    usuario.setFaculdade(faculdadeSelecionada);

                    if (AndroidUtils.isNetworkAvailable(getApplicationContext())) {
                        new UsuarioTask().execute(usuario);
                    } else {
                        snack(findViewById(R.id.loginActivity), "Conexão  indisponível");
                    }
                }
            }
        };
    }

    private boolean validaCampos() {
        boolean validaOk = ValidationUtil.validateNotNull(Arrays.asList(txtNome, txtEmail, txtSenha));
        if (validaOk) {
            validaOk = ValidationUtil.validaSpinnerFaculdade(spFaculdades);
        }
        if (validaOk) {
            validaOk = ValidationUtil.validateName(txtNome);
        }
        if (validaOk) {
            validaOk = ValidationUtil.validateEmail(txtEmail);
        }
        if (validaOk) {
            validaOk = ValidationUtil.validateSenha(txtSenha);
        }
        return validaOk;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        this.fileImage = cameraUtil.pegarImagem(requestCode, resultCode, data, imgView);
    }

    private void buscarFaculdades() {

        if (AndroidUtils.isNetworkAvailable(getContext())) {
            new FaculdadesTask().execute();
        } else {
            alert("Alerta", "Você não esta conectado na internet");
        }

    }

    private class UsuarioTask extends AsyncTask<Usuario, Void, Usuario> {

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(getContext(), "Atualizando", "Atualizando cadastro aguarde...", false, false);
        }

        @Override
        protected Usuario doInBackground(Usuario... usuarios) {
            try {
                Usuario usuario = usuarios[0];

                UsuarioRest usuarioRest = new UsuarioRest(AlterarDadosUsuarioActivity.this);
                EventoRest eventoRest = new EventoRest(getApplicationContext());

                // Faz upload da foto
                if (fileImage != null && fileImage.exists()) {
                    ResponseWithURL responseWithURL = eventoRest.postFotoBase64(fileImage);
                    if (responseWithURL != null && responseWithURL.isOk()) {
                        // Atualiza a foto do usuario
                        usuario.setFotoPerfil(responseWithURL.getUrl());
                    }
                }

                return usuarioRest.update(usuario);
            } catch (Exception e) {
                Log.e("ERRO", e.getMessage(), e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(Usuario usuario) {
            progressDialog.dismiss();

            if (usuario != null && usuario.getId() != null) {

                // Salva o usuário
                try {
                    DataBaseHelper dataBaseHelper = EventosApplication.getInstance().getDataBaseHelper();
                    UsuarioDAO dao = new UsuarioDAO(dataBaseHelper.getConnectionSource());
                    dao.deletarUsuarioDonoDoCelular();
                    dao.saveUsuarioDonoDoCelular(usuario);
                    finish();
                } catch (SQLException e) {
                    Log.i("", e.getMessage());
                }
            } else {
                alert("Alerta", "Aconteceu um erro ao tentar atualizar os dados usuário!");
            }
        }
    }

    private class FaculdadesTask extends AsyncTask<Void, Void, List<Faculdade>> {

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(getContext(), "Buscando faculdades", "Aguarde enquando as faculdades são carregadas...", false, false);
        }

        @Override
        protected List<Faculdade> doInBackground(Void... voids) {
            FaculdadeRest service = new FaculdadeRest(getContext());

            try {
                return service.getFaculdades();
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

        Faculdade f = new Faculdade();
        f.setNome("Selecione uma faculdade");
        f.setId(Long.MAX_VALUE);
        faculdades.add(0, f);

        for (int i = 0; i < 20; i++) {
            Faculdade f1 = new Faculdade();
            f1.setNome("Faculdade " + i);
            f1.setId((long) i);
            faculdades.add(f1);
        }

        BaseAdapter adapter = new FaculdadesAdapter(getContext(), faculdades);
        AlterarDadosUsuarioActivity.this.spFaculdades.setAdapter(adapter);
        AlterarDadosUsuarioActivity.this.spFaculdades.setSelection(0);

        final List<Faculdade> faculdadesAux = faculdades;

        AlterarDadosUsuarioActivity.this.spFaculdades.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                faculdadeSelecionada = faculdadesAux.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        selecionaItemSpinner(AlterarDadosUsuarioActivity.this.spFaculdades, usuario.getFaculdade().getId());
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
}
