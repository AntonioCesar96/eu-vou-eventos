package eventos.com.br.eventos.dao;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.List;

import eventos.com.br.eventos.config.EventosApplication;
import eventos.com.br.eventos.model.Filtro;

/**
 * Created by antonio on 12/03/17.
 */

public class FiltroDAO extends BaseDaoImpl<Filtro, Long> {
    private ConnectionSource cs;

    public FiltroDAO() throws SQLException {
        super(Filtro.class);

        DataBaseHelper dataBaseHelper = EventosApplication.getInstance().getDataBaseHelper();
        this.cs = dataBaseHelper.getConnectionSource();
        setConnectionSource(cs);
        initialize();
    }

    public void save(Filtro filtro) {
        try {

            if (filtro.getId() != null && idExists(filtro.getId())) {
                update(filtro);
            } else {
                filtro.setId(1L);
                create(filtro);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Filtro getFiltro() {

        try {
            List<Filtro> filtros = queryForAll();
            if (filtros != null && filtros.size() != 0) {
                return filtros.get(0);
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void deletarFiltro() {
        try {
            delete(queryForAll());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}