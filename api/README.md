# ⚙️ API - Observador de Preços

Backend Spring Boot da aplicação Observador de Preços. API REST com autenticação JWT, banco PostgreSQL e documentação OpenAPI.

## 📂 Organização de Pastas

```
api/src/main/java/com/luanpaiva/observador_de_precos/
├── config/                      # Configurações da aplicação
│   └── CorsConfig.java          # Política CORS para frontend
│
├── controller/                  # Controllers REST
│   ├── AuthController.java      # Endpoints de autenticação
│   ├── UserController.java      # Endpoints de usuários
│   └── TestController.java      # Endpoints de teste
│
├── modules/                     # Módulos de negócio
│   ├── auth/                    # Módulo de autenticação
│   │   ├── controller/          # Controllers do módulo
│   │   ├── dto/                 # Data Transfer Objects
│   │   │   ├── AuthResponseDTO.java
│   │   │   ├── LoginRequestDTO.java
│   │   │   └── RegisterRequestDTO.java
│   │   ├── mapper/              # Mapeadores DTO ↔ Entity
│   │   └── service/             # Lógica de negócio
│   │       └── AuthService.java
│   │
│   └── users/                   # Módulo de usuários
│       ├── controller/
│       │   └── UserController.java
│       ├── dto/
│       │   └── UserResponseDTO.java
│       ├── entity/              # Entidades JPA
│       │   └── User.java
│       ├── mapper/              # Mapeadores
│       ├── repository/          # Spring Data JPA
│       │   └── UserRepository.java
│       └── service/             # Lógica de negócio
│
├── security/                    # Segurança e autenticação
│   ├── CustomUserDetailsService.java  # Implementa UserDetailsService
│   ├── JwtAuthenticationFilter.java   # Filtro JWT
│   ├── JwtService.java          # Geração e validação de tokens
│   └── SecurityConfig.java      # Configuração Spring Security
│
└── shared/                      # Utilitários compartilhados

api/src/main/resources/
├── application.yml              # Configuração principal
├── application-test.yml         # Configuração para testes
└── db/migration/                # Scripts Flyway
    └── V1__create_users_table.sql

api/src/test/java/com/luanpaiva/observador_de_precos_test/
├── unit/                        # Testes unitários
│   ├── AuthServiceRegisterTests.java
│   ├── AuthControllerRegisterTests.java
│   ├── RegisterRequestDTOTests.java
│   ├── UserEntityTests.java
│   └── UserRepositoryTests.java
├── integration/                 # Testes de integração
└── e2e/                         # Testes end-to-end
```

## 📦 Instalação

```bash
cd api

# Instalar dependências e fazer build
mvn clean install
```

## ▶️ Desenvolvimento

```bash
# Iniciar servidor Spring Boot
mvn clean spring-boot:run

# API disponível em: http://localhost:8080
# Swagger/OpenAPI: http://localhost:8080/swagger-ui.html
# Documentação OpenAPI: http://localhost:8080/v3/api-docs
```

## 🧪 Testes

```bash
# Executar todos os testes
mvn test -Dtest="*Tests"

# Executar testes específicos
mvn test -Dtest=AuthServiceRegisterTests
mvn test -Dtest=UserRepositoryTests

# Testes com relatório de cobertura
mvn test jacoco:report

# Visualizar relatório de cobertura
# Abra: target/site/jacoco/index.html
```

## 🔨 Comandos Maven

```bash
# Build completo com testes
mvn clean install

# Build sem executar testes
mvn clean install -DskipTests

# Iniciar servidor
mvn clean spring-boot:run

# Análise de qualidade de código
mvn clean verify

# Gerar documentação do projeto
mvn javadoc:javadoc

# Empacotar para deployment
mvn clean package

# Deploy em produção (sem testes)
mvn clean package -DskipTests
```

## 🗄️ Banco de Dados

### Configuração PostgreSQL

```bash
# Criar banco de dados
createdb observador_precos

# Conectar ao banco
psql -U postgres -d observador_precos
```

### Migrações Flyway

As migrações SQL executam automaticamente na primeira execução:

```
db/migration/
└── V1__create_users_table.sql
```

Adicione novas migrações com o padrão: `VN__descricao.sql`

## 📋 Configuração Spring Boot

### application.yml (Desenvolvimento)

