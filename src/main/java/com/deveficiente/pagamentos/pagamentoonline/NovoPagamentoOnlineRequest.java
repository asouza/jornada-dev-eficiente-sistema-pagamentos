package com.deveficiente.pagamentos.pagamentoonline;

import java.math.BigDecimal;

import javax.persistence.EntityManager;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.CreditCardNumber;

import com.deveficiente.pagamentos.compartilhado.ExistsId;
import com.deveficiente.pagamentos.modeladominio.FormaPagamento;
import com.deveficiente.pagamentos.modeladominio.Restaurante;
import com.deveficiente.pagamentos.modeladominio.Usuario;
import com.deveficiente.pagamentos.pagamentooffline.Pagamento;
import com.deveficiente.pagamentos.pagamentooffline.StatusTransacao;

public class NovoPagamentoOnlineRequest implements TemCombinacaoUsuarioRestauranteFormaPagamento {

	@NotNull
	private FormaPagamento formaPagamento;
	@NotNull
	@ExistsId(domainClass = Restaurante.class, fieldName = "id")
	private Long idRestaurante;
	@NotNull
	@ExistsId(domainClass = Usuario.class, fieldName = "id")
	private Long idUsuario;
	@CreditCardNumber
	@NotBlank
	private String numeroCartao;
	@Min(100)
	@Max(999)
	private int codigoSeguranca;
	
	public NovoPagamentoOnlineRequest(@NotNull FormaPagamento formaPagamento,
			@NotNull Long idRestaurante, @NotNull Long idUsuario,
			@CreditCardNumber String numeroCartao,
			@Min(100) @Max(999) int codigoSeguranca) {
		super();
		this.formaPagamento = formaPagamento;
		this.idRestaurante = idRestaurante;
		this.idUsuario = idUsuario;
		this.numeroCartao = numeroCartao;
		this.codigoSeguranca = codigoSeguranca;
	}
	
	@Override
	public Long getIdRestaurante() {
		return idRestaurante;
	}
	
	@Override
	public Long getIdUsuario() {
		// TODO Auto-generated method stub
		return idUsuario;
	}
	
	@Override
	public FormaPagamento getFormaPagamento() {
		return formaPagamento;
	}

	public boolean pagamentoOnline() {
		return formaPagamento.online;
	}

	public Pagamento toPagamento(Long idPedido, BigDecimal valor,
			EntityManager manager) {
		@NotNull @Valid Usuario comprador = manager.find(Usuario.class, idUsuario);
		@NotNull @Valid Restaurante restaurante = manager.find(Restaurante.class, idRestaurante);
		
		return new Pagamento(idPedido, valor,formaPagamento ,comprador, restaurante, StatusTransacao.esperando_confirmacao_pagamento);
	}
	
	

}
