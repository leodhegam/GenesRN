package com.pa2.genesrn.controller;

import com.pa2.genesrn.model.*;
import com.pa2.genesrn.service.CompraService;
import com.pa2.genesrn.service.ItensCompraService;
import com.pa2.genesrn.service.ProdutoService;
import com.pa2.genesrn.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping()
public class CompraController {
    @Autowired
    private ItensCompraService itensCompraService;

    @Autowired
    private CompraService compraService;

    @Autowired
    private ProdutoService produtoService;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/meusPedidos")
    public ModelAndView meusPedidos(Principal p) {
        ModelAndView modelAndView = new ModelAndView("/pedido/meusPedidos");

        Usuario usuario = usuarioService.findByEmail(p.getName());
        List<Compra> compras = compraService.getAllByUsuario(usuario);
        List<Pedido> pedidos = itensCompraService.getAllByCompra(compras);

        modelAndView.addObject("pedidos", pedidos);
        modelAndView.addObject("usuario", usuario);
        return modelAndView;
    }

    @GetMapping("/minhasVendas")
    public ModelAndView minhasCompras(Principal p) {
        ModelAndView modelAndView = new ModelAndView("/pedido/minhasVendas");

        Usuario usuario = usuarioService.findByEmail(p.getName());
        List<Produto> produtos = produtoService.pegarMeusProdutos(usuario);
        List<ItensCompra> itensCompras = itensCompraService.getAllByProduto(produtos);

        List<String> status = new ArrayList<>();

        status.add("Processando");
        status.add("Encaminhado");
        status.add("Enviado");
        status.add("Entregue");

        modelAndView.addObject("compras", itensCompras);
        modelAndView.addObject("usuario", usuario);
        modelAndView.addObject("allStatus", status);
        return modelAndView;
    }

}
