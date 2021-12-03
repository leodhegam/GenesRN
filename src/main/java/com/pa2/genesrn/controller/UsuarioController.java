package com.pa2.genesrn.controller;

import com.pa2.genesrn.model.Produto;
import com.pa2.genesrn.model.Usuario;
import com.pa2.genesrn.repository.UsuarioRepository;
import com.pa2.genesrn.service.ProdutoService;
import com.pa2.genesrn.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ProdutoService produtoService;

    @Autowired
    private BCryptPasswordEncoder bp;

    @GetMapping("/home")
    public String home(Principal p, Model model) {
        String nome = p.getName();
        Usuario usuario = usuarioRepository.findByEmail(nome);
        List<Produto> produtos = produtoService.findAll();
        System.out.println(produtos);
        model.addAttribute("usuario", usuario);
        model.addAttribute("produtos", produtos);

        return "home";
    }

    @GetMapping("/")
    public String login() {
        return "login";
    }

    @GetMapping("/cadastrarUsuario")
    public String cadastrar() {
        return "cadastrarUsuario";
    }

    @PostMapping("/cadastrarUsuario")
    public String cadastrar(@ModelAttribute @Valid Usuario usuario, HttpSession session, Errors errors, BindingResult bindingResult) {
        Usuario usuarioExists = usuarioRepository.findByEmail(usuario.getEmail());
        if (usuarioExists != null) {
            session.setAttribute("messageError", "E-mail já existente");
            return "redirect:/cadastrarUsuario";
        }
        if (errors.hasErrors()) {
            session.setAttribute("message", "Verifique os campos");
            return "/cadastrarUsuario";
        } else {
            usuario.setSenha(bp.encode(usuario.getSenha()));
            usuarioRepository.save(usuario);
            session.setAttribute("message", "Usuário registrado com sucesso");
            return "redirect:/";
        }


    }

}
