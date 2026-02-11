package com.barbearia.service;

import com.barbearia.dto.DTOs.ClienteRequest;
import com.barbearia.exception.BusinessException;
import com.barbearia.exception.ResourceNotFoundException;
import com.barbearia.model.Cliente;
import com.barbearia.model.Usuario;
import com.barbearia.repository.ClienteRepository;
import com.barbearia.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public ClienteService(ClienteRepository clienteRepository,
                          UsuarioRepository usuarioRepository,
                          PasswordEncoder passwordEncoder) {
        this.clienteRepository = clienteRepository;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Cliente criar(ClienteRequest request) {
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("Email já cadastrado: " + request.getEmail());
        }

        Usuario usuario = new Usuario();
        usuario.setNome(request.getNome());
        usuario.setEmail(request.getEmail());
        usuario.setSenha(passwordEncoder.encode("123456")); // depois você troca pra request.getSenha()
        usuario.setRole("CLIENTE");
        usuarioRepository.save(usuario);

        Cliente cliente = new Cliente();
        cliente.setUsuario(usuario);
        cliente.setNome(request.getNome());
        cliente.setEmail(request.getEmail());
        cliente.setTelefone(request.getTelefone());

        return clienteRepository.save(cliente);
    }

    @Transactional(readOnly = true)
    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Cliente buscarPorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado: " + id));
    }

    public Cliente atualizar(Long id, ClienteRequest request) {
        Cliente cliente = buscarPorId(id);

        if (request.getEmail() != null
                && !cliente.getEmail().equals(request.getEmail())
                && clienteRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("Email já cadastrado por outro cliente.");
        }

        if (request.getNome() != null) cliente.setNome(request.getNome());
        if (request.getEmail() != null) cliente.setEmail(request.getEmail());
        if (request.getTelefone() != null) cliente.setTelefone(request.getTelefone());

        return clienteRepository.save(cliente);
    }

    public void deletar(Long id) {
        Cliente cliente = buscarPorId(id);

        // Se você tiver campo "ativo" no Usuario, você pode desativar aqui.
        if (cliente.getUsuario() != null) {
            usuarioRepository.save(cliente.getUsuario());
        }

        // Se você quiser realmente remover do banco, use delete(cliente).
        // Aqui está "soft delete" simplificado:
        clienteRepository.save(cliente);
    }
}