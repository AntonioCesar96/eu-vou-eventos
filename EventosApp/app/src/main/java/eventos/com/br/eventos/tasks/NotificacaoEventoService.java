package eventos.com.br.eventos.tasks;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.sql.SQLException;
import java.util.Calendar;

import eventos.com.br.eventos.R;
import eventos.com.br.eventos.activity.EventoActivity;
import eventos.com.br.eventos.config.EventosApplication;
import eventos.com.br.eventos.dao.DataBaseHelper;
import eventos.com.br.eventos.dao.EventoDAO;
import eventos.com.br.eventos.dao.NotificacaoDAO;
import eventos.com.br.eventos.model.Evento;
import eventos.com.br.eventos.model.Notificacao;
import eventos.com.br.eventos.util.AlarmEventoUtil;
import eventos.com.br.eventos.util.NotificationUtil;

/**
 * Created by Matheus on 26/04/2017.
 */

public class NotificacaoEventoService extends Service {

    @Override
    public IBinder onBind(Intent i) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        new WorkerThread(intent, startId).start();

        return super.onStartCommand(intent, flags, startId);
    }

    // Thread que faz o trabalho pesado
    class WorkerThread extends Thread {
        private Intent intent;
        private int startId;

        public WorkerThread(Intent intent, int startId) {
            this.intent = intent;
            this.startId = startId;
        }

        public void run() {

            try {
                Long idEvento = (Long) intent.getSerializableExtra("idEvento");

                DataBaseHelper dataBaseHelper = EventosApplication.getInstance().getDataBaseHelper();
                EventoDAO dao = new EventoDAO(dataBaseHelper.getConnectionSource());
                Evento evento = dao.getById(idEvento);

                Intent notifIntent = new Intent(getApplicationContext(), EventoActivity.class);
                notifIntent.putExtra("notificacao", true);
                notifIntent.putExtra("evento", evento);

                NotificacaoDAO notificacaoDAO = new NotificacaoDAO(dataBaseHelper.getConnectionSource());
                AlarmEventoUtil alarmEventoUtil = new AlarmEventoUtil(getApplicationContext(), dataBaseHelper);

                Notificacao n = notificacaoDAO.queryForId(idEvento);

                if (n.isPrimeiraNotificacao()) {
                    NotificationUtil.create(getApplicationContext(), idEvento.intValue(), notifIntent, R.mipmap.ic_launcher, "É amanha!!!", evento.getNome());

                    n.setPrimeiraNotificacao(false);
                    n.setDataEvento(agendarAlarmParaAmanha(n.getDataEvento()));
                    notificacaoDAO.update(n);

                    alarmEventoUtil.agendarAlarm(n.getIdEvento(), n.getDataEvento());
                } else {
                    NotificationUtil.create(getApplicationContext(), idEvento.intValue(), notifIntent, R.mipmap.ic_launcher, "É daqui a pouco!!!", evento.getNome());
                    notificacaoDAO.deletar(idEvento);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

            stopSelf(startId);
        }

        private long agendarAlarmParaAmanha(Long data) {
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(data);
            c.add(Calendar.SECOND, 10);
            long time = c.getTimeInMillis();

            return time;
        }
    }
}
