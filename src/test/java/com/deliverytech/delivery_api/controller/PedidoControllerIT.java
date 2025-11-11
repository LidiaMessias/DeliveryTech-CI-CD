package com.deliverytech.delivery_api.controller;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.deliverytech.delivery_api.config.TestDataConfiguration;
import com.deliverytech.delivery_api.config.TestSecurityConfig;
import com.deliverytech.delivery_api.dto.ItemPedidoDTO;
import com.deliverytech.delivery_api.dto.PedidoDTO;
import com.deliverytech.delivery_api.exception.GlobalExceptionHandler;
import com.deliverytech.delivery_api.model.Cliente;
import com.deliverytech.delivery_api.model.Produto;
import com.deliverytech.delivery_api.model.Restaurante;
import com.deliverytech.delivery_api.repository.ClienteRepository;
import com.deliverytech.delivery_api.repository.RestauranteRepository;
import com.deliverytech.delivery_api.repository.ProdutoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import({TestSecurityConfig.class, TestDataConfiguration.class, GlobalExceptionHandler.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@DisplayName("Testes de Integração do PedidoController")
class PedidoControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private RestauranteRepository restauranteRepository;

    @MockBean
    private AuthenticationManager authenticationManager;

    @Test
    @DisplayName("Deve criar pedido com dados válidos")
    void should_CreatePedido_When_ValidData() throws Exception {
        // Given
        //Cliente cliente = clienteRepository.findAll().get(0);
        //Produto produto = produtoRepository.findAll().get(0);

        Cliente cliente = clienteRepository.findAll().stream().filter(Cliente::isAtivo).findFirst().orElseThrow();
        Restaurante restaurante = restauranteRepository.findAll().stream().filter(Restaurante::isAtivo).findFirst().orElseThrow();
        Produto produto = produtoRepository.findAll().stream()
        .filter(p -> p.isDisponivel() && p.getRestaurante().getId().equals(restaurante.getId()))
        .findFirst().orElseThrow();


        ItemPedidoDTO itemDTO = new ItemPedidoDTO();
        itemDTO.setProdutoId(produto.getId());
        itemDTO.setQuantidade(2);

        PedidoDTO pedidoDTO = new PedidoDTO();
        pedidoDTO.setClienteId(cliente.getId());
        pedidoDTO.setRestauranteId(restaurante.getId());
        pedidoDTO.setEnderecoEntrega("Rua das Flores, 123 - Centro");
        pedidoDTO.setCep("01234-567");
        pedidoDTO.setFormaPagamento("PIX");
        pedidoDTO.setItens(List.of(itemDTO));
        pedidoDTO.setObservacoes("Entregar na portaria");


        // When & Then
        mockMvc.perform(post("/api/pedidos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pedidoDTO)))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.data.id", notNullValue()))
            .andExpect(jsonPath("$.data.status", is("PENDENTE")))
            .andExpect(jsonPath("$.data.valorTotal", is(59.80)))
            .andExpect(jsonPath("$.data.itens", hasSize(1)));
    }

    @Test
    @DisplayName("Deve retornar erro quando produto não existe")
    void should_ReturnError_When_ProductNotExists() throws Exception {
        // Given
        Cliente cliente = clienteRepository.findAll().stream().filter(Cliente::isAtivo).findFirst().orElseThrow();
        Restaurante restaurante = restauranteRepository.findAll().stream().filter(Restaurante::isAtivo).findFirst().orElseThrow();

        ItemPedidoDTO itemDTO = new ItemPedidoDTO();

        itemDTO.setProdutoId(999L); // Produto inexistente
        itemDTO.setQuantidade(1);

        PedidoDTO pedidoDTO = new PedidoDTO();
        pedidoDTO.setClienteId(cliente.getId());
        pedidoDTO.setRestauranteId(restaurante.getId());
        pedidoDTO.setEnderecoEntrega("Rua das Flores, 123 - Centro");
        pedidoDTO.setCep("01234-567");
        pedidoDTO.setFormaPagamento("PIX");
        pedidoDTO.setItens(List.of(itemDTO));
        pedidoDTO.setObservacoes("Teste de erro com produto inexistente");

        // When & Then
        mockMvc.perform(post("/api/pedidos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pedidoDTO)))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message", containsString("Produto não encontrado")));
    }

    @Test
    @DisplayName("Deve buscar histórico de pedidos do cliente")
    void should_ReturnClientePedidos_When_ClienteExists() throws Exception {
        // Given
        Cliente cliente = clienteRepository.findAll().stream().filter(Cliente::isAtivo).findFirst().orElseThrow();
        Restaurante restaurante = restauranteRepository.findAll().stream().filter(Restaurante::isAtivo).findFirst().orElseThrow();
        Produto produto = produtoRepository.findAll().stream()
        .filter(p -> p.isDisponivel() && p.getRestaurante().getId().equals(restaurante.getId()))
        .findFirst().orElseThrow();

        // Criar um pedido primeiro
        ItemPedidoDTO itemDTO = new ItemPedidoDTO();
        itemDTO.setProdutoId(produto.getId());
        itemDTO.setQuantidade(1);

        PedidoDTO pedidoDTO = new PedidoDTO();
        pedidoDTO.setClienteId(cliente.getId());
        pedidoDTO.setRestauranteId(restaurante.getId());
        pedidoDTO.setEnderecoEntrega("Rua das Flores, 123 - Centro");
        pedidoDTO.setCep("01234-567");
        pedidoDTO.setFormaPagamento("PIX");
        pedidoDTO.setItens(List.of(itemDTO));
        pedidoDTO.setObservacoes("Pedido para histórico");


        mockMvc.perform(post("/api/pedidos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pedidoDTO)))
            .andExpect(status().isCreated());

        // When & Then
        mockMvc.perform(get("/api/pedidos/cliente/{id}", cliente.getId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success", is(true)))
            .andExpect(jsonPath("$.data", hasSize(greaterThan(0))));
           
    }

    @Test
    @DisplayName("Deve atualizar status do pedido")
    void should_UpdatePedidoStatus_When_PedidoExists() throws Exception {
        // Given
        Cliente cliente = clienteRepository.findAll().stream().filter(Cliente::isAtivo).findFirst().orElseThrow();
        Restaurante restaurante = restauranteRepository.findAll().stream().filter(Restaurante::isAtivo).findFirst().orElseThrow();
        Produto produto = produtoRepository.findAll().stream()
            .filter(p -> p.isDisponivel() && p.getRestaurante().getId().equals(restaurante.getId()))
            .findFirst().orElseThrow();
    
        // Criar pedido primeiro
        ItemPedidoDTO itemDTO = new ItemPedidoDTO();
        itemDTO.setProdutoId(produto.getId());
        itemDTO.setQuantidade(1);

        PedidoDTO pedidoDTO = new PedidoDTO();
        pedidoDTO.setClienteId(cliente.getId());
        pedidoDTO.setRestauranteId(restaurante.getId());
        pedidoDTO.setEnderecoEntrega("Rua das Flores, 123 - Centro");
        pedidoDTO.setCep("01234-567");
        pedidoDTO.setFormaPagamento("PIX");
        pedidoDTO.setItens(List.of(itemDTO));
        pedidoDTO.setObservacoes("Pedido para teste de atualização de status");


        String response = mockMvc.perform(post("/api/pedidos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pedidoDTO)))
            .andExpect(status().isCreated())  
            .andReturn().getResponse().getContentAsString();

        Long pedidoId = objectMapper.readTree(response).get("data").get("id").asLong();

        // When & Then
        mockMvc.perform(patch("/api/pedidos/{id}/status", pedidoId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("\"CONFIRMADO\""))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.status", is("CONFIRMADO")));
    }

    @Test
    @DisplayName("Deve validar cálculo correto do valor total")
    void should_CalculateCorrectTotal_When_MultipleItems() throws Exception {
        // Given
        Cliente cliente = clienteRepository.findAll().stream().filter(Cliente::isAtivo).findFirst().orElseThrow();
        Restaurante restaurante = restauranteRepository.findAll().stream().filter(Restaurante::isAtivo).findFirst().orElseThrow();
        Produto produto = produtoRepository.findAll().stream()
            .filter(p -> p.isDisponivel() && p.getRestaurante().getId().equals(restaurante.getId()))
            .findFirst().orElseThrow();


        ItemPedidoDTO item1 = new ItemPedidoDTO();
        item1.setProdutoId(produto.getId());
        item1.setQuantidade(2); // 2 x 29.90 = 59.80

        ItemPedidoDTO item2 = new ItemPedidoDTO();
        item2.setProdutoId(produto.getId());
        item2.setQuantidade(1); // 1 x 29.90 = 29.90

        PedidoDTO pedidoDTO = new PedidoDTO();
        pedidoDTO.setClienteId(cliente.getId());
        pedidoDTO.setRestauranteId(restaurante.getId());
        pedidoDTO.setEnderecoEntrega("Rua das Flores, 123 - Centro");
        pedidoDTO.setCep("01234-567");
        pedidoDTO.setFormaPagamento("PIX");
        pedidoDTO.setItens(List.of(item1, item2));
        pedidoDTO.setObservacoes("Pedido com múltiplos itens");

        // When & Then
        mockMvc.perform(post("/api/pedidos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pedidoDTO)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.data.valorTotal", is(89.70))); // Total: 89.70
    }
}

/*
    @Test
    @DisplayName("Deve retornar erro quando estoque insuficiente")
    void should_ReturnError_When_InsufficientStock() throws Exception {
    // Given
        Cliente cliente = clienteRepository.findAll().get(0);
        Produto produto = produtoRepository.findAll().get(0);

        ItemPedidoDTO itemDTO = new ItemPedidoDTO();
        itemDTO.setProdutoId(produto.getId());
        itemDTO.setQuantidade(100); // Quantidade maior que estoque

        PedidoDTO pedidoDTO = new PedidoDTO();
        pedidoDTO.setClienteId(cliente.getId());
        pedidoDTO.setItens(Arrays.asList(itemDTO));
        // When & Then
        mockMvc.perform(post("/api/pedidos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(pedidoDTO)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message", containsString("Estoque insuficiente")));
    }
*/
