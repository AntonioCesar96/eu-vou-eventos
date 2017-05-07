package eventos.com.br.eventos.tasks;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;

import java.io.File;

import eventos.com.br.eventos.model.Evento;
import eventos.com.br.eventos.model.Response;
import eventos.com.br.eventos.model.ResponseWithURL;
import eventos.com.br.eventos.rest.EventoRest;


/**
 * Created by Matheus on 26/04/2017.
 */

public class EventoTask extends AsyncTask<Void, Void, Response> {
    ProgressDialog progressDialog;
    private Evento evento;
    private File fileImage;
    private AppCompatActivity activity;
    private CallbackSaveEvento callbackSaveEvento;

    public EventoTask(Evento evento, File fileImage, AppCompatActivity activity, CallbackSaveEvento callbackSaveEvento) {
        this.evento = evento;
        this.fileImage = fileImage;
        this.activity = activity;
        this.callbackSaveEvento = callbackSaveEvento;
    }

    @Override
    protected void onPreExecute() {
        if (evento.getId() != null) {
            progressDialog = ProgressDialog.show(activity, "Atualizando", "Realizando a atualização dos dados do evento, aguarde...", false, false);
        } else {
            progressDialog = ProgressDialog.show(activity, "Cadastrando", "Realizando o cadastro do evento, aguarde...", false, false);
        }
    }

    @Override
    protected Response doInBackground(Void... voids) {
        EventoRest service = new EventoRest(activity);

        Response response;

        try {
            // Faz upload da foto
            if (fileImage != null && fileImage.exists()) {
                ResponseWithURL responseWithURL = service.postFotoBase64(fileImage);
                if (responseWithURL != null && responseWithURL.isOk()) {
                    // Atualiza a foto do evento
                    evento.setEnderecoImagem(responseWithURL.getUrl());
                }
            }
            // Salva o evento
            if (evento.getId() != null) {
                response = service.update(evento);
            } else {
                response = service.save(evento);
            }

        } catch (Exception e) {
            response = null;
        }
        return response;
    }

    @Override
    protected void onPostExecute(Response response) {
        progressDialog.dismiss();
        callbackSaveEvento.onCallbackSaveEvento(response);
    }

    public interface CallbackSaveEvento {
        void onCallbackSaveEvento(Response response);
    }
}