package eventos.com.br.eventos.util;

import java.sql.SQLException;

import eventos.com.br.eventos.dao.FiltroDAO;
import eventos.com.br.eventos.model.Filtro;
import eventos.com.br.eventos.model.FiltroTipo;

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
            /*
            if (filtro == null) {
                return url + "/proximos";
            }

            if (filtro.getFiltroTipo().equals(FiltroTipo.TODOS)) {
                return url + "/proximos";
            }

            if (filtro.getFiltroTipo().equals(FiltroTipo.CIDADE)) {
                return url + "/cidade/" + filtro.getIdCidade();
            }

            if (filtro.getFiltroTipo().equals(FiltroTipo.FACULDADE)) {
                return url + "/faculdade/" + filtro.getIdFaculdade();
            }

            return url + "/proximos";
            */
        } catch (SQLException e) {
            e.printStackTrace();
            //return url + "/proximos";
            return new Filtro();
        }

    }
}
