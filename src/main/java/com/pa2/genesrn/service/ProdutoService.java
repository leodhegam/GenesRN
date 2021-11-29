package com.pa2.genesrn.service;

import com.pa2.genesrn.model.Produto;
import com.pa2.genesrn.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class ProdutoService {

    @Autowired
    ProdutoRepository produtoRepository;

    public List<Produto> findAll(){
        List<Produto> produtos = produtoRepository.findAll();
        return produtos;
    }
    public void salvar(Produto produto) {
        produtoRepository.save(produto);
    }

    public Produto getProductById(String nome) {
        return produtoRepository.findByNome(nome);
    }

    public List<Produto> buscarProdutos(Integer idUsuario){
        return produtoRepository.listarProdutoPorUsuario(idUsuario);
    }
}
