package com.estudo_raul.java.business;

import com.estudo_raul.java.business.converter.UsuarioConverter;
import com.estudo_raul.java.business.dtos.EnderecoDTO;
import com.estudo_raul.java.business.dtos.TelefoneDTO;
import com.estudo_raul.java.business.dtos.UsuarioDTO;
import com.estudo_raul.java.infrastructure.entity.Endereco;
import com.estudo_raul.java.infrastructure.entity.Telefone;
import com.estudo_raul.java.infrastructure.entity.Usuario;
import com.estudo_raul.java.infrastructure.exceptions.ConflictException;
import com.estudo_raul.java.infrastructure.exceptions.ResourceNotFoundException;
import com.estudo_raul.java.infrastructure.repository.EnderecoRepository;
import com.estudo_raul.java.infrastructure.repository.TelefoneRepository;
import com.estudo_raul.java.infrastructure.repository.UsuarioRepository;
import com.estudo_raul.java.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioConverter usuarioConverter;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final EnderecoRepository enderecoRepository;
    private final TelefoneRepository telefoneRepository;


    /**
     * Salva um novo usuário após verificar a existência do e-mail e codificar a senha.
     * * @param usuarioDTO O DTO contendo os dados do novo usuário.
     * @return O UsuarioDTO do usuário salvo.
     * @throws ConflictException se o e-mail já estiver cadastrado.
     */
    public UsuarioDTO salvaUsuario(UsuarioDTO usuarioDTO) {
        // Verifica se o email já existe. Se existir, lança ConflictException.
        emailExiste(usuarioDTO.getEmail());

        // Codifica a senha antes de converter para entidade
        usuarioDTO.setSenha(passwordEncoder.encode(usuarioDTO.getSenha()));

        // Converte DTO para Entidade
        Usuario usuario = usuarioConverter.paraUsuario(usuarioDTO);

        // Salva a Entidade e converte o retorno para DTO
        return usuarioConverter.paraUsuarioDTO(usuarioRepository.save(usuario));
    }

    /**
     * Verifica se o e-mail já existe no banco de dados e lança ConflictException se positivo.
     * * @param email O e-mail a ser verificado.
     * @throws ConflictException se o e-mail já estiver em uso.
     */
    public void emailExiste(String email){
        if (verificaEmailExistente(email)){
            // Lança a exceção diretamente, sem necessidade de try-catch aqui
            throw new ConflictException("Email já cadastrado: " + email);
        }
    }

    /**
     * Consulta o repositório para verificar a existência de um e-mail.
     * * @param email O e-mail a ser consultado.
     * @return True se o e-mail existe, false caso contrário.
     */
    public boolean verificaEmailExistente(String email){
        return usuarioRepository.existsByEmail(email);
    }

    public Usuario buscaUsuarioPorEmail(String email){
        return usuarioRepository.findByEmail(email).orElseThrow(()
                -> new ResourceNotFoundException("Email não encontrado " + email));
    }

    public void deletaUsuarioPorEmail(String email){
        usuarioRepository.deleteByEmail(email);
    }

    /**
     * Atualiza os dados principais do usuário com base no token JWT.
     * * @param token O token de autorização (incluindo "Bearer ").
     * @param dto O DTO com os dados que podem ser atualizados.
     * @return O UsuarioDTO com os dados atualizados.
     * @throws ResourceNotFoundException se o e-mail do token não for encontrado.
     */
    public UsuarioDTO atualizaDadosUsuario(String token, UsuarioDTO dto){
        // Buscando o email do usuário através do token (removendo "Bearer ")
        String email = jwtUtil.extrairEmailToken(token.substring(7));

        // Criptografia de senha: só codifica se a senha foi fornecida no DTO
        dto.setSenha(dto.getSenha() != null ? passwordEncoder.encode(dto.getSenha()) : null);

        // Buscamos os dados do usuário no banco de dados
        Usuario usuarioEntity = usuarioRepository.findByEmail(email).orElseThrow(()->
                new ResourceNotFoundException("Email não localizado: " + email));

        // Mescla os dados que recebemos na requisição DTO com os dados do banco de dados (Entidade)
        Usuario usuario = usuarioConverter.updateUsuario(dto, usuarioEntity);

        // Salva a entidade mesclada e converte o retorno para UsuarioDTO
        return usuarioConverter.paraUsuarioDTO(usuarioRepository.save(usuario));
    }

    /**
     * Atualiza os dados de um Endereço específico.
     * * @param idEndereco ID do endereço a ser atualizado.
     * @param enderecoDTO DTO com os novos dados.
     * @return EnderecoDTO com os dados atualizados.
     * @throws ResourceNotFoundException se o ID do endereço não for encontrado.
     */
    public EnderecoDTO atualizaEndereco(Long idEndereco, EnderecoDTO enderecoDTO){
        // Busca a entidade Endereco ou lança exceção
        Endereco entity = enderecoRepository.findById(idEndereco).orElseThrow(() ->
                new ResourceNotFoundException("Id de endereço não encontrado: " + idEndereco));

        // Mescla os dados do DTO na Entidade existente
        Endereco endereco = usuarioConverter.updateEndereco(enderecoDTO, entity);

        // Salva e converte o resultado para DTO
        return usuarioConverter.paraEnderecoDTO(enderecoRepository.save(endereco));
    }

    /**
     * Atualiza os dados de um Telefone específico.
     * * @param idTelefone ID do telefone a ser atualizado.
     * @param dto DTO com os novos dados.
     * @return TelefoneDTO com os dados atualizados.
     * @throws ResourceNotFoundException se o ID do telefone não for encontrado.
     */
    public TelefoneDTO atualizaTelefone (Long idTelefone, TelefoneDTO dto){
        // Busca a entidade Telefone ou lança exceção
        Telefone entity = telefoneRepository.findById(idTelefone).orElseThrow(() ->
                new ResourceNotFoundException("Id de telefone não econtrado: " + idTelefone));

        // Mescla os dados do DTO na Entidade existente
        Telefone telefone = usuarioConverter.updateTelefone(dto, entity);

        // Salva e converte o resultado para DTO
        return usuarioConverter.paraTelefoneDTO(telefoneRepository.save(telefone));
    }

    public EnderecoDTO cadastraEndereco(String token, EnderecoDTO dto){
        String email = jwtUtil.extrairEmailToken(token.substring(7));
        Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow(()->
                new ResourceNotFoundException("Email não localizado: " + email));

        Endereco endereco = usuarioConverter.paraEnderecoEntity(dto, usuario.getId());
        return usuarioConverter.paraEnderecoDTO(enderecoRepository.save(endereco));
    }

    public TelefoneDTO cadastraTelefone(String token, TelefoneDTO dto){
        String email = jwtUtil.extrairEmailToken(token.substring(7));
        Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow(() ->
                new ResourceNotFoundException("Email não localizado " + email));

        Telefone telefone = usuarioConverter.paraTelefoneEntity(dto, usuario.getId());
        return usuarioConverter.paraTelefoneDTO(telefoneRepository.save(telefone));
    }
}
