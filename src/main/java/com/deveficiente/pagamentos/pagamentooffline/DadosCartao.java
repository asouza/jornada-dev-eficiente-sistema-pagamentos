package com.deveficiente.pagamentos.pagamentooffline;

/**
 * Representa os dados de um cartão que pode ter sido salvo junto com um pagamento
 * @author albertoluizsouza
 *
 */
public class DadosCartao {

	private String numero;
	private int codigoSeguranca;
	
	@Deprecated
	public DadosCartao() {

	}

	public DadosCartao(String numero, int codigoSeguranca) {
		super();
		this.numero = numero;
		this.codigoSeguranca = codigoSeguranca;
	}
	
	public String getNumero() {
		return numero;
	}
	
	public int getCodigoSeguranca() {
		return codigoSeguranca;
	}

}
