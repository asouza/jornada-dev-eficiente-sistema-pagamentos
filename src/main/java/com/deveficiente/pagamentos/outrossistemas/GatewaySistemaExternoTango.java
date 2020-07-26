package com.deveficiente.pagamentos.outrossistemas;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GatewaySistemaExternoTango implements PodeMeDeixarCaotico{
	
	private static final Logger log = LoggerFactory
			.getLogger(GatewaySistemaExternoTango.class);


	@PostMapping(value = "/tango/processa")
	public void processa(@RequestBody @Valid DadosCompraGenerico request) {
		log.info("Processando pagamento gateway tango{}",request);
	}
}
