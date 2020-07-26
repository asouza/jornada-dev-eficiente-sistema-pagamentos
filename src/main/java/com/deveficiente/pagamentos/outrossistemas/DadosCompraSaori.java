package com.deveficiente.pagamentos.outrossistemas;

import java.math.BigDecimal;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import org.hibernate.validator.constraints.CreditCardNumber;

import com.deveficiente.pagamentos.pagamentooffline.DadosCartao;
import com.deveficiente.pagamentos.pagamentooffline.Pagamento;

public class DadosCompraSaori {

	@CreditCardNumber
	private String num_cartao;
	@Min(100)
	@Max(999)
	private int codigo_seguranca;
	@Positive
	@NotNull
	private BigDecimal valor_compra;

	public DadosCompraSaori(@CreditCardNumber String num_cartao,
			@Min(100) @Max(999) int codigo_seguranca,
			@Positive BigDecimal valor_compra) {
		super();
		this.num_cartao = num_cartao;
		this.codigo_seguranca = codigo_seguranca;
		this.valor_compra = valor_compra;
	}

	public DadosCompraSaori(@NotNull @Valid Pagamento pagamento) {
		DadosCartao dadosCartao = pagamento.getDadosCartao();
		this.codigo_seguranca = dadosCartao.getCodigoSeguranca();
		this.num_cartao = dadosCartao.getNumero();
		this.valor_compra = pagamento.getValor();
	}
	
	public BigDecimal getValor_compra() {
		return valor_compra;
	}
	
	public String getNum_cartao() {
		return num_cartao;
	}
	
	public int getCodigo_seguranca() {
		return codigo_seguranca;
	}

	@Override
	public String toString() {
		return "DadosCompraSaori [num_cartao=" + num_cartao
				+ ", codigo_seguranca=" + codigo_seguranca + ", valor_compra="
				+ valor_compra + "]";
	}
	
	

}
