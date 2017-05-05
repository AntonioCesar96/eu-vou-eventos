package eventos.com.br.eventos.tasks;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;

import java.io.IOException;

import eventos.com.br.eventos.model.Evento;
import eventos.com.br.eventos.rest.EventoRest;

public class BuscarEventoTask extends AsyncTask<Long, Void, Evento> {
    ProgressDialog progressDialog;
    private AppCompatActivity activity;
    private CallbackBuscarEvento callbackBuscarEvento;

    public BuscarEventoTask(AppCompatActivity activity, CallbackBuscarEvento callbackBuscarEvento) {
        this.activity = activity;
        this.callbackBuscarEvento = callbackBuscarEvento;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = ProgressDialog.show(activity, "Buscando evento", "Aguarde...", false, false);
    }

    @Override
    protected Evento doInBackground(Long... longs) {
        Long idEvento = longs[0];
        EventoRest service = new EventoRest(activity);

        try {
            return service.getEvento(idEvento);
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    protected void onPostExecute(Evento evento) {
        progressDialog.dismiss();
        callbackBuscarEvento.onCallbackBuscarEvento(evento);

    }

    public interface CallbackBuscarEvento {
        void onCallbackBuscarEvento(Evento e);
    }
}