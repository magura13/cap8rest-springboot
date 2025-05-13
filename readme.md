# ♻️ Eco Tracker API

## 📝 Introdução

**Eco Tracker API** é um **projeto didático** criado para alunos de **Análise e Desenvolvimento de Sistemas (FIAP)** explorarem, de ponta a ponta, o desenvolvimento e o deploy de uma API moderna usando:

| Tema | Tecnologia | Por que usar? |
|------|------------|---------------|
| **Backend** | Spring Boot 3 + Java 21 | Framework líder no mercado, padrões REST, código limpo |
| **Persistência** | Spring Data JPA + Hibernate 6 + Oracle 19c | Banco corporativo real — prática além dos bancos “em memória” |
| **Segurança** | JWT (HS256) + Spring Security 6 | Autenticação *stateless*, padrão em micro‑serviços |
| **Infra** | Docker & docker‑compose | Provisionamento reprodutível da app **e** do banco |
| **Build** | Maven Wrapper | Pipeline unificado (`mvnw → Docker`) |

### Objetivo pedagógico

1. **Containerização completa**  
   *Imagem multistage* (Temurin Alpine) + Oracle Free lado a lado.
2. **Pipeline único**  
   `docker compose up --build` já compila, empacota e sobe tudo.
3. **Segurança realista**  
   Tokens JWT de 10 min, filtro customizado, rotas protegidas.
4. **Boas práticas REST**  
   DTOs, status HTTP corretos, paginação opcional (`page` & `size`).
5. **Código limpo e modular**  
   Layers: Controller → Service → Repository → Oracle.

### Como usar para aprender

* **Siga o fluxo**: cria usuário → faz login → usa token para CRUD.
* **Abra no IntelliJ** e navegue pelos pacotes `controller`, `service`, `repository`.
* **Experimente**:
    - adicionar migrações Flyway
    - documentar com Swagger/OpenAPI
    - subir no Heroku/Railway usando a imagem Docker
    - escrever testes de integração (Testcontainers + Oracle)

---

