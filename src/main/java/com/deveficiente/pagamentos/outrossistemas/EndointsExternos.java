package com.deveficiente.pagamentos.outrossistemas;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Random;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EndointsExternos {
	

	@GetMapping("/api/pedidos/{idPedido}/valor")
	public Map<String, Object> valorPedido(Long idPedido) {
		String[] valores = new String[]{"50.00", "150.00","60","200"};
		int posicao = new Random().nextInt(4);
		return Map.of("valor",new BigDecimal(valores[posicao])); 
	}
}
