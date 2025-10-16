
# Guia Prático: Integração e Entrega Contínua (CI/CD) com Java e GitHub Actions

**Objetivo:** Este guia prático tem como objetivo demonstrar a criação de uma API REST simples com Spring Boot e a configuração de uma pipeline de Integração Contínua (CI) utilizando GitHub Actions. Ao final, você terá um projeto que é automaticamente testado e empacotado a cada novo commit.

<br>

<details>
<summary><strong>Parte 1: Criando o Projeto com Spring Boot</strong></summary>

A forma mais rápida de iniciar um projeto Spring Boot é usando o Spring Initializr, uma ferramenta web que gera a estrutura básica do projeto com as dependências que precisamos.

**Etapas:**

1.  Acesse o site [start.spring.io](http://start.spring.io).
2.  Preencha as informações do projeto:
      * **Project**: `Maven`
      * **Language**: `Java`
      * **Spring Boot**: `3.2.6` (ou a versão estável mais recente)
      * **Group**: `br.com.fatec`
      * **Artifact**: `api-links-uteis`
      * **Name**: `api-links-uteis`
      * **Description**: `Catálogo de Links Úteis`
      * **Package name**: `br.com.fatec.api_links_uteis`
3.  Adicione as seguintes **Dependencies**:
      * `Spring Web`
      * `Spring Boot DevTools`
4.  Clique em **GENERATE** e descompacte o arquivo `.zip` resultante.

</details>

<details>
<summary><strong>Parte 2: Entendendo a Estrutura do Spring Boot</strong></summary>

### O que é o Spring Boot?

O Spring Boot simplifica a criação de aplicações Java através da autoconfiguração. Com base nas dependências, ele configura a aplicação automaticamente, como iniciar um servidor web ao adicionar o `Spring Web`.

### A Classe Principal (`ApiLinksUteisApplication.java`)

```java
package br.com.fatec.api_links_uteis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApiLinksUteisApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiLinksUteisApplication.class, args);
    }

}
```

  - `@SpringBootApplication`: Anotação principal que habilita a autoconfiguração e a varredura de componentes do Spring.
  - `main(String[] args)`: Ponto de entrada que inicia a aplicação.

</details>

<details>
<summary><strong>Parte 3: Construindo a Primeira API</strong></summary>

**Etapas:**

1.  Dentro de `src/main/java/br/com/fatec/api_links_uteis`, crie um novo pacote chamado `controller`.

2.  Dentro do pacote `controller`, crie uma nova classe Java chamada `HelloController.java` com o seguinte conteúdo:

    ```java
    package br.com.fatec.api_links_uteis.controller;

    import org.springframework.web.bind.annotation.GetMapping;
    import org.springframework.web.bind.annotation.RequestMapping;
    import org.springframework.web.bind.annotation.RestController;

    @RestController
    @RequestMapping("/api")
    public class HelloController {

        @GetMapping("/hello")
        public String hello() {
            return "Olá Fatec";
        }

    }
    ```

3.  Execute a aplicação a partir da classe `ApiLinksUteisApplication`.

4.  Acesse `http://localhost:8080/api/hello` em seu navegador para ver o resultado.

</details>

<details>
<summary><strong>Parte 4: Adicionando e Validando Testes</strong></summary>

Testes automatizados garantem que novas alterações não quebrem o código existente.

**Etapas:**

1.  **Entendendo o teste padrão:** O Spring Initializr já cria um teste básico em `src/test/java/br/com/fatec/api_links_uteis/ApiLinksUteisApplicationTests.java`.

    ```java
    package br.com.fatec.api_links_uteis;

    import org.junit.jupiter.api.Test;
    import org.springframework.boot.test.context.SpringBootTest;

    @SpringBootTest
    class ApiLinksUteisApplicationTests {

        @Test
        void contextLoads() {
            // Este teste apenas verifica se o contexto do Spring Boot 
            // é carregado com sucesso, sem erros.
        }

    }
    ```

2.  **Rodando os testes localmente:**

    ```bash
    ./mvnw.cmd test
    ```

3.  **Forçando um erro:** Vamos ver o que acontece quando um teste falha.

      * **O que é `assert`?** É uma declaração que verifica se uma condição é verdadeira. Se for falsa, o teste falha.
      * Adicione `assert(false);` dentro do método `contextLoads()` e rode os testes novamente.

    <!-- end list -->

    ```java
    @Test
    void contextLoads() {
        assert(false); // Forçando a falha do teste
    }
    ```

4.  **O que acontece quando um teste falha?** O processo de `build` é interrompido, e um relatório de erro é exibido, indicando qual teste falhou e o motivo. Isso é crucial para a Integração Contínua, pois impede que código com defeito avance. Não se esqueça de remover o `assert(false);` após o teste.

</details>

<details>
<summary><strong>Parte 5: Testes Unitários e de Integração</strong></summary>

  - **Teste Unitário:** Testa a menor parte de um código (um método) de forma isolada.
  - **Teste de Integração:** Testa a interação entre diferentes partes do sistema, como uma requisição HTTP até o controller.

**Etapas:**

1.  Crie o **Teste Unitário** `HelloControllerTest.java` em `src/test/java/br/com/fatec/api_links_uteis/controller/`:

    ```java
    package br.com.fatec.api_links_uteis.controller;

    import static org.junit.jupiter.api.Assertions.assertEquals;
    import org.junit.jupiter.api.Test;

    class HelloControllerTest {

        private final HelloController controller = new HelloController();

        @Test
        void deveRetornarMensagemHello() {
            // Testamos diretamente o retorno do método `hello()`
            String resultado = controller.hello();
            assertEquals("Olá Fatec", resultado);
        }

    }
    ```

2.  Crie o **Teste de Integração** `HelloControllerIT.java` na mesma pasta:

    ```java
    package br.com.fatec.api_links_uteis.controller;

    import org.junit.jupiter.api.Test;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
    import org.springframework.test.web.servlet.MockMvc;
    import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
    import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
    import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

    @WebMvcTest(HelloController.class)
    class HelloControllerIT {

        @Autowired
        private MockMvc mockMvc; // Objeto para simular requisições HTTP

        @Test
        void deveRetornarMensagemHelloQuandoGetHelloEndpoint() throws Exception {
            // Simulamos uma chamada GET para /api/hello
            mockMvc.perform(get("/api/hello"))
                   // Verificamos se o status da resposta é 200 OK
                   .andExpect(status().isOk())
                   // Verificamos se o conteúdo da resposta é "Olá Fatec"
                   .andExpect(content().string("Olá Fatec"));
        }

    }
    ```

3. Configure o plugin para rodar os testes de integração no `pom.xml`:

```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-failsafe-plugin</artifactId>
            <version>3.0.0-M9</version>
            <executions>
                <execution>
                    <phase>integration-test</phase>
                    <goals>
                        <goal>integration-test</goal>
                        <goal>verify</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```

4. Execute os comandos para rodar os testes unitários e de integração:

```bash
.\mvnw.cmd test
```


```bash
.\mvnw.cmd verify
```

</details>

<details>
<summary><strong>Parte 6: Configurando a Pipeline de CI/CD com GitHub Actions</strong></summary>

Agora, vamos automatizar todo o processo. Criaremos uma pipeline que primeiro valida o código (CI - Integração Contínua) e depois, se tudo estiver certo, empacota a aplicação (CD - Entrega Contínua).

### 6.1 - Criando o Job de CI (Testes)

Primeiro, vamos garantir que os testes rodem automaticamente a cada `push`.

1.  Na raiz do projeto, crie o arquivo `.github/workflows/ci.yml`.

2.  Adicione o seguinte conteúdo. Este é nosso Job de CI, responsável apenas por validar o código:

    ```yaml
    # Nome do workflow que aparece no GitHub
    name: CI/CD Pipeline

    # Quando executar: a cada push ou pull request
    on:
      push:
        branches: [ main ]
      pull_request:
        branches: [ main ]

    # Lista de trabalhos (jobs) a executar
    jobs:
      # Job 1: CI - Testes e validação
      ci:
        # Sistema operacional para executar
        runs-on: ubuntu-latest

        # Lista de passos (steps) do job
        steps:
        # Passo 1: Baixar o código do repositório
        - name: Fazer checkout do código
          uses: actions/checkout@v4

        # Passo 2: Configurar Java 21
        - name: Configurar Java 21
          uses: actions/setup-java@v4
          with:
            java-version: '21'
            distribution: 'temurin'

        # Passo 3: Executar testes
        - name: Executar testes
          run: mvn -B test --file pom.xml
    ```

3.  Faça o `commit` e `push` do arquivo `.yml`. Acesse a aba **Actions** no seu repositório do GitHub. Você verá a pipeline rodando e executando apenas os testes.

### 6.2 - Adicionando o Job de CD (Build e Artefato)

Agora que os testes estão automatizados, vamos adicionar o Job de CD para empacotar a aplicação e salvar o arquivo `.jar` (artefato).

1.  Altere o arquivo `.github/workflows/ci.yml` para adicionar o segundo job.

    ```yaml
    name: CI/CD Pipeline

    on:
      push:
        branches: [ main ]
      pull_request:
        branches: [ main ]

    jobs:
      # Job 1: CI - Testes e validação
      ci:
        runs-on: ubuntu-latest
        steps:
        - name: Fazer checkout do código
          uses: actions/checkout@v4

        - name: Configurar Java 21
          uses: actions/setup-java@v4
          with:
            java-version: '21'
            distribution: 'temurin'

        - name: Executar testes
          run: mvn -B test --file pom.xml

      # Job 2: CD - Deploy e artefatos
      cd:
        # Só executa se o job 'ci' passou com sucesso
        needs: ci
        # Só roda em push direto no branch 'main' (não em Pull Requests)
        if: github.ref == 'refs/heads/main'
        runs-on: ubuntu-latest

        steps:
        - name: Fazer checkout do código
          uses: actions/checkout@v4

        - name: Configurar Java 21
          uses: actions/setup-java@v4
          with:
            java-version: '21'
            distribution: 'temurin'

        # Geramos o pacote .jar, pulando os testes que já rodaram no job de CI
        - name: Gerar JAR para deploy
          run: mvn -B package --file pom.xml -DskipTests

        # Faz o upload do .jar como um artefato do workflow
        - name: Upload JAR para deploy
          uses: actions/upload-artifact@v4
          with:
            name: app-jar
            path: target/*.jar
    ```

2.  Faça o `commit` e `push` novamente. Na aba **Actions**, você verá:

      * **Em um Pull Request:** Apenas o job `ci` será executado.
      * **Em um push para a `main`:** O job `ci` rodará primeiro. Se ele passar, o job `cd` começará. Ao final, na página de resumo do workflow, você poderá baixar o `app-jar.zip` contendo o arquivo `.jar` da sua aplicação.

</details>

Testando CD não deveria rodar no pull request