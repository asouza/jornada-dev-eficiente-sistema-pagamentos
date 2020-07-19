package com.deveficiente.pagamentos.pagamentooffline;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import com.deveficiente.pagamentos.modeladominio.Restaurante;
import com.deveficiente.pagamentos.modeladominio.Usuario;

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

	@Deprecated
	public Pagamento() {

	}

	public Pagamento(@NotNull Long idPedido,
			@NotNull @Positive BigDecimal valor,
			@NotNull @Valid Usuario comprador,
			@NotNull @Valid Restaurante restaurante,
			@NotNull StatusTransacao statusInicial) {
		this.idPedido = idPedido;
		this.valor = valor;
		this.comprador = comprador;
		this.restaurante = restaurante;
		this.transacoes.add(new Transacao(statusInicial));
		this.codigo = UUID.randomUUID().toString();
	}
	
	public String getCodigo() {
		return codigo;
	}

}
