package eventos.com.br.eventos.tasks;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.List;

import eventos.com.br.eventos.model.Estado;
import eventos.com.br.eventos.rest.EnderecoRest;


/**
 * Created by Matheus on 26/04/2017.
 */

public class EstadoTask extends AsyncTask<Void, Void, List<Estado>> {
    ProgressDialog progressDialog;
    private AppCompatActivity activity;
    private CallbackSearchEstados callbackSearchEstados;

    public EstadoTask(AppCompatActivity activity, CallbackSearchEstados callbackSearchEstados){
        this.activity = activity;
        this.callbackSearchEstados = callbackSearchEstados;
    }

    @Override
    protected void onPreExecute() {
        progressDialog = ProgressDialog.show(activity, "Buscando estados", "Aguarde...", false, false);
    }

    @Override
    protected List<Estado> doInBackground(Void... voids) {
        EnderecoRest service = new EnderecoRest(activity);

        try {
            return service.getEstados();
        } catch (Exception e) {
            Log.e("ERRO", e.getMessage(), e);
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<Estado> estados) {
        progressDialog.dismiss();
        callbackSearchEstados.onCallbackSearchEstados(estados);
    }

    public interface CallbackSearchEstados {
        void onCallbackSearchEstados(List<Estado> estados);
    }
}