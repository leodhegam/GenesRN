package com.pa2.genesrn.repository;


import com.pa2.genesrn.enums.EnumGenero;
import com.pa2.genesrn.model.Produto;
import com.pa2.genesrn.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProdutoRepository extends JpaRepository<Produto, Integer>,ProdutoCustomRepository {
    Produto findById(Long id);

    List<Produto> findAllByUsuario(Usuario usuario);

    List<Produto> findAllByGeneroAndUsuarioNot(EnumGenero genero, Usuario usuario);


    Produto findByNome(String nome);
}
