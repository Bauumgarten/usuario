package com.estudo_raul.java.business;

import com.estudo_raul.java.business.converter.UsuarioConverter;
import com.estudo_raul.java.business.dtos.UsuarioDTO;
import com.estudo_raul.java.infrastructure.entity.Usuario;
import com.estudo_raul.java.infrastructure.exceptions.ConflictException;
import com.estudo_raul.java.infrastructure.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.config.ConfigDataException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ConcurrentModificationException;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioConverter usuarioConverter;
    private final PasswordEncoder passwordEncoder;


    public Usuario salvaUsuario(Usuario usuario) {
        try {
            emailExiste(usuario.getEmail());
           usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
            return usuarioRepository.save(usuario);
        } catch (ConflictException e) {
            throw new ConflictException("Email já cadastrado", e.getCause());
        }
    }

    public void emailExiste(String email){
        try{
            boolean existe = verificaEmailExistente(email);
            if (existe){
                throw new ConflictException("Email já cadastrado " + email);
            }
        }catch (ConflictException e){
            throw new ConflictException("Email já cadastrado", e.getCause());
        }
    }

    public boolean verificaEmailExistente(String email){
        return usuarioRepository.existsByEmail(email);
    }

    public UsuarioDTO salvaUsuario(UsuarioDTO usuarioDTO){
        Usuario usuario = usuarioConverter.paraUsuario(usuarioDTO);
        return usuarioConverter.paraUsuarioDTO(
                usuarioRepository.save(usuario));
    }

}
