package com.deliverytech.delivery_api.service;

import java.util.List;
//import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deliverytech.delivery_api.dto.ClienteDTO;
import com.deliverytech.delivery_api.dto.ClienteResponseDTO;
import com.deliverytech.delivery_api.exception.BusinessException;
//import com.deliverytech.delivery_api.mapper.ClienteMapper;
import com.deliverytech.delivery_api.model.Cliente;
import com.deliverytech.delivery_api.repository.ClienteRepository;
import com.deliverytech.delivery_api.exception.EntityNotFoundException;
//import jakarta.persistence.EntityNotFoundException;

@Service
@Transactional
public class ClienteServiceImpl implements ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ModelMapper modelMapper;

    // Cadastrar novo cliente
    @Override
    public ClienteResponseDTO cadastrarCliente(ClienteDTO clienteDTO) {
        // Validar email único
        if (clienteRepository.existsByEmail(clienteDTO.getEmail())) {
            throw new BusinessException("Email já cadastrado: " + clienteDTO.getEmail());
        }

        // Converter DTO para entidade
        Cliente cliente = modelMapper.map(clienteDTO, Cliente.class);
        cliente.setAtivo(true);

        Cliente novoCliente = clienteRepository.save(cliente);
        return modelMapper.map(novoCliente, ClienteResponseDTO.class);
    }

    // Buscar cliente por ID
    @Override
    @Transactional(readOnly = true)
    public ClienteResponseDTO buscarClientePorId(Long id) {
        Cliente cliente = clienteRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado com ID: " + id));
        
        return modelMapper.map(cliente, ClienteResponseDTO.class); 
    }

    // Buscar cliente por email
    @Override
    @Transactional(readOnly = true)
    public ClienteResponseDTO buscarClientePorEmail(String email) {
        Cliente cliente = clienteRepository.findByEmail(email)
            .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado com email: " + email));
        
        return modelMapper.map(cliente, ClienteResponseDTO.class);
    }

    // Atualizar dados do cliente
    @Override
    public ClienteResponseDTO atualizarCliente(Long id, ClienteDTO clienteDTO) {
        Cliente clienteExistente = clienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado com ID: " + id));

        // Verificar se email não está sendo usado por outro cliente
        if (!clienteExistente.getEmail().equals(clienteDTO.getEmail()) &&
                clienteRepository.existsByEmail(clienteDTO.getEmail())) {
            throw new BusinessException("Email já cadastrado: " +
                    clienteDTO.getEmail());
        }

        // Atualizar campos (exemplo: nome, telefone, endereço)
        clienteExistente.setNome(clienteDTO.getNome());
        clienteExistente.setEmail(clienteDTO.getEmail());
        clienteExistente.setTelefone(clienteDTO.getTelefone());
        clienteExistente.setEndereco(clienteDTO.getEndereco());
        
        Cliente clienteAtualizado = clienteRepository.save(clienteExistente);
        return modelMapper.map(clienteAtualizado, ClienteResponseDTO.class);
    }

    // Ativar e Desativar cliente
    @Override
    public ClienteResponseDTO ativarDesativarCliente(Long id) {
        Cliente clienteExistente = clienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado com ID: " + id));
        clienteExistente.setAtivo(!clienteExistente.isAtivo());

        Cliente clienteAtualizado = clienteRepository.save(clienteExistente);
        return modelMapper.map(clienteAtualizado, ClienteResponseDTO.class);
        
    }

    // Listar todos os clientes ativos
    @Override
    @Transactional(readOnly = true)
    public List<ClienteResponseDTO> listarClientesAtivos() {
        List<Cliente> clientesAtivos = clienteRepository.findByAtivoTrue();
        
        return clientesAtivos.stream()
            .map(cliente -> modelMapper.map(cliente, ClienteResponseDTO.class))
            .collect(Collectors.toList());
    }

}

    /* 
    // Listar todos os clientes
    @Transactional(readOnly = true)
    public List<ClienteDTO> listarTodos() {
        List<Cliente> clientes = clienteRepository.findAll();
        return clienteMapper.toClienteDTOList(clientes);
    }
 
    // Listar clientes com pedidos
    /*@Transactional(readOnly = true)
    public List<Cliente> listarClientesAtivosComPedidos() {
        return clienteRepository.findByAtivoTrueWithPedidos();
    }
    @Transactional(readOnly = true)
    public List<ClienteDTO> listarClientesAtivosComPedidos() {
        List<Cliente> clientes = clienteRepository.findByAtivoTrueWithPedidos();
        return clienteMapper.toClienteDTOList(clientes);
    }

    // Buscar clientes por nome
    @Transactional(readOnly = true)
    public List<Cliente> buscarPorNome(String nome) {
        return clienteRepository.findByNomeContainingIgnoreCase(nome);
    }

    // Validações de negócio
    private void validarDadosCliente(Cliente cliente) {
        if (cliente.getNome() == null || cliente.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome é obrigatório");
        }
        if (cliente.getEmail() == null || cliente.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email é obrigatório");
        }
        if (cliente.getTelefone() == null || cliente.getTelefone().trim().isEmpty()) {
            throw new IllegalArgumentException("Telefone é obrigatório");
        }
        if (cliente.getNome().length() < 2) {
            throw new IllegalArgumentException("Nome deve ter pelo menos 2 caracteres");
        }
    }
    */
