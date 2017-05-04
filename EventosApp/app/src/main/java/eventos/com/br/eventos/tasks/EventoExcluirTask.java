package eventos.com.br.eventos.tasks;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;

import eventos.com.br.eventos.model.Response;
import eventos.com.br.eventos.rest.EventoRest;


/**
 * Created by Matheus on 26/04/2017.
 */

public class EventoExcluirTask extends AsyncTask<Long, Void, Response> {
    ProgressDialog progressDialog;
    private AppCompatActivity activity;
    private CallbackExcluirEvento callbackExcluirEvento;

    public EventoExcluirTask(AppCompatActivity activity, CallbackExcluirEvento callbackExcluirEvento) {
        this.activity = activity;
        this.callbackExcluirEvento = callbackExcluirEvento;
    }

    @Override
    protected void onPreExecute() {
        progressDialog = ProgressDialog.show(activity, "Excluindo", "Excluindo evento aguarde...", false, false);
    }

    @Override
    protected Response doInBackground(Long... ids) {
        Long id = ids[0];
        EventoRest service = new EventoRest(activity);

        try {
            return service.excluir(id);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    protected void onPostExecute(Response response) {
        progressDialog.dismiss();
        callbackExcluirEvento.onCallbackExcluirEvento(response);
    }

    public interface CallbackExcluirEvento {
        void onCallbackExcluirEvento(Response response);
    }
}