package eventos.com.br.eventos.tasks;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;

import java.io.File;
import java.util.List;

import eventos.com.br.eventos.model.Estado;
import eventos.com.br.eventos.util.IOUtils;
import eventos.com.br.eventos.util.SDCardUtils;


/**
 * Created by Matheus on 26/04/2017.
 */

public class DownloadImagemTask extends AsyncTask<String, Void, File> {
    private AppCompatActivity activity;
    private CallbackDownloadImagem callbackDownloadImagem;

    public DownloadImagemTask(AppCompatActivity activity, CallbackDownloadImagem callbackDownloadImagem) {
        this.activity = activity;
        this.callbackDownloadImagem = callbackDownloadImagem;
    }

    @Override
    protected File doInBackground(String... urls) {
        String url = urls[0];

        String fileName = "foto_temp.jpg";
        File file = SDCardUtils.getPrivateFile(activity, fileName, "DIRECTORY_PICTURES");

        IOUtils.downloadToFile(url, file);
        return file;
    }

    @Override
    protected void onPostExecute(File imagem) {
        callbackDownloadImagem.onCallbackDownloadImagem(imagem);
    }

    public interface CallbackDownloadImagem {
        void onCallbackDownloadImagem(File imagem);
    }
}