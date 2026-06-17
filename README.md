# DocType Backend

API backend do sistema DocType, desenvolvida com Spring Boot.

## Visão geral

O projeto expõe endpoints REST para autenticação, usuários, documentos, atividades, dashboard, tipos de documento e relatórios. A aplicação usa JWT para proteger a maior parte das rotas.

## Tecnologias

- Java 17
- Spring Boot 3.2
- Spring Web
- Spring Security
- Spring Data JPA
- MySQL
- JWT

## Como rodar

1. Abra um terminal na raiz do projeto.
2. Execute:

```bash
./mvnw spring-boot:run
```

No Windows, use:

```powershell
./mvnw.cmd spring-boot:run
```

A aplicação sobe em `http://localhost:8081`.

## Configuração local

O backend está configurado para conectar em um MySQL local com as credenciais abaixo:

- Banco: `cadastro_documentos`
- Usuário: `root`
- Senha: `12345678`

A configuração fica em [src/main/resources/application.properties](src/main/resources/application.properties).

## Autenticação

As rotas `POST /api/auth/login` e `POST /api/auth/registro` são públicas.

As demais rotas protegidas exigem token JWT no header:

```http
Authorization: Bearer <token>
```

## Conta inicial

- Email: `admin@doctype.com`
- Senha: `123456`

## Endpoints principais

- `GET /api/health`
- `POST /api/auth/login`
- `POST /api/auth/registro`
- `GET /api/usuarios`
- `GET /api/usuarios/me`
- `POST /api/usuarios`
- `PUT /api/usuarios/{id}`
- `GET /api/documentos`
- `GET /api/documentos/meus-documentos`
- `GET /api/documentos/{id}`
- `GET /api/documentos/protocolo/{protocolo}`
- `POST /api/documentos`
- `GET /api/atividades/recentes`
- `GET /api/dashboard/resumo`
- `GET /api/tipos-documento`
- `POST /api/tipos-documento`
- `GET /api/relatorios/documentos`

## Observações

- O servidor está configurado na porta `8081`.
- A aplicação libera arquivos estáticos e a página inicial pelo próprio Spring Boot.