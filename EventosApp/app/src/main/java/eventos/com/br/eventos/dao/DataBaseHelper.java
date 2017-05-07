package eventos.com.br.eventos.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import eventos.com.br.eventos.model.Cidade;
import eventos.com.br.eventos.model.Estado;
import eventos.com.br.eventos.model.Evento;
import eventos.com.br.eventos.model.EventoRascunho;
import eventos.com.br.eventos.model.Faculdade;
import eventos.com.br.eventos.model.Filtro;
import eventos.com.br.eventos.model.Local;
import eventos.com.br.eventos.model.Notificacao;
import eventos.com.br.eventos.model.Pais;
import eventos.com.br.eventos.model.Usuario;

/**
 * Created by antonio on 12/03/17.
 */

public class DataBaseHelper extends OrmLiteSqliteOpenHelper {
    private static final String dataBaseName = "eventos.db";
    private static final int dataBaseVersion = 1;

    public DataBaseHelper(Context context) {
        super(context, dataBaseName, null, dataBaseVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase sd, ConnectionSource cs) {
        try {
            TableUtils.createTable(cs, Usuario.class);
            TableUtils.createTable(cs, Faculdade.class);
            TableUtils.createTable(cs, Cidade.class);
            TableUtils.createTable(cs, Estado.class);
            TableUtils.createTable(cs, Pais.class);
            TableUtils.createTable(cs, Evento.class);
            TableUtils.createTable(cs, Local.class);
            TableUtils.createTable(cs, Filtro.class);
            TableUtils.createTable(cs, Notificacao.class);
            TableUtils.createTable(cs, EventoRascunho.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sd, ConnectionSource cs, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(cs, Usuario.class, true);
            TableUtils.dropTable(cs, Faculdade.class, true);
            TableUtils.dropTable(cs, Cidade.class, true);
            TableUtils.dropTable(cs, Estado.class, true);
            TableUtils.dropTable(cs, Pais.class, true);
            TableUtils.dropTable(cs, Evento.class, true);
            TableUtils.dropTable(cs, Local.class, true);
            TableUtils.dropTable(cs, Filtro.class, true);
            TableUtils.dropTable(cs, Notificacao.class, true);
            TableUtils.dropTable(cs, EventoRascunho.class, true);

            onCreate(sd, cs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void close() {
        super.close();
    }
}