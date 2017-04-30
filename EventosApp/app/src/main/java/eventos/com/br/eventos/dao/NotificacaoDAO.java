package eventos.com.br.eventos.dao;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import eventos.com.br.eventos.model.Notificacao;

/**
 * Created by antonio on 12/03/17.
 */

public class NotificacaoDAO extends BaseDaoImpl<Notificacao, Long> {
    private ConnectionSource cs;

    public NotificacaoDAO(ConnectionSource cs) throws SQLException {
        super(Notificacao.class);
        this.cs = cs;
        setConnectionSource(cs);
        initialize();
    }

    public void save(Notificacao notificacao) {

        try {
            if (!idExists(notificacao.getIdEvento())) {
                create(notificacao);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deletar(Long idEvento) {
        try {
            DeleteBuilder<Notificacao, Long> deleteBuilder = deleteBuilder();
            deleteBuilder.where().eq("idEvento", idEvento);
            deleteBuilder.delete();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Notificacao> all() {

        try {
            return this.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}