package eventos.com.br.eventos.tasks;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.List;

import eventos.com.br.eventos.model.Faculdade;
import eventos.com.br.eventos.rest.FaculdadeRest;


/**
 * Created by Matheus on 26/04/2017.
 */

public class FaculdadeTask extends AsyncTask<Long, Void, List<Faculdade>> {
    ProgressDialog progressDialog;
    private AppCompatActivity activity;
    private CallbackSearchFaculdades callbackSearchFaculdades;

    public FaculdadeTask(AppCompatActivity activity, CallbackSearchFaculdades callbackSearchFaculdades) {
        this.activity = activity;
        this.callbackSearchFaculdades = callbackSearchFaculdades;
    }

    @Override
    protected void onPreExecute() {
        progressDialog = ProgressDialog.show(activity, "Buscando Faculdades", "Aguarde...", false, false);
    }

    @Override
    protected List<Faculdade> doInBackground(Long... longs) {
        Long idCidade = longs[0];

        FaculdadeRest service = new FaculdadeRest(activity);

        try {
            return service.getFaculdades(idCidade);
        } catch (Exception e) {
            Log.e("ERRO", e.getMessage(), e);
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<Faculdade> faculdades) {
        progressDialog.dismiss();
        callbackSearchFaculdades.onCallbackSearchFaculdades(faculdades);
    }

    public interface CallbackSearchFaculdades {
        void onCallbackSearchFaculdades(List<Faculdade> faculdades);
    }
}