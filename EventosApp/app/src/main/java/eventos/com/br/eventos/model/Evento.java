package eventos.com.br.eventos.model;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

@DatabaseTable(tableName = "evento")
public class Evento implements Serializable {
    private static final long serialVersionUID = 1L;

    @DatabaseField(id = true, generatedId = false)
    private Long id;

    @DatabaseField
    private String nome;

    @DatabaseField(dataType = DataType.STRING)
    private String descricao;

    @DatabaseField
    private String enderecoImagem;

    @DatabaseField(dataType = DataType.SERIALIZABLE)
    private Calendar dataHora;

    @DatabaseField
    private String nomeAtletica;

    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private Usuario usuario;

    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private Faculdade faculdade;

    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private Local local;

    public Evento() {

    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getNomeAtletica() {
        return nomeAtletica;
    }

    public void setNomeAtletica(String nomeAtletica) {
        this.nomeAtletica = nomeAtletica;
    }

    public Long getId() {
        return id;
    }

    public Faculdade getFaculdade() {
        return faculdade;
    }

    public void setFaculdade(Faculdade faculdade) {
        this.faculdade = faculdade;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEnderecoImagem() {
        return enderecoImagem;
    }

    public void setEnderecoImagem(String enderecoImagem) {
        this.enderecoImagem = enderecoImagem;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Calendar getDataHora() {
        return dataHora;
    }

    public void setDataHora(Calendar dataHora) {
        this.dataHora = dataHora;
    }

    public Local getLocal() {
        return local;
    }

    public void setLocal(Local local) {
        this.local = local;
    }

    @Override
    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        return "Evento{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", descricao='" + descricao + '\'' +
                ", enderecoImagem='" + enderecoImagem + '\'' +
                ", dataHora=" + dateFormat.format(dataHora.getTime()) +
                ", nomeAtletica='" + nomeAtletica + '\'' +
                ", usuario=" + usuario +
                ", faculdade=" + faculdade +
                ", local=" + local +
                '}';
    }
}
