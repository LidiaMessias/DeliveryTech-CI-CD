package com.deliverytech.delivery_api.config;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.event.annotation.BeforeTestMethod;

import com.deliverytech.delivery_api.model.Cliente;
import com.deliverytech.delivery_api.model.Produto;
import com.deliverytech.delivery_api.model.Restaurante;
import com.deliverytech.delivery_api.repository.ClienteRepository;
import com.deliverytech.delivery_api.repository.ProdutoRepository;
import com.deliverytech.delivery_api.repository.RestauranteRepository;

@TestConfiguration
public class TestDataConfiguration {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private RestauranteRepository restauranteRepository;
    
    @BeforeTestMethod
    public void setupTestData() {
    
        // Limpar dados existentes
        clienteRepository.deleteAll();
        produtoRepository.deleteAll();
        restauranteRepository.deleteAll();

        // Criar dados de teste
        Cliente cliente = new Cliente();
        cliente.setNome("João Teste");
        cliente.setEmail("joao.teste@email.com");
        cliente.setTelefone("11999999999");
        clienteRepository.save(cliente);

        Restaurante restaurante = new Restaurante();
        restaurante.setNome("Restaurante Teste");
        restaurante.setEndereco("Rua dos Testes, 123");
        restaurante.setCategoria("JAPONESA");
        restaurante.setTelefone("11987459988");
        restaurante = restauranteRepository.save(restaurante);


        Produto produto = new Produto();
        produto.setNome("Pizza Teste");
        produto.setDescricao("Pizza para testes");
        produto.setPreco(BigDecimal.valueOf(29.90));
        produto.setCategoria("Pizza");
        produto.setDisponivel(true);
        produto.setRestaurante(restaurante);
        produtoRepository.save(produto);
    }
}
