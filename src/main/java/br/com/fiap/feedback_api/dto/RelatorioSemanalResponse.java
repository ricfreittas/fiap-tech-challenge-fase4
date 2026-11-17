package br.com.fiap.feedback_api.dto;

import br.com.fiap.feedback_api.model.Urgencia;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public record RelatorioSemanalResponse(
        long totalAvaliacoes,
        double mediaNotas,
        Map<Urgencia, Long> quantidadePorUrgencia,
        Map<LocalDate, Long> quantidadePorDia,
        List<DetalheAvaliacaoResponse> detalhes
) {
}