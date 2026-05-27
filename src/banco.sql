/*
Grupo Sistema de Pontos:
Gabrielly Abreu
Renan Zorzal Berger
Samuel dos Santos Moura
*/

-- Para executar no SQL Server, selecione o banco desejado:
use [DA123_Exerc_G08]
GO

---+++++++++++++++++++++++++++++++++++
-- CRIA AS TABELAS
---+++++++++++++++++++++++++++++++++++

-- Tabela de estados (usado como dropdown)
CREATE TABLE ESTADO (
    IDESTADO    int          IDENTITY(1,1) NOT NULL,
    SIGLA       varchar(5)   NOT NULL,
    NOMEESTADO  varchar(50)  NOT NULL
);
go

-- Tabela de cidades (usado como dropdown, filtrado pelo estado selecionado)
CREATE TABLE CIDADE (
    IDCIDADE    int          IDENTITY(1,1) NOT NULL,
    NOMECIDADE  varchar(45)  NOT NULL,
    IDESTADO    int          NOT NULL
);
go

-- Tabela de empresas (endereco embutido com colunas diretas)
CREATE TABLE EMPRESA (
    IDEMPRESA          int          IDENTITY(1,1) NOT NULL,
    CNPJ               varchar(18)  NOT NULL,
    RAZAOSOCIAL        varchar(45)  NOT NULL,
    NOMEFANTASIA       varchar(45)  NOT NULL,
    INSCRICAOESTADUAL  varchar(45)  NOT NULL,
    NOMEEMPRESA        varchar(50)  NOT NULL,
    EMAILEMPRESA       varchar(50)  NOT NULL,
    SENHAEMPRESA       varchar(45)  NOT NULL,
    -- Endereco (flat)
    LOGRADOURO         varchar(100) NOT NULL,
    NUMERO             int          NOT NULL,
    COMPLEMENTO        varchar(45)  NULL,
    CEP                varchar(10)  NOT NULL,
    IDCIDADE           int          NOT NULL
);
go

-- Tabela de funcionarios (endereco embutido com colunas diretas)
CREATE TABLE FUNCIONARIO (
    IDFUNCIONARIO    int          IDENTITY(1,1) NOT NULL,
    NOMEFUNCIONARIO  varchar(50)  NOT NULL,
    CPFFUNCIONARIO   varchar(14)  NOT NULL,
    CARGO            varchar(50)  NOT NULL,
    TELEFONE         varchar(30)  NOT NULL,
    EMAILFUNCIONARIO varchar(50)  NOT NULL,
    SENHAFUNCIONARIO varchar(50)  NOT NULL,
    -- Endereco (flat)
    LOGRADOURO       varchar(100) NOT NULL,
    NUMERO           int          NOT NULL,
    COMPLEMENTO      varchar(45)  NULL,
    CEP              varchar(10)  NOT NULL,
    IDCIDADE         int          NOT NULL,
    -- Relacionamentos
    IDEMPRESA        int          NOT NULL
);
go

-- Tabela de pontos
CREATE TABLE PONTO (
    IDPONTO            int        IDENTITY(1,1) NOT NULL,
    HORARIO            varchar(5) NOT NULL,          -- Hora de entrada (HH:mm)
    HORARIOFECHAMENTO  varchar(5) NULL,              -- Hora de saida  (HH:mm), preenchido ao fechar
    DATAPONTO          date       NOT NULL,
    STATUS             varchar(10) NOT NULL,         -- 'ABERTO' ou 'FECHADO'
    IDFUNCIONARIO      int        NOT NULL
);
go

---+++++++++++++++++++++++++++++++++++
-- CRIA AS CONSTRAINTS
---+++++++++++++++++++++++++++++++++++

