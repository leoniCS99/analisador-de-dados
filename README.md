# Analisador de Dados

[![CircleCI](https://circleci.com/gh/leoniCS99/analisador-de-dados.svg?style=shield)](https://circleci.com/gh/leoniCS99/analisador-de-dados)

[![codecov](https://codecov.io/gh/leoniCS99/analisador-de-dados/branch/master/graph/badge.svg)](https://codecov.io/gh/leoniCS99/analisador-de-dados)

---

## Objetivo

Aplicação desenvolvida em **Java 17** para processamento de arquivos de vendas no formato `.dat`.

A aplicação monitora continuamente um diretório de entrada, identifica novos arquivos, realiza o processamento de seus registros e gera automaticamente um relatório contendo:

- Quantidade de clientes
- Quantidade de vendedores
- ID da venda mais cara
- Pior vendedor

Os arquivos processados são gravados automaticamente no diretório de saída com o sufixo `.done.dat`.

---

# Tecnologias

- Java 17
- Maven
- JUnit 5
- Mockito
- JaCoCo
- CircleCI
- Codecov

---

# Estrutura do Projeto

```
src
├── main
│   ├── java
│   │   └── br.com.agi
│   │       ├── enums
│   │       ├── model
│   │       ├── service
│   │       └── util
│   └── resources
│
└── test
    ├── java
    │   ├── integration
    │   └── service
    └── resources
        └── stub
```

---

# Arquitetura

O projeto foi desenvolvido seguindo princípios de **Clean Code** e **SOLID**, mantendo uma arquitetura simples, coesa e adequada ao escopo do desafio.

Cada componente possui responsabilidade única.

## DirectoryWatcher

Responsável pelo monitoramento contínuo do diretório de entrada utilizando a API `WatchService`.

Sempre que um novo arquivo `.dat` é encontrado, seu processamento é iniciado de forma assíncrona utilizando `ExecutorService`.

---

## FileProcessor

Responsável por:

- leitura do arquivo;
- parsing dos registros;
- conversão dos dados para objetos de domínio;
- tratamento de linhas inválidas;
- geração do relatório.

---

## ReportService

Centraliza toda a regra de negócio da aplicação.

Calcula:

- quantidade de clientes;
- quantidade de vendedores;
- venda mais cara;
- pior vendedor.

---

## FileWriterUtil

Responsável exclusivamente pela geração do arquivo de saída.

---

## RecordType

Enum utilizado para eliminar códigos literais (`001`, `002` e `003`), tornando o código mais legível e de fácil manutenção.

---

# Concorrência

O processamento dos arquivos ocorre de forma concorrente.

Foi utilizado um `ExecutorService` com quantidade de threads baseada no número de processadores disponíveis da máquina.

Cada arquivo é processado de maneira independente, sem compartilhamento de estado entre execuções.

---

# Estrutura esperada

### Entrada

```
%USER_HOME%/data/in
```

### Saída

```
%USER_HOME%/data/out
```

### Arquivos de entrada

```
arquivo.dat
```

### Arquivos gerados

```
arquivo.done.dat
```

---

# Como executar

## Compilar o projeto

```bash
mvn clean verify
```

## Executar

```bash
java -jar target/analisador-de-dados-1.0.0.jar
```

---

# Testes

## Executar todos os testes

```bash
mvn test
```

## Executar validação completa

```bash
mvn clean verify
```

---

# Cobertura de Testes

O projeto utiliza:

- JUnit 5
- Mockito
- JaCoCo
- Codecov

Foram implementados:

- Testes unitários
- Testes de integração

Os testes cobrem o fluxo completo da aplicação:

```
Leitura
      ↓
Processamento
      ↓
Geração do relatório
      ↓
Escrita do arquivo
```

### Cenários cobertos

- Parsing correto dos registros
- Tratamento de linhas malformadas
- Cálculo da venda mais cara
- Identificação do pior vendedor
- Geração do arquivo `.done.dat`
- Escrita do relatório
- Processamento concorrente do monitor de diretório

### Cobertura atual

| Métrica | Resultado |
|---------|----------:|
| Line Coverage | **81%** |
| Branch Coverage | **65%** |

A cobertura é gerada automaticamente pelo **JaCoCo** e publicada no **Codecov** durante a execução da pipeline.

---

# Integração Contínua

O projeto utiliza **CircleCI** para integração contínua.

A cada commit são executadas automaticamente as seguintes etapas:

1. Checkout do código
2. Build do projeto
3. Execução dos testes
4. Geração do relatório de cobertura (JaCoCo)
5. Publicação da cobertura no Codecov

---

# Decisões Arquiteturais

Durante o desenvolvimento foi priorizada uma solução simples e de fácil manutenção.

As principais decisões foram:

- separação entre processamento e regra de negócio;
- utilização de Enum para representar os tipos de registro;
- utilização de ExecutorService para processamento concorrente;
- tratamento de linhas inválidas sem interromper o processamento;
- componente dedicado para escrita do relatório;
- testes unitários focados nas regras de negócio;
- testes de integração validando o fluxo completo da aplicação.

---

# Uso de Inteligência Artificial

Ferramentas de Inteligência Artificial foram utilizadas como apoio durante o desenvolvimento para:

- discussão de alternativas de implementação;
- revisão da arquitetura;
- sugestões de refatoração;
- apoio na elaboração dos testes automatizados;
- revisão da documentação.

---
