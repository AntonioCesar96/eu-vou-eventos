package eventos.com.br.eventos.util;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import eventos.com.br.eventos.R;

/**
 * Created by antonio on 04/03/17.
 */

public class ColorsUtils {

    public static void changeColorToolbar(ImageView img, final AppCompatActivity activity, final CollapsingToolbarLayout c) {

        BitmapDrawable drawable = (BitmapDrawable) img.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        if (bitmap != null) {
            // Palleta cores
            Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                @Override
                public void onGenerated(Palette palette) {

                    Palette.Swatch mSwatch = palette.getMutedSwatch();

                    // Cor toolbar
                    int color = mSwatch != null ? mSwatch.getRgb() : ContextCompat.getColor(activity, R.color.primary_dark);
                    c.setContentScrimColor(getPrimaryColor(color));

                    // Cor status bar
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                        Window window = activity.getWindow();
                        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                        window.setStatusBarColor(color);
                    }
                }
            });
        }
    }

    public static int getPrimaryColor(int color) {
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        int alpha = Color.alpha(color);
        return Color.argb(alpha, red + 10, green + 32, blue + 33);
    }

    public static int getPrimaryDarkColor(int color) {
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        int alpha = Color.alpha(color);
        return Color.argb(alpha, red - 10, green - 32, blue - 33);
    }
}
