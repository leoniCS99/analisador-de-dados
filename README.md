# Analisador de Dados

## Objetivo

Aplicação desenvolvida em Java para processar arquivos de vendas no formato `.dat`.

O sistema monitora continuamente um diretório de entrada, identifica novos arquivos, processa seus registros e gera automaticamente um relatório no diretório de saída contendo:

- Quantidade de clientes
- Quantidade de vendedores
- ID da venda mais cara
- Pior vendedor

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
│   │       ├── config
│   │       ├── enums
│   │       ├── model
│   │       ├── service
│   │       └── util
│   └── resources
│
└── test
    ├── java
    └── resources
```

---

# Arquitetura

A aplicação foi organizada em pequenas responsabilidades seguindo princípios de Clean Code e SOLID, evitando abstrações desnecessárias para o tamanho do projeto.

## DirectoryWatcher

Responsável por monitorar continuamente o diretório de entrada utilizando a API `WatchService`.

Sempre que um novo arquivo `.dat` é encontrado, seu processamento é iniciado de forma assíncrona utilizando `ExecutorService`.

---

## FileProcessor

Responsável por:

- Ler o arquivo
- Interpretar cada linha
- Converter os registros em objetos do domínio
- Acionar a geração do relatório

Também trata registros inválidos, garantindo que uma linha incorreta não interrompa o processamento do restante do arquivo.

---

## ReportService

Centraliza toda a regra de negócio responsável pela geração do relatório.

Calcula:

- quantidade de clientes;
- quantidade de vendedores;
- venda mais cara;
- pior vendedor.

---

## FileWriterUtil

Responsável exclusivamente pela escrita do arquivo de saída.

---

## RecordType

Utilizado para eliminar o uso de códigos literais (`001`, `002` e `003`), tornando o código mais legível e de fácil manutenção.

---

# Concorrência

A aplicação suporta o processamento simultâneo de múltiplos arquivos.

Foi utilizado um `ExecutorService` com quantidade de threads baseada no número de processadores disponíveis da máquina.

Cada arquivo é processado de forma independente, permitindo processamento concorrente sem compartilhamento de estado.

---

# Estrutura esperada

Entrada

```
%HOMEPATH%/data/in
```

Saída

```
%HOMEPATH%/data/out
```

Arquivos de entrada

```
arquivo.dat
```

Arquivos gerados

```
arquivo.done.dat
```

---

# Como executar

Compilar o projeto

```bash
mvn clean package
```

Executar

```bash
java -jar target/analisador-de-dados-1.0.0.jar
```

---

# Executando os testes

Executar todos os testes

```bash
mvn test
```

Executar validação completa

```bash
mvn verify
```

---

# Cobertura de testes

O projeto utiliza:

- JUnit 5
- Mockito
- JaCoCo

Foram implementados:

- Testes unitários
- Testes de integração

Os testes cobrem o fluxo completo de:

Leitura → Processamento → Escrita

Incluindo:

- parsing dos registros;
- tratamento de linhas inválidas;
- cálculo da venda mais cara;
- identificação do pior vendedor;
- geração do arquivo de saída.

---

# Pipeline

O projeto possui integração contínua utilizando CircleCI.

A pipeline executa automaticamente:

- Build
- Testes
- Análise de cobertura com JaCoCo
- Envio da cobertura para o Codecov

---

# Decisões Arquiteturais

Durante o desenvolvimento foi priorizada uma solução simples, objetiva e de fácil manutenção.

As responsabilidades foram separadas apenas quando agregavam valor ao projeto, evitando a criação de camadas e padrões desnecessários para o escopo do desafio.

As principais decisões foram:

- separação entre processamento e regra de negócio;
- utilização de Enum para tipos de registro;
- processamento concorrente utilizando ExecutorService;
- tratamento de linhas inválidas sem interromper a execução;
- escrita do relatório em componente dedicado.

---

# Uso de Inteligência Artificial

Ferramentas de Inteligência Artificial foram utilizadas como apoio durante o desenvolvimento para:

- discussão de alternativas de implementação;
- revisão da arquitetura;
- identificação de oportunidades de refatoração;
- elaboração dos testes automatizados;
- revisão da documentação.

Todas as sugestões foram analisadas, adaptadas e validadas manualmente antes de serem incorporadas ao projeto.

O código entregue foi revisado integralmente e todas as decisões arquiteturais são de responsabilidade do autor.

---

# Melhorias Futuras

Como possíveis evoluções do projeto:

- suporte a novos tipos de registros;
- configuração externa dos diretórios;
- utilização de framework de logging;
- processamento distribuído;
- suporte a diferentes formatos de entrada.
