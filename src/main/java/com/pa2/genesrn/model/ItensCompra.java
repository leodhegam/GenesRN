package com.pa2.genesrn.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "itens_compra")
public class ItensCompra {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Produto produto;
    @ManyToOne
    private Compra compra;

    private String status;

    private Integer quantidade;

    private Double valorUnitario = 0.;

    private Double valorTotal = 0.;

    public Integer getQuantidade() {
        if (quantidade == null) {
            quantidade = 0;
        }
        return quantidade;
    }
}
