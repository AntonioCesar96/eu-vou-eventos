package br.com.eventos.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import br.com.eventos.model.Faculdade;

@Repository
public class FaculdadeDAO {

	@PersistenceContext
	private EntityManager manager;

	public List<Faculdade> getFaculdades() {

		TypedQuery<Faculdade> query = manager.createQuery("from Faculdade", Faculdade.class);
		return query.getResultList();
	}
}
