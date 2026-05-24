# 📱 Cliente - Observador de Preços

Frontend Angular da aplicação Observador de Preços com programação reativa usando NgRx, RxJS e Signals.

## 📂 Organização de Pastas

```
client/src/
├── app/
│   ├── core/                    # Serviços singleton, guards e interceptadores globais
│   │   ├── guards/              # Route guards (auth, unsaved changes)
│   │   └── interceptors/        # HTTP interceptadores (headers, errors)
│   │
│   ├── features/                # Módulos de funcionalidades
│   │   ├── auth/
│   │   │   ├── guards/          # Auth guard (protege rotas)
│   │   │   ├── interceptors/    # JWT token injection
│   │   │   ├── models/          # Interfaces (LoginRequest, AuthResponse)
│   │   │   ├── pages/           # Login e Register pages
│   │   │   ├── services/        # AuthService (chamadas API)
│   │   │   └── store/           # NgRx actions, reducers, selectors
│   │   │
│   │   └── products/            # Gerenciamento de produtos
│   │       ├── models/          # Interfaces de produtos
│   │       ├── pages/           # Product list e detail pages
│   │       ├── services/        # ProductService
│   │       └── store/           # Estado de produtos
│   │
│   ├── layout/                  # Componentes de layout (header, footer, sidebar)
│   ├── shared/                  # Componentes e pipes reutilizáveis
│   └── store/                   # Estado NgRx global
│
├── environments/                # Configurações por ambiente
│   ├── environment.ts           # Produção
│   └── environment.development.ts
│
├── styles/                      # Estilos SCSS
│   ├── abstracts/               # Variáveis, mixins, funções
│   ├── base/                    # Estilos globais
│   └── components/              # Estilos de componentes
│
├── types.d.ts                   # Definições de tipos globais
├── main.ts                      # Entrypoint da aplicação
├── main.server.ts               # Entrypoint SSR
├── server.ts                    # Configuração servidor Express
└── index.html
```

## 📦 Instalação

```bash
cd client

# Instalar dependências
npm install
```

## ▶️ Desenvolvimento

```bash
# Iniciar servidor de desenvolvimento
npm start

# Acesso em: http://localhost:4200
```

## 🧪 Testes

```bash
# Executar testes unitários
npm test

# Testes com cobertura
npm test -- --code-coverage
```

## 🏗️ Build

```bash
# Build para produção
npm run build

# Build com SSR
npm run build -- --ssr

# Servir SSR localmente
npm run serve:ssr:client
```

## 🌍 Variáveis de Ambiente

### Define Options (types.d.ts)

```typescript
// src/types.d.ts
declare const api_url: string;  // URL da API Backend
```

### Configuração por Ambiente

**Produção** - `src/environments/environment.ts`:
```typescript
export const environment = {
  production: true,
  apiUrl: 'https://api.observador-precos.com'
};
```

**Desenvolvimento** - `src/environments/environment.development.ts`:
```typescript
export const environment = {
  production: false,
  apiUrl: 'http://localhost:8080/api'
};
```

## 📚 Stack de Desenvolvimento

| Tecnologia | Versão | Propósito |
|-----------|--------|----------|
| Angular | 21.2 | Framework Web |
| TypeScript | 5.9 | Linguagem tipada |
| NgRx | 21.1 | Gerenciamento de estado |
| RxJS | 7.8 | Programação reativa |
| Angular Signals | - | Reatividade com signals |
| Vitest | 4.0 | Testes unitários |
| Jasmine | 6.0 | Assertions nos testes |
| Angular SSR | 21.2 | Server-Side Rendering |

## 🏗️ Arquitetura

### Estrutura de Features

Cada feature (`auth`, `products`) segue a seguinte estrutura:

```
feature/
├── models/           # Interfaces e tipos TypeScript
├── pages/            # Componentes de página (smart components)
├── services/         # Serviços que chamam a API
├── guards/           # Route guards
├── interceptors/     # HTTP interceptadores
└── store/            # NgRx (actions, reducers, effects, selectors)
```

### Programação Reativa

- **RxJS**: Observables para requisições HTTP e event handling
- **NgRx**: Gerenciamento centralizado de estado
- **Signals**: Reatividade granular com o novo sistema de signals do Angular

### Guards

- **AuthGuard**: Protege rotas autenticadas, redireciona para login
- **Interceptadores**: Injetam JWT token em todas requisições HTTP

## 🔐 Autenticação

1. Usuário faz login na página de login
2. `AuthService` envia credenciais para a API
3. API retorna JWT token
4. Token é armazenado localmente
5. `AuthInterceptor` injeta o token em todas as requisições
6. Token é validado em cada requisição

## 🎨 Estilos

Os estilos seguem a arquitetura SCSS modularizada:

```
styles/
├── abstracts/
│   ├── _colors.scss      # Paleta de cores
│   ├── _typography.scss  # Estilos de tipografia
│   └── _variables.scss   # Variáveis reutilizáveis
├── base/
│   └── _global.scss      # Estilos globais
└── components/
    ├── _buttons.scss
    └── _inputs.scss
```

## 🚀 Deploy

```bash
# Build de produção
npm run build

# Teste o build localmente
npm run serve:ssr:client

# Pronto para deploy em servidor Node.js
```

## 🐛 Debugging

```bash
# Usar Angular DevTools (extensão Chrome)
# Usar Redux DevTools (extensão Chrome para NgRx)
# Usar Chrome DevTools para debugging de Network
```

## 📝 Padrões de Código

- **Naming**: camelCase para variáveis, PascalCase para classes/componentes
- **Formatação**: Prettier (execute `npm run prettier`)
- **Linting**: Angular Style Guide
- **TypeScript**: Strict mode habilitado

## 🤝 Contribuindo

1. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
2. Commit com mensagens descritivas
3. Push para a branch
4. Abra um Pull Request

---

**Última atualização:** Maio de 2026
