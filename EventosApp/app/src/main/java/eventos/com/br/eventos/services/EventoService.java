package eventos.com.br.eventos.services;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eventos.com.br.eventos.R;
import eventos.com.br.eventos.config.EventosApplication;
import eventos.com.br.eventos.dao.UsuarioDAO;
import eventos.com.br.eventos.model.Evento;
import eventos.com.br.eventos.model.Response;
import eventos.com.br.eventos.model.ResponseWithURL;
import eventos.com.br.eventos.model.Usuario;
import eventos.com.br.eventos.util.CalendarDeserializer;
import eventos.com.br.eventos.util.CalendarSerializer;
import eventos.com.br.eventos.util.TipoBusca;
import livroandroid.lib.utils.FileUtils;
import livroandroid.lib.utils.HttpHelper;
import livroandroid.lib.utils.IOUtils;

/**
 * Created by antonio on 16/07/16.
 */
public class EventoService {
    private String url;
    private Context context;

    public EventoService(Context context) {
        url = EventosApplication.getURL(context) + "evento";
        this.context = context;
    }

    public List<Evento> getEventos(TipoBusca tipoDeBusca) throws Exception {

        if (tipoDeBusca.equals(TipoBusca.FAVORITOS)) {
            return new ArrayList<>();
        }
        if (tipoDeBusca.equals(TipoBusca.USUARIO)) {
            return getEventosPorUsuario();
        }
        return getEventosProximos();
    }

    private List<Evento> getEventosProximos() throws IOException {
        HttpHelper helper = new HttpHelper();
        helper.LOG_ON = true;

        String json = helper.doGet(url + "/proximos");

        Type listType = new TypeToken<ArrayList<Evento>>() {
        }.getType();

        GsonBuilder builder = new GsonBuilder();

        // Serializador para classe Calendar
        builder.registerTypeAdapter(Calendar.class, new CalendarDeserializer());
        builder.registerTypeAdapter(GregorianCalendar.class, new CalendarDeserializer());

        Gson gson = builder.create();

        return gson.fromJson(json, listType);
    }

    private List<Evento> getEventosPorUsuario() throws IOException {

        UsuarioDAO dao = new UsuarioDAO(context);
        List<Usuario> usuarios = dao.findAll();

        if (usuarios != null && usuarios.size() != 0) {
            HttpHelper helper = new HttpHelper();
            helper.LOG_ON = true;

            String json = helper.doGet(url + "/usuario/"+usuarios.get(0).getId());

            Type listType = new TypeToken<ArrayList<Evento>>() {
            }.getType();

            GsonBuilder builder = new GsonBuilder();

            // Serializador para classe Calendar
            builder.registerTypeAdapter(Calendar.class, new CalendarDeserializer());
            builder.registerTypeAdapter(GregorianCalendar.class, new CalendarDeserializer());

            Gson gson = builder.create();

            return gson.fromJson(json, listType);
        }
        return new ArrayList<>();
    }

    public Evento getEvento(Long id) throws IOException {

        HttpHelper helper = new HttpHelper();
        String json = helper.doGet(url + "/" + id);

        GsonBuilder builder = new GsonBuilder();

        // Serializador para classe Calendar
        builder.registerTypeAdapter(Calendar.class, new CalendarDeserializer());
        builder.registerTypeAdapter(GregorianCalendar.class, new CalendarDeserializer());

        Gson gson = builder.create();
        Evento evento = gson.fromJson(json, Evento.class);

        return evento;
    }

    public List<Evento> getCarrosFromRaw() throws IOException {

        String json = FileUtils.readRawFileString(this.context, R.raw.eventos, "UTF-8");

        Type listType = new TypeToken<ArrayList<Evento>>() {
        }.getType();

        Gson gson = new Gson();
        return gson.fromJson(json, listType);
    }

    public ResponseWithURL postFotoBase64(File file) throws IOException {
        String urlUpload = url + "/postFotoBase64";

        // Converte para Base64
        byte[] bytes = IOUtils.toBytes(new FileInputStream(file));
        String base64 = Base64.encodeToString(bytes, Base64.NO_WRAP);

        Map<String, String> params = new HashMap<>();
        params.put("fileName", file.getName());
        params.put("base64", base64);

        HttpHelper http = new HttpHelper();
        http.setContentType("application/x-www-form-urlencoded");
        http.setCharsetToEncode("UTF-8");
        String json = http.doPost(urlUpload, params, "UTF-8");

        return new Gson().fromJson(json, ResponseWithURL.class);
    }

    public Response save(Evento evento) throws IOException {

        GsonBuilder builder = new GsonBuilder();

        // Serializador para classe Calendar
        builder.registerTypeAdapter(Calendar.class, new CalendarSerializer());
        builder.registerTypeAdapter(GregorianCalendar.class, new CalendarSerializer());

        Gson gson = builder.create();

        String jsonEvento = gson.toJson(evento);

        HttpHelper http = new HttpHelper();
        http.setContentType("application/json; charset=utf-8");
        String json = http.doPost(url, jsonEvento.getBytes(), "UTF-8");

        return new Gson().fromJson(json, Response.class);
    }
}
