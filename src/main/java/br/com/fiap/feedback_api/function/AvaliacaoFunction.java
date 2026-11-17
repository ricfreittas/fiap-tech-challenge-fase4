package br.com.fiap.feedback_api.function;

import br.com.fiap.feedback_api.service.AvaliacaoService;
import com.microsoft.azure.functions.*;
import com.microsoft.azure.functions.annotation.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import br.com.fiap.feedback_api.dto.AvaliacaoRequest;

import java.util.Optional;

public class AvaliacaoFunction {


    @FunctionName("avaliacaoHttp")
    public HttpResponseMessage executar(
            @HttpTrigger(
                    name = "req",
                    methods = {HttpMethod.GET, HttpMethod.POST},
                    authLevel = AuthorizationLevel.FUNCTION,
                    route = "avaliacoes"
            )
            HttpRequestMessage<Optional<String>> request,
            ExecutionContext context) {

        try {
            AvaliacaoService service =
                    SpringContextProvider.getContext()
                            .getBean(AvaliacaoService.class);

            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());

            if (request.getHttpMethod() == HttpMethod.POST) {

                String corpo = request.getBody().orElse("");

                if (corpo.isBlank()) {
                    return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
                            .body("O corpo da requisicao e obrigatorio.")
                            .build();
                }

                AvaliacaoRequest avaliacaoRequest =
                        mapper.readValue(corpo, AvaliacaoRequest.class);

                if (avaliacaoRequest.descricao() == null
                        || avaliacaoRequest.descricao().isBlank()
                        || avaliacaoRequest.nota() == null
                        || avaliacaoRequest.nota() < 0
                        || avaliacaoRequest.nota() > 10) {

                    return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
                            .body("Descricao e nota entre 0 e 10 sao obrigatorias.")
                            .build();
                }

                var avaliacao = service.processar(avaliacaoRequest);

                String json = mapper.writeValueAsString(avaliacao);

                return request.createResponseBuilder(HttpStatus.CREATED)
                        .header("Content-Type", "application/json")
                        .body(json)
                        .build();
            }

            var avaliacoes = service.listar();
            String json = mapper.writeValueAsString(avaliacoes);

            return request.createResponseBuilder(HttpStatus.OK)
                    .header("Content-Type", "application/json")
                    .body(json)
                    .build();

        } catch (com.fasterxml.jackson.core.JsonProcessingException e) {

            return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
                    .body("JSON invalido.")
                    .build();

        } catch (Exception e) {

            context.getLogger().severe("Erro: " + e.getMessage());

            return request.createResponseBuilder(
                            HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno ao processar a avaliacao.")
                    .build();
        }
    }

}
