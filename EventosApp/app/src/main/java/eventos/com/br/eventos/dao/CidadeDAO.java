package eventos.com.br.eventos.dao;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

import eventos.com.br.eventos.model.Cidade;
import eventos.com.br.eventos.model.Local;

/**
 * Created by antonio on 12/03/17.
 */

public class CidadeDAO extends BaseDaoImpl<Cidade, Long> {
    public CidadeDAO(ConnectionSource cs) throws SQLException{
        super(Cidade.class);
        setConnectionSource(cs);
        initialize();
    }

    public void save(Cidade c) throws SQLException {
        if (c != null) {
            if (!idExists(c.getId())){
                create(c);
            }
        }
    }
}