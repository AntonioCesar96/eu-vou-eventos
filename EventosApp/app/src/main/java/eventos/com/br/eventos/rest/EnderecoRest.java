package eventos.com.br.eventos.rest;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import eventos.com.br.eventos.config.EventosApplication;
import eventos.com.br.eventos.model.Cidade;
import eventos.com.br.eventos.model.Estado;
import eventos.com.br.eventos.model.Localizacao;
import eventos.com.br.eventos.util.HttpHelper;

/**
 * Created by antonio on 25/06/16.
 */
public class EnderecoRest {
    private Context context;

    public EnderecoRest(Context context) {
        this.context = context;
    }

    public List<Estado> getEstados() throws Exception {
        String url = EventosApplication.getURL(context) + "estados";

        HttpHelper helper = new HttpHelper();
        //helper.setTIMEOUT_MILLIS(500);
        String retorno = helper.doGet(url);

        Type listType = new TypeToken<ArrayList<Estado>>() {
        }.getType();

        Gson gson = new Gson();
        return gson.fromJson(retorno, listType);
    }

    public List<Cidade> getCidades(Long idEstado) throws Exception {
        String url = EventosApplication.getURL(context) + "cidades/" + idEstado;

        HttpHelper helper = new HttpHelper();
        //helper.setTIMEOUT_MILLIS(500);
        String retorno = helper.doGet(url);

        Type listType = new TypeToken<ArrayList<Cidade>>() {
        }.getType();

        Gson gson = new Gson();
        return gson.fromJson(retorno, listType);
    }

    public Localizacao getLocalizacao(String cep) throws Exception {
        String url = "http://viacep.com.br/ws/" + cep + "/json";

        HttpHelper helper = new HttpHelper();
        String retorno = helper.doGet(url);

        JSONObject obj = new JSONObject(retorno);

        if (!obj.has("erro")) {
            Type listType = new TypeToken<Localizacao>() {
            }.getType();

            Gson gson = new Gson();
            return gson.fromJson(retorno, listType);
        } else {
            return new Localizacao();
        }
    }

}
