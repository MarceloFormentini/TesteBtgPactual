package br.com.btgpactual.orderms.dto;

import org.springframework.data.domain.Page;

public record PaginatinResponse(
		Integer page, 
		Integer pageSize, 
		Long totalElements,
		Integer totalPages) {
	
	public static PaginatinResponse fromPage(Page<?> page) {
		return new PaginatinResponse(
			page.getNumber(),
			page.getSize(),
			page.getTotalElements(),
			page.getTotalPages()
		);
	}

}
