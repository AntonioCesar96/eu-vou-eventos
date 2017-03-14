package eventos.com.br.eventos.config;

import android.content.Context;

import livroandroid.lib.utils.Prefs;

/**
 * Created by Antonio Cesar on 18/07/2016.
 */
public class EventosApplication {

    private static String URL = "http://10.100.3.49:8080/eventos/rest/";
    //private static String URL = "http://192.168.1.26:8080/eventos/rest/";

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
