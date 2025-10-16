package br.com.fatec.api_links_uteis.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Testes de integração para o LinksController.
 *
 * Esta classe testa os endpoints REST da API de links úteis,
 * incluindo operações CRUD completas (GET, POST, PUT, PATCH, DELETE).
 *
 * Usa @WebMvcTest para testar apenas a camada web do Spring,
 * simulando requisições HTTP com MockMvc.
 *
 * @DirtiesContext garante que cada teste tenha um contexto Spring limpo,
 * evitando interferência entre testes devido ao estado compartilhado do HashMap.
 */
@WebMvcTest(LinksController.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class LinksControllerIT {

    @Autowired
    private MockMvc mockMvc;

    // 1. Teste para listar todos os links via endpoint
    @Test
    void deveRetornarTodosOsLinksQuandoGetLinksEndpoint() throws Exception {
        mockMvc.perform(get("/api/links"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].titulo").value("GitHub"))
                .andExpect(jsonPath("$[1].titulo").value("Stack Overflow"));
    }

    // 2. Teste para buscar link por ID existente via endpoint
    @Test
    void deveRetornarLinkQuandoGetLinkPorIdEndpointComIdExistente() throws Exception {
        mockMvc.perform(get("/api/links/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.titulo").value("GitHub"))
                .andExpect(jsonPath("$.url").value("https://github.com"));
    }

    // 3. Teste para buscar link por ID inexistente via endpoint
    @Test
    void deveRetornarVazioQuandoGetLinkPorIdEndpointComIdInexistente() throws Exception {
        mockMvc.perform(get("/api/links/999"))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    // 4. Teste para criar novo link via endpoint
    @Test
    void deveCriarNovoLinkQuandoPostLinksEndpoint() throws Exception {
        String novoLink = """
                {
                    "titulo": "Google",
                    "url": "https://google.com"
                }
                """;

        mockMvc.perform(post("/api/links")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(novoLink))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("3"))
                .andExpect(jsonPath("$.titulo").value("Google"))
                .andExpect(jsonPath("$.url").value("https://google.com"));
    }

    // 5. Teste para atualizar link completo (PUT) via endpoint
    @Test
    void deveAtualizarLinkCompletamenteQuandoPutLinksEndpoint() throws Exception {
        String linkAtualizado = """
                {
                    "titulo": "GitHub Atualizado",
                    "url": "https://github.com/novo"
                }
                """;

        mockMvc.perform(put("/api/links/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(linkAtualizado))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.titulo").value("GitHub Atualizado"))
                .andExpect(jsonPath("$.url").value("https://github.com/novo"));
    }

    // 6. Teste para atualizar parcialmente (PATCH) via endpoint
    @Test
    void deveAtualizarLinkParcialmenteQuandoPatchLinksEndpoint() throws Exception {
        String atualizacaoParcial = """
                {
                    "titulo": "Só o Título Novo"
                }
                """;

        mockMvc.perform(patch("/api/links/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(atualizacaoParcial))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("2"))
                .andExpect(jsonPath("$.titulo").value("Só o Título Novo"))
                .andExpect(jsonPath("$.url").value("https://stackoverflow.com")); // URL original mantida
    }

    // 7. Teste para PATCH com link inexistente via endpoint
    @Test
    void deveRetornarVazioQuandoPatchLinksEndpointComIdInexistente() throws Exception {
        String atualizacaoParcial = """
                {
                    "titulo": "Teste"
                }
                """;

        mockMvc.perform(patch("/api/links/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(atualizacaoParcial))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    // 8. Teste para deletar link via endpoint
    @Test
    void deveDeletarLinkQuandoDeleteLinksEndpoint() throws Exception {
        mockMvc.perform(delete("/api/links/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Link 1 removido"));
    }

}