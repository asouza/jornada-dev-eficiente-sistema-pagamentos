package com.deveficiente.pagamentos.pagamentooffline;

import java.util.Optional;

import org.springframework.stereotype.Repository;

@Repository
public interface PagamentoRepository extends org.springframework.data.repository.Repository<Pagamento, Long>{

	public Optional<Pagamento> findByIdPedido(Long idPedido);

	public Optional<Pagamento> findByCodigo(String codigoPagamento);
}
