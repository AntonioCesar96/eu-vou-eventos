package eventos.com.br.eventos.tasks;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.sql.SQLException;
import java.util.List;

import eventos.com.br.eventos.config.EventosApplication;
import eventos.com.br.eventos.dao.DataBaseHelper;
import eventos.com.br.eventos.dao.NotificacaoDAO;
import eventos.com.br.eventos.model.Notificacao;
import eventos.com.br.eventos.util.AlarmEventoUtil;


public class AgendarNotificacoesService extends Service {

    @Override
    public IBinder onBind(Intent i) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        new WorkerThread(startId).start();

        return super.onStartCommand(intent, flags, startId);
    }

    // Thread que faz o trabalho pesado
    class WorkerThread extends Thread {
        private int startId;

        public WorkerThread(int startId) {
            this.startId = startId;
        }

        public void run() {

            try {
                DataBaseHelper dataBaseHelper = EventosApplication.getInstance().getDataBaseHelper();

                AlarmEventoUtil alarmEventoUtil = new AlarmEventoUtil(getApplicationContext(), dataBaseHelper);
                NotificacaoDAO dao = new NotificacaoDAO(dataBaseHelper.getConnectionSource());

                List<Notificacao> notificacoes = dao.all();

                for (Notificacao n : notificacoes) {
                    alarmEventoUtil.agendarAlarm(n.getIdEvento(), n.getDataEvento());
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            stopSelf(startId);
        }
    }
}
