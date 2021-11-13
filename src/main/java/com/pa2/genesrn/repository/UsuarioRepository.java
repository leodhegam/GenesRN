package com.pa2.genesrn.repository;

import com.pa2.genesrn.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario,Integer> {

    public Usuario findByEmail(String email);
}
