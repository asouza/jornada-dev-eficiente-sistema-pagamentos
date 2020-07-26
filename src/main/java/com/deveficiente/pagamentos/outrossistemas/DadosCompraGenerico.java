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

public class DadosCompraGenerico {

	@CreditCardNumber
	private String num_cartao;
	@Min(100)
	@Max(999)
	private int codigo_seguranca;
	@Positive
	@NotNull
	private BigDecimal valor_compra;

	public DadosCompraGenerico(@CreditCardNumber String num_cartao,
			@Min(100) @Max(999) int codigo_seguranca,
			@Positive BigDecimal valor_compra) {
		super();
		this.num_cartao = num_cartao;
		this.codigo_seguranca = codigo_seguranca;
		this.valor_compra = valor_compra;
	}

	public DadosCompraGenerico(@NotNull @Valid Pagamento pagamento) {
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + codigo_seguranca;
		result = prime * result
				+ ((num_cartao == null) ? 0 : num_cartao.hashCode());
		result = prime * result
				+ ((valor_compra == null) ? 0 : valor_compra.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DadosCompraGenerico other = (DadosCompraGenerico) obj;
		if (codigo_seguranca != other.codigo_seguranca)
			return false;
		if (num_cartao == null) {
			if (other.num_cartao != null)
				return false;
		} else if (!num_cartao.equals(other.num_cartao))
			return false;
		if (valor_compra == null) {
			if (other.valor_compra != null)
				return false;
		} else if (!valor_compra.equals(other.valor_compra))
			return false;
		return true;
	}
	
	
	
	

}
