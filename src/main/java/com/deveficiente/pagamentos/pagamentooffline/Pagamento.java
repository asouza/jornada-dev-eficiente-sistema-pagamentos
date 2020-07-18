package com.deveficiente.pagamentos.pagamentooffline;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Entity
public class Pagamento {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ElementCollection
	private Set<Transacao> transacoes = new HashSet<>();
	private @NotNull Long idPedido;

	public Pagamento(@NotNull Long idPedido,@NotNull @Valid Transacao transacao) {
		this.idPedido = idPedido;
		this.transacoes.add(transacao);
	}
		

}
