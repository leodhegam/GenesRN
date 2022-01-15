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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
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

        return "/home";
    }
//    @GetMapping("/home")
//    public String home(Model model) {
//        List<Produto> produtos = produtoService.findAll();
//        model.addAttribute("produtos", produtos);
//        return "/home";
//    }

//    @GetMapping("/sobre")
//    public ModelAndView sobre(Principal principal) {
//        ModelAndView modelAndView = new ModelAndView();
//        Usuario usuario = usuarioRepository.findByEmail(principal.getName());
//        modelAndView.addObject("usuario", usuario);
//        modelAndView.setViewName("sobre");
//        return modelAndView;
//    }

    @GetMapping("/")
    public String sobre() {
        return "/sobre";
    }

    @GetMapping("/login")
    public String login() {
        return "/login";
    }


    @GetMapping("/cadastrarUsuario")
    public String cadastrar() {
        return "/usuario/cadastrarUsuario";
    }

    @PostMapping("/cadastrarUsuario")
    public String cadastrar(@ModelAttribute @Valid Usuario usuario, HttpSession session, Errors errors, HttpServletRequest request) {
        Usuario usuarioExists = usuarioService.findByEmail(usuario.getEmail());

        String confirmaSenha = request.getParameter("confirmaSenha");
        String senha = usuario.getSenha();

        if (usuarioExists != null) {
            session.setAttribute("messageError", "E-mail já existente");
            return "redirect:/cadastrarUsuario";
        }
        if (errors.hasErrors()) {
            session.setAttribute("message", "Verifique os campos");
            return "/cadastrarUsuario";
        } else if (!senha.equals(confirmaSenha)) {
            session.setAttribute("messageError", "As senhas não coincidem");
            return "/cadastrarUsuario";
        } else {
            usuarioService.save(usuario);
            session.setAttribute("message", "Usuário registrado com sucesso");
            return "redirect:/";
        }
    }

    @GetMapping("edit/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid usuario Id:" + id));
        model.addAttribute("usuario", usuario);
        return "/usuario/editarUsuario";
    }

    @PostMapping("update/{id}")
    public String updateUsuario(@PathVariable("id") Integer id, @Valid Usuario usuario, BindingResult result, Model model, HttpServletRequest httpServletRequest, HttpSession session, RedirectAttributes redirectAttributes) {
            if (result.hasErrors()) {
                usuario.setId(id);
                return "/usuario/editarUsuario";
            }

            String senhaDb = usuario.getSenha();
            String senha = httpServletRequest.getParameter("inputSenha");
            String confirmarSenha = httpServletRequest.getParameter("inputConfirmarSenha");
            String senhaAtual = httpServletRequest.getParameter("inputSenhaAtual");

            if (senha.isEmpty() && confirmarSenha.isEmpty() && senhaAtual.isEmpty()) {
                usuarioService.saveAndFlush(usuario);
            } else if (senha.isEmpty() && confirmarSenha.isEmpty()) {
                session.setAttribute("messageError", "Senha invalida");
                return "/usuario/editarUsuario";
            } else if (senha.equals(confirmarSenha)) {
                if (bp.matches(senhaAtual, senhaDb)) {
                    usuario.setSenha(bp.encode(senha));
                    usuarioService.saveAndFlush(usuario);
                }  else {
                    session.setAttribute("messageError", "Senha atual incorreta");
                    return "/usuario/editarUsuario";
                }
            } else {
                session.setAttribute("messageError", "As senhas não coincidem");
                return "/usuario/editarUsuario";
            }
            model.addAttribute("message", "Atualização de dados realizada com sucesso!");
            model.addAttribute("alertClass", "alert-success");
            return "/usuario/editarUsuario";
        }

    }
