package br.com.fiap.feedback_api.controller;

import br.com.fiap.feedback_api.dto.RelatorioSemanalResponse;
import br.com.fiap.feedback_api.service.RelatorioService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/relatorios")
public class RelatorioController {

    private final RelatorioService relatorioService;

    public RelatorioController(RelatorioService relatorioService) {
        this.relatorioService = relatorioService;
    }

    @GetMapping("/semanal")
    public RelatorioSemanalResponse gerarRelatorio() {
        return relatorioService.gerarRelatorio();
    }
}