package eventos.com.br.eventos.dao;

import android.content.Context;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.List;

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

    public void save(Usuario usuario) {
        try {
            FaculdadeDAO faculdadeDAO = new FaculdadeDAO(cs);

            int result = this.create(usuario);

            if (result == 1) {
                faculdadeDAO.create(usuario.getFaculdade());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Usuario getUsuario() {

        try {
            List<Usuario> usuarios = this.queryForAll();

            if (usuarios != null && usuarios.size() != 0) {
                return usuarios.get(0);
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void deletar() {
        try {
            this.delete(this.queryForAll());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}