# Trabalho Prático de Engenharia de Dados — 2026.1

Projeto desenvolvido para a disciplina de **Engenharia de Dados**, ministrada pelo professor **André Britto de Carvalho**.

O trabalho tem como objetivo desenvolver aplicações que realizem operações de CRUD em bancos de dados relacionais e não relacionais, além de preparar a base para uma futura etapa de integração de dados em um esquema estrela.

## Objetivos do Projeto

- Implementar operações de **CRUD**: criação, leitura, atualização e exclusão de dados.
- Desenvolver uma aplicação integrada a um banco de dados relacional.
- Desenvolver uma aplicação integrada a um banco de dados NoSQL.
- Mapear estruturas do modelo relacional para uma representação em MongoDB.
- Discutir e implementar restrições de dados no modelo relacional e no modelo NoSQL.
- Preparar o projeto para uma futura etapa de integração de dados.

## Fases do Trabalho

O trabalho está dividido em três partes principais:

### Parte 1 — CRUD Relacional

A primeira parte consiste no desenvolvimento de um programa capaz de executar operações de CRUD nas tabelas do esquema relacional trabalhado em aula.

As tabelas utilizadas são:

- `usuario`
- `estudante`
- `vinculo`
- `curso`

Nesta etapa, a aplicação deve se comunicar com um SGBD relacional e executar operações de manipulação de dados.

#### Banco de dados obrigatório

- **PostgreSQL**
- Hospedado na **AWS**
- Deve conter todas as tabelas do esquema relacional

### Parte 2 — CRUD NoSQL

A segunda parte consiste no mapeamento das tabelas do modelo relacional para o banco de dados MongoDB e na implementação do CRUD correspondente.

Nesta etapa, todas as estruturas do modelo relacional devem ser representadas no MongoDB.

#### Banco de dados obrigatório

- **MongoDB**
- Hospedado na **AWS**
- Deve conter todas as estruturas mapeadas a partir do esquema relacional

### Parte 3 — Integração de Dados

A terceira parte será definida posteriormente.

A proposta geral é implementar rotinas de integração dos dados em um banco de dados no modelo de **esquema estrela**.

## Modelagem Relacional

O banco relacional deve conter as seguintes tabelas:

### usuario

Representa os usuários cadastrados no sistema.

Exemplos de atributos esperados:

- `id_usuario`
- `nome`
- `email`
- `senha`
- `tipo_usuario`

### estudante

Representa os estudantes vinculados ao sistema acadêmico.

Exemplos de atributos esperados:

- `id_estudante`
- `matricula`
- `id_usuario`
- `data_nascimento`

### curso

Representa os cursos disponíveis.

Exemplos de atributos esperados:

- `id_curso`
- `nome`
- `codigo`
- `carga_horaria`

### vinculo

Representa o vínculo entre estudante e curso.

Exemplos de atributos esperados:

- `id_vinculo`
- `id_estudante`
- `id_curso`
- `data_inicio`
- `status`

> Observação: os atributos devem ser ajustados de acordo com o esquema relacional trabalhado em aula.

## Modelagem NoSQL

O projeto lógico NoSQL consiste no mapeamento das tabelas relacionais para estruturas do MongoDB.

Uma possível representação é:

### Coleção `usuarios`

Pode armazenar os dados básicos dos usuários.

```json
{
  "_id": "ObjectId",
  "nome": "Nome do usuário",
  "email": "usuario@email.com",
  "senha": "hash_da_senha",
  "tipo_usuario": "estudante"
}
```

### Coleção `estudantes`

Pode armazenar os dados acadêmicos do estudante e referenciar o usuário correspondente.

```json
{
  "_id": "ObjectId",
  "matricula": "20260001",
  "usuario_id": "ObjectId",
  "data_nascimento": "2000-01-01"
}
```

### Coleção `cursos`

Pode armazenar os dados dos cursos.

```json
{
  "_id": "ObjectId",
  "nome": "Engenharia de Computação",
  "codigo": "ECOMP",
  "carga_horaria": 3600
}
```

### Coleção `vinculos`

Pode representar a associação entre estudantes e cursos.

```json
{
  "_id": "ObjectId",
  "estudante_id": "ObjectId",
  "curso_id": "ObjectId",
  "data_inicio": "2026-01-01",
  "status": "ativo"
}
```

## Restrições de Dados

Durante o mapeamento para MongoDB, devem ser discutidas e tratadas as seguintes restrições:

### Chave primária

No PostgreSQL, as tabelas utilizam chaves primárias.

No MongoDB, cada documento possui o campo `_id`, que funciona como identificador único.

### Integridade referencial

No PostgreSQL, a integridade referencial é garantida por chaves estrangeiras.

No MongoDB, essa integridade pode ser tratada pela aplicação, validando se os documentos referenciados existem antes de inserir ou atualizar dados.

### Domínio

No PostgreSQL, o domínio pode ser controlado por tipos de dados, `CHECK constraints` e validações.

No MongoDB, pode ser tratado por validação de schema e validações na aplicação.

### NOT NULL

No PostgreSQL, campos obrigatórios podem ser definidos com `NOT NULL`.

No MongoDB, essa obrigatoriedade pode ser controlada com JSON Schema Validator e regras na camada da aplicação.

## Tecnologias Sugeridas

