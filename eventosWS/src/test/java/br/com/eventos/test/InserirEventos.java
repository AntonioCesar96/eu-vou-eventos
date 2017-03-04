package br.com.eventos.test;

import java.util.Calendar;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import br.com.eventos.model.Evento;
import br.com.eventos.model.Local;
import br.com.eventos.model.Usuario;

public class InserirEventos {

	public static void main(String[] args) throws Exception {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		EntityManagerFactory factory = context.getBean(EntityManagerFactory.class);
		EntityManager manager = factory.createEntityManager();

		manager.getTransaction().begin();

		Usuario usuario = new Usuario();
		usuario.setNome("Antonio Cesar");
		usuario.setEmail("antoniocss19@gmail.com");
		usuario.setSenha("12345");

		manager.persist(usuario);

		for (int i = 0; i < 10; i++) {
			Evento e = new Evento();
			e.setUsuario(usuario);
			e.setNome("Nome do evento " + i);
			e.setDescricao("Essa é a descrição do evento, aqui sera descrito tudo que acontecera no evento " + i);

			Calendar dataInicio = Calendar.getInstance();
			dataInicio.set(Calendar.HOUR, i);
			dataInicio.set(Calendar.DAY_OF_MONTH, i);
			e.setDataHora(dataInicio);

			Local local = new Local();
			local.setNome("Casa " + i);
			local.setBairro("Jardim Aero Rancho");
			local.setCep("79083-590");
			//local.setCidade("Campo Grande");
			//local.setEstado("MS");
			local.setNumero("112");
			local.setRua("Rua Cajazeiras");

			e.setLocal(local);

			manager.persist(local);
			manager.persist(e);
		}

		manager.getTransaction().commit();
		manager.close();
		factory.close();
	}
}
