package com.pa2.genesrn.controller;

import com.pa2.genesrn.model.Usuario;
import com.pa2.genesrn.repository.UsuarioRepository;
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

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequestMapping("/")

public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private BCryptPasswordEncoder bp;

    @GetMapping("/home")
    public String home(Principal p, Model model) {
        String nome = p.getName();
        Usuario usuario = usuarioRepository.findByEmail(nome);
        model.addAttribute("usuario",usuario);
        return "home";
    }

    @GetMapping("/")
    public String index(){
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }

    @PostMapping("/cadastrar")
    public String cadastrar(@ModelAttribute @Valid Usuario usuario, HttpSession session, Errors errors, BindingResult bindingResult) {
//        Usuario userExists = usuarioService.findUserByEmail(usuario.getEmail());
//        if(userExists != null){
//            session.setAttribute("message", "E-mail já existente");
//        }
        Usuario usuarioExists = usuarioRepository.findByEmail(usuario.getEmail());
        if(usuarioExists != null){
            session.setAttribute("messageError", "E-mail já existente");
            return "redirect:/";
        }
        if (errors.hasErrors()) {
            session.setAttribute("message", "Verifique os campos");
            return "/";
        } else {
            usuario.setSenha(bp.encode(usuario.getSenha()));
            usuarioRepository.save(usuario);
            session.setAttribute("message", "Usuário registrado com sucesso");
            return "redirect:/";
        }


    }

}
