# Parte 3 – Integração de Dados e Esquema Estrela

**Disciplina:** Engenharia de Dados – 2026.1  
**Professor:** André Britto de Carvalho  
**Data:** Julho de 2026  

---

## 📌 Visão Geral

Esta etapa consiste na construção de um **esquema estrela** para análise de turmas de graduação da UFS, e no desenvolvimento de **pipelines ETL** no Apache Hop para integrar dados de duas fontes:

- **Banco relacional (Parte 1)** – PostgreSQL hospedado na AWS RDS.
- **CSVs do grupo Ensino** – disponíveis no site [dados.ufs.br](https://dados.ufs.br/group/ensino).

O resultado final é um modelo dimensional que permite consultas analíticas sobre disciplinas, professores, semestres, campi e métricas de turmas.

---

## 🧩 Modelagem do Esquema Estrela

### Tabelas Dimensão

| Dimensão | Colunas | Fonte |
| :--- | :--- | :--- |
| **dim_departamento** | `id_departamento` (PK), `codigo`, `nome` | CSV Unidades Acadêmicas |
| **dim_disciplina** | `id_disciplina` (PK), `codigo`, `nome`, `departamento_responsavel`, `cr_total` | CSV Componentes Curriculares |
| **dim_professor** | `id_professor` (PK), `nome`, `tipo_jornada`, `formacao`, `departamento_lotacao` | CSV Docentes |
| **dim_semestre** | `id_semestre` (PK), `ano`, `periodo` | Extraído das Turmas |
| **dim_campus** | `id_campus` (PK), `nome` | Extraído das Turmas |

### Tabela Fato

| Coluna | Tipo | Descrição |
| :--- | :--- | :--- |
| `id_turma` | SERIAL PK | Chave primária artificial |
| `id_disciplina` | INTEGER FK → dim_disciplina | Disciplina da turma |
| `id_professor` | INTEGER FK → dim_professor | Professor responsável |
| `id_semestre` | INTEGER FK → dim_semestre | Semestre de oferta |
| `id_campus` | INTEGER FK → dim_campus | Campus onde a turma foi oferecida |
| `num_discentes_matriculados` | INTEGER DEFAULT 0 | Número de alunos matriculados |

---

## 📁 Fontes de Dados

### 1. Banco Relacional (Parte 1)
- **Tabelas:** `usuario`, `estudante`, `vinculo`, `curso` (schema `universidade`).
- **Localização:** PostgreSQL no RDS (AWS).
- **Uso:** Fornece dados de vínculo (para métricas de alunos), embora a fato use principalmente os CSVs.

### 2. CSVs do Grupo Ensino (dados.ufs.br)

| Arquivo | Conteúdo | Uso |
| :--- | :--- | :--- |
| `unidades_academicas.csv` | Departamentos/unidades | `dim_departamento` |
| `componentes_curriculares.csv` | Disciplinas (código, nome, créditos) | `dim_disciplina` |
| `docentes.csv` | Professores (nome, jornada, formação, lotação) | `dim_professor` |
| `turmas-de-{ano}.csv` (2019–2026) | Turmas: disciplina, professor, semestre, campus, matriculados | `dim_semestre`, `dim_campus`, `fato_turma` |

---

## 🔧 Pipelines ETL

Os pipelines foram desenvolvidos no **Apache Hop** e estão organizados em transformações (`.hpl`) e um workflow (`.hwf`) para orquestrar a execução.

### Dimensões

| Pipeline | Entrada | Transformações | Saída |
| :--- | :--- | :--- | :--- |
| **dim_departamento** | CSV Unidades | Renomear campos, remover duplicatas | `estrela.dim_departamento` |
| **dim_disciplina** | CSV Componentes | Selecionar colunas, remover duplicatas | `estrela.dim_disciplina` |
| **dim_professor** | CSV Docentes | Selecionar colunas, remover duplicatas | `estrela.dim_professor` |
| **dim_semestre** | CSVs Turmas (2019–2026) | Extrair `ano, periodo`, `Unique rows` | `estrela.dim_semestre` |
| **dim_campus** | CSVs Turmas (2019–2026) | Extrair `campus_turma`, `Unique rows` | `estrela.dim_campus` |

### Tabela Fato – `fato_turma`

**Pipeline:**

1. **Leitura:** CSVs de Turmas (2019–2026).
2. **Seleção:** Campos `codigo_componente`, `docentes`, `ano`, `periodo`, `campus_turma`, `codigo_turma`, `matriculados`.
3. **Tratamento de `docentes`:** Extrai apenas o primeiro nome (antes da primeira vírgula) com `String operations`.
4. **Lookups (Database Lookup):**
   - `id_disciplina` via `codigo_componente` → `codigo` em `dim_disciplina`.
   - `id_professor` via `docentes` → `nome` em `dim_professor`.
   - `id_semestre` via `ano` e `periodo` → `ano, periodo` em `dim_semestre`.
   - `id_campus` via `campus_turma` → `nome` em `dim_campus`.
5. **Seleção Final:** Apenas `codigo_turma` (renomeado para `id_turma`), `id_disciplina`, `id_professor`, `id_semestre`, `id_campus`, `matriculados` (renomeado para `num_discentes_matriculados`).
6. **Inserção:** `Table Output` em `estrela.fato_turma`.

**Workflow:** Executa os pipelines de dimensão primeiro, e depois a fato.

---

## 🚀 Como Executar

### Pré-requisitos
- Apache Hop (versão 2.18.1 ou superior) instalado.
- Conexão com o RDS configurada (usando o usuário `etl_user`).
- Arquivos CSV baixados e colocados na pasta `etl/csvs/`.

### Passos

1. **Clone o repositório** e navegue até a pasta `etl/pipelines/`.
2. **Abra o Hop** e carregue o workflow `workflow_estrela.hwf`.
3. **Configure a conexão** com o banco de dados (RDS) no Hop (nome: `RDS_PostgreSQL`).
4. **Execute o workflow**. Ele rodará todas as transformações na ordem correta.
5. **Verifique os resultados** no banco com as consultas abaixo.

### Comandos úteis (via `psql`)

Conectar-se ao RDS:
```bash
psql -h <endpoint-rds> -U etl_user -d engenharia_dados