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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

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

        Usuario usuario = usuarioRepository.findByEmail(p.getName());
        List<Produto> produtos = produtoService.pegarMeusProdutos(usuario);
        System.out.println("Produtos antes "+produtos);
        model.addAttribute("usuario", usuario);
        model.addAttribute("produtos", produtos);
        return "/produto/meusProdutos";
    }

    @GetMapping("/alterar/{produto}")
    public ModelAndView alterarProduto(@PathVariable(name = "produto") String idProduto, Principal p){
        ModelAndView modelAndView = new ModelAndView("/produto/editarProduto");
        Usuario usuario = usuarioRepository.findByEmail(p.getName());
        var upProd = produtoService.getProductById(Long.valueOf(idProduto));
        modelAndView.setViewName("/produto/editarProduto");
        modelAndView.addObject("usuario", usuario);
        modelAndView.addObject("produto", upProd);
        return modelAndView;
    }
    
    @GetMapping("/filtro/{nomeFiltro}")
    public ModelAndView vitrineProdutos(
            @PathVariable String nomeFiltro,
            Principal p) {

        String nome = p.getName();
        Usuario usuario = usuarioRepository.findByEmail(nome);
        List<Produto> prod = produtoService.findAll();
        List<Produto> produtos = new ArrayList<>();
        System.out.println(prod);
        prod.forEach(e -> {
            System.out.println(e.getGenero().getDescricao());
            if(
                    e.getUsuario().getId() != usuario.getId() &&
                    e.getGenero().getDescricao().equals(nomeFiltro)
            ){
                produtos.add(e);
            }
        });
        ModelAndView model = new ModelAndView("/filtro");
        model.addObject("usuario", usuario);
        model.addObject("produtos", produtos);
        System.out.println("Coleta de produtos " +produtos);

        return model;
    }

    @RequestMapping("/detalhesProduto/{produto}")
    public ModelAndView detalhesProduto(@PathVariable(name = "produto") String idProduto, Principal p){
        ModelAndView modelAndView = new ModelAndView("/produto/detalhesProduto");

        Usuario usuario = usuarioRepository.findByEmail(p.getName());
        var produto = produtoService.getProductById(Long.valueOf(idProduto));
        if(usuario.getId().equals(produto.getUsuario().getId())){
            return new ModelAndView("redirect:/home");
        }

        modelAndView.addObject("produto", produto);
        modelAndView.addObject("usuario", usuario);
        return modelAndView;
    }

    @RequestMapping("/cadastrarNovoProduto")
    public ModelAndView cadastrarNovoProduto(Principal p) {
        ModelAndView modelAndView = new ModelAndView("/produto/cadastrarProduto");
        modelAndView.addObject("produto", new Produto());
        modelAndView.addObject("usuario", usuarioRepository.findByEmail(p.getName()));
        return modelAndView;
    }

    @PostMapping("/cadastrarProduto")
    public String cadastrarProduto(
            @ModelAttribute Produto produto,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
            BindingResult result,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("message", "Falha ao tentar salvar os dados.");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            redirectAttributes.addFlashAttribute("produto", produto);
            if(produto.getId() != null) {
                return "redirect:/produto/alterar/"+ produto.getId();
            }
            return "redirect:/produto/cadastrarNovoProduto";
        }

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

        redirectAttributes.addFlashAttribute("message", "Sucesso ao realizar cadastro!");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");
        return "/sweet/sweetCadastrarProduto";

        //"redirect:/produto/meusProdutos"
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
