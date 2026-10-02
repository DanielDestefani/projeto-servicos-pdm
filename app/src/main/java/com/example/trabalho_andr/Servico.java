package com.example.trabalho_andr;

import android.net.Uri;

public class Servico {
    private String titulo;
    private String descricao;
    private double valor;
    private boolean aceito;

    private Uri fotoUri;

    public Servico(String titulo, String descricao, double valor, boolean aceito, Uri fotoUri) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.valor = valor;
        this.aceito = aceito;
        this.fotoUri = fotoUri;
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

    public Uri getFotoUri() {
        return fotoUri;
    }

    public void setFotoUri(Uri fotoUri) {
        this.fotoUri = fotoUri;
    }
}
