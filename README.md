# Projeto Final - Engenharia de Dados

Repositório: <https://github.com/thejosephantony/ProjetoFinal-EngenhariaDeDados>

Este repositório contém o desenvolvimento do trabalho prático da disciplina de Engenharia de Dados. O projeto está organizado em três partes principais: CRUD relacional, CRUD NoSQL e integração/ETL para construção de um modelo analítico.

## Visão geral

O objetivo do trabalho é aplicar conceitos de persistência, manipulação de dados, modelagem relacional, modelagem NoSQL e integração de dados. A primeira etapa consiste em desenvolver uma aplicação capaz de se comunicar com um SGBD PostgreSQL e executar operações de CRUD sobre tabelas do esquema relacional trabalhado em aula.

A Parte 1 usa o schema oficial `universidade`, criado a partir do arquivo `universidade-dump-engdados.sql`.

## Estrutura do projeto

```text
ProjetoFinal-EngenhariaDeDados/
├── CRUD Relacional - Parte1/
│   └── backend/
│       ├── pom.xml
│       └── src/
│           ├── main/
│           │   ├── java/br/ufs/engenhariadados/
│           │   │   ├── controller/
│           │   │   ├── dto/
│           │   │   ├── exception/
│           │   │   ├── model/
│           │   │   ├── repository/
│           │   │   ├── service/
│           │   │   └── EngenhariaDadosApplication.java
│           │   └── resources/
│           │       └── application.properties
│           └── test/
├── CRUD NoSQL - Parte2/
├── Integração de dados - Parte3/
├── dis-csv-discentes-de-graduacao-de-2025_1.csv
├── universidade-dump-engdados.sql
├── README.md
└── Trabalho Prático de Engenharia de Dados 2026.1.pdf
```

## Status atual

| Parte | Descrição | Status |
|---|---|---|
| Parte 1 | CRUD Relacional com PostgreSQL e Spring Boot | Concluído localmente |
| Parte 1 | Testes de CRUD em `usuario`, `estudante`, `curso` e `vinculo` | Concluído localmente |
| Parte 1 | Importação do dump oficial `universidade-dump-engdados.sql` | Concluída localmente |
| Parte 1 | Hospedagem do PostgreSQL na AWS | Pendente |
| Parte 2 | CRUD NoSQL | Pendente |
| Parte 3 | Integração de dados, ETL e esquema estrela | Pendente |

## Tecnologias utilizadas na Parte 1

- Java
- Spring Boot
- Spring Web
- Spring Data JPA
- Bean Validation
- PostgreSQL
- Maven
- Lombok
- PowerShell para testes HTTP com `Invoke-RestMethod`

## Banco de dados da Parte 1

Banco local utilizado durante o desenvolvimento:

```text
engenharia_dados
```

Schema oficial:

```text
universidade
```

Arquivo de dump utilizado:

```text
universidade-dump-engdados.sql
```

Principais tabelas trabalhadas no CRUD:

```text
universidade.usuario
universidade.estudante
universidade.curso
universidade.vinculo
```

Outras tabelas do schema também são criadas pelo dump, como `professor`, `departamento`, `disciplina`, `turma`, `sala`, `horario`, `leciona`, `cursa`, `plano`, `projeto`, `semestre` e `alocacao`.

## Modelo relacional usado no CRUD

### usuario

| Campo | Tipo no PostgreSQL | Observação |
|---|---|---|
| `cpf` | `universidade.tipo_cpf` | Chave primária |
| `nome` | `varchar(100)` | Obrigatório |
| `data_nascimento` | `date` | Opcional |
| `email` | `varchar[]` | Lista de e-mails |
| `telefone` | `varchar[]` | Lista de telefones |
| `login` | `varchar(45)` | Único |
| `senha` | `varchar(32)` | Não retornada nas respostas da API |

### estudante

| Campo | Tipo no PostgreSQL | Observação |
|---|---|---|
| `mat_estudante` | `universidade.matricula` | Chave primária |
| `cpf` | `universidade.tipo_cpf` | FK para `usuario` |
| `mc` | `numeric(2,0)` | Média/coefficient do estudante |
| `ano_ingresso` | `integer` | Ano de ingresso |

### curso

| Campo | Tipo no PostgreSQL | Observação |
|---|---|---|
| `idcurso` | `serial` | Chave primária |
| `nome` | `varchar(100)` | Obrigatório |
| `grau` | `universidade.tipo_grau` | Enum |
| `turno` | `universidade.tipo_turno` | Enum obrigatório |
| `campus` | `varchar(100)` | Opcional |
| `nivel` | `universidade.tipo_nivel` | Enum |

