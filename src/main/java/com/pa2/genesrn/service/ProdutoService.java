package com.pa2.genesrn.service;

import com.pa2.genesrn.enums.EnumGenero;
import com.pa2.genesrn.model.Produto;
import com.pa2.genesrn.model.Usuario;
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
    public List<Produto> listarProdutos(Integer idUsuario){
       return produtoRepository.listarProduto(idUsuario);
    }
    public void salvar(Produto produto) {
        produtoRepository.save(produto);
    }

    public Produto getProductById(Long id) {
        return produtoRepository.findById(id);
    }

    public List<Produto> buscarProdutos(Integer id){
        return produtoRepository.listarProdutoPorUsuario(id);
    }

    public List<Produto> pegarMeusProdutos(Usuario usuario) {
        return produtoRepository.findAllByUsuario(usuario);
    }

}
