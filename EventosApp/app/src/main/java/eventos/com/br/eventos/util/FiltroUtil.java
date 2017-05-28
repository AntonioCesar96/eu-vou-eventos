package eventos.com.br.eventos.util;

import java.sql.SQLException;

import eventos.com.br.eventos.dao.FiltroDAO;
import eventos.com.br.eventos.model.Filtro;

/**
 * Created by antonio on 19/03/17.
 */

public class FiltroUtil {

    public static Filtro getFiltro() {

        try {
            FiltroDAO dao = new FiltroDAO();
            Filtro filtro = dao.getFiltro();

            if (filtro == null) {
                filtro = new Filtro();
                filtro.setId(Long.MAX_VALUE);
                filtro.setIdCidade(Long.MAX_VALUE);
                filtro.setIdEstado(Long.MAX_VALUE);
                filtro.setIdFaculdade(Long.MAX_VALUE);
            }

            return filtro;
        } catch (SQLException e) {
            e.printStackTrace();
            return new Filtro();
        }
    }
}