Valores aceitos no dump:

```text
tipo_grau: Bacharelado, Licenciatura Plena
tipo_turno: Matutino, Vespertino, Noturno, Turno Indefinido
tipo_nivel: Graduação, Mestrado, Doutorado, Lato
```

### vinculo

| Campo | Tipo no PostgreSQL | Observação |
|---|---|---|
| `idvinculo` | `serial` | Chave primária |
| `mat_estudante` | `universidade.matricula` | FK para `estudante` |
| `curso` | `integer` | FK para `curso` |
| `data_entrada` | `date` | Data de entrada |
| `status` | `universidade.status_estudante` | Enum |
| `data_saida` | `date` | Data de saída |

Valores aceitos no dump:

```text
status_estudante: Ativo, Cancelada, Formando, Graduado
```

## Configuração local do banco

### 1. Criar o banco

```powershell
psql -U postgres -d postgres
```

Dentro do `psql`:

```sql
DROP DATABASE IF EXISTS engenharia_dados;
CREATE DATABASE engenharia_dados;
\q
```

### 2. Importar o dump com UTF-8

No PowerShell:

```powershell
chcp 65001
$env:PGCLIENTENCODING="UTF8"

psql -U postgres -d engenharia_dados -f "C:\Users\Joseph\Downloads\ProjetoFinal-EngenhariaDeDados\universidade-dump-engdados.sql"
```

### 3. Conferir o schema

```powershell
psql -U postgres -d engenharia_dados
```

Dentro do `psql`:

```sql
\dn
\dt universidade.*

SELECT COUNT(*) FROM universidade.usuario;
SELECT COUNT(*) FROM universidade.estudante;
SELECT COUNT(*) FROM universidade.curso;
SELECT COUNT(*) FROM universidade.vinculo;
```

### 4. Corrigir sequences após o dump

Quando o dump insere dados manualmente em tabelas com `serial`, pode ser necessário atualizar as sequences:

```sql
SELECT setval(
    'universidade.curso_idcurso_seq',
    (SELECT MAX(idcurso) FROM universidade.curso)
);

SELECT setval(
    'universidade.vinculo_idvinculo_seq',
    (SELECT MAX(idvinculo) FROM universidade.vinculo)
);
```

## Configuração do Spring Boot

Arquivo:

```text
CRUD Relacional - Parte1/backend/src/main/resources/application.properties
```

Configuração usada no ambiente local:

```properties
spring.application.name=engenharia-dados

spring.datasource.url=jdbc:postgresql://localhost:5432/engenharia_dados?currentSchema=universidade
spring.datasource.username=postgres
spring.datasource.password=SUA_SENHA_DO_POSTGRES

spring.jpa.hibernate.ddl-auto=none
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.open-in-view=false

spring.flyway.enabled=false

server.port=8080
```

Recomendação para ambientes compartilhados ou AWS: não salvar senha real no GitHub. Usar variáveis de ambiente.

Exemplo:

```properties
spring.datasource.url=${SPRING_DATASOURCE_URL}
spring.datasource.username=${SPRING_DATASOURCE_USERNAME}
spring.datasource.password=${SPRING_DATASOURCE_PASSWORD}
```

## Executando o backend

```powershell
cd "C:\Users\Joseph\Downloads\ProjetoFinal-EngenhariaDeDados\CRUD Relacional - Parte1\backend"
mvn clean install
mvn spring-boot:run
```

A aplicação sobe em:

```text
http://localhost:8080
```

## Endpoints principais da Parte 1

### Usuários

```text
POST   /api/usuarios
GET    /api/usuarios
GET    /api/usuarios/{cpf}
PUT    /api/usuarios/{cpf}
DELETE /api/usuarios/{cpf}
```

### Estudantes

```text
POST   /api/estudantes
GET    /api/estudantes
GET    /api/estudantes/{matricula}
PUT    /api/estudantes/{matricula}
DELETE /api/estudantes/{matricula}
```

### Cursos

```text
POST   /api/cursos
GET    /api/cursos
GET    /api/cursos/{id}
PUT    /api/cursos/{id}
DELETE /api/cursos/{id}
```

### Vínculos

```text
POST   /api/vinculos
GET    /api/vinculos
GET    /api/vinculos/{id}
PUT    /api/vinculos/{id}
DELETE /api/vinculos/{id}
```

## Testes manuais realizados

Foram testadas operações de criação, leitura, atualização e remoção para as quatro entidades principais:

