package br.com.eventos.dao;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.eventos.model.Faculdade;
import br.com.eventos.model.Usuario;

@Repository
public class UsuarioDAO {

	@PersistenceContext
	private EntityManager manager;

	@Transactional
	public Usuario save(Usuario u) {
		if (u.getFaculdade() != null && u.getFaculdade().getId() != null) {
			Faculdade faculdade = manager.find(Faculdade.class, u.getFaculdade().getId());
			u.setFaculdade(faculdade);
		}

		manager.persist(u);

		return getPorId(u);
	}

	public Usuario logar(String email, String senha) {
		
		String sql = "from Usuario u join fetch u.faculdade where u.email = :email and u.senha = :senha";
		
		TypedQuery<Usuario> query = manager.createQuery(sql, Usuario.class);
		query.setParameter("email", email);
		query.setParameter("senha", senha);

		Usuario usuario;
		try {
			usuario = query.getSingleResult();
		} catch (NoResultException e) {
			usuario = new Usuario();
		}
		return usuario;
	}

	@Transactional
	public Usuario update(Usuario u) {
		manager.merge(u);

		return getPorId(u);
	}

	private Usuario getPorId(Usuario u) {
		String sql = "from Usuario u join fetch u.faculdade where u.id = :id";

		TypedQuery<Usuario> query = manager.createQuery(sql, Usuario.class);
		query.setParameter("id", u.getId());
		return query.getSingleResult();
	}
}
