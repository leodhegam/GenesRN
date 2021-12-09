package com.pa2.genesrn.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

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

    @NotNull
    private String endereco;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "usuario")
    List<Produto> produtos;

    @Override
    public String toString() {
        return "Usuario{" +
                +
                        '}';
    }
}
