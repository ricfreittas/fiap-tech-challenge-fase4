
package br.com.fiap.feedback_api.function;

import br.com.fiap.feedback_api.service.AvaliacaoService;
import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.QueueTrigger;
import br.com.fiap.feedback_api.service.NotificacaoEmailService;

public class NotificacaoCriticaQueueFunction {

    @FunctionName("notificacaoCriticaQueue")
    public void processar(
            @QueueTrigger(
                    name = "mensagem",
                    queueName = "notificacoes-criticas",
                    connection = "NotificacoesStorage"
            ) String avaliacaoId,
            ExecutionContext context) {

        context.getLogger().info(
                "Avaliacao critica recebida: " + avaliacaoId
        );

        try {
            context.getLogger().info("Inicializando contexto Spring...");

            var springContext = SpringContextProvider.getContext();

            context.getLogger().info("Contexto Spring inicializado.");

            AvaliacaoService service =
                    springContext.getBean(AvaliacaoService.class);

            NotificacaoEmailService emailService =
                    springContext.getBean(NotificacaoEmailService.class);

            context.getLogger().info("Servicos Spring obtidos.");

            var avaliacao = service.buscarPorId(avaliacaoId);

            context.getLogger().info("Avaliacao encontrada no Cosmos DB.");

            String destinatario = System.getenv("ACS_EMAIL_DESTINATARIO");

            if (destinatario == null || destinatario.isBlank()) {
                throw new IllegalStateException(
                        "ACS_EMAIL_DESTINATARIO nao configurado."
                );
            }

            String assunto = "FIAP - Alerta de avaliacao critica";

            String mensagem = """
                    Uma avaliacao critica foi registrada.
                    
                    ID: %s
                    Descricao: %s
                    Nota: %d
                    Urgencia: %s
                    Data de envio: %s
                    
                    Esta notificacao foi gerada automaticamente.
                    """.formatted(
                    avaliacao.getId(),
                    avaliacao.getDescricao(),
                    avaliacao.getNota(),
                    avaliacao.getUrgencia(),
                    java.time.ZonedDateTime.now(
                            java.time.ZoneId.of("America/Sao_Paulo")
                    ).format(
                            java.time.format.DateTimeFormatter
                                    .ofPattern("dd/MM/yyyy HH:mm:ss")
                    )
            );

            context.getLogger().info("Iniciando envio do email...");

            emailService.enviarEmail(destinatario, assunto, mensagem);

            context.getLogger().info(
                    "Notificacao enviada para avaliacao: " + avaliacaoId
            );

        } catch (Exception e) {
            context.getLogger().severe(
                    "Falha no processamento da avaliacao "
                            + avaliacaoId + ": " + e
            );

            java.io.StringWriter detalhes = new java.io.StringWriter();
            e.printStackTrace(new java.io.PrintWriter(detalhes));

            context.getLogger().severe(detalhes.toString());

            throw e;
        }

    }
}
