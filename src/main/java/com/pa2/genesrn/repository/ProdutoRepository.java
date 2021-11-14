package com.pa2.genesrn.repository;


import com.pa2.genesrn.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Integer> {
}
