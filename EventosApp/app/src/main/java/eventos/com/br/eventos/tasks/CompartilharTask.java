package eventos.com.br.eventos.tasks;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;

import java.io.File;

import livroandroid.lib.utils.IOUtils;
import livroandroid.lib.utils.SDCardUtils;

/**
 * Created by Matheus on 26/04/2017.
 */

public class CompartilharTask extends AsyncTask<String, Void, Uri> {
    private AppCompatActivity activity;

    public CompartilharTask(AppCompatActivity activity) {
        this.activity = activity;
    }

    @Override
    protected Uri doInBackground(String... urls) {
        String url = urls[0];

        String fileName = "foto_temp.jpg";
        File file = SDCardUtils.getPrivateFile(activity, fileName, "DIRECTORY_PICTURES");

        IOUtils.downloadToFile(url, file);
        return Uri.fromFile(file);
    }

    @Override
    protected void onPostExecute(Uri uri) {

        // CompartilharEvento2
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.setType("image/*");
        activity.startActivity(Intent.createChooser(shareIntent, "Compartilhar Evento"));
    }
}