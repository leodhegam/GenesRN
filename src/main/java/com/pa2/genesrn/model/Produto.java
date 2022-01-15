package com.pa2.genesrn.model;

import com.pa2.genesrn.enums.EnumGenero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "produto")
public class Produto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "O nome do produto não pode estar vazio.")
    private String nome;
    private String descricao;
    private Double valor;
    private String fotoReprodutor;
    private Integer quantidade;
    private String linhagem;
    private String registro;
    private String criador;
    private String proprietario;
    private String dataNascimento;
    private String codigo;
    private String categoria;
    private Float peso;
    private String raca;

    @NotNull
    @Column(name = "genero")
    @Enumerated(EnumType.STRING)
    private EnumGenero genero;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;


}
