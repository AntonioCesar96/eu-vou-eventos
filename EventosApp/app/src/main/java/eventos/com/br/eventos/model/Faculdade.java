package eventos.com.br.eventos.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable(tableName = "faculdade")
public class Faculdade implements Serializable {
    private static final long serialVersionUID = 1L;

    @DatabaseField(id = true, generatedId = false)
    private Long id;

    @DatabaseField
    private String nome;

    @DatabaseField(foreign = true)
    private Cidade cidade;

    public Faculdade(){

    }

    public Cidade getCidade() {
        return cidade;
    }

    public void setCidade(Cidade cidade) {
        this.cidade = cidade;
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

    @Override
    public String toString() {
        return "Faculdade{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", cidade=" + cidade +
                '}';
    }
}
