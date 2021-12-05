package com.pa2.genesrn.service;

import com.pa2.genesrn.model.Usuario;
import com.pa2.genesrn.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioService {
    @Autowired
    UsuarioRepository usuarioRepository;


    public Optional<Usuario> save(Usuario usuario) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String senhaEconder = encoder.encode(usuario.getSenha());
        usuario.setSenha(senhaEconder);
        return Optional.of(usuarioRepository.save(usuario));
    }

    public Optional<Usuario> saveAndFlush(Usuario usuario) {
        return Optional.of(usuarioRepository.saveAndFlush(usuario));
    }

    public Usuario findByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public Optional<Usuario> editarUsuario(Usuario usuario) {
        return Optional.of(usuarioRepository.saveAndFlush(usuario));
    }


    public Usuario findUserByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }
}
