# 🕐 MistyPoint — Sistema de Controle de Ponto

<p align="center">
  <img src="https://img.shields.io/badge/Java-JavaFX-orange?style=for-the-badge&logo=java" alt="Java + JavaFX"/>
  <img src="https://img.shields.io/badge/Banco%20de%20Dados-SQL%20Server-blue?style=for-the-badge&logo=microsoftsqlserver" alt="SQL Server"/>
  <img src="https://img.shields.io/badge/Arquitetura-MVC-purple?style=for-the-badge" alt="MVC"/>
  <img src="https://img.shields.io/badge/Status-Conclu%C3%ADdo-brightgreen?style=for-the-badge" alt="Status"/>
</p>

---

## 📋 Sobre o Projeto

**MistyPoint** é um sistema desktop de **controle de ponto eletrônico** desenvolvido em Java com interface gráfica JavaFX. O sistema permite que empresas gerenciem o registro de entrada e saída de seus funcionários de forma simples, centralizada e segura.

A aplicação oferece dois perfis de acesso distintos — **Empresa** e **Funcionário** — cada um com seu próprio fluxo de autenticação e conjunto de funcionalidades.

---

## ✨ Funcionalidades

### 🏢 Perfil Empresa
- Cadastro de nova empresa (CNPJ, Razão Social, Nome Fantasia, Inscrição Estadual, endereço completo)
- Login seguro por e-mail e senha
- Painel home para visualização e gerenciamento de funcionários
- Cadastro de funcionários vinculados à empresa
- Visualização dos registros de ponto de cada funcionário

### 👤 Perfil Funcionário
- Login seguro por e-mail e senha
- Registro de ponto de **entrada** (abertura) e **saída** (fechamento)
- Histórico de pontos com data, horário e status (`ABERTO` / `FECHADO`)

---

## 🏗️ Arquitetura

O projeto segue o padrão **MVC (Model-View-Controller)** com camada DAO para acesso ao banco de dados:

```
src/
├── model/          # Entidades do domínio (Empresa, Funcionário, Estado, etc.)
├── view/           # Telas JavaFX (Landing, Login, Cadastro, Home)
├── controller/     # Controllers que conectam View e DAO
└── dao/            # Data Access Objects — acesso ao banco de dados
```

| Camada       | Responsabilidade                                      |
|--------------|-------------------------------------------------------|
| `model`      | Representação das entidades de negócio                |
| `view`       | Interface gráfica (JavaFX) com animações e estilos    |
| `controller` | Regras de validação e orquestração entre View e DAO   |
| `dao`        | Consultas SQL ao banco de dados SQL Server            |

---

## 🗄️ Banco de Dados

O banco é gerenciado pelo **Microsoft SQL Server** e conta com as seguintes tabelas:

| Tabela        | Descrição                                              |
|---------------|--------------------------------------------------------|
| `ESTADO`      | Lista dos 27 estados brasileiros (sigla e nome)        |
| `EMPRESA`     | Dados cadastrais e de endereço da empresa              |
| `FUNCIONARIO` | Dados do funcionário, cargo, endereço e vínculo        |
| `PONTO`       | Registros de entrada e saída com data, hora e status   |

> O script completo de criação do banco está em [`banco.sql`](banco.sql).

---

## 🛠️ Tecnologias Utilizadas

| Tecnologia              | Versão / Detalhe              |
|-------------------------|-------------------------------|
| Java                    | JDK 11+                       |
| JavaFX                  | Interface gráfica             |
| Microsoft SQL Server    | Banco de dados relacional     |
| JDBC (`mssql-jdbc`)     | 12.4.2 (JRE 11)               |
| Eclipse IDE             | Ambiente de desenvolvimento   |

---

## 🚀 Como Executar

### Pré-requisitos

- JDK 11 ou superior instalado
- Microsoft SQL Server configurado e em execução
- Eclipse IDE (ou outra IDE compatível com JavaFX)

### Passos

1. **Clone ou baixe** o repositório.
2. **Execute o script SQL** `banco.sql` no seu SQL Server para criar o banco `DA123_Exerc_G08` e popular os estados.
3. **Configure a conexão** em `src/model/Conexao.java` com seu host, usuário e senha do SQL Server.
4. **Importe o projeto** no Eclipse como *Existing Java Project*.
5. **Adicione a biblioteca** `libs/mssql-jdbc-12.4.2.jre11.jar` ao Build Path do projeto.
6. **Execute** a classe principal `src/view/Principal.java`.

---

## 👥 Integrantes

| Nome                          | Papel              |
|-------------------------------|--------------------|
| **Gabrielly Abreu Soares Santos** | Desenvolvedora |
| **Renan Zorzal Berger**           | Desenvolvedor  |
| **Samuel dos Santos Moura**       | Desenvolvedor  |

> 📁 Grupo **G08** — Projeto *Sistema de Pontos*

---

<p align="center">
  MistyPoint © 2026
</p>
