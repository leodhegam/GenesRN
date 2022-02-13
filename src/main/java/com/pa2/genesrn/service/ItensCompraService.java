package com.pa2.genesrn.service;

import com.pa2.genesrn.model.Compra;
import com.pa2.genesrn.model.ItensCompra;
import com.pa2.genesrn.model.Pedido;
import com.pa2.genesrn.model.Produto;
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

    public ItensCompra getById(Long id) {
        return repository.getById(id);
    }

    public ItensCompra save(ItensCompra compra) {
        return repository.save(compra);
    }

    public List<ItensCompra> getAllByProduto(List<Produto> produtos) {
        List<ItensCompra> itens = new ArrayList<>();

        for (Produto p : produtos) {
            var itensComprasList = repository.findAllByProduto(p);
            itens.addAll(itensComprasList);
        }
        return itens;
    }
}
