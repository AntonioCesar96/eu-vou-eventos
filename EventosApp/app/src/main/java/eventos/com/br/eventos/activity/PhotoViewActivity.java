package eventos.com.br.eventos.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import eventos.com.br.eventos.R;
import uk.co.senab.photoview.PhotoViewAttacher;

public class PhotoViewActivity extends BaseActivity {
    private ImageView imgView;
    private ProgressBar progress;
    private PhotoViewAttacher mAttacher;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_view);
        setUpToolbar();

        ActionBar actionBar = getSupportActionBar();
        String nome = (String) getIntent().getSerializableExtra("nome");
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);

            if (nome != null) {
                actionBar.setTitle(nome);
            }
        }

        url = (String) getIntent().getSerializableExtra("url");

        initFields();
        loadImage(url);
    }

    private void initFields() {
        imgView = (ImageView) findViewById(R.id.img);
        progress = (ProgressBar) findViewById(R.id.progressImg);
    }

    private void loadImage(String url) {
        progress.setVisibility(View.VISIBLE);
        Picasso.with(getContext()).load(url).into(imgView, new Callback() {
            @Override
            public void onSuccess() {
                progress.setVisibility(View.GONE);
                if (mAttacher != null) {
                    mAttacher.update();
                } else {
                    mAttacher = new PhotoViewAttacher(imgView);
                }
            }

            @Override
            public void onError() {
                progress.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_photo_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        } if (item.getItemId() == R.id.ic_share) {

            // CompartilharEvento2
            Uri uriImage = Uri.parse(url);
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra( Intent.EXTRA_STREAM, uriImage );
            shareIntent.setType( "*/*" );
            startActivity(Intent.createChooser(shareIntent, "Compartilhar Evento"));

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
