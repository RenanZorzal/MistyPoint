-- ============================================================
-- MIGRATION: adapta EMPRESA e FUNCIONARIO para endereco flat
-- Execute SOMENTE se o banco ja existir com a estrutura antiga
-- ============================================================

-- 1. Adicionar colunas de endereco flat na tabela EMPRESA
ALTER TABLE empresa ADD logradouro   varchar(100) NULL;
go
ALTER TABLE empresa ADD numero       int          NULL;
go
ALTER TABLE empresa ADD complemento  varchar(45)  NULL;
go
ALTER TABLE empresa ADD cep          varchar(10)  NULL;
go
ALTER TABLE empresa ADD idcidade     int          NULL;
go

-- 2. Adicionar colunas de endereco flat na tabela FUNCIONARIO
--    (remover idendereco que nao existe mais)
ALTER TABLE funcionario ADD logradouro   varchar(100) NULL;
go
ALTER TABLE funcionario ADD numero       int          NULL;
go
ALTER TABLE funcionario ADD complemento  varchar(45)  NULL;
go
ALTER TABLE funcionario ADD cep          varchar(10)  NULL;
go
ALTER TABLE funcionario ADD idcidade     int          NULL;
go

-- 3. (Opcional) Remover coluna idendereco de FUNCIONARIO, se existir
-- ALTER TABLE funcionario DROP COLUMN idendereco;
-- go

-- 4. FKs para idcidade
ALTER TABLE empresa     ADD CONSTRAINT empresa_idcidade_FK
    FOREIGN KEY (idcidade) REFERENCES cidade (idcidade);
go
ALTER TABLE funcionario ADD CONSTRAINT funcionario_idcidade_FK
    FOREIGN KEY (idcidade) REFERENCES cidade (idcidade);
go

-- 5. CHECK de numero
ALTER TABLE empresa     ADD CONSTRAINT empresa_numero_ck     CHECK (numero >= 0);
go
ALTER TABLE funcionario ADD CONSTRAINT funcionario_numero_ck CHECK (numero >= 0);
go
