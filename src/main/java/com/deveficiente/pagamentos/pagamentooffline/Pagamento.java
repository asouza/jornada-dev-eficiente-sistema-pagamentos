package com.deveficiente.pagamentos.pagamentooffline;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import org.hibernate.validator.constraints.CreditCardNumber;
import org.springframework.util.Assert;

import com.deveficiente.pagamentos.compartilhado.FacilitadorJackson;
import com.deveficiente.pagamentos.modeladominio.FormaPagamento;
import com.deveficiente.pagamentos.modeladominio.Restaurante;
import com.deveficiente.pagamentos.modeladominio.Usuario;

@Entity
public class Pagamento {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ElementCollection
	private Set<Transacao> transacoes = new HashSet<>();
	@Column(unique = true)
	private @NotNull Long idPedido;
	private @NotNull @Positive BigDecimal valor;
	@ManyToOne
	private @NotNull @Valid Usuario comprador;
	@ManyToOne
	private @NotNull @Valid Restaurante restaurante;
	@NotNull
	private String codigo;
	private @NotNull FormaPagamento formaPagamento;
	private String infoAdicional;

	@Deprecated
	public Pagamento() {

	}

	private Pagamento(@NotNull Long idPedido,
			@NotNull @Positive BigDecimal valor,
			@NotNull FormaPagamento formaPagamento,
			@NotNull @Valid Usuario comprador,
			@NotNull @Valid Restaurante restaurante,
			@NotNull StatusTransacao statusInicial) {
		this.idPedido = idPedido;
		this.valor = valor;
		this.formaPagamento = formaPagamento;
		this.comprador = comprador;
		this.restaurante = restaurante;
		this.transacoes.add(new Transacao(statusInicial));
		this.codigo = UUID.randomUUID().toString();
	}

	public String getCodigo() {
		return codigo;
	}

	public void conclui() {
		Assert.state(!foiConcluido(),
				"Você não pode concluir uma compra que já foi concluída");
		this.transacoes.add(new Transacao(StatusTransacao.concluida));
	}

	public boolean foiConcluido() {
		return this.transacoes.stream().anyMatch(
				transacao -> transacao.temStatus(StatusTransacao.concluida));
	}

	public static Pagamento cartao(Long idPedido, BigDecimal valor,
			@NotNull FormaPagamento formaPagamento,
			@CreditCardNumber @NotBlank String numeroCartao,
			@Min(100) @Max(999) int codigoSeguranca,
			@NotNull @Valid Usuario comprador,
			@NotNull @Valid Restaurante restaurante,
			StatusTransacao statusTransacao) {
		Assert.isTrue(formaPagamento.online,
				"Forma pagamento aqui precisa ser online");

		Pagamento pagamento = new Pagamento(idPedido, valor, formaPagamento,
				comprador, restaurante, statusTransacao);

		pagamento.infoAdicional = FacilitadorJackson.serializa(Map.of("numero",
				numeroCartao, "codigoSeguranca", codigoSeguranca));
		return pagamento;
	}

	public static Pagamento offline(Long idPedido, BigDecimal valor,
			@NotNull FormaPagamento formaPagamento,
			@NotNull @Valid Usuario comprador,
			@NotNull @Valid Restaurante restaurante,
			StatusTransacao statusTransacao) {
		Assert.isTrue(!formaPagamento.online,
				"Forma pagamento aqui precisa ser offline");

		Pagamento pagamento = new Pagamento(idPedido, valor, formaPagamento,
				comprador, restaurante, statusTransacao);
		return pagamento;
	}

	public void adicionaTransacao(Transacao novaTransacao) {
		Assert.state(
				transacoes.stream()
						.noneMatch(transacao -> transacao
								.temStatus(StatusTransacao.concluida)),
				"Não pode adicionar transacao quando já tem uma marcando que concluiu");
		
			Assert.state(this.transacoes.add(novaTransacao),"A transação sendo adicionada já existe no pagamento => "+novaTransacao);
	}

	public FormaPagamento getFormaPagamento() {
		return this.formaPagamento;
	}

	public DadosCartao getDadosCartao() {
		Assert.isTrue(formaPagamento.online,"Não tem dado de cartão para forma de pagamento que não é online");
		Assert.hasText(infoAdicional, "Você deveria ter adicionado informacao adicional relativa ao cartao");
		
		return FacilitadorJackson.desserializa(infoAdicional,DadosCartao.class);
	}

	public BigDecimal getValor() {
		return valor;
	}

}
