package com.pa2.genesrn.repository;

import com.pa2.genesrn.model.Compra;
import com.pa2.genesrn.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompraRepository extends JpaRepository<Compra,Long> {
    List<Compra> findAllByUsuario(Usuario usuario);

    List<Compra> findAllByUsuarioOrderByDataCompraDesc(Usuario usuario);
}
