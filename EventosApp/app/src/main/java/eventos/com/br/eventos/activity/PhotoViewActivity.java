package eventos.com.br.eventos.activity;

import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_view);

        String url = (String) getIntent().getSerializableExtra("url");

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
                if(mAttacher!=null){
                    mAttacher.update();
                }else{
                    mAttacher = new PhotoViewAttacher(imgView);
                }
            }

            @Override
            public void onError() {
                progress.setVisibility(View.GONE);
            }
        });
    }
}
