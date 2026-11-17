package br.com.fiap.feedback_api.repository;

import com.azure.cosmos.CosmosClient;
import com.azure.cosmos.CosmosContainer;
import com.azure.cosmos.models.PartitionKey;
import com.azure.cosmos.models.CosmosItemResponse;
import com.azure.cosmos.models.SqlQuerySpec;
import com.azure.cosmos.models.CosmosItemRequestOptions;

import br.com.fiap.feedback_api.document.AvaliacaoDocument;
import br.com.fiap.feedback_api.model.Avaliacao;
import br.com.fiap.feedback_api.model.Urgencia;
import java.time.Instant;

import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;


@Repository
public class CosmosAvaliacaoRepository {

    private final CosmosContainer container;

    public CosmosAvaliacaoRepository(CosmosClient cosmosClient) {

        this.container = cosmosClient
                .getDatabase("fiap-feedback")
                .getContainer("Avaliacoes");
    }

    private AvaliacaoDocument paraDocumento(Avaliacao avaliacao) {

        return new AvaliacaoDocument(
                avaliacao.getId(),
                avaliacao.getDescricao(),
                avaliacao.getNota(),
                avaliacao.getUrgencia().name(),
                avaliacao.getDataCriacao().toString()
        );
    }

    private Avaliacao paraAvaliacao(AvaliacaoDocument documento) {

        return new Avaliacao(
                documento.getId(),
                documento.getDescricao(),
                documento.getNota(),
                Instant.parse(documento.getDataCriacao()),
                Urgencia.valueOf(documento.getUrgencia())
        );
    }

    public void salvar(Avaliacao avaliacao) {

        AvaliacaoDocument documento = paraDocumento(avaliacao);

        container.createItem(
                documento,
                new PartitionKey(documento.getId()),
                new CosmosItemRequestOptions()
        );
    }

    public void salvarDocumento(Map<String, Object> documento) {

        String id = (String) documento.get("id");

        container.upsertItem(
                documento,
                new PartitionKey(id),
                null
        );
    }

    public Avaliacao buscarPorId(String id) {

        CosmosItemResponse<AvaliacaoDocument> response =
                container.readItem(
                        id,
                        new PartitionKey(id),
                        AvaliacaoDocument.class
                );

        AvaliacaoDocument documento = response.getItem();

        return paraAvaliacao(documento);
    }

    public List<Map> listarTodas() {

        List<Map> avaliacoes = new ArrayList<>();

        String sql = "SELECT * FROM c";

        container.queryItems(
                new SqlQuerySpec(sql),
                null,
                Map.class
        ).forEach(avaliacoes::add);

        return avaliacoes;
    }

    public List<Avaliacao> listar() {

        List<Avaliacao> avaliacoes = new ArrayList<>();

        String sql = "SELECT * FROM c";

        container.queryItems(
                new SqlQuerySpec(sql),
                null,
                AvaliacaoDocument.class
        ).forEach(documento -> {

            Avaliacao avaliacao = paraAvaliacao(documento);

            avaliacoes.add(avaliacao);
        });

        return avaliacoes;
    }

    public void atualizar(
            String id,
            String descricao,
            Integer nota,
            Urgencia urgencia) {

        // Busca a avaliação existente
        Avaliacao existente = buscarPorId(id);

        // Cria uma nova versão, preservando ID e data original
        Avaliacao atualizada = new Avaliacao(
                existente.getId(),
                descricao,
                nota,
                existente.getDataCriacao(),
                urgencia
        );

        // Converte para o formato do Cosmos DB
        AvaliacaoDocument documento = paraDocumento(atualizada);

        // Substitui o documento no Azure
        container.replaceItem(
                documento,
                id,
                new PartitionKey(id),
                new CosmosItemRequestOptions()
        );
    }

    public void excluir(String id) {

        container.deleteItem(
                id,
                new PartitionKey(id),
                new CosmosItemRequestOptions()
        );
    }

    public void atualizarDocumento(
            String id,
            Map<String, Object> documento) {

        // Confirma que o documento existe
        container.readItem(
                id,
                new PartitionKey(id),
                Map.class
        );

        // Mantém o identificador original
        documento.put("id", id);

        // Substitui o documento existente
        container.replaceItem(
                documento,
                id,
                new PartitionKey(id),
                new CosmosItemRequestOptions()
        );
    }

    public void excluirPorId(String id) {

        container.deleteItem(
                id,
                new PartitionKey(id),
                new CosmosItemRequestOptions()
        );
    }
}