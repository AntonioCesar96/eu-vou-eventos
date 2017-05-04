package br.com.eventos.model;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Table(name = "evento")
public class Evento implements Serializable {
	private static final long serialVersionUID = 1L;
 
	@Id 
	@Column(unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String nome;

	@Column(columnDefinition = "TEXT")
	private String descricao;

	@Column(name = "endereco_imagem")
	private String enderecoImagem;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_hora")
	private Calendar dataHora;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_usuario")
	private Usuario usuario;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_faculdade")
	private Faculdade faculdade;

	private String nomeAtletica;

	@ManyToOne(cascade = { CascadeType.ALL })
	@JoinColumn(name = "id_local")
	private Local local;

	public Evento(Long id, String nome, String enderecoImagem, Calendar dataHora, String nomeLocal) {
		this.id = id;
		this.nome = nome;
		this.enderecoImagem = enderecoImagem;
		this.dataHora = dataHora;

		this.local = new Local();
		this.local.setNome(nomeLocal);
	}

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
		return "Evento [id=" + id + ", nome=" + nome + ", descricao=" + descricao + ", enderecoImagem=" + enderecoImagem
				+ ", dataHora=" + dataHora + ", usuario=" + usuario + ", faculdade=" + faculdade + ", nomeAtletica="
				+ nomeAtletica + ", local=" + local + "]";
	}

}
