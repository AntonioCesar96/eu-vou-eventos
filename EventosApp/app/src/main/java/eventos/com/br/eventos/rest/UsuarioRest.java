package eventos.com.br.eventos.rest;

import android.content.Context;

import com.google.gson.Gson;

import java.io.IOException;

import eventos.com.br.eventos.config.EventosApplication;
import eventos.com.br.eventos.model.Usuario;
import eventos.com.br.eventos.util.HttpHelper;

/**
 * Created by antonio on 25/06/16.
 */
public class UsuarioRest {
    private String url;

    public UsuarioRest(Context context) {
        url = EventosApplication.getURL(context) + "usuario";
    }

    public Usuario logar(String email, String senha) throws IOException {

        HttpHelper helper = new HttpHelper();
        String json = helper.doGet(url + "?email=" + email + "&senha=" + senha);

        Gson gson = new Gson();
        return gson.fromJson(json, Usuario.class);
    }

    public Usuario save(Usuario usuario) throws Exception {
        String json = new Gson().toJson(usuario);

        HttpHelper helper = new HttpHelper();
        helper.setContentType("application/json");
        String retorno = helper.doPost(url, json.getBytes(), "UTF-8");

        Gson gson = new Gson();
        return gson.fromJson(retorno, Usuario.class);
    }

    public Usuario update(Usuario usuario) throws Exception {
        String json = new Gson().toJson(usuario);

        HttpHelper helper = new HttpHelper();
        helper.setContentType("application/json");
        String retorno = helper.doPut(url, json.getBytes(), "UTF-8");

        Gson gson = new Gson();
        return gson.fromJson(retorno, Usuario.class);
    }
}
