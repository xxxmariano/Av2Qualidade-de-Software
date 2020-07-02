package com.example.crudusuario.usuario;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Usuario implements Serializable {
    private int id;
    private String nome;
    private String login;
    private String senha;

    @Override
    public String toString() {
        return "Nome: "+this.nome;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
