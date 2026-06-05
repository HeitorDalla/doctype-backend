# DocType Backend

Backend Spring Boot para o frontend estático em `frontend/`.

Na execução local, o próprio Spring Boot pode servir o frontend na raiz `http://localhost:8081/`.

## Como rodar o backend

1. Abra um terminal na pasta raiz do projeto.
2. Rode:

```bash
./mvnw spring-boot:run
```

No Windows PowerShell ou CMD, você também pode usar:

```powershell
./mvnw.cmd spring-boot:run
```

O backend sobe em `http://localhost:8081`.

## Como rodar o frontend

O frontend pode ser servido de duas formas:

1. Pelo Spring Boot, acessando `http://localhost:8081/` depois de subir o backend.
2. Separado, se o terminal já estiver dentro de `frontend/`, rode:

```bash
python -m http.server 5500
```

Se você estiver na pasta raiz do projeto, use:

```bash
python -m http.server 5500 -d frontend
```

Se usar o servidor separado, abra `http://localhost:5500/`. Se usar o Spring Boot, abra `http://localhost:8081/`.

## Fluxo de teste

1. Abra o frontend.
2. Registre um usuário em `register.html` ou use a conta inicial abaixo.
3. Faça login.
4. O dashboard e a tela de perfil já consomem a API local.

## Conta inicial

- Email: `admin@doctype.com`
- Senha: `123456`

## Endpoints principais

- `POST /api/auth/login`
- `POST /api/auth/registro`
- `GET /api/usuarios/me`
- `GET /api/documentos/meus-documentos`
- `GET /api/atividades/recentes`
- `GET /api/dashboard/resumo`
- `GET /api/relatorios/documentos`