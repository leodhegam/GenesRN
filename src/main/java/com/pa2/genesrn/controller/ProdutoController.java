package com.pa2.genesrn.controller;


import com.pa2.genesrn.enums.EnumGenero;
import com.pa2.genesrn.model.Produto;
import com.pa2.genesrn.model.Usuario;
import com.pa2.genesrn.repository.ProdutoRepository;
import com.pa2.genesrn.repository.UsuarioRepository;
import com.pa2.genesrn.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/produto")
public class ProdutoController {

    private static String caminhoImagens = "/src/main/resources/static/fotos/";

    @Autowired
    ProdutoRepository produtoRepository;

    @Autowired
    ProdutoService produtoService;

    @Autowired
    UsuarioRepository usuarioRepository;


    @GetMapping("/meusProdutos")
    public String produtos(Principal p, Model model) {
        System.out.println("meusProdutos");
        Usuario usuario = usuarioRepository.findByEmail(p.getName());
        List<Produto> produtos = produtoService.buscarProdutos(usuario);
        System.out.println("Produtos antes "+produtos);
        model.addAttribute("usuario", usuario);
        model.addAttribute("produtos", produtos);
        return "meusProdutos";
    }

    @RequestMapping("/alterar/{produto}")
    public ModelAndView alterarProduto(@PathVariable(name = "produto") String idProduto){
        ModelAndView modelAndView = new ModelAndView("alterar");
        var upProd = produtoService.getProductById(Long.valueOf(idProduto));

        modelAndView.setViewName("alterar");
        modelAndView.addObject("produto", upProd);
        return modelAndView;
    }

    @RequestMapping("/detalhesProduto/{produto}")
    public ModelAndView detalhesProduto(@PathVariable(name = "produto") String idProduto){
        ModelAndView modelAndView = new ModelAndView("detalhesProduto");
        var upProd = produtoService.getProductById(Long.valueOf(idProduto));

        modelAndView.setViewName("detalhesProduto");
        modelAndView.addObject("produto", upProd);
        return modelAndView;
    }
    
    @GetMapping("/vitrine")
    public String vitrineProdutos(Principal p, Model model) {
        String nome = p.getName();
        Usuario usuario = usuarioRepository.findByEmail(nome);
        List<Produto> produtos = produtoService.buscarProdutos(usuario);
        model.addAttribute("usuario", usuario);
        model.addAttribute("produtos", produtos);

        return "vitrine";
    }

    @PostMapping("/cadastrarProduto")
    public String cadastrarProduto(@ModelAttribute Produto produto, @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) {
        produtoRepository.save(produto);
        try {
            if (produto.getFotoReprodutor() == null && imageFile != null) {
                byte[] bytes = imageFile.getBytes();
                Path currentPath = Paths.get(".");
                Path absolutePath = currentPath.toAbsolutePath();
                Path caminho = Paths.get(absolutePath + caminhoImagens + String.valueOf(produto.getId()) + imageFile.getOriginalFilename());
                Files.write(caminho, bytes);
                produto.setFotoReprodutor(String.valueOf(produto.getId()) + imageFile.getOriginalFilename());
                produtoRepository.save(produto);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "redirect:/produto/meusProdutos";
    }

    @GetMapping("/mostrarImagem/{imagem}")
    @ResponseBody
    public byte[] retornarImagem(@PathVariable("imagem") String imagem) throws IOException {
        Path currentPath = Paths.get(".");
        Path absolutePath = currentPath.toAbsolutePath();
        File imagemArquivo = new File( absolutePath+caminhoImagens + imagem);

        if (imagem != null || imagem.trim().length() > 0) {
            return Files.readAllBytes(imagemArquivo.toPath());
        }
        return null;
    }
}
