package br.com.fiap.feedback_api.dto;

import br.com.fiap.feedback_api.model.Urgencia;

import java.time.Instant;

public record DetalheAvaliacaoResponse(
        String descricao,
        Integer nota,
        Urgencia urgencia,
        Instant dataCriacao

) {
}
