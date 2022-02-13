package com.pa2.genesrn.controller;

import com.pa2.genesrn.model.*;
import com.pa2.genesrn.service.CompraService;
import com.pa2.genesrn.service.ItensCompraService;
import com.pa2.genesrn.service.ProdutoService;
import com.pa2.genesrn.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
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
    public ModelAndView meusPedidos(Principal p, HttpSession session) {
        ModelAndView modelAndView = new ModelAndView("/pedido/meusPedidos");

        Usuario usuario = usuarioService.findByEmail(p.getName());
        List<Compra> compras = compraService.getAllByUsuario(usuario);
        List<Pedido> pedidos = itensCompraService.getAllByCompra(compras);

        modelAndView.addObject("pedidos", pedidos);
        modelAndView.addObject("usuario", usuario);

        Integer count = (Integer)session.getAttribute("count");
        modelAndView.addObject("count", count);

        return modelAndView;
    }

    @GetMapping("/minhasVendas")
    public ModelAndView minhasCompras(Principal p, HttpSession session) {
        ModelAndView modelAndView = new ModelAndView("/pedido/minhasVendas");

        Usuario usuario = usuarioService.findByEmail(p.getName());
        List<Produto> produtos = produtoService.pegarMeusProdutos(usuario);
        List<ItensCompra> itensCompras = itensCompraService.getAllByProduto(produtos);

        List<String> status = new ArrayList<>();

        status.add("Processando");
        status.add("Transporte");
        status.add("Enviado");
        status.add("Entregue");

        modelAndView.addObject("compras", itensCompras);
        modelAndView.addObject("usuario", usuario);
        modelAndView.addObject("allStatus", status);

        Integer count = (Integer)session.getAttribute("count");
        modelAndView.addObject("count", count);

        return modelAndView;
    }

    @GetMapping(value = "/compra")
    public ModelAndView compra(Principal p) {
        if (p == null) {
            return new ModelAndView("/home");
        }

        ModelAndView modelAndView = new ModelAndView("/pedido/atualizarStatus");

        List<String> status = new ArrayList<>();

        status.add("Processando");
        status.add("Transporte");
        status.add("Enviado");
        status.add("Entregue");

        modelAndView.addObject("compra", new ItensCompra());
        modelAndView.addObject("allStatus", status);
        return modelAndView;
    }

    @PostMapping(value = "/atualizarStatus", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ModelAndView atualizarStatusCompra(Principal p, ItensCompra compra, HttpSession session) {
        ItensCompra novo = itensCompraService.getById(compra.getId());
        novo.setStatus(compra.getStatus());
        System.out.println(novo.getCompra().getUsuario());
        itensCompraService.save(novo);
        return minhasCompras(p,session);
    }
}
