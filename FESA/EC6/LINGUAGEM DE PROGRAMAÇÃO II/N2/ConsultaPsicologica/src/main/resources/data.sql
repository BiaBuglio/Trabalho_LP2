-- Criação da tabela de usuários com constraints
CREATE TABLE IF NOT EXISTS usuarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    nome VARCHAR(100) NOT NULL,
    perfil VARCHAR(20) NOT NULL CHECK (perfil IN ('ADMIN','PSICOLOGO','PACIENTE','RECEPCIONISTA')),
    ativo BOOLEAN NOT NULL DEFAULT true,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT email_format CHECK (email LIKE '%@%.%')
);

-- Índices para otimização
CREATE INDEX idx_usuario_email ON usuarios(email);
CREATE INDEX idx_usuario_perfil ON usuarios(perfil);

-- Inserção de usuários iniciais (senhas criptografadas com BCrypt)
-- Senha padrão: 'senha123'
INSERT INTO usuarios (email, senha, nome, perfil) VALUES 
('admin@clinica.com', '$2a$10$XptfskLsT1l/bRTLRiiCgejHqOpgXFreUnNUa35gJdCr2v2QbVFzu', 'Administrador', 'ADMIN'),
('psicologo@clinica.com', '$2a$10$XptfskLsT1l/bRTLRiiCgejHqOpgXFreUnNUa35gJdCr2v2QbVFzu', 'Dr. Psicólogo', 'PSICOLOGO'),
('paciente@clinica.com', '$2a$10$XptfskLsT1l/bRTLRiiCgejHqOpgXFreUnNUa35gJdCr2v2QbVFzu', 'João Paciente', 'PACIENTE');

-- Tabela de auditoria de login (opcional)
CREATE TABLE IF NOT EXISTS log_acessos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    data_hora TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    endereco_ip VARCHAR(45),
    acao VARCHAR(20) NOT NULL CHECK (acao IN ('LOGIN','LOGOUT','FALHA_LOGIN')),
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
);

-- CREATE: Inserir novo usuário
INSERT INTO usuarios (email, senha, nome, perfil)
VALUES ('novo.usuario@clinica.com', '$2a$10$HASHDA.SENHA.AQUI', 'Nome do Usuário', 'PACIENTE');

-- READ: Listar todos os usuários
SELECT * FROM usuarios;

-- READ: Buscar usuário por ID
SELECT * FROM usuarios WHERE id = 1;

-- READ: Buscar usuário por e-mail
SELECT * FROM usuarios WHERE email = 'admin@clinica.com';

-- READ: Buscar usuários ativos por perfil
SELECT * FROM usuarios WHERE ativo = true AND perfil = 'PSICOLOGO';

-- UPDATE: Atualizar nome e perfil do usuário
UPDATE usuarios
SET nome = 'Novo Nome', perfil = 'RECEPCIONISTA'
WHERE id = 2;

-- UPDATE: Atualizar senha (deve estar criptografada)
UPDATE usuarios
SET senha = '$2a$10$NOVOHASHDA.SENHA'
WHERE id = 3;

-- UPDATE: Inativar usuário (exclusão lógica)
UPDATE usuarios
SET ativo = false
WHERE id = 3;

-- UPDATE: Reativar usuário
UPDATE usuarios
SET ativo = true
WHERE id = 3;

-- DELETE: Remover usuário permanentemente
DELETE FROM usuarios WHERE id = 3;
