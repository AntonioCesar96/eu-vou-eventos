package eventos.com.br.eventos.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;

import eventos.com.br.eventos.R;
import eventos.com.br.eventos.config.EventosApplication;
import eventos.com.br.eventos.dao.DataBaseHelper;
import eventos.com.br.eventos.dao.UsuarioDAO;
import eventos.com.br.eventos.model.Usuario;
import eventos.com.br.eventos.rest.UsuarioRest;
import eventos.com.br.eventos.util.AndroidUtils;
import eventos.com.br.eventos.util.ValidationUtil;

public class LoginActivity extends BaseActivity {

    private Button btnEntrar;
    private EditText txtEmail, txtSenha;
    private TextView txtCadastrar;
    private ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setUpToolbar();
        setUpNavigation();

        progress = (ProgressBar) findViewById(R.id.progress);
        btnEntrar = (Button) findViewById(R.id.btnEntrar);
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtSenha = (EditText) findViewById(R.id.txtSenha);
        txtCadastrar = (TextView) findViewById(R.id.txtCadastrar);

        focusEditText();

        btnEntrar.setOnClickListener(clickBtnEntrar());
        txtCadastrar.setOnClickListener(clickTxtEntrar());
    }

    private View.OnClickListener clickTxtEntrar() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), CadastroUsuarioActivity.class));
                finish();
            }
        };
    }

    private View.OnClickListener clickBtnEntrar() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean validaOk = validaCampos();

                if (validaOk) {
                    String email = txtEmail.getText().toString();
                    String senha = txtSenha.getText().toString();

                    if (AndroidUtils.isNetworkAvailable(getApplicationContext())) {
                        new UsuarioTask().execute(email, senha);
                    } else {
                        snack(findViewById(R.id.loginActivity), "Conexão  indisponível");
                    }
                }
            }
        };
    }

    private boolean validaCampos() {
        boolean validaOk = ValidationUtil.validateNotNull(Arrays.asList(txtEmail, txtSenha));
        if (validaOk) {
            validaOk = ValidationUtil.validateEmail(txtEmail);
        }
        if (validaOk) {
            validaOk = ValidationUtil.validateSenha(txtSenha);
        }
        return validaOk;
    }

    public void focusEditText() {

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
    }

    private class UsuarioTask extends AsyncTask<String, Void, Usuario> {

        @Override
        protected void onPreExecute() {
            LoginActivity.this.progress.setVisibility(View.VISIBLE);
        }

        @Override
        protected Usuario doInBackground(String... strings) {
            UsuarioRest service = new UsuarioRest(LoginActivity.this);
            try {
                String email = strings[0];
                String senha = strings[1];

                return service.logar(email, senha);
            } catch (IOException e) {
                Log.e("ERRO", e.getMessage(), e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(Usuario usuario) {
            LoginActivity.this.progress.setVisibility(View.INVISIBLE);

            if (usuario != null && usuario.getId() != null) {
                // Salva o usuário
                try {
                    DataBaseHelper dataBaseHelper = EventosApplication.getInstance().getDataBaseHelper();
                    UsuarioDAO dao = new UsuarioDAO(dataBaseHelper.getConnectionSource());
                    dao.saveUsuarioDonoDoCelular(usuario);

                    // Salva usuário na memória enquanto o aplicativo estiver aberto
                    EventosApplication.getInstance().setUsuario(usuario);

                    finish();
                } catch (SQLException e) {
                    Log.i("Error", e.getMessage());
                }
            } else {
                alert("Alerta", "Login ou senha estão incorretos!");
            }
        }
    }
}
