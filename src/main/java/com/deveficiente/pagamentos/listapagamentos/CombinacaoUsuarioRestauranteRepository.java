package com.deveficiente.pagamentos.listapagamentos;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.deveficiente.pagamentos.modeladominio.CombinacaoUsuarioRestaurante;

@Repository
public interface CombinacaoUsuarioRestauranteRepository extends org.springframework.data.repository.Repository<CombinacaoUsuarioRestaurante, Long>{

	@Query("select count(1) from CombinacaoUsuarioRestaurante comb where comb.usuario.id=:usuarioId and comb.restaurante.id=:restauranteId")
	public long contaSelecaoUsuarioRestaurante(@Param("usuarioId") Long usuarioId,@Param("restauranteId") Long restauranteId);
}
