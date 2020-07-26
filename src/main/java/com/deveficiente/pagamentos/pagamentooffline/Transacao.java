package com.deveficiente.pagamentos.pagamentooffline;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;

import com.deveficiente.pagamentos.compartilhado.FacilitadorJackson;
import com.deveficiente.pagamentos.pagamentoonline.gateways.Gateway;

@Embeddable
public class Transacao {

	@NotNull
	private StatusTransacao statusTransacao;
	@NotBlank
	private String codigo;
	@NotNull
	@PastOrPresent
	private LocalDateTime instante;
	private String informacaoAdicional;

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

	public void setInfoAdicional(Map<String, Object> infoAdicional) {
		this.informacaoAdicional = FacilitadorJackson.serializa(infoAdicional);
	}

	public static Transacao concluida(Gateway gateway) {
		Transacao transacao = new Transacao(StatusTransacao.concluida);
		transacao.informacaoAdicional = FacilitadorJackson
				.serializa(Map.of("gateway", gateway.toString()));
		return transacao;
	}

}
