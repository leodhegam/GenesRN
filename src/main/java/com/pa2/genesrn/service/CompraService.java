package com.pa2.genesrn.service;

import com.pa2.genesrn.model.Compra;
import com.pa2.genesrn.model.Usuario;
import com.pa2.genesrn.repository.CompraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompraService {

    @Autowired
    private CompraRepository repository;

    public List<Compra> getAllByUsuario(Usuario usuario) {
        return repository.findAllByUsuarioOrderByDataCompraDesc(usuario);
    }
}
