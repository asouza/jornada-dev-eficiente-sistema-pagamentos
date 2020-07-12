package com.deveficiente.pagamentos.listapagamentos;

import com.deveficiente.pagamentos.modeladominio.FormaPagamento;
import com.deveficiente.pagamentos.modeladominio.Usuario;

/**
 * Interface que define o contrato para qualquer regra de fraude no sistema
 * @author albertoluizsouza
 *
 */
public interface RegraFraude {

	/**
	 * 
	 * @param formaPagamento
	 * @param usuario
	 * @return true se a regra permitir a combinacao
	 */
	boolean aceita(FormaPagamento formaPagamento, Usuario usuario);

}