package com.deveficiente.pagamentos.listapagamentos;

/**
 * Interface para servir de especializacao em relação a selecao de restaurante por parte do usuario
 * @author albertoluizsouza
 *
 */
public interface ContaSelecaoUsuarioRestaurante {

	public long contaSelecaoUsuarioRestaurante(Long usuarioId,Long restauranteId);
}
