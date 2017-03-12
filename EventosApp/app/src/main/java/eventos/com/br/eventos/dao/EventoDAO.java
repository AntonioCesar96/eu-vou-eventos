package eventos.com.br.eventos.dao;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import eventos.com.br.eventos.model.Evento;

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
            List<Evento> eventos = this.queryForAll();
            return eventos;
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
}