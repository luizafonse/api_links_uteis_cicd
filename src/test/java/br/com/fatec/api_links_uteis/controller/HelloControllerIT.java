package br.com.fatec.api_links_uteis.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Testes de integração para o HelloController.
 *
 * Esta classe testa apenas o endpoint de saudação,
 * usando @WebMvcTest para testar apenas a camada web do Spring,
 * simulando requisições HTTP com MockMvc.
 */
@WebMvcTest(HelloController.class)
class HelloControllerIT {

    @Autowired
    private MockMvc mockMvc;

    // Teste do endpoint hello - mais simples
    @Test
    void deveRetornarMensagemHelloQuandoGetHelloEndpoint() throws Exception {
        mockMvc.perform(get("/api/hello"))
                .andExpect(status().isOk())
                .andExpect(content().string("Olá Fatec"));
    }

}