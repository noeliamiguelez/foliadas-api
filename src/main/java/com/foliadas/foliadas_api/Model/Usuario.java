package com.foliadas.foliadas_api.Model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @Column(name = "email", unique = true, nullable = false, length = 100)
    private String email;

    @Column(name = "password", nullable = false, length = 255)
    private String password;


    @ManyToMany
    @JoinTable(
            name = "favorita",
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "foliada_id")
    )
    private Set<Foliada> favoritas= new HashSet<>();

    public Usuario() {}

    public Usuario(int id, String nome, String email, String password, Set<Foliada> favoritas) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.password = password;
        this.favoritas = favoritas;
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

    public Set<Foliada> getFavoritas() {
        return favoritas;
    }

    public void setFavoritas(Set<Foliada> favoritas) {
        this.favoritas = favoritas;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}