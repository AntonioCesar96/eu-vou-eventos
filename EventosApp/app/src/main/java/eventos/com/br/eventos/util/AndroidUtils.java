package eventos.com.br.eventos.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;


public class AndroidUtils {
    protected static final String TAG = "livroandroid";

    /**
     * <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
     */
    public static boolean isNetworkAvailable(Context context) {
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity == null) {
                return false;
            } else {
                NetworkInfo[] info = connectivity.getAllNetworkInfo();
                if (info != null) {
                    for (int i = 0; i < info.length; i++) {
                        if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                            return true;
                        }
                    }
                }
            }
        } catch (SecurityException e) {
            alertDialog(context, e.getClass().getSimpleName(), e.getMessage());
        }
        return false;
    }

    public static void alertDialog(final Context context, final int title, final int mensagem) {
        try {
            AlertDialog dialog = new AlertDialog.Builder(context).setTitle(title).setMessage(mensagem).create();
            dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            dialog.show();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    public static void alertDialog(final Context context, final String title, final String mensagem) {
        try {
            AlertDialog dialog = new AlertDialog.Builder(context).setTitle(
                    title).setMessage(mensagem)
                    .create();
            dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            dialog.show();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    // Fecha o teclado virtual se aberto (view com foque)
    public static boolean closeVirtualKeyboard(Context context, View view) {
        // Fecha o teclado virtual
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            boolean b = imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            return b;
        }
        return false;
    }

    /**
     * Converte de DIP para Pixels
     */
    public static float toPixels(Context context, float dip) {
        Resources r = context.getResources();
        //int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, r.getDisplayMetrics());

        float scale = r.getDisplayMetrics().density;
        int px = (int) (dip * scale + 0.5f);

        return px;
    }

    /**
     * Converte de Pixels para DIP
     */
    public static float toDip(Context context, float pixels) {
        Resources r = context.getResources();

        float scale = r.getDisplayMetrics().density;

        int dip = (int) (pixels / scale + 0.5f);
        return dip;
    }

    /**
     * Para fazer android:foreground="?android:attr/selectableItemBackground"
     *
     * @param context getActionBar().getThemedContext()
     * @param attrId  android.R.attr.selectableItemBackground
     */
    public Drawable getDrawableAttr(Context context, int attrId) {
        int[] attrs = new int[]{attrId};
        TypedArray ta = context.obtainStyledAttributes(attrs);
        Drawable drawable = ta.getDrawable(0 /* index */);
        ta.recycle();
        return drawable;
    }

    public static boolean isAndroid3Honeycomb() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    public static boolean isAndroid4ICS() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
    }

    public static boolean isAndroid5Lollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }


    // Retorna se a tela é large ou xlarge
    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    // Retona se é um tablet com Android 3.x
    public static boolean isAndroid_3_Tablet(Context context) {
        return isAndroid3Honeycomb() && isTablet(context);
    }


    /* Lê a versão do app */
    public static String getVersionName(Activity activity) {
        PackageManager pm = activity.getPackageManager();
        String packageName = activity.getPackageName();
        String versionName;
        try {
            PackageInfo info = pm.getPackageInfo(packageName, 0);
            versionName = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            versionName = "N/A";
        }
        return versionName;
    }

}
