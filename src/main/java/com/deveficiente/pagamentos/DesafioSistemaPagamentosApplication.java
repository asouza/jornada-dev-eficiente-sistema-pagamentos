package com.deveficiente.pagamentos;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.deveficiente.pagamentos.modeladominio.FormaPagamento;
import com.deveficiente.pagamentos.modeladominio.Restaurante;
import com.deveficiente.pagamentos.modeladominio.Usuario;
import com.deveficiente.pagamentos.outrossistemas.PodeMeDeixarCaoticoInterceptor;

@SpringBootApplication
@EnableFeignClients
public class DesafioSistemaPagamentosApplication implements CommandLineRunner,WebMvcConfigurer {

	public static void main(String[] args) {
		SpringApplication.run(DesafioSistemaPagamentosApplication.class, args);
	}
	
	@Autowired
	private Environment env;
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new PodeMeDeixarCaoticoInterceptor(env));
	}

	@PersistenceContext
	private EntityManager manager;

	@Override
	@Transactional
	public void run(String... args) throws Exception {
		Usuario seya = new Usuario("seya@deveficiente.com", FormaPagamento.visa,
				FormaPagamento.master,FormaPagamento.hipercard);
		manager.persist(seya);

		Usuario shun = new Usuario("shun@deveficiente.com", FormaPagamento.visa,
				FormaPagamento.elo, FormaPagamento.dinheiro);
		manager.persist(shun);

		Usuario saori = new Usuario("saori@deveficiente.com",
				FormaPagamento.visa, FormaPagamento.master,
				FormaPagamento.maquineta);
		manager.persist(saori);

		Restaurante sagaSantuario = new Restaurante("saga santuario",
				FormaPagamento.visa, FormaPagamento.master, FormaPagamento.elo);
		manager.persist(sagaSantuario);
		
		Restaurante sagaDeuses = new Restaurante("saga deuses",
				FormaPagamento.elo,FormaPagamento.hipercard, FormaPagamento.elo);
		manager.persist(sagaDeuses);
		
		Restaurante sagaPoseydon = new Restaurante("saga poseydon",
				FormaPagamento.elo,FormaPagamento.dinheiro,FormaPagamento.maquineta);
		manager.persist(sagaPoseydon);
	}

}
