CREATE TABLE CLIENTE(
USER_NAME VARCHAR(255),
STATUS BOOLEAN,
IP VARCHAR(15),
PORTA VARCHAR(5),
CONSTRAINT PK_CLIENTE PRIMARY KEY (USER_NAME)
);


CREATE TABLE LISTA_CONTATOS(
USER_NAME VARCHAR(255),
USER_NAME_CONTATO VARCHAR(255),
CONSTRAINT FK_CONTATO FOREIGN KEY (USER_NAME_CONTATO) REFERENCES CLIENTE(USER_NAME),
CONSTRAINT FK_USER_NAME FOREIGN KEY(USER_NAME) REFERENCES CLIENTE(USER_NAME)
);

CREATE TABLE IF NOT EXISTS configuracoes (
  id INT(11) NOT NULL,
  tempo_validacao_keep_alive VARCHAR(10) NULL DEFAULT NULL,
  numero_maximo_keep_off INT(11) NULL DEFAULT NULL,
  PRIMARY KEY (id)
);
--MODIFICAR O ENDERECO IP PARA O ENDERECO IPV4 DA SUA REDE
INSERT INTO CLIENTE VALUES('giones', false, '192.168.0.104', '8083');
INSERT INTO CLIENTE VALUES('eltoman', false, '192.168.0.104','8082');
INSERT INTO CLIENTE VALUES('bacarca', false, '192.168.0.104','8084');
INSERT INTO CLIENTE VALUES('vitao69', false, '192.168.0.104','8085');

INSERT INTO LISTA_CONTATOS VALUES ('giones','eltoman');
INSERT INTO LISTA_CONTATOS VALUES ('giones','bacarca');
INSERT INTO LISTA_CONTATOS VALUES ('giones','vitao69');

INSERT INTO LISTA_CONTATOS VALUES ('eltoman','giones');
INSERT INTO LISTA_CONTATOS VALUES ('eltoman','bacarca');
INSERT INTO LISTA_CONTATOS VALUES ('eltoman','vitao69');

insert into lista_contatos value ('bacarca', 'eltoman');
insert into lista_contatos value ('bacarca', 'giones');
insert into lista_contatos value ('bacarca', 'vitao69');

insert into lista_contatos value ('vitao69', 'eltoman');
insert into lista_contatos value ('vitao69', 'giones');
insert into lista_contatos value ('vitao69', 'bacarca');

INSERT INTO configuracoes  VALUES (1, '100000', 4);