package eventos.com.br.eventos.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable(tableName = "local")
public class Local implements Serializable {
    private static final long serialVersionUID = 1L;

    @DatabaseField(id = true, generatedId = false)
    private Long id;

    @DatabaseField
    private String nome;

    @DatabaseField
    private String cep;

    @DatabaseField
    private String rua;

    @DatabaseField
    private String bairro;

    @DatabaseField
    private String numero;

    @DatabaseField
    private String longitude;

    @DatabaseField
    private String latitude;

    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private Cidade cidade;

    public Local() {

    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public Cidade getCidade() {
        return cidade;
    }

    public void setCidade(Cidade cidade) {
        this.cidade = cidade;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}