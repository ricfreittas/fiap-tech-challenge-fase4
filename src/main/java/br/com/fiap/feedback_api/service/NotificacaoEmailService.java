package br.com.fiap.feedback_api.service;

import com.azure.communication.email.EmailClient;
import com.azure.communication.email.EmailClientBuilder;
import com.azure.communication.email.models.EmailMessage;
import org.springframework.stereotype.Service;

@Service
public class NotificacaoEmailService {

    private final EmailClient emailClient;

    public NotificacaoEmailService() {

        String connectionString =
                System.getenv("ACS_CONNECTION_STRING");

        if (connectionString == null || connectionString.isBlank()) {
            throw new IllegalStateException(
                    "ACS_CONNECTION_STRING nao configurada."
            );
        }

        this.emailClient = new EmailClientBuilder()
                .connectionString(connectionString)
                .buildClient();
    }


    public void enviarEmail(String destinatario, String assunto, String mensagem) {

        String remetente = System.getenv("ACS_EMAIL_REMETENTE");

        if (remetente == null || remetente.isBlank()) {
            throw new IllegalStateException(
                    "ACS_EMAIL_REMETENTE nao configurado."
            );
        }

        EmailMessage emailMessage = new EmailMessage()
                .setSenderAddress(remetente)
                .setToRecipients(destinatario)
                .setSubject(assunto)
                .setBodyPlainText(mensagem);

        emailClient.beginSend(emailMessage)
                .getFinalResult();
    }

}