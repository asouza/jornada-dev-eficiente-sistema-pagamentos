package com.deveficiente.pagamentos.compartilhado;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class FacilitadorJackson {

	/**
	 * 
	 * @param source
	 * @return String do objetos serializada para json
	 * @throws RuntimeException caso ocorra algum problema de serialização
	 */
	public static String serializa(@NotNull Object source) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(source);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}		
	}

	/**
	 * 
	 * @param <T> tipo do retorno
	 * @param json json de entrada
	 * @param clazz classe do tipo esperado de retorno
	 * @return objeto montado do tipo <T>
	 */
	public static <T> T desserializa(@NotBlank String json,@NotNull Class<T> clazz) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.readValue(json, clazz);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}
}
