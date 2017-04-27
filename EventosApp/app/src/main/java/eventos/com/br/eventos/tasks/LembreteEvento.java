package eventos.com.br.eventos.tasks;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.sql.SQLException;

import eventos.com.br.eventos.R;
import eventos.com.br.eventos.activity.EventoActivity;
import eventos.com.br.eventos.activity.MainActivity;
import eventos.com.br.eventos.config.EventosApplication;
import eventos.com.br.eventos.dao.DataBaseHelper;
import eventos.com.br.eventos.dao.EventoDAO;
import eventos.com.br.eventos.dao.UsuarioDAO;
import eventos.com.br.eventos.model.Evento;
import livroandroid.lib.utils.NotificationUtil;

/**
 * Created by Matheus on 26/04/2017.
 */

public class LembreteEvento extends BroadcastReceiver {
    public static final String ACTION = "eventos.com.br.eventos.LEMBRETE_EVENTO";

    @Override
    public void onReceive(Context context, Intent intent) {

        try {
            Long idEvento = (Long) intent.getSerializableExtra("idEvento");

            DataBaseHelper dataBaseHelper = EventosApplication.getInstance().getDataBaseHelper();
            EventoDAO dao = new EventoDAO(dataBaseHelper.getConnectionSource());
            Evento evento = dao.getById(idEvento);

            Intent notifIntent = new Intent(context, EventoActivity.class);
            notifIntent.putExtra("evento", evento);

            NotificationUtil.create(context, 1, notifIntent, R.mipmap.ic_launcher, "É amanha!!!!!!!!!", evento.getNome());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
