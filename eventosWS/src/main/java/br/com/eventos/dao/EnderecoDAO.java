package br.com.eventos.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import br.com.eventos.model.Cidade;
import br.com.eventos.model.Estado;

@Repository
public class EnderecoDAO {

	@PersistenceContext
	private EntityManager manager;

	public List<Estado> getEstados() {

		TypedQuery<Estado> query = manager.createQuery("from Estado", Estado.class);
		return query.getResultList();
	}

	public List<Cidade> getCidades(Long idEstado) {

		TypedQuery<Cidade> query = manager.createQuery("from Cidade where estado.id = :idEstado", Cidade.class);
		query.setParameter("idEstado", idEstado);
		return query.getResultList();
	}
}
