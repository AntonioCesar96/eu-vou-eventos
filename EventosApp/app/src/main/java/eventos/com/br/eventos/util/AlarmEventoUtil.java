package eventos.com.br.eventos.util;

import android.content.Context;
import android.content.Intent;

import java.sql.SQLException;

import eventos.com.br.eventos.dao.DataBaseHelper;
import eventos.com.br.eventos.dao.NotificacaoDAO;

/**
 * Created by antonio on 30/04/17.
 */

public class AlarmEventoUtil {
    public static final String ACTION = "eventos.com.br.eventos.NOTIFICAR_EVENTO";
    private final Context context;
    private final DataBaseHelper dataBaseHelper;

    public AlarmEventoUtil(Context context, DataBaseHelper dataBaseHelper) {
        this.context = context;
        this.dataBaseHelper = dataBaseHelper;
    }

    public void agendarAlarm(Long idEvento, long dataEmMilisegundos) {

        Intent intent = new Intent(ACTION);
        intent.putExtra("idEvento", idEvento);
        AlarmUtil.schedule(context, idEvento.intValue(), intent, dataEmMilisegundos);
    }

    public void cancelarAlarm(Long idEvento) {

        try {
            new NotificacaoDAO(dataBaseHelper.getConnectionSource()).deletar(idEvento);

            Intent intent = new Intent(ACTION);
            AlarmUtil.cancel(context, idEvento.intValue(), intent);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
