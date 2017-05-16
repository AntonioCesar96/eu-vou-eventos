package eventos.com.br.eventos.tasks;

import android.app.ProgressDialog;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;
import java.util.Locale;

import eventos.com.br.eventos.model.Evento;
import eventos.com.br.eventos.model.Local;


/**
 * Created by Matheus on 26/04/2017.
 */

public class MapaTask extends AsyncTask<Evento, Void, LatLng> {
    ProgressDialog progressDialog;
    private AppCompatActivity activity;
    private CallbackMap callbackMap;

    public MapaTask(AppCompatActivity activity, CallbackMap callbackMap) {
        this.activity = activity;
        this.callbackMap = callbackMap;
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected LatLng doInBackground(Evento... eventos) {
        Evento evento = eventos[0];

        LatLng latLng = null;
        try {
            Local l = evento.getLocal();
            String endereco = l.getNome() + " " + l.getRua() + " " + l.getCep() + ", " + l.getNumero() + " - "
                    + l.getBairro() + ", " + l.getCidade().getNome();

            Log.e("AAAAA", endereco);

            Geocoder geocoder = new Geocoder(activity, Locale.getDefault());
            List<Address> enderecos = geocoder.getFromLocationName(endereco, 1);

            if (enderecos != null && enderecos.size() == 0) {
                endereco = l.getRua() + " " + l.getCep() + ", " + l.getNumero() + " - "
                        + l.getBairro() + ", " + l.getCidade().getNome();
                enderecos = geocoder.getFromLocationName(endereco, 1);
            }

            if (enderecos != null && enderecos.size() > 0) {
                Address address = enderecos.get(0);
                latLng = new LatLng(address.getLatitude(), address.getLongitude());
            }

            return latLng;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(LatLng latLng) {
        callbackMap.onCallbackMap(latLng);
    }

    public interface CallbackMap {
        void onCallbackMap(LatLng latLng);
    }
}