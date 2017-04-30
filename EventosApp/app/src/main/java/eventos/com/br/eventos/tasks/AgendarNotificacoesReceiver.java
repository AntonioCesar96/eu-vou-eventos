package eventos.com.br.eventos.tasks;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Matheus on 26/04/2017.
 */

public class AgendarNotificacoesReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        context.startService(new Intent(context, AgendarNotificacoesService.class));
    }
}
