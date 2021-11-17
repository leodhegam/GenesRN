package com.pa2.genesrn.controller;


import com.pa2.genesrn.model.Produto;
import com.pa2.genesrn.model.Usuario;
import com.pa2.genesrn.repository.ProdutoRepository;
import com.pa2.genesrn.repository.UsuarioRepository;
import com.pa2.genesrn.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/produto")
public class ProdutoController {

    @Autowired
    ProdutoRepository produtoRepository;

    @Autowired
    ProdutoService produtoService;

    @Autowired
    UsuarioRepository usuarioRepository;
//TODO
    @GetMapping("/listar")
    public String listar(Model model, Principal p) {

        String nome = p.getName();
        Usuario usuario = usuarioRepository.findByEmail(nome);
        List<Produto> produtos = produtoService.buscarProdutos(usuario.getId());
        model.addAttribute("produtos", produtos);
        model.addAttribute("usuario", usuario);


        return "/produto/listar";
    }

    @PostMapping("/cadastrar")
    public String cadastrar(@ModelAttribute Produto produto) {
        produtoRepository.save(produto);
        return "redirect:/home";
    }
}
