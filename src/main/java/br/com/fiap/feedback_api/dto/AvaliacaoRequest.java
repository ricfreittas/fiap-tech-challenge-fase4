package br.com.fiap.feedback_api.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;

public record AvaliacaoRequest(
        @NotBlank
        String descricao,

        @NotNull
        @Min(0)
        @Max(10)
        Integer nota
) {
}
