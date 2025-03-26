package br.com.btgpactual.orderms.dto;

import java.math.BigDecimal;

public record OrderItenEvent(
		String produto, 
		Integer quantidade,
		BigDecimal preco) {

}
