package eventos.com.br.eventos.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import eventos.com.br.eventos.model.Faculdade;
import eventos.com.br.eventos.model.Usuario;

public class UsuarioDAO extends SQLiteOpenHelper {
    // Nome do banco
    public static final String NOME_BANCO = "eventos.sqlite";
    private static final String TAG = "sql";
    private static final int VERSAO_BANCO = 1;

    public UsuarioDAO(Context context) {
        // context, nome do banco, factory, versão
        super(context, NOME_BANCO, null, VERSAO_BANCO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists usuario (_id integer primary key autoincrement, id_usuario integer, " +
                "email text, nome text, senha text, foto_perfil text, id_faculdade integer, nome_faculdade text, " +
                "administrador integer, queroSerAdmin integer);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Caso mude a versão do banco de dados, podemos executar um SQL aqui
    }

    public long save(Usuario usuario) {
        long id = 0;
        SQLiteDatabase db = getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put("id_usuario", usuario.getId());
            values.put("nome", usuario.getNome());
            values.put("email", usuario.getEmail());
            values.put("senha", usuario.getSenha());
            values.put("foto_perfil", usuario.getFotoPerfil());

            //1 = true, 0 = false
            values.put("administrador", usuario.isAdministrador() ? 1 : 0);
            values.put("queroSerAdmin", usuario.isQueroSerAdmin() ? 1 : 0);

            if (usuario.getFaculdade() != null && usuario.getFaculdade().getId() != null) {
                values.put("id_faculdade", usuario.getFaculdade().getId());
                values.put("nome_faculdade", usuario.getFaculdade().getNome());
            }

            id = db.insert("usuario", "", values);
            return id;

        } finally {
            db.close();
        }
    }

    public int deletar() {
        SQLiteDatabase db = getWritableDatabase();
        try {
            int count = db.delete("usuario", null, null);
            return count;
        } finally {
            db.close();
        }
    }

    public List<Usuario> findAll() {
        SQLiteDatabase db = getWritableDatabase();
        try {
            Cursor c = db.query("usuario", null, null, null, null, null, null, null);
            return toList(c);
        } finally {
            db.close();
        }
    }

    private List<Usuario> toList(Cursor c) {
        List<Usuario> usuarios = new ArrayList<Usuario>();
        if (c.moveToFirst()) {
            do {
                Usuario u = new Usuario();

                u.setId(c.getLong(c.getColumnIndex("id_usuario")));
                u.setNome(c.getString(c.getColumnIndex("nome")));
                u.setEmail(c.getString(c.getColumnIndex("email")));
                u.setSenha(c.getString(c.getColumnIndex("senha")));
                u.setFotoPerfil(c.getString(c.getColumnIndex("foto_perfil")));

                //1 = true, 0 = false
                u.setAdministrador(c.getInt(c.getColumnIndex("administrador")) == 1);
                u.setQueroSerAdmin(c.getInt(c.getColumnIndex("queroSerAdmin")) == 1);

                Faculdade faculdade = new Faculdade();
                faculdade.setId(c.getLong(c.getColumnIndex("id_faculdade")));
                faculdade.setNome(c.getString(c.getColumnIndex("nome_faculdade")));

                u.setFaculdade(faculdade);

                usuarios.add(u);
            } while (c.moveToNext());
        }
        return usuarios;
    }

    public Usuario getUsuario() {
        List<Usuario> usuarios = findAll();

        if (usuarios != null && usuarios.size() != 0) {
            return usuarios.get(0);
        }
        return null;
    }
}
