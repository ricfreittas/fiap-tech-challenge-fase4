package br.com.fiap.feedback_api.service;

import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.storage.queue.QueueClient;
import com.azure.storage.queue.QueueClientBuilder;
import org.springframework.stereotype.Service;

@Service
public class NotificacaoQueueService {

    private final QueueClient queueClient;

    public NotificacaoQueueService() {

        String endpoint =
                "https://fiapfeedbackfunctio96db.queue.core.windows.net";

        this.queueClient = new QueueClientBuilder()
                .endpoint(endpoint)
                .queueName("notificacoes-criticas")
                .credential(
                        new DefaultAzureCredentialBuilder().build()
                )
                .buildClient();
    }

    public void enviarNotificacao(String mensagem) {

        queueClient.sendMessage(mensagem);

    }
}