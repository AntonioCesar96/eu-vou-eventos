package br.com.eventos.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.eventos.dao.EventosDAO;
import br.com.eventos.model.Evento;
import br.com.eventos.model.Filtro;
import br.com.eventos.model.dto.EventoFullDTO;
import br.com.eventos.model.dto.EventosFeedDTO;
import javassist.expr.NewArray;

@Component
public class EventosService {

	@Autowired
	private EventosDAO db;

	public List<EventosFeedDTO> getProximosEventos(Filtro filtro) {
		List<Evento> eventos = new ArrayList<>();

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

		eventos = db.getProximosEventos();
		eventos = filtrarPorData(eventos, filtro);
		return EventosFeedDTO.createListEventoFeedDTO(eventos);
	}

	private List<Evento> filtrarPorData(List<Evento> eventos, Filtro filtro) {

		if (filtro.getDataInicial() == null && filtro.getDataFinal() == null) {
			return eventos;
		}

		eventos.forEach(
				e -> System.out.println(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(e.getDataHora().getTime())));
		System.out.println("Data Inicial "
				+ new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(filtro.getDataInicial().getTime()));
		System.out.println(
				"Data Final " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(filtro.getDataFinal().getTime()));

		Stream<Evento> stream = eventos.stream();
		Stream<Evento> filter = stream.filter(
				e -> e.getDataHora().after(filtro.getDataInicial()) && e.getDataHora().before(filtro.getDataFinal()));
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

	public void delete(Evento e) {
		db.delete(e);
	}

	public void update(Evento e) {
		db.update(e);
	}
}
