# 🗃️ Feature Store

Projeto desenvolvido para a disciplina de **Banco de Dados I**, focado na modelagem e implementação de um catálogo de datasets com versionamento, features e trilha de auditoria de acessos e downloads.


## 📜 Descrição

O sistema funciona como um repositório central de datasets, onde cada dataset possui um **histórico de versões** organizado em árvore (cada versão referencia sua versão pai), permitindo rastrear a evolução dos dados ao longo do tempo. Cada versão de dataset carrega:

- **Features** — Colunas/atributos documentados que compõem aquela versão do dataset
- **Arquivo de dados** — O arquivo (`.csv`) associado à versão, disponível para download
- **Modificações** — Descrição do que mudou em relação à versão anterior

O backend expõe uma API REST responsável por gerenciar usuários, datasets, versões, features, acessos e downloads, persistindo tudo em **PostgreSQL** através de uma camada DAO própria (sem ORM). O frontend consome essa API para oferecer:

- **Dashboard** — Estatísticas de atividade (acessos/downloads diários e por hora) e resumo dos datasets
- **Catálogo de datasets e versões** — Listagem, criação e edição de datasets e suas versões
- **Visualização em grafo** — Árvore de linhagem entre versões de um mesmo dataset, renderizada com React Flow
- **Autenticação** — Login via CPF/senha com sessão baseada em Spring Security


## ⚙️ Tecnologias Utilizadas

**Back-end**
- **Java 17** + **Spring Boot 4** (Web MVC, Security, Validation)
- **PostgreSQL** — Banco de dados relacional
- **JDBC puro** (DAO pattern) — Sem uso de ORM/JPA
- **Lombok** — Redução de boilerplate
- **Maven** — Gerenciamento de dependências e build

**Front-end**
- **Next.js 16** + **React 19**
- **TypeScript**
- **@xyflow/react (React Flow)** — Visualização em grafo do histórico de versões
- **Recharts** — Gráficos do dashboard
- **Tailwind CSS** + **Radix UI / shadcn** — Estilização e componentes
- **Axios** — Consumo da API

**Infraestrutura**
- **Docker Compose** — Orquestração do banco de dados PostgreSQL


## 🚀 Como Executar

### Pré-requisitos

- JDK 17+
- Node.js 20+
- Docker e Docker Compose

### 1. Suba o banco de dados

```bash
cd app/back-end
docker compose up -d
```

### 2. Configure as credenciais do banco

Crie o arquivo `app/back-end/src/main/resources/.env.properties` com:

```properties
PG_URL=localhost:5432/mydb
PG_USER=user
PG_PASSWORD=password
```

> O schema (`db/schema.sql`) e um usuário admin padrão (`cpf: 000.000.000-00`, `senha: admin123`) são criados automaticamente na primeira execução.

### 3. Execute o back-end

```bash
cd app/back-end
./mvnw spring-boot:run
```

A API sobe em `http://localhost:8080`.

### 4. Execute o front-end

```bash
cd app/front-end/my-app
npm install
npm run dev
```

A aplicação fica disponível em `http://localhost:3000`.


## 🏙️ Funcionalidades

- **📦 Gestão de Datasets**
  Criação, edição, listagem e remoção de datasets, cada um com metadados de origem, descrição e criador

- **🌳 Versionamento em Árvore**
  Cada versão de dataset referencia sua versão pai, formando uma linhagem navegável de mudanças

- **🔗 Visualização em Grafo**
  Renderização interativa da árvore de versões de um dataset usando React Flow

- **🧬 Features por Versão**
  Cadastro das colunas/atributos que compõem cada versão do dataset, com descrição individual

- **⬇️ Download de Versões**
  Download do arquivo de uma versão específica, com registro de auditoria (quem baixou e quando)

- **🔐 Controle de Acesso**
  Autenticação via Spring Security (CPF/senha) e registro de acessos por usuário e versão

- **📊 Dashboard de Atividade**
  Estatísticas de acessos e downloads (diários/por hora) e resumo de atividade por dataset


## 📄 Endpoints Principais

| Recurso | Rota base | Descrição |
|---|---|---|
| Auth | `POST /api/auth/login` | Autenticação de usuário |
| Datasets | `/api/dataset` | CRUD de datasets |
| Versões | `/api/dataset-version` | CRUD de versões (`multipart/form-data` na criação, com upload de arquivo) |
| Features | `/api/dataset-version-feature` | Listagem de features por versão |
| Downloads | `/api/dataset-version/{id}/download` | Download do arquivo da versão |
| Acessos | `/api/dataset-version-access` (alias `/api/access`) | Trilha de acessos por usuário/versão |
| Estatísticas | `/api/dataset/stats` | Atividade diária, por hora e resumo por dataset |
| Usuários | `/api/users` | Listagem e criação de usuários |


## 📁 Estrutura do Projeto

```
feature-store-db-I/
│
├── app/
│   ├── back-end/                      # API REST (Spring Boot)
│   │   ├── src/main/java/com/bd_i/feature_store/
│   │   │   ├── controllers/          # Endpoints REST
│   │   │   ├── services/             # Regras de negócio
│   │   │   ├── dao/                  # Acesso a dados (JDBC puro)
│   │   │   ├── model/                # Entidades de domínio
│   │   │   ├── dto/                  # Objetos de requisição/resposta
│   │   │   ├── config/               # Segurança, CORS, inicialização do banco
│   │   │   └── persistence/          # Estratégia de conexão com o Postgres
│   │   ├── src/main/resources/
│   │   │   └── db/schema.sql         # Schema e dados de exemplo
│   │   └── docker-compose.yml        # Container do PostgreSQL
│   │
│   └── front-end/my-app/              # Aplicação web (Next.js)
│       ├── app/
│       │   ├── dashboard/            # Estatísticas e gráficos
│       │   ├── dataset-versions/     # Listagem e grafo de versões
│       │   ├── dataset-version-features/  # Features por versão
│       │   ├── signIn/               # Autenticação
│       │   └── api/                  # Client HTTP e tipos consumindo o back-end
│       └── components/ui/            # Componentes de UI (shadcn)
│
└── docs/                              # Documentação do sistema (descrição, modelagem, relatórios)
```
