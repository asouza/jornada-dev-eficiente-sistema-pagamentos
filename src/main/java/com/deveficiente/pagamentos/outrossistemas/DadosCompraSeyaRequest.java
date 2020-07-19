package com.deveficiente.pagamentos.outrossistemas;

import java.math.BigDecimal;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;

import org.hibernate.validator.constraints.CreditCardNumber;

public class DadosCompraSeyaRequest {

	@CreditCardNumber
	private String num_cartao;
	@Min(100)
	@Max(999)
	private int codigo_seguranca;
	@Positive
	private BigDecimal valor_compra;

	public DadosCompraSeyaRequest(@CreditCardNumber String num_cartao,
			@Min(100) @Max(999) int codigo_seguranca,
			@Positive BigDecimal valor_compra) {
		super();
		this.num_cartao = num_cartao;
		this.codigo_seguranca = codigo_seguranca;
		this.valor_compra = valor_compra;
	}

}
