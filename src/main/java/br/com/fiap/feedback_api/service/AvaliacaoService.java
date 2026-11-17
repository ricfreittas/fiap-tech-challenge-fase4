package br.com.fiap.feedback_api.service;

import br.com.fiap.feedback_api.dto.AvaliacaoRequest;
import br.com.fiap.feedback_api.model.Avaliacao;
import br.com.fiap.feedback_api.model.Urgencia;
import br.com.fiap.feedback_api.repository.CosmosAvaliacaoRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class AvaliacaoService {

    private final CosmosAvaliacaoRepository avaliacaoRepository;
    private final NotificacaoQueueService notificacaoQueueService;

    public AvaliacaoService(
            CosmosAvaliacaoRepository avaliacaoRepository,
            NotificacaoQueueService notificacaoQueueService) {

        this.avaliacaoRepository = avaliacaoRepository;
        this.notificacaoQueueService = notificacaoQueueService;
    }
    public Avaliacao processar(AvaliacaoRequest request) {

        Urgencia urgencia = classificarUrgencia(request.nota());

        Avaliacao avaliacao = new Avaliacao(
                UUID.randomUUID().toString(),
                request.descricao(),
                request.nota(),
                Instant.now(),
                urgencia
        );

        avaliacaoRepository.salvar(avaliacao);

        if (urgencia == Urgencia.CRITICA) {

            notificacaoQueueService.enviarNotificacao(
                    avaliacao.getId()
            );

        }

        return avaliacao;
    }

    public List<Avaliacao> listar(){
        return avaliacaoRepository.listar();
    }

    public Avaliacao buscarPorId(String id){
         return avaliacaoRepository.buscarPorId(id);
    }

    public Avaliacao atualizar(String id, AvaliacaoRequest request){

        Urgencia urgencia = classificarUrgencia(request.nota());

        avaliacaoRepository.atualizar(
                id,
                request.descricao(),
                request.nota(),
                urgencia
        );

        return avaliacaoRepository.buscarPorId(id);
    }

    public void excluir(String id){
        avaliacaoRepository.excluir(id);

    }

    public Urgencia classificarUrgencia(Integer nota) {

        if (nota <= 3) {
            return Urgencia.CRITICA;
        }

        if (nota <= 6) {
            return Urgencia.ATENCAO;
        }

        return Urgencia.NORMAL;
    }
}