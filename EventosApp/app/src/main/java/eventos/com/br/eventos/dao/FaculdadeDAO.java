package eventos.com.br.eventos.dao;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

import eventos.com.br.eventos.model.Faculdade;

/**
 * Created by antonio on 12/03/17.
 */

public class FaculdadeDAO extends BaseDaoImpl<Faculdade, Integer> {
    public FaculdadeDAO(ConnectionSource cs) throws SQLException{
        super(Faculdade.class);
        setConnectionSource(cs);
        initialize();
    }
}