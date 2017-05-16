package br.com.eventos.dao;

import java.util.Calendar;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.eventos.model.Cidade;
import br.com.eventos.model.Evento;
import br.com.eventos.model.Faculdade;
import br.com.eventos.model.Filtro;
import br.com.eventos.model.Usuario;

@Repository
public class EventosDAO {

	@PersistenceContext
	private EntityManager manager;

	public List<Evento> getProximosEventos(int page, int max, Filtro filtro) {
		Calendar dataInicial = Calendar.getInstance();
		Calendar dataFinal = filtro.getDataFinal();

		String sql = "";
		if (dataFinal == null) {
			sql = "SELECT NEW br.com.eventos.model.Evento(e.id, e.nome, e.enderecoImagem, e.dataHora, e.local.nome) "
					+ "FROM Evento e WHERE e.dataHora >= :dataInicial ORDER BY e.dataHora";

			if (filtro.getDataInicial() != null) {
				dataInicial = filtro.getDataInicial();
			}
		} else {
			sql = "SELECT NEW br.com.eventos.model.Evento(e.id, e.nome, e.enderecoImagem, e.dataHora, e.local.nome) "
					+ "FROM Evento e WHERE e.dataHora BETWEEN :dataInicial AND :dataFinal ORDER BY e.dataHora";

			if (filtro.getDataInicial() != null) {
				dataInicial = filtro.getDataInicial();
			}
		}

		TypedQuery<Evento> query = manager.createQuery(sql, Evento.class);

		query.setParameter("dataInicial", dataInicial, TemporalType.TIMESTAMP);

		if (dataFinal != null) {
			query.setParameter("dataFinal", dataFinal, TemporalType.TIMESTAMP);
		}

		setPage(query, page, max);

		return query.getResultList();
	}

	public List<Evento> getEventosPorUsuario(Long idUsuario) {

		TypedQuery<Evento> query = manager.createQuery(
				"select new br.com.eventos.model.Evento(e.id, e.nome, e.enderecoImagem, e.dataHora, e.local.nome) "
						+ "from Evento e where e.usuario.id = :idUsuario order by e.dataHora",
				Evento.class);

		query.setParameter("idUsuario", idUsuario);
		return query.getResultList();
	}

	public Evento getEventoPorId(Long idEvento) {
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
		Query query = manager.createQuery("delete from Evento where id = :id");
		query.setParameter("id", e.getId());
		query.executeUpdate();
	}

	@Transactional
	public void update(Evento e) {
		manager.merge(e);
	}

	public List<Evento> getEventosPorFaculdade(Long idFaculdade, int page, int max, Filtro filtro) {

		Calendar dataInicial = Calendar.getInstance();
		Calendar dataFinal = filtro.getDataFinal();

		String sql = "";
		if (dataFinal == null) {
			sql = "SELECT NEW br.com.eventos.model.Evento(e.id, e.nome, e.enderecoImagem, e.dataHora, e.local.nome) "
					+ "FROM Evento e WHERE e.faculdade.id = :idFaculdade AND e.dataHora >= :dataInicial ORDER BY e.dataHora";

			if (filtro.getDataInicial() != null) {
				dataInicial = filtro.getDataInicial();
			}
		} else {
			sql = "SELECT NEW br.com.eventos.model.Evento(e.id, e.nome, e.enderecoImagem, e.dataHora, e.local.nome) "
					+ "FROM Evento e WHERE e.faculdade.id = :idFaculdade AND e.dataHora BETWEEN :dataInicial AND :dataFinal ORDER BY e.dataHora";

			if (filtro.getDataInicial() != null) {
				dataInicial = filtro.getDataInicial();
			}
		}

		TypedQuery<Evento> query = manager.createQuery(sql, Evento.class);

		query.setParameter("idFaculdade", idFaculdade);
		query.setParameter("dataInicial", dataInicial, TemporalType.TIMESTAMP);

		if (dataFinal != null) {
			query.setParameter("dataFinal", dataFinal, TemporalType.TIMESTAMP);
		}

		setPage(query, page, max);

		return query.getResultList();
	}

	public List<Evento> getEventosPorCidade(Long idCidade, int page, int max, Filtro filtro) {

		Calendar dataInicial = Calendar.getInstance();
		Calendar dataFinal = filtro.getDataFinal();

		String sql = "";
		if (dataFinal == null) {
			sql = "SELECT NEW br.com.eventos.model.Evento(e.id, e.nome, e.enderecoImagem, e.dataHora, e.local.nome) "
					+ "FROM Evento e INNER JOIN e.local l ON (l.cidade.id = :idCidade) "
					+ "WHERE e.dataHora >= :dataInicial ORDER BY e.dataHora";

			if (filtro.getDataInicial() != null) {
				dataInicial = filtro.getDataInicial();
			}
		} else {
			sql = "SELECT NEW br.com.eventos.model.Evento(e.id, e.nome, e.enderecoImagem, e.dataHora, e.local.nome) "
					+ "FROM Evento e INNER JOIN e.local l ON (l.cidade.id = :idCidade) "
					+ "WHERE e.dataHora BETWEEN :dataInicial AND :dataFinal ORDER BY e.dataHora";

			if (filtro.getDataInicial() != null) {
				dataInicial = filtro.getDataInicial();
			}
		}

		TypedQuery<Evento> query = manager.createQuery(sql, Evento.class);

		query.setParameter("idCidade", idCidade);
		query.setParameter("dataInicial", dataInicial, TemporalType.TIMESTAMP);

		if (dataFinal != null) {
			query.setParameter("dataFinal", dataFinal, TemporalType.TIMESTAMP);
		}

		setPage(query, page, max);

		return query.getResultList();
	}

	public List<Evento> getEventosPorEstado(Long idEstado, int page, int max, Filtro filtro) {

		Calendar dataInicial = Calendar.getInstance();
		Calendar dataFinal = filtro.getDataFinal();

		String sql = "";
		if (dataFinal == null) {
			sql = "SELECT NEW br.com.eventos.model.Evento(e.id, e.nome, e.enderecoImagem, e.dataHora, e.local.nome) "
					+ "FROM Evento e INNER JOIN e.local l INNER JOIN l.cidade c INNER JOIN c.estado et ON (et.id = :idEstado) "
					+ "WHERE e.dataHora >= :dataInicial ORDER BY e.dataHora";

			if (filtro.getDataInicial() != null) {
				dataInicial = filtro.getDataInicial();
			}
		} else {
			sql = "SELECT NEW br.com.eventos.model.Evento(e.id, e.nome, e.enderecoImagem, e.dataHora, e.local.nome) "
					+ "FROM Evento e INNER JOIN e.local l INNER JOIN l.cidade c INNER JOIN c.estado et ON (et.id = :idEstado) "
					+ "WHERE e.dataHora BETWEEN :dataInicial AND :dataFinal ORDER BY e.dataHora";

			if (filtro.getDataInicial() != null) {
				dataInicial = filtro.getDataInicial();
			}
		}

		TypedQuery<Evento> query = manager.createQuery(sql, Evento.class);

		query.setParameter("idEstado", idEstado);
		query.setParameter("dataInicial", dataInicial, TemporalType.TIMESTAMP);

		if (dataFinal != null) {
			query.setParameter("dataFinal", dataFinal, TemporalType.TIMESTAMP);
		}

		setPage(query, page, max);

		return query.getResultList();
	}

	protected void setPage(Query q, int page, int max) {
		// Paginação
		page = page - 1;
		int firstResult = 0;
		if (page != 0) {
			firstResult = page * max;
		}
		q.setFirstResult(firstResult);
		q.setMaxResults(max);
	}
}
