package com.deveficiente.pagamentos.pagamentooffline;

import java.math.BigDecimal;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
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
public class Transacao {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private @NotNull Long idPedido;
	private @NotNull @Positive BigDecimal valor;
	@NotNull @Valid
	@ManyToOne
	private Usuario usuario;
	@NotNull @Valid
	@ManyToOne
	private Restaurante restaurante;
	@Enumerated
	private StatusTransacao statusTransacao;
	private String uuid;

	public Transacao(@NotNull Long idPedido, @NotNull @Positive BigDecimal valor,
			@NotNull @Valid Usuario usuario, @NotNull @Valid Restaurante restaurante,
			@NotNull StatusTransacao statusTransacao) {
				this.idPedido = idPedido;
				this.valor = valor;
				this.usuario = usuario;
				this.restaurante = restaurante;
				this.statusTransacao = statusTransacao;
				this.uuid = UUID.randomUUID().toString();
	}

	public String getUuid() {
		return uuid;
	}
	
	@Override
	public String toString() {
		return "Transacao [idPedido=" + idPedido + ", valor=" + valor
				+ ", usuario=" + usuario + ", restaurante=" + restaurante
				+ ", statusTransacao=" + statusTransacao + "]";
	}
	
	

}
