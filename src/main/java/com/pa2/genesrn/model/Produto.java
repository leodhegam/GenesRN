package com.pa2.genesrn.model;

import com.pa2.genesrn.enums.EnumGenero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "produto")
public class Produto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String descricao;
    private Float valor;
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


    public String getFotoReprodutor() {
        return fotoReprodutor;
    }

    public void setFotoReprodutor(String fotoReprodutor) {
        this.fotoReprodutor = fotoReprodutor;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Float getValor() {
        return valor;
    }

    public void setValor(Float valor) {
        this.valor = valor;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    @Override
    public String toString() {
        return "Produto{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", descricao='" + descricao + '\'' +
                ", valor=" + valor +
                ", fotoReprodutor='" + fotoReprodutor + '\'' +
                ", quantidade=" + quantidade +
                ", usuario=" + usuario +
                '}';
    }
}
