package com.pa2.genesrn.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "usuario")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Min(5)
    private String nome;

    @Column(unique = true)
    @Email
    @NotNull
    private String email;

    @NotNull
    private String senha;

    private String rua;

    private String complemento;

    private int numero;

    private String cidade;

    private String uf;


    private String cep;

    @Column(name = "reset_password_token")
    private String resetPasswordToken;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "usuario")
    List<Produto> produtos;

    @Override
    public String toString() {
        return "Usuario{" +
                +
                        '}';
    }
}
