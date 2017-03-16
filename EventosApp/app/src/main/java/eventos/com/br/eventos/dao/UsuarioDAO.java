package eventos.com.br.eventos.dao;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.List;

import eventos.com.br.eventos.model.Evento;
import eventos.com.br.eventos.model.Faculdade;
import eventos.com.br.eventos.model.Usuario;

/**
 * Created by antonio on 12/03/17.
 */

public class UsuarioDAO extends BaseDaoImpl<Usuario, Long> {
    private ConnectionSource cs;

    public UsuarioDAO(ConnectionSource cs) throws SQLException {
        super(Usuario.class);
        this.cs = cs;
        setConnectionSource(cs);
        initialize();
    }

    public void saveUsuarioDonoDoCelular(Usuario usuario) {
        usuario.setDonoDoCelular(true);

        try {
            if (idExists(usuario.getId())) {
                update(usuario);
            } else {
                create(usuario);
            }

            new FaculdadeDAO(cs).save(usuario.getFaculdade());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void save(Usuario usuario) {

        try {
            if (!idExists(usuario.getId())) {
                create(usuario);
            }

            new FaculdadeDAO(cs).save(usuario.getFaculdade());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Usuario getUsuarioDonoDoCelular() {

        try {
            List<Usuario> usuarios = queryForEq("donoDoCelular", true);
            if (usuarios != null && usuarios.size() != 0) {
                return usuarios.get(0);
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void deletarUsuarioDonoDoCelular() {
        try {
            DeleteBuilder<Usuario, Long> deleteBuilder = deleteBuilder();
            deleteBuilder.where().eq("donoDoCelular", true);
            deleteBuilder.delete();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}