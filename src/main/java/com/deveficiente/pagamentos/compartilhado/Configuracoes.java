package com.deveficiente.pagamentos.compartilhado;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class Configuracoes {

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
