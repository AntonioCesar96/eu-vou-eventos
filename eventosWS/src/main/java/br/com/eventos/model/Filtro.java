package br.com.eventos.model;

import java.io.Serializable;
import java.util.Calendar;

public class Filtro implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private Long idFaculdade;
	private Long idCidade;
	private Long idEstado;
	private Calendar dataInicial;
	private Calendar dataFinal;

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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getIdEstado() {
		return idEstado;
	}

	public void setIdEstado(Long idEstado) {
		this.idEstado = idEstado;
	}
	
	public static void main(String[] args) {
		System.out.println(Long.MAX_VALUE);
	}
}
