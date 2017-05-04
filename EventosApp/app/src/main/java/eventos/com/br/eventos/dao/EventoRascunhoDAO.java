package eventos.com.br.eventos.dao;

import android.util.Log;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.List;

import eventos.com.br.eventos.config.EventosApplication;
import eventos.com.br.eventos.model.EventoRascunho;

/**
 * Created by antonio on 12/03/17.
 */

public class EventoRascunhoDAO extends BaseDaoImpl<EventoRascunho, Long> {
    private ConnectionSource cs;

    public EventoRascunhoDAO() throws SQLException {
        super(EventoRascunho.class);

        DataBaseHelper dataBaseHelper = EventosApplication.getInstance().getDataBaseHelper();
        this.cs = dataBaseHelper.getConnectionSource();
        setConnectionSource(cs);

        initialize();
    }

    public void save(EventoRascunho evento) {
        try {

            if (evento.getId() != null && idExists(evento.getId())) {
                update(evento);
            } else {
                evento.setId(1L);
                create(evento);
            }

            new FaculdadeDAO(cs).save(evento.getFaculdade());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeAll() {
        try {
            delete(queryForAll());
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public EventoRascunho getRascunho() {

        try {
            List<EventoRascunho> eventos = queryForAll();
            if (eventos != null && eventos.size() > 0) {
                return eventos.get(0);
            }
            return new EventoRascunho();
        } catch (SQLException e) {
            Log.e("ERRO", e.getMessage());
            return new EventoRascunho();
        }
    }
}