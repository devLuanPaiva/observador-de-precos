# 📊 Observador de Preços

> **Versão:** 0.1

Um sistema inteligente de monitoramento de preços que realiza scraping automático de produtos em plataformas de e-commerce (Amazon, Mercado Livre, etc.) e acompanha variações de preço ao longo do tempo.

## 🎯 Visão Geral

O **Observador de Preços** permite que usuários autenticados cadastrem URLs de produtos de diferentes plataformas de e-commerce. O sistema executa scraping periódico desses produtos, armazena histórico de preços e fornece insights sobre variações, tendências e melhores momentos para compra.

### Principais Características
- ✅ Autenticação e gerenciamento de usuários seguro
- ✅ Cadastro de produtos via URL de e-commerce
- ✅ Scraping automático e periódico de preços
- ✅ Histórico completo de variações de preço
- ✅ Monitoramento em tempo real com programação reativa
- ✅ Interface responsiva e intuitiva

---

## 🛠️ Stack Tecnológico

### Frontend (Cliente)
- **Framework**: Angular 21
- **Gerenciamento de Estado**: NgRx com Signals
- **Programação Reativa**: RxJS
- **Linguagem**: TypeScript 5.9
- **Build Tool**: Vite + Angular CLI
- **Testes**: Vitest + Jasmine
- **Server Side Rendering**: Angular SSR

### Backend (API)
- **Framework**: Spring Boot 4.0.6
- **Linguagem**: Java 21
- **Banco de Dados**: PostgreSQL
- **ORM**: JPA/Hibernate
- **Autenticação**: Spring Security + JWT
- **Documentação**: OpenAPI/Swagger
- **Build Tool**: Maven

### Orquestração de Scraping
- **Motor de Automação**: N8N
- Workflows customizados para diferentes plataformas
- Execução agendada e confiável de scraping

---

## 📁 Estrutura do Projeto

```
observador-de-precos/
├── api/                          # Backend Spring Boot
│   ├── src/
│   ├── pom.xml
│   ├── mvnw/mvnw.cmd
│   └── README.md                 # Documentação detalhada da API
│
├── client/                       # Frontend Angular
│   ├── src/
│   ├── angular.json
│   ├── package.json
│   └── README.md                 # Documentação detalhada do Cliente
│
├── docker-compose.yml            # Orquestração de serviços
└── README.md                     # Este arquivo (visão geral)
```

---

## 🚀 Quick Start

### Pré-requisitos
- Node.js 20+
- npm 10+
- Java 21
- PostgreSQL 15+
- Maven 3.9+ (ou usar Maven Wrapper)

### Setup Rápido

```bash
# Clone o repositório
git clone <repo-url>
cd observador-de-precos

# Inicie os serviços com Docker Compose
docker-compose up -d
```

### Setup Manual (Desenvolvimento)

```bash
# Terminal 1 - Backend
cd api
mvn clean spring-boot:run
# API disponível em http://localhost:8080

# Terminal 2 - Frontend
cd client
npm install
npm start
# Frontend disponível em http://localhost:4200
```

---

## 📚 Documentação Detalhada

- **[Cliente (Angular)](./client/README.md)** - Instruções de desenvolvimento, instalação, testes e variáveis de ambiente
- **[API (Spring Boot)](./api/README.md)** - Instruções de desenvolvimento, comandos Maven, configuração e autenticação

---

## 🔄 Integração N8N

O N8N é responsável pela orquestração do scraping de preços:

- **Workflows**: Customizados por plataforma de e-commerce
- **Agendamento**: Execuções periódicas configuráveis
- **Webhook**: Integração com API de produtos
- **Logs**: Rastreamento completo de execuções

---

## 🐳 Docker Compose

```bash
# Iniciar todos os serviços
docker-compose up -d

# Parar serviços
docker-compose down

# Visualizar logs
docker-compose logs -f
```

**Serviços:**
- PostgreSQL (porta 5432)
- Spring Boot API (porta 8080)
- Angular Frontend (porta 4200)
- N8N (porta 5678)

---

## 🤝 Contribuindo

1. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
2. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
3. Push para a branch (`git push origin feature/AmazingFeature`)
4. Abra um Pull Request

### Padrões de Código
- Frontend: Angular Style Guide + Prettier
- Backend: Google Java Style Guide
- Commits: Conventional Commits

---

## 📄 Licença

Este projeto está licenciado sob a MIT License - veja o arquivo LICENSE para detalhes.

---

## 👥 Autor

**Luan Paiva** - Desenvolvedor Full Stack

---

## 📞 Suporte

Para dúvidas ou sugestões, abra uma issue ou entre em contato através do email do projeto.

---

**Última atualização:** Maio de 2026
