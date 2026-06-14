CREATE SCHEMA feature_app;

CREATE TYPE tipo_usuario AS ENUM ('admin', 'user');

CREATE TABLE feature_app.usuario(
	id UUID DEFAULT gen_random_uuid(),
	cpf VARCHAR(14) NOT NULL, 
	nome VARCHAR(50) NOT NULL,
	tipo tipo_usuario NOT NULL,
	senha TEXT NOT NULL, 

	CONSTRAINT pk_usuario PRIMARY KEY (id),
	CONSTRAINT uq_cpf UNIQUE (cpf)
);

CREATE TABLE feature_app.dataset(
	id UUID DEFAULT gen_random_uuid(),
	criado_em TIMESTAMP DEFAULT now(), 
	nome VARCHAR(50) NOT NULL,
	usuario_criador UUID NOT NULL, 
	atualizado_em TIMESTAMP NOT NULL, 
	descricao TEXT,
	origem VARCHAR(50),

	CONSTRAINT pk_dataset PRIMARY KEY (id),
	CONSTRAINT fk_dataset_usuario_criador_id FOREIGN KEY (usuario_criador)
		REFERENCES feature_app.usuario(id)
);

CREATE TABLE feature_app.versao_dataset(
	id UUID DEFAULT gen_random_uuid(),
	versao INT NOT NULL, 
	modificacoes TEXT, 
	criado_em TIMESTAMP DEFAULT now(),
	caminho_arquivo TEXT NOT NULL,
	usuario_submetente UUID NOT NULL,
	dataset_id UUID NOT NULL, 
	dataset_versao_pai UUID, 

	CONSTRAINT pk_versao_dataset PRIMARY KEY (id),
	CONSTRAINT fk_versao_dataset_submetente_id FOREIGN KEY (usuario_submetente)
		REFERENCES feature_app.usuario(id),
	CONSTRAINT fk_versao_dataset_dataset_id FOREIGN KEY (dataset_id)
		REFERENCES feature_app.dataset(id),
	CONSTRAINT fk_dataset_versao_pai_id FOREIGN KEY (dataset_versao_pai)
		REFERENCES feature_app.versao_dataset(id),
	CONSTRAINT ck_versao_dataset_pai CHECK (dataset_versao_pai IS NULL OR dataset_versao_pai != id),
	CONSTRAINT un_versao_dataset_version UNIQUE (versao, dataset_id)
);

CREATE TABLE feature_app.download(
	user_id UUID, 
	data_hora TIMESTAMP DEFAULT now(),
	dataset_versao_id UUID NOT NULL,

	CONSTRAINT pk_download PRIMARY KEY (user_id, data_hora),
	CONSTRAINT fk_download_dataset_versao_id FOREIGN KEY (dataset_versao_id)
		REFERENCES feature_app.versao_dataset(id) ON DELETE CASCADE,
	CONSTRAINT fk_download_user_id FOREIGN KEY (user_id)	
		REFERENCES feature_app.usuario(id) ON DELETE CASCADE
);

CREATE TABLE feature_app.acesso(
	user_id UUID, 
	data_hora TIMESTAMP DEFAULT now(),
	dataset_versao_id UUID NOT NULL,

	CONSTRAINT pk_acesso PRIMARY KEY (user_id, data_hora),
	CONSTRAINT fk_acesso_dataset_versao_id FOREIGN KEY (dataset_versao_id)
		REFERENCES feature_app.versao_dataset(id) ON DELETE CASCADE,
	CONSTRAINT fk_acesso_user_id FOREIGN KEY (user_id)	
		REFERENCES feature_app.usuario(id) ON DELETE CASCADE
);

-- POPULA BANCO
INSERT INTO feature_app.usuario (
	cpf,
	nome,
	tipo
) VALUES (
	'000.000.000-00',
	'Gustavo',
	'admin'
);

INSERT INTO feature_app.dataset (
	nome,
	usuario_criador,
	atualizado_em,
	descricao,
	origem
) VALUES (
	'dataset 01',
	'764ce06c-39b1-4362-9d9b-789708b712d8',
	now(),
	'descricao do dataset',
	'alguma origem'
);

-- parent
INSERT INTO feature_app.versao_dataset (
	versao,
	modificacoes,
	caminho_arquivo,
	usuario_submetente,
	dataset_id,
	dataset_versao_pai
) VALUES (
	1,
	'modificacoes', 
	'/home/user/file.csv', 
	'764ce06c-39b1-4362-9d9b-789708b712d8',
	'47473886-489f-4526-9e41-f9d0cc0a1ac7',
	NULL	
);

-- child
INSERT INTO feature_app.versao_dataset (
	versao,
	modificacoes,
	caminho_arquivo,
	usuario_submetente,
	dataset_id,
	dataset_versao_pai
) VALUES (
	2,
	'modificacoes', 
	'/home/user/file2.csv', 
	'764ce06c-39b1-4362-9d9b-789708b712d8',
	'47473886-489f-4526-9e41-f9d0cc0a1ac7',
	'f3a53e2a-7f0a-496a-8831-4fa009262f90'	
);

-- download
INSERT INTO feature_app.download (
	user_id,
	dataset_versao_id
) VALUES (
	'764ce06c-39b1-4362-9d9b-789708b712d8',
	'9b9265c9-4ba4-41f0-a30b-e934d23964d8'
);

-- access
INSERT INTO feature_app.acesso (
	user_id,
	dataset_versao_id
) VALUES (
	'764ce06c-39b1-4362-9d9b-789708b712d8',
	'9b9265c9-4ba4-41f0-a30b-e934d23964d8'
);


-- CONSULTAS
SELECT * FROM feature_app.usuario;
SELECT * FROM feature_app.dataset;
SELECT * FROM feature_app.versao_dataset;
SELECT * FROM feature_app.download;
SELECT * FROM feature_app.acesso;


