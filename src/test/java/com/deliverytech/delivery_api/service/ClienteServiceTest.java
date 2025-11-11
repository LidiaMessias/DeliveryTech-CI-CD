package com.deliverytech.delivery_api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import com.deliverytech.delivery_api.dto.ClienteDTO;
import com.deliverytech.delivery_api.dto.ClienteResponseDTO;
import com.deliverytech.delivery_api.exception.BusinessException;
import com.deliverytech.delivery_api.model.Cliente;
import com.deliverytech.delivery_api.repository.ClienteRepository;

import com.deliverytech.delivery_api.exception.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes do serviço de Cliente")
public class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ClienteServiceImpl clienteService;

    private ClienteDTO clienteDTO;
    private Cliente cliente;
    private ClienteResponseDTO clienteResponseDTO;

    @BeforeEach
    void setUp() {
        clienteDTO = new ClienteDTO();
        //cliente.setId(1L);
        clienteDTO.setNome("João Silva");
        clienteDTO.setEmail("joao@email.com");
        clienteDTO.setTelefone("11999999999");
        //clienteDTO.setCpf("12345678901");

        // Objeto de Mock do REPOSITORY (Entidade JPA)
        cliente = new Cliente(); 
        cliente.setId(1L);
        cliente.setNome("João Silva");
        cliente.setEmail("joao@email.com");
        cliente.setTelefone("11999999999");

        // Objeto de Mock do RESPONSE DTO
        clienteResponseDTO = new ClienteResponseDTO();
        clienteResponseDTO.setNome("João Silva");
        clienteResponseDTO.setEmail("joao@email.com");
    }

    @Test
    @DisplayName("Deve salvar cliente com dados válidos")
    void should_SaveCliente_When_ValidData() {
        // Given
        Cliente cliente = new Cliente();
        cliente.setNome(clienteDTO.getNome());
        cliente.setEmail(clienteDTO.getEmail());
        cliente.setTelefone(clienteDTO.getTelefone());

        when(modelMapper.map(any(ClienteDTO.class), eq(Cliente.class))).thenReturn(cliente);

        when(modelMapper.map(any(Cliente.class), eq(ClienteResponseDTO.class))).thenReturn(clienteResponseDTO);

        when(clienteRepository.existsByEmail(anyString())).thenReturn(false);
        //when(clienteRepository.existsByCpf(anyString())).thenReturn(false);
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);
        

        // When
        ClienteResponseDTO resultado = clienteService.cadastrarCliente(clienteDTO);

        // Then
        assertNotNull(resultado);
        assertEquals("João Silva", resultado.getNome());
        assertEquals("joao@email.com", resultado.getEmail());
        verify(clienteRepository).save(cliente);
        verify(clienteRepository).existsByEmail("joao@email.com");
        //verify(clienteRepository).existsByCpf("12345678901");
    }

    @Test
    @DisplayName("Deve lançar exceção quando email já existe")
    void should_ThrowException_When_EmailAlreadyExists() {
        // Given
        when(clienteRepository.existsByEmail("joao@email.com")).thenReturn(true);
        // When & Then
        assertThrows(BusinessException.class, () -> {
            clienteService.cadastrarCliente(clienteDTO);
        });
        
        verify(clienteRepository).existsByEmail("joao@email.com");
        verify(clienteRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve buscar cliente por ID existente")
    void should_ReturnCliente_When_IdExists() {
        // Given
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        
        // When
        ClienteResponseDTO clienteResponse = new ClienteResponseDTO(); 
        clienteResponse.setNome("João Silva");
        clienteResponse.setEmail("joao@email.com");

        when(modelMapper.map(any(Cliente.class), eq(ClienteResponseDTO.class)))
            .thenReturn(clienteResponse);

        ClienteResponseDTO resultado = clienteService.buscarClientePorId(1L);

        // Then
        assertNotNull(resultado);
        assertEquals("João Silva", resultado.getNome());
        verify(modelMapper).map(any(Cliente.class), eq(ClienteResponseDTO.class));
        verify(clienteRepository).findById(1L);
    }

    @Test
    @DisplayName("Deve lançar EntityNotFoundException quando ID não existe")
    void should_ThrowException_When_IdNotExists() {
        // Given
        when(clienteRepository.findById(999L)).thenReturn(Optional.empty());
        
        // When
        assertThrows(EntityNotFoundException.class, () -> { 
            clienteService.buscarClientePorId(999L);
        });
        
        // Then
        verify(clienteRepository).findById(999L);
    }

    @Test
    @DisplayName("Deve atualizar cliente existente")
    void should_UpdateCliente_When_ClienteExists() {
        // Given
        ClienteDTO clienteAtualizado = new ClienteDTO();
        clienteAtualizado.setNome("João Santos");
        clienteAtualizado.setEmail("joao.santos@email.com");

        Cliente clienteSalvo = new Cliente();
        clienteSalvo.setId(1L);
        clienteSalvo.setNome("João Santos");
        clienteSalvo.setEmail("joao.santos@email.com");

        ClienteResponseDTO clienteResponseSaida = new ClienteResponseDTO();
        clienteResponseSaida.setNome("João Santos");
        clienteResponseSaida.setEmail("joao.santos@email.com");

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(clienteRepository.existsByEmail("joao.santos@email.com")).thenReturn(false);
        when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteSalvo);

        when(modelMapper.map(any(Cliente.class), eq(ClienteResponseDTO.class)))
            .thenReturn(clienteResponseSaida);
        
        // When
        ClienteResponseDTO resultado = clienteService.atualizarCliente(1L, clienteAtualizado);
        // Then
        assertNotNull(resultado);
        assertEquals("João Santos", resultado.getNome());
        verify(clienteRepository).save(any(Cliente.class));
        verify(modelMapper).map(any(Cliente.class), eq(ClienteResponseDTO.class));
    }

}  
    /*
    @Test
    @DisplayName("Deve lançar exceção quando CPF já existe")
    void should_ThrowException_When_CpfAlreadyExists() {
        // Given
        when(clienteRepository.existsByEmail(anyString())).thenReturn(false);
        when(clienteRepository.existsByCpf(anyString())).thenReturn(true);
        
        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> clienteService.cadastrarCliente(clienteDTO)
        );
        assertEquals("CPF já cadastrado", exception.getMessage());
        verify(clienteRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve listar clientes com paginação")
    void should_ReturnPagedClientes_When_RequestedWithPagination() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        Page<Cliente> page = new PageImpl<>(Arrays.asList(cliente));
        when(clienteRepository.findAll(pageable)).thenReturn(page);
        
        // When
        Page<Cliente> resultado = clienteService.listarTodos(pageable);
        
        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.getTotalElements());
        assertEquals("João Silva", resultado.getContent().get(0).getNome());
        verify(clienteRepository).findAll(pageable);
    }

    */


