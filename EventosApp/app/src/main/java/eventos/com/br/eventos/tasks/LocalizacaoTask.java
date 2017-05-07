package eventos.com.br.eventos.tasks;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;

import eventos.com.br.eventos.model.Localizacao;
import eventos.com.br.eventos.rest.EnderecoRest;


/**
 * Created by Matheus on 26/04/2017.
 */

public class LocalizacaoTask extends AsyncTask<String, Void, Localizacao> {
    private AppCompatActivity activity;
    private CallbackSearchLocation callbackSearchLocation;

    public LocalizacaoTask(AppCompatActivity activity, CallbackSearchLocation callbackSearchLocation) {
        this.activity = activity;
        this.callbackSearchLocation = callbackSearchLocation;
    }

    @Override
    protected Localizacao doInBackground(String... urls) {
        String cep = urls[0];

        EnderecoRest rest = new EnderecoRest(activity);
        try {
            return rest.getLocalizacao(cep);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(Localizacao localizacao) {
        callbackSearchLocation.onCallbackSearchLocation(localizacao);
    }

    public interface CallbackSearchLocation {
        void onCallbackSearchLocation(Localizacao localizacao);
    }
}