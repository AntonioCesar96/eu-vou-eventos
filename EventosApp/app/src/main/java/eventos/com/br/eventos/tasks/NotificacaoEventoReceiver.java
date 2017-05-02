package eventos.com.br.eventos.tasks;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Matheus on 26/04/2017.
 */

public class NotificacaoEventoReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Intent intentService = new Intent(context, NotificacaoEventoService.class);
        intentService.putExtra("idEvento", intent.getSerializableExtra("idEvento"));

        context.startService(intentService);
    }
}
