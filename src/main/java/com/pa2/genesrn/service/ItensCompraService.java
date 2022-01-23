package com.pa2.genesrn.service;

import com.pa2.genesrn.model.Compra;
import com.pa2.genesrn.model.ItensCompra;
import com.pa2.genesrn.model.Pedido;
import com.pa2.genesrn.repository.ItensCompraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItensCompraService {

    @Autowired
    private ItensCompraRepository repository;

    public List<Pedido> getAllByCompra(List<Compra> compras) {
        List<Pedido> pedidos = new ArrayList<>();
        for (Compra c : compras) {
            List<ItensCompra> itens = repository.findAllByCompra(c);
            pedidos.add(new Pedido(c, itens));
        }
        return pedidos;
    }
}
