-- ======================================================================
-- SCRIPT DE CIDADES (Principais cidades de cada estado)
-- Como o Brasil possui 5.570 municípios, este script insere 
-- as capitais e algumas das principais cidades de cada estado 
-- para facilitar os testes no sistema.
-- ======================================================================

use [DA123_Exerc_G08]
go

-- 1: AC (Acre)
INSERT INTO CIDADE (NOMECIDADE, IDESTADO) VALUES ('Rio Branco', 1), ('Cruzeiro do Sul', 1), ('Sena Madureira', 1);

-- 2: AL (Alagoas)
INSERT INTO CIDADE (NOMECIDADE, IDESTADO) VALUES ('Maceio', 2), ('Arapiraca', 2), ('Rio Largo', 2);

-- 3: AP (Amapa)
INSERT INTO CIDADE (NOMECIDADE, IDESTADO) VALUES ('Macapa', 3), ('Santana', 3), ('Laranjal do Jari', 3);

-- 4: AM (Amazonas)
INSERT INTO CIDADE (NOMECIDADE, IDESTADO) VALUES ('Manaus', 4), ('Parintins', 4), ('Itacoatiara', 4);

-- 5: BA (Bahia)
INSERT INTO CIDADE (NOMECIDADE, IDESTADO) VALUES ('Salvador', 5), ('Feira de Santana', 5), ('Vitoria da Conquista', 5);

-- 6: CE (Ceara)
INSERT INTO CIDADE (NOMECIDADE, IDESTADO) VALUES ('Fortaleza', 6), ('Caucaia', 6), ('Juazeiro do Norte', 6);

-- 7: DF (Distrito Federal)
INSERT INTO CIDADE (NOMECIDADE, IDESTADO) VALUES ('Brasilia', 7), ('Taguatinga', 7), ('Ceilandia', 7);

-- 8: ES (Espirito Santo)
INSERT INTO CIDADE (NOMECIDADE, IDESTADO) VALUES ('Vitoria', 8), ('Vila Velha', 8), ('Serra', 8), ('Cariacica', 8), ('Linhares', 8);

-- 9: GO (Goias)
INSERT INTO CIDADE (NOMECIDADE, IDESTADO) VALUES ('Goiania', 9), ('Aparecida de Goiania', 9), ('Anapolis', 9);

-- 10: MA (Maranhao)
INSERT INTO CIDADE (NOMECIDADE, IDESTADO) VALUES ('Sao Luis', 10), ('Imperatriz', 10), ('Sao Jose de Ribamar', 10);

-- 11: MT (Mato Grosso)
INSERT INTO CIDADE (NOMECIDADE, IDESTADO) VALUES ('Cuiaba', 11), ('Varzea Grande', 11), ('Rondonopolis', 11);

-- 12: MS (Mato Grosso do Sul)
INSERT INTO CIDADE (NOMECIDADE, IDESTADO) VALUES ('Campo Grande', 12), ('Dourados', 12), ('Tres Lagoas', 12);

-- 13: MG (Minas Gerais)
INSERT INTO CIDADE (NOMECIDADE, IDESTADO) VALUES ('Belo Horizonte', 13), ('Uberlandia', 13), ('Contagem', 13), ('Juiz de Fora', 13);

-- 14: PA (Para)
INSERT INTO CIDADE (NOMECIDADE, IDESTADO) VALUES ('Belem', 14), ('Ananindeua', 14), ('Santarem', 14);

-- 15: PB (Paraiba)
INSERT INTO CIDADE (NOMECIDADE, IDESTADO) VALUES ('Joao Pessoa', 15), ('Campina Grande', 15), ('Santa Rita', 15);

-- 16: PR (Parana)
INSERT INTO CIDADE (NOMECIDADE, IDESTADO) VALUES ('Curitiba', 16), ('Londrina', 16), ('Maringa', 16);

-- 17: PE (Pernambuco)
INSERT INTO CIDADE (NOMECIDADE, IDESTADO) VALUES ('Recife', 17), ('Jaboatao dos Guararapes', 17), ('Olinda', 17);

-- 18: PI (Piaui)
INSERT INTO CIDADE (NOMECIDADE, IDESTADO) VALUES ('Teresina', 18), ('Parnaiba', 18), ('Picos', 18);

-- 19: RJ (Rio de Janeiro)
INSERT INTO CIDADE (NOMECIDADE, IDESTADO) VALUES ('Rio de Janeiro', 19), ('Sao Goncalo', 19), ('Duque de Caxias', 19), ('Niteroi', 19);

-- 20: RN (Rio Grande do Norte)
INSERT INTO CIDADE (NOMECIDADE, IDESTADO) VALUES ('Natal', 20), ('Mossoro', 20), ('Parnamirim', 20);

-- 21: RS (Rio Grande do Sul)
INSERT INTO CIDADE (NOMECIDADE, IDESTADO) VALUES ('Porto Alegre', 21), ('Caxias do Sul', 21), ('Canoas', 21), ('Pelotas', 21);

-- 22: RO (Rondonia)
INSERT INTO CIDADE (NOMECIDADE, IDESTADO) VALUES ('Porto Velho', 22), ('Ji-Parana', 22), ('Ariquemes', 22);

-- 23: RR (Roraima)
INSERT INTO CIDADE (NOMECIDADE, IDESTADO) VALUES ('Boa Vista', 23), ('Rorainopolis', 23), ('Caracarai', 23);

-- 24: SC (Santa Catarina)
INSERT INTO CIDADE (NOMECIDADE, IDESTADO) VALUES ('Florianopolis', 24), ('Joinville', 24), ('Blumenau', 24), ('Sao Jose', 24);

-- 25: SP (Sao Paulo)
INSERT INTO CIDADE (NOMECIDADE, IDESTADO) VALUES ('Sao Paulo', 25), ('Guarulhos', 25), ('Campinas', 25), ('Sao Bernardo do Campo', 25), ('Santo Andre', 25);

-- 26: SE (Sergipe)
INSERT INTO CIDADE (NOMECIDADE, IDESTADO) VALUES ('Aracaju', 26), ('Nossa Senhora do Socorro', 26), ('Lagarto', 26);

-- 27: TO (Tocantins)
INSERT INTO CIDADE (NOMECIDADE, IDESTADO) VALUES ('Palmas', 27), ('Araguaina', 27), ('Gurupi', 27);
go