O grupo pode escolher livremente a linguagem de programação e o framework. Uma sugestão de stack é:

### Back-end

- Java 21
- Spring Boot
- Spring Data JPA
- Spring Data MongoDB
- Maven

### Bancos de Dados

- PostgreSQL
- MongoDB
- AWS RDS para PostgreSQL
- MongoDB em instância EC2 ou serviço compatível hospedado na AWS

### Ferramentas

- Git
- GitHub
- Postman ou Insomnia
- Docker, opcionalmente
- DBeaver ou pgAdmin
- MongoDB Compass

## Estrutura Sugerida do Projeto

```text
engenharia-dados-crud/
├── backend/
│   ├── src/
│   ├── pom.xml
│   └── README.md
├── database/
│   ├── postgresql/
│   │   ├── schema.sql
│   │   └── seed.sql
│   └── mongodb/
│       ├── collections.json
│       └── validators.js
├── docs/
│   ├── mapeamento-nosql.md
│   ├── relatorio.md
│   └── apresentacao.md
├── README.md
└── .gitignore
```

## Funcionalidades Esperadas

A aplicação deve permitir operações de CRUD para as seguintes entidades:

### Usuário

- Cadastrar usuário
- Listar usuários
- Buscar usuário por ID
- Atualizar usuário
- Remover usuário

### Estudante

- Cadastrar estudante
- Listar estudantes
- Buscar estudante por ID
- Atualizar estudante
- Remover estudante

### Curso

- Cadastrar curso
- Listar cursos
- Buscar curso por ID
- Atualizar curso
- Remover curso

### Vínculo

- Cadastrar vínculo entre estudante e curso
- Listar vínculos
- Buscar vínculo por ID
- Atualizar vínculo
- Remover vínculo

## Endpoints Sugeridos

Caso o grupo utilize uma API REST, os seguintes endpoints podem ser adotados:

### Usuários

```http
POST /usuarios
GET /usuarios
GET /usuarios/{id}
PUT /usuarios/{id}
DELETE /usuarios/{id}
```

### Estudantes

```http
POST /estudantes
GET /estudantes
GET /estudantes/{id}
PUT /estudantes/{id}
DELETE /estudantes/{id}
```

### Cursos

```http
POST /cursos
GET /cursos
GET /cursos/{id}
PUT /cursos/{id}
DELETE /cursos/{id}
```

### Vínculos

```http
POST /vinculos
GET /vinculos
GET /vinculos/{id}
PUT /vinculos/{id}
DELETE /vinculos/{id}
```

## Como Executar o Projeto

### 1. Clonar o repositório

```bash
git clone <url-do-repositorio>
cd engenharia-dados-crud
```

### 2. Configurar o PostgreSQL

Crie o banco de dados PostgreSQL na AWS e configure as credenciais no arquivo de configuração da aplicação.

Exemplo:

```properties
spring.datasource.url=jdbc:postgresql://<host>:5432/<database>
spring.datasource.username=<usuario>
spring.datasource.password=<senha>
```

### 3. Configurar o MongoDB

Configure a string de conexão do MongoDB hospedado na AWS.

Exemplo:

```properties
spring.data.mongodb.uri=mongodb://<usuario>:<senha>@<host>:27017/<database>
```

### 4. Executar a aplicação

Caso esteja usando Spring Boot:

```bash
cd backend
mvn spring-boot:run
```

### 5. Testar os endpoints

Utilize Postman, Insomnia ou outra ferramenta para testar as operações de CRUD.

## Relatório

O relatório deve apresentar:

- Descrição do projeto.
- Tecnologias utilizadas.
- Modelo relacional utilizado.
- Mapeamento do modelo relacional para MongoDB.
- Discussão sobre restrições:
  - chave primária;
  - integridade referencial;
  - domínio;
  - campos obrigatórios.
- Explicação das operações de CRUD implementadas.
- Evidências do efeito de cada método no banco de dados.
- Prints, logs ou exemplos de requisições e respostas.
- Link para o repositório do código-fonte.

## Evidências Esperadas

Para cada operação de CRUD, recomenda-se apresentar:

- Requisição realizada.
- Resposta da aplicação.
- Estado do banco antes ou depois da operação.
- Print ou consulta comprovando o efeito no banco.

Exemplo:

```sql
SELECT * FROM usuario;
```

Exemplo no MongoDB:

```javascript
db.usuarios.find();
```

## Status do Projeto

- [ ] Criar estrutura do repositório
- [ ] Criar banco PostgreSQL na AWS
- [ ] Criar tabelas relacionais
- [ ] Implementar CRUD relacional
- [ ] Criar banco MongoDB na AWS
- [ ] Mapear tabelas relacionais para MongoDB
- [ ] Implementar CRUD NoSQL
- [ ] Documentar restrições no NoSQL
- [ ] Registrar evidências dos métodos
- [ ] Elaborar relatório
- [ ] Preparar apresentação final

## Equipe


## Observações

Este projeto segue as orientações do trabalho prático da disciplina de Engenharia de Dados 2026.1.

O código-fonte deve ser disponibilizado, preferencialmente, em um repositório GitHub ou ferramenta similar. Durante a apresentação, o grupo deverá demonstrar o funcionamento da aplicação e o efeito das operações no banco de dados.
