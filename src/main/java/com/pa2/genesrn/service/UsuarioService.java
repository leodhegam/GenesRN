package com.pa2.genesrn.service;

import com.pa2.genesrn.model.Usuario;
import com.pa2.genesrn.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
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

    public void updateResetPasswordToken(String token, String email)  {
        Usuario usuario = usuarioRepository.findByEmail(email);
        if (usuario != null) {
            usuario.setResetPasswordToken(token);
            usuarioRepository.save(usuario);
        }
//        else {
////            throw new CustomerNotFoundException("Could not find any customer with the email " + email);
//        }
    }

    public Usuario getByResetPasswordToken(String token) {
        return usuarioRepository.findByResetPasswordToken(token);
    }

    public void updatePassword(Usuario usuario, String newPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(newPassword);
        usuario.setSenha(encodedPassword);

        usuario.setResetPasswordToken(null);
        usuarioRepository.save(usuario);
    }
}
