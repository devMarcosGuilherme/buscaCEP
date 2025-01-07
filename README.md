# CEP API Project

## **Sobre o Projeto**
Este projeto é uma API REST para consultar informações de CEP, registrar logs das consultas realizadas e simular respostas usando o Wiremock. O sistema permite realizar consultas a um serviço externo (mockado com Wiremock) e salvar logs detalhados no banco de dados.

### **Funcionalidades**
- Consultar informações de um CEP especificado.
- Registrar logs de consultas contendo:
    - CEP consultado.
    - Dados retornados.
    - Horário da consulta.
- Simular respostas de serviços externos usando Wiremock.

---

## **Tecnologias Utilizadas**

### **Back-end**
- **Java 17**: Linguagem de programação utilizada.
- **Spring Boot 3.4.1**:
    - Spring Web: Para criação da API REST.
    - Spring Data JPA: Para interação com o banco de dados.
    - Spring DevTools: Para facilitar o desenvolvimento.
- **H2 Database**: Banco de dados em memória para testes e desenvolvimento.
- **Maven**: Gerenciador de dependências e build.
- **Docker**: Para conteinerização da aplicação.
- **Wiremock**: Para simular o comportamento de serviços externos.

---

## **Como Executar o Projeto**

### **Requisitos**
- Docker e Docker Compose instalados.
- Java 17 instalado (caso deseje executar fora de contêiner).
- Maven instalado (caso deseje buildar manualmente).

### **Passos para Buildar e Executar com Docker Compose**
1. Certifique-se de que o Docker esta instalado em sua máquina e esta sendo executado.

2. Suba os contêineres com Docker 
   Compose:
 ```bash
   docker-compose up --build
   ```

3. Verifique se os serviços estão rodando:
    - **CEP API**: Acesse `http://localhost:8080`
    - **Wiremock**: Acesse `http://localhost:8081`

### **Passos para Executar Localmente (Sem Docker)**
1. Instale as dependências usando o Maven:
   ```bash
   mvn clean install
   ```

2. Execute a aplicação:
   ```bash
   mvn spring-boot:run
   ```

3. Certifique-se de que o Wiremock está configurado e rodando em `http://localhost:8081`.

---

## **Como Testar**

### **1. Testar a Consulta de CEP**
A API está configurada para receber consultas no endpoint:
- **URL:** `http://localhost:8080/api/cep/{cep}`
- **Método:** `GET`

#### **Exemplo de Requisição:**
```bash
GET http://localhost:8080/cep/01001000
```

#### **Resposta Esperada:**
```json
{
  "id": 1,
  "cep": "01001000",
  "logradouro": "Praça da Sé",
  "bairro": "Sé",
  "cidade": "São Paulo",
  "estado": "SP"
}
```

### **2. Verificar Logs de Consulta**
Os logs são armazenados automaticamente na tabela `log_consulta`. Para acessá-los:
1. Acesse o H2 Console:
    - **URL:** `http://localhost:8080/h2-console`
    - **JDBC URL:** `jdbc:h2:mem:testdb`
    - **Usuário:** `sa`
    - **Senha:** (deixe em branco, ou conforme configurado).

2. Execute a consulta SQL:
   ```sql
   SELECT * FROM log_consulta;
   ```

### **3. Simular Respostas com Wiremock**
1. Configure os mapeamentos em `wiremock/mappings/cep-mock.json`.
2. Exemplo de mapeamento:
   ```json
   {
     "request": {
       "method": "GET",
       "urlPath": "/cep/01001000"
     },
     "response": {
       "status": 200,
       "body": "{\"cep\":\"01001000\",\"logradouro\":\"Praça da Sé\",\"bairro\":\"Sé\",\"cidade\":\"São Paulo\",\"estado\":\"SP\"}",
       "headers": {
         "Content-Type": "application/json"
       }
     }
   }
   ```
3. Reinicie o contêiner do Wiremock para aplicar as alterações:
   ```bash
   docker-compose restart wiremock
   ```

---
