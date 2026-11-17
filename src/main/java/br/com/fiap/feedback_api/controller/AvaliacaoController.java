package br.com.fiap.feedback_api.controller;

import br.com.fiap.feedback_api.dto.AvaliacaoRequest;
import br.com.fiap.feedback_api.model.Avaliacao;
import br.com.fiap.feedback_api.service.AvaliacaoService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/avaliacao")
public class AvaliacaoController {

    private final AvaliacaoService avaliacaoService;

    public AvaliacaoController(AvaliacaoService avaliacaoService) {
        this.avaliacaoService = avaliacaoService;
    }


    @PostMapping
    public Avaliacao avaliar(
            @Valid @RequestBody AvaliacaoRequest request) {

        return avaliacaoService.processar(request);
    }

    @GetMapping
    public List<Avaliacao> listar(){
        return avaliacaoService.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Avaliacao> buscarPorId(@PathVariable String id){

        Avaliacao avaliacao = avaliacaoService.buscarPorId(id);

        if(avaliacao == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(avaliacao);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Avaliacao> atualizar(
            @PathVariable String id,
            @Valid @RequestBody AvaliacaoRequest request
    ){
        Avaliacao avaliacao = avaliacaoService.atualizar(id, request);

        return ResponseEntity.ok(avaliacao);


    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Void> tratarNaoEncontrado(
            NoSuchElementException e) {

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable String id){

         avaliacaoService.excluir(id);

         return ResponseEntity.noContent().build();
    }
}