package com.deveficiente.pagamentos.pagamentooffline;

import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.servlet.HandlerMapping;

/**
 * Esse validator foi criado para verificar se um id de pedido já
 * existe no sistema. Entretanto ele tem uma falha de design. Está
 * intimamente acoplado com o fato de que o parametro que indica o
 * id do pedido deve vir na url chamando de idPedido
 * @author albertoluizsouza
 *
 */
@Component
public class PagamentoGeradoValidator implements Validator {

	private HttpServletRequest request;
	private PagamentoRepository pagamentoRepository;

	public PagamentoGeradoValidator(HttpServletRequest request,PagamentoRepository pagamentoRepository) {
		super();
		this.request = request;
		this.pagamentoRepository = pagamentoRepository;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return NovoPedidoOfflineRequest.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		if(errors.hasErrors()) {
			return ;
		}
		
		//utilizando o fw a meu favor
		Map<String, String> variaveisUrl = (Map<String, String>) request
				.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
		String paramIdPedido = variaveisUrl.get("idPedido");
		
		//super acoplado com o endereco
		Assert.state(StringUtils.hasText(paramIdPedido), "Para este validator funcionar, o PathVariable que representa o pedido precisa se chamar idPedido");
		Long idPedido = Long.valueOf(variaveisUrl.get("idPedido"));
		
		Optional<Pagamento> possivelPagamento = pagamentoRepository.findByIdPedido(idPedido);
		if(possivelPagamento.isPresent()) {
			errors.reject(null, "Já existe um pagamento iniciado para esse pedido");
		}
	}

}
