# 🛒 API de E-commerce — Projeto Backend

## 📌 Descrição

API REST desenvolvida em **Java + Spring Boot** para simular um fluxo completo de e-commerce, incluindo:

* gerenciamento de produtos e categorias
* carrinho de compras
* aplicação de cupons e promoções
* criação e controle de pedidos
* avaliações de produtos
* relatórios e métricas

O sistema foi projetado com foco em **regras de negócio reais**, garantindo consistência entre carrinho, pedido, cupom e estoque.

---

## 🚀 Tecnologias utilizadas

* Java 17
* Spring Boot
* Spring Data JPA
* Hibernate
* Oracle DB
* Lombok
* Swagger (OpenAPI)

---

## 📂 Estrutura do projeto

Organizado em camadas:

```
controller  -> endpoints REST
service     -> regras de negócio
repository  -> acesso ao banco
model       -> entidades
dto         -> request/response
mapper      -> conversões
docs        -> documentação Swagger
```

---

## 🔁 Fluxo principal do sistema

### 1. Carrinho

* Usuário adiciona produtos ao carrinho
* Carrinho é criado automaticamente se não existir

### 2. Cupom (Opcional)

* Usuário pode aplicar cupom no carrinho
* Cupom:

    * NÃO é obrigatório
    * NÃO é consumido nesse momento
* O desconto fica salvo no carrinho

### 3. Pedido (Checkout)

* Pedido é criado a partir do carrinho
* O sistema:

    * calcula valor final
    * aplica desconto do carrinho
    * baixa o estoque
    * consome o cupom (se houver)

### 4. Status do Pedido

Fluxo de status:

```
CREATED → PAID → SHIPPED → DELIVERED
```

Cancelamento permitido apenas em:

* CREATED
* PAID

---

## 🎟️ Regras de Cupons e Promoções

Tipos suportados:

* Desconto percentual (%)
* Desconto fixo (R$)
* Por produto
* Por categoria
* Global

Validações:

* Cupom expirado → rejeitado
* Cupom sem relação com o carrinho → rejeitado
* Cupom já utilizado pelo usuário → rejeitado
* Cupom com limite atingido → rejeitado

📌 Importante:

* O cupom só é consumido no **checkout**
* Cupom único (`limiteUso = 1`) só pode ser usado uma vez no sistema

---

## ⭐ Avaliações (Reviews)

Regras:

* Apenas quem comprou pode avaliar
* Pedido deve estar **DELIVERED**
* Apenas 1 avaliação por produto por pedido
* Média do produto é recalculada automaticamente

---

## 📊 Relatórios disponíveis

Endpoints:

* `GET /relatorios/vendas`
* `GET /relatorios/produtos-mais-vendidos`
* `GET /relatorios/estoque-baixo`
* `GET /relatorios/promocoes-mais-utilizadas`

Funcionalidades:

* Faturamento por período
* Produtos mais vendidos
* Produtos com estoque baixo
* Promoções mais utilizadas

📌 Observação:

* Apenas pedidos **PAID, SHIPPED e DELIVERED** entram nos relatórios

---

## 📦 Estoque

* Baixa automática ao criar pedido
* Devolução automática ao cancelar pedido
* Controle de estoque mínimo
* Flag de estoque baixo

---

## 🔗 Principais endpoints

### Produtos

```
POST   /produtos
GET    /produtos
GET    /produtos/{id}
PUT    /produtos/{id}
DELETE /produtos/{id}
```

### Categorias

```
POST   /categorias
GET    /categorias
PUT    /categorias/{id}
DELETE /categorias/{id}
```

### Carrinho

```
GET    /carrinho?usuarioId=1
POST   /carrinho/itens
PUT    /carrinho/itens/{id}
DELETE /carrinho/itens/{id}
```

### Cupons

```
POST /cupons/aplicar
POST /promocoes
```

### Pedidos

```
POST /pedidos
GET  /pedidos/{id}

POST /pedidos/{id}/pagar
POST /pedidos/{id}/enviar
POST /pedidos/{id}/entregar
POST /pedidos/{id}/cancelar
```

### Avaliações

```
POST /avaliacoes
GET  /avaliacoes/produto/{produtoId}
```

---

### Fluxo esperado da aplicação

Categoria
→ Criar categoria (com ou sem hierarquia)

Produto
→ Criar produto vinculado a uma categoria

Estoque
→ Adicionar estoque (entrada)
→ Controlar movimentações (entrada/saída/ajuste/devolução)

Carrinho
→ Usuário adiciona produtos
→ Sistema salva precoMomento
→ Recalcula total automaticamente

Cupom (opcional)
→ Usuário aplica cupom no carrinho
→ Sistema valida (validade, uso, relação com produtos)
→ Cupom NÃO é consumido aqui

Pedido (Checkout)
→ Criar pedido a partir do carrinho
→ Validar estoque
→ Aplicar desconto do cupom
→ Baixar estoque
→ Consumir cupom
→ Status: CREATED

Fluxo do Pedido
→ CREATED → PAID → SHIPPED → DELIVERED

Cancelamento
→ Permitido em CREATED ou PAID
→ Devolve estoque

Avaliação (Review)
→ Apenas após DELIVERED
→ Usuário avalia produto do pedido
→ 1 avaliação por produto por pedido
→ Recalcula média do produto

Relatórios
→ Vendas (PAID/SHIPPED/DELIVERED)
→ Produtos mais vendidos
→ Estoque baixo
→ Promoções mais utilizadas

## 🧪 Como executar o projeto

### 1. Clonar repositório

```bash
git clone <repo>
```

### 2. Configurar banco de dados

No `application.properties`:

```properties
spring.datasource.url=jdbc:oracle:thin:@localhost:1521:XE
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha

spring.datasource.driver-class-name=oracle.jdbc.OracleDriver

spring.jpa.database-platform=org.hibernate.dialect.OracleDialect
spring.jpa.hibernate.ddl-auto=update

spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

### 3. Rodar aplicação

```bash
mvn spring-boot:run
```

---

## 📘 Documentação da API

Acesse via Swagger:

```
http://localhost:8080/swagger-ui.html
```

---

## 📌 Diferenciais do projeto

* Fluxo de compra realista
* Consumo correto de cupom (somente no checkout)
* Controle consistente de estoque
* Validações de negócio completas
* Estrutura organizada e escalável
* Relatórios integrados

---

| Etapa         | Status     |
| ------------- | ---------- |
| 1. Categorias | ✅ OK       |
| 2. Estoque    | ✅ OK       |
| 3. Carrinho   | ✅ OK       |
| 4. Pedidos    | ✅ OK       |
| 5. Cupons     | ✅ OK       |
| 6. Reviews    | ✅ OK       |
| 7. Auditoria  | ❌ Pendente |
| 8. Relatórios | ✅ OK       |


## 📎 Possíveis melhorias futuras

* Auditoria (audit log)
* Paginação nos endpoints
* Autenticação com JWT
* Notificações (email/alertas)
* Dashboard com métricas

---

## 👨‍💻 Autor - Manoel Souza

Projeto desenvolvido para prática de backend com foco em regras de negócio reais.

---
