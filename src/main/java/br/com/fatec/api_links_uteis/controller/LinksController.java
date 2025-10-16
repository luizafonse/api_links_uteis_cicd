package br.com.fatec.api_links_uteis.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller REST para gerenciar links úteis da FATEC.
 *
 * Esta classe demonstra operações CRUD básicas usando uma HashMap
 * como armazenamento em memória para fins educacionais.
 *
 * Endpoints disponíveis:
 * - GET /api/links - Lista todos os links
 * - GET /api/links/{id} - Busca link por ID
 * - POST /api/links - Cria novo link
 * - PUT /api/links/{id} - Atualiza link completo
 * - PATCH /api/links/{id} - Atualiza parcialmente um link
 * - DELETE /api/links/{id} - Remove um link
 */
@RestController
@RequestMapping("/api/links")
public class LinksController {

    // "Banco de dados" simulado
    private final Map<Integer, Map<String, String>> linksDB = new HashMap<>();
    private int nextId = 1;

    public LinksController() {
        // Dados iniciais
        linksDB.put(1, Map.of("id", "1", "titulo", "GitHub", "url", "https://github.com"));
        linksDB.put(2, Map.of("id", "2", "titulo", "Stack Overflow", "url", "https://stackoverflow.com"));
        nextId = 3;
    }

    // GET - Listar todos os links
    @GetMapping
    public List<Map<String, String>> getLinks() {
        return new ArrayList<>(linksDB.values());
    }

    // GET - Buscar link por ID
    @GetMapping("/{id}")
    public Map<String, String> getLinkById(@PathVariable int id) {
        return linksDB.get(id);
    }

    // POST - Criar novo link
    @PostMapping
    public Map<String, String> createLink(@RequestBody Map<String, String> data) {
        Map<String, String> novoLink = new HashMap<>();
        novoLink.put("id", String.valueOf(nextId));
        novoLink.put("titulo", data.get("titulo"));
        novoLink.put("url", data.get("url"));

        linksDB.put(nextId, novoLink);
        nextId++;

        return novoLink;
    }

    // PUT - Atualizar link completo
    @PutMapping("/{id}")
    public Map<String, String> updateLink(@PathVariable int id, @RequestBody Map<String, String> data) {
        Map<String, String> linkAtualizado = new HashMap<>();
        linkAtualizado.put("id", String.valueOf(id));
        linkAtualizado.put("titulo", data.get("titulo"));
        linkAtualizado.put("url", data.get("url"));

        linksDB.put(id, linkAtualizado);
        return linkAtualizado;
    }

    // PATCH - Atualizar parcialmente
    @PatchMapping("/{id}")
    public Map<String, String> patchLink(@PathVariable int id, @RequestBody Map<String, String> data) {
        Map<String, String> linkExistente = linksDB.get(id);
        if (linkExistente != null) {
            Map<String, String> linkAtualizado = new HashMap<>(linkExistente);
            if (data.get("titulo") != null) linkAtualizado.put("titulo", data.get("titulo"));
            if (data.get("url") != null) linkAtualizado.put("url", data.get("url"));

            linksDB.put(id, linkAtualizado);
            return linkAtualizado;
        }
        return null;
    }

    // DELETE - Remover link
    @DeleteMapping("/{id}")
    public String deleteLink(@PathVariable int id) {
        linksDB.remove(id);
        return "Link " + id + " removido";
    }

}