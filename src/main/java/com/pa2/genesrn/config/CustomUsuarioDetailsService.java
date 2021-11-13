package com.pa2.genesrn.config;

import com.pa2.genesrn.model.Usuario;
import com.pa2.genesrn.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CustomUsuarioDetailsService implements UserDetailsService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            Usuario usuario = usuarioRepository.findByEmail(email);
            if (usuario == null) {
                throw new UsernameNotFoundException("Usuario não encontrado");
            } else {
                return new CustomUsuarioDetails(usuario);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}