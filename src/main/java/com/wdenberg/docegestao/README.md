### 🍰 Confeitaria Pro API
Uma solução robusta de ERP especializado para confeitarias, focada em precisão financeira, escalabilidade e experiência do usuário final.

### 🚀 Diferenciais de Negócio
Diferente de CRMs genéricos, esta API resolve a dor latente do pequeno produtor: a incerteza do lucro.

Engenharia de Custos: Cálculo automatizado baseado em insumos (gramatura/unidade) + custos fixos + depreciação.

Pricing Inteligente: Sugestão de preço de venda baseada em margem de contribuição desejada.

Catálogo Dinâmico: Vitrine pública para clientes com integração direta ao fluxo de pedidos.

Gestão de Insumos: Lista de compras gerada automaticamente com base nas encomendas da semana.

### 🏗️ Arquitetura e Design
A aplicação utiliza um Modular Monolith com princípios de Clean Architecture. Essa escolha prioriza a manutenibilidade e o baixo custo de infraestrutura inicial, sem abrir mão do desacoplamento necessário para uma futura migração para microserviços.

Organização de Camadas:
api: Entrypoints REST, tratamento de exceções global e mapeamento de DTOs.

application: Orquestração de casos de uso e serviços de domínio.

domain: O coração do software. Contém entidades ricas, objetos de valor (Value Objects) e regras de negócio puras.

infrastructure: Implementações técnicas (Persistence, Security, External APIs).

### 🛠️ Stack Tecnológica (Sênior Selection)TecnologiaPropósitoJava 25 LTSAproveitando as últimas features de performance e Virtual Threads.

Spring Boot 4.0.3Framework base na versão mais atualizada.
Spring Security + JWTAutenticação stateless com suporte a multi-tenancy.
PostgreSQL + FlywayBanco relacional robusto com versionamento de schema.
TestcontainersTestes de integração reais e confiáveis.
MapStructMapeamento de objetos de alta performance (sem reflexão em runtime).
Micrometer + ActuatorObservabilidade e métricas de saúde da aplicação.

### 📊 Modelagem de Dados (High-Level)
A modelagem foi pensada para consistência eventual e integridade referencial:

Multitenancy: Separação lógica de dados por usuário/confeitaria.

Ficha Técnica: Relacionamento N:N entre Produtos e Insumos com controle de desperdício.

Fluxo de Caixa: Registro transacional de despesas e receitas vinculado a pedidos.

### 🔐 Segurança e Governança
RBAC (Role-Based Access Control): Diferenciação entre administradores e assistentes.

Audit Log: Rastreabilidade de quem alterou preços ou deletou pedidos.

Validation: Uso rigoroso de Bean Validation para garantir a integridade antes da persistência.

### 🏃 Como rodar o projeto
Certifique-se de ter o Docker instalado.

Clone o repositório.

Execute o comando: docker-compose up -d

Acesse o Swagger em: http://localhost:8080/swagger-ui.html