## Índice
1. [Stack & Arquitetura](#stack--arquitetura)
2. [Como rodar](#como-rodar)
3. [Configuração](#configuração)
4. [Endpoints](#endpoints)
5. [Modelagem](#modelagem)
6. [Segurança (JWT)](#segurança-jwt)
7. [Estrutura de pastas](#estrutura-de-pastas)
8. [Rotinas de build & CI](#rotinas-de-build--ci)

---

## Stack & Arquitetura

| Camada            | Tech                               | Notas                                                         |
|-------------------|------------------------------------|--------------------------------------------------------------|
| **REST**          | Spring MVC + Spring Web            | Controllers enxutos, DTOs via `record`                       |
| **Segurança**     | Spring Security 6 + JWT (`jjwt`)   | Filtro `JwtAuthFilter` monta `Authentication` por request    |
| **Persistência**  | Spring Data JPA + Hibernate 6      | Oracle 19c (*oracle‑free:23‑slim*)                           |
| **Build**         | Maven Wrapper (`mvnw`)             | Java 21                                                       |
| **Containers**    | Docker & docker‑compose           | Multistage build (Temurin Alpine)                            |

```
Controller  →  Service  → Repository  → Oracle
               ↑
             JwtAuth
```

---

## Como rodar

```bash
git clone https://github.com/magura13/cap8rest-springboot.git
cd cap8rest-springboot
docker compose up --build -d
docker compose logs -f app
```

| Serviço     | Porta host | URL / Acesso                                  |
|-------------|------------|-----------------------------------------------|
| Spring Boot | **8080**   | <http://localhost:8080>                       |
| Oracle DB   | 1521       | `oracle:1521/123456` (rede Compose interna) |

---

## Configuração

| Variável                | Default (compose)                               | Descrição                                  |
|-------------------------|-------------------------------------------------|--------------------------------------------|
| `SPRING_DATASOURCE_URL` | `--------------------------------------- `      | JDBC URL                                   |
| `SPRING_DATASOURCE_USERNAME` | `       `                                  | usuário DB                                 |
| `SPRING_DATASOURCE_PASSWORD` | `      `                                   | senha DB                                   |
| `SPRING_JWT_SECRET`     | **≥ 32 bytes**                                  | chave HS256                                |
| `SPRING_PROFILES_ACTIVE`| `docker`                                        | ativa `application-docker.properties`      |

---

## Endpoints

> Todos trocam **JSON**.  
> Rotas protegidas necessitam `Authorization: Bearer <token>`.

### Autenticação

| Método | URL          | Body                               | Status / Retorno                     |
|--------|--------------|------------------------------------|--------------------------------------|
| POST   | `/users`     | `{ email, username, password }`    | `201 Created`                        |
| POST   | `/auth/login`| `{ identifier, password }`         | `200 OK` → `{ token, expiresAt }`    |

### Resíduos

| Método | URL                                     | Body                                        | Descrição            |
|--------|-----------------------------------------|---------------------------------------------|----------------------|
| POST   | `/residues`                             | `{ "type":"PLASTIC", "volumeKg":2.3 }`      | Cria resíduo         |
| GET    | `/residues?page=0&size=10`              | —                                           | Lista paginado       |
| PUT    | `/residues/{id}`                        | `{ "type":"GLASS","volumeKg":1.8 }`         | Atualiza por ID      |
| DELETE | `/residues/{id}`                        | —                                           | Remove por ID        |

Erro padrão de token:

```json
HTTP/1.1 401 Unauthorized
{ "message": "Invalid or expired token" }
```

---

## Modelagem

### Tabelas

| app_user |                                     |
|----------|-------------------------------------|
| id (PK)  | identity                            |
| email    | varchar(120) unique                 |
| username | varchar(60) unique                  |
| password_hash | varchar(72) BCrypt             |

| residue          |                                       |
|------------------|---------------------------------------|
| id (PK)          | identity                              |
| type             | varchar(20) enum                      |
| volume_kg        | number                                |
| timestamp        | timestamp default now                 |
| user_id (FK)     | app_user(id) — dono do lançamento     |

---

## Segurança (JWT)

* **Assinatura** : HS256
* **TTL** : 10 min
* **Filtro** : `JwtAuthFilter`
* **401** : token ausente, inválido ou expirado
* **403** : tentativa de alterar/deletar registro de outro usuário

---

## Estrutura de pastas

```
src/main/java/br/com/fiap/lelis/cap8rest_springboot
├─ config
│  ├─ JwtConfig.java
│  ├─ SecurityConfig.java
│  └─ security/JwtAuthFilter.java
├─ controller
│  ├─ UserController.java
│  ├─ AuthController.java
│  └─ ResidueController.java
├─ dto
│  ├─ CreateUserRequest.java
│  ├─ LoginRequest/Response.java
│  └─ ResidueRequest/Response.java
├─ exception (BusinessException, RestExceptionHandler)
├─ model
│  ├─ entity (AppUser, Residue)
│  └─ repository (AppUserRepository, ResidueRepository)
├─ service (UserService, AuthService, ResidueService)
└─ util (JwtUtil)
```

---

## Rotinas de build & CI

| Ação             | Comando                                         |
|------------------|-------------------------------------------------|
| Build local      | `./mvnw clean package`                          |
| Testes           | `./mvnw test`                                   |
| Imagem Docker    | `docker build -t eco-tracker-app .`             |
| Compose          | `docker compose up -d`                          |

Exemplo de GitHub Actions:

```yaml
jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v4
        with: { distribution: temurin, java-version: "21" }
      - run: ./mvnw -B test
      - uses: docker/build-push-action@v5
        with:
          push: true
          tags: ghcr.io/your-org/eco-tracker:latest
```

---