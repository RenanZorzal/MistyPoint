-- ============================================================
-- MIGRATION: adiciona STATUS e HORARIOFECHAMENTO na tabela PONTO
-- Execute no banco existente caso ele ja exista
-- ============================================================

-- 1. Adiciona coluna de horario de saida (nullable)
ALTER TABLE ponto ADD horariofechamento varchar(5) NULL;
go

-- 2. Adiciona coluna de status com default ABERTO
ALTER TABLE ponto ADD status varchar(10) NOT NULL DEFAULT 'ABERTO';
go

-- 3. Preenche status FECHADO para pontos ja existentes
UPDATE ponto SET status = 'FECHADO' WHERE status IS NULL OR status = '';
go

-- 4. CHECK: so aceita ABERTO ou FECHADO
ALTER TABLE ponto ADD CONSTRAINT ponto_status_ck CHECK (status IN ('ABERTO','FECHADO'));
go

-- 5. UNIQUE: impede dois pontos no mesmo minuto para o mesmo funcionario no mesmo dia
ALTER TABLE ponto ADD CONSTRAINT ponto_horario_uq UNIQUE (idfuncionario, dataponto, horario);
go
