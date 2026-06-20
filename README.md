# Trabalho Prático de Engenharia de Dados — 2026.1

Projeto desenvolvido para a disciplina de **Engenharia de Dados**, ministrada pelo professor **André Britto de Carvalho**.

O trabalho prático consiste no desenvolvimento de aplicações para bancos de dados relacionais e não relacionais, além da construção de rotinas de integração de dados em um banco modelado no formato de **esquema estrela**.

## Sumário

- [Objetivos](#objetivos)
- [Fases do Trabalho](#fases-do-trabalho)
- [Parte 1 — CRUD Relacional](#parte-1--crud-relacional)
- [Parte 2 — CRUD NoSQL](#parte-2--crud-nosql)
- [Parte 3 — Integração de Dados](#parte-3--integração-de-dados)
- [Tecnologias Sugeridas](#tecnologias-sugeridas)
- [Estrutura Sugerida do Projeto](#estrutura-sugerida-do-projeto)
- [Como Executar](#como-executar)
- [Relatório](#relatório)
- [Checklist](#checklist)
- [Equipe](#equipe)

## Objetivos

- Implementar operações de **CRUD** em banco de dados relacional.
- Implementar operações de **CRUD** em banco de dados NoSQL.
- Mapear estruturas do modelo relacional para o modelo orientado a documentos do MongoDB.
- Discutir e implementar restrições de chave, integridade referencial, domínio e obrigatoriedade de campos.
- Construir um modelo dimensional em **esquema estrela**.
- Desenvolver pipelines de **ETL** para carregar dimensões e tabela fato.
- Integrar dados provenientes de banco relacional e arquivos CSV externos.
- Demonstrar o funcionamento das aplicações e pipelines na apresentação final.

## Fases do Trabalho

O trabalho está dividido em três partes:

1. **Parte 1 — CRUD Relacional**
2. **Parte 2 — CRUD NoSQL**
3. **Parte 3 — Integração de Dados**

---

# Parte 1 — CRUD Relacional

A Parte 1 consiste no desenvolvimento de um programa que realiza operações de CRUD em tabelas de um banco de dados relacional.

As operações esperadas são:

- `INSERT`
- `DELETE`
- `UPDATE`
- leitura/consulta de dados

## Banco de Dados

O banco de dados obrigatório para esta etapa é:

- **PostgreSQL**
- Hospedado na **AWS**
- Contendo todas as tabelas do esquema relacional trabalhado na disciplina

## Tabelas Relacionais

As tabelas utilizadas são:

- `usuario`
- `estudante`
- `vinculo`
- `curso`

## Entidades

### Usuário

Representa os usuários cadastrados no sistema.

Exemplo de atributos:

- `id_usuario`
- `nome`
- `email`
- `senha`
- `tipo_usuario`

### Estudante

Representa os estudantes vinculados ao sistema acadêmico.

Exemplo de atributos:

- `id_estudante`
- `matricula`
- `id_usuario`
- `data_nascimento`

### Curso

Representa os cursos disponíveis.

Exemplo de atributos:

- `id_curso`
- `nome`
- `codigo`
- `carga_horaria`

### Vínculo

Representa o vínculo entre estudante e curso.

Exemplo de atributos:

- `id_vinculo`
- `id_estudante`
- `id_curso`
- `data_inicio`
- `status`

> Os atributos devem ser ajustados conforme o esquema relacional trabalhado em aula.

## Endpoints Sugeridos

Caso a aplicação seja desenvolvida como API REST, os endpoints podem seguir o seguinte padrão:

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

---

# Parte 2 — CRUD NoSQL

A Parte 2 consiste no mapeamento das tabelas relacionais para o banco de dados **MongoDB** e na implementação das operações de CRUD para as estruturas mapeadas.

## Banco de Dados

O banco de dados obrigatório para esta etapa é:

- **MongoDB**
- Hospedado na **AWS**
- Contendo todas as estruturas mapeadas a partir do esquema relacional

## Projeto Lógico NoSQL

O projeto lógico NoSQL deve representar todas as tabelas relacionais no MongoDB.

Além da implementação, o grupo deve discutir como garantir:

- restrição de chave;
- integridade referencial;
- restrição de domínio;
- restrição `NOT NULL`.

## Mapeamento Sugerido

### Coleção `usuarios`

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

```json
{
  "_id": "ObjectId",
  "matricula": "20260001",
  "usuario_id": "ObjectId",
  "data_nascimento": "2000-01-01"
}
```

### Coleção `cursos`

```json
{
  "_id": "ObjectId",
  "nome": "Engenharia de Computação",
  "codigo": "ECOMP",
  "carga_horaria": 3600
}
```

### Coleção `vinculos`

```json
{
  "_id": "ObjectId",
  "estudante_id": "ObjectId",
  "curso_id": "ObjectId",
  "data_inicio": "2026-01-01",
  "status": "ativo"
}
```

## Restrições no MongoDB

### Chave

No PostgreSQL, cada tabela possui uma chave primária.

No MongoDB, cada documento possui o campo `_id`, que funciona como identificador único.

### Integridade Referencial

No PostgreSQL, a integridade referencial é garantida por chaves estrangeiras.

No MongoDB, essa integridade deve ser tratada pela aplicação ou por validações adicionais, verificando se os documentos referenciados existem antes de inserir ou atualizar dados.

### Domínio

No PostgreSQL, as restrições de domínio podem ser controladas por tipos de dados, `CHECK constraints` e validações.

No MongoDB, podem ser usadas validações via JSON Schema e validações na camada da aplicação.

### NOT NULL

No PostgreSQL, campos obrigatórios são definidos com `NOT NULL`.

No MongoDB, essa obrigatoriedade pode ser representada com JSON Schema Validator e regras de validação na aplicação.

---
# Parte 3 — Integração de Dados

A Parte 3 consiste na construção de um banco no formato de **esquema estrela** e no desenvolvimento de pipelines de **ETL** para preenchimento das tabelas de dimensão e da tabela de fatos.

## Objetivo da Integração

Integrar dados de diferentes fontes para construir uma base analítica sobre **turmas de graduação**, considerando professores, disciplinas, departamentos, semestres, campus e métricas acadêmicas.

## Ferramenta de ETL

A ferramenta obrigatória para as rotinas de integração é:

- **Apache Hop**

Os pipelines deverão carregar tanto as dimensões quanto a tabela fato.

É permitido criar vários pipelines separados ou construir um workflow para executar todo o processo de integração.

## Banco de Dados do Esquema Estrela

O banco do esquema estrela deve estar disponível em um database no servidor **RDS da AWS**.

Também deve ser criado um usuário específico para as rotinas de integração, com as permissões necessárias para povoamento do banco.

## Fontes de Dados

As fontes utilizadas na integração são:

1. Banco de dados relacional da Parte 1, considerando:
   - todas as linhas originais do script;
   - linhas inseridas através do CRUD.

2. Arquivos `.csv` do grupo **Ensino** do site dados.ufs.br:
   - Unidades Acadêmicas;
   - Componentes Curriculares;
   - Docentes;
   - Turmas.

Para os arquivos de turmas, devem ser consideradas turmas de **2019 até 2025**.

## Modelagem do Esquema Estrela

O esquema estrela deve modelar a situação de turmas de graduação.

### Dimensão Professor

Os professores são definidos por:

- nome;
- tipo da jornada de trabalho;
- formação;
- departamento de lotação.

Exemplo:

```sql
CREATE TABLE dim_professor (
    id_professor SERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    tipo_jornada VARCHAR(100),
    formacao VARCHAR(100),
    departamento_lotacao VARCHAR(100)
);
```

### Dimensão Disciplina

As disciplinas, ou componentes curriculares, são definidas por:

- código;
- nome;
- departamento responsável;
- número de créditos (`cr_total`).

Exemplo:

```sql
CREATE TABLE dim_disciplina (
    id_disciplina SERIAL PRIMARY KEY,
    codigo VARCHAR(50) NOT NULL,
    nome VARCHAR(255) NOT NULL,
    departamento_responsavel VARCHAR(100),
    cr_total INTEGER
);
```

### Dimensão Departamento

Os departamentos, ou unidades, são definidos por:

- código ou sigla;
- nome.

Exemplo:

```sql
CREATE TABLE dim_departamento (
    id_departamento SERIAL PRIMARY KEY,
    codigo VARCHAR(50) NOT NULL,
    nome VARCHAR(255) NOT NULL
);
```

### Dimensão Semestre

Os semestres são definidos por:

- ano;
- período.

Exemplo:

```sql
CREATE TABLE dim_semestre (
    id_semestre SERIAL PRIMARY KEY,
    ano INTEGER NOT NULL,
    periodo INTEGER NOT NULL
);
```

### Dimensão Campus

Representa o campus associado à turma.

Exemplo:

```sql
CREATE TABLE dim_campus (
    id_campus SERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL
);
```

### Tabela Fato Turma

As turmas representam associações entre professores, disciplinas, departamentos, semestre e campus.

A tabela fato deve conter métricas como:

- número de discentes matriculados;
- média de notas, quando disponível;
- número de aprovados, quando disponível;
- número de reprovados, quando disponível.

Exemplo:

```sql
CREATE TABLE fato_turma (
    id_fato_turma SERIAL PRIMARY KEY,

    id_professor INTEGER REFERENCES dim_professor(id_professor),
    id_disciplina INTEGER REFERENCES dim_disciplina(id_disciplina),
    id_departamento INTEGER REFERENCES dim_departamento(id_departamento),
    id_semestre INTEGER REFERENCES dim_semestre(id_semestre),
    id_campus INTEGER REFERENCES dim_campus(id_campus),

    numero_discentes_matriculados INTEGER,
    media_notas NUMERIC(5,2),
    numero_aprovados INTEGER,
    numero_reprovados INTEGER
);
```

## Fluxo Sugerido dos Pipelines

### Pipeline 1 — Carga de Departamentos

Fonte:

- CSV de Unidades Acadêmicas

Destino:

- `dim_departamento`

Transformações possíveis:

- selecionar colunas necessárias;
- remover duplicidades;
- padronizar siglas;
- tratar campos nulos.

### Pipeline 2 — Carga de Disciplinas

Fonte:

- CSV de Componentes Curriculares

Destino:

- `dim_disciplina`

Transformações possíveis:

- selecionar código, nome, departamento responsável e créditos;
- converter tipos;
- remover registros duplicados;
- validar departamento responsável.

### Pipeline 3 — Carga de Professores

Fonte:

- CSV de Docentes

Destino:

- `dim_professor`

Transformações possíveis:

- selecionar nome, jornada, formação e departamento;
- padronizar textos;
- tratar ausência de dados;
- remover duplicidades.

### Pipeline 4 — Carga de Semestres

Fonte:

- CSV de Turmas

Destino:

- `dim_semestre`

Transformações possíveis:

- extrair ano e período;
- considerar apenas turmas de 2019 até 2025;
- remover duplicidades.

### Pipeline 5 — Carga de Campus

Fonte:

- CSV de Turmas ou outra fonte disponível

Destino:

- `dim_campus`

Transformações possíveis:

- extrair campus;
- padronizar nomes;
- remover duplicidades.

### Pipeline 6 — Carga da Tabela Fato

Fontes:

- CSV de Turmas;
- banco relacional da Parte 1;
- dimensões previamente carregadas.

Destino:

- `fato_turma`

Transformações possíveis:

- fazer lookup das dimensões;
- calcular número de discentes matriculados;
- calcular média de notas, quando disponível;
- calcular número de aprovados;
- calcular número de reprovados;
- inserir registros na tabela fato.

## Avaliação da Parte 3

Na avaliação, as tabelas do esquema estrela estarão vazias.

O grupo deverá:

1. Executar os pipelines no Apache Hop.
2. Carregar as dimensões.
3. Carregar a tabela fato.
4. Mostrar o efeito das rotinas no banco de dados.
5. Demonstrar consultas SQL comprovando que os dados foram integrados.

Exemplos de consultas para demonstração:

```sql
SELECT COUNT(*) FROM dim_professor;
SELECT COUNT(*) FROM dim_disciplina;
SELECT COUNT(*) FROM dim_departamento;
SELECT COUNT(*) FROM dim_semestre;
SELECT COUNT(*) FROM dim_campus;
SELECT COUNT(*) FROM fato_turma;
```

Consulta analítica exemplo:

```sql
SELECT
    s.ano,
    s.periodo,
    d.nome AS disciplina,
    p.nome AS professor,
    f.numero_discentes_matriculados,
    f.media_notas,
    f.numero_aprovados,
    f.numero_reprovados
FROM fato_turma f
JOIN dim_semestre s ON s.id_semestre = f.id_semestre
JOIN dim_disciplina d ON d.id_disciplina = f.id_disciplina
JOIN dim_professor p ON p.id_professor = f.id_professor
ORDER BY s.ano, s.periodo, d.nome;
```

---
# Tecnologias Sugeridas

A linguagem de programação e o framework são livres.

Uma stack recomendada é:

## Back-end

- C# com ASP.NET Core Web API  
ou
- Java com Spring Boot  
ou
- Python com FastAPI

## Bancos de Dados

- PostgreSQL
- MongoDB
- AWS RDS
- MongoDB hospedado na AWS

## ETL

- Apache Hop

## Ferramentas de Apoio

- Git
- GitHub
- Postman ou Insomnia
- DBeaver ou pgAdmin
- MongoDB Compass
- Docker, opcionalmente

---

# Estrutura Sugerida do Projeto

```text
engenharia-dados-crud-etl/
├── backend/
│   ├── src/
│   ├── README.md
│   └── ...
├── database/
│   ├── postgresql/
│   │   ├── schema.sql
│   │   ├── seed.sql
│   │   └── star_schema.sql
│   └── mongodb/
│       ├── collections.json
│       └── validators.js
├── etl/
│   ├── pipelines/
│   │   ├── carga_departamentos.hpl
│   │   ├── carga_disciplinas.hpl
│   │   ├── carga_professores.hpl
│   │   ├── carga_semestres.hpl
│   │   ├── carga_campus.hpl
│   │   └── carga_fato_turma.hpl
│   └── workflows/
│       └── workflow_carga_dw.hwf
├── data/
│   ├── unidades_academicas/
│   ├── componentes_curriculares/
│   ├── docentes/
│   └── turmas/
├── docs/
│   ├── mapeamento-nosql.md
│   ├── esquema-estrela.md
│   ├── etl-apache-hop.md
│   ├── evidencias.md
│   └── relatorio.md
├── README.md
└── .gitignore
```

---

# Como Executar

## 1. Clonar o Repositório

```bash
git clone <url-do-repositorio>
cd engenharia-dados-crud-etl
```

## 2. Configurar PostgreSQL na AWS

Criar o banco PostgreSQL no RDS e configurar as credenciais da aplicação.

Exemplo:

```properties
spring.datasource.url=jdbc:postgresql://<host>:5432/<database>
spring.datasource.username=<usuario>
spring.datasource.password=<senha>
```

Ou, em C#:

```json
{
  "ConnectionStrings": {
    "PostgreSQL": "Host=<host>;Port=5432;Database=<database>;Username=<usuario>;Password=<senha>"
  }
}
```

## 3. Configurar MongoDB na AWS

Exemplo:

```properties
spring.data.mongodb.uri=mongodb://<usuario>:<senha>@<host>:27017/<database>
```

Ou, em C#:

```json
{
  "MongoDB": {
    "ConnectionString": "mongodb://<usuario>:<senha>@<host>:27017",
    "DatabaseName": "<database>"
  }
}
```

## 4. Executar o Back-end

Exemplo usando Spring Boot:

```bash
cd backend
mvn spring-boot:run
```

Exemplo usando ASP.NET Core:

```bash
cd backend
dotnet run
```

## 5. Testar o CRUD

Utilizar Postman, Insomnia ou ferramenta semelhante para testar:

- usuários;
- estudantes;
- cursos;
- vínculos.

## 6. Executar os Pipelines no Apache Hop

Abrir o Apache Hop e executar os pipelines da pasta:

```text
etl/pipelines/
```

Ou executar o workflow principal:

```text
etl/workflows/workflow_carga_dw.hwf
```

---

# Relatório

O relatório deve apresentar:

- descrição do projeto;
- tecnologias utilizadas;
- modelo relacional utilizado;
- implementação do CRUD relacional;
- mapeamento do modelo relacional para MongoDB;
- implementação do CRUD NoSQL;
- discussão sobre restrições no MongoDB:
  - chave;
  - integridade referencial;
  - domínio;
  - `NOT NULL`;
- modelagem do esquema estrela;
- descrição das dimensões;
- descrição da tabela fato;
- fontes de dados utilizadas;
- explicação dos pipelines no Apache Hop;
- evidências dos efeitos dos métodos no banco de dados;
- evidências da execução dos pipelines;
- consultas SQL comprovando o povoamento do esquema estrela;
- link para o repositório do código-fonte.

---

# Evidências Esperadas

Para cada método CRUD, recomenda-se apresentar:

- requisição realizada;
- resposta da aplicação;
- estado do banco antes e depois da operação;
- print ou consulta comprovando o efeito no banco.

Exemplo PostgreSQL:

```sql
SELECT * FROM usuario;
```

Exemplo MongoDB:

```javascript
db.usuarios.find();
```

Para a Parte 3, recomenda-se apresentar:

- prints dos pipelines no Apache Hop;
- logs de execução;
- consultas SQL antes da execução;
- consultas SQL depois da execução;
- contagem de registros nas dimensões;
- contagem de registros na tabela fato;
- consulta analítica usando joins entre fato e dimensões.

---

# Checklist

## Parte 1 — CRUD Relacional

- [ ] Criar repositório no GitHub
- [ ] Criar banco PostgreSQL na AWS
- [ ] Criar tabelas relacionais
- [ ] Criar usuário de acesso ao banco
- [ ] Implementar CRUD de usuário
- [ ] Implementar CRUD de estudante
- [ ] Implementar CRUD de curso
- [ ] Implementar CRUD de vínculo
- [ ] Testar operações no Postman/Insomnia
- [ ] Registrar evidências no banco

## Parte 2 — CRUD NoSQL

- [ ] Criar banco MongoDB na AWS
- [ ] Pesquisar modelos NoSQL
- [ ] Mapear tabelas relacionais para coleções MongoDB
- [ ] Implementar validações de chave
- [ ] Implementar validações de integridade referencial
- [ ] Implementar validações de domínio
- [ ] Implementar validações de campos obrigatórios
- [ ] Implementar CRUD de usuários
- [ ] Implementar CRUD de estudantes
- [ ] Implementar CRUD de cursos
- [ ] Implementar CRUD de vínculos
- [ ] Registrar evidências no banco

## Parte 3 — Integração de Dados

- [ ] Criar database do esquema estrela no RDS
- [ ] Criar usuário para rotinas de integração
- [ ] Criar tabelas de dimensão
- [ ] Criar tabela fato
- [ ] Baixar CSVs de Unidades Acadêmicas
- [ ] Baixar CSVs de Componentes Curriculares
- [ ] Baixar CSVs de Docentes
- [ ] Baixar CSVs de Turmas
- [ ] Filtrar turmas de 2019 até 2025
- [ ] Criar pipeline de departamentos
- [ ] Criar pipeline de disciplinas
- [ ] Criar pipeline de professores
- [ ] Criar pipeline de semestres
- [ ] Criar pipeline de campus
- [ ] Criar pipeline da tabela fato
- [ ] Criar workflow de execução, se necessário
- [ ] Testar carga com tabelas vazias
- [ ] Registrar evidências da execução dos pipelines

## Documentação e Apresentação

- [ ] Criar relatório
- [ ] Documentar mapeamento NoSQL
- [ ] Documentar esquema estrela
- [ ] Documentar pipelines do Apache Hop
- [ ] Adicionar prints e evidências
- [ ] Preparar roteiro de apresentação
- [ ] Validar execução completa antes da avaliação

---

# Equipe

Adicionar os integrantes do grupo:

- Nome do integrante 1
- Nome do integrante 2
- Nome do integrante 3

---

# Observações

Este projeto segue as orientações do Trabalho Prático de Engenharia de Dados 2026.1.

O código-fonte deve ser disponibilizado, preferencialmente, em um repositório GitHub ou ferramenta similar.

Na apresentação final, o grupo deverá demonstrar:

- funcionamento do CRUD relacional;
- funcionamento do CRUD NoSQL;
- execução dos pipelines no Apache Hop;
- efeito das operações e integrações nos bancos de dados.
