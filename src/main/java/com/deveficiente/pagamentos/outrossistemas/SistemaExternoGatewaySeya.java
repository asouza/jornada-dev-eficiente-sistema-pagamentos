package com.deveficiente.pagamentos.outrossistemas;

import java.util.HashSet;
import java.util.Set;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class SistemaExternoGatewaySeya implements PodeMeDeixarCaotico{
	
	private Set<Integer> codigosGerados = new HashSet<>();
	
	private static final Logger log = LoggerFactory
			.getLogger(SistemaExternoGatewaySeya.class);


	@PostMapping(value = "/seya/verifica")
	public int verifica(@RequestBody @Valid DadosCartaoSeyaRequest request) {
		log.info("Verificando cartao seya {}",request);
		
		codigosGerados.add(request.getCodigo_seguranca());
		return request.getCodigo_seguranca();
				
	}
	
	@PostMapping(value = "/seya/processa/{codigo}")
	public void processa(@PathVariable("codigo") Integer codigo,@RequestBody @Valid DadosCompraSeyaRequest request) {
		if(!codigosGerados.contains(codigo)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		
		log.info("Processando seya codigo {} com cartao {}",codigo,request);
		
		
	}

}
