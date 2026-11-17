package br.com.fiap.feedback_api.document;

public class AvaliacaoDocument {

    private String id;
    private String descricao;
    private Integer nota;
    private String urgencia;
    private String dataCriacao;

    public AvaliacaoDocument() {
    }

    public AvaliacaoDocument(
            String id,
            String descricao,
            Integer nota,
            String urgencia,
            String dataCriacao) {

        this.id = id;
        this.descricao = descricao;
        this.nota = nota;
        this.urgencia = urgencia;
        this.dataCriacao = dataCriacao;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getNota() {
        return nota;
    }

    public void setNota(Integer nota) {
        this.nota = nota;
    }

    public String getUrgencia() {
        return urgencia;
    }

    public void setUrgencia(String urgencia) {
        this.urgencia = urgencia;
    }

    public String getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(String dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
}