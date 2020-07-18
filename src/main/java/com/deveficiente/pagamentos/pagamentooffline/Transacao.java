package com.deveficiente.pagamentos.pagamentooffline;

import java.math.BigDecimal;
import java.util.UUID;

import javax.persistence.Embeddable;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import com.deveficiente.pagamentos.modeladominio.Restaurante;
import com.deveficiente.pagamentos.modeladominio.Usuario;

@Embeddable
public class Transacao {

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
	
	@Deprecated
	public Transacao() {

	}

	public Transacao(@NotNull @Positive BigDecimal valor,
			@NotNull @Valid Usuario usuario, @NotNull @Valid Restaurante restaurante,
			@NotNull StatusTransacao statusTransacao) {
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
		return "Transacao [valor=" + valor
				+ ", usuario=" + usuario + ", restaurante=" + restaurante
				+ ", statusTransacao=" + statusTransacao + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
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
		Transacao other = (Transacao) obj;
		if (uuid == null) {
			if (other.uuid != null)
				return false;
		} else if (!uuid.equals(other.uuid))
			return false;
		return true;
	}
	
	
	
	

}
