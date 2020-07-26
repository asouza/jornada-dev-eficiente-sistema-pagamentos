package com.deveficiente.pagamentos.pagamentoonline;

import java.util.Arrays;

import org.springframework.util.Assert;

import com.deveficiente.pagamentos.pagamentooffline.Transacao;

/**
 * Indica o resultado de uma operação. Inspirado no Either do Scala.
 * @author albertoluizsouza
 *
 * @param <E> Tipo que representa o erro
 * @param <S> Tipo que representa o sucesso
 */
public class Resultado<E extends Exception, S> {

	private E erro;
	private S sucesso;
	
	private Resultado() {
	}

	public boolean temErro() {
		return erro!=null;
	}

	public E getException() {
		Assert.isNull(sucesso, "Não pode ter sucesso para ter erro");
		Assert.isTrue(temErro(),"Você só deveria buscar por quando tiver dado erro");
		return erro;
	}
	
	/**
	 * 
	 * @return stackTrace como uma String
	 */
	public String getStackTrace() {
		Assert.isNull(sucesso, "Não pode ter sucesso para ter erro");
		Assert.isTrue(temErro(),"Você só deveria buscar por quando tiver dado erro");
		return Arrays.toString(erro.getStackTrace());
	}

	public S get() {
		Assert.isTrue(!temErro(),"Não pode ter tido erro para ter tido sucesso");
		return sucesso;
	}

	public static <T> Resultado<Exception, T> sucesso(T objeto) {
		Resultado<Exception, T> resultado = new Resultado<Exception,T>();
		resultado.sucesso = objeto;
		return resultado;
	}

	public static <E extends Exception,T> Resultado<E, T> erro(E exception) {
		Resultado<E, T> resultado = new Resultado<E,T>();
		resultado.erro = exception;
		return resultado;
	}

}