```yaml
spring:
  application:
    name: observador-de-precos
  
  datasource:
    url: jdbc:postgresql://localhost:5432/observador_precos
    username: postgres
    password: sua_senha
    driver-class-name: org.postgresql.Driver
  
  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: true
    properties:
      hibernate:
        format_sql: true
        dialect: org.hibernate.dialect.PostgreSQLDialect
  
  security:
    jwt:
      secret: sua_chave_secreta_super_segura_aqui
      expiration: 86400000  # 24 horas em ms

server:
  port: 8080
  servlet:
    context-path: /api
```

### application-test.yml (Testes)

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/observador_precos_test
    username: postgres
    password: sua_senha
  jpa:
    hibernate:
      ddl-auto: create-drop
```

## 🔐 Autenticação JWT

### Fluxo de Autenticação

1. **Login**: Cliente envia credenciais (email/senha)
2. **Validação**: `AuthService` valida credenciais
3. **Token**: `JwtService` gera JWT token
4. **Resposta**: Token retornado ao cliente
5. **Requisições**: Cliente inclui token no header `Authorization: Bearer <token>`
6. **Filtro**: `JwtAuthenticationFilter` valida token em cada requisição

### Configuração JWT

- **Secret**: Chave secreta para assinar tokens (mínimo 32 caracteres)
- **Expiration**: Tempo de expiração (padrão 24 horas)
- **Algorithm**: HS256
- **Claims**: Incluem userId, email, authorities

### Proteção de Endpoints

```java
// SecurityConfig.java
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .authorizeHttpRequests(authz -> authz
            .requestMatchers("/auth/**").permitAll()
            .anyRequest().authenticated()
        )
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        .csrf().disable();
    
    return http.build();
}
```

## 📚 Stack Técnico

| Tecnologia | Versão | Propósito |
|-----------|--------|----------|
| Spring Boot | 4.0.6 | Framework REST |
| Java | 21 | Linguagem |
| PostgreSQL | 15+ | Banco de dados |
| Spring Security | - | Autenticação/Autorização |
| Spring Data JPA | - | ORM e persistência |
| Hibernate | - | Mapping objeto-relacional |
| Flyway | - | Migração de banco de dados |
| OpenAPI/Swagger | 2.8.9 | Documentação API |
| JUnit 5 | - | Framework de testes |

## 🏗️ Arquitetura

### Camadas

```
Controller (REST)
    ↓
Service (Lógica de Negócio)
    ↓
Repository (Acesso a Dados)
    ↓
Database
```

### Módulos

Cada módulo (`auth`, `users`) é autocontido com:
- Controllers
- DTOs
- Entities
- Repositories
- Services
- Mappers

## 🔀 CORS Configuration

O CORS está configurado em `CorsConfig.java` para aceitar requisições do frontend:

```yaml
# Configurações CORS
allowed-origins: http://localhost:4200,https://observador-precos.com
allowed-methods: GET,POST,PUT,DELETE,OPTIONS
allowed-headers: Content-Type,Authorization
allow-credentials: true
```

## 📊 Documentação API

A API é documentada com OpenAPI (Swagger). Acesse em:

```
http://localhost:8080/swagger-ui.html
```

Anotações disponíveis:
- `@Operation`: Descreve uma operação
- `@Parameter`: Descreve um parâmetro
- `@ApiResponse`: Descreve uma resposta
- `@Schema`: Descreve um modelo de dados

## 🐛 Debugging

```bash
# Debug mode
mvn spring-boot:run -Dspring-boot.run.arguments="--debug"

# Logs detalhados
export JAVA_TOOL_OPTIONS=-Ddebug=true
```

## 📝 Padrões de Código

- **Naming**: camelCase para variáveis, PascalCase para classes
- **Convenção**: Google Java Style Guide
- **Estrutura**: Package-by-feature
- **Exceções**: Custom exceptions para erros de negócio
- **Logging**: SLF4J com Logback

## 🚀 Deploy

```bash
# Gerar JAR executável
mvn clean package -DskipTests

# Executar JAR
java -jar target/observador-de-precos-0.0.1-SNAPSHOT.jar
```

## 🤝 Contribuindo

1. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
2. Commit com mensagens descritivas
3. Push para a branch
4. Abra um Pull Request

---

**Última atualização:** Maio de 2026
