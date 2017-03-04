package br.com.eventos.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.eventos.model.Token;

@Repository
public class TokenDAO {

	@PersistenceContext
	private EntityManager manager;

	public Token getToken() {
		TypedQuery<Token> query = manager.createQuery("from Token", Token.class);
		query.setHint("org.hibernate.cacheable", "true");
		return query.getSingleResult();
	}

	@Transactional
	public void update(Token newToken) {
		manager.merge(newToken);
	}
}
