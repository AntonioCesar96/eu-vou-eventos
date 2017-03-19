package eventos.com.br.eventos.model;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Created by antonio on 19/03/17.
 */

@DatabaseTable(tableName = "filtro")
public class Filtro implements Serializable {
    private static final long serialVersionUID = 1L;

    @DatabaseField(id = true, generatedId = false)
    private Long id;

    @DatabaseField
    private Long idFaculdade;

    @DatabaseField
    private Long idCidade;

    @DatabaseField(dataType = DataType.SERIALIZABLE)
    private Calendar dataInicial;

    @DatabaseField(dataType = DataType.SERIALIZABLE)
    private Calendar dataFinal;

    @DatabaseField(dataType = DataType.ENUM_STRING)
    private FiltroTipo filtroTipo;

    public Filtro() {

    }

    public Long getIdCidade() {
        return idCidade;
    }

    public void setIdCidade(Long idCidade) {
        this.idCidade = idCidade;
    }

    public Long getIdFaculdade() {
        return idFaculdade;
    }

    public void setIdFaculdade(Long idFaculdade) {
        this.idFaculdade = idFaculdade;
    }

    public Calendar getDataInicial() {
        return dataInicial;
    }

    public void setDataFinal(Calendar dataFinal) {
        this.dataFinal = dataFinal;
    }

    public Calendar getDataFinal() {
        return dataFinal;
    }

    public void setDataInicial(Calendar dataInicial) {
        this.dataInicial = dataInicial;
    }

    public FiltroTipo getFiltroTipo() {
        return filtroTipo;
    }

    public void setFiltroTipo(FiltroTipo filtroTipo) {
        this.filtroTipo = filtroTipo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
