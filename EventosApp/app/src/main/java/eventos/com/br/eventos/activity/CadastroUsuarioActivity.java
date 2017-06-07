package eventos.com.br.eventos.activity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import eventos.com.br.eventos.R;
import eventos.com.br.eventos.adapter.FaculdadesAdapter;
import eventos.com.br.eventos.config.EventosApplication;
import eventos.com.br.eventos.dao.DataBaseHelper;
import eventos.com.br.eventos.dao.UsuarioDAO;
import eventos.com.br.eventos.model.Faculdade;
import eventos.com.br.eventos.model.Usuario;
import eventos.com.br.eventos.rest.FaculdadeRest;
import eventos.com.br.eventos.rest.UsuarioRest;
import eventos.com.br.eventos.util.AndroidUtils;
import eventos.com.br.eventos.util.ValidationUtil;

public class CadastroUsuarioActivity extends BaseActivity {

    private Button btnCadastrar;
    private EditText txtNome, txtEmail, txtSenha;
    private CheckBox cbOrganizador;
    private Spinner spFaculdades;
    private ProgressDialog progressDialog;
    private Faculdade faculdadeSelecionada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);
        setUpToolbar();
        setUpNavigation();

        btnCadastrar = (Button) findViewById(R.id.btnCadastrar);
        txtNome = (EditText) findViewById(R.id.txtNome);
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtSenha = (EditText) findViewById(R.id.txtSenha);
        spFaculdades = (Spinner) findViewById(R.id.spFaculdades);
        cbOrganizador = (CheckBox) findViewById(R.id.cbOrganizador);

        btnCadastrar.setOnClickListener(clickBtnCadastrar());

        buscarFaculdades();
    }

    private View.OnClickListener clickBtnCadastrar() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean validaOk = validaCampos();

                if (validaOk) {
                    Usuario usuario = new Usuario();
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
            progressDialog = ProgressDialog.show(getContext(), "Cadastrando", "Realizando cadastro, aguarde...", false, false);
        }

        @Override
        protected Usuario doInBackground(Usuario... usuarios) {
            UsuarioRest service = new UsuarioRest(CadastroUsuarioActivity.this);
            try {
                Usuario usuario = usuarios[0];

                return service.save(usuario);
            } catch (Exception e) {
                Log.e("ERRO", e.getMessage(), e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Usuario usuario) {
            progressDialog.dismiss();

            if (usuario != null && usuario.getId() != null) {
                // Salva o usuário
                try {
                    DataBaseHelper dataBaseHelper = EventosApplication.getInstance().getDataBaseHelper();
                    UsuarioDAO dao = new UsuarioDAO(dataBaseHelper.getConnectionSource());
                    dao.saveUsuarioDonoDoCelular(usuario);

                    // Salva usuário na memória enquanto o aplicativo estiver aberto
                    EventosApplication.getInstance().setUsuario(usuario);

                    setResult(MainActivity.RECRIAR_ACTIVITY);
                    finish();
                } catch (SQLException e) {
                    Log.i("Error", e.getMessage());
                }

            } else {
                alert("Alerta", "Aconteceu um erro ao tentar cadastrar o usuário!");
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

        BaseAdapter adapter = new FaculdadesAdapter(getContext(), faculdades);
        CadastroUsuarioActivity.this.spFaculdades.setAdapter(adapter);
        CadastroUsuarioActivity.this.spFaculdades.setSelection(0);

        final List<Faculdade> faculdadesAux = faculdades;

        CadastroUsuarioActivity.this.spFaculdades.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                faculdadeSelecionada = faculdadesAux.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}
