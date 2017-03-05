package eventos.com.br.eventos.config;

import android.app.Application;
import android.content.Context;

import livroandroid.lib.utils.Prefs;

/**
 * Created by Antonio Cesar on 18/07/2016.
 */
public class EventosApplication extends Application{

    private static String URL = "http://192.168.1.24:8080/eventos/rest/";
    private static EventosApplication instance = null;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
    }

    public static EventosApplication getInstance() {
        return instance;
    }

    public static void setURL(String endereco, Context context) {

        URL = "http://" + endereco + ":8080/eventos/rest/";
        Prefs.setString(context, "url", URL);
    }

    public static String getURL(Context context) {

        String url = Prefs.getString(context, "url");

        if (url != null && !"".equals(url)) {
            return url;
        }
        return URL;
    }
}
