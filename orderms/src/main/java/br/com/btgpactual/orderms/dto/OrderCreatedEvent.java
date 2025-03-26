package br.com.btgpactual.orderms.dto;

import java.util.List;

public record OrderCreatedEvent(
		Long codigoPedido,
		Long codigoCliente,
		List<OrderItenEvent> itens) {

}
