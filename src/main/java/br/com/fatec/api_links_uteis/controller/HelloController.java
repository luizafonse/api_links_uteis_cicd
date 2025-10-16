package br.com.fatec.api_links_uteis.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller REST para mensagens de boas-vindas.
 *
 * Esta classe demonstra um endpoint simples de saudação
 * para fins educacionais.
 *
 * Endpoint disponível:
 * - GET /api/hello - Mensagem de boas-vindas
 */
@RestController
@RequestMapping("/api")
public class HelloController {

    @GetMapping("hello")
    public String hello() {
        return "Olá Fatec";
    }

}
