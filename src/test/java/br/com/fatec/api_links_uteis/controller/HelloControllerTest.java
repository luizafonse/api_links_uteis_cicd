package br.com.fatec.api_links_uteis.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

/**
 * Testes unitários simples para o HelloController.
 *
 * Esta classe testa os métodos do controller diretamente,
 * sem usar o contexto do Spring ou simulação HTTP.
 *
 * Ideal para testar a lógica de negócio isoladamente.
 */
class HelloControllerTest {

    private final HelloController controller = new HelloController();

    @Test
    void deveRetornarMensagemHello() {
        // Teste simples
        String resultado = controller.hello();
        assertEquals("Olá Fatec", resultado);
    }

}