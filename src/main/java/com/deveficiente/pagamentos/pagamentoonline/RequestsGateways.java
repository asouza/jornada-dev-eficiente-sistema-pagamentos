package com.deveficiente.pagamentos.pagamentoonline;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.deveficiente.pagamentos.outrossistemas.DadosCartaoSeyaRequest;
import com.deveficiente.pagamentos.outrossistemas.DadosCompraSaori;
import com.deveficiente.pagamentos.outrossistemas.DadosCompraSeyaRequest;

@FeignClient(url = "${enderecos-externos.gateways.base-url}", name = "gateways")
public interface RequestsGateways {

	@PostMapping("/seya/verifica")
	int seyaVerifica(DadosCartaoSeyaRequest request);

	@PostMapping(value = "/seya/processa/{codigo}")
	public void seyaProcessa(@PathVariable("codigo") Integer codigo,
			DadosCompraSeyaRequest request);
	
	@PostMapping(value = "/saori/processa")
	public void saoriProcessa(DadosCompraSaori request);	

}
