# DESAFIO BACKEND com Java, Spring Boot, RabbitMQ e MongoDB
Resolver o desafio com:
- um microserviço com Spring Boot;
- consumir uma fila do RabbitMQ;
- comunicar com o banco de dados MongoDB utilizando o Docker.
- Mapear uma collection no MongoDB com Spring
- fazer aggregation no MongoDb com Spring;
- logs com o SLF4J;

# Problema
Construir um microsserviço que seja capaz de:
- Processar pedidos a partir de uma fila do RabbitMQ;
- Criar uma API Rest que permita consultar:
  - Valor total de um pedido;
  - Quantidade de pedidos por cliente;
  - Lista de pedidos realizados por cliente;
- Exemplo de mensagem que deverá ser consumida da fila RabbitMQ:
```
{
  "codigoPedido": 1001,
  "codigoCliente":1,
  "itens": [
    {
      "produto": "lápis",
      "quantidade": 100,
      "preco": 1.10
    },
    {
      "produto": "caderno",
      "quantidade": 10,
      "preco": 1.00
    }
  ]
}
```
- A partir dessa mensagem, estruture como será a modelagem de banco de dados e sua API

# Solução
- Java 21 com Spring Boot;
- Banco de dados MongoDB;

Contruida a API que consome a fila do RabbitMQ e grava esse JSON no banco de dados MongoDB. Ao gravar os dados já é calculado o total do pedido.

A API tem um endpoint para disponibilizar os dados que foi solicitado, retornando para o cliente
- Valor total de um pedido;
- Quantidade de pedidos por cliente;
- Lista de pedidos realizados por cliente;

Exemplo de requisição e retorno:

```
GET http://localhost:9090/customers/1/orders

Response:
{
    "summary": {
        "totalOnOrders": 2670.10
    },
    "data": [
        {
            "orderId": 1001,
            "customerId": 1,
            "total": 120.00
        },
        {
            "orderId": 1002,
            "customerId": 1,
            "total": 2550.10
        }
    ],
    "pagination": {
        "page": 0,
        "pageSize": 10,
        "totalElements": 2,
        "totalPages": 1
    }
}
```