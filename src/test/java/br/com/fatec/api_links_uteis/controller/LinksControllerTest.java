package br.com.fatec.api_links_uteis.controller;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

/**
 * Testes unitários simples para o LinksController.
 *
 * Esta classe testa os métodos do controller diretamente,
 * sem usar o contexto do Spring ou simulação HTTP.
 *
 * Ideal para testar a lógica de negócio isoladamente.
 */
class LinksControllerTest {

    private final LinksController controller = new LinksController();

    // 1. Teste para listar todos os links
    @Test
    void deveRetornarTodosOsLinks() {
        List<Map<String, String>> links = controller.getLinks();
        assertEquals(2, links.size());
        assertEquals("GitHub", links.get(0).get("titulo"));
    }

    // 2. Teste para buscar link por ID existente
    @Test
    void deveRetornarLinkQuandoIdExiste() {
        Map<String, String> link = controller.getLinkById(1);
        assertEquals("1", link.get("id"));
        assertEquals("GitHub", link.get("titulo"));
    }

    // 3. Teste para buscar link por ID inexistente
    @Test
    void deveRetornarNullQuandoIdNaoExiste() {
        Map<String, String> link = controller.getLinkById(999);
        assertEquals(null, link);
    }

    // 4. Teste para criar novo link
    @Test
    void deveCriarNovoLinkComIdGerado() {
        Map<String, String> novoLink = Map.of("titulo", "Google", "url", "https://google.com");
        Map<String, String> resultado = controller.createLink(novoLink);

        assertEquals("Google", resultado.get("titulo"));
        assertEquals("https://google.com", resultado.get("url"));
        assertEquals("3", resultado.get("id")); // Próximo ID
    }

    // 5. Teste para atualizar link existente
    @Test
    void deveAtualizarLinkExistenteCompletamente() {
        Map<String, String> dadosAtualizacao = Map.of("titulo", "Novo Título", "url", "https://novo.com");
        Map<String, String> resultado = controller.updateLink(1, dadosAtualizacao);

        assertEquals("Novo Título", resultado.get("titulo"));
        assertEquals("https://novo.com", resultado.get("url"));
    }

    // 6. Teste para atualizar parcialmente (PATCH)
    @Test
    void deveAtualizarLinkParcialmente() {
        Map<String, String> dadosParciais = Map.of("titulo", "Título Atualizado");
        Map<String, String> resultado = controller.patchLink(2, dadosParciais);

        assertEquals("Título Atualizado", resultado.get("titulo"));
        assertEquals("https://stackoverflow.com", resultado.get("url")); // URL original mantida
    }

    // 7. Teste para PATCH com link inexistente
    @Test
    void deveRetornarNullQuandoPatchLinkInexistente() {
        Map<String, String> dadosParciais = Map.of("titulo", "Teste");
        Map<String, String> resultado = controller.patchLink(999, dadosParciais);

        assertEquals(null, resultado);
    }

    // 8. Teste para deletar link
    @Test
    void deveDeletarLinkERetornarMensagemConfirmacao() {
        String resultado = controller.deleteLink(1);
        assertEquals("Link 1 removido", resultado);
    }

}