package com.pa2.genesrn.repository;

import com.pa2.genesrn.model.Compra;
import com.pa2.genesrn.model.ItensCompra;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ItensCompraRepository extends JpaRepository<ItensCompra, Long> {
}