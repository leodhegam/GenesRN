package com.pa2.genesrn.service;

import com.pa2.genesrn.model.Usuario;
import com.pa2.genesrn.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    UsuarioRepository usuarioRepository;

    public Usuario save(Usuario usuario){
        return usuarioRepository.save(usuario);
    }
    public Usuario findUserByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }
}
