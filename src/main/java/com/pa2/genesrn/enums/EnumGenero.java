package com.pa2.genesrn.enums;

public enum EnumGenero {


    BOVINO("Bovino"),
    EQUINO("Equino"),
    SUINO("Suino");


    private final String descricao;

    EnumGenero(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
