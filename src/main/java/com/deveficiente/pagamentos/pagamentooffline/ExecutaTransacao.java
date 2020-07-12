package com.deveficiente.pagamentos.pagamentooffline;

import java.util.function.Supplier;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

@Service
public class ExecutaTransacao {

	@Transactional
	public <T> T executa(Supplier<T> supplier) {
		return supplier.get();
	}
}
