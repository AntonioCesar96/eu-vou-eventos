package eventos.com.br.eventos.dao;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

import eventos.com.br.eventos.model.Pais;

/**
 * Created by antonio on 12/03/17.
 */

public class PaisDAO extends BaseDaoImpl<Pais, Long> {
    public PaisDAO(ConnectionSource cs) throws SQLException{
        super(Pais.class);
        setConnectionSource(cs);
        initialize();
    }
}