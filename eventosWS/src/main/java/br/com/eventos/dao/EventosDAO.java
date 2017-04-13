package br.com.eventos.dao;

import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.eventos.model.Cidade;
import br.com.eventos.model.Evento;
import br.com.eventos.model.Faculdade;
import br.com.eventos.model.Usuario;

@Repository
public class EventosDAO {

	@PersistenceContext
	private EntityManager manager;

	public List<Evento> getTodosEventos() {
		TypedQuery<Evento> query = manager.createQuery(
				"select new br.com.eventos.model.Evento(e.id, e.nome, e.enderecoImagem, e.dataHora, e.local.nome) "
						+ "from Evento e order by e.dataHora",
				Evento.class);
		// query.setHint("org.hibernate.cacheable", "true");
		return query.getResultList();
	}

	public List<Evento> getProximosEventos() {
		TypedQuery<Evento> query = manager.createQuery(
				"select new br.com.eventos.model.Evento(e.id, e.nome, e.enderecoImagem, e.dataHora, e.local.nome) "
						+ "from Evento e order by e.dataHora",
				Evento.class);
		// query.setHint("org.hibernate.cacheable", "true");

		List<Evento> list = query.getResultList();

		Calendar dataAtual = Calendar.getInstance();
		return list.stream().filter(e -> e.getDataHora().after(dataAtual)).collect(Collectors.toList());
	}

	public List<Evento> getEventosPorUsuario(Long idUsuario) {

		TypedQuery<Evento> query = manager.createQuery(
				"select new br.com.eventos.model.Evento(e.id, e.nome, e.enderecoImagem, e.dataHora, e.local.nome) "
						+ "from Evento e where e.usuario.id = :idUsuario order by e.dataHora",
				Evento.class);

		query.setParameter("idUsuario", idUsuario);
		return query.getResultList();
	}

	public Evento getEventosPorId(Long idEvento) {
		TypedQuery<Evento> query = manager
				.createQuery("from Evento e join fetch e.usuario join fetch e.faculdade join fetch e.local "
						+ "join fetch e.local where e.id = :idEvento", Evento.class);
		query.setParameter("idEvento", idEvento);
		return query.getSingleResult();
	}

	@Transactional
	public void save(Evento e) {
		Usuario usuario = manager.find(Usuario.class, e.getUsuario().getId());
		e.setUsuario(usuario);

		Faculdade faculdade = manager.find(Faculdade.class, e.getFaculdade().getId());
		e.setFaculdade(faculdade);

		Cidade cidade = manager.find(Cidade.class, e.getLocal().getCidade().getId());
		e.getLocal().setCidade(cidade);

		manager.persist(e.getLocal());
		manager.persist(e);
	}

	@Transactional
	public void delete(Evento e) {
		manager.remove(e);
	}

	@Transactional
	public void update(Evento e) {
		manager.merge(e);
	}

	public List<Evento> getEventosPorFaculdade(Long idFaculdade) {
		TypedQuery<Evento> query = manager.createQuery(
				"select new br.com.eventos.model.Evento(e.id, e.nome, e.enderecoImagem, e.dataHora, e.local.nome) "
						+ "from Evento e where e.faculdade.id = :idFaculdade order by e.dataHora",
				Evento.class);

		query.setParameter("idFaculdade", idFaculdade);

		List<Evento> list = query.getResultList();

		Calendar dataAtual = Calendar.getInstance();
		return list.stream().filter(e -> e.getDataHora().after(dataAtual)).collect(Collectors.toList());
	}

	public List<Evento> getEventosPorCidade(Long idCidade) {
		TypedQuery<Evento> query = manager.createQuery(
				"select new br.com.eventos.model.Evento(e.id, e.nome, e.enderecoImagem, e.dataHora, e.local.nome) "
						+ "from Evento e inner join e.local l on (l.cidade.id = :idCidade) order by e.dataHora",
				Evento.class);

		query.setParameter("idCidade", idCidade);

		List<Evento> list = query.getResultList();

		Calendar dataAtual = Calendar.getInstance();
		return list.stream().filter(e -> e.getDataHora().after(dataAtual)).collect(Collectors.toList());
	}

	public List<Evento> getEventosPorEstado(Long idEstado) {
		TypedQuery<Evento> query = manager.createQuery(
				"select new br.com.eventos.model.Evento(e.id, e.nome, e.enderecoImagem, e.dataHora, e.local.nome) from Evento e "
						+ "inner join e.local l inner join l.cidade c inner join c.estado et on (et.id = :idEstado) order by e.dataHora",
				Evento.class);

		query.setParameter("idEstado", idEstado);

		List<Evento> list = query.getResultList();

		Calendar dataAtual = Calendar.getInstance();
		return list.stream().filter(e -> e.getDataHora().after(dataAtual)).collect(Collectors.toList());
	}
}
