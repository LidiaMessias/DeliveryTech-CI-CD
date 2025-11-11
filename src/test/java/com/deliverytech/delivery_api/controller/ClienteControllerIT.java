package com.deliverytech.delivery_api.controller;

import com.deliverytech.delivery_api.dto.ClienteDTO;
import com.deliverytech.delivery_api.exception.GlobalExceptionHandler;
import com.deliverytech.delivery_api.repository.ClienteRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.deliverytech.delivery_api.DeliveryApiApplication;
import com.deliverytech.delivery_api.config.TestDataConfiguration;
import com.deliverytech.delivery_api.config.TestSecurityConfig;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

//@WebMvcTest(ClienteController.class)
@SpringBootTest(classes = {DeliveryApiApplication.class})
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
@Import({TestSecurityConfig.class, TestDataConfiguration.class, GlobalExceptionHandler.class})
@DisplayName("Testes de Integração do ClienteController")
public class ClienteControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ClienteRepository clienteRepository;

    @MockBean
    private AuthenticationManager authenticationManager;

    private final String API_PATH = "/api/clientes";

    @Test
    @DisplayName("Deve criar cliente com dados válidos")
    void should_CreateCliente_When_ValidData() throws Exception {

        ClienteDTO novoCliente = new ClienteDTO();
        novoCliente.setNome("Anderson da Silva");
        novoCliente.setEmail("anderson.silva@email.com");
        novoCliente.setTelefone("11888887777");
        novoCliente.setEndereco("Rua Universo Paralelo, 987 - Belas Artes - São Paulo / SP");

        /*ClienteResponseDTO retorno = new ClienteResponseDTO();
        retorno.setId(1L);
        retorno.setNome(novoCliente.getNome());
        retorno.setEmail(novoCliente.getEmail());
        retorno.setTelefone(novoCliente.getTelefone());
        
        when(clienteService.cadastrarCliente(any())).thenReturn(retorno); */

        mockMvc.perform(post(API_PATH)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(novoCliente)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.nome", is("Anderson da Silva")))
            .andExpect(jsonPath("$.email", is("anderson.silva@email.com")))
            .andExpect(jsonPath("$.id", notNullValue()));
    }

    @Test
    @DisplayName("Deve retornar erro 400 quando dados inválidos")
    void should_ReturnBadRequest_When_InvalidData() throws Exception {
        // Nome vazio, email inválido
        ClienteDTO clienteInvalido = new ClienteDTO();
        clienteInvalido.setNome("");
        clienteInvalido.setEmail("email-invalido");

        mockMvc.perform(post(API_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clienteInvalido)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errorCode", is("VALIDATION_ERROR")))
            .andExpect(jsonPath("$.details.nome", not(emptyOrNullString())))
            .andExpect(jsonPath("$.details.email", not(emptyOrNullString())));

    }

    @Test
    @DisplayName("Deve buscar cliente por ID existente")
    void should_ReturnCliente_When_IdExists() throws Exception {
        /*ClienteResponseDTO retorno = new ClienteResponseDTO();
        retorno.setId(1L);
        retorno.setNome("João");
        retorno.setEmail("joao@email.com");
        retorno.setTelefone("11999999999");

        when(clienteService.buscarClientePorId(1L)).thenReturn(retorno);*/
        Long id = clienteRepository.findAll().get(0).getId();

        mockMvc.perform(get(API_PATH + "/{id}", id))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(id.intValue())))
            .andExpect(jsonPath("$.nome", is("João Teste")))
            .andExpect(jsonPath("$.email", is("joao.teste@email.com")));
    }

    @Test
    @DisplayName("Deve retornar 404 quando cliente não existe")
    void should_ReturnNotFound_When_ClienteNotExists() throws Exception {
        /*when(clienteService.buscarClientePorId(999L))
            .thenThrow(new ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "Cliente não encontrado"));*/

        Long idInexistente = clienteRepository.findAll().stream()
                .mapToLong(cliente -> cliente.getId())
                .max()
                .orElse(0L) + 100;

        mockMvc.perform(get(API_PATH + "/{id}", idInexistente))
            .andDo(print())
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message", containsString("Cliente não encontrado com ID: " + idInexistente)))
            .andExpect(jsonPath("$.errorCode", is("ENTITY_NOT_FOUND")))
            .andExpect(jsonPath("$.status", is(404)))
            .andExpect(jsonPath("$.path", is("/api/clientes/" + idInexistente)));

    }

    
    @Test
    @DisplayName("Deve atualizar cliente existente")
    void should_UpdateCliente_When_ClienteExists() throws Exception {
        
        ClienteDTO updated = new ClienteDTO();
        updated.setNome("Nome Atualizado");
        updated.setEmail("email@atualizado.com");
        updated.setTelefone("11777777777");
        updated.setEndereco("Endereço de teste atualizado");

        /*when(clienteService.atualizarCliente(eq(1L), any())).thenReturn(updated);*/
        Long id = clienteRepository.findAll().get(0).getId();

        mockMvc.perform(put(API_PATH + "/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updated)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(id.intValue())))
            .andExpect(jsonPath("$.nome", is("Nome Atualizado")))
            .andExpect(jsonPath("$.email", is("email@atualizado.com")))
            .andExpect(jsonPath("$.endereco", is("Endereço de teste atualizado")))
            .andExpect(jsonPath("$.telefone", is("11777777777")));
    }
 
}

/* 
    @Test
    @DisplayName("Deve listar clientes com paginação")
    void should_ReturnPagedClientes_When_RequestedWithPagination() throws Exception {
        ClienteResponseDTO cli = new ClienteResponseDTO();
        cli.setId(1L);
        cli.setNome("Ana");
        cli.setEmail("ana@email.com");
        cli.setTelefone("11911111111");

        when(clienteService.listarClientes(org.mockito.ArgumentMatchers.any(Pageable.class)))
            .thenReturn(new PageImpl<>(List.of(cli), PageRequest.of(0, 10), 1));

        mockMvc.perform(get(API_PATH)
            .param("page", "0")
            .param("size", "10"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content", hasSize(greaterThan(0))))
            .andExpect(jsonPath("$.totalElements", greaterThan(0)))
            .andExpect(jsonPath("$.number", is(0)))
            .andExpect(jsonPath("$.size", is(10)));
    }

    @Test
    @DisplayName("Deve deletar cliente existente")
    void should_DeleteCliente_When_ClienteExists() throws Exception {
        doNothing().when(clienteService).deletarCliente(1L);
        // após deleção, buscar retorna 404
        when(clienteService.buscarClientePorId(1L))
            .thenThrow(new ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "Cliente não encontrado"));

        mockMvc.perform(delete(API_PATH + "/{id}", 1L))
            .andExpect(status().isNoContent());

        mockMvc.perform(get(API_PATH + "/{id}", 1L))
            .andExpect(status().isNotFound());
    }

*/
