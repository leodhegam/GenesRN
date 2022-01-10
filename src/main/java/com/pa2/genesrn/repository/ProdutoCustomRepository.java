package com.pa2.genesrn.repository;

import com.pa2.genesrn.model.Produto;

import java.util.List;

public interface ProdutoCustomRepository {
    public List<Produto> listarProdutoPorUsuario(Integer idUsuario);
    public List<Produto> listarProduto(Integer idUsuario);
}
