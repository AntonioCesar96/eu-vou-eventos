package eventos.com.br.eventos.dao;

import android.util.Log;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import eventos.com.br.eventos.model.Evento;
import eventos.com.br.eventos.model.Local;

/**
 * Created by antonio on 12/03/17.
 */

public class EventoDAO extends BaseDaoImpl<Evento, Long> {
    private ConnectionSource cs;

    public EventoDAO(ConnectionSource cs) throws SQLException {
        super(Evento.class);

        this.cs = cs;
        setConnectionSource(cs);

        initialize();
    }

    public void save(Evento evento) {
        try {
            UsuarioDAO usuarioDAO = new UsuarioDAO(cs);
            FaculdadeDAO faculdadeDAO = new FaculdadeDAO(cs);
            LocalDAO localDAO = new LocalDAO(cs);

            int result = this.create(evento);

            if (result == 1) {
                usuarioDAO.create(evento.getUsuario());
                faculdadeDAO.create(evento.getFaculdade());
                localDAO.create(evento.getLocal());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

        public List<Evento> all() {

        try {
            List<Evento> eventosFiltrados = new ArrayList<>();
            List<Evento> eventos = this.queryForAll();

            for (Evento e: eventos) {
                if (e.getLocal() == null){
                    Local l = new Local();
                    l.setNome("");
                    e.setLocal(l);
                }

                if (e.getDataHora().after(Calendar.getInstance())) {
                    eventosFiltrados.add(e);
                }
            }

           Collections.sort(eventosFiltrados, new Comparator<Evento>() {
                public int compare(Evento o1, Evento o2) {
                    return o1.getDataHora().compareTo(o2.getDataHora());
                }
            });

            return eventosFiltrados;
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public void remove(Evento e) {
        try {
            DeleteBuilder<Evento, Long> deleteBuilder = deleteBuilder();

            deleteBuilder.where().eq("id", e.getId());
            deleteBuilder.delete();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public Evento getById(Long id) {

        try {
            List<Evento> eventos = queryForEq("id", id);

            if (eventos != null && eventos.size() > 0) {
                return eventos.get(0);
            }
            return null;
        } catch (SQLException e) {
            Log.e("ERROR", e.getMessage());
            return null;
        }
    }
}