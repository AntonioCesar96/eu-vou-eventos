package eventos.com.br.eventos.util;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by antonio on 19/02/17.
 */

public class AlertUtils {

    public static void alert(AppCompatActivity activity, String title, String message) {
        alert(activity, title, message, 0, 0);
    }

    public static void alert(AppCompatActivity activity, String message) {
        alert(activity, null, message, 0, 0);
    }

    public static void alert(AppCompatActivity activity, String title, String message, int okButton, int icon) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        if (icon > 0) {
            builder.setIcon(icon);
        }
        if (title != null) {
            builder.setTitle(title);
        }
        builder.setMessage(message);

        String okString = okButton > 0 ? activity.getString(okButton) : "OK";

        AlertDialog dialog = builder.create();
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, okString, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });
        dialog.show();
    }

    public static void alert(Context context, int title, int message, int okButton, final Runnable runnable) {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context);
        builder.setTitle(title).setMessage(message);
        String okString = okButton > 0 ? context.getString(okButton) : "OK";
        // Add the buttons
        builder.setPositiveButton(okString, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (runnable != null) {
                    runnable.run();
                }
            }
        });
        android.support.v7.app.AlertDialog dialog = builder.create();
        dialog.show();
    }
}
