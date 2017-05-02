package eventos.com.br.eventos.tasks;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.List;

import eventos.com.br.eventos.model.Cidade;
import eventos.com.br.eventos.rest.EnderecoRest;


/**
 * Created by Matheus on 26/04/2017.
 */

public class CidadeTask extends AsyncTask<Long, Void, List<Cidade>> {
    ProgressDialog progressDialog;
    private AppCompatActivity activity;
    private CallbackSearchCidades callbackSearchCidades;

    public CidadeTask(AppCompatActivity activity, CallbackSearchCidades callbackSearchCidades){
        this.activity = activity;
        this.callbackSearchCidades = callbackSearchCidades;
    }

    @Override
    protected void onPreExecute() {
        progressDialog = ProgressDialog.show(activity, "Buscando cidades", "Aguarde...", false, false);
    }

    @Override
    protected List<Cidade> doInBackground(Long... ids) {
        Long idEstado = ids[0];

        EnderecoRest service = new EnderecoRest(activity);

        try {
            return service.getCidades(idEstado);
        } catch (Exception e) {
            Log.e("ERRO", e.getMessage(), e);
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<Cidade> cidades) {
        progressDialog.dismiss();
        callbackSearchCidades.onCallbackSearchCidades(cidades);
    }

    public interface CallbackSearchCidades {
        void onCallbackSearchCidades(List<Cidade> cidades);
    }
}