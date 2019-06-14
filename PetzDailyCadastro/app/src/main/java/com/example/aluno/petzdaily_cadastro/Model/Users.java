package com.example.aluno.petzdaily_cadastro.Model;

public class Users {
    private String Nome, Email, Login, Senha;

    public String getNome() {
        return Nome;
    }

    public void setNome(String nome) {
        Nome = nome;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getLogin() {
        return Login;
    }

    public void setLogin(String login) {
        Login = login;
    }

    public String getSenha() {
        return Senha;
    }

    public void setSenha(String senha) {
        Senha = senha;
    }

    public Users(String nome, String email, String login, String senha) {
        Nome = nome;
        Email = email;
        Login = login;
        Senha = senha;
    }

    public Users()
    {

    }


}
