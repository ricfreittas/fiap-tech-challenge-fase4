package br.com.fiap.feedback_api.service;

import br.com.fiap.feedback_api.dto.DetalheAvaliacaoResponse;
import br.com.fiap.feedback_api.model.Urgencia;
import br.com.fiap.feedback_api.dto.RelatorioSemanalResponse;
import br.com.fiap.feedback_api.model.Avaliacao;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.DayOfWeek;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RelatorioService {

    private final AvaliacaoService avaliacaoService;

    public RelatorioService(AvaliacaoService avaliacaoService) {
        this.avaliacaoService = avaliacaoService;
    }
    public RelatorioSemanalResponse gerarRelatorio() {

        ZoneId zona = ZoneId.of("America/Sao_Paulo");

        LocalDate hoje = LocalDate.now(zona);

        LocalDate inicioSemana = hoje.with(DayOfWeek.MONDAY)
                .minusWeeks(1);

        Instant inicio = inicioSemana
                .atStartOfDay(zona)
                .toInstant();

        Instant fim = inicioSemana
                .plusWeeks(1)
                .atStartOfDay(zona)
                .toInstant();

        List<Avaliacao> avaliacoes = avaliacaoService.listar()
                .stream()
                .filter(avaliacao ->
                        !avaliacao.getDataCriacao().isBefore(inicio)
                                && avaliacao.getDataCriacao().isBefore(fim)
                )
                .toList();

        long totalAvaliacoes = avaliacoes.size();

        double mediaNotas = avaliacoes.stream()
                .mapToInt(Avaliacao::getNota)
                .average()
                .orElse(0.0);

        Map<Urgencia, Long> quantidadePorUrgencia =
                avaliacoes.stream()
                        .collect(Collectors.groupingBy(
                                Avaliacao::getUrgencia,
                                Collectors.counting()
                        ));

        Map<LocalDate, Long> quantidadePorDia =
                avaliacoes.stream()
                        .collect(Collectors.groupingBy(
                                avaliacao -> avaliacao.getDataCriacao()
                                        .atZone(zona)
                                        .toLocalDate(),
                                Collectors.counting()
                        ));
        List<DetalheAvaliacaoResponse> detalhes =
                avaliacoes.stream()
                        .map(avaliacao -> new DetalheAvaliacaoResponse(
                                avaliacao.getDescricao(),
                                avaliacao.getNota(),
                                avaliacao.getUrgencia(),
                                avaliacao.getDataCriacao()
                        ))
                        .toList();

        return new RelatorioSemanalResponse(
                totalAvaliacoes,
                mediaNotas,
                quantidadePorUrgencia,
                quantidadePorDia,
                detalhes
        );

    }
}