package com.deveficiente.pagamentos.pagamentooffline;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;

@Embeddable
public class Transacao {

	@NotNull
	private StatusTransacao statusTransacao;
	@NotBlank
	private String codigo;
	@NotNull
	@PastOrPresent
	private LocalDateTime instante;

	@Deprecated
	public Transacao() {

	}

	public Transacao(@NotNull StatusTransacao statusTransacao) {
		this.statusTransacao = statusTransacao;
		this.codigo = UUID.randomUUID().toString();
		this.instante = LocalDateTime.now();
	}

	
	@Override
	public String toString() {
		return "Transacao [statusTransacao=" + statusTransacao + ", codigo="
				+ codigo + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
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
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		return true;
	}

	public boolean temStatus(StatusTransacao status) {
		return this.statusTransacao.equals(status);
	}

}
