package eventos.com.br.eventos.util;

import java.sql.SQLException;

import eventos.com.br.eventos.dao.FiltroDAO;
import eventos.com.br.eventos.model.Filtro;
import eventos.com.br.eventos.model.FiltroTipo;

/**
 * Created by antonio on 19/03/17.
 */

public class FiltroUtil {

    public static String gerarUrl(String url) {

        try {
            FiltroDAO dao = new FiltroDAO();
            Filtro filtro = dao.getFiltro();

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
        } catch (SQLException e) {
            e.printStackTrace();
            return url + "/proximos";
        }

    }
}
