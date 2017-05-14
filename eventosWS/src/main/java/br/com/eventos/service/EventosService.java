package br.com.eventos.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.eventos.dao.EventosDAO;
import br.com.eventos.model.Evento;
import br.com.eventos.model.Filtro;
import br.com.eventos.model.Usuario;
import br.com.eventos.model.dto.EventoFullDTO;
import br.com.eventos.model.dto.EventosFeedDTO;
import br.com.eventos.rest.Response;

@Component
public class EventosService {

	@Autowired
	private EventosDAO db;

	public List<EventosFeedDTO> getProximosEventos(Filtro filtro) {
		List<Evento> eventos = new ArrayList<>();

		/*
		if (filtro.getIdFaculdade() != Long.MAX_VALUE) {
			eventos = db.getEventosPorFaculdade(filtro.getIdFaculdade());
			eventos = filtrarPorData(eventos, filtro);
			return EventosFeedDTO.createListEventoFeedDTO(eventos);
		}

		if (filtro.getIdCidade() != Long.MAX_VALUE) {
			eventos = db.getEventosPorCidade(filtro.getIdCidade());
			eventos = filtrarPorData(eventos, filtro);
			return EventosFeedDTO.createListEventoFeedDTO(eventos);
		}

		if (filtro.getIdEstado() != Long.MAX_VALUE) {
			eventos = db.getEventosPorEstado(filtro.getIdEstado());
			eventos = filtrarPorData(eventos, filtro);
			return EventosFeedDTO.createListEventoFeedDTO(eventos);
		}
*/
		eventos = db.getProximosEventos();
		eventos = filtrarPorData(eventos, filtro);
		return EventosFeedDTO.createListEventoFeedDTO(eventos);
	}

	public List<EventosFeedDTO> getProximosEventos(Filtro filtro, int page, int max) {
		List<Evento> eventos = new ArrayList<>();

		if (filtro.getIdFaculdade() != Long.MAX_VALUE) {
			eventos = db.getEventosPorFaculdade(filtro.getIdFaculdade(), page, max);
			eventos = filtrarPorData(eventos, filtro);
			return EventosFeedDTO.createListEventoFeedDTO(eventos);
		}

		if (filtro.getIdCidade() != Long.MAX_VALUE) {
			eventos = db.getEventosPorCidade(filtro.getIdCidade(), page, max);
			eventos = filtrarPorData(eventos, filtro);
			return EventosFeedDTO.createListEventoFeedDTO(eventos);
		}

		if (filtro.getIdEstado() != Long.MAX_VALUE) {
			eventos = db.getEventosPorEstado(filtro.getIdEstado(), page, max);
			eventos = filtrarPorData(eventos, filtro);
			return EventosFeedDTO.createListEventoFeedDTO(eventos);
		}

		eventos = db.getProximosEventos(page, max);
		eventos = filtrarPorData(eventos, filtro);
		return EventosFeedDTO.createListEventoFeedDTO(eventos);
	}

	private List<Evento> filtrarPorData(List<Evento> eventos, Filtro filtro) {

		if (filtro.getDataInicial() == null) {
			return eventos;
		}

		if (filtro.getDataFinal() != null) {
			Stream<Evento> stream = eventos.stream();
			Stream<Evento> filter = stream.filter(e -> e.getDataHora().after(filtro.getDataInicial())
					&& e.getDataHora().before(filtro.getDataFinal()));
			List<Evento> list = filter.collect(Collectors.toList());
			return list;
		}

		Stream<Evento> stream = eventos.stream();
		Stream<Evento> filter = stream.filter(e -> e.getDataHora().after(filtro.getDataInicial()));
		List<Evento> list = filter.collect(Collectors.toList());
		return list;
	}

	public List<Evento> getTodosEventos() {
		List<Evento> eventos = db.getTodosEventos();
		return eventos;
	}

	public List<EventosFeedDTO> getEventosPorUsuario(Long idUsuario) {
		List<Evento> eventos = db.getEventosPorUsuario(idUsuario);
		return EventosFeedDTO.createListEventoFeedDTO(eventos);
	}

	public EventoFullDTO getEventosPorId(Long idEvento) {
		Evento evento = db.getEventosPorId(idEvento);
		return EventoFullDTO.createEventoFullDTO(evento);
	}

	public void save(Evento e) {
		db.save(e);
	}

	public Response excluir(Usuario u, Long id) {

		Evento e = db.getEventosPorId(id);

		if (u.getId().equals(e.getUsuario().getId())) {
			db.delete(e);
			return Response.Ok("Evento excluindo!");
		}
		return Response.Error("Você não tem permissão para excluir este evento");
	}

	public void update(Evento e) {
		db.update(e);
	}
}
