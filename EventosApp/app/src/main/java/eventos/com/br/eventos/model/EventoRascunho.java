package eventos.com.br.eventos.model;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.Calendar;

@DatabaseTable(tableName = "evento_rascunho")
public class EventoRascunho implements Serializable {
    private static final long serialVersionUID = 1L;

    @DatabaseField(id = true, generatedId = false)
    private Long id;

    @DatabaseField
    private String nomeEvento;

    @DatabaseField(dataType = DataType.STRING)
    private String descricao;

    @DatabaseField
    private String enderecoImagem;

    @DatabaseField(dataType = DataType.SERIALIZABLE)
    private Calendar dataHora;

    @DatabaseField
    private String nomeAtletica;

    @DatabaseField
    private String nomeLocal;

    @DatabaseField
    private String cep;

    @DatabaseField
    private String rua;

    @DatabaseField
    private String bairro;

    @DatabaseField
    private String numero;

    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private Faculdade faculdade;

    public EventoRascunho() {

    }

    public Long getId() {
        return id;
    }

    public String getNomeEvento() {
        return nomeEvento;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getEnderecoImagem() {
        return enderecoImagem;
    }

    public Calendar getDataHora() {
        return dataHora;
    }

    public String getNomeAtletica() {
        return nomeAtletica;
    }

    public String getNomeLocal() {
        return nomeLocal;
    }

    public String getCep() {
        return cep;
    }

    public String getRua() {
        return rua;
    }

    public String getBairro() {
        return bairro;
    }

    public String getNumero() {
        return numero;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNomeEvento(String nomeEvento) {
        this.nomeEvento = nomeEvento;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setEnderecoImagem(String enderecoImagem) {
        this.enderecoImagem = enderecoImagem;
    }

    public void setDataHora(Calendar dataHora) {
        this.dataHora = dataHora;
    }

    public void setNomeAtletica(String nomeAtletica) {
        this.nomeAtletica = nomeAtletica;
    }

    public void setNomeLocal(String nomeLocal) {
        this.nomeLocal = nomeLocal;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Faculdade getFaculdade() {
        return faculdade;
    }

    public void setFaculdade(Faculdade faculdade) {
        this.faculdade = faculdade;
    }

    @Override
    public String toString() {
        return "EventoRascunho{" +
                "id=" + id +
                ", nomeEvento='" + nomeEvento + '\'' +
                ", descricao='" + descricao + '\'' +
                ", enderecoImagem='" + enderecoImagem + '\'' +
                ", nomeAtletica='" + nomeAtletica + '\'' +
                ", nomeLocal='" + nomeLocal + '\'' +
                ", cep='" + cep + '\'' +
                ", rua='" + rua + '\'' +
                ", bairro='" + bairro + '\'' +
                ", numero='" + numero + '\'' +
                '}';
    }
}
