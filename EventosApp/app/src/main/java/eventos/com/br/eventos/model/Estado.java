package eventos.com.br.eventos.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable(tableName = "estado")
public class Estado implements Serializable {
	private static final long serialVersionUID = 1L;

	@DatabaseField(id = true, generatedId = false)
	private Long id;

	@DatabaseField
	private String nome;

	@DatabaseField
	private String uf;

	@DatabaseField(foreign = true)
	private Pais pais;

	public Estado(){

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

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	public Pais getPais() {
		return pais;
	}

	public void setPais(Pais pais) {
		this.pais = pais;
	}

}
