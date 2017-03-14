package eventos.com.br.eventos.rest;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import eventos.com.br.eventos.config.EventosApplication;
import eventos.com.br.eventos.model.Faculdade;
import eventos.com.br.eventos.util.HttpHelper;

/**
 * Created by antonio on 25/06/16.
 */
public class FaculdadeRest {
    private String url;

    public FaculdadeRest(Context context) {
        url = EventosApplication.getURL(context) + "faculdades";
    }

    public List<Faculdade> getFaculdades() throws Exception {

        HttpHelper helper = new HttpHelper();
        //helper.setTIMEOUT_MILLIS(500);
        String retorno = helper.doGet(url);

        Type listType = new TypeToken<ArrayList<Faculdade>>() {
        }.getType();

        Gson gson = new Gson();
        return gson.fromJson(retorno, listType);
    }

    public List<Faculdade> getFaculdades(Long idCidade) throws Exception {
        HttpHelper helper = new HttpHelper();
        //helper.setTIMEOUT_MILLIS(500);
        String retorno = helper.doGet(url + "/" + idCidade);

        Type listType = new TypeToken<ArrayList<Faculdade>>() {
        }.getType();

        Gson gson = new Gson();
        return gson.fromJson(retorno, listType);
    }
}
