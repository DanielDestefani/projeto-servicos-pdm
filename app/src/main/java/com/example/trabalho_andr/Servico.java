package com.example.trabalho_andr;

public class Servico {
    private String titulo;
    private String descricao;
    private double valor;
    private boolean aceito;

    public Servico(String titulo, String descricao, double valor, boolean aceito) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.valor = valor;
        this.aceito = aceito;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public boolean isAceito() {
        return aceito;
    }

    public void setAceito(boolean aceito) {
        this.aceito = aceito;
    }
}
