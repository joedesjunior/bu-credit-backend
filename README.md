
# Projeto bu-credit-backend

Este projeto é um backend desenvolvido em Spring Boot que se conecta a um banco de dados PostgreSQL e utiliza Apache Kafka para comunicação assíncrona. A aplicação está configurada para ser executada em containers Docker, facilitando o processo de configuração e execução. Essa aplicação foi desenvolvida por Joedes Souza de Freitas Junior para uma avaliação técnica.


## Download and Deploy

Como rodar o projeto (Certificar-se de ter o Docker e Docker-Compose instalados na máquina):

```bash
  git clone https://github.com/joedesjunior/bu-credit-backend.git
```

```bash
  mvn clean install
```

```bash
  docker-compose up --build
```


## API Reference

#### Api DEBT:

##### Link para o Swagger: http://localhost:8080/swagger-ui/index.html#/

#### Post debt

```http
  POST /api/v1/debt
```

| Body | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `totalAmount`      | `decimal` | **Required**. totalAmount of item to fetch |
| `creditorName`      | `string` | **Required**. creditorName of item to fetch 
| `dueDate`      | `date` | **Required**. date of item to fetch |
| `numberOfInstallments`      | `decimal` | **Required**. numberOfInstallments of item to fetch |

#### Get debt by id

```http
  GET /api/v1/debt/{id}
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `id`      | `string` | **Required**. Id of item to fetch |


#### Get debts by creditor name

```http
  GET /api/v1/debt/name?creditorName=Joedes
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `creditorName`      | `string` | **Required**. Id of item to fetch ||



#### Get debts by status

```http
  GET /api/v1/debt/name?status=pay
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `status`      | `string` | **Required**. Id of item to fetch |


## Running Tests

To run tests, run the following command

```bash
  npm run test
```


## Authors

- [@joedesjunior](https://www.github.com/joedesjunior)

