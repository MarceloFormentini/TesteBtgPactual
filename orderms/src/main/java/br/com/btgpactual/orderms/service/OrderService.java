package br.com.btgpactual.orderms.service;

import java.math.BigDecimal;
import java.util.List;

import org.bson.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import br.com.btgpactual.orderms.dto.OrderCreatedEvent;
import br.com.btgpactual.orderms.dto.OrderResponse;
import br.com.btgpactual.orderms.entity.OrderEntity;
import br.com.btgpactual.orderms.entity.OrderItem;
import br.com.btgpactual.orderms.repository.OrderRepository;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Service
public class OrderService {

	private final OrderRepository orderRepository;
	private final MongoTemplate mongoTemplate;
	
	public OrderService(OrderRepository orderRepository, MongoTemplate mongoTemplate) {
		this.orderRepository = orderRepository;
		this.mongoTemplate = mongoTemplate;
	}
	
	public void save(OrderCreatedEvent event) {
		var entity = new OrderEntity();
		entity.setOrderId(event.codigoPedido());
		entity.setCustomerId(event.codigoCliente());
		entity.setItems(getOrderItems(event));
		entity.setTotal(getTotal(event));
		
		orderRepository.save(entity);
	}
	
	private BigDecimal getTotal(OrderCreatedEvent event) {
		return event.itens().stream()
				.map(i -> i.preco().multiply(BigDecimal.valueOf(i.quantidade())))
				.reduce(BigDecimal::add)
				.orElse(BigDecimal.ZERO);
	}

	private static List<OrderItem> getOrderItems(OrderCreatedEvent event){
		return event.itens().stream()
				.map(i -> new OrderItem(i.produto(), i.quantidade(), i.preco()))
				.toList();
	}
	
	public Page<OrderResponse> findAllByCustomerId(Long customerId, PageRequest pageResquest){
		var orders = orderRepository.findAllByCustomerId(customerId, pageResquest);
		
		return orders.map(OrderResponse::fromEntity);
	}
	
	public BigDecimal findTotalOnOrdersByCustomerId(Long customerId) {
		var aggregations = newAggregation(
			match(Criteria.where("customerId").is(customerId)),
			group().sum("total").as("total")
		);
		
		var response = mongoTemplate.aggregate(aggregations, "orders", Document.class);
		
		return new BigDecimal(response.getUniqueMappedResult().get("total").toString());
	}
}
