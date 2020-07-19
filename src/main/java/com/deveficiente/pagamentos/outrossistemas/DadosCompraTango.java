package com.deveficiente.pagamentos.outrossistemas;

import java.math.BigDecimal;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;

import org.hibernate.validator.constraints.CreditCardNumber;

public class DadosCompraTango {

	@CreditCardNumber
	private String numero;
	@Min(100)
	@Max(999)
	private int codigo;
	@Positive
	private BigDecimal valor;

	public DadosCompraTango(@CreditCardNumber String numero,
			@Min(100) @Max(999) int codigo,
			@Positive BigDecimal valor) {
		super();
		this.numero = numero;
		this.codigo = codigo;
		this.valor = valor;
	}

	@Override
	public String toString() {
		return "DadosCompraTango [num_cartao=" + numero
				+ ", codigo_seguranca=" + codigo + ", valor_compra="
				+ valor + "]";
	}
	
	

}
