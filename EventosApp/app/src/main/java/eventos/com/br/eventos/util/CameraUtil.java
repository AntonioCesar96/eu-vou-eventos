package eventos.com.br.eventos.util;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import java.io.File;

/**
 * Created by antonio on 26/02/17.
 */

public class CameraUtil {
    private static final int SELECT_PICTURE = 1;
    private static final int CAMERA = 2;
    private AppCompatActivity activity;
    private File fileImage;

    public CameraUtil(AppCompatActivity activity) {
        this.activity = activity;
    }


    public void abrirGaleria(AppCompatActivity activity) {
        Intent i = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        activity.startActivityForResult(Intent.createChooser(i, "Selecione uma imagem"), SELECT_PICTURE);
    }


    public File pegarImagem(int requestCode, int resultCode, Intent data, ImageView imgView) {
        // Imagem da Galeria
        if (resultCode == AppCompatActivity.RESULT_OK && requestCode == SELECT_PICTURE) {

            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = activity.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            fileImage = new File(picturePath);

            Bitmap bitmap = ImageResizeUtils.getResizedImage(Uri.fromFile(fileImage), 0, 0, false);
            imgView.setImageBitmap(bitmap);

            return fileImage;
        }

        if (resultCode == AppCompatActivity.RESULT_OK && requestCode == CAMERA && fileImage != null) {

            if (fileImage.exists()) {

                Bitmap bitmap = ImageResizeUtils.getResizedImage(Uri.fromFile(fileImage), 0, 0, false);
                imgView.setImageBitmap(bitmap);
                return fileImage;
            }
        }
        return fileImage;
    }

    public void abrirCamera(AppCompatActivity activity) {
        // Cria o caminho do arquivo no sdcard
        fileImage = SDCardUtils.getPrivateFile(activity, "foto.jpg", Environment.DIRECTORY_PICTURES);

        // Chama a intent informando o arquivo para salvar a fotobtnSalvar.setOnClickListener(onClickSalvar());
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(fileImage));
        activity.startActivityForResult(i, CAMERA);
    }


    public File setImage(String diretorioImagem, ImageView imgView) {
        // Imagem da Galeria
        if (diretorioImagem != null && diretorioImagem.trim().length() > 0) {

            File fileImage = new File(diretorioImagem);

            if (fileImage.exists()) {
                Bitmap bitmap = ImageResizeUtils.getResizedImage(Uri.fromFile(fileImage), 0, 0, false);
                imgView.setImageBitmap(bitmap);
                return fileImage;
            }
        }
        return null;
    }
}
