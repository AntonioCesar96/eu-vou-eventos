package eventos.com.br.eventos.dao;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

import eventos.com.br.eventos.model.Estado;

/**
 * Created by antonio on 12/03/17.
 */

public class EstadoDAO extends BaseDaoImpl<Estado, Long> {
    public EstadoDAO(ConnectionSource cs) throws SQLException{
        super(Estado.class);
        setConnectionSource(cs);
        initialize();
    }
}