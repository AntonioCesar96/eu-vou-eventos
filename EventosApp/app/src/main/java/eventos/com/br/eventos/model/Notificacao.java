package eventos.com.br.eventos.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable(tableName = "notificacao")
public class Notificacao implements Serializable {
    private static final long serialVersionUID = 1L;

    @DatabaseField(id = true, generatedId = false)
    private Long idEvento;

    @DatabaseField
    private Long dataEvento;


    public Notificacao() {

    }

    public Long getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(Long idEvento) {
        this.idEvento = idEvento;
    }

    public Long getDataEvento() {
        return dataEvento;
    }

    public void setDataEvento(Long dataEvento) {
        this.dataEvento = dataEvento;
    }
}
