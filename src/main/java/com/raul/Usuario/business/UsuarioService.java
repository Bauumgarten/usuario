package com.raul.Usuario.business;

import com.raul.Usuario.business.converter.UsuarioConverter;
import com.raul.Usuario.business.dto.UsuarioDTO;
import com.raul.Usuario.infrastructure.entity.Usuario;
import com.raul.Usuario.infrastructure.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioConverter usuarioConverter;

    public UsuarioDTO salvaUsuario(UsuarioDTO usuarioDTO){
        Usuario usuario = usuarioConverter.paraUsuario(usuarioDTO);
        return usuarioConverter.paraUsuarioDTO(usuario = usuarioRepository.save(usuario));
    }

}
