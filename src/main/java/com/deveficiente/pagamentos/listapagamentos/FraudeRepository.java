package com.deveficiente.pagamentos.listapagamentos;

import java.util.Optional;

import org.springframework.stereotype.Repository;

@Repository
public interface FraudeRepository extends org.springframework.data.repository.Repository<Fraude, Long>{
	
	public Optional<Fraude> findByEmail(String email);

}
