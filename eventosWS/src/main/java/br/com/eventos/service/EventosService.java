package br.com.eventos.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.eventos.dao.EventosDAO;
import br.com.eventos.model.Evento;
import br.com.eventos.model.dto.EventoFullDTO;
import br.com.eventos.model.dto.EventosFeedDTO;

@Component
public class EventosService {

	@Autowired
	private EventosDAO db;

	public List<EventosFeedDTO> getProximosEventos() {

		List<Evento> eventos = db.getProximosEventos();
		return EventosFeedDTO.createListEventoFeedDTO(eventos);
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
	
	public List<EventosFeedDTO> getEventosPorFaculdade(Long idFaculdade) {
		List<Evento> eventos = db.getEventosPorFaculdade(idFaculdade);
		return EventosFeedDTO.createListEventoFeedDTO(eventos);
	}
	
	public List<EventosFeedDTO> getEventosPorCidade(Long idCidade) {
		List<Evento> eventos = db.getEventosPorCidade(idCidade);
		return EventosFeedDTO.createListEventoFeedDTO(eventos);
	}

	public void save(Evento e) {
		db.save(e);
	}

	public void delete(Evento e) {
		db.delete(e);
	}

	public void update(Evento e) {
		db.update(e);
	}
}