-- PKs
ALTER TABLE estado      ADD CONSTRAINT estado_idestado_PK            PRIMARY KEY (idestado);
go
ALTER TABLE cidade      ADD CONSTRAINT cidade_idcidade_PK             PRIMARY KEY (idcidade);
go
ALTER TABLE empresa     ADD CONSTRAINT empresa_idempresa_PK           PRIMARY KEY (idempresa);
go
ALTER TABLE funcionario ADD CONSTRAINT funcionario_idfuncionario_PK   PRIMARY KEY (idfuncionario);
go
ALTER TABLE ponto       ADD CONSTRAINT ponto_idponto_PK               PRIMARY KEY (idponto);
go

-- FKs
ALTER TABLE cidade ADD CONSTRAINT cidade_idestado_FK
    FOREIGN KEY (idestado) REFERENCES estado (idestado);
go
ALTER TABLE empresa ADD CONSTRAINT empresa_idcidade_FK
    FOREIGN KEY (idcidade) REFERENCES cidade (idcidade);
go
ALTER TABLE funcionario ADD CONSTRAINT funcionario_idcidade_FK
    FOREIGN KEY (idcidade) REFERENCES cidade (idcidade);
go
ALTER TABLE funcionario ADD CONSTRAINT funcionario_idempresa_FK
    FOREIGN KEY (idempresa) REFERENCES empresa (idempresa);
go
ALTER TABLE ponto ADD CONSTRAINT ponto_idfuncionario_FK
    FOREIGN KEY (idfuncionario) REFERENCES funcionario (idfuncionario);
go

-- UNIQUEs
ALTER TABLE empresa     ADD CONSTRAINT empresa_cnpj_uq                UNIQUE (cnpj);
go
ALTER TABLE empresa     ADD CONSTRAINT empresa_emailempresa_uq        UNIQUE (emailempresa);
go
ALTER TABLE funcionario ADD CONSTRAINT funcionario_cpf_uq             UNIQUE (cpffuncionario);
go
ALTER TABLE funcionario ADD CONSTRAINT funcionario_emailfuncionario_uq UNIQUE (emailfuncionario);
go

-- CHECKs
ALTER TABLE empresa     ADD CONSTRAINT empresa_numero_ck     CHECK (numero >= 0);
go
ALTER TABLE funcionario ADD CONSTRAINT funcionario_numero_ck CHECK (numero >= 0);
go

-- UNIQUE: impede dois pontos no mesmo minuto para o mesmo funcionario no mesmo dia
ALTER TABLE ponto ADD CONSTRAINT ponto_horario_uq UNIQUE (idfuncionario, dataponto, horario);
go

-- CHECK: status apenas ABERTO ou FECHADO
ALTER TABLE ponto ADD CONSTRAINT ponto_status_ck CHECK (status IN ('ABERTO','FECHADO'));
go

-- DEFAULT
ALTER TABLE ponto ADD CONSTRAINT ponto_dataponto_df DEFAULT (getdate()) FOR dataponto;
go
ALTER TABLE ponto ADD CONSTRAINT ponto_status_df    DEFAULT ('ABERTO')  FOR status;
go

---+++++++++++++++++++++++++++++++++++
-- POPULA ESTADOS BRASILEIROS
---+++++++++++++++++++++++++++++++++++

INSERT INTO ESTADO (SIGLA, NOMEESTADO) VALUES
('AC', 'Acre'),
('AL', 'Alagoas'),
('AP', 'Amapa'),
('AM', 'Amazonas'),
('BA', 'Bahia'),
('CE', 'Ceara'),
('DF', 'Distrito Federal'),
('ES', 'Espirito Santo'),
('GO', 'Goias'),
('MA', 'Maranhao'),
('MT', 'Mato Grosso'),
('MS', 'Mato Grosso do Sul'),
('MG', 'Minas Gerais'),
('PA', 'Para'),
('PB', 'Paraiba'),
('PR', 'Parana'),
('PE', 'Pernambuco'),
('PI', 'Piaui'),
('RJ', 'Rio de Janeiro'),
('RN', 'Rio Grande do Norte'),
('RS', 'Rio Grande do Sul'),
('RO', 'Rondonia'),
('RR', 'Roraima'),
('SC', 'Santa Catarina'),
('SP', 'Sao Paulo'),
('SE', 'Sergipe'),
('TO', 'Tocantins');
go


