package eventos.com.br.eventos.dao;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

import eventos.com.br.eventos.model.Faculdade;
import eventos.com.br.eventos.model.Usuario;

/**
 * Created by antonio on 12/03/17.
 */

public class FaculdadeDAO extends BaseDaoImpl<Faculdade, Long> {
    public FaculdadeDAO(ConnectionSource cs) throws SQLException{
        super(Faculdade.class);
        setConnectionSource(cs);
        initialize();
    }

    public void save(Faculdade f) throws SQLException {
        if (f != null) {
            if (!idExists(f.getId())){
                create(f);
            }

            new CidadeDAO(connectionSource).save(f.getCidade());
        }
    }
}