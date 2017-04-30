package eventos.com.br.eventos.util;

import android.content.Context;
import android.content.Intent;

import java.sql.SQLException;
import java.util.Calendar;

import eventos.com.br.eventos.dao.DataBaseHelper;
import eventos.com.br.eventos.dao.NotificacaoDAO;
import eventos.com.br.eventos.model.Notificacao;

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

        long dia = pegarMilisegundosDiaDaNotificacao(dataEmMilisegundos);

        try {
            Notificacao n = criarNotificacao(idEvento, dia);
            new NotificacaoDAO(dataBaseHelper.getConnectionSource()).save(n);

            Intent intent = new Intent(ACTION);
            intent.putExtra("idEvento", idEvento);
            AlarmUtil.schedule(context, idEvento.intValue(), intent, dia);
        } catch (SQLException e) {
            e.printStackTrace();
        }
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

    private long pegarMilisegundosDiaDaNotificacao(Long data) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        c.add(Calendar.SECOND, 30);
        long time = c.getTimeInMillis();
        return time;
    }

    private Notificacao criarNotificacao(Long idEvento, long dia) {
        Notificacao n = new Notificacao();
        n.setDataEvento(dia);
        n.setIdEvento(idEvento);
        return n;
    }
}