```text
usuario    CREATE / READ / UPDATE / DELETE OK
estudante  CREATE / READ / UPDATE / DELETE OK
curso      CREATE / READ / UPDATE / DELETE OK
vinculo    CREATE / READ / UPDATE / DELETE OK
```

### Exemplo de criação de usuário

```powershell
$body = @{
    cpf = 33333333301
    nome = "Usuario Teste Java"
    dataNascimento = "2000-01-15"
    email = @("usuario.teste@email.com")
    telefone = @("79999999999")
    login = "usuario.teste.java"
    senha = "123456"
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8080/api/usuarios" -Method Post -ContentType "application/json; charset=utf-8" -Body $body
```

### Exemplo de criação de estudante

```powershell
$body = @{
    matricula = "3330001"
    cpf = 33333333301
    mc = 80
    anoIngresso = 2025
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8080/api/estudantes" -Method Post -ContentType "application/json; charset=utf-8" -Body $body
```

### Exemplo de criação de curso

```powershell
$body = @{
    nome = "Curso Teste Java"
    grau = "Bacharelado"
    turno = "Noturno"
    campus = "Campus Teste"
    nivel = $null
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8080/api/cursos" -Method Post -ContentType "application/json; charset=utf-8" -Body $body
```

### Exemplo de criação de vínculo

```powershell
$body = @{
    matriculaEstudante = "3330001"
    cursoId = 7
    dataEntrada = "2025-01-20"
    status = "Ativo"
    dataSaida = $null
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8080/api/vinculos" -Method Post -ContentType "application/json; charset=utf-8" -Body $body
```

## Próximo passo obrigatório: PostgreSQL na AWS

A Parte 1 exige que o PostgreSQL esteja hospedado na AWS. Portanto, depois da validação local, o próximo passo é:

1. Criar uma instância PostgreSQL no Amazon RDS.
2. Liberar acesso pelo grupo de segurança apenas para IPs necessários.
3. Criar o banco `engenharia_dados` na instância remota.
4. Importar o arquivo `universidade-dump-engdados.sql` no RDS.
5. Corrigir as sequences `curso_idcurso_seq` e `vinculo_idvinculo_seq` no banco remoto.
6. Atualizar as variáveis de ambiente do Spring Boot para apontar para o banco remoto.
7. Reexecutar os testes de CRUD usando o banco da AWS.
8. Documentar evidências: prints do RDS, conexão, tabelas e testes HTTP.

## Roadmap geral

### Parte 1 - CRUD Relacional

- [x] Criar projeto Spring Boot.
- [x] Configurar Maven e dependências.
- [x] Configurar conexão com PostgreSQL local.
- [x] Importar o dump oficial `universidade-dump-engdados.sql`.
- [x] Mapear entidades do schema `universidade`.
- [x] Implementar repositories.
- [x] Implementar DTOs.
- [x] Implementar services.
- [x] Implementar controllers.
- [x] Testar CRUD de `usuario`.
- [x] Testar CRUD de `estudante`.
- [x] Testar CRUD de `curso`.
- [x] Testar CRUD de `vinculo`.
- [ ] Hospedar PostgreSQL na AWS RDS.
- [ ] Testar CRUD com banco remoto.
- [ ] Documentar evidências da Parte 1.

### Parte 2 - CRUD NoSQL

- [ ] Definir banco NoSQL.
- [ ] Definir coleção/documentos.
- [ ] Implementar CRUD NoSQL.
- [ ] Criar testes de inserção, leitura, atualização e remoção.
- [ ] Documentar decisões de modelagem.

### Parte 3 - Integração de Dados e Modelo Analítico

- [ ] Construir esquema estrela.
- [ ] Preparar fontes de dados.
- [ ] Integrar banco relacional da Parte 1.
- [ ] Integrar CSVs externos do grupo Ensino.
- [ ] Usar Apache Hop para ETL.
- [ ] Construir dimensões.
- [ ] Construir tabela fato.
- [ ] Carregar dados de turmas de 2019 a 2025.
- [ ] Validar métricas: matriculados, média de notas, aprovados e reprovados.
- [ ] Documentar pipeline e evidências.

## Comandos Git sugeridos

```powershell
git status
git add -A
git commit -m "Atualiza README e plano da Parte 1 relacional"
git push
```

## Observações importantes

- Não versionar senhas reais.
- Não deixar credenciais da AWS no `application.properties`.
- Usar UTF-8 ao importar dumps com acentos.
- Corrigir sequences depois da importação do dump quando houver tabelas com `serial` e dados inseridos manualmente.
- Manter evidências dos testes para apresentação e entrega.
