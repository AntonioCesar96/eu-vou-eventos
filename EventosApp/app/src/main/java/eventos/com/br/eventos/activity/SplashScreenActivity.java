package eventos.com.br.eventos.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.util.List;

import eventos.com.br.eventos.R;
import eventos.com.br.eventos.dao.UsuarioDAO;
import eventos.com.br.eventos.model.Usuario;
import eventos.com.br.eventos.services.UsuarioService;
import livroandroid.lib.utils.AndroidUtils;

public class SplashScreenActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        UsuarioDAO dao = new UsuarioDAO(this);
        List<Usuario> usuarios = dao.findAll();

        if (usuarios != null && usuarios.size() > 0) {

            Usuario u = usuarios.get(0);

            if (AndroidUtils.isNetworkAvailable(getContext())) {
                new UsuarioTask().execute(u.getEmail(), u.getSenha());
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
            UsuarioService service = new UsuarioService(SplashScreenActivity.this);
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
                UsuarioDAO dao = new UsuarioDAO(getContext());
                dao.deletar();
                dao.save(usuario);

            }
            Intent intent = new Intent(getContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
