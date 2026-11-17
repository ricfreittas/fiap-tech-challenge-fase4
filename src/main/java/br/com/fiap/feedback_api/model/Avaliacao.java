package br.com.fiap.feedback_api.model;

import java.time.Instant;

public class Avaliacao {
    private String id;
    private String descricao;
    private Integer nota;
    private Instant dataCriacao;
    private Urgencia urgencia;

    public Avaliacao(String id, String descricao, Integer nota, Instant dataCriacao, Urgencia urgencia) {
        this.id = id;
        this.descricao = descricao;
        this.nota = nota;
        this.dataCriacao = dataCriacao;
        this.urgencia = urgencia;
    }

    public String getId (){
        return id;
    }
    public String getDescricao(){
        return descricao;
    }

    public Integer getNota(){
        return nota;
    }

    public Instant getDataCriacao(){return dataCriacao;}

    public Urgencia getUrgencia(){return urgencia;}
}
