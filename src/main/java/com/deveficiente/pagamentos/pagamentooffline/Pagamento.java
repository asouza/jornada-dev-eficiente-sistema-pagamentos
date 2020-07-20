package com.deveficiente.pagamentos.pagamentooffline;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

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

import com.deveficiente.pagamentos.modeladominio.FormaPagamento;
import com.deveficiente.pagamentos.modeladominio.Restaurante;
import com.deveficiente.pagamentos.modeladominio.Usuario;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Entity
public class Pagamento {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ElementCollection
	private Set<Transacao> transacoes = new HashSet<>();
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

	public Pagamento(@NotNull Long idPedido,
			@NotNull @Positive BigDecimal valor,
			@NotNull FormaPagamento formaPagamento, @NotNull @Valid Usuario comprador,
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
		Assert.state(!foiConcluido(),"Você não pode concluir uma compra que já foi concluída");
		this.transacoes.add(new Transacao(StatusTransacao.concluida));
	}

	public boolean foiConcluido() {
		return this.transacoes.stream().anyMatch(
				transacao -> transacao.temStatus(StatusTransacao.concluida));
	}

	/**
	 * 
	 * @param informacaoAdicional um mapa que vai ser serializado para json
	 */
	public void setInfoAdicional(Map<String, Object> informacaoAdicional) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			this.infoAdicional = mapper.writeValueAsString(informacaoAdicional);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}


}
