package eventos.com.br.eventos.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.sql.SQLException;

import eventos.com.br.eventos.R;
import eventos.com.br.eventos.config.EventosApplication;
import eventos.com.br.eventos.dao.DataBaseHelper;
import eventos.com.br.eventos.model.Usuario;
import eventos.com.br.eventos.dao.UsuarioDAO;
import eventos.com.br.eventos.rest.UsuarioRest;
import livroandroid.lib.utils.AndroidUtils;

public class SplashScreenActivity extends BaseActivity {
    protected DataBaseHelper dataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        dataBaseHelper = EventosApplication.getInstance().getDataBaseHelper();

        Usuario usuario = null;
        try {
            UsuarioDAO dao = new UsuarioDAO(dataBaseHelper.getConnectionSource());
            usuario = dao.getUsuarioDonoDoCelular();
        } catch (SQLException e) {
            Log.i("", e.getMessage());
        }

        if (usuario != null) {

            // Salva usuário na memória enquanto o aplicativo estiver aberto
            EventosApplication.getInstance().setUsuario(usuario);

            if (AndroidUtils.isNetworkAvailable(getContext())) {
                new UsuarioTask().execute(usuario.getEmail(), usuario.getSenha());
            } else {
                goMainActivity();
            }
        } else {
            goMainActivity();
        }
    }

    private void goMainActivity() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 500);
    }

    private class UsuarioTask extends AsyncTask<String, Void, Usuario> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Usuario doInBackground(String... strings) {
            UsuarioRest service = new UsuarioRest(SplashScreenActivity.this);
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

            if (usuario != null && usuario.getId() != null) {
                // Salva o usuário
                try {
                    UsuarioDAO dao = new UsuarioDAO(dataBaseHelper.getConnectionSource());
                    dao.deletarUsuarioDonoDoCelular();
                    dao.saveUsuarioDonoDoCelular(usuario);

                    // Salva usuário na memória enquanto o aplicativo estiver aberto
                    EventosApplication.getInstance().setUsuario(usuario);

                } catch (SQLException e) {
                    Log.i("", e.getMessage());
                }
            }
            Intent intent = new Intent(getContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
