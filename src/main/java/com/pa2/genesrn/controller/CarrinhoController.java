package com.pa2.genesrn.controller;

import com.pa2.genesrn.model.Compra;
import com.pa2.genesrn.model.ItensCompra;
import com.pa2.genesrn.model.Produto;
import com.pa2.genesrn.model.Usuario;
import com.pa2.genesrn.repository.CompraRepository;
import com.pa2.genesrn.repository.ItensCompraRepository;
import com.pa2.genesrn.repository.ProdutoRepository;
import com.pa2.genesrn.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Controller
public class CarrinhoController {

    private List<ItensCompra> itensCompra = new ArrayList<ItensCompra>();
    private Compra compra = new Compra();
    @Autowired
    ProdutoRepository produtoRepository;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    private CompraRepository compraRepository;

    @Autowired
    private ItensCompraRepository itensCompraRepository;

    private void calcularTotal() {
        compra.setValorTotal(0.);
        for (ItensCompra it : itensCompra) {
            compra.setValorTotal(compra.getValorTotal() + it.getValorTotal());
        }
    }

    @GetMapping("/carrinho")
    public ModelAndView chamarCarrinho(Principal p) {
        Usuario usuario = usuarioRepository.findByEmail(p.getName());
        ModelAndView mv = new ModelAndView("carrinho");
        calcularTotal();
        Integer count = itensCompra.size();
        mv.addObject("compra", compra);
        mv.addObject("usuario", usuario);
        mv.addObject("listaItens", itensCompra);
        mv.addObject("count", count);
        return mv;
    }

    @GetMapping("/finalizar")
    public ModelAndView finalizarCompra(Principal p) {
        Usuario usuario = usuarioRepository.findByEmail(p.getName());
        ModelAndView mv = new ModelAndView("finalizar");
        calcularTotal();
        Integer count = itensCompra.size();
        mv.addObject("compra", compra);
        mv.addObject("listaItens", itensCompra);
        mv.addObject("usuario", usuario);

        return mv;
    }

    @PostMapping("/finalizar/confirmar")
    public ModelAndView confirmarCompra(Principal p, String formaPagamento) {
        ModelAndView mv = new ModelAndView("mensagemFinalizou");
        Usuario usuario = usuarioRepository.findByEmail(p.getName());
        compra.setUsuario(usuario);
        compra.setFormaPagamento(formaPagamento);
        compraRepository.saveAndFlush(compra);

        for (ItensCompra c : itensCompra) {
            c.setCompra(compra);
            c.setStatus("Processando");
            itensCompraRepository.saveAndFlush(c);
        }
        itensCompra = new ArrayList<>();
        compra = new Compra();
        return mv;
    }

    @GetMapping("/alterarQuantidade/{id}/{acao}")
    public String alterarQuantidade(@PathVariable Long id, @PathVariable Integer acao) {

        for (ItensCompra it : itensCompra) {
            if (it.getProduto().getId().equals(id)) {
                if (acao.equals(1)) {
                    it.setQuantidade(it.getQuantidade() + 1);
                    it.setValorTotal(0.);
                    it.setValorTotal(it.getValorTotal() + (it.getQuantidade() * it.getValorUnitario()));
                } else if (acao.equals(0)) {
                    it.setQuantidade(it.getQuantidade() - 1);
                    it.setValorTotal(0.);
                    it.setValorTotal(it.getValorTotal() + (it.getQuantidade() * it.getValorUnitario()));
                }
                break;
            }
        }
        return "redirect:/carrinho";
    }

    @GetMapping("/removerProduto/{id}")
    public String removerProduto(@PathVariable Long id) {
        for (ItensCompra it : itensCompra) {
            if (it.getProduto().getId().equals(id)) {
                itensCompra.remove(it);
                break;
            }
        }
        return "redirect:/carrinho";
    }

    @GetMapping("/adicionarCarrinho/{id}")
    public String adicionarCarrinho(@PathVariable Long id) {


        Optional<Produto> prod = Optional.ofNullable(produtoRepository.findById(id));
        Produto produto = prod.get();
        int controle = 0;
        for (ItensCompra it : itensCompra) {
            if (it.getProduto().getId().equals(produto.getId())) {
                it.setQuantidade(it.getQuantidade() + 1);
                it.setValorTotal(0.);
                it.setValorTotal(it.getValorTotal() + (it.getQuantidade() * it.getValorUnitario()));
                controle = 1;
                break;
            }
        }
        if (controle == 0) {
            ItensCompra item = new ItensCompra();
            item.setProduto(produto);
            item.setValorUnitario(produto.getValor());
            item.setQuantidade(item.getQuantidade() + 1);

            item.setValorTotal(item.getValorTotal() + (item.getQuantidade() * item.getValorUnitario()));
            itensCompra.add(item);
        }

        return "redirect:/carrinho";
    }


}